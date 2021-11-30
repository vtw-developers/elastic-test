package com.example.camelelastic;

import com.sun.management.UnixOperatingSystemMXBean;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.ElasticsearchEndpointBuilderFactory;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// @Component
public class MetricRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {
        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();


        final OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        if (mxBean instanceof UnixOperatingSystemMXBean) {
            double systemCpuLoad = ((UnixOperatingSystemMXBean) mxBean).getSystemCpuLoad();
        }
    }
}
