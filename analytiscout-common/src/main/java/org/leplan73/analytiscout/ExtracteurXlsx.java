package org.leplan73.analytiscout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.leplan73.analytiscout.calcul.Unite;
import org.leplan73.analytiscout.calcul.Unites;
import org.leplan73.analytiscout.extraction.Adherent;
import org.leplan73.analytiscout.extraction.Adherents;
import org.leplan73.analytiscout.extraction.ColonnesAdherents;
import org.leplan73.analytiscout.extraction.Parents;

public class ExtracteurXlsx {
	
	protected Adherents adherents_;
	protected Parents parents_;
	protected ColonnesAdherents colonnes_;
	protected Unites unites_;
	
	public Adherents getAdherents()
	{
		return adherents_;
	}
	
	public Parents getParents()
	{
		return parents_;
	}
	
	public ColonnesAdherents getColonnes()
	{
		return colonnes_;
	}
	
	public Unites getUnites()
	{
		return unites_;
	}
	
	public List<Unite> getUnitesList() {
		List<Unite> unites = new ArrayList<Unite>();
		unites_.forEach((k,v) -> {
			unites.add(v);
		});
		return unites;
	}
	
	public void charge(final String path, boolean age) throws ExtractionException, IOException
	{
   		FileInputStream excelFile = new FileInputStream(new File(path));
   		charge(excelFile, age);
		excelFile.close();
	}
	
	public void charge(final InputStream stream, boolean age) throws ExtractionException, IOException
	{
        Workbook workbook = new XSSFWorkbook(stream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        
        // Scan des colonnes
        int nbColumns = 0;
        colonnes_ = new ColonnesAdherents();
        Row firstRow = datatypeSheet.getRow(0);
        Iterator<Cell> firstCellIterator = firstRow.iterator();
        while (firstCellIterator.hasNext()) {
            Cell currentCell = firstCellIterator.next();
            String cell = currentCell.getStringCellValue();
            colonnes_.add(nbColumns++, cell);
        }
        
        // Calcul des codes
        colonnes_.calculCodes();
        
        // Chargement des lignes d'adherents
        adherents_ = new Adherents();
        String groupe = null;
        Iterator<Row> iterator = datatypeSheet.iterator();
        int nbLignes = 0;
		while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Adherent adherent = new Adherent(colonnes_);
        	int index = 0;
        	for (int i=0;i<nbColumns;i++)
        	{
        		Cell currentCell = currentRow.getCell(i);
        		if (currentCell != null)
        		{
        			 if (currentCell.getCellType() == CellType.STRING) {
                     	adherent.add(index, currentCell.getStringCellValue());
                     } else if (currentCell.getCellType() == CellType.NUMERIC) {
                     	adherent.add(index, String.valueOf(currentCell.getNumericCellValue()));
                     }
        		}
                index++;
        	}
        	if (nbLignes != 0)
        	{
            	adherent.init(age);
				if (adherent.getFonction() == Consts.CODE_VIOLETS)
				{
					groupe = adherent.getUnite();
				}
        		adherents_.put(adherent.getCode(), adherent);
        	}
            nbLignes++;
        }
        workbook.close();
        
		parents_ = adherents_.parents(colonnes_);
		parents_.complete();
		
		unites_ = new Unites();
		adherents_.forEach((code,ad) ->
		{
			String unite = ad.getUnite();
			Unite uniteObj = unites_.computeIfAbsent(unite, k -> new Unite(unite, ad.getCodestructure(), ad.getFonction()));
			uniteObj.ajouter(ad.getJeune(), ad.getChef());
			
			if (ad.getBranche().compareTo(ad.getBrancheanneeprochaine()) != 0) {
				// Changement de branche
				uniteObj.ajouterMontees();
			}
		});
	}
}
