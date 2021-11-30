package com.example.camelelastic;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.ElasticsearchEndpointBuilderFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ElasticRoute2 extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {

        // NameNode
        from(timer("NameNode").period("5s"))
        .to(http("192.168.37.128:9870/jmx"))
        .unmarshal().json()
        .process(exchange -> {
            Map beans = exchange.getMessage().getBody(Map.class);
            List<Map<String, Object>> list = (List) beans.get("beans");

            Map<String, Object> metric = new LinkedHashMap<>();
            metric.put("timestamp", System.currentTimeMillis());

            Map<String, Object> jvmMetrics = list.stream().filter(map -> map.get("name").equals("Hadoop:service=NameNode,name=JvmMetrics")).findFirst().get();
            log.info("JvmMetrics: " + jvmMetrics);
            metric.put("JvmMetrics", jvmMetrics);

            Map<String, Object> nameNodeActivity = list.stream().filter(map -> map.get("name").equals("Hadoop:service=NameNode,name=NameNodeActivity")).findFirst().get();
            log.info("NameNodeActivity: " + nameNodeActivity);
            metric.put("NameNodeActivity", nameNodeActivity);

            Map<String, Object> blockStats = list.stream().filter(map -> map.get("name").equals("Hadoop:service=NameNode,name=BlockStats")).findFirst().get();
            log.info("BlockStats: " + blockStats);
            metric.put("BlockStats", blockStats);

            Map<String, Object> fSNamesystem = list.stream().filter(map -> map.get("name").equals("Hadoop:service=NameNode,name=FSNamesystem")).findFirst().get();
            log.info("FSNamesystem: " + fSNamesystem);
            metric.put("FSNamesystem", fSNamesystem);

            Map<String, Object> RpcActivityForPort9000 = list.stream().filter(map -> map.get("name").equals("Hadoop:service=NameNode,name=RpcActivityForPort9000")).findFirst().get();
            log.info("RpcActivityForPort9000: " + RpcActivityForPort9000);
            metric.put("RpcActivityForPort9000", RpcActivityForPort9000);

            Map<String, Object> OperatingSystem = list.stream().filter(map -> map.get("name").equals("java.lang:type=OperatingSystem")).findFirst().get();
            log.info("OperatingSystem: " + OperatingSystem);
            metric.put("OperatingSystem", OperatingSystem);

            Map<String, Object> Runtime = list.stream().filter(map -> map.get("name").equals("java.lang:type=Runtime")).findFirst().get();
            log.info("Runtime: " + Runtime);
            metric.put("Runtime", Runtime);

            log.info("metric: {}", metric);

            exchange.getMessage().setBody(metric);
        })
        .to(elasticsearchRest("NameNode")
                .operation(ElasticsearchEndpointBuilderFactory.ElasticsearchOperation.Index)
                .indexName("metrics-namenode"));

        // ResourceManager
        from(timer("ResourceManager").period("5s"))
        .to(http("192.168.37.128:8088/jmx"))
        .unmarshal().json()
        .process(exchange -> {
            Map beans = exchange.getMessage().getBody(Map.class);
            List<Map<String, Object>> list = (List) beans.get("beans");

            Map<String, Object> metric = new LinkedHashMap<>();
            metric.put("timestamp", System.currentTimeMillis());

            Map<String, Object> jvmMetrics = list.stream().filter(map -> map.get("name").equals("Hadoop:service=ResourceManager,name=JvmMetrics")).findFirst().get();
            log.info("JvmMetrics: " + jvmMetrics);
            metric.put("JvmMetrics", jvmMetrics);

            Map<String, Object> OperatingSystem = list.stream().filter(map -> map.get("name").equals("java.lang:type=OperatingSystem")).findFirst().get();
            log.info("OperatingSystem: " + OperatingSystem);
            metric.put("OperatingSystem", OperatingSystem);

            Map<String, Object> Runtime = list.stream().filter(map -> map.get("name").equals("java.lang:type=Runtime")).findFirst().get();
            log.info("Runtime: " + Runtime);
            metric.put("Runtime", Runtime);

            log.info("metric: {}", metric);

            exchange.getMessage().setBody(metric);
        })
        .to(elasticsearchRest("ResourceManager")
                .operation(ElasticsearchEndpointBuilderFactory.ElasticsearchOperation.Index)
                .indexName("metrics-resourcemanager"));

        // DataNode
        from(timer("DataNode").period("5s"))
        .to(http("192.168.37.128:9864/jmx"))
        .unmarshal().json()
        .process(exchange -> {
            Map beans = exchange.getMessage().getBody(Map.class);
            List<Map<String, Object>> list = (List) beans.get("beans");

            Map<String, Object> metric = new LinkedHashMap<>();
            metric.put("timestamp", System.currentTimeMillis());

            Map<String, Object> jvmMetrics = list.stream().filter(map -> map.get("name").equals("Hadoop:service=DataNode,name=JvmMetrics")).findFirst().get();
            log.info("JvmMetrics: " + jvmMetrics);
            metric.put("JvmMetrics", jvmMetrics);

            Map<String, Object> OperatingSystem = list.stream().filter(map -> map.get("name").equals("java.lang:type=OperatingSystem")).findFirst().get();
            log.info("OperatingSystem: " + OperatingSystem);
            metric.put("OperatingSystem", OperatingSystem);

            log.info("metric: {}", metric);

            exchange.getMessage().setBody(metric);
        })
        .to(elasticsearchRest("DataNode")
                .operation(ElasticsearchEndpointBuilderFactory.ElasticsearchOperation.Index)
                .indexName("metrics-datanode"));
    }
}
