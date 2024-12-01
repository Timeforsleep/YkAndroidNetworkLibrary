package com.example.yknetworklibrary;

import com.example.yknetworklibrary.util.YkNetThreadPool;

import java.io.IOException;
import java.util.PriorityQueue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class YkRequestQueue {

    private boolean isRunning = true; // 用于控制线程的运行
    private final PriorityQueue<YkRequest> queue = new PriorityQueue<>();

    public void addRequest(YkRequest priorityRequest) {
        // 将请求添加到队列中
        queue.add(priorityRequest);
    }

    // 判断是否有待执行的请求
    public boolean hasRequests() {
        return !queue.isEmpty();
    }

    // 启动一个线程来轮询并执行请求
    public void startPolling() {
        YkNetThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        if (hasRequests()) {
                            // 获取队列中优先级最高的请求
                            YkRequest nextRequest;
                            synchronized (queue) {
                                nextRequest = queue.poll();  // 获取并移除队列头部的元素
                            }

                            if (nextRequest != null) {
                                enqueue(nextRequest); // 发起请求
                            }
                        } else {
                            // 队列为空时，稍作休息，避免 CPU 占用过高
                            Thread.sleep(100); // 每 100 毫秒检查一次
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    // 停止轮询线程
    public void stopPolling() {
        isRunning = false;
    }

    // 发起请求的方法
    public void enqueue(YkRequest request) {
        Call call = YkNetworkManager.getInstance().getOkHttpManager().getClient().newCall(request.getmOkHttpRequest());

        call.enqueue(new Callback() {  // 异步请求
            @Override
            public void onFailure(Call call, IOException e) {
                if (request.getmCallback() != null) {
                    request.getmCallback().onFailure(e);  // 请求失败时调用
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (request.getmCallback() != null) {
                        request.getmCallback().onSuccess(response);  // 请求成功时调用
                    }
                } else {
                    if (request.getmCallback() != null) {
                        request.getmCallback().onError(response.code(), response.message());  // 请求失败时调用
                    }
                }
            }
        });
    }


}
