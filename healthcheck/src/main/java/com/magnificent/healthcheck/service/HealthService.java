package com.magnificent.healthcheck.service;
/*
service to check health status.
service will try to fetch current health status from status map.
It will retry 3 times if service is unreachable ,
then it will hit circuit breaker to retrun unreachable service response
 */
public interface HealthService {

    public String getStatus();
}
