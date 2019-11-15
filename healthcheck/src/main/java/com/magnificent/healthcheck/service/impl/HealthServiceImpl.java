package com.magnificent.healthcheck.service.impl;

import com.magnificent.healthcheck.Constants;
import com.magnificent.healthcheck.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class HealthServiceImpl implements HealthService {
    @Autowired
    private Map<Boolean, Set<Long>> statusCheckMap;

    @Override
    public String getStatus() {
        if (Constants.isIsServiceDown()){
            return "Service is down temporarily. PLease check again";
        }
      int passCount =   statusCheckMap.get(Boolean.TRUE).size();
      int failCount = statusCheckMap.get(Boolean.FALSE).size();
      double passPercentage = (double) passCount*100/(passCount+failCount);

        return String.format("Service is healthy %s percentage in last one minute ",passPercentage);
    }
}
