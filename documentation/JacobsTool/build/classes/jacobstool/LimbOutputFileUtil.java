/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacobstool;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;



/**
 *
 * Approach we take is to get list of books to process based on list in L:/completed/PDF and then do union with limb db that show complete. (some files may exist in complete, but not be fully completed...theoreticaly)
 * 1 call initWorkTodo.  It populates pdfList and pdfTodoList.
 * 2 call getNext
 */
public class LimbOutputFileUtil {
   /*not used String postLimbSourcePdf;
    String postLimbSourceTiff;
    String postLimbSourceJpeg;
    String postLimbSourceAltoXml;
    String postLimbSourcePreProcessTiff;
    String postLimbDestPdf;
    String postLimbDestTiff;
    String postLimbDestJpeg;
    String postLimbDestAltoXml;
    String postLimbDestPreProcessTiff;
    String postLimbDestArchive; */
    Connection c;
    Connection tfdbC;
    String s3Pimil = null;
      
    //Create duplicate arraylists for each dir that will be processed - this may be overkill since we assume all will be the same
    volatile private Map<String,Object[]> pdfList; //<tn,hotfolder>   //L:\Completed\PDF -->  s3://bookscanning/limbserver/pdfs
    //private List<String> pdfErrorList; //L:\Completed\PDF -->  O:/04PDFReview\!review of PDF
    /*private List<String> jpgList; //L:\Completed\JPEG -->  s3://bookscanning/limbserver/jpegs
    private List<String> altoXmlList; //L:\Completed\ALTO -->  s3://bookscanning/limbserver/altos
    private List<String> tiffList; //L:\Completed\TIFF -->  s3://bookscanning/limbserver/tiffs
    private List<String> archiveList; //L:\Completed\??(pdf and tiff? check with RoS) -->  s3://bookscanning/archive
    private List<String> preProcessTiffList; //L:\HOT_*** -->  s3://bookscanning/limbserver/preprocessed_tiffs
    */
    
    //These are used for work.  Remove entry when complete.
    volatile private List<String> pdfTodoList;
   // private List<String> pdfErrorTodoList;
    /*private List<String> jpgTodoList;
    private List<String> altoXmlTodoList;
    private List<String> tiffTodoList;
    private List<String> archiveTodoList;
    private List<String> preProcessTiffTodoList;
    */
    volatile private Map<String, String> pdfReadyList;
    
    //states of processing
    public final String STATE_0 = "NotStarted";   
    public final String STATE_NEXT = "CHOSEN NEXT";   
    
    public final String STATE_PDF_ERRORCHECK = "PDF-Error Checking";   
    public final String STATE_PDF_ERROR_TRANSFERING = "PDF-Error Transfering";   
    public final String STATE_PDF_ZIPPING = "PDF-Zipping";   
    public final String STATE_PDF_TRANSFERING = "PDF-Transfering";   
    public final String STATE_PDFCOMPLETE_TRANSFERING = "PDFCOMPLETE-Transfering";  
    public final String STATE_PDF_DELETING = "PDF-Deleting";  
    public final String STATE_JPEG_ZIPPING = "JPEG-Zipping";   
    public final String STATE_JPEG_TRANSFERING = "JPEG-Transfering";  
    public final String STATE_JPEG_DELETING = "JPEG-Deleting";
    public final String STATE_TIFF_ZIPPING = "TIFF-Zipping";   
    public final String STATE_TIFF_TRANSFERING = "TIFF-Transfering"; 
    public final String STATE_TIFF_DELETING = "TIFF-Deleting";
    public final String STATE_ALTOXML_ZIPPING = "ALTO-Zipping";   
    public final String STATE_ALTOXML_TRANSFERING = "ALTO-Transfering";  
    public final String STATE_ALTOXML_DELETING = "ALTO-Deleting";
    public final String STATE_ARCHIVE_ZIPPING = "ARCHIVE-Zipping";   
    public final String STATE_ARCHIVE_TRANSFERING = "ARCHIVE-Transfering";  
    public final String STATE_ARCHIVE_DELETING = "ARCHIVE-Deleting"; 
    public final String STATE_PREPROCESSTIFF_ZIPPING = "PREPROCESSTIFF-Zipping";   
    public final String STATE_PREPROCESSTIFF_TRANSFERING = "PREPROCESSTIFF-Transfering";   
    public final String STATE_PREPROCESSTIFF_DELETING = "PREPROCESSTIFF-Deleting";
    public final String STATE_TFDB_UPDATE = "TFDB ocr_complete_date Updating";
    
    public final String STATE_PDF_ERRORCHECK_COMPLETE = "PDF-Error Check COMPLETE";  
    public final String STATE_PDF_ERROR_TRANSFERING_COMPLETE = "PDF-Error Transfering COMPLETE";   
    public final String STATE_PDF_ZIPPING_COMPLETE = "PDF-Zipping COMPLETE";   
    public final String STATE_PDF_TRANSFERING_COMPLETE = "PDF-Transfering COMPLETE";   
    public final String STATE_PDFCOMPLETE_TRANSFERING_COMPLETE = "PDFCOMPLETE-Transfering COMPLETE";  
    public final String STATE_PDF_DELETING_COMPLETE = "PDF-Deleting COMPLETE";   
    public final String STATE_JPEG_ZIPPING_COMPLETE = "JPEG-Zipping COMPLETE";   
    public final String STATE_JPEG_TRANSFERING_COMPLETE = "JPEG-Transfering COMPLETE";   
    public final String STATE_JPEG_DELETING_COMPLETE = "JPEG-Deleting COMPLETE";  
    public final String STATE_TIFF_ZIPPING_COMPLETE = "TIFF-Zipping COMPLETE";   
    public final String STATE_TIFF_TRANSFERING_COMPLETE = "TIFF-Transfering COMPLETE";  
    public final String STATE_TIFF_DELETING_COMPLETE = "TIFF-Deleting COMPLETE";  
    public final String STATE_ALTOXML_ZIPPING_COMPLETE = "ALTO-Zipping COMPLETE";   
    public final String STATE_ALTOXML_TRANSFERING_COMPLETE = "ALTO-Transfering COMPLETE";   
    public final String STATE_ALTOXML_DELETING_COMPLETE = "ALTO-Deleting COMPLETE";
    public final String STATE_ARCHIVE_ZIPPING_COMPLETE = "ARCHIVE-Zipping COMPLETE";   
    public final String STATE_ARCHIVE_TRANSFERING_COMPLETE = "ARCHIVE-Transfering COMPLETE";   
    public final String STATE_ARCHIVE_DELETING_COMPLETE = "ARCHIVE-Deleting COMPLETE"; 
    public final String STATE_PREPROCESSTIFF_ZIPPING_COMPLETE = "PREPROCESSTIFF-Zipping COMPLETE";   
    public final String STATE_PREPROCESSTIFF_TRANSFERING_COMPLETE = "PREPROCESSTIFF-Transfering COMPLETE";   
    public final String STATE_PREPROCESSTIFF_DELETING_COMPLETE = "PREPROCESSTIFF-Deleting COMPLETE";   
    public final String STATE_TFDB_UPDATE_COMPLETE = "TFDB ocr_complete_date COMPLETE";
    
    public final String STATE_PDF_ERRORCHECK_FAILED = "PDF-Error Check FAILED - see log";  
    public final String STATE_PDF_ERROR_TRANSFERING_FAILED = "PDF-Error Transfering FAILED - see log";  
    public final String STATE_PDF_ZIPPING_FAILED = "PDF-Zipping FAILED - see log";   
    public final String STATE_PDF_TRANSFERING_FAILED = "PDF-Transfering FAILED - see log";   
    public final String STATE_PDFCOMPLETE_TRANSFERING_FAILED = "PDFCOMPLETE-Transfering FAILED - see log";   
    public final String STATE_PDF_DELETING_FAILED = "PDF-Deleting FAILED - see log";  
    public final String STATE_JPEG_ZIPPING_FAILED = "JPEG-Zipping FAILED - see log";   
    public final String STATE_JPEG_TRANSFERING_FAILED = "JPEG-Transfering FAILED - see log";  
    public final String STATE_JPEG_DELETING_FAILED = "JPEG-Deleting FAILED - see log";  
    public final String STATE_TIFF_ZIPPING_FAILED = "TIFF-Zipping FAILED - see log";   
    public final String STATE_TIFF_TRANSFERING_FAILED = "TIFF-Transfering FAILED - see log";  
    public final String STATE_TIFF_DELETING_FAILED = "TIFF-Deleting FAILED - see log";  
    public final String STATE_ALTOXML_ZIPPING_FAILED = "ALTO-Zipping FAILED - see log";   
    public final String STATE_ALTOXML_TRANSFERING_FAILED = "ALTO-Transfering FAILED - see log";   
    public final String STATE_ALTOXML_DELETING_FAILED = "ALTO-Deleting FAILED - see log";  
    public final String STATE_ARCHIVE_ZIPPING_FAILED = "ARCHIVE-Zipping FAILED - see log";   
    public final String STATE_ARCHIVE_TRANSFERING_FAILED = "ARCHIVE-Transfering FAILED - see log";   
    public final String STATE_ARCHIVE_DELETING_FAILED = "ARCHIVE-Deleting FAILED - see log";  
    public final String STATE_PREPROCESSTIFF_ZIPPING_FAILED = "PREPROCESSTIFF-Zipping FAILED - see log";   
    public final String STATE_PREPROCESSTIFF_TRANSFERING_FAILED = "PREPROCESSTIFF-Transfering FAILED - see log";  
    public final String STATE_PREPROCESSTIFF_DELETING_FAILED = "PREPROCESTIFF-Deleting FAILED - see log";  
    public final String STATE_TFDB_UPDATE_FAILED = "TFDB ocr_complete_date FAILED - see log";
    
    public final String STATE_COMPLETE = "Done";   
  /*  private String ALTOXML = "ALTOXML";   
    private String TIFF = "TIFF";   
    private String PDF = "PDF";   
    private String PDF = "PDF";   
    private String PDF = "PDF";   
    */
     
    public void setS3Pimil(String s){
        s3Pimil = s;
    }

    //returns map of tn and array of attributes from limbdb [hotfolder,finishdate]
    public Map<String,Object[]> getPdfList() {
        return pdfList;
    }
    //return map of <filename,tn>
    public Map<String, String> getPdfReadyList() {
        return pdfReadyList;
    }
    /*
    public List<String> getJpgList() {
        return jpgList;
    }
    public List<String> getAltoXmlList() {
        return altoXmlList;
    }
    public List<String> getTiffList() {
        return tiffList;
    }
    public List<String> getArchiveList() {
        return archiveList;
    }
    public List<String> getPreProcessTiffList() {
        return preProcessTiffList;
    }
    */
    

    public List<String> getPdfTodoList() {
        return pdfTodoList;
    }
    /*
    public List<String> getJpgTodoList() {
        return jpgTodoList;
    }
    public List<String> getAltoXmlTodoList() {
        return altoXmlTodoList;
    }
    public List<String> getTiffTodoList() {
        return tiffTodoList;
    }   
    public List<String> getArchiveTodoList() {
        return archiveTodoList;
    }
    public List<String> getPreProcessTiffTodoList() {
        return preProcessTiffTodoList;
    }*/

 
    //in case of crash, on app startup, call this to see where we left off for cleanup and reprocessing
    public synchronized Map<String,String> getPdfsCurrentlyWorking() {
        Properties p = new Properties();
        Map<String, String> m = new HashMap<>();
        try{
            p.load(new FileReader("limbOutWorking.properties"));//put in same dir as executable jar or inside root project in netbeans
            Set tns = p.keySet();
            for(String tn: (Set<String>) tns){
                m.put(tn, p.getProperty(tn));
            }
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error opening limbOutWorking.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return m;
    }
    public synchronized String getStatusStringAll(){
        Map<String,String> m = getPdfsCurrentlyWorking();//tn,status
        List<String> todo = getPdfTodoList();
        Map<String,Object[]> all = getPdfList();
        
        for(String tn : todo){
            m.put(tn, STATE_0);
        }
        
        for(String tn : all.keySet()){
            if(!m.containsKey(tn)){
                m.put(tn, STATE_COMPLETE);
            }
        }
        
        String retVal = "";
        Set<String> keys = m.keySet();
        for(String tn : keys){
            String state = m.get(tn);
            retVal += tn + "\t " + state + "\n";
        }
        return retVal;
        
    }
    
    public synchronized void showStatusAll(JTextArea textArea){
        String status = getStatusStringAll();
        textArea.setText(status);
                
    }
/*    public String getJpgCurrentlyWorking() {
        return null;
    }
    public String getAltoXmlCurrentlyWorking() {
        return null;
    }
    public String getTiffCurrentlyWorking() {
        return null;
    }   
    public String getArchiveCurrentlyWorking() {
        return null;
    }
    public String getPreProcessTiffCurrentlyWorking() {
        return null;
    }
*/

    //pass in null to remove current
    //save in case of crash
    public void setPdfCurrentlyWorking(String tn) {
        setProperty(tn, STATE_NEXT);
    }
    /*
    public void setJpgCurrentlyWorking(String tn) {
        setProperty(tn, tn);
    }
    public void setAltoXmlCurrentlyWorking(String tn) {
        return null;
    }
    public void setTiffCurrentlyWorking(String tn) {
        return null;
    }   
    public void setArchiveCurrentlyWorking(String tn) {
        return null;
    }
    public void setPreProcessTiffCurrentlyWorking(String tn) {
        return null;
    }
*/
     
    public synchronized String getNextPdfToProcess(){
        
        if(pdfTodoList.size()==0)
            return null;
        
        String tn = pdfTodoList.get(0);
        setPdfCurrentlyWorking(tn);
        pdfTodoList.remove(0);
        return tn;
    }
    
    
    public synchronized void  clearAllProperty(){
        Properties p = new Properties();
        try{
                        
            p.store(new FileWriter(new File("limbOutWorking.properties")), null);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error opening limbOutWorking.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public synchronized void setProcessingDone(String tn){
        removeProperty(tn);
    }
    
     
    
    private synchronized void  removeProperty(String tn){
        Properties p = new Properties();
        try{
            p.load(new FileReader("limbOutWorking.properties"));//put in same dir as executable jar or inside root project in 
            p.remove(tn);
            p.store(new FileWriter(new File("limbOutWorking.properties")), null);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error opening limbOutWorking.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public synchronized void  setProperty(String key, String val){
        Properties p = new Properties();
        try{
            p.load(new FileReader("limbOutWorking.properties"));//put in same dir as executable jar or inside root project in netbeans
            p.setProperty(key, val);
            p.store(new FileWriter(new File("limbOutWorking.properties")), null);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error opening limbOutWorking.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //returns null if not exist
    private synchronized String getProperty(String key){
        Properties p = new Properties();
        try{
            p.load(new FileReader("limbOutWorking.properties"));//put in same dir as executable jar or inside root project in netbeans
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error opening limbOutWorking.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //set to last tab when ran last
        String val = p.getProperty(key);
        return val;
    }
   
    public String doPdfErrorCheck(String src, String tn){
        setProperty(tn, STATE_PDF_ERRORCHECK);
    
        try{
            src = src + "/" + tn  + "/" + tn + ".pdf";
            if (src.indexOf("\\\\") == -1) {
                src = src.replace("\\", "\\\\");
            }

            File f = new File(src);
            //check for errors (ie 1k size files)
            String ret = null;
            if(f.length() < 1025){
                return "1k size";
            }

        }catch(Exception e){
             setProperty(tn, STATE_PDF_ERRORCHECK_FAILED);
             return e.toString();
        }
        setProperty(tn, STATE_PDF_ERRORCHECK_COMPLETE);
        return null;//no errors
         
    }
    
    public String doPdfErrorHandle(String src, String dest, String tn){
        setProperty(tn, STATE_PDF_ERROR_TRANSFERING);
   
        
        String ret = null;
        try{
            src = src + "/" + tn + "/" + tn + ".pdf";
            dest = dest + "/" + tn + ".pdf";

            File f = new File(src);
            ret = doCopyFile(src, dest);
            
        }catch(Exception e){
            setProperty(tn, STATE_PDF_ERROR_TRANSFERING_FAILED);
            return e.toString();
        }
        if(ret!=null){
            setProperty(tn, STATE_PDF_ERROR_TRANSFERING_FAILED);
        }else{
            setProperty(tn, STATE_PDF_ERROR_TRANSFERING_COMPLETE);
        }
        return ret;
    }
    
    public String doZipPdf(String src, String dest, String tn){
        /*setProperty(tn, STATE_PDF_ZIPPING);
        
        String ret = doZipDir(src, tn);//doS3Transfer(src + "/" + tn, dest, tn + ".pdf");
        if(ret!=null){
            setProperty(tn, STATE_PDF_ZIPPING_FAILED);
        }else{
            setProperty(tn, STATE_PDF_ZIPPING_COMPLETE);
        }
        return ret;*/
        return null;
    }
    
    public String doZipJpeg(String src, String dest, String tn){
        setProperty(tn, STATE_JPEG_ZIPPING);
        
       
        String ret = doZipDir(src + "/" + tn, dest, tn);//doS3Transfer(src + "/" + tn, dest, tn + ".pdf");
        if(ret!=null){
            setProperty(tn, STATE_JPEG_ZIPPING_FAILED);
        }else{
            setProperty(tn, STATE_JPEG_ZIPPING_COMPLETE);
        }
        
        return ret;
    }
    
    public String doZipTiff(String src, String dest, String tn){
        setProperty(tn, STATE_TIFF_ZIPPING);
        
       
        String ret = doZipDir(src + "/" + tn, dest, tn);//doS3Transfer(src + "/" + tn, dest, tn + ".pdf");
        if(ret!=null){
            setProperty(tn, STATE_TIFF_ZIPPING_FAILED);
        }else{
            setProperty(tn, STATE_TIFF_ZIPPING_COMPLETE);
        }
        
        return ret;
    }
    
    public String doZipAltoXml(String src, String dest, String tn){
        setProperty(tn, STATE_ALTOXML_ZIPPING);
        
       
        String ret = doZipDir(src + "/" + tn, dest, tn);//doS3Transfer(src + "/" + tn, dest, tn + ".pdf");
        if(ret!=null){
            setProperty(tn, STATE_ALTOXML_ZIPPING_FAILED);
        }else{
            setProperty(tn, STATE_ALTOXML_ZIPPING_COMPLETE);
        }
        
        return ret;
    }
    
    
    public String doZipPreprocessTiff(String src, String dest, String tn){
        setProperty(tn, STATE_PREPROCESSTIFF_ZIPPING);
        
        
        String ret = doZipDir(src, dest, tn);//doS3Transfer(src + "/" + tn, dest, tn + ".pdf");
        if(ret!=null){
            setProperty(tn, STATE_PREPROCESSTIFF_ZIPPING_FAILED);
        }else{
            setProperty(tn, STATE_PREPROCESSTIFF_ZIPPING_COMPLETE);
        }
        
        return ret;
    }
    
    public String doPdfTransfer(String src, String dest, String tn){
        setProperty(tn, STATE_PDF_TRANSFERING);
        
        
        String ret = doS3Transfer(src + "/" + tn, dest, tn + ".pdf");
        if(ret!=null){
            setProperty(tn, STATE_PDF_TRANSFERING_FAILED);
        }else{
            setProperty(tn, STATE_PDF_TRANSFERING_COMPLETE);
        }
        return ret;
    }
    
    public String doPdfTransferPdfComplete(String src, String dest, String tn, String trimtn){
        setProperty(tn, STATE_PDFCOMPLETE_TRANSFERING);
        
        
        String ret = doCopyFile(src + "/" + tn + "/" + tn + ".pdf", dest + "/" + trimtn + ".pdf" );
        if(ret!=null){
            setProperty(tn, STATE_PDFCOMPLETE_TRANSFERING_FAILED);
        }else{
            setProperty(tn, STATE_PDFCOMPLETE_TRANSFERING_COMPLETE);
        }
        return ret;
    }
    
    public String doJpegTransfer(String src, String dest, String tn){
        setProperty(tn, STATE_JPEG_TRANSFERING);
        
        
        String ret = doS3Transfer(src, dest, tn + ".zip");
        if(ret!=null){
            setProperty(tn, STATE_JPEG_TRANSFERING_FAILED);
        }else{
            setProperty(tn, STATE_JPEG_TRANSFERING_COMPLETE);
        }
        return ret;
    }
    
    public String doTiffTransfer(String src, String dest, String tn){
        setProperty(tn, STATE_TIFF_TRANSFERING);
        
        
        String ret = doS3Transfer(src, dest, tn + ".zip");
        if(ret!=null){
            setProperty(tn, STATE_TIFF_TRANSFERING_FAILED);
        }else{
            setProperty(tn, STATE_TIFF_TRANSFERING_COMPLETE);
        }
        return ret;
    }
    
    public String doAltoXmlTransfer(String src, String dest, String tn){
        setProperty(tn, STATE_ALTOXML_TRANSFERING);
        
        
        String ret = doS3Transfer(src, dest, tn + ".zip");
        if(ret!=null){
            setProperty(tn, STATE_ALTOXML_TRANSFERING_FAILED);
        }else{
            setProperty(tn, STATE_ALTOXML_TRANSFERING_COMPLETE);
        }
        return ret;
    }
     
    
            
    public String doPreprocessTiffTransfer(String src, String dest, String tn){
        setProperty(tn, STATE_PREPROCESSTIFF_TRANSFERING);
        
        
        String ret = doS3Transfer(src, dest, tn + ".zip");
        if(ret!=null){
            setProperty(tn, STATE_PREPROCESSTIFF_TRANSFERING_FAILED);
        }else{
            setProperty(tn, STATE_PREPROCESSTIFF_TRANSFERING_COMPLETE);
        }
        return ret;
    }
             
    public String doDeletePdf(String src, String tn){
        setProperty(tn, STATE_PDF_DELETING);
        
        
        
        String ret = doDeleteFile(src +  "/" + tn + "/" + tn + ".pdf");
        if(ret==null){
             ret = doDeleteFile(src +  "/" + tn);//delete dir also
        }
        
        if(ret!=null){
            setProperty(tn, STATE_PDF_DELETING_FAILED);
        }else{
            setProperty(tn, STATE_PDF_DELETING_COMPLETE);
        }
        return ret;
    }
    
    
             
    public String doDeleteJpeg(String src, String tn){
        setProperty(tn, STATE_JPEG_DELETING);
        
        
        String ret = doDeleteFile(src + "/" + tn + ".zip");
        if(ret==null){
            ret = doDeleteFile(src + "/" + tn);//delete dir also
        }
        
        if(ret!=null){
            setProperty(tn, STATE_JPEG_DELETING_FAILED);
        }else{
            setProperty(tn, STATE_JPEG_DELETING_COMPLETE);
        }
        return ret;
    }
    
             
    public String doDeleteTiff(String src, String tn){
        setProperty(tn, STATE_TIFF_DELETING);
        
        
        String ret = doDeleteFile(src + "/" + tn + ".zip");
        if(ret==null){
            ret = doDeleteFile(src + "/" + tn);//delete dir also
        }
        
        if(ret!=null){
            setProperty(tn, STATE_TIFF_DELETING_FAILED);
        }else{
            setProperty(tn, STATE_TIFF_DELETING_COMPLETE);
        }
        return ret;
    }
    
             
    public String doDeleteAltoXml(String src, String tn){
        setProperty(tn, STATE_ALTOXML_DELETING);
        
        String ret = doDeleteFile(src + "/" + tn + ".zip");
        if(ret==null){
            ret = doDeleteFile(src + "/" + tn);//delete dir also
        }
        
        if(ret!=null){
            setProperty(tn, STATE_ALTOXML_DELETING_FAILED);
        }else{
            setProperty(tn, STATE_ALTOXML_DELETING_COMPLETE);
        }
        return ret;
    }
    
     
    
    public String doDeletePreprocessTiff(String preprocessTiffHotPath, String hotFolderZipPath, String tn){
        setProperty(tn, STATE_PREPROCESSTIFF_DELETING);
        
        String ret = doDeleteFile(hotFolderZipPath + "/" + tn + ".zip");
        if(ret==null){
            ret = doDeleteFile(preprocessTiffHotPath );//delete dir also
        }
        
        if(ret!=null){
            setProperty(tn, STATE_PREPROCESSTIFF_DELETING_FAILED);
        }else{
            setProperty(tn, STATE_PREPROCESSTIFF_DELETING_COMPLETE);
        }
        return ret;
    }
    
     
    
    
    
    private String doDeleteFile(String src){
        File f = new File(src);
        if(f.isDirectory()){
            String[] entries = f.list();
            for(String e : entries){
                File currentFile = new File(f.getPath(),e);
                currentFile.delete(); 
            }
        }
        
        boolean ret = true;
        if(f.exists()){
           ret = f.delete();//delete file or dir
        }
        
        if(ret==false)
            return src + " - Delete Failed";
        else 
            return null;
    }
    
    public String doTfdbUpdate(String tnFull, String tn, java.sql.Timestamp limbStartDate, java.sql.Timestamp limbCompleteDate){
        setProperty(tnFull, STATE_TFDB_UPDATE);
        
        //OCR_complete_date this.tfdbC
        int updateCount = 0;
        try{
            
            PreparedStatement ps = tfdbC.prepareStatement("update book set ocr_start_date = ? , ocr_complete_date = ?  where tn = ? ");
            ps.setTimestamp(1, limbStartDate);
            ps.setTimestamp(2, limbCompleteDate);
            ps.setString(3, tn);
            updateCount = ps.executeUpdate();
            //tfdbC.commit();
            
            ps.close();
             
        }catch(Exception e){
            setProperty(tnFull, STATE_TFDB_UPDATE_FAILED);
            System.out.println(e);
            JacobsTool.logLimbUploadMsg(e.toString());
            e.printStackTrace();
            return "Error updating tfdb database \n" + e.toString();
        }
        
        if(updateCount != 1){
           setProperty(tnFull, STATE_TFDB_UPDATE_FAILED);
           return "Error updating tfdb database";
        }else{
            setProperty(tnFull, STATE_TFDB_UPDATE_COMPLETE);
        }
        return null;//no errors
    }

    private String doCopyFile(String src, String dest){
        try {
           
            if (src.indexOf("\\\\") == -1) {
                src = src.replace("\\", "\\\\");
            }

          
            if (dest.indexOf("\\\\") == -1) {
                dest = dest.replace("\\", "\\\\");
            }

            File sourceFile = new File(src);
            File destFile = new File(dest);

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

        } catch (Exception e) {
            System.out.println(e);
            //throw e
            return "error " + e.toString();
        }
        return null;
    }
     
     
    private String doS3Transfer(String src, String dest, String fileName){
        src = src + "/" + fileName;
        if(src.indexOf("\\\\") == -1){
            src = src.replace("\\", "\\\\");
        }
        try{

			//String bucketName = dest;//"/bookscanning/paul";
			String accessKey            = "AKIAIRWIDC7AVGILZ33Q";
			String sk = s3Pimil;
			//String destinationKey           = "c:\\Bookscan\\Completed\\PDF\\SE-803913\\SE-803913.pdf";  //C:\Bookscan\Completed\PDF
 
			 
		 
			///////

			         AWSCredentials credentials = new BasicAWSCredentials(accessKey, sk);
 
			//////////
			
			//DefaultAWSCredentialsProviderChain credentialProviderChain = new DefaultAWSCredentialsProviderChain();
			
			         TransferManager tx = new TransferManager(credentials);        
			  
			//TransferManager tx = new TransferManager(
				//	credentialProviderChain.getCredentials());
			
			         Upload myUpload = tx.upload(dest, fileName, new File(src));

			// You can poll your transfer's status to check its progress
			
			if (myUpload.isDone() == false) {
				System.out.println("Transfer: " + myUpload.getDescription());
				System.out.println("  - State: " + myUpload.getState());
				System.out.println("  - Progress: "
						+ myUpload.getProgress().getBytesTransferred());
			}

			// Transfers also allow you to set a <code>ProgressListener</code> to receive
			// asynchronous notifications about your transfer's progress.
			// myUpload.addProgressListener(myProgressListener);

			// Or you can block the current thread and wait for your transfer to
			// to complete. If the transfer fails, this method will throw an
			// AmazonClientException or AmazonServiceException detailing the reason.
			myUpload.waitForCompletion();

			// After the upload is complete, call shutdownNow to release the resources.
			tx.shutdownNow();

		}catch(Exception e){
			System.out.println(e);
                        //throw e
                        return "error " + e.toString();
		}
        return null;
    }
    //sets various lists of work to be processed
    public void initWorkTodo( String postLimbSourcePdf, String postLimbSourceTiff, String postLimbSourceJpeg, String postLimbSourceAltoXml, String postLimbSourcePreProcessTiff, String postLimbDestPdf, String postLimbDestTiff, String postLimbDestJpeg, String postLimbDestAltoXml, String postLimbDestPreProcessTiff, String postLimbDestArchive, Connection c, Connection tfdbC){
      
        //Assume that we only want to process books of all 4 outputs (no partial processing pdf, but not jpg for example)
        //So just query folder that contains pdf completes
        
        //1 get files in complete folder (use pdf i guess, since they all should be same theoretically)
        //2 put in sql query IN list
        //3 query limb db for books that are complete and in IN list
        
        //For preprocess tiffs, use complete-pdf and hot-folder content to create list of todos
       /*not used this.postLimbSourcePdf = postLimbSourcePdf;
        this.postLimbSourceTiff =  postLimbSourceTiff;
        this.postLimbSourceJpeg = postLimbSourceJpeg; 
        this.postLimbSourceAltoXml =  postLimbSourceAltoXml;
        this.postLimbSourcePreProcessTiff =  postLimbSourcePreProcessTiff;
        this.postLimbDestPdf = postLimbDestPdf;
        this.postLimbDestTiff = postLimbDestTiff;
        this.postLimbDestJpeg =  postLimbDestJpeg;
        this.postLimbDestAltoXml =  postLimbDestAltoXml;
        this.postLimbDestPreProcessTiff = postLimbDestPreProcessTiff;
        this.postLimbDestArchive = postLimbDestArchive */
        this.c = c;
        this.tfdbC = tfdbC;
      
         
        
        File f = new File(postLimbSourcePdf);
        File[] pdfFiles = f.listFiles();
        
        Map<String, Object[]> tnLimbList = new HashMap();//tn,hotfolderlocation
                      
        //tn,filename
        Map<String, String> todoPdfReadyTns = new HashMap<>(); 
        
        String inClause = " ( ";
        for(int x = 0; x < pdfFiles.length ; x++){
            inClause = inClause + "'" + pdfFiles[x].getName() + "', ";//these will be directory names - each pdf in own dir
            
            
            /* remove code 5.22.2017
           
            Object[] xx = new Object[2];
            xx[0] = pdfFiles[x].getName();
            xx[1] = pdfFiles[x].getName();
            tnLimbList.put( pdfFiles[x].getName(),  xx);  
            todoPdfReadyTns.put(pdfFiles[x].getName(), getTnFromFileName(pdfFiles[x].getName()));//map of filename->tn
             */
            
        }
        inClause = inClause.substring(0, inClause.length()-2) + ")";//trim ending", "
        if(inClause.equals(" )"))
            inClause = " ('') ";
       
        /* no longer need to check if book is in ready but no release date. Instead just creat map of file->tn below
        try{
        //get from tfdb report "PDF Date but no Release date"
        
            Statement s = tfdbC.createStatement();
            ResultSet rs = s.executeQuery("select tn, pdf_ready, filename, num_of_pages, scanned_by, owning_institution from TF_IN_PROC_PDF_DATE_NO_REL_DAT");
                      
            while (rs.next()) {
                todoPdfReadyTns.put(rs.getString(3),rs.getString(1));      
                
                 //System.out.println("b"+rs.getString(3));
            }
            
            rs.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);  
            JacobsTool.logLimbUploadMsg(e.toString());
            e.printStackTrace();
        }
        */
      
           
        Set<String> stillProcessing = new HashSet();
        try{
            //finisheddate is not nullable, so used finished column=0 to know if still processing
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select [Finished], [relativefolder] as hotpath, [documentname] tn , CreationDate, FinishedDate"
                    + " from dbo.WorkflowKflows where [documentname] in " + inClause + " order by FinishedDate");//order by finishdate in case multiple ocrs run with different hotfolder locations get last one to finish
             /* todo undo commented code below 5.22.17   */
            while (rs.next()) {
                
                //what if tn is not in pdfReady list?
                //Decided to just not process book if not pdfReady.
                //This makes sense since it will insure that pdf files and tfdb db is in sync
                String finished = rs.getString(1);//if 0 then still processing
                String hotFolderName = rs.getString(2);// HOT_Dut_DuB_Dan_Afr\SE-1768122         
                java.sql.Timestamp createDate = rs.getTimestamp(4);
                java.sql.Timestamp finishDate = rs.getTimestamp(5);    
                String fileTn = rs.getString(3);
                if(finished.equals("1")){
                    if(fileTn!= null){// && todoPdfReadyTns.containsKey(fileTn) == true){
                        Object[] limbValues = new Object[3];
                        limbValues[0] = hotFolderName;
                        limbValues[1] = createDate;
                        limbValues[2] = finishDate;
                        
                        tnLimbList.put(fileTn, limbValues);  
                        todoPdfReadyTns.put(fileTn, getTnFromFileName(fileTn));//map of filename->tn
                    } 
                }else{
                    //still processing
                    //need to remove if another row of same tn exists (ie was processed before for some reason)
                    stillProcessing.add(fileTn);    
                }
                 
            }
            rs.close();
            s.close();
            for(String tn : stillProcessing){
                tnLimbList.remove(tn);//remove if still processing
            }
            
            
        }catch(Exception e){
            System.out.println(e);
            JacobsTool.logLimbUploadMsg(e.toString());
            e.printStackTrace();
        }
       
        //pdfReady list is ALL that tfdb shows as ready...not just LIMB output books
        setWorkTodoLists(tnLimbList, todoPdfReadyTns);
      //  return null;
        
    }
    
    private void setWorkTodoLists(Map<String,Object[]> pdfList, Map<String, String> pdfReadyTnList){
         
        //Create duplicate arraylists for each dir that will be processed
        this.pdfList = pdfList;
        //pdfErrorList = new ArrayList(Arrays.asList(errorTns.toArray()));
        //jpgList = new ArrayList(Arrays.asList(todoTns.toArray()));
        //altoXmlList = new ArrayList(Arrays.asList(todoTns.toArray()));
        //tiffList = new ArrayList(Arrays.asList(todoTns.toArray()));
            
        //These are used for work.  Remove entry when complete.
        Set<String> ks = pdfList.keySet();
        pdfTodoList = new ArrayList(ks);
        //pdfErrorTodoList = new ArrayList(Arrays.asList(errorTns.toArray()));
        //jpgTodoList = new ArrayList(Arrays.asList(todoTns.toArray()));
        //altoXmlTodoList = new ArrayList(Arrays.asList(todoTns.toArray()));
        //tiffTodoList = new ArrayList(Arrays.asList(todoTns.toArray()));
        pdfReadyList = pdfReadyTnList;
    }
    
    //get tn without letters in front for copying to output for publishing
    private String getTnFromFileName(String fileName){
        int i = 0;
        while (i < fileName.length() && !Character.isDigit(fileName.charAt(i))) {
            i++;
        }
        int j = i;
        while (j < fileName.length() && Character.isDigit(fileName.charAt(j))) {
            j++;
        }
          
        //System.out.println("xxx fileName=" + fileName + "  tn=" + fileName.substring(i, j));
          
        return fileName.substring(i, j); // might be an off-by-1 here
      
      
    }
    
    private String doZipDir(String sourceDir, String dest, String tn)
    {
        
        String zipFile = dest  + "/" + tn + ".zip"; 
        
        try
        {
            
            
                //create File object from source directory
                File fileSource = new File(sourceDir);
                File[] tmpFiles = fileSource.listFiles();
                if(!fileSource.exists()){
                    return "Error: directory does not exist: " + sourceDir;
                }
                if (tmpFiles.length == 0){
                    return "Error: directory contains no files: " + sourceDir;
                }
                
                //create object of FileOutputStream
                FileOutputStream fout = new FileOutputStream(zipFile);

                //create object of ZipOutputStream from FileOutputStream
                ZipOutputStream zout = new ZipOutputStream(fout);

                

                addDirectory(zout, fileSource);

                //close the ZipOutputStream
                zout.close();

                //System.out.println("Zip file has been created!");

                return null;
        }
        catch(IOException ioe)
        {
                return("IOException :" + ioe);     
        }
 
    }

    private String addDirectory(ZipOutputStream zout, File fileSource) {

        //get sub-folder/files list
        File[] files = fileSource.listFiles();

        //System.out.println("Adding directory " + fileSource.getName());

        for(int i=0; i < files.length; i++)
        {
            //if the file is directory, call the function recursively
            if(files[i].isDirectory())
            {
                    addDirectory(zout, files[i]);
                    continue;
            }
 

            try
            {
               // System.out.println("Adding file " + files[i].getName());

                //create byte buffer
                byte[] buffer = new byte[1024];

                //create object of FileInputStream
                FileInputStream fin = new FileInputStream(files[i]);

                zout.putNextEntry(new ZipEntry(files[i].getName()));

                
                 // After creating entry in the zip file, actually
                 //write the file.
                 
                int length;

                while((length = fin.read(buffer)) > 0)
                {
                   zout.write(buffer, 0, length);
                }

                
                 // After writing the file to ZipOutputStream, use
                 
                 //* void closeEntry() method of ZipOutputStream class to
                 //* close the current entry and position the stream to
                 //* write the next entry.
                 

                 zout.closeEntry();

                 //close the InputStream
                 fin.close();

                  
            }
            catch(IOException ioe)
            {
                    return "IOException :" + ioe;                             
            }
        }

        return null;
    }
        /*
    public void doZipFile() throws Exception {

        // input file 
        FileInputStream in = new FileInputStream("F:/sometxt.txt");

        // out put file 
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream("F:/tmp.zip"));

        // name the file inside the zip  file 
        out.putNextEntry(new ZipEntry("zippedjava.txt")); 

        // buffer size
        byte[] b = new byte[1024];
        int count;

        while ((count = in.read(b)) > 0) {
            out.write(b, 0, count);
        }
        out.close();
        in.close();
    }*/

  

}
