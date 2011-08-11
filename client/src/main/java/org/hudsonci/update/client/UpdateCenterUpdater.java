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

package org.hudsonci.update.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.hudsonci.update.client.model.Plugin;
import org.hudsonci.update.client.model.UpdateSiteMetadata;

/**  
 * Updater that updates Hudson update-center either with newer version of
 * plugins from Jenkins or Maven Nexus
 * @author Winston Prakash
 */
public class UpdateCenterUpdater {

    public final String jenkinsUpdateCenterURL = "http://updates.jenkins-ci.org/update-center.json";
    private final String hudsonWebFolder = "/home/hudson/public_html/";
    //private final String hudsonWebFolder = "/Users/winstonp/Downloads/";
    private final String pluginsFolder = hudsonWebFolder + "downloads/plugins";
    private final String updatableJenkisPluginsList = hudsonWebFolder + "UpdatableJenkinsPlugins.lst";
    private static final String newUpdatesBaseUrl = "http://us3.maven.org:8085/rest";
    private static final String jvnet_groupid = "org.jvnet.hudson.plugins";
    private static final String hudsonci_groupid = "org.hudsonci.plugins";
    public final String hudsonUpdateCenter = hudsonWebFolder + "update-center.json";
    private final String pluginsDownloadUrl = "http://hudson-ci.org/downloads/plugins/";
    private UpdateSiteMetadata hudsonUpdateSiteMetadata;
    private boolean foundUpdates = false;

    public UpdateCenterUpdater() throws IOException {
        StringBuilder StringBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(hudsonUpdateCenter));
        String str;
        while ((str = in.readLine()) != null) {
            StringBuilder.append(str);
            StringBuilder.append("\n");
        }
        in.close();
        String json = StringBuilder.toString();

        if (json.startsWith("updateCenter.post(")) {
            json = json.substring("updateCenter.post(".length());
        }

        if (json.endsWith(");")) {
            json = json.substring(0, json.lastIndexOf(");"));
        }
        hudsonUpdateSiteMetadata = UpdateCenterUtils.parse(json);
    }

    public void checkForNewUpdates(String groupid) throws IOException {
        System.out.println("Checking for updates at maven central (groupid: " + groupid);
        UpdateSiteMetadata hudsonNewUpdates = UpdateCenterUtils.getNewUpdates(newUpdatesBaseUrl, groupid);

        for (String pluginName : hudsonNewUpdates.getPlugins().keySet()) {
            Plugin newPlugin = hudsonNewUpdates.findPlugin(pluginName);
            Plugin hudsonPlugin = hudsonUpdateSiteMetadata.findPlugin(pluginName);
            if ((hudsonPlugin == null) || newPlugin.isNewerThan(hudsonPlugin)) {
                update(hudsonPlugin, newPlugin, false);
            }
        }
    }

    public void checkForJenkinsUpdates() throws IOException {
        System.out.println("Checking for updates at Jenkins Update Center.");
        UpdateSiteMetadata jenkinsUpdateSiteMetadata = UpdateCenterUtils.parseFromUrl(jenkinsUpdateCenterURL);

        List<String> updatablePlugins = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(updatableJenkisPluginsList));
        String updatablePlugin;
        while ((updatablePlugin = in.readLine()) != null) {
            updatablePlugins.add(updatablePlugin);
        }
        in.close();

        for (String pluginName : jenkinsUpdateSiteMetadata.getPlugins().keySet()) {
            if (updatablePlugins.contains(pluginName)) {
                Plugin jenkinsPlugin = jenkinsUpdateSiteMetadata.findPlugin(pluginName);
                Plugin hudsonPlugin = hudsonUpdateSiteMetadata.findPlugin(pluginName);

                if ((hudsonPlugin == null) || jenkinsPlugin.isNewerThan(hudsonPlugin)) {
                    update(hudsonPlugin, jenkinsPlugin, true);
                }
            }
        }
    }

    private void update(Plugin oldPlugin, Plugin newPlugin, boolean isJenkins) throws IOException {
        foundUpdates = true;

        File pluginFolder = new File(pluginsFolder, newPlugin.getName());
        File pluginVersionFolder = new File(pluginFolder, newPlugin.getVersion());
        pluginVersionFolder.mkdirs();
        UpdateCenterUtils.downloadFile(newPlugin.getUrl(), new File(pluginVersionFolder, newPlugin.getName() + ".hpi"));
        newPlugin.setUrl(pluginsDownloadUrl + newPlugin.getName() + "/" + newPlugin.getVersion() + "/" + newPlugin.getName() + ".hpi");
        if (isJenkins) {
            newPlugin.setWiki(newPlugin.getWiki().replaceAll("jenkins", "hudson").replaceAll("JENKINS", "HUDSON"));
        }
        if (oldPlugin == null) {
            System.out.println("New Plugin found - " + newPlugin.getName() + "(" + newPlugin.getVersion() + ")");
            hudsonUpdateSiteMetadata.add(newPlugin);
        } else {
            System.out.println("Newer version available for \"" + newPlugin.getName() + "\" Current: " + oldPlugin.getVersion() + " New: " + newPlugin.getVersion());
            hudsonUpdateSiteMetadata.replacePlugin(oldPlugin, newPlugin);
        }
    }

    public void persistJson() throws IOException {
        if (foundUpdates) {
            String newJson = UpdateCenterUtils.getAsString(hudsonUpdateSiteMetadata);
            newJson = "updateCenter.post(" + newJson + ");";
            System.out.println(newJson);
            File newUpdateCenter = new File(hudsonWebFolder + "update-center_new.json");
            BufferedWriter out = new BufferedWriter(new FileWriter(newUpdateCenter));
            out.write(newJson);
            out.close();
            File oldUpdateCenter = new File(hudsonUpdateCenter);
            if (!oldUpdateCenter.delete()) {
                throw new IOException("Failed to delete " + hudsonUpdateCenter);
            }

            if (!newUpdateCenter.renameTo(oldUpdateCenter)) {
                throw new IOException("Failed to rename " + newUpdateCenter.getName() + " to " + oldUpdateCenter.getName());
            }
        } else {
            System.out.println("\n\nNo new updates found!\n\n");
        }
    }

    public static void main(String[] args) throws IOException {

        UpdateCenterUpdater updateCenterUpdater = new UpdateCenterUpdater();
        updateCenterUpdater.checkForJenkinsUpdates();
        updateCenterUpdater.persistJson();
        updateCenterUpdater.checkForNewUpdates(jvnet_groupid);
        updateCenterUpdater.persistJson();
        updateCenterUpdater.checkForNewUpdates(hudsonci_groupid);
        updateCenterUpdater.persistJson();
    }
}
