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

package org.hudsonci.update.maven;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.resolution.ModelResolver;

@Singleton
@Named("aether")
class AetherPOMBuilder
    implements POMBuilder
{
    private ModelResolver modelResolver;

    private ModelBuilder modelBuilder;

    @Inject
    AetherPOMBuilder( ModelResolver modelResolver )
    {
        this.modelResolver = modelResolver;
        this.modelBuilder = new DefaultModelBuilderFactory().newInstance();
    }

    public Model build( File file )
        throws Exception
    {
        ModelBuildingRequest modelRequest = new DefaultModelBuildingRequest();
        modelRequest.setModelSource( new FileModelSource( file ) );
        modelRequest.setSystemProperties( System.getProperties() );
        modelRequest.setValidationLevel( ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL );
        modelRequest.setLocationTracking( false );
        modelRequest.setProcessPlugins( false );
        modelRequest.setModelResolver( modelResolver );

        ModelBuildingResult modelResult = modelBuilder.build( modelRequest );
        Model model = modelResult.getEffectiveModel();
        return model;
    }

}