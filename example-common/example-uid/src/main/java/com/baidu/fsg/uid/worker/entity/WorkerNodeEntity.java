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
package com.baidu.fsg.uid.worker.entity;

import com.baidu.fsg.uid.worker.WorkerNodeType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity for M_WORKER_NODE
 */
@Getter
@Setter
public class WorkerNodeEntity {

    /**
     * Entity unique id (table unique)
     */
    private long id;

    /**
     * type是CONTAINER（容器）: 主机名, ACTUAL（实际机器） : IP.
     */
    private String hostName;

    /**
     * type是CONTAINER（容器）： Port, ACTUAL（实际机器） : 时间戳 + 随机数(0-10000)
     */
    private String port;

    /**
     * type of {@link WorkerNodeType}
     */
    private int type;

    /**
     * Worker launch date, default now
     */
    private LocalDate launchDate = LocalDate.now();

    /**
     * Created time
     */
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * Last modified
     */
    private LocalDateTime updateTime = LocalDateTime.now();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
