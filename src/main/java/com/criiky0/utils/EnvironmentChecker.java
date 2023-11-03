package com.criiky0.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentChecker {
    private Environment environment;

    @Autowired
    public EnvironmentChecker(Environment environment) {
        this.environment = environment;
    }

    public boolean isDevelopment() {
        return environment.getActiveProfiles().length > 0 &&
                environment.getActiveProfiles()[0].equals("dev");
    }

    public boolean isProduction() {
        return environment.getActiveProfiles().length > 0 &&
                environment.getActiveProfiles()[0].equals("prod");
    }
}
