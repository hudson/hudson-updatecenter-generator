package org.hudsonci.update.model;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(false)
public class Developer {

    private String developerId;
    private String email;
    private String name;

    public Developer(String name, String developerId, String email) {
        this.name = name;
        this.developerId = developerId;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public String getEmail() {
        return email;
    }
}
