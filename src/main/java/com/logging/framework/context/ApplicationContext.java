package com.logging.framework.context;

public class ApplicationContext {
    private String applicationName;
    private String organizationName;
    private String environment;

    // Singleton instance
    private static ApplicationContext instance;

    private ApplicationContext() {}

    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    // Getters and setters
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}