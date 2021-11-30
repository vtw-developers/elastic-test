package com.example.camelelastic;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.ElasticsearchEndpointBuilderFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// @Component
public class ElasticRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {

        String[] agents = {"에이전트#1", "에이전트#2", "에이전트#3"};
        String[] programs = {"사용자", "코드", "권한", "프로그램", "나의할일"};
        String[] txTypes = {"생성", "변경", "삭제"};

        from(timer("test").period("1s"))
        .process(exchange -> {
            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", System.currentTimeMillis());

            Random r = new Random();
            int num = r.nextInt(3);
            data.put("agent", agents[num]);

            num = r.nextInt(5);
            data.put("program", programs[num]);

            num = r.nextInt(3);
            data.put("txType", txTypes[num]);

            exchange.getMessage().setBody(data);

            num = r.nextInt(3000);
            TimeUnit.MILLISECONDS.sleep(num);
        })
        .to(elasticsearchRest("elastic-test")
            .operation(ElasticsearchEndpointBuilderFactory.ElasticsearchOperation.Index)
            .indexName("test-003"))
        .log("${body}");



        from(timer("test2").period("2s"))
        .process(exchange -> {
            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", System.currentTimeMillis());

            Random r = new Random();
            int num = r.nextInt(3);
            data.put("agent", agents[num]);

            num = r.nextInt(5);
            data.put("program", programs[num]);

            num = r.nextInt(3);
            data.put("txType", txTypes[num]);

            exchange.getMessage().setBody(data);

            num = r.nextInt(1000);
            TimeUnit.MILLISECONDS.sleep(num);
        })
        .to(elasticsearchRest("elastic-test")
                .operation(ElasticsearchEndpointBuilderFactory.ElasticsearchOperation.Index)
                .indexName("test-003"))
        .log("${body}");
    }
}
