package org.leplan73.outilssgdf.intranet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class ExtractionAdherents extends ExtractionIntranet {

	private Integer ddStructure_ = null;
	private Integer tbStructure_ = null;
	private String tbAutoCompleteCode_ = null;
	
	public String extract(int structure, boolean recursif, int type, boolean adherents, String codeFonction, int specialite, int categorie, int diplome, int qualification, int formation, int format, boolean brut) throws ClientProtocolException, IOException, JDOMException
	{
		HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/adherents/ExtraireAdherents.aspx");
		httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
	       
		CloseableHttpResponse response = httpclient.execute(httpget);

       	HttpEntity entity = response.getEntity();
       	String obj = EntityUtils.toString(entity);
	    if (logger_.isDebugEnabled())
	    	logger_.debug(obj);
	    Document doc = Jsoup.parse(obj);
	    viewstate = doc.select("#__VIEWSTATE").first().val();
       	response.close();
       	
    	Map<Integer, Integer> structureMap = new TreeMap<Integer, Integer>();
    	
       	// Extraction des codes structure "dd" internes (visible avec un profile "Groupe")
       	Element ddCodes = doc.selectFirst("select[id=ctl00_MainContent__selecteur__ddStructure]");
       	if (ddCodes != null && ddStructure_ == null)
       	{
       		Integer ddStructure = null;
       		
	       	Elements ddCodes2 = ddCodes.select("option");
	       	for (int i=0;i<ddCodes2.size();i++)
	       	{
	       		Element code = ddCodes2.get(i);
	       		if (code != null)
	       		{
		       		String va = code.text();
		       		if (va.compareTo("Toutes") != 0)
		       		{
			       		va = va.substring(0, va.indexOf(" - "));
			       		String value = code.attributes().get("value");
			       		structureMap.put(Integer.valueOf(va), Integer.valueOf(value));
		       		}
	       		}
	       	}
	       	ddStructure = (structure != ExtractionIntranet.STRUCTURE_TOUT) ? structureMap.get(structure) : null;
	       	ddStructure_ = ddStructure;
       	}
       	
       	if (ddCodes == null && tbStructure_ == null)
       	{
       		Integer tbStructure = null;
       		String tbAutoCompleteCode = null;
       		
       		HttpPost httppostStructures = new HttpPost(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/WebServices/AutoComplete.asmx/GetStructures");
       		httppostStructures.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
       		httppostStructures.addHeader("Content-Type","application/json; charset=UTF-8");
       		httppostStructures.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
       		
       		String query = "{q: \""+structure+"\", id_token: \"undefined\"}";
       		StringEntity JsonEntity = new StringEntity(query);
    	    httppostStructures.setEntity(JsonEntity);
    	    response = httpclient.execute(httppostStructures);
    	    
    	    entity = response.getEntity();
           	obj = EntityUtils.toString(entity);
    	    if (logger_.isDebugEnabled())
    	    	logger_.debug(obj);
           	response.close();
       		
       		Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(obj);
       		if (jsonDocument != null)
       		{
       			tbAutoCompleteCode = JsonPath.read(jsonDocument,"$.d");
       			jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(tbAutoCompleteCode);
       			tbAutoCompleteCode_ = tbAutoCompleteCode;
       			try
       			{
       				Object nodeId = JsonPath.read(jsonDocument,"$.[0].id");
       				structureMap.put(structure, Integer.valueOf(nodeId.toString()));
       			}
       			catch(PathNotFoundException ex)
       			{
       				throw ex;
       			}
       		}
       		tbStructure = structure != ExtractionIntranet.STRUCTURE_TOUT ? structureMap.get(structure) : 0;
       		tbStructure_ = tbStructure;
       	}
	       
       	HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/adherents/ExtraireAdherents.aspx");
       	httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
       	httppost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
       	httppost.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
       	List<NameValuePair> formparams = new ArrayList<NameValuePair>();
       	formparams.add(new BasicNameValuePair("__EVENTTARGET","ctl00$MainContent$_btnExporter"));
	   	formparams.add(new BasicNameValuePair("__EVENTARGUMENT",""));
		formparams.add(new BasicNameValuePair("__eo_obj_states","")); 
		formparams.add(new BasicNameValuePair("__eo_sc",""));
		formparams.add(new BasicNameValuePair("__LASTFOCUS","")); 
		formparams.add(new BasicNameValuePair("__VIEWSTATE",viewstate));
		formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","D7E61A64"));
		formparams.add(new BasicNameValuePair("ctl00$_tbRechAdherent",""));
		formparams.add(new BasicNameValuePair("ctl00$_tbRechStructure","")); 
		formparams.add(new BasicNameValuePair("eo_version","11.0.20.2"));
		formparams.add(new BasicNameValuePair("eo_style_keys","/wFk"));
		formparams.add(new BasicNameValuePair("ctl00$_ddDelegations","0"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddlRequetesExistantes","-1"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_tbNomNouvelleRequete",""));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_selecteur$_hidCodeStructure",""+structure));
		if (ddStructure_ != null)
		{
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_selecteur$_ddStructure",""+ ddStructure_));
		}
		if (tbStructure_ != null)
		{
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_selecteur$_tbCode",""+ tbStructure_));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_selecteur$_autocompleteStructures$_txtAutoComplete",""+ tbStructure_));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_selecteur$_autocompleteStructures$_hiddenAutoComplete",tbAutoCompleteCode_));
		}
		if (structure != 0 && recursif)
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbRecursif","on"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_tbCodesFonctions",codeFonction != null ? codeFonction : "")); 
		formparams.add(new BasicNameValuePair("ctl00$MainContent$CodesFonctionsSignifications","_rbCFS_FonctionsPrincipalesEtSecondaires"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddCategorieMembre",""+categorie));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddTypeInscription",""+type));
		if (adherents)
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbSontAdherents", "on"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddSpecialite",""+specialite));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddTypeContact","-1"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddDiplome",""+diplome));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddQualification",""+qualification));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddFormation",""+formation));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddRevue","-1"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddNpai","tous"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddMailInfoMouv","-1"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddMailInfoExt","-1"));
		if ((format & ExtractionIntranet.FORMAT_INDIVIDU) == ExtractionIntranet.FORMAT_INDIVIDU)
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireIndividu","on"));
		if ((format & ExtractionIntranet.FORMAT_PARENTS) == ExtractionIntranet.FORMAT_PARENTS)
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireParents","on"));
		if ((format & ExtractionIntranet.FORMAT_INSCRIPTION) == ExtractionIntranet.FORMAT_INSCRIPTION)
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireInscription","on"));
		if ((format & ExtractionIntranet.FORMAT_ADHESION) == ExtractionIntranet.FORMAT_ADHESION)
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireAdhesion","on"));
		if ((format & ExtractionIntranet.FORMAT_JS) == ExtractionIntranet.FORMAT_JS)
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireJsInformations","on"));
		if ((format & ExtractionIntranet.FORMAT_SANS_QF) == ExtractionIntranet.FORMAT_SANS_QF)
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbInclureQF","on"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_btnExporter.x","53"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_btnExporter.y","13"));
		formparams.add(new BasicNameValuePair("ctl00$_hidReferenceStatistiqueUtilisation","-1"));
		if (logger_.isDebugEnabled())
		{
			formparams.forEach(k ->
			{
				logger_.debug("Param : " + k.getName() + " -> " + k.getValue());
			});
		}
			  
	    entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
	    httppost.setEntity(entity);
	    response = httpclient.execute(httppost);
	   
	    entity = response.getEntity();
	    obj = EntityUtils.toString(entity);
	    response.close();
	    if (logger_.isDebugEnabled())
	    	logger_.debug(obj);
	    if (brut)
	    {
	    	return obj;
	    }
	    else
	    {
	    	doc = Jsoup.parse(obj);
		    Elements tables = doc.select("table");
		    return tables.html();
	    }
	}
}
