/*******************************************************************************
 *
 * Copyright (c) 2011 Sonatype Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *
 *    Jason Van Zyl
 *     
 *
 *******************************************************************************/

package org.hudsonci.update.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.model.Model;

public class Core {

    protected Model hudsonPluginMavenPom;
    protected Metadata hudsonPluginMavenMetadata;
    protected String downloadSite = "http://repo1.maven.org/maven2/";

    public Core() {
        // Create a Dummy to satisfy JSOn writer
    }

    /**
     * @param hudsonPluginMavenPom
     * @param hudsonPluginManifestAttributes
     * @param hudsonPluginMavenMetadata 
     */
    public Core(Model hudsonPluginMavenPom, Metadata hudsonPluginMavenMetadata) {
        this.hudsonPluginMavenPom = hudsonPluginMavenPom;
        this.hudsonPluginMavenMetadata = hudsonPluginMavenMetadata;
    }

    public String getBuildDate() {
        DateFormat formatter = new SimpleDateFormat("MMM d, yyyy");

        DateFormat mavenMetadataDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        if (hudsonPluginMavenMetadata != null) {
            try {
                Date lastUpdated = mavenMetadataDateFormat.parse(hudsonPluginMavenMetadata.getVersioning().getLastUpdated());
                return formatter.format(lastUpdated);
            } catch (ParseException e) {
                // Do nothing, return Unknown
            }
        }
        return "Unknown";
    }

    public String getUrl() {
        String path = "";
        if (hudsonPluginMavenMetadata != null) {
            //
            // This is null until we do parent POM parsing
            //
            //String groupId = mavenPom.getGroupId();
            String groupId = "org.jvnet.hudson.plugins";

            path = groupId.replace('.', '/') + "/" + hudsonPluginMavenPom.getArtifactId() + "/" + getVersion() + "/"
                    + hudsonPluginMavenPom.getArtifactId() + "-" + getVersion() + ".war";
        }
        return downloadSite + path;
    }

    public String getVersion() {
        if (hudsonPluginMavenMetadata != null) {
            return hudsonPluginMavenMetadata.getVersioning().getLatest();
        } else {
            return "";
        }

    }

    public String getName() {
        if (hudsonPluginMavenMetadata != null) {
            return "core";
        } else {
            return "dummy";
        }
    }
}
