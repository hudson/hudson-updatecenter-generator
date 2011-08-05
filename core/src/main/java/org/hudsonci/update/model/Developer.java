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

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(false)
public class Developer {

    private String developerId;
    private String email;
    private String name;

    public Developer(String name, String developerId, String email) {
        this.name = name;
        this.developerId = developerId;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public String getEmail() {
        return email;
    }
}
