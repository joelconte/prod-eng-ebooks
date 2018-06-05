/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.ia.controller;

import java.io.FileReader;
import java.net.URL;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

@Controller("localitySearchController")
public class LocalitySearchController implements MessageSourceAware{
	 
	private String identSession = null;
	
	private BookService bookService;
	private MessageSource messageSource;

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	public LocalitySearchController(BookService bookService) {
		this.bookService = bookService;
		
		
	}
	
	 
	@RequestMapping(value="ia/localitySearch", method=RequestMethod.GET)
	public String displayLocalitySearch( Model model, Locale locale) {
		 
		model.addAttribute("pageTitle", messageSource.getMessage("ia.localitySearch", null, locale));
	 
		return "ia/localitySearch";
	} 


	//get locality data for dropdowns - used with button "locality1" and dropdowns selections
	@RequestMapping(value="ia/localitySearch",  method=RequestMethod.POST)
	public  HttpEntity<byte[]> displayLocalitySearchPost( String localityNumber, String id, HttpServletRequest req, Locale locale, Principal principal ) {		
		//first get sessionid to Place api
		doSessionToIdent(req);
		
		//byte[] documentBody = "hello1".getBytes();
		byte[] documentBody = null;
		String localityStr = null;
		if(localityNumber.equals("1")){
			documentBody = getLocality1().getBytes();
		}else{
			localityStr =  getLocalityX(localityNumber, id);
			documentBody = localityStr.getBytes();
			//String localityStr2 =  new String(documentBody);
			//System.out.println(localityStr2);
		}
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "xml"));
		header.setContentLength(documentBody.length);
		return new HttpEntity<byte[]>(documentBody, header);
	 
	}  
	
	private void doSessionToIdent(HttpServletRequest req) {
		if(this.identSession==null) {
			Properties p = new Properties();
			String identPimil = null;
			try{
				String workingDir = req.getServletContext().getRealPath("/");
				
	            p.load(new FileReader(workingDir + "WEB-INF/BookScan.properties"));//put in same dir as executable jar or inside root project in netbeans
	            identPimil = p.getProperty("internetArchiveSearch.identPimil");
	        }catch(Exception e){
	            //JOptionPane.showMessageDialog(this, "Error opening jt.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	            //JOptionPane.showMessageDialog(this, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
	        	System.out.println("Error, cann't get access to BookScan.properties file.");
	        }
			
			this.logonIdent(identPimil);
		}
	}

    //get session id for identity rest services
    public String logonIdent(String identPimil) {                                            
        
        
        try{
            //https://familysearch.org/platform/places/search
            //https://beta.familysearch.org/platform/places/search
            String uri = "https://ident.familysearch.org/cis-public-api/v4/init?key=" + identPimil;

            URL url = new URL(uri);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            java.io.InputStream istr = connection.getInputStream();
           
       /*return from ident 
       <?xml version="1.0" encoding="UTF-8"?>
<ns5:identity xmlns:ns5="http://api.familysearch.org/identity/v4" xmlns:ns2="http://api.familysearch.org/identity/" xmlns:ns3="http://api.familysearch.org/identity/v3" xmlns:ns4="http://api.familysearch.org/identity/v4/findmrn">
   <session developerKey="xxxx-N4RZ-GHHQ-QD9P-GT4Q-ZNX8-yyyy-xxxx" id="USYSC826DB32D3282AEE031F5AFA213A36E0_idses-prod02.a.fsglobal.net" ipAddress="174.23.96.231">
      <assuranceData>
         <level>1.1</level>
         <protocol>cisApi</protocol>
         <provider>cis</provider>
      </assuranceData>
      <values />
   </session>
   <users />
</ns5:identity>
       */
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
             
            db = dbf.newDocumentBuilder();
            org.xml.sax.InputSource is = new  org.xml.sax.InputSource(istr);
            //is.setCharacterStream(new StringReader(xml));
             
            Document doc = db.parse(is);
         
            Node n = doc.getFirstChild();  
            NodeList nl = n.getChildNodes();
            n = nl.item(0);
            NamedNodeMap nnm = n.getAttributes();
            n = nnm.getNamedItem("id");             
            this.identSession = n.getNodeValue();
         
            istr.close();
            
            return null;//ok
        }
        catch (Exception e){
            System.out.println(e.toString());
            System.out.println("error logging into PLACE api. \n\n"+ e.toString());
            return "error";
        }
    }      
    
    //types in first dropdown
    public String getLocality1( ) {                                            
    	Map<String,String> localityMap = new HashMap<String,String>();
         
        try{
            //https://familysearch.org/platform/places/search
            //https://beta.familysearch.org/platform/places/search
            String uri = "https://api.familysearch.org/platform/places/types"  + "?access_token=" + identSession;

            URL url = new URL(uri);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            java.io.InputStream istr = connection.getInputStream();
            
        ///////////////////////tmp 
        /*Scanner s = new Scanner(istr);
        String xmlStr = "";
        while (s.hasNext()) {
            xmlStr += s.nextLine() + "\r\n";
        }
        System.out.println(xmlStr); */
        ///////////////////////////
        
       /*return from places
    
            
<RDF xmlns="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns...>
  <Description about="htt1xxCCDE_idses-prod04.a.fsglobal.net">
    <type resource="http://www.w3.org/1999/02/22-rdf-syntax-ns#Seq"/>
    <dc:title>Place Types</dc:title>
    <dc:description>List of available place types.</dc:description>
    <dc:hasPart parseType="Collection">
      <Description about="https://familysearch.org/platform/places/types/143"/>
      <Description about="https://familysearch.org/platform/places/types/144"/>...
    </dc:hasPart>
    <li>
      <Description about="https://familysearch.org/platform/places/types/143">
        <dc:identifier>143</dc:identifier>
        <rdfs:label xml:lang="en">Aboriginal Council</rdfs:label>
        <rdfs:comment xml:lang="en">A political jurisdiction in countries with native populations such as Australia.</rdfs:comment>
      </Description>
    </li>
    <li>...
       */
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
             
            db = dbf.newDocumentBuilder();
            org.xml.sax.InputSource is = new  org.xml.sax.InputSource(istr);
            //is.setCharacterStream(new StringReader(xml));
             
            Document doc = db.parse(is);
         
            Node n = doc.getFirstChild();  //<RDF tag
            NodeList nl = n.getChildNodes();
            n = getNodeByName(nl, "Description");//<Descriptin tag
            nl = n.getChildNodes();
            int index = 0;
            while(true){
                //actual types are in <li> tags
                index = getNodeByNameStartAtIndex(nl, "li", index);
                if(index == -1)
                    break;
                n = nl.item(index);//<li tag
                NodeList nl2 = n.getChildNodes();
                n = getNodeByName(nl2, "Description");//<Description tag
                nl2 = n.getChildNodes();
                String id = null;
                String label = null;
                n = getNodeByName(nl2, "rdfs:label");
                label = n.getTextContent();
                n = getNodeByName(nl2, "dc:identifier");
                id = n.getTextContent();
                 
                localityMap.put(label, id);
                index++;
            }
            
            String[] elems = new String[localityMap.size()+1];

            localityMap.keySet().toArray(elems);
            elems[localityMap.size()] = "---select an option---";
            Arrays.sort(elems);
            
            //put into xml to send to client
            StringBuilder sbXml = new StringBuilder();
            sbXml.append("<localities>");
            for(int i = 0; i < localityMap.size()+1; i++) {
            	String id = localityMap.get(elems[i]);
            	if(id==null)
            		id = "noid";
            	sbXml.append("<locality id='" + id + "' value='" + elems[i] + "'></locality>");
            }
            sbXml.append("</localities>");
            istr.close();
   
            ///xxxx
      
          //  return "<localities><locality id='noid' value='select an option'>xx</locality><locality id='143' value='Aboriginal Council'>xx</locality><locality id='144' value='Administrative Area'>xx</locality><locality id='145' value='Administrative District (German Kreishauptmannschaft)'>xx</locality><locality id='395' value='Administrative District (German Oberamt)'>xx</locality><locality id='146' value='Administrative District (German Regierungsbezirk)'>xx</locality><locality id='396' value='Administrative District (German Verwaltungsbezirk)'>xx</locality><locality id='147' value='Administrative Region'>xx</locality><locality id='148' value='Administrative Subdivision'>xx</locality><locality id='149' value='Administrative Territory'>xx</locality><locality id='1' value='Airport'>xx</locality><locality id='150' value='Ancient Parish'>xx</locality><locality id='151' value='Anglican Cathedral'>xx</locality><locality id='152' value='Anglican Chapelry'>xx</locality><locality id='153' value='Anglican Deanery'>xx</locality><locality id='154' value='Anglican Diocese'>xx</locality><locality id='155' value='Anglican Parish'>xx</locality><locality id='156' value='Area'>xx</locality><locality id='157' value='Area Outside Territorial Authority'>xx</locality><locality id='158' value='Atoll'>xx</locality><locality id='168' value='Autonomous Banner'>xx</locality><locality id='159' value='Autonomous City'>xx</locality><locality id='160' value='Autonomous Community'>xx</locality><locality id='161' value='Autonomous County'>xx</locality><locality id='162' value='Autonomous District'>xx</locality><locality id='163' value='Autonomous Province'>xx</locality><locality id='164' value='Autonomous Region'>xx</locality><locality id='165' value='Autonomous Republic'>xx</locality><locality id='166' value='Autonomous Territorial Unit'>xx</locality><locality id='167' value='Banner'>xx</locality><locality id='169' value='Baptist Congregation'>xx</locality><locality id='170' value='Barony'>xx</locality><locality id='3' value='Basin'>xx</locality><locality id='4' value='Battlefield'>xx</locality><locality id='5' value='Bay'>xx</locality><locality id='6' value='Beach'>xx</locality><locality id='7' value='Bench'>xx</locality><locality id='95' value='Border Post'>xx</locality><locality id='171' value='Borough'>xx</locality><locality id='8' value='Boundary Marker'>xx</locality><locality id='9' value='Breakwater'>xx</locality><locality id='10' value='Bridge'>xx</locality><locality id='11' value='Building'>xx</locality><locality id='12' value='Camp'>xx</locality><locality id='13' value='Canal'>xx</locality><locality id='172' value='Canton'>xx</locality><locality id='14' value='Canyon'>xx</locality><locality id='15' value='Cape'>xx</locality><locality id='173' value='Capital City'>xx</locality><locality id='174' value='Capital District'>xx</locality><locality id='175' value='Capital Territory'>xx</locality><locality id='16' value='Caravan Route'>xx</locality><locality id='17' value='Castle'>xx</locality><locality id='18' value='Causeway'>xx</locality><locality id='19' value='Cave'>xx</locality><locality id='20' value='Cemetery'>xx</locality><locality id='177' value='Census Area'>xx</locality><locality id='178' value='Census Division'>xx</locality><locality id='179' value='Cession/Treaty'>xx</locality><locality id='22' value='Channel'>xx</locality><locality id='478' value='Chapelry'>xx</locality><locality id='180' value='Chartered City'>xx</locality><locality id='23' value='Church'>xx</locality><locality id='184' value='Church Province'>xx</locality><locality id='181' value='Church of Ireland Cathedral'>xx</locality><locality id='182' value='Church of Ireland Diocese'>xx</locality><locality id='183' value='Church of Ireland Parish'>xx</locality><locality id='185' value='Circumscription'>xx</locality><locality id='186' value='City'>xx</locality><locality id='385' value='City Association'>xx</locality><locality id='397' value='City Council'>xx</locality><locality id='189' value='City District'>xx</locality><locality id='190' value='City Province'>xx</locality><locality id='191' value='City State'>xx</locality><locality id='187' value='City and Borough'>xx</locality><locality id='188' value='City and County'>xx</locality><locality id='192' value='Civil Parish'>xx</locality><locality id='193' value='Civil Registration District'>xx</locality><locality id='194' value='Civil Registration Sub-District'>xx</locality><locality id='195' value='Clan'>xx</locality><locality id='196' value='Clerical District'>xx</locality><locality id='24' value='Cliff'>xx</locality><locality id='197' value='Cloister District'>xx</locality><locality id='137' value='Colony'>xx</locality><locality id='25' value='Common Area'>xx</locality><locality id='198' value='Commonwealth'>xx</locality><locality id='199' value='Commonwealth District'>xx</locality><locality id='200' value='Commonwealth Nation'>xx</locality><locality id='201' value='Commune'>xx</locality><locality id='202' value='Community'>xx</locality><locality id='203' value='Community Government Council'>xx</locality><locality id='204' value='Confederation'>xx</locality><locality id='205' value='Constituency'>xx</locality><locality id='206' value='Constitutional Province'>xx</locality><locality id='26' value='Continent'>xx</locality><locality id='27' value='Convent'>xx</locality><locality id='207' value='Council Area'>xx</locality><locality id='208' value='Council District'>xx</locality><locality id='209' value='County'>xx</locality><locality id='521' value='County (Top level)'>xx</locality><locality id='210' value='County Borough'>xx</locality><locality id='28' value='Courthouse'>xx</locality><locality id='29' value='Crater'>xx</locality><locality id='212' value='Crown Dependency'>xx</locality><locality id='30' value='Customs House'>xx</locality><locality id='31' value='Dairy'>xx</locality><locality id='32' value='Dam'>xx</locality><locality id='213' value='Deanery'>xx</locality><locality id='214' value='Delegation'>xx</locality><locality id='215' value='Department'>xx</locality><locality id='216' value='Departmental Collective'>xx</locality><locality id='217' value='Dependency'>xx</locality><locality id='33' value='Desert'>xx</locality><locality id='218' value='Diocese'>xx</locality><locality id='219' value='Directly Administered Unit'>xx</locality><locality id='220' value='Disputed Territory'>xx</locality><locality id='221' value='District'>xx</locality><locality id='225' value='District (Arabic Cercle)'>xx</locality><locality id='226' value='District (Arabic Manatik)'>xx</locality><locality id='227' value='District (Arabic Minṭaqâ)'>xx</locality><locality id='228' value='District (Arabic Muderiah)'>xx</locality><locality id='229' value='District (Arabic Qadaa)'>xx</locality><locality id='230' value='District (Arabic Sha`Biyah)'>xx</locality><locality id='238' value='District (Chinese Gongnongqu)'>xx</locality><locality id='239' value='District (Chinese Qu)'>xx</locality><locality id='231' value='District (Danish Herred)'>xx</locality><locality id='232' value='District (Danish Landsdele)'>xx</locality><locality id='233' value='District (Dutch Ambt)'>xx</locality><locality id='234' value='District (Dutch Distrikt)'>xx</locality><locality id='398' value='District (German Amt)'>xx</locality><locality id='399' value='District (German Amtshauptmannschaft)'>xx</locality><locality id='400' value='District (German Bezirksamt)'>xx</locality><locality id='512' value='District (German Stadtteilen)'>xx</locality><locality id='235' value='District (Swedish Härad)'>xx</locality><locality id='236' value='District (Swedish Ting)'>xx</locality><locality id='237' value='District (Turkmen Etrap)'>xx</locality><locality id='222' value='District Council'>xx</locality><locality id='223' value='District Court'>xx</locality><locality id='224' value='District Municipality'>xx</locality><locality id='34' value='Divide'>xx</locality><locality id='240' value='Division'>xx</locality><locality id='534' value='Domain'>xx</locality><locality id='241' value='Domain District'>xx</locality><locality id='242' value='Duchy'>xx</locality><locality id='243' value='Economic Prefecture'>xx</locality><locality id='508' value='Electorate'>xx</locality><locality id='245' value='Emirate'>xx</locality><locality id='246' value='Empire'>xx</locality><locality id='477' value='Episcopal Diocese'>xx</locality><locality id='476' value='Episcopal Parish'>xx</locality><locality id='247' value='Estate'>xx</locality><locality id='248' value='Evangelical Diocese'>xx</locality><locality id='249' value='External Territory'>xx</locality><locality id='250' value='Extra-Parochial'>xx</locality><locality id='21' value='Facility Center'>xx</locality><locality id='36' value='Factory'>xx</locality><locality id='38' value='Farm'>xx</locality><locality id='251' value='Federal City'>xx</locality><locality id='252' value='Federal Dependency'>xx</locality><locality id='253' value='Federal District'>xx</locality><locality id='254' value='Federal Republic'>xx</locality><locality id='255' value='Federal Territory'>xx</locality><locality id='256' value='Federation'>xx</locality><locality id='39' value='Ferry'>xx</locality><locality id='40' value='Field'>xx</locality><locality id='41' value='Fjord'>xx</locality><locality id='42' value='Forest'>xx</locality><locality id='257' value='Forest District'>xx</locality><locality id='43' value='Fort'>xx</locality><locality id='258' value='Free State'>xx</locality><locality id='259' value='French Huguenot'>xx</locality><locality id='44' value='Garden'>xx</locality><locality id='45' value='Gate'>xx</locality><locality id='260' value='Gore'>xx</locality><locality id='261' value='Governorate'>xx</locality><locality id='262' value='Grand Duchy'>xx</locality><locality id='263' value='Grant'>xx</locality><locality id='46' value='Grazing Area'>xx</locality><locality id='264' value='Greek Catholic Parish'>xx</locality><locality id='265' value='Group'>xx</locality></localities>";
            
            return sbXml.toString();//ok
        }
        catch (Exception e){
            //System.out.println(e.toString());
            //JOptionPane.showMessageDialog(null, "error getting PLACE data. \n\n"+ e.toString());
            return "<localities><locality id=\"error\" value=\"error\"></locality></localities>";
        }
    }      
    

    //types in 2 through X dropdown
    public String getLocalityX(String localityNumber, String parentId) {                                            
    	Map<String,String> localityMap = new HashMap<String,String>();
    	String searchKeyword = "parentId:";
    	if(localityNumber.equals("locality2")){
            searchKeyword = "typeId:";
    	}
    	
        try{
            //https://familysearch.org/platform/places/search
            //https://beta.familysearch.org/platform/places/search
            String uri = "https://api.familysearch.org/platform/places/search?q=%2B" + searchKeyword + parentId + "&access_token=" + identSession;

            URL url = new URL(uri);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            java.io.InputStream istr = connection.getInputStream();

            ///////////////////////tmp 
           /* Scanner s = new Scanner(istr);
            String xmlStr = "";
            while (s.hasNext()) {
                xmlStr += s.nextLine() + "\r\n";
            }
            System.out.println(xmlStr); */
            ///////////////////////////
            
           /*return from places
    <feed xmlns:gx="http://gedcomx.org/v1/" xmlns:fs="http://familysearch.org/v1/" xmlns="http://www.w3.org/2005/Atom">
        <subtitle>Results of searching for places.</subtitle>
        <title>Place Search Results</title>
        <entry>
            <content type="application/x-gedcomx-v1+xml">
                <gx:gedcomx>
                    <gx:place type="https://familysearch.org/platform/places/types/337" xml:lang="sk" id="4924">
                        <gx:link rel="description" href="https://familysearch.org/platform/places/description/4924?access_token=USYS46EA18F6226DDA2B4B6D7F06FC983BE4_idses-prod06.a.fsglobal.net"/>
                        <gx:identifier type="http://gedcomx.org/Primary">https://familysearch.org/platform/places/1931134</gx:identifier>
                        <gx:identifier>urn:uuid:420253c4-6048-4969-93ed-2e903b8514d1</gx:identifier>
                        <gx:name xml:lang="en">Banská Bystrica, Slovakia</gx:name>
                        <gx:temporalDescription>
                            <gx:formal>+1996/</gx:formal>
                        </gx:temporalDescription>
                        <gx:latitude>48.5</gx:latitude>
                        <gx:longitude>19.5</gx:longitude>
                        <gx:jurisdiction resource="https://familysearch.org/platform/places/description/145?access_token=USYS46EA18F6226DDA2B4B6D7F06FC983BE4_idses-prod06.a.fsglobal.net" resourceId="145"/>
                        <gx:display>
                            <gx:fullName>Banská Bystrica, Slovakia</gx:fullName>
                            <gx:name>Banská Bystrica</gx:name>
                            <gx:type>Region</gx:type>
                        </gx:display>
                    </gx:place>
                    <gx:place type="https://familysearch.org/platform/places/types/343" xml:lang="en" id="145">
                        <gx:link rel="description" href="https://familysearch.org/platform/places/description/145?access_token=USYS46EA18F6226DDA2B4B6D7F06FC983BE4_idses-prod06.a.fsglobal.net"/>
                        <gx:link rel="place-type" href="https://familysearch.org/platform/places/types/343?access_token=USYS46EA18F6226DDA2B4B6D7F06FC983BE4_idses-prod06.a.fsglobal.net"/>
                        <gx:identifier type="http://gedcomx.org/Primary">https://familysearch.org/platform/places/1927146</gx:identifier>
                        <gx:identifier>urn:uuid:4dcf0fe7-271f-4ce1-978b-841faf1d0e90</gx:identifier>
                        <gx:name>Slovakia</gx:name>
                        <gx:place resource="https://familysearch.org/platform/places/1927146" resourceId="1927146"/>
                    </gx:place>
                </gx:gedcomx>
            </content>
            <id>4924</id>
            <link rel="description" href="https://familysearch.org/platform/places/description/4924?access_token=USYS46EA18F6226DDA2B4B6D7F06FC983BE4_idses-prod06.a.fsglobal.net"/>
            <gx:score>100.0</gx:score>
        </entry>
            
           */
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
             
            db = dbf.newDocumentBuilder();
            org.xml.sax.InputSource is = new  org.xml.sax.InputSource(istr);
            //is.setCharacterStream(new StringReader(xml));
            Document doc = null;
            try {
            	doc = db.parse(is);
            }catch(SAXParseException e)
            {
            	//System.out.println(e);
            	//e.printStackTrace();
            	return "<localities><locality id=\"end\" value=\"---End---\"></locality></localities>";
            }
            Node n = doc.getFirstChild();  //<feed tag
            NodeList nl = n.getChildNodes();
           
            
            int index = 0;
            while(true){
                String id = null;
                String label = null;
                
                index = getNodeByNameStartAtIndex(nl, "entry", index);
                if(index == -1)
                    break;
                n = nl.item(index);//<entry tag
                
                NodeList nl2 = n.getChildNodes();
                n = getNodeByName(nl2, "Content");//<Content tag
                nl2 = n.getChildNodes();
                n = getNodeByName(nl2, "gx:gedcomx");
                nl2 = n.getChildNodes();
                n = getNodeByName(nl2, "gx:place");
                
                NamedNodeMap nnm = n.getAttributes();
                
                id = nnm.getNamedItem("id").getNodeValue();
                
                nl2 = n.getChildNodes();
                n = getNodeByName(nl2, "gx:display");
                nl2 = n.getChildNodes();
                n = getNodeByName(nl2, "gx:name");
             
                
                label = n.getTextContent(); 
                 
               /* if(label.equals("United States")){
                    Map<String,String> m = new HashMap<>();
                    m.put(label, id);
                    String idX = m.get(label);
                    System.out.println("aaaaaaaaaaaaaaaaaaaaa"+idX);
                    
                }*/
                localityMap.put(label, id);
                index++;
            }
                
            
            String[] elems = new String[localityMap.size()+1];

            localityMap.keySet().toArray(elems);
            elems[localityMap.size()] = "---select an option---";
            Arrays.sort(elems);
            
            //put into xml to send to client
            StringBuilder sbXml = new StringBuilder();
            sbXml.append("<localities>");
            for(int i = 0; i < localityMap.size()+1; i++) {
            	String id = localityMap.get(elems[i]);
            	if(id==null)
            		id = "noid";
            	sbXml.append("<locality id='" + id + "' value='" + elems[i] + "'></locality>");
            }
            sbXml.append("</localities>");
            istr.close();
   
            ///xxxx
      
          //  return "<localities><locality id='noid' value='select an option'>xx</locality><locality id='143' value='Aboriginal Council'>xx</locality><locality id='144' value='Administrative Area'>xx</locality><locality id='145' value='Administrative District (German Kreishauptmannschaft)'>xx</locality><locality id='395' value='Administrative District (German Oberamt)'>xx</locality><locality id='146' value='Administrative District (German Regierungsbezirk)'>xx</locality><locality id='396' value='Administrative District (German Verwaltungsbezirk)'>xx</locality><locality id='147' value='Administrative Region'>xx</locality><locality id='148' value='Administrative Subdivision'>xx</locality><locality id='149' value='Administrative Territory'>xx</locality><locality id='1' value='Airport'>xx</locality><locality id='150' value='Ancient Parish'>xx</locality><locality id='151' value='Anglican Cathedral'>xx</locality><locality id='152' value='Anglican Chapelry'>xx</locality><locality id='153' value='Anglican Deanery'>xx</locality><locality id='154' value='Anglican Diocese'>xx</locality><locality id='155' value='Anglican Parish'>xx</locality><locality id='156' value='Area'>xx</locality><locality id='157' value='Area Outside Territorial Authority'>xx</locality><locality id='158' value='Atoll'>xx</locality><locality id='168' value='Autonomous Banner'>xx</locality><locality id='159' value='Autonomous City'>xx</locality><locality id='160' value='Autonomous Community'>xx</locality><locality id='161' value='Autonomous County'>xx</locality><locality id='162' value='Autonomous District'>xx</locality><locality id='163' value='Autonomous Province'>xx</locality><locality id='164' value='Autonomous Region'>xx</locality><locality id='165' value='Autonomous Republic'>xx</locality><locality id='166' value='Autonomous Territorial Unit'>xx</locality><locality id='167' value='Banner'>xx</locality><locality id='169' value='Baptist Congregation'>xx</locality><locality id='170' value='Barony'>xx</locality><locality id='3' value='Basin'>xx</locality><locality id='4' value='Battlefield'>xx</locality><locality id='5' value='Bay'>xx</locality><locality id='6' value='Beach'>xx</locality><locality id='7' value='Bench'>xx</locality><locality id='95' value='Border Post'>xx</locality><locality id='171' value='Borough'>xx</locality><locality id='8' value='Boundary Marker'>xx</locality><locality id='9' value='Breakwater'>xx</locality><locality id='10' value='Bridge'>xx</locality><locality id='11' value='Building'>xx</locality><locality id='12' value='Camp'>xx</locality><locality id='13' value='Canal'>xx</locality><locality id='172' value='Canton'>xx</locality><locality id='14' value='Canyon'>xx</locality><locality id='15' value='Cape'>xx</locality><locality id='173' value='Capital City'>xx</locality><locality id='174' value='Capital District'>xx</locality><locality id='175' value='Capital Territory'>xx</locality><locality id='16' value='Caravan Route'>xx</locality><locality id='17' value='Castle'>xx</locality><locality id='18' value='Causeway'>xx</locality><locality id='19' value='Cave'>xx</locality><locality id='20' value='Cemetery'>xx</locality><locality id='177' value='Census Area'>xx</locality><locality id='178' value='Census Division'>xx</locality><locality id='179' value='Cession/Treaty'>xx</locality><locality id='22' value='Channel'>xx</locality><locality id='478' value='Chapelry'>xx</locality><locality id='180' value='Chartered City'>xx</locality><locality id='23' value='Church'>xx</locality><locality id='184' value='Church Province'>xx</locality><locality id='181' value='Church of Ireland Cathedral'>xx</locality><locality id='182' value='Church of Ireland Diocese'>xx</locality><locality id='183' value='Church of Ireland Parish'>xx</locality><locality id='185' value='Circumscription'>xx</locality><locality id='186' value='City'>xx</locality><locality id='385' value='City Association'>xx</locality><locality id='397' value='City Council'>xx</locality><locality id='189' value='City District'>xx</locality><locality id='190' value='City Province'>xx</locality><locality id='191' value='City State'>xx</locality><locality id='187' value='City and Borough'>xx</locality><locality id='188' value='City and County'>xx</locality><locality id='192' value='Civil Parish'>xx</locality><locality id='193' value='Civil Registration District'>xx</locality><locality id='194' value='Civil Registration Sub-District'>xx</locality><locality id='195' value='Clan'>xx</locality><locality id='196' value='Clerical District'>xx</locality><locality id='24' value='Cliff'>xx</locality><locality id='197' value='Cloister District'>xx</locality><locality id='137' value='Colony'>xx</locality><locality id='25' value='Common Area'>xx</locality><locality id='198' value='Commonwealth'>xx</locality><locality id='199' value='Commonwealth District'>xx</locality><locality id='200' value='Commonwealth Nation'>xx</locality><locality id='201' value='Commune'>xx</locality><locality id='202' value='Community'>xx</locality><locality id='203' value='Community Government Council'>xx</locality><locality id='204' value='Confederation'>xx</locality><locality id='205' value='Constituency'>xx</locality><locality id='206' value='Constitutional Province'>xx</locality><locality id='26' value='Continent'>xx</locality><locality id='27' value='Convent'>xx</locality><locality id='207' value='Council Area'>xx</locality><locality id='208' value='Council District'>xx</locality><locality id='209' value='County'>xx</locality><locality id='521' value='County (Top level)'>xx</locality><locality id='210' value='County Borough'>xx</locality><locality id='28' value='Courthouse'>xx</locality><locality id='29' value='Crater'>xx</locality><locality id='212' value='Crown Dependency'>xx</locality><locality id='30' value='Customs House'>xx</locality><locality id='31' value='Dairy'>xx</locality><locality id='32' value='Dam'>xx</locality><locality id='213' value='Deanery'>xx</locality><locality id='214' value='Delegation'>xx</locality><locality id='215' value='Department'>xx</locality><locality id='216' value='Departmental Collective'>xx</locality><locality id='217' value='Dependency'>xx</locality><locality id='33' value='Desert'>xx</locality><locality id='218' value='Diocese'>xx</locality><locality id='219' value='Directly Administered Unit'>xx</locality><locality id='220' value='Disputed Territory'>xx</locality><locality id='221' value='District'>xx</locality><locality id='225' value='District (Arabic Cercle)'>xx</locality><locality id='226' value='District (Arabic Manatik)'>xx</locality><locality id='227' value='District (Arabic Minṭaqâ)'>xx</locality><locality id='228' value='District (Arabic Muderiah)'>xx</locality><locality id='229' value='District (Arabic Qadaa)'>xx</locality><locality id='230' value='District (Arabic Sha`Biyah)'>xx</locality><locality id='238' value='District (Chinese Gongnongqu)'>xx</locality><locality id='239' value='District (Chinese Qu)'>xx</locality><locality id='231' value='District (Danish Herred)'>xx</locality><locality id='232' value='District (Danish Landsdele)'>xx</locality><locality id='233' value='District (Dutch Ambt)'>xx</locality><locality id='234' value='District (Dutch Distrikt)'>xx</locality><locality id='398' value='District (German Amt)'>xx</locality><locality id='399' value='District (German Amtshauptmannschaft)'>xx</locality><locality id='400' value='District (German Bezirksamt)'>xx</locality><locality id='512' value='District (German Stadtteilen)'>xx</locality><locality id='235' value='District (Swedish Härad)'>xx</locality><locality id='236' value='District (Swedish Ting)'>xx</locality><locality id='237' value='District (Turkmen Etrap)'>xx</locality><locality id='222' value='District Council'>xx</locality><locality id='223' value='District Court'>xx</locality><locality id='224' value='District Municipality'>xx</locality><locality id='34' value='Divide'>xx</locality><locality id='240' value='Division'>xx</locality><locality id='534' value='Domain'>xx</locality><locality id='241' value='Domain District'>xx</locality><locality id='242' value='Duchy'>xx</locality><locality id='243' value='Economic Prefecture'>xx</locality><locality id='508' value='Electorate'>xx</locality><locality id='245' value='Emirate'>xx</locality><locality id='246' value='Empire'>xx</locality><locality id='477' value='Episcopal Diocese'>xx</locality><locality id='476' value='Episcopal Parish'>xx</locality><locality id='247' value='Estate'>xx</locality><locality id='248' value='Evangelical Diocese'>xx</locality><locality id='249' value='External Territory'>xx</locality><locality id='250' value='Extra-Parochial'>xx</locality><locality id='21' value='Facility Center'>xx</locality><locality id='36' value='Factory'>xx</locality><locality id='38' value='Farm'>xx</locality><locality id='251' value='Federal City'>xx</locality><locality id='252' value='Federal Dependency'>xx</locality><locality id='253' value='Federal District'>xx</locality><locality id='254' value='Federal Republic'>xx</locality><locality id='255' value='Federal Territory'>xx</locality><locality id='256' value='Federation'>xx</locality><locality id='39' value='Ferry'>xx</locality><locality id='40' value='Field'>xx</locality><locality id='41' value='Fjord'>xx</locality><locality id='42' value='Forest'>xx</locality><locality id='257' value='Forest District'>xx</locality><locality id='43' value='Fort'>xx</locality><locality id='258' value='Free State'>xx</locality><locality id='259' value='French Huguenot'>xx</locality><locality id='44' value='Garden'>xx</locality><locality id='45' value='Gate'>xx</locality><locality id='260' value='Gore'>xx</locality><locality id='261' value='Governorate'>xx</locality><locality id='262' value='Grand Duchy'>xx</locality><locality id='263' value='Grant'>xx</locality><locality id='46' value='Grazing Area'>xx</locality><locality id='264' value='Greek Catholic Parish'>xx</locality><locality id='265' value='Group'>xx</locality></localities>";
            
            return sbXml.toString();//ok
        }
        catch (Exception e){
            //System.out.println(e.toString());
            //JOptionPane.showMessageDialog(null, "error getting PLACE data. \n\n"+ e.toString());
        	return "<localities><locality id=\"error\" value=\"error\"></locality></localities>";
        }
    }      
    
    private Node getNodeByName(NodeList nl, String name){
        for(int i = 0; i < nl.getLength() ; i++){
            //System.out.println(nl.item(i).getNodeName());
            if(name.equalsIgnoreCase(nl.item(i).getNodeName()))
                return nl.item(i);
        }
        return null;
    }
    
    //returns index of node
    private int getNodeByNameStartAtIndex(NodeList nl, String name, int index){
        for(int i = index; i < nl.getLength() ; i++){
            //System.out.println(nl.item(i).getNodeName());
            if(name.equalsIgnoreCase(nl.item(i).getNodeName()))
                return i;
        }
        return -1;
    }
       
    
    //returns index of node
    private String getNodeStringsByAttr(NodeList nl, String attrName){
        for(int i = 0; i < nl.getLength() ; i++){
            Node n = nl.item(i);
            //System.out.println(n.getNodeName());
            if("str".equalsIgnoreCase(n.getNodeName())){
                NamedNodeMap nnm = n.getAttributes();
                Node n2 = nnm.getNamedItem("name");
                String attr = n2.getNodeValue();
                if(attr.equalsIgnoreCase(attrName)){
                    String val = n.getTextContent();
                    return val;
                }
            }else if("arr".equalsIgnoreCase(n.getNodeName())){
                String concatVal = "";
                NamedNodeMap nnm = n.getAttributes();
                Node n2 = nnm.getNamedItem("name");
                String attr = n2.getNodeValue();
                if(attr.equalsIgnoreCase(attrName)){
                    //concatinate <str> tag contens
                    NodeList nl2 = n.getChildNodes();
                    for(int j = 0; j< nl2.getLength(); j++){
                        Node n3 = nl2.item(j);
                        String val = n3.getTextContent();
                        concatVal += val+ "\n";
                    }
                    return concatVal;
                }
            }
        }
        return "";
    }
    
}