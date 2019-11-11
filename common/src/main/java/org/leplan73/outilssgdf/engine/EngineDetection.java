package org.leplan73.outilssgdf.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.extraction.Adherent;
import org.leplan73.outilssgdf.extraction.Adherents;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.slf4j.Logger;

public class EngineDetection extends EngineConnecte {

	public class Utilisateur
	{
		public String structure;
		public String email;
	}
	
	public EngineDetection(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	public Utilisateur go(String identifiant, String motdepasse) throws EngineException, LoginEngineException
	{
		Utilisateur utilisateur = new Utilisateur();
		
		start();
		try
		{
			ExtractionAdherents app = new ExtractionAdherents();
			app.init(false);
			
			logger_.info("Connexion");
			progress_.setProgress(20);
			boolean ret = app.login(identifiant, motdepasse);
			if (!ret)
			{
				throw new LoginEngineException("Identifiant ou mot de passe incorrect");
			}
			
			logger_.info("Extraction");
			progress_.setProgress(40);
			String donnees = app.extract(ExtractionIntranet.STRUCTURE_TOUT, false, ExtractionIntranet.TYPE_INSCRIT, true, null, ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE, ExtractionIntranet.CATEGORIE_RESPONSABLE, ExtractionIntranet.DIPLOME_TOUT,ExtractionIntranet.QUALIFICATION_TOUT,ExtractionIntranet.FORMATION_TOUT, ExtractionIntranet.FORMAT_INDIVIDU,false);
			ExtracteurIndividusHtml x = new ExtracteurIndividusHtml();
			x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true,false);
			
			logger_.info("Recherche adhérent");
			progress_.setProgress(60);
			Adherents adherents = x.getAdherents();
			Adherent adherent = adherents.get(Integer.parseInt(identifiant));
			utilisateur.structure = adherent.getCodestructure();
			utilisateur.email = adherent.getEmailPersonnel();
			
			app.close();
			progress_.stop();
		} catch (IOException | JDOMException | ExtractionException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
		
		return utilisateur;
	}
}
