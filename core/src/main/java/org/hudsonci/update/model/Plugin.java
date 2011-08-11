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

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import org.apache.commons.lang.time.FastDateFormat;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.model.Model;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.plexus.util.StringUtils;

// An example of what the metadata for a plugin looks like in the update center
//
//  "BlameSubversion":     {
//      "buildDate": "Nov 01, 2010",
//      "dependencies": [],
//      "developers": [      {
//        "developerId": "tangjinou",
//        "email": "tangjinou@gmail.com",
//        "name": "Developer Guy"
//      }],
//      "excerpt": "This plug-in provides utilities for getting svn info from upstream job to downstream job",
//      "labels": ["scm"],
//      "name": "BlameSubversion",
//      "previousTimestamp": "2010-11-01T22:58:56.00Z",
//      "previousVersion": "1.24",
//      "releaseTimestamp": "2010-11-02T00:15:06.00Z",
//      "requiredCore": "1.355",
//      "scm": "svn.dev.java.net",
//      "title": "BlameSubversion",
//      "url": "http://updates.jenkins-ci.org/download/plugins/BlameSubversion/1.25/BlameSubversion.hpi",
//      "version": "1.25",
//      "wiki": "http://wiki.jenkins-ci.org/display/JENKINS/BlameSubversion"
//    }
@JsonWriteNullProperties(false)
public class Plugin {

    //
    // Information we can glean from the Maven POM of the Hudson plugin project.
    //
    private Model hudsonPluginMavenPom;
    //
    //
    //
    private Metadata hudsonPluginMavenMetadata;
    //
    // The main URL where the plugins are available from.
    //
    protected String downloadSite = "http://repo1.maven.org/maven2/";
    public static final FastDateFormat XS_DATETIME_FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'", new SimpleTimeZone(0, "GMT"));

    /**
     * @param hudsonPluginMavenPom
     * @param hudsonPluginManifestAttributes
     * @param hudsonPluginMavenMetadata 
     */
    public Plugin(Model hudsonPluginMavenPom, Attributes hudsonPluginManifestAttributes, Metadata hudsonPluginMavenMetadata) {
        this.hudsonPluginMavenPom = hudsonPluginMavenPom;
        this.hudsonPluginMavenMetadata = hudsonPluginMavenMetadata;
        manifestAttributes = hudsonPluginManifestAttributes;
    }
    // Information we can glean from the Hudson plugin MANIFEST.MF
    //
    private Attributes manifestAttributes;

    public String getExcerpt() {
        return hudsonPluginMavenPom.getDescription();
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getScm() {
        if (hudsonPluginMavenPom.getScm() != null) {
//            if (hudsonPluginMavenPom.getScm().getConnection().indexOf("svn.dev.java.net") > 0) {
//                return "svn.dev.java.net";
//            } else if (hudsonPluginMavenPom.getScm().getConnection().indexOf("github.com") > 0) {
//                return "github.com";
//            }

            return hudsonPluginMavenPom.getScm().getUrl();
        } else {
            return "";
        }
    }

    public String getTitle() {
        String title = hudsonPluginMavenPom.getName();
        if ((title == null) || "".equals(title)) {
            title = hudsonPluginMavenPom.getArtifactId();
        }
        return title;
    }

    public String getReleaseTimestamp() {
        DateFormat mavenMetadataDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        if (hudsonPluginMavenMetadata != null) {
            try {
                Date lastUpdated = mavenMetadataDateFormat.parse(hudsonPluginMavenMetadata.getVersioning().getLastUpdated());
                return XS_DATETIME_FORMATTER.format(lastUpdated);
            } catch (ParseException e) {
                // Do nothing, return Unknown
            }
        }

        return "Unknown";
    }

    public List<Dependency> getDependencies() {
        List<Dependency> dependencies = new ArrayList<Dependency>();
        //
        // Plugin-Dependencies: maven-plugin:1.358,dashboard-view:1.5;resolution: =optional                                                                         
        //
        String pluginDependencies = manifestAttributes.getValue("Plugin-Dependencies");
        if (pluginDependencies != null) {
            String[] ds = StringUtils.split(pluginDependencies, ",");
            for (String d : ds) {
                String[] s = StringUtils.split(d, ":");
                if (s.length == 2) {
                    //
                    //  maven-plugin:1.358
                    //
                    dependencies.add(new Dependency(s[0], false, s[1]));
                } else {
                    //
                    // dashboard-view:1.5;resolution: =optional 
                    //
                    dependencies.add(new Dependency(s[0], true, s[1].substring(0, s[1].indexOf(';'))));
                }
            }
        }

        return dependencies;
    }

    public List<Developer> getDevelopers() {
        List<Developer> developers = new ArrayList<Developer>();
        //
        // Plugin-Developers: Ulli Hafner:drulli:                                                                                                         
        //
        String pluginDevelopers = manifestAttributes.getValue("Plugin-Developers");
        if (pluginDevelopers != null) {
            String[] ds = StringUtils.split(pluginDevelopers, ",");
            for (String developer : ds) {
                String[] s = StringUtils.split(developer, ":");
                if (s.length == 2) {
                    //
                    // Ulli Hafner:drulli:
                    //                    
                    developers.add(new Developer(s[0], s[1], null));
                } else if (s.length == 3) {
                    //
                    // Developer Guy:tangjinou:tangjinou@gmail.com
                    //
                    developers.add(new Developer(s[0], s[1], s[2]));
                }
            }
        }

        return developers;
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
        //
        // This is null until we do parent POM parsing
        //
        String groupId = hudsonPluginMavenMetadata.getGroupId();
        //String groupId = "org.jvnet.hudson.plugins";

        String path = groupId.replace('.', '/') + "/" + hudsonPluginMavenPom.getArtifactId() + "/" + getVersion() + "/"
                + hudsonPluginMavenPom.getArtifactId() + "-" + getVersion() + ".hpi";

        return downloadSite + path;
    }

    public String getVersion() {
        return hudsonPluginMavenMetadata.getVersioning().getLatest();
    }

    //
    // This was being pulled out of Confluence, but lets start pulling it from the POM. Specifically from a hudsonTags
    // property:
    //
    // <properties>
    //   <hudsonTags>scm,subversion</hudsonTags>
    // </properties>
    //
    public List<String> getLabels() {
        List<String> lables = new ArrayList<String>();
        String hudsonTags = hudsonPluginMavenPom.getProperties().getProperty("hudsonTags");

        if (hudsonTags != null) {
            for (String label : StringUtils.split(hudsonTags, ",")) {
                lables.add(label);
            }
        }

        return lables;
    }

    public String getName() {
        return hudsonPluginMavenPom.getArtifactId();
    }

    public String getRequiredCore() {
        return manifestAttributes.getValue("Hudson-Version");
    }

    public String getWiki() {
        return hudsonPluginMavenPom.getUrl();
    }
}
