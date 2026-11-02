package com.su.property.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务发现演示接口（难点突破：Nacos 服务注册与发现）。
 *
 * Nacos 关闭时（spring.cloud.nacos.discovery.enabled=false，默认）：
 * 接口可访问，返回空服务清单，应用单机跑通、不依赖 Nacos。
 * Nacos 开启并连接后：返回注册中心服务清单（应包含 property-service）。
 */
@RestController
@RequestMapping("/api/discovery")
public class DiscoveryController {

    private final ObjectProvider<DiscoveryClient> discoveryClient;

    @Value("${spring.cloud.nacos.discovery.enabled:false}")
    private boolean nacosEnabled;

    public DiscoveryController(ObjectProvider<DiscoveryClient> discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/services")
    public Map<String, Object> services() {
        DiscoveryClient client = discoveryClient.getIfAvailable();
        List<String> services = client == null ? List.of() : client.getServices();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("nacosEnabled", nacosEnabled);
        result.put("serviceName", "property-service");
        result.put("services", services);
        return result;
    }
}
