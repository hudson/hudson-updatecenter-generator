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

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * Model representing Update Center 
 * @author Winston Prakash
 */
@JsonWriteNullProperties(false)
public class UpdateSiteMetadata {

    private Core core;
    private String id = "default";
    private String connectionCheckUrl  = "http://www.google.com";
    private Map<String, Plugin> plugins = new HashMap<String, Plugin>();
    private Signature signature = new Signature();

    public String getUpdateCenterVersion() {
        return updateCenterVersion;
    }

    public void setUpdateCenterVersion(String updateCenterVersion) {
        this.updateCenterVersion = updateCenterVersion;
    }
    private String updateCenterVersion;

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public String getConnectionCheckUrl() {
        return connectionCheckUrl;
    }

    public void setConnectionCheckUrl(String connectionCheckUrl) {
        this.connectionCheckUrl = connectionCheckUrl;
    }

    public Core getCore() {
        return core;
    }

    public void setCore(Core core) {
        this.core = core;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(Map<String, Plugin> plugins) {
        this.plugins = plugins;
    }

    @JsonIgnore
    public Plugin findPlugin(String name) {
        return plugins.get(name);
    }

    @JsonIgnore
    public void replacePlugin(Plugin oldPlugin, Plugin newPlugin) {    
        plugins.get(oldPlugin.getName()).set(newPlugin);
    }

    @JsonIgnore
    public void add(Plugin plugin) {
        plugins.put(plugin.getName(), plugin);
    }
}
