package com.neyogoo.example.common.core.util;

import com.alibaba.ttl.TtlRunnable;
import com.neyogoo.example.common.core.exception.BizException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class ThreadUtils {

    public static CompletableFuture<Void> ttlRunAsync(Runnable runnable) {
        return CompletableFuture.runAsync(TtlRunnable.get(runnable));
    }

    public static CompletableFuture<Void> ttlRunAsync(Runnable runnable, Executor executor) {
        return CompletableFuture.runAsync(TtlRunnable.get(runnable), executor);
    }

    /**
     * 运行任务并将异常放入 vector
     */
    public static CompletableFuture<Void> ttlRunAsync(Runnable runnable, Executor executor,
                                                      Collection<BizException> error) {
        return ttlRunAsync(
                () -> {
                    try {
                        runnable.run();
                    } catch (BizException e) {
                        error.add(e);
                    }
                },
                executor
        );
    }

    /**
     * 运行所有任务
     */
    public static <T> void runAllFuture(List<CompletableFuture<T>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * 合并执行结果
     */
    public static <T> List<T> getFutureResult(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allCompletableFuture = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[futures.size()])
        );
        return allCompletableFuture.thenApply(
                e -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList())
        ).join();
    }

    private ThreadUtils() {

    }
}
