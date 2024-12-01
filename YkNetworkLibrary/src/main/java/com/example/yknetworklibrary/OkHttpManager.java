package com.example.yknetworklibrary;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class OkHttpManager {
    private long writeTimeout = 15000;

    //读超时时间
    private long readTimeout = 15000;

    //连接超时时间
    private long connectTimeout = 15000;

    //最大请求并发数量
    private int maxRequests = 5;

    //主机最大并发请求数
    private int maxRequestsPerHost = 5;

    private OkHttpClient client;

    private final Dispatcher dispatcher = new Dispatcher();

    private OkHttpManager(Builder builder) {
        this.writeTimeout = builder.writeTimeout;
        this.readTimeout = builder.readTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.maxRequests = builder.maxRequests;
        this.maxRequestsPerHost = builder.maxRequestsPerHost;
    }

    public static class Builder{
        //写超时时间
        private long writeTimeout = 15000;

        //读超时时间
        private long readTimeout = 15000;

        //连接超时时间
        private long connectTimeout = 15000;

        //最大请求并发数量
        private int maxRequests = 5;

        //主机最大并发请求数
        private int maxRequestsPerHost = 5;

        public Builder writeTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder readTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder connectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public OkHttpManager build() {
            OkHttpManager okHttpManager = new OkHttpManager(this);
            okHttpManager.dispatcher.setMaxRequests(maxRequests);
            okHttpManager.dispatcher.setMaxRequestsPerHost(maxRequestsPerHost);
            okHttpManager.client = new OkHttpClient.Builder()
                    .dispatcher(okHttpManager.dispatcher)
                    .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeout,TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeout,TimeUnit.MILLISECONDS)
                    .build();
            return okHttpManager;
        }

        public Builder maxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
            return this;
        }

        public Builder maxRequestsPerHost(int maxRequestsPerHost) {
            this.maxRequestsPerHost = maxRequestsPerHost;
            return this;
        }
    }



    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public OkHttpClient getClient() {
        return client;
    }


}
