package org.hudsonci.update.maven;

import java.io.File;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelBuildingException;

public interface POMBuilder
{
    public Model build( File file )
        throws Exception;
}
