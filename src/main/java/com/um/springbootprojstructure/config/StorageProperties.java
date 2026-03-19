package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    /**
     * Directory where identity documents are stored.
     */
    private String identityDocumentsDir;

    public String getIdentityDocumentsDir() {
        return identityDocumentsDir;
    }

    public void setIdentityDocumentsDir(String identityDocumentsDir) {
        this.identityDocumentsDir = identityDocumentsDir;
    }
}