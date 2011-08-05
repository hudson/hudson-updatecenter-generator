package org.hudsonci.update;

import java.io.File;
import java.io.IOException;

public interface HudsonUpdateSiteMetadataGenerator
{
    File generate( File repository )
        throws IOException;
    
    public String getGeneratedJson();
}
