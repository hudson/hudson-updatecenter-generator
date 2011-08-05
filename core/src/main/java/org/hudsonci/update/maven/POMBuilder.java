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

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelBuildingException;

public interface POMBuilder
{
    public Model build( File file )
        throws Exception;
}
