package com.mono.component.common.utils;

import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Asynchronous Executor Utils
 *
 * @author Mono 2022/9/2 09:54 gralves@163.com
 */
@SuppressWarnings("NullableProblems")
public class AsyncExecutorUtils {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10, new CustomizableThreadFactory("AsyncExecutorUtils-"));

    private static final ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);

    /**
     * submit task
     *
     * @param work          work
     * @param consumer      consumer
     * @param errorConsumer errorConsumer
     * @return futureTask
     * @author Graves 2022/8/29 17:09 gralves@163.com
     */
    public static <T> ListenableFuture<T> submit(
            Callable<T> work,
            Consumer<T> consumer,
            Consumer<Throwable> errorConsumer
    ) {
        ListenableFuture<T> listenableFuture = listeningExecutorService.submit(work);
        Futures.addCallback(
                listenableFuture,
                new FutureCallback<T>() {
                    @Override
                    public void onSuccess(@Nullable T s) {
                        if (null != consumer) {
                            consumer.accept(s);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        if (null != errorConsumer) {
                            errorConsumer.accept(throwable);
                        }
                    }
                },
                listeningExecutorService);
        return listenableFuture;
    }

    /**
     * submit task
     *
     * @param work work
     * @return futureTask
     * @author Graves 2022/8/29 17:09 gralves@163.com
     */
    public static <T> ListenableFuture<T> submit(Callable<T> work) {
        return submit(work, null, null);
    }
}
