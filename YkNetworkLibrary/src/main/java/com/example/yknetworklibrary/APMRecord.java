package com.example.yknetworklibrary;

import java.util.concurrent.TimeUnit;

public class APMRecord {
    private long startTime;       // 请求开始时间
    private long connectStartTime; // 连接建立开始时间
    private long connectEndTime;   // 连接建立结束时间
    private long requestEndTime;   // 请求完成时间
    private String url;            // 请求的 URL
    private String headers;        // 请求头
    private String body;           // 请求体
    private long connectionTime;
    private long totalRequestTime;

    public APMRecord(String url) {
        this.startTime = System.currentTimeMillis();
        this.url = url;
    }

    public void setConnectStartTime(long connectStartTime) {
        this.connectStartTime = connectStartTime;
    }

    public void setConnectEndTime(long connectEndTime) {
        this.connectEndTime = connectEndTime;
    }

    public void setRequestEndTime(long requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "APMRecord{" +
                "startTime=" + formatTimestamp(startTime) +
                ", connectStartTime=" + formatTimestamp(connectStartTime) +
                ", connectEndTime=" + formatTimestamp(connectEndTime) +
                ", requestEndTime=" + formatTimestamp(requestEndTime) +
                ", connectionTime=" + connectionTime + " ms" +
                ", totalRequestTime=" + totalRequestTime + " ms" +
                ", url='" + url + '\'' +
                ", headers='" + (headers != null ? headers : "N/A") + '\'' +
                ", body='" + (body != null ? body : "N/A") + '\'' +
                '}';
    }


    public void setConnectionTime(long connectionTime) {
        this.connectionTime = connectionTime;
    }

    public void setTotalRequestTime(long totalRequestTime) {
        this.totalRequestTime = totalRequestTime;
    }

    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) return "N/A";
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .format(new java.util.Date(timestamp));
    }
}

