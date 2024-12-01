//package com.example.yknetworklibrary.configs;
//import java.util.Comparator;
//import java.util.PriorityQueue;
//public class YkServantBuilder {
//
//    private int maxRequests = 5;  // 默认最大请求并发数
//    private int maxRequestsPerHost = 5;  // 默认每个主机最大并发数
//    private Comparator<ykBaseRequest> priorityComparator;  // 用于请求队列的优先级排序
//
//    // 构造器
//    public YkServantBuilder() {
//        // 默认的优先级比较规则：按照请求 URL 长度进行排序
//        this.priorityComparator = new Comparator<ykBaseRequest>() {
//            @Override
//            public int compare(ykBaseRequest o1, ykBaseRequest o2) {
//                return o1.getUrl().compareTo(o2.getUrl());
//            }
//        };
//    }
//
//    // 设置最大请求并发数
//    public YkServantBuilder setMaxRequests(int maxRequests) {
//        this.maxRequests = maxRequests;
//        return this;
//    }
//
//    // 设置每个主机最大并发请求数
//    public YkServantBuilder setMaxRequestsPerHost(int maxRequestsPerHost) {
//        this.maxRequestsPerHost = maxRequestsPerHost;
//        return this;
//    }
//
//    // 设置队列的优先级比较器
//    public YkServantBuilder setPriorityComparator(Comparator<ykBaseRequest> comparator) {
//        if (comparator == null) {
//            throw new IllegalArgumentException("Comparator cannot be null");
//        }
//        this.priorityComparator = comparator;
//        return this;
//    }
//
//    // 发送请求
//    public void request(ykBaseRequest request) {
//        // 创建 YkBaseServant 实例
//        YkBaseServant servant = new YkBaseServant();
//
//        // 设置优先级队列
//        servant.setPriorityQueue(new PriorityQueue<>(priorityComparator));
//
//        // 配置请求并加入队列
//        servant.addRequestToQueue(request);
//
//        // 执行请求
//        servant.executeRequests();
//    }
//}
