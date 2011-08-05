package org.hudsonci.update;

import java.io.File;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.guice.bean.containers.InjectedTestCase;
import org.sonatype.guice.bean.reflect.URLClassSpace;
import org.sonatype.guice.plexus.binders.PlexusAnnotatedBeanModule;
import org.sonatype.guice.plexus.binders.PlexusBindingModule;

import com.google.inject.Binder;

public class HudsonUpdateSiteMetadataGeneratorTest
    extends InjectedTestCase
{
    @Inject
    private HudsonUpdateSiteMetadataGenerator generator;

    @Inject @Named( "${basedir}/src/test/repository" )
    private File repository;

    @Override
    public void configure( Binder binder )
    {
        super.configure( binder );
        binder.install( new PlexusBindingModule( null, new PlexusAnnotatedBeanModule( new URLClassSpace( getClass().getClassLoader() ), null ) ) );        
    }
    
    public void testMetadataGeneration()
        throws Exception
    {
        generator.generate( repository );
    }

    @Override
    public void configure( Properties properties )
    {
        super.configure( properties );

        properties.setProperty( "scanner.parallel.threads", "2" );
    }
}
