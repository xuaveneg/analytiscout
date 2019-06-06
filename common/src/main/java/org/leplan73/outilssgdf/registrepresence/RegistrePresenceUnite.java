package org.leplan73.outilssgdf.registrepresence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

public class RegistrePresenceUnite {

	private String nom_;
	private String nom_court_;
	private String structure_;
	private String code_groupe_;
	private String groupe_;
	private List<RegistrePresenceActivite> activites_ = new ArrayList<RegistrePresenceActivite>();
	private boolean animateurs_;
	private boolean jeunes_;
	
	public RegistrePresenceUnite(String nom) {
		nom_ = nom;
		nom_court_ = nom.substring(nom.indexOf(" - ")+3);
		structure_ = nom.substring(0,nom.indexOf(" - "));
		code_groupe_ = structure_.substring(0, structure_.length()-2);
		code_groupe_+="00";
	}
	
	public boolean estGroupe()
	{
		return (structure_.compareTo(code_groupe_) == 0);
	}

	public void complete(String groupe) {
		groupe_ = groupe;
	}
	
	public void exportInfluxDb(String groupe, PrintStream os) {
		String prefix = "activite,unite="+nom_+",unite_court="+nom_court_+",structure="+structure_+",code_groupe="+code_groupe_+",groupe="+groupe;
		activites_.forEach(v -> v.exportInfluxDb(prefix,os));
	}
	
	@Override
	public String toString()
	{
		return nom_ + " / " + activites_.size();
	}

	public String structure() {
		return structure_;
	}

	public String nom() {
		return nom_;
	}

	public String nom_court() {
		return nom_court_;
	}

	public String code_groupe() {
		return code_groupe_;
	}
	
	public List<RegistrePresenceActivite> getActivites()
	{
		return activites_;
	}

	public void charge(CSVRecord record) {
		String nom = record.get(0);
		if (nom.compareTo("Activités") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				RegistrePresenceActivite activite = new RegistrePresenceActivite(record.get(i));
				activites_.add(activite);
			}
		}
		if (nom.compareTo("Date de début et de fin") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				String sdates[] = record.get(i).split("-");
				if (sdates.length == 1)
				{
					String ddate = sdates[0];
					String fdate = ddate;
					activites_.get(i-2).dates(ddate,fdate);
				}
				else if (sdates.length == 2)
				{
			        String ddate = sdates[0];
					String fdate = sdates[1].substring(1);
					activites_.get(i-2).dates(ddate,fdate);
				}	
			}
		}
		if (nom.compareTo("Heure de début et de fin") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				String sdates[] = record.get(i).split("-");
				if (sdates.length == 1)
				{
					String ddate = sdates[0];
					String fdate = ddate;
					activites_.get(i-2).heures(ddate,fdate);
				}
				else if (sdates.length == 2)
				{
			        String ddate = sdates[0];
					String fdate = sdates[1].substring(1);
					activites_.get(i-2).heures(ddate,fdate);
				}
					
			}
		}
		if (nom.compareTo("Volume horaire réel") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				activites_.get(i-2).complete();
			}
		}
		if (nom.compareTo("Volume horaire forfaitaire") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				activites_.get(i-2).complete();
			}
		}
		if (nom.compareTo("Animateurs") == 0)
		{
			animateurs_ = true;
		}
		if (nom.compareTo("Jeunes") == 0)
		{
			animateurs_ = false;
			jeunes_ = true;
		}
		if (nom.compareTo("Total jeunes par activité") == 0)
		{
			jeunes_ = false;
		}
		if (animateurs_)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				activites_.get(i-2).ajoutChef(record.get(0), record.get(i));
			}
		}
		if (jeunes_)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				activites_.get(i-2).ajoutJeune(record.get(0), record.get(i));
			}
		}
	}

	public void genere(List<RegistrePresenceActiviteHeure> activites) {
		activites_.forEach(v -> v.genere(nom_, nom_court_, structure_, code_groupe_, groupe_, activites));
	}
}