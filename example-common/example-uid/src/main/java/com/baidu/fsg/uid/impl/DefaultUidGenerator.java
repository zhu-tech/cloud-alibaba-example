/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.fsg.uid.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baidu.fsg.uid.BitsAllocator;
import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.exception.UidGenerateException;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Represents an implementation of {@link UidGenerator}
 * <p>
 * The unique id has 64bits (long), default allocated as blow:<br>
 * <li>sign: The highest bit is 0
 * <li>delta seconds: The next 28 bits, represents delta seconds since a customer epoch(2016-05-20 00:00:00.000).
 * Supports about 8.7 years until to 2024-11-20 21:24:16
 * <li>worker id: The next 22 bits, represents the worker's id which assigns based on database, max id is about 420W
 * <li>sequence: The next 13 bits, represents a sequence within the same second, max for 8192/s<br><br>
 * <p>
 * The {@link DefaultUidGenerator#parseUid(long)} is a tool method to parse the bits
 *
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          28bits              22bits         13bits
 * }</pre>
 * <p>
 * You can also specified the bits by Spring property setting.
 * <li>timeBits: default as 28
 * <li>workerBits: default as 22
 * <li>seqBits: default as 13
 * <li>epochStr: Epoch date string format 'yyyy-MM-dd'. Default as '2016-05-20'<p>
 *
 * <b>Note that:</b> The total bits must be 64 -1
 */
@Slf4j
public class DefaultUidGenerator implements UidGenerator, InitializingBean {

    long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1600012800000L);
    /**
     * Stable fields after spring bean initializing
     */
    BitsAllocator bitsAllocator;
    long workerId;
    /**
     * Bits allocate
     */
    private int timeBits = 31;
    private int workerBits = 20;
    private int seqBits = 12;
    /**
     * Customer epoch, unit as second. For example 2016-05-20 (ms: 1463673600000)
     */
    private String epochStr = "2022-12-01";
    /**
     * Volatile fields caused by nextId()
     */
    private long sequence = 0L;
    private long lastSecond = -1L;

    /**
     * Spring property
     */
    private WorkerIdAssigner workerIdAssigner;

    @Override
    public void afterPropertiesSet() throws Exception {
        // initialize bits allocator
        bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);

        // initialize worker id
        workerId = workerIdAssigner.assignWorkerId();
        if (workerId > bitsAllocator.getMaxWorkerId()) {
            throw new RuntimeException("Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
        }

        log.info("Initialized bits(1, {}, {}, {}) for workerID:{}", timeBits, workerBits, seqBits, workerId);
    }

    @Override
    public long getUid() throws UidGenerateException {
        try {
            // 年份放前两位，便于分库分表key
            return Long.parseLong(String.format("%s%s", LocalDate.now().getYear() % 100, nextId()));
        } catch (Exception e) {
            log.error("Generate unique id exception. ", e);
            throw new UidGenerateException(e);
        }
    }

    @Override
    public String parseUid(long uid) {
        uid = Long.parseLong(String.valueOf(uid).substring(2));
        long totalBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long workerIdBits = bitsAllocator.getWorkerIdBits();
        long sequenceBits = bitsAllocator.getSequenceBits();

        // parse UID
        final long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        final long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
        final long deltaSeconds = uid >>> (workerIdBits + sequenceBits);

        Date thatTime = new Date(TimeUnit.SECONDS.toMillis(epochSeconds + deltaSeconds));
        String thatTimeStr = DateUtil.format(thatTime, DatePattern.NORM_DATETIME_PATTERN);

        // format as string //delta seconds    | worker node id | sequence
        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}",
                uid, thatTimeStr, workerId, sequence);
    }

    /**
     * Get UID
     *
     * @return UID
     * @throws UidGenerateException in the case: Clock moved backwards; Exceeds the max timestamp
     */
    protected synchronized long nextId() {
        long currentSecond = getCurrentSecond();

        // Clock moved backwards, refuse to generate uid
        // 时钟回拨问题待解决
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            throw new UidGenerateException("Clock moved backwards. Refusing for %d seconds", refusedSeconds);
        }

        // At the same second, increase sequence
        //同一秒内的，本次发号请求不是本秒的第一次, sequence 加一
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            // Exceed the max sequence, we wait the next second to generate uid
            // 号发完了，等到下一秒
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }

            // At the different second, sequence restart from zero
        } else {
            //新的一秒，重新开始发号
            sequence = 0L;
        }

        lastSecond = currentSecond;

        // Allocate bits for UID
        return bitsAllocator.allocate(currentSecond - epochSeconds, workerId, sequence);
    }

    /**
     * Get next millisecond
     */
    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }

    /**
     * Get current second
     */
    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochSeconds > bitsAllocator.getMaxDeltaSeconds()) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }
        return currentSecond;
    }

    /**
     * Setters for spring property
     */
    public void setWorkerIdAssigner(final WorkerIdAssigner workerIdAssigner) {
        this.workerIdAssigner = workerIdAssigner;
    }

    public void setTimeBits(final int timeBits) {
        if (timeBits > 0) {
            this.timeBits = timeBits;
        }
    }

    public void setWorkerBits(int workerBits) {
        if (workerBits > 0) {
            this.workerBits = workerBits;
        }
    }

    public void setSeqBits(int seqBits) {
        if (seqBits > 0) {
            this.seqBits = seqBits;
        }
    }

    public void setEpochStr(final String epochStr) {
        if (StrUtil.isNotBlank(epochStr)) {
            this.epochStr = epochStr;
            this.epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtil.parse(this.epochStr,
                    DatePattern.NORM_DATE_PATTERN).getTime());
        }
    }
}
