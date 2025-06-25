package com.playground.api.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestService {
    private final ZeebeClient zeebeClient;

    @Autowired
    public TestService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    public Map<String, Double> chargeCreditCard(Double total) {
        final String processId = "charge-credit-card";
        System.out.println("Processing job with type: " + processId);

        final var result = zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId(processId)
                .latestVersion()
                .variables(Map.of("total", total))
                .send()
                .join();

        System.out.println("Process instance created with key: " + result.getProcessInstanceKey());
        System.out.println(result);

        return Map.of();
    }

}
