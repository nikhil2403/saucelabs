package com.magnificent.healthcheck.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class Conf {

    @Bean
    public ScheduledExecutorService getExecutor(){

        return Executors.newSingleThreadScheduledExecutor(
                (Runnable r) -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                });
    }

    @Bean
    public RestTemplate getTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public LinkedHashMap<Long,Boolean> getTimeEntryMap(){
        return new LinkedHashMap<>();
    }

    @Bean
    public HashMap<Boolean, Set<Long>> getStatusMap(){
        HashMap<Boolean, Set<Long>> statusMap = new HashMap<>();
        statusMap.put(Boolean.FALSE,new HashSet<>());
        statusMap.put(Boolean.TRUE,new HashSet<>());
        return statusMap;
    }
}
