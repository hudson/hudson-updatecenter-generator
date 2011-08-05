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

package org.hudsonci.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;
import org.apache.maven.model.Model;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.hudsonci.update.maven.POMBuilder;
import org.hudsonci.update.model.Core;
import org.hudsonci.update.model.Plugin;
import org.hudsonci.update.model.UpdateSiteMetadata;
import org.slf4j.Logger;
import org.sonatype.sisu.resource.scanner.Listener;
import org.sonatype.sisu.resource.scanner.Scanner;
import org.sonatype.sisu.resource.scanner.scanners.SerialScanner;
import org.hudsonci.update.maven.SimplePOMBuilder;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class DefaultHudsonUpdateSiteMetadataGenerator
        implements HudsonUpdateSiteMetadataGenerator {

    @Inject
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Inject
    @Named("serial")
    private Scanner scanner = new SerialScanner();
    @Inject
    @Named("simple")
    private POMBuilder modelReader = new SimplePOMBuilder();
    private ObjectMapper json = new ObjectMapper();
    private MetadataXpp3Reader metadataReader = new MetadataXpp3Reader();
    private String generatedJson;

    public String getGeneratedJson() {
        return generatedJson;
    }

    public File generate(File repository)
            throws IOException {
        if (repository == null) {
            throw new IllegalArgumentException("The repository to scan for Hudson plugins cannot be null.");
        }

        UpdateSiteMetadata metadata = new UpdateSiteMetadata("default", "http://www.google.com");

        HudsonPluginProcessor listener = new HudsonPluginProcessor(metadata);

        scanner.scan(repository, listener);

        return null;
    }

    class HudsonPluginProcessor implements Listener {

        private UpdateSiteMetadata updateSiteMetadata;

        public HudsonPluginProcessor(UpdateSiteMetadata metadata) {
            updateSiteMetadata = metadata;
        }

        public void onBegin() {
            logger.debug("begin");
        }

        public void onEnterDirectory(File directory) {
            logger.debug("onEnterDirectory: ");
        }

        public void onExitDirectory(File directory) {
            logger.debug("onExitDirectory: " + directory);
        }

        public void onFile(File file) {
            String name = file.getName();
            if (file.getName().endsWith("maven-metadata.xml")) {
                try {
                    Metadata hudsonPluginMavenMetadata = metadataReader.read(new FileInputStream(file));
                    String artifactId = hudsonPluginMavenMetadata.getArtifactId();
                    //if (hudsonPluginMavenMetadata.getArtifactId().equals("hudson-war"); 
                    String latestVersion = hudsonPluginMavenMetadata.getVersioning().getLatest();
                    File latestVersionFolder = new File(file.getParentFile(), latestVersion);
                    File latestVersionPom = new File(latestVersionFolder, artifactId + "-" + latestVersion + ".pom");
                    Model hudsonPluginMavenPom = modelReader.build(latestVersionPom);

                    if (artifactId.equals("hudson-war")) {
                        File latestVersionWar = new File(latestVersionFolder, artifactId + "-" + latestVersion + ".war");
                        if (latestVersionWar.exists()) {
                            // We got the latest war folder, try to get the core info
                            Core core = new Core(hudsonPluginMavenPom, hudsonPluginMavenMetadata);
                            updateSiteMetadata.setCore(core);
                        }
                    } else {

                        File latestVersionHpi = new File(latestVersionFolder, artifactId + "-" + latestVersion + ".hpi");
                        if (latestVersionHpi.exists()) {
                            //We got the latest plugin folder, add the plugin
                            JarFile jarFile = new JarFile(latestVersionHpi);
                            InputStream in = jarFile.getInputStream(jarFile.getEntry("META-INF/MANIFEST.MF"));
                            Attributes hudsonPluginManifestEntries = new Manifest(in).getMainAttributes();

                            Plugin plugin = new Plugin(hudsonPluginMavenPom, hudsonPluginManifestEntries, hudsonPluginMavenMetadata);

                            updateSiteMetadata.addPlugin(plugin);
                        }
                    }

                } catch (IOException e) {
                    logger.error("Error reading HPI MANIFEST.MF.", e);
                } catch (Exception e) {
                    logger.error("Error reading HPI POM.", e);
                }
            }
        }

        public void onEnd() {
            logger.debug("onEnd");
            Writer writer = new StringWriter();
            try {
                json.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
                json.writeValue(writer, updateSiteMetadata);
                generatedJson = writer.toString();
                //System.out.println(generatedJson);
            } catch (JsonGenerationException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) throws IOException{
        File repository = new File("/Users/winstonp/maven2");
        HudsonUpdateSiteMetadataGenerator generator = new DefaultHudsonUpdateSiteMetadataGenerator();
        generator.generate(repository);
    }
}
