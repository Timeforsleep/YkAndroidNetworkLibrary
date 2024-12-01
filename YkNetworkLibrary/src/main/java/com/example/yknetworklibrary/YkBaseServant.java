package com.example.yknetworklibrary;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import java.io.IOException;

public class YkBaseServant {

    private YkRequest mRequest;
    private YkRequestQueue ykRequestQueue = new YkRequestQueue();

    // 链式调用的构造方法
    public YkBaseServant() {
        this.mRequest = new YkRequest();  // 创建默认的请求对象
    }

    // 设置请求的 URL
    public YkBaseServant url(String url) {
        this.mRequest.url(url);  // 设置 URL
        return this;  // 返回当前对象，实现链式调用
    }

    // 设置请求体
    public YkBaseServant requestBody(RequestBody requestBody) {
        this.mRequest.requestBody(requestBody);  // 设置请求体
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
        Request okhttpRequest = mRequest.build();
        mRequest.setmOkHttpRequest(okhttpRequest);// 构建请求
        mRequest.setCallback(callback);
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
