package org.leplan73.outilssgdf.cmd;

import java.io.File;

import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineAnalyseurRegistreDePresence;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseurregistredepresence", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurRegistreDePresence extends CommonParamsG {

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_registrepresence.xlsx");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;

	@Option(names = "-annee", description = "Fichier Année \"N\"")
	protected File annee;
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		Logging.logger_.info("Lancement");
		chargeParametres();

		try {
			check();
			CmdProgress progress = new CmdProgress();
			EngineAnalyseurRegistreDePresence en = new EngineAnalyseurRegistreDePresence(progress, Logging.logger_);
			en.go(annee, modele, sortie);
		} catch (Exception e) {
			Logging.logger_.error(Logging.dumpStack(null, e));
		}
	}
}
