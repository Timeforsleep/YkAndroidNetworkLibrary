package com.example.yknetworklibrary;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class YkBaseServant {


    // 仅内部使用的拦截器
    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            // 从请求中获取 APMRecord
            Request request = chain.request();
            APMRecord apmRecord = request.tag(APMRecord.class); // 获取 APMRecord

            if (apmRecord != null) {
                long connectStartTime = System.nanoTime();
                apmRecord.setConnectStartTime(connectStartTime);  // 设置连接开始时间

                Response response = null;
                try {
                    response = chain.proceed(request);  // 执行请求
                } finally {
                    long connectEndTime = System.nanoTime();
                    apmRecord.setConnectEndTime(connectEndTime);  // 设置连接结束时间

                    long requestEndTime = System.nanoTime();
                    apmRecord.setRequestEndTime(requestEndTime);  // 设置请求结束时间

                    apmRecord.setHeaders(request.headers().toString());  // 设置请求头

                    if (request.body() != null) {
                        apmRecord.setBody(request.body().toString());  // 设置请求体
                    }

                    // 记录请求的连接时间和总请求时间
                    long connectionTime = TimeUnit.NANOSECONDS.toMillis(connectEndTime - connectStartTime);
                    long totalRequestTime = TimeUnit.NANOSECONDS.toMillis(requestEndTime - connectStartTime);
                    apmRecord.setConnectEndTime(connectionTime);
                    apmRecord.setTotalRequestTime(totalRequestTime);
                    Log.w("APMRecord: ",apmRecord.toString());
                }
                return response;
            } else {
                // 如果没有 APMRecord，则直接执行请求
                return chain.proceed(request);
            }
        }
    }


    private YkRequest mRequest;
    private YkRequestQueue ykRequestQueue = new YkRequestQueue();

    private APMRecord apmRecord;

    // 链式调用的构造方法
    public YkBaseServant() {
        this.mRequest = new YkRequest();  // 创建默认的请求对象
    }

    // 设置请求的 URL
    public YkBaseServant url(String url) {
        this.mRequest.url(url);  // 设置 URL
        return this;  // 返回当前对象，实现链式调用
    }

    public APMRecord getApmRecord() {
        return apmRecord;
    }

    public YkBaseServant setApmRecord(APMRecord apmRecord) {
        this.apmRecord = apmRecord;
        return this;
    }


    // 设置请求体
    public YkBaseServant requestBody(RequestBody requestBody) {
        this.mRequest.requestBody(requestBody);  // 设置请求体
        return this;  // 返回当前对象，实现链式调用
    }

    //设置优先级
    public YkBaseServant priority(YkPriority priority) {
        this.mRequest.setPriority(priority);  // 设置请求方法
        return this;  // 返回当前对象，实现链式调用
    }

    // 设置请求方法
    public YkBaseServant method(String method) {
        this.mRequest.method(method);  // 设置请求方法
        return this;  // 返回当前对象，实现链式调用
    }

    // 添加请求头
    public YkBaseServant addHeader(String name, String value) {
        this.mRequest.addHeader(name, value);  // 设置请求头
        return this;  // 返回当前对象，实现链式调用
    }

    // 添加拦截器
    public YkBaseServant addInterceptor(Interceptor interceptor) {
        this.mRequest.addInterceptor(interceptor);  // 添加拦截器
        return this;  // 返回当前对象，实现链式调用
    }

    // 发起异步请求，支持回调
    public YkBaseServant request(final RequestCallback callback) {
        // 构建 OkHttpRequest

        mRequest.setCallback(callback);
        mRequest.setApmRecord(apmRecord);
        mRequest.addInterceptor(new LoggingInterceptor());

        // 构建请求
        Request okhttpRequest = mRequest.build();
        mRequest.setmOkHttpRequest(okhttpRequest);
        ykRequestQueue.addRequest(mRequest);
        ykRequestQueue.startPolling();
        return this;
    }

    public YkRequestQueue getYkRequestQueue() {
        return ykRequestQueue;
    }

    public void setYkRequestQueue(YkRequestQueue ykRequestQueue) {
        this.ykRequestQueue = ykRequestQueue;
    }

    // 请求成功回调接口
    public interface RequestCallback {
        void onSuccess(Response response);

        void onFailure(IOException e);

        void onError(int statusCode, String errorMessage);
    }

    // 发起同步请求，支持回调
    public Response requestSync() throws IOException {

        // 构建 OkHttpRequest
        Request okhttpRequest = mRequest.build();  // 构建请求

        // 使用 OkHttp 执行同步请求
        Call call = YkNetworkManager.getInstance().getOkHttpManager().getClient().newCall(okhttpRequest);

        // 阻塞等待请求完成并返回结果
        Response response = call.execute();

        return response;  // 返回响应结果
    }
}
