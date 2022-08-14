package com.tiiaan.rpc.common.factory;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class ThreadPoolFactory {

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    /**
     * 通过 threadNamePrefix 区分线程池
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    private static Map<String, ExecutorService> THREAD_POOLS_MAP = new ConcurrentHashMap<>();

    private ThreadPoolFactory() {
    }

    public static ExecutorService createThreadPoolIfAbsent(String threadNamePrefix) {
        return createThreadPoolIfAbsent(threadNamePrefix, false);
    }

    public static ExecutorService createThreadPoolIfAbsent(String threadNamePrefix, Boolean daemon) {
        ExecutorService pool = THREAD_POOLS_MAP.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadNamePrefix, daemon));
        if (pool.isShutdown() || pool.isTerminated()) {
            THREAD_POOLS_MAP.remove(threadNamePrefix);
            pool = createThreadPool(threadNamePrefix, daemon);
            THREAD_POOLS_MAP.put(threadNamePrefix, pool);
        }
        return pool;
    }

    public static void shutDownAllThreadPools() {
        log.info("正在关闭线程池");
        THREAD_POOLS_MAP.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            log.info("ThreadPool [{}] [{}]", entry.getKey(), executorService.isTerminated());
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("关闭线程池失败", e);
                executorService.shutdownNow();
            }
        });
    }


    private static ExecutorService createThreadPool(String threadNamePrefix, Boolean daemon) {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MINUTES,
                workQueue,
                threadFactory);
    }


    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(daemon)
                        .build();
            } else {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .build();
            }
        }
        return Executors.defaultThreadFactory();
    }


    public static void monitorThreadPoolStatus(ThreadPoolExecutor threadPool) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,
                createThreadFactory("monitor-thread-pool-status", false));
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            log.info("ThreadPoolSize=[{}], Active=[{}], TasksCompleted=[{}], TasksInQueue=[{}]",
                    threadPool.getPoolSize(),
                    threadPool.getActiveCount(),
                    threadPool.getCompletedTaskCount(),
                    threadPool.getQueue().size());
        }, 0, 1, TimeUnit.SECONDS);
    }

}
