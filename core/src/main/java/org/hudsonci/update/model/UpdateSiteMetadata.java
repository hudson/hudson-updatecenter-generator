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

import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(false)
public class UpdateSiteMetadata
{
    public Core core ;
    public String id = "default";
    public String connectionCheckUrl = "http://www.google.com";
    public Map<String,Plugin> plugins = new HashMap<String,Plugin>();;   
    
    public UpdateSiteMetadata(String id, String connectionCheckUrl )
    {
        this.id = id;
        this.connectionCheckUrl = connectionCheckUrl;
        this.plugins = new HashMap<String,Plugin>();
    }
    
    public void setCore(Core core){
        this.core = core;
    }

    public void addPlugin( Plugin plugin )
    {
        plugins.put( plugin.getName(), plugin );        
    }
}
