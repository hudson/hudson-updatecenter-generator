package org.hudsonci.update.client.model;

/**
 * Model representing Plugin Dependency
 * @author Winston Prakash
 */
public class Dependency {

    private String name;
    private boolean optional = true;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
