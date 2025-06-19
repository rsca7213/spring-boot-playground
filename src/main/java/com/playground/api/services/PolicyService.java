package com.playground.api.services;

import com.playground.api.entities.Policy;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ApiException;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.ConsequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PolicyService {
    private final KieContainer kieContainer;

    @Autowired
    public PolicyService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public Boolean evaluatePolicy(Policy policy) {
        try (KieSession kieSession = kieContainer.newKieSession()) {
            kieSession.insert(policy);
            kieSession.fireAllRules();
            return true;
        }
        catch (ConsequenceException e) {
            Throwable error = e.getCause();

            if (error instanceof ApiException apiException) {
                throw apiException;
            } else {
                throw new ApiException(
                        "An unexpected error occurred while evaluating the policy: " + error.getMessage(),
                        ErrorCode.RUNTIME_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }
    }
}
