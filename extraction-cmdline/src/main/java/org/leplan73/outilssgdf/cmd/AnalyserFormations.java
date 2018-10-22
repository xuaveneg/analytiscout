package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.fusesource.jansi.AnsiConsole;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.extraction.AdherentFormes;

import net.sf.jett.transform.ExcelTransformer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

@Command(name = "AnalyserFormations", mixinStandardHelpOptions = true, version = "1.0")
public class AnalyserFormations extends CommonParamsG {

	@Option(names = "-batch", required=true, description = "batch")
	private String batch = "";

	@Option(names = "-modele", required=true, description = "modele")
	private String modele = "";

	@Option(names = "-entree", required=true, description = "entree")
	private String entree = "";

	@Option(names = "-formations", description = "formations")
	private String formations = "";

	@Option(names = "-sortie", required=true, description = "sortie")
	private String sortie = "";
	
	public void run()
	{
		Instant now = Instant.now();
		Logging.initLogger(AnalyserFormations.class);
		
		Logging.logger_.info("Lancement");
	    
	    if (debug)
	    {
	    	Logging.enableDebug();
	    }
	    
	    Logging.logger_.info("Chargement du fichier de traitement");
		
		Properties pbatch = new Properties();
		try {
			pbatch.load(new FileInputStream(new File(batch)));

		Map<String, ExtracteurExtraHtml> map = new TreeMap<String, ExtracteurExtraHtml>();
		File fichierAdherents = null;

		File dossierStructure = new File(entree,""+structures);
		dossierStructure.exists();
		
		int index=1;
		for(;;)
		{
			String generateur = pbatch.getProperty("generateur."+index);
			if (generateur == null)
			{
				break;
			}
			
			String nom = pbatch.getProperty("nom."+index,"");
			File fichier = new File(dossierStructure, nom+"."+generateur);

			 Logging.logger_.info("Chargement du fichier \""+fichier.getName()+"\"");
			    
			if (nom.compareTo("tout") == 0)
			{
				fichierAdherents = fichier;
			}
			else
				map.put(nom, new ExtracteurExtraHtml(fichier.getAbsolutePath()));
			index++;
		}

	    Logging.logger_.info("Chargement du fichier \""+fichierAdherents.getName()+"\"");
		 ExtracteurHtml adherents = new ExtracteurHtml(fichierAdherents, map);
		 
		 AdherentFormes chefs = new AdherentFormes();
		 chefs.charge(adherents,map);

		FileOutputStream outputStream = new FileOutputStream(sortie);

	    Logging.logger_.info("Generation du fichier \""+sortie+"\" à partir du modèle \""+modele+"\"");		
		ExcelTransformer trans = new ExcelTransformer();
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("chefs", adherents.getChefsList());
		beans.put("unites", adherents.getUnitesList());
		beans.put("formations", formations);
		Workbook workbook = trans.transform(new FileInputStream(modele), beans);
		workbook.write(outputStream);
		
		outputStream.flush();
		outputStream.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExtractionException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
	
	public static void main(String[] args) {
		AnalyserFormations command = new AnalyserFormations();
		try
		{
			new CommandLine(command).parse(args);
	        command.run();
		}
		catch(PicocliException e)
		{
			System.out.println("Erreur : " + e.getMessage());
			CommandLine.usage(command, System.out, Ansi.ON);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		AnsiConsole.systemUninstall();
    }
}
