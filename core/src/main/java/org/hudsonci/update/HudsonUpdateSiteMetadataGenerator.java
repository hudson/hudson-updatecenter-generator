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
import java.io.IOException;

public interface HudsonUpdateSiteMetadataGenerator
{
    File generate( File repository )
        throws IOException;
    
    public String getGeneratedJson();
}
