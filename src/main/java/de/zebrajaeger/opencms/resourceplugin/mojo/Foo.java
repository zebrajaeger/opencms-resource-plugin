package de.zebrajaeger.opencms.resourceplugin.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Created by lars on 12.02.2017.
 */
@Mojo(name="Foo", defaultPhase = LifecyclePhase.CLEAN, requiresProject = false)
public class Foo extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("FFFOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }
}
