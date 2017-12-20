package org.familysearch.prodeng.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.swing.SwingWorker;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InternetArchiveService {


    //states
	
	
	public static String statusSelectBooks ="select books";
	public static String statusOlibDupeCheckBooks ="olib dupe check";//not used yet
	public static String statusVerifyBooks ="verify books";
	public static String statusPreDownloadBooks ="pre-download books";
	//these are part of whole download threads work
	public static String statusDownloadNotStartedBooks ="download not started";
	public static String statusDownloadStartedBooks = "download started";
	public static String statusGenerateMetadata = "generating xml";
	public static String statusCompleteDownloadAndXml = "download and xml complete";
	public static String statusErrorPdfMetadata = "pdf or xml error";
	public static String statusErrorCopyright = "copyright - not public domain";
	
	public static String statusInsertTfdb = "ready to add tfdb";
	public static String statusComplete = "complete";
	public static String statusCompleteRejected = "complete and rejected";//of no interest to familysearch
	//these are part of whole download threads states (all show up on page what shows monitoring)
	public static String[] allDownloadStates = {statusDownloadNotStartedBooks, statusDownloadStartedBooks, statusGenerateMetadata ,statusCompleteDownloadAndXml, statusErrorPdfMetadata, statusErrorCopyright};//just valid states that can be changed from...into another download state
	public static String[] allDownloadStatesExceptComplete = {statusDownloadNotStartedBooks, statusDownloadStartedBooks, statusGenerateMetadata ,statusErrorPdfMetadata , statusErrorCopyright};
	
    private List<String> todoList = new ArrayList();
    //List<String> nogoList = new ArrayList<String>();
   
    volatile int todoListIndex = 0;
    volatile int countLeftThread = 0;
    
    String destDir = "";
    BookService bookService = null;
    
    List<SwingWorker> runningThreadWorkers = null;
 
    public InternetArchiveService(BookService bookService) {
		this.bookService = bookService;
		runningThreadWorkers = new ArrayList<SwingWorker>();
	}
    
	//do WGETs of actual pdf and metadata
    //since code was initially in swing, just reuse it's threading system...no problem
	public void doInternetArchiveWorkingBooksDownloadWgetAndXml(HttpServletRequest req, String user){
		
		String dirDate = new SimpleDateFormat("yyyy-MM-dd__hh-mm-ss").format(new Date());
		Properties p = new Properties();
		try{
			String workingDir = req.getServletContext().getRealPath("/");

			p.load(new FileReader(workingDir + "WEB-INF/BookScan.properties"));//put in same dir as executable jar or inside root project in netbeans
			destDir = p.getProperty("internetArchiveSearch.destinationDir");
		}catch(Exception e){

			System.out.println("Error, cann't get access to BookScan.properties file.\n" + e.toString());
		}

		final String destDirFinal =   destDir + "/date_" + dirDate + "_publish";//quote if path has space
		final String destDirWget =   destDir + "/date_" + dirDate + "_wget";
		//save itemlist to file
		File f = new File( destDirWget );

		if(f.exists()==false){
			f.mkdir();
		}else{
			deleteFolder(f);
			f.mkdir();
		}
		
		File f2 = new File( destDirFinal );

		if(f2.exists()==false){
			f2.mkdir();
		} 
		
		//get total count to do for printing out status
		//int downloadCount = 0;
		//int downloadCountTotal = 0;
		//List rows = this.getTodoList();
		
		int threadCount = 2;
		countLeftThread = threadCount;
		for(int x = 0; x< threadCount; x++){

			SwingWorker worker = new SwingWorker<String, String>() {
				List<Process> runningProcs = new ArrayList<Process>();//used to stop if needed
				 
				@Override
				public String doInBackground() {

					while(true){
						String bookId = getNextBookToDownload();
						
						
						if(bookId == null){
							countLeftThread--;
							//jt.getDownloadIaStatus().setText( "Download threads still running " + countLeftThread);
							break;
						}
						
						setBookToDownloadStatus(bookId, statusDownloadStartedBooks, destDirWget);
						
						try{
							// WGETs
							//jt.getDownloadIaStatus().setText( downloadCount + "/" + downloadCountTotal + "  Downloading " + bookId);
							System.out.println( "Thread " + Thread.currentThread().getName()  + " internetarchive downloading " + bookId);
							//String wget_command = "wget -r -l 1 -H -nc -np -nH --cut-dirs=1 -e robots=off -P ia_download_working_folder -A .pdf,meta.xml -i ia_download_working_folder/itemlist.txt -B 'http://archive.org/download/'";
							String wget_command = "wget -r -l 1 -H -nc -np -nH --cut-dirs=1 -e robots=off -P " + destDirWget + " -A .pdf,meta.xml 'http://archive.org/download/" + bookId + "'";
							 
							if(destDirWget.startsWith("/")) {
								//running on linux 
								//use sudo since mount is root owner 
								//on linux, use cut-dirs=2 for some reason???
								//also no quote around url since shell is not able to remove them or something like that (when run form commandline, quotes get removed)
								wget_command = "sudo wget -r -l 1 -H -nc -np -nH --cut-dirs=2 -e robots=off -P " + destDirWget + " -A .pdf,meta.xml http://archive.org/download/" + bookId;

							}
							System.out.println(wget_command);
							Process process = Runtime.getRuntime().exec(wget_command);
							runningProcs.add(process);
							java.io.InputStream stderr = process.getErrorStream();
							InputStreamReader isr = new InputStreamReader(stderr);
							BufferedReader br = new BufferedReader(isr);
							String line = null;
							//System.out.println("<ERROR>");
							while ( (line = br.readLine()) != null)
							{
								 
								//System.out.println("Error from WGET: " + line);//prints status...alot
							}
							
							//System.out.println("</ERROR>");
							int exitVal = process.waitFor();
							
							//todo comment out println
							System.out.println("RC from WGET: " + exitVal);
	 				
							//jt.getDownloadIaStatus().setText( "DONE downloading " + bookId  );
							/* int xx = 0;
	                            while(process.isAlive()){
	                                System.out.println( xx++ + " " +process.isAlive());
	                                jt.getDownloadIaStatus().setText( xx++ + " seconds running...");
	                                Thread.sleep(1000);
	                            }
	                            jt.getDownloadIaStatus().setText( xx++ + "done with " + bookId);
	                            System.out.println( xx++ + "done with " + bookId);
							 */
							//now should be in  ia_download_working_folder

							//generate xml metadata
							setBookToDownloadStatus(bookId, statusGenerateMetadata, destDirFinal);
							File wgetFolder = new File(destDirWget + "/" + bookId);
							String err = processFile(bookId, wgetFolder, new File(destDirFinal));//generate xml and move to correct path structures needed for publishing on fs
							if(err!= null && err.equals("already_handled_error")) {
								//noop
							}else if(err != null && !err.equals("")) {
							
								//save status of book to error
								setBookToDownloadStatus(bookId, statusErrorPdfMetadata, destDirFinal);
								 
								bookService.updateInternetArchiveWorkingBooksError( bookId, err);
							}else {
							 
								//delete folder from WGET
								//todo keep for now deleteFolder(wgetFolder);
								
								
								//save status of book done
								setBookToDownloadStatus(bookId, statusCompleteDownloadAndXml, destDirFinal);
	
								///insert into tfdb todo....in next page i think
							}
						}catch (Exception e){
							//JOptionPane.showMessageDialog(null, "Error while performing WGET command to download books from IA.\n" + e.toString());
							System.out.println("Error while performing WGET command to download books from IA.\n" + e.toString());
							e.printStackTrace();

							//save status of book to error
							setBookToDownloadStatus(bookId, statusErrorPdfMetadata, destDirFinal);
							 
							bookService.updateInternetArchiveWorkingBooksError( bookId, e.toString());
						}
					}
					System.out.println("Thread ended - " + Thread.currentThread());
					return "Thread done ";
				}
				//get msg from publish() above
				//For now, just used to update status in text area...no logging since it is above
				@Override
				protected void process(List<String> list) {

					/*for(int x = 0; x<list.size(); x++){
	                        Calendar c = new GregorianCalendar();
	                        java.util.Date now = c.getTime();
	                        String logText = now + " process-->"+x+" " + list.get(x) + "\n";
	                        //log?? no
	                    }*/
					//util.showStatusAll(statusAll);
					
				}
				

				public void stopDownloadProcesses() {
					for(Process p : runningProcs) {
						try {
							System.out.println("Thread: " + Thread.currentThread() + " ---- Stopping IA downlaod process " + p);
							p.destroy();
						}catch(Exception e) {
							System.out.println(e);
							e.printStackTrace();
						}
					}
				}

			};
			runningThreadWorkers.add(worker);
			worker.execute();
		}

	}
 
	public void stopWorkerThreadsAndDownloadProcesses() {
		todoListIndex = 9999999;//hack to just trick loop that we are done.
		for(SwingWorker sw : runningThreadWorkers) {
			try {
				sw.cancel(true);
			}catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
	
	public void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if(files!=null) { //some JVMs return null for empty dirs
			for(File f: files) {
				if(f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

    public void setTodoList( List<String> todoList){
        this.todoList = todoList;
    }
    
    public  List<String> getTodoList(){
        return todoList;
    }
    
/*
    public void setNogoList( List<String> nogoList){
        this.nogoList = nogoList;
    }
    
    public List<String> getNogoList(){
        return nogoList;
    }
    */
    
	private synchronized String getNextBookToDownload(){
		 List<String> rows = getTodoList();
	
		if( todoListIndex >= rows.size()){
			return null;
		}

		String r = rows.get(todoListIndex);
		todoListIndex++;
	
		return r;
	}
	
	private synchronized void setBookToDownloadStatus(String bookId, String status, String folder) {
		 System.out.println("Setting wget status on book: " + bookId + "  to: " + status + "   Folder: " + folder);
		 
		 bookService.updateInternetArchiveWorkingBooksChangeStateDownloadX( bookId, allDownloadStates, status, folder);
		 /*
		 while(i >= 0) {
			 if(todoList.get(i).get(5).equals(bookId)) {
				 if(status.equals(statusDownloading)) {
					 String dirDate = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss").format(new Date());
					 todoList.get(i).set(1, status);
					 todoList.get(i).set(2, dirDate);
				 }else if(status.equals(statusComplete)) {
					 String dirDate = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss").format(new Date());
					 todoList.get(i).set(1, status);
					 todoList.get(i).set(3, dirDate);
				 }else {
					 todoList.get(i).set(1, status);
				 }
				 
				 return;
			 }
			 i--;
		 }*/
	}
	  
	
	 private String processFile(String bookId, File inDir, File outputDir)  {
	        boolean isPublicDomain = false;//temp hault of copy protected books...solution to just separate out into diff folder
	        File origCopyrightProtectedPdf = null;
	        File origCopyrightProtectedSip = null;  
	        
	        String msgs = "";
	        String inNameFolder = inDir.getName(); //name of book folder..use it to rename if files don't match

	        File metaInputFile = null;
	        File pdfInputFile = null;
	        
        
            File[] fInFiles = inDir.listFiles();//pdf and meta.xml
            //find xml
            for (int x = 0; x < fInFiles.length; x++) {
                if (fInFiles[x].getName().equalsIgnoreCase(inNameFolder + "_meta.xml")) {
                    metaInputFile = fInFiles[x];
                    break;
                }
            }

            for (int x = 0; x < fInFiles.length; x++) {
                if (fInFiles[x].getName().endsWith( "_text.pdf")) {
                    pdfInputFile = fInFiles[x];
                    //no warnings in webapp msgs += "WARNING, no pdf, copying _text.pdf to: " + inDir.getAbsolutePath() + "\r\n";
                    break;
                }  
            }

            if (pdfInputFile == null) {
                for (int x = 0; x < fInFiles.length; x++) {
                    if (fInFiles[x].getName().endsWith(".pdf")) {
                        //Spence says that if folder contains .pdf_text, then just rename it and use it
                        //File copy = new File(inDir.getAbsolutePath() + "/" + inNameFolder + ".pdf");//use folder as name model
                        //try{

                          //  this.copyFile(fInFiles[x], copy);
                        //}catch(Exception e){
                           // msgs += "Error copying pdf file " + fInFiles[x].getName() + " to " + copy.getName() + " " +  e.getMessage();  
                        //}
                        pdfInputFile = fInFiles[x];
                        break;
                    }
                }
            }

            //still no matching pdf, check pdf_text
            if (pdfInputFile == null) {
                for (int x = 0; x < fInFiles.length; x++) {
                    if (fInFiles[x].getName().endsWith(".pdf_text")) {
                        //Spence says that if folder contains .pdf_text, then just rename it and use it
                        //File copy = new File(inDir.getAbsolutePath() + "/" + inNameFolder + ".pdf");//use folder as name model
                        //try{

                          //  this.copyFile(fInFiles[x], copy);
                        //}catch(Exception e){
                           // msgs += "Error copying pdf file " + fInFiles[x].getName() + " to " + copy.getName() + " " +  e.getMessage();  
                        //}
                        pdfInputFile = fInFiles[x];
                      //no warnings in webapp msgs += "WARNING, no pdf, copying .pdf_text to: " + inDir.getAbsolutePath() + "\r\n";
                        break;
                    }
                }
            }
            //still no pdf, lastly check for any pdf
            if(pdfInputFile == null){
                 for(int x = 0; x< fInFiles.length; x++){
                    if(fInFiles[x].getName().endsWith(".pdf")){
                        //Spence says that if folder contains .pdf_text, then just rename it and use it
                        //File copy = new File(inDir.getAbsolutePath() + "/" + inNameFolder + ".pdf");//use folder as name model
                        //try{
                            //this.copyFile(fInFiles[x], copy);
                        //}catch(Exception e){
                          //  msgs += "Error copying pdf file " + fInFiles[x].getName() + " to " + copy.getName() + " " +  e.getMessage();
                        //}
                        pdfInputFile = fInFiles[x];
                      //no warnings in webapp msgs += "WARNING, no matching folder and pdf name, copying " + fInFiles[x].getName() + " to " + inDir.getAbsolutePath()+ "\r\n";
                        break;
                    }
                }
            }
      
        
	        //still no input files
	        if(metaInputFile == null){
	            msgs = "Error, source meta.xml missing in: " + inDir.getAbsolutePath();
	            return msgs +"\r\n";
	        }
	        if(pdfInputFile == null){
	            msgs = "Error, source pdf missing in: " + inDir.getAbsolutePath();
	            return msgs+"\r\n";
	        }
	        //use pdfBox  to get pagecount out of pdf
	        //http://pdfbox.apache.org/download.cgi#20x
	        int pageCount = 0; 
	       
            try{
            	  //  PdfReader pdfr = new PdfReader(pdfInputFile.getAbsolutePath());
            	//!!!some large books hangs/blocks when trying to create a pdf doc, so set a 10 second timer
                final File ff = pdfInputFile;
                final PDDocument doc = null;
                final int[] pageCountArray = new int[1];
                
                class RunMe implements Runnable{
                	public void run() {
                		PDDocument doc = null;
                		try{
                			doc = PDDocument.load(ff); 
                			pageCountArray[0] = doc.getNumberOfPages();
                			  if(doc != null)
                  				doc.close();
                			 
                		}catch(IOException ioe ){
                			System.out.println("Error, ignore");
                			
                		} 
                		try{
                			if(doc != null)
                				doc.close();
                		}catch(IOException ioe ){
                			System.out.println("Error, ignore");
                		} 
                	}
                }
                
                
                
                Thread thr = new Thread(new RunMe());
                thr.start();//run in sep thread
                
                try{
                    Thread.sleep(10000); //sleep main thread (for this book) while trying to create doc.  If more than X seconds skip and use pageCount in xml if exists.
                }catch(InterruptedException e){
                    //pageCount = x;// 
                	thr.interrupt();
                }
                
              
                //doc = PDDocument.load(pdfInputFile);
                 
                pageCount = pageCountArray[0];//if not set (0), then code below will not use it and uses imagecount in xml instead
              
            }catch(Exception e){
            	//no warnings in webapp  msgs += "WARNING, cannot get page count from pdf: " + pdfInputFile.getName() + ".  Using count in xml \r\n";             
            }
	       
	        //1.  copy meta file
	        /*
	        try{
	            copyFile(metaInputFile, new File(outputDir.getAbsoluteFile() + "/" + inNameFolder + "_meta.xml"));
	        }catch(Exception e){
	            msgs += "Error while copying file " + metaInputFile.getName() + " " + e.getMessage();
	            return msgs+"\r\n";
	        }*/
	        //2. Copy pdf to new location with no change
	        //create .\dcms\assets\airforceregiste1962wash_0
	        File pdfOutputDir = new File(outputDir.getAbsoluteFile() + "/dcms/");
	        pdfOutputDir.mkdir();
	        pdfOutputDir = new File(pdfOutputDir.getAbsoluteFile() + "/assets/");
	        pdfOutputDir.mkdir();
	      
	        pdfOutputDir = new File(pdfOutputDir.getAbsoluteFile() + "/" + inNameFolder);
	      
	        pdfOutputDir.mkdir();
	        String newPdfFileName = pdfOutputDir.getName();//same as inNameFolder - use in xml output also in case pdf name was updated by code and diff from input metadata
	        pdfOutputDir = new File(pdfOutputDir.getAbsoluteFile()  + "/PRESERVATION_MASTER" );
	        pdfOutputDir.mkdir();
	         
	        File sipOutputDir = new File(outputDir.getAbsoluteFile() + "/dcms/");//all in batch go to same dir
	        sipOutputDir.mkdir();
	        sipOutputDir = new File(sipOutputDir.getAbsoluteFile() + "/metadata/");//all in batch go to same dir
	        sipOutputDir.mkdir();
	        
	    
	        sipOutputDir = new File(sipOutputDir.getAbsoluteFile() + "/ldssip/");//all in batch go to same dir
	        sipOutputDir.mkdir();
	       
	      
	        File pdfOutputFile = new File(pdfOutputDir.getAbsolutePath() + "/" + newPdfFileName +".pdf");
	        origCopyrightProtectedPdf = pdfOutputFile;//save for possible move of file below
	        
	        try{
	            copyFile(pdfInputFile, pdfOutputFile);
	        }catch(Exception e){
	            msgs += "Error while copying file " + pdfInputFile.getName() + " " + e.getMessage();
	            return msgs+"\r\n";
	        }
	        
	        //3.  generate and copy sip xml file R:\Internet Archives\Elder Shaver Downloads\In Process\2016_04_28_IA_Florida_Mar_2016_converted\dcms\metadata\ldssip
	        File sipOutputFile;
	        
	        sipOutputFile = new File(sipOutputDir + "/" + inNameFolder + ".sip.xml");
       
            HashMap<String, String> inTagsValues;

            try {
                inTagsValues = parseInputFile(metaInputFile);
            } catch (Exception e) {
                return e.getMessage() + "\r\n";
            }

            String[][] mdValues = new String[30][2];

            int mdIndex = 0;
            try {

                //special processing of certain xml values
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                String dateStamp = dateFormat.format(cal.getTime());
                String fileName = pdfOutputFile.getName();//pdfInputFile.getName();
                String fileSize = String.valueOf(pdfInputFile.length() / 1024) + " KB";

                addedTagsKeys[2][1] = fileName; //{"ldsterms:filename","3347482.pdf"}
                addedTagsKeys[3][1] = fileSize; //{"ldsterms:filesize","20728.24 KB"}
                addedTagsKeys[14][1] = dateStamp; //{"dc:date",""}
                recordValues[3][1] = dateStamp; // {"eventDateTime",dateStamp},
            
                //Need to also get tag licenseurl and possible-copyright-status tags to check it for public domain status.   
                //Note "usage" field seems to be tied to licenseurl
                String loanStatus = inTagsValues.get("loans__status__status");
                String licenseUrl = inTagsValues.get("licenseurl");
                String propertyRight = inTagsValues.get(translationKeys[7]);//possible-copyright-status tag
                if(loanStatus != null) {
                	//contains tag indicating an on-loan book
                	inTagsValues.put(translationKeys[7], "Protected");
                }else if (propertyRight != null && ( propertyRight.equalsIgnoreCase("NOT_IN_COPYRIGHT")  ||  propertyRight.toLowerCase().contains("public domain") || propertyRight.toLowerCase().contains("is unaware of any copyright restrictions"))) {
                    inTagsValues.put(translationKeys[7], "Public");
                    isPublicDomain = true;
                }else if(licenseUrl!=null && (licenseUrl.toLowerCase().contains("publicdomain") || licenseUrl.toLowerCase().contains("www.usa.gov/government-works"))){
                    inTagsValues.put(translationKeys[7], "Public");
                    isPublicDomain = true;
                }else if(propertyRight == null && licenseUrl == null){
                    inTagsValues.put(translationKeys[7], "Public");
                    isPublicDomain = true;
                }else{
                    //have to use protected if we do not know
                    inTagsValues.put(translationKeys[7], "Protected");
                }
                
                //rem 7.31.17 since found out some ia books are not public inTagsValues.put(translationKeys[7], "Public");//10.22.2016

                if (pageCount > 0) {
                    //count from xml above
                    inTagsValues.put(translationKeys[14], String.valueOf(pageCount));//replace pagecount form xmlfile
                }

                //populate array with translated tag names and values passed in from meta.xml
                mdIndex = 0;
                for (int x = 0; x < translationCount; x++) {

                    String k = translationKeys[x];// input tag key
                    String v = translationValues[x];

                    mdValues[mdIndex][0] = v; //value of output key xml tag
             
                    mdValues[mdIndex][1] = inTagsValues.get(k);//value using input tag key
                   
                    mdIndex++;
                     
                }

                //append addedTag hardcoded tags
                for (int x = 0; x < addedTagsKeys.length; x++) {
                    if(false == arrayContains(mdValues, addedTagsKeys[x][0])){
                        
                        mdValues[mdIndex][0] = addedTagsKeys[x][0];
                        mdValues[mdIndex][1] = addedTagsKeys[x][1];
                        mdIndex++;
                    }
                }

            } catch (Exception e) {
                return msgs + "Error setting metadata xml values of file " + metaInputFile.getName() + "\r\n";
            }

            try {
                Document doc = generateXmlDoc(mdValues, mdIndex, recordValues, sipOutputFile.getName());

                doc.setXmlStandalone(true); //remove standalone=no attr at top

               // FileWriter fw = new FileWriter(sipOutputFile);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                Result output = new StreamResult(sipOutputFile);
                origCopyrightProtectedSip = sipOutputFile;//save for possible move of file below
                
                Source input = new DOMSource(doc);

                transformer.transform(input, output);
            } catch (Exception e) {
                return msgs + e.getMessage() + "\r\n";
            }
      
	        
            try{
                //move files if copy protected
                if (isPublicDomain == false) {
                    //Path pdfPath = origCopyrightProtectedPdf.toPath();
                    String pdfPathStr = origCopyrightProtectedPdf.getAbsolutePath();
                    //Path sipPath = origCopyrightProtectedSip.toPath();
                    String sipPathStr = origCopyrightProtectedSip.getAbsolutePath();

                    pdfPathStr = pdfPathStr.replace("dcms", "dcms-copyprotected");
                    sipPathStr = sipPathStr.replace("dcms", "dcms-copyprotected");
                    File pdfDestFile = new File(pdfPathStr);
                    File sipDestFile = new File(sipPathStr);
                    File f1 = pdfDestFile.getParentFile();
                    f1.mkdirs();
                    File f2 = sipDestFile.getParentFile();
                    f2.mkdirs();
                  
                    origCopyrightProtectedPdf.renameTo(new File(pdfPathStr));
                    origCopyrightProtectedSip.renameTo(new File(sipPathStr));
                     
                    deleteFolder(new File(origCopyrightProtectedPdf.getParentFile().getParentFile().getAbsolutePath()));
                    deleteFolder(new File(origCopyrightProtectedSip.getAbsolutePath()));
                     
                    //!!for now, we don't import non-publicdomain books, so just set flag to False
                    bookService.updateInternetArchiveWorkingBook(bookId , "false", null);
                	bookService.updateInternetArchiveWorkingBookToState(bookId, statusErrorCopyright);
                	bookService.updateInternetArchiveWorkingBooksError(bookId, "Book is not public domain.  It will not be added to TBDB.");
                	return "already_handled_error";
                }

            } catch (Exception e){
                return msgs +  "Error while moving copyprotected files to dcms-copyprotected directory - "
                                + e.getMessage() + "\r\n";
            }
            
	        return msgs;
	    }


	    private void copyFile(File sourceFile, File destFile) throws IOException {
	       
	        //File io large files: http://crunchify.com/java-tips-what-is-the-fastest-way-to-copy-file-in-java/


	        if (!destFile.exists()) {
	            destFile.createNewFile();
	        }

	        FileChannel source = null;
	        FileChannel destination = null;

	        try {
	            source = new FileInputStream(sourceFile).getChannel();
	            destination = new FileOutputStream(destFile).getChannel();
	            destination.transferFrom(source, 0, source.size());
	        } finally {
	            if (source != null) {
	                source.close();
	            }
	            if (destination != null) {
	                destination.close();
	            }
	        }
	    }

	    private HashMap<String,String>  parseInputFile(File in) throws Exception{
	      
	        HashMap<String,String> inputKeyValues = new HashMap<String,String>();
	        try{
	            /*Scanner scan = new Scanner(in);
	            while(true){
	                String txt = scan.nextLine();
	                
	                if(txt.trim().startsWith("<metadata>"))
	                    break;
	            }
	            
	            while(true){
	                String txt = scan.nextLine();
	                if(txt.trim().endsWith("</metadata>"))
	                    break;
	                else {
	                    String[] kv = getKeyValueFromTagLine(txt);
	                    String key = kv[0];
	                    String value = kv[1];
	                    if(inputKeyValues.get(key) == null){
	                        inputKeyValues.put(key,value);
	                    }else{
	                        inputKeyValues.put(key,inputKeyValues.get(key) + "; " + value);
	                    }
	                }         
	            }*/
	            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	            Document doc = dBuilder.parse(in);
	            //doc.getDocumentElement().normalize();
	            
	            NodeList nList = doc.getElementsByTagName("metadata").item(0).getChildNodes();
	            //printNode(doc.getChildNodes());
	            for (int temp = 0; temp < nList.getLength(); temp++) {
	                Node nNode = nList.item(temp);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                    //System.out.println("\nCurrent Element :" + nNode.getNodeName() + " -> " + nNode.getTextContent());
	                    //System.out.println("val->" + nNode.getNodeValue());
	                    Element eElement = (Element) nNode;
	                    inputKeyValues.put(eElement.getNodeName(), eElement.getTextContent());

			}
	            }
	        }catch(Exception e){
	            throw new Exception("Error parsing input meta.xml file: " + in.getName());
	        }
	        return inputKeyValues;
	    }
	    

	    private boolean arrayContains(String[][] mdValues, String v){
	        for(int x = 0; x< mdValues.length; x++){
	            if(v.equalsIgnoreCase(mdValues[x][0]))
	                return true;
	        }
	        return false;
	    }
	    

	    private Document generateXmlDoc(String[][] mdValues, int mdLength, String[][] recordValues, String fileName)throws Exception {

	        for (int x = 0; x < mdLength; x++) {
	            if (mdValues[x][1] == null) {
	                mdValues[x][1] = "";
	            }

	        }
	        for (int x = 0; x < recordValues.length; x++) {

	            if (recordValues[x][1] == null) {
	                recordValues[x][1] = "";
	            }
	        }
	        try {
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
	            for (int x = 0; x < mdLength; x++) {

	                if ((mdValues[x][0] != null) && ("".equals(mdValues[x][0]) == false)) {
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
	            for (int x = 0; x < recordValues.length; x++) {
	                if ((recordValues[x][0] != null) && ("".equals(recordValues[x][0]) == false)) {
	                    Element keyElem = doc.createElement("key");
	                    keyElem.setAttribute("id", recordValues[x][0]);//ie  eventIdentifierType
	                    keyElem.appendChild(doc.createTextNode(recordValues[x][1]));//ie Provenance Event
	                    recordElem.appendChild(keyElem);
	                }
	            }

	            Element assetpathElem = doc.createElement("assetpath");
	            String path = "";
	            if (fileName.length() > 5) {
	                path = fileName.substring(0, fileName.length() - 8);//remove .pdf
	            }
	            assetpathElem.appendChild(doc.createTextNode(path));
	            rootElement.appendChild(assetpathElem);

	            Element accessElem = doc.createElement("access");//nonspc
	            accessElem.appendChild(doc.createTextNode("nonspc"));
	            rootElement.appendChild(accessElem);

	            return doc;

	        } catch (Exception e) {
	            System.out.println(e);
	            e.printStackTrace();
	            throw e;
	        }

	    }

	    private int translationCount = 16; 
	    private String[] translationKeys = {"title","creator","description",
	    "subject","publisher","date","language","possible-copyright-status",
	    "sponsor","contributor",   "mediatype","collection",
	    "call_number","identifier","imagecount","oclc-id"};

	    private String[] translationValues = {"dc:title","dc:creator","dc:description",
	    "dc:subject","dc:publisher","dcterms:created","dc:language","dcterms:accessRights",
	    "ldsterms:owninst","ldsterms:pubdigital","dc:format","dcterms:isPartOf",
	    "ldsterms:callno3","dc:identifier","ldsterms:pagecount","dcterms:oclc"};       
	        
	 
	    private String[][] addedTagsKeys = {{"ldsterms:pubdigital","Internet Archive"},
	        {"dcterms:isPartOf","Family History Archive - Internet Archive"},            
	        {"ldsterms:filename","x"},
	        {"ldsterms:filesize","x KB"},
	        {"dc:rights","https://www.familysearch.org/terms"},
	        {"ldsterms:mdentry","codepending"},
	        {"dc:provenance","Digitization"},
	        {"dcterms:hasVersion","Electronic Reproduction"},
	        {"ldsterms:subinst","FHD"},
	        {"ldsterms:mdentrytool","Internet Archive"},
	        {"dcterms:requires","Internet Connectivity. Worldwide Web browser. Adobe Acrobat reader."},
	        {"dc:source","Manuscript"},  
	        {"dcterms:rightsHolder","Refer to document for copyright information"},
	        {"dc:type","text"},
	        {"dc:date","x"}};
	 
	    private String[][]  recordValues = {{"eventIdentifierType","Provenance Event"},
	            {"eventType","Scan"},
	            {"eventDescription","Creation of image"},
	            {"eventDateTime","x"},
	            {"eventOutcome1","codepending"},
	            {"eventOutcomeDetail1","Original image capture"},
	            {"linkingAgentIdentifierType1","Image Capture"},
	            {"linkingAgentIdentifierValue1","Internet Archive"}};
	     
	       
}
