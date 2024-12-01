package com.example.yknetworklibrary.util;

import android.util.Log;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class YkNetThreadPool {
    private static final String TAG = "DNS.ThreadPool";
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(4,new NamedThreadFactory("DnsThreadPool"));

    private static YkNetThreadPool sThreadPool = new YkNetThreadPool();

    private YkNetThreadPool() {
        startMonitorThread();
    }

    public static YkNetThreadPool getInstance() {
        return sThreadPool;
    }

    public void execute(Runnable r) {
        THREAD_POOL.execute(r);
//    printThreadToolStatus();
    }

    public <T> Future<T> submit(Callable<T> r) {
        return THREAD_POOL.submit(r);
    }

    public void printThreadToolStatus() {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)THREAD_POOL;
        int queueSize = threadPoolExecutor.getQueue().size();
        int activeCount = threadPoolExecutor.getActiveCount();
        long completeTaskCount = threadPoolExecutor.getCompletedTaskCount();
        long taskCount = threadPoolExecutor.getTaskCount();

        if (activeCount >= 4) {
            Log.d(TAG, String.format("-2-总线程数:%d, 当前排队线程数:%d, 当前活动线程数:%d, 执行完成线程数:%d",taskCount, queueSize, activeCount, completeTaskCount));
            Map<Thread, StackTraceElement[]> stackElementsMap = Thread.getAllStackTraces();
            for (Map.Entry<Thread, StackTraceElement[]> entry : stackElementsMap.entrySet()) {
                Thread thread = entry.getKey();
                String threadName = entry.getKey().getName();
                StackTraceElement[] elements = entry.getValue();
                if(threadName.startsWith("DnsThreadPool")) {  //pool-2-thread
                    printElement(20, elements, thread);
                }
            }
        }
    }
    private static void printElement(int elementMax, StackTraceElement[] stackElements, Thread thread) {
        if (stackElements == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(thread.getName()).append(",").append(thread.isAlive()).append(":\n");
        int num = 0;
        for (StackTraceElement element : stackElements) {
            if (elementMax != 0 && num >= elementMax) {
                return;
            }
            num++;

            String msg = "%s(%s:%s)-%s()\n";
            msg = String.format(msg, element.getClassName(), element.getFileName(), element.getLineNumber(), element.getMethodName());
            stringBuilder.append(msg);
        }
        Log.d(TAG, stringBuilder.toString());
    }

    private void startMonitorThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) THREAD_POOL;
                        int activeCount = threadPoolExecutor.getActiveCount();
                        if (activeCount >= 4) {
                            printThreadToolStatus();
                        }
                        Thread.sleep(5000);
                        Log.d(TAG, "MonitorThread");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static class NamedThreadFactory implements ThreadFactory {
        //        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private  String namePrefix;
        private ThreadGroup group;

        public NamedThreadFactory( String name ) {
            // + "-" + poolNumber.getAndIncrement()
            this.namePrefix = name + "-thread-";
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            if (null == group) {    // 有可能空吗？
                group = new ThreadGroup("DnsThreadPoolGroup");
            }
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            return t;
        }
    }
}
