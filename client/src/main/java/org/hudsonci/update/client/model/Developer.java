package org.hudsonci.update.client.model;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Model representing Developer of a Plugin 
 * @author Winston Prakash
 */
@JsonWriteNullProperties(false)
public class Developer {

    private String developerId;
    private String email;
    private String name;

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
