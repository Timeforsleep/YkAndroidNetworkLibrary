package com.example.yknetworklibrary;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;


public class YkRequest implements Comparable<YkRequest> {
    private final Request.Builder requestBuilder; // 用于构建请求
    private String url; // 记录请求的 URL
    private RequestBody requestBody; // 请求体
    private String method = "GET"; // 默认方法为 GET
    private Request mOkHttpRequest;
    //优先级
    private int mPriority = YkPriority.NORMAL;
    private List<Interceptor> interceptors = new ArrayList<>();

    private YkBaseServant.RequestCallback mCallback;  // 作为类的成员变量，保存回调

    // 构造方法
    public YkRequest() {
        this.requestBuilder = new Request.Builder();
    }

    // 设置请求的回调
    public YkRequest setCallback(YkBaseServant.RequestCallback callback) {
        this.mCallback = callback;  // 保存回调
        return this;  // 返回当前对象，实现链式调用
    }

    // 构建最终的 Request 对象
    public Request build() {
        // 如果存在请求体且未设置方法，默认使用 POST
        if (this.requestBody != null && (method == null || method.isEmpty())) {
            this.method = "POST";
        }

        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("URL must be set before building the request");
        }
        for (int i = 0; i < interceptors.size(); i++) {
            YkNetworkManager.getInstance().getOkHttpManager().getClient().interceptors().add(interceptors.get(i));
        }
        this.requestBuilder.method(method, requestBody);
        return requestBuilder.build();
    }

    // 设置 URL
    public YkRequest url(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        this.url = url;
        this.requestBuilder.url(url);
        return this;
    }

    // 添加请求头
    public YkRequest addHeader(String name, String value) {
        if (name == null || value == null) {
            throw new IllegalArgumentException("Header name or value cannot be null");
        }
        this.requestBuilder.addHeader(name, value);
        return this;
    }

    // 添加请求体
    public YkRequest requestBody(RequestBody requestBody) {
        if (requestBody == null) {
            throw new IllegalArgumentException("RequestBody cannot be null");
        }
        this.requestBody = requestBody;
        return this;
    }

    // 设置请求方法
    public YkRequest method(String method) {
        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("HTTP method cannot be null or empty");
        }
        this.method = method.toUpperCase();
        return this;
    }

    // 设置 tag
    public <T> YkRequest tag(T tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        this.requestBuilder.tag(tag);
        return this;
    }

    // 添加拦截器
    public YkRequest addInterceptor(Interceptor interceptor) {
        if (interceptor == null) {
            throw new IllegalArgumentException("Interceptor cannot be null");
        }
        this.interceptors.add(interceptor);
        return this;
    }

    // 移除请求头
    public YkRequest removeHeader(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Header name cannot be null or empty");
        }
        this.requestBuilder.removeHeader(name);
        return this;
    }

    // 获取当前设置的 URL
    public String getUrl() {
        return url;
    }

    // 获取当前设置的 HTTP 方法
    public String getMethod() {
        return method;
    }

    // 获取请求体
    public RequestBody getRequestBody() {
        return requestBody;
    }

    // 生成用于调试的字符串
    @Override
    public String toString() {
        return "YkRequest{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", requestBody=" + (requestBody != null ? requestBody.toString() : "null") +
                '}';
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    @Override
    public int compareTo(YkRequest other) {
        //数字越大优先级越高
        return Integer.compare(other.mPriority, this.mPriority);
    }

    public Request getmOkHttpRequest() {
        return mOkHttpRequest;
    }

    public void setmOkHttpRequest(Request mOkHttpRequest) {
        this.mOkHttpRequest = mOkHttpRequest;
    }

    public YkBaseServant.RequestCallback getmCallback() {
        return mCallback;
    }
}
