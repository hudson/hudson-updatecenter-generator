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
 *
 *******************************************************************************/ 

package org.hudsonci.update.restapi;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.hudsonci.update.DefaultHudsonUpdateSiteMetadataGenerator;
import org.hudsonci.update.HudsonUpdateSiteMetadataGenerator;

/**
 * Hudson Update REST Web Service
 *
 * @author Winston Prakash
 */
@Path("artifacts")
public class ArtifactsResource {

    @Context
    private UriInfo context;
    
    @Context ServletContext servletContext;

    /** Creates a new instance of ArtifactsResource */
    public ArtifactsResource() {
    }

    /**
     * Retrieves representation of an instance of org.hudsonci.update.restapi.ArtifactsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getJson(@QueryParam("path") String artifactPath) throws IOException {
        String artifactsRoot = System.getProperty("artifactsRoot", servletContext.getRealPath("/") + "/repository");
        File repository = new File(artifactsRoot + File.separator + artifactPath);
        System.out.println(repository);
        HudsonUpdateSiteMetadataGenerator generator = new DefaultHudsonUpdateSiteMetadataGenerator();
        generator.generate(repository);
        return generator.getGeneratedJson();
    }
}
