package org.hudsonci.update.model;

public class Dependency
{
    private String name;
    private boolean optional = true;
    private String version;
    
    public Dependency( String name, boolean optional, String version )
    {
        this.name = name;
        this.optional = optional;
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public boolean isOptional()
    {
        return optional;
    }

    public String getVersion()
    {
        return version;
    }           
}
