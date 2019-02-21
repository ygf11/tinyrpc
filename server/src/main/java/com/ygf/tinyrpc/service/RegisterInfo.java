package com.ygf.tinyrpc.service;

import java.util.List;

/**
 * 写入zk的信息
 *
 * @author theo
 * @date 20190221
 */
public class RegisterInfo {
    private String protocal;
    private String ip;
    private String serviceId;
    private String appName;
    private String iService;
    private List<String> methods;
}
