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

public class Dependency
{
    private String name;
    private boolean optional = true;
    private String version;
    
    public Dependency( String name, boolean optional, String version )
    {
        this.name = name;
        this.optional = optional;
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public boolean isOptional()
    {
        return optional;
    }

    public String getVersion()
    {
        return version;
    }           
}
