package org.familysearch.prodeng.xmlMetadata.controller;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Controller("XmlMetadataController")
public class XmlMetadataController {
	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public XmlMetadataController(BookService bookService) {
		this.bookService = bookService;
	}
	
	//?	@ResponseBody
	@RequestMapping(value = "xmlMetadata/getTn/{tn}", method = RequestMethod.GET)
	public HttpEntity<byte[]>  getXmlMetadataFile( @PathVariable("tn") String tn, HttpServletRequest req) {

	    byte[] documentBody = getXml(tn);//xml.getBytes();
	    if(documentBody==null)
	    	return null;
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(new MediaType("application", "xml"));
	    header.setContentLength(documentBody.length);
	    return new HttpEntity<byte[]>(documentBody, header);
 
	}

	public byte[] getXml(String tn) {
		try {

			//select title, author, current_date, pages_physical_description, coalesce(b.publish_name , e.publish_name), subject, c.publish_name as language, 
			//publisher_original, publication_type as serial, call_#, filmno, dgsno, property_right, num_of_pages 
			//from book a left outer join site b on (a.owning_institution = b.id ) left outer join site e on (a.scanned_by = e.id )  left outer join  languages c on ( a.language = c.id)  where tn = '1000144';


			String[][] mdValues =  new String[37][2];
			String[][] recordValues = new String[8][2];	//put in id attr names and key inner-html in this array
			boolean pass = bookService.queryXmlMetadataOracle(tn, mdValues, recordValues);
			if(!pass){
				//error not in db
				return null;
			}			 

			Document doc = generateXmlDoc(mdValues, recordValues, mdValues[33][1]);
			doc.setXmlStandalone(true); //remove standalone=no attr at top


			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			//not working transformerFactory.setAttribute("indent-number", 5); 
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //with this I get /r/n which is needed (still no indent though)
			DOMSource source = new DOMSource(doc);
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			StreamResult result=new StreamResult(bos);
			transformer.transform(source, result);
			byte[] array=bos.toByteArray();
			return array;
		}catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}
	
	public Document generateXmlDoc(String[][] mdValues, String[][] recordValues, String fileName){

		for(int x = 0; x<mdValues.length; x++){
			if(mdValues[x][1]==null){
				mdValues[x][1] = "";
			}
			 
		}
		for(int x = 0; x<recordValues.length; x++){
			 
			if(recordValues[x][1]==null){
				recordValues[x][1] = "";
			}
		}
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("lds-sip");
			doc.appendChild(rootElement);

			Element mdElem = doc.createElement("metadata");
			mdElem.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			mdElem.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
			mdElem.setAttribute("xmlns:dcterms", "http:/purl.org/dc/terms/");
			mdElem.setAttribute("xmlns:ldsterms", "http://ldschurch.org/ldsterms/ldsterms.xsd");
			rootElement.appendChild(mdElem);

			//all metadata children
			//ie: <dc:identifier>20996_01</dc:identifier> 
			for(int x = 0; x< mdValues.length; x++){
			 				
				if((mdValues[x][0] != null) && ("".equals(mdValues[x][0]) == false)){
					Element md = doc.createElement(mdValues[x][0]);
					md.appendChild(doc.createTextNode(mdValues[x][1]));
					mdElem.appendChild(md);
				}
			}



			Element dnxElem = doc.createElement("dnx");
			rootElement.appendChild(dnxElem);

			Element sectionElem = doc.createElement("section");
			sectionElem.setAttribute("id", "event");
			dnxElem.appendChild(sectionElem);

			Element recordElem = doc.createElement("record");
			sectionElem.appendChild(recordElem);

			//<key id="eventIdentifierType">Provenance Event</key> 
			for(int x=0; x< recordValues.length; x++){
				if((recordValues[x][0] != null) && ("".equals(recordValues[x][0]) == false)){
					Element keyElem = doc.createElement("key");
					keyElem.setAttribute("id", recordValues[x][0]);//ie  eventIdentifierType
					keyElem.appendChild(doc.createTextNode(recordValues[x][1]));//ie Provenance Event
					recordElem.appendChild(keyElem);
				}
			}

			Element assetpathElem = doc.createElement("assetpath");
			String path = "";
			if(fileName.length()>5){
				path = fileName.substring(0, fileName.length()-4);//remove .pdf
			}
			assetpathElem.appendChild(doc.createTextNode(path));
			rootElement.appendChild(assetpathElem);

			Element accessElem = doc.createElement("access");//nonspc
			accessElem.appendChild(doc.createTextNode("nonspc"));
			rootElement.appendChild(accessElem);

			return doc;

		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return null;
		}

	}

}
