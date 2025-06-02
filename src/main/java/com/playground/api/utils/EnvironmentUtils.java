package com.playground.api.utils;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

public class EnvironmentUtils {
    public static boolean isDevelopment(Environment environment) {
        return environment.acceptsProfiles(Profiles.of("dev", "local", "test"));
    }
}
