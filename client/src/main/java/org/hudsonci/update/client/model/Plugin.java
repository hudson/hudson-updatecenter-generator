/*******************************************************************************
 *
 * Copyright (c) 2011 Oracle Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *
 *    Winston Prakash
 *     
 *******************************************************************************/

package org.hudsonci.update.client.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Model representing Plugin info
 * @author Winston Prakash
 */
@JsonSerialize(
    include = JsonSerialize.Inclusion.NON_NULL
)
public class Plugin {

    protected static final String DEFAULT_CORE_VERSION = "1.395";
    private List<String> labels = new ArrayList<String>();
    private String excerpt;
    private List<Dependency> dependencies = new ArrayList<Dependency>();
    private List<Developer> developers = new ArrayList<Developer>();
    private String buildDate;
    private String name;
    private String previousTimestamp;
    private String releaseTimestamp;
    private String requiredCore ;
    private String scm ;
    private String title;
    private String url;
    private String version;
    private String wiki;
    private String previousVersion;
    
    public void set(Plugin newPlugin){
        if (CollectionUtils.isNotEmpty(newPlugin.getLabels())){
            labels = newPlugin.getLabels();
        }
        if (StringUtils.isNotBlank(newPlugin.getExcerpt())){
            excerpt = newPlugin.getExcerpt();
        }
        if (CollectionUtils.isNotEmpty(newPlugin.getDependencies())){
            dependencies = newPlugin.getDependencies();
        }
        if (CollectionUtils.isNotEmpty(newPlugin.getDevelopers())){
            developers = newPlugin.getDevelopers();
        }
        if (StringUtils.isNotBlank(newPlugin.getBuildDate())){
            buildDate = newPlugin.getBuildDate();
        }
        if (StringUtils.isNotBlank(newPlugin.getPreviousTimestamp())){
            previousTimestamp = newPlugin.getPreviousTimestamp();
        }
        if (StringUtils.isNotBlank(newPlugin.getReleaseTimestamp())){
            releaseTimestamp = newPlugin.getReleaseTimestamp();
        }
        if (StringUtils.isNotBlank(newPlugin.getRequiredCore())){
            requiredCore = newPlugin.getRequiredCore();
        } else {
            if (requiredCore == null) {
                requiredCore = DEFAULT_CORE_VERSION;
            }
        }
        if (StringUtils.isNotBlank(newPlugin.getScm())){
            scm = newPlugin.getScm();
        }
        if (StringUtils.isNotBlank(newPlugin.getTitle())){
            title = newPlugin.getTitle();
        }
        if (StringUtils.isNotBlank(newPlugin.getUrl())){
            url = newPlugin.getUrl();
        }
        if (StringUtils.isNotBlank(newPlugin.getVersion())){
            version = newPlugin.getVersion();
        }
        if (StringUtils.isNotBlank(newPlugin.getWiki())){
            wiki = newPlugin.getWiki();
        }
        if (StringUtils.isNotBlank(newPlugin.getPreviousVersion())){
            previousVersion = newPlugin.getPreviousVersion();
        }
    }

    @JsonAnySetter
    void addEntry(String key, Object value) {
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }
        

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreviousTimestamp() {
        return previousTimestamp;
    }

    public void setPreviousTimestamp(String previousTimestamp) {
        this.previousTimestamp = previousTimestamp;
    }

    public String getReleaseTimestamp() {
        return releaseTimestamp;
    }

    public void setReleaseTimestamp(String releaseTimestamp) {
        this.releaseTimestamp = releaseTimestamp;
    }

    public String getRequiredCore() {
        return requiredCore;
    }

    public void setRequiredCore(String requiredCore) {
        this.requiredCore = requiredCore;
    }
    
    public String getPreviousVersion() {
        return previousVersion;
    }

    public void setPreviousVersion(String previousVersion) {
        this.previousVersion = previousVersion;
    }

    public String getScm() {
        return scm;
    }

    public void setScm(String scm) {
        this.scm = scm;
    }

    public String getTitle() {
        if ("".equals(title)){
            title = name;
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }
    
    @JsonIgnore
    public  boolean isNewerThan(Plugin otherPlugin) {
        try {
            return new VersionNumber(getVersion()).compareTo(new VersionNumber(otherPlugin.getVersion())) > 0;
        } catch (IllegalArgumentException e) {
            // couldn't parse as the version number.
            return false;
        }
    }
    
    
}
