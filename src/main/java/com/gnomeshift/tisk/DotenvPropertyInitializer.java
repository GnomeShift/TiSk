package com.gnomeshift.tisk;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        // Get all profiles
        String[] profiles = environment.getActiveProfiles();

        // If profile is undefined - exit
        if (profiles.length == 0) {
            return;
        }

        // Get first active profile
        String activeProfile = profiles[0];

        String dotenvFile = ".env." + activeProfile;

        Dotenv dotenv = Dotenv.configure()
                .filename(dotenvFile)
                .ignoreIfMissing()
                .load();

        // If dotenv is empty - exit
        if (dotenv.entries().isEmpty()) {
            return;
        }

        Map<String, Object> vars = new HashMap<>();
        dotenv.entries().forEach(entry ->
                vars.put(entry.getKey(), entry.getValue())
        );

        // Add at first
        environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", vars));
    }
}
