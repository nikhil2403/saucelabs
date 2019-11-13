package com.magnificent.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

@SpringBootApplication
@EnableRetry(proxyTargetClass=true)
public class HealthcheckApplication implements CommandLineRunner {

	@Autowired
	ScheduledExecutorService executor;

	@Autowired
	RestTemplate restTemplate;

	@Value("${target.serviceurl}")
	private String serviceURl;

	@Value("${timeSlice.frequency}")
	private int frequency;

	@Autowired
	LinkedHashMap<Long,Boolean> timeEntryMap;

	@Autowired
	HashMap<Boolean, Set<Long>> statusCheckMap;

	public static void main(String[] args) {
		SpringApplication.run(HealthcheckApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	Runnable statusCheck = () -> {
			long second = Instant.now().getEpochSecond();
			Map.Entry<Long, Boolean> firstEntry = timeEntryMap.entrySet().iterator().hasNext()?timeEntryMap.entrySet().iterator().next():null;
			try {
				callDownStreamService();
				if (timeEntryMap.size()==frequency){
					timeEntryMap.remove(firstEntry);
					Boolean pass = firstEntry.getValue();
					Long timeSecond = firstEntry.getKey();
					Set<Long> passTimes = statusCheckMap.get(pass);
					passTimes.remove(timeSecond);
				}
			timeEntryMap.put(second,Boolean.TRUE);

			statusCheckMap.computeIfPresent(Boolean.TRUE,(k,v)->{
				v.add(second);
				return v;
			});

			}
			catch (HttpServerErrorException e){
				if (timeEntryMap.size()==frequency){
					timeEntryMap.remove(firstEntry);
					Boolean pass = firstEntry.getValue();
					Long timeSecond = firstEntry.getKey();
					Set<Long> passTimes = statusCheckMap.get(pass);
					passTimes.remove(timeSecond);
				}
				timeEntryMap.put(second,Boolean.FALSE);

				statusCheckMap.computeIfPresent(Boolean.FALSE,(k,v)->{
					v.add(second);
					return v;
				});
			}
	};
	executor.scheduleAtFixedRate(statusCheck,0,1,TimeUnit.SECONDS);
	}
@Retryable( value = { ResourceAccessException.class },
		maxAttempts = 2,
		backoff = @Backoff(delay = 1000))
	public void callDownStreamService() {
			restTemplate.getForEntity(serviceURl, String.class);
			Constants.setIsServiceDown(false);
	}

	@Recover
 public void recover(ResourceAccessException e){
		Constants.setIsServiceDown(true);
	}
}
