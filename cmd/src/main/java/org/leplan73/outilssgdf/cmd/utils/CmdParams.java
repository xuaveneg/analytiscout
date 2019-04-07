package org.leplan73.outilssgdf.cmd.utils;

import java.io.IOException;
import java.util.List;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Params;

import com.jcabi.manifests.Manifests;

import picocli.CommandLine;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

public class CmdParams implements IVersionProvider {

	@Option(names = "-debug", description = "debug (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean debug = false;
	
	@Option(names = "-log", description = "log (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean log = false;
	
	protected String version_;
	
	public void chargeParametres()
	{
		Logging.logger_.info("Chargement du fichier de paramètres");
		Params.init();
		try
		{
			version_ = Manifests.read("version");
		}
		catch(java.lang.IllegalArgumentException e) {
		}
	}

	@Override
	public String[] getVersion() throws Exception {
		return new String[] {"Version: "+Manifests.read("version"),"Date du build: "+Manifests.read("Build-Time")};
	}
	
	public void run(CommandLine commandLine) throws CmdLineException, IOException, ExtractionException, JDOMException
	{
	}
	
	protected void go(String[] args, Class<?> classn)
	{
		Logging.initLogger(classn, debug);
		try
		{
			CommandLine commandLine = new CommandLine(this);
			List<CommandLine> parsed = commandLine.parse(args);
			if (commandLine.isVersionHelpRequested())
			{
			    commandLine.printVersionHelp(System.out);
			    return;
			}
			if (commandLine.isUsageHelpRequested())
			{
			    commandLine.usage(System.out);
			    return;
			}
	        run(parsed.get(0));
		}
		catch(PicocliException e)
		{
			System.out.println("Erreur : " + e.getMessage());
			CommandLine.usage(this, System.out);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
