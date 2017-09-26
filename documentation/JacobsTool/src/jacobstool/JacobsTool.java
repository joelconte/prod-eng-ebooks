/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jacobstool;
 
//import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

 
public class JacobsTool extends javax.swing.JFrame {
    private String limbPimil = null;
    private String limbTfdbPimil = null;
    private String s3Pimil = null;
            
    private String identPimil = null;
   
    
    private boolean isNonIA = true;
    
    private int translationCount = 15; 
    private String[] translationKeys = {"title","creator","description",
    "subject","publisher","date","language","possible-copyright-status",
    "sponsor","contributor",   "mediatype","collection",
    "call_number","identifier","imagecount"};

    private String[] translationValues = {"dc:title","dc:creator","dc:description",
    "dc:subject","dc:publisher","dcterms:created","dc:language","dcterms:accessRights",
    "ldsterms:owninst","ldsterms:pubdigital","dc:format","dcterms:isPartOf",
    "ldsterms:callno3","dc:identifier","ldsterms:pagecount"};       
        
 
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
 
    /*special logic
        -dcterms:accessRights Public if possible-copyright-status null or "Public" or "NOT_IN_COPYRIGHT" else content of tag
        -10/22/2016 changes to be all Public for IA books
    
    A few tags we can ignore as an FYI:  "scanningcenter","repub_state","operator""scanner" "updaetdate" "updater","uploader","addeddate" "publicdate", "ppi", "lcamid","rcamid","camera", 
        "foldoutcount","identifier-access","identifier-ark","scanfactors","curation",    
    /**
  
    */
    String[][]  recordValues = {{"eventIdentifierType","Provenance Event"},
        {"eventType","Scan"},
        {"eventDescription","Creation of image"},
        {"eventDateTime","x"},
        {"eventOutcome1","codepending"},
        {"eventOutcomeDetail1","Original image capture"},
        {"linkingAgentIdentifierType1","Image Capture"},
        {"linkingAgentIdentifierValue1","Internet Archive"}};
 
   
 
    public JacobsTool() {
        initComponents();
        processPropertiesFile();
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
           @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                 closeWindow();
             }
        });
    }

    public void processPropertiesFile(){
        Properties p = new Properties();
        try{
            p.load(new FileReader("jt.properties"));//put in same dir as executable jar or inside root project in netbeans
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error opening jt.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //set to last tab when ran last
        String tab = p.getProperty("lastTab");
        if(tab.equals("0")){
            jTabbedPane1.setSelectedIndex(0);
        }else if(tab.equals("1")){
            jTabbedPane1.setSelectedIndex(1);
        }else if(tab.equals("2")){
            jTabbedPane1.setSelectedIndex(2);
        }else if(tab.equals("3")){
            //jTabbedPane1.setSelectedIndex(3);
        }
        System.out.println(tab);
        
        ///jacobs tool pre-publish prep
        sourceLabel.setText(p.getProperty("sourceDir"));
        destinationLabel.setText(p.getProperty("destinationDir"));
        String isIABooks = p.getProperty("isIABooks");
        if(isIABooks.equals("true")){
            radioIA.setSelected(true);
        }else{
            radioNonIA.setSelected(true);
        }
        
        populateListBox(sourceLabel, filesListBox);
        populateListBox(destinationLabel, filesListBoxDest);
        
        
        ///Limb post processing
        postLimbSourcePdf.setText(p.getProperty("postLimb.pdfSourceDir"));
        postLimbSourceJpeg.setText(p.getProperty("postLimb.jpegSourceDir"));
        postLimbSourceTiff.setText(p.getProperty("postLimb.tiffSourceDir"));
        postLimbSourceAltoXml.setText(p.getProperty("postLimb.altoXmlSourceDir"));
        postLimbSourcePreProcessTiff.setText(p.getProperty("postLimb.preProcessTiffSourceDir"));
       
        postLimbDestPdf.setText(p.getProperty("postLimb.pdfDestinationDir"));
        postLimbDestJpeg.setText(p.getProperty("postLimb.jpegDestinationDir"));
        postLimbDestTiff.setText(p.getProperty("postLimb.tiffDestinationDir"));
        postLimbDestAltoXml.setText(p.getProperty("postLimb.altoXmlDestinationDir"));
        postLimbDestPreProcessTiff.setText(p.getProperty("postLimb.preProcessTiffDestinationDir"));
        postLimbDestPdfComplete.setText(p.getProperty("postLimb.pdfCompleteDestinationDir"));
        postLimbDestPdfError.setText(p.getProperty("postLimb.pdfErrorDestinationDir"));
        
        threadCountDropdown.setSelectedItem(p.getProperty("postLimb.threadCount"));
        
        limbPimil = p.getProperty("postLimb.limbPimil");
        limbTfdbPimil = p.getProperty("postLimb.limbTfdbPimil");
        
        s3Pimil = p.getProperty("postLimb.s3Pimil");
        
        
        ////////////Internet Archive Search props
        identPimil = p.getProperty("internetArchiveSearch.identPimil");
       // populateListBox(sourceLabel1, filesListBox1);
        //populateListBox(destinationLabel1, filesListBoxDest1);
        
         
    }
    
    private Connection getLimbConnection(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = null;

            //works local String url = "jdbc:sqlserver://localhost\\sqlexpress:1433;databaseName=PaulLimb";
            String url = "jdbc:sqlserver://10.34.151.103\\LIMB:1433;DatabaseName=LimbServer.LimbDb";
            //String url = "jdbc:sqlserver://10.34.151.103\\LIMB:1433";

            //works local connection = DriverManager.getConnection(url,"sa","sqlserver");
            String x = limbPimil;
            connection = DriverManager.getConnection(url, "LimbAdmin", x);
            return connection;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            logLimbUploadMsg(e.toString());
        }
        return null;
    }
    
    private Connection getTfdbConnection(){
        Connection connection = null;
       try{
		Class.forName("org.postgresql.Driver");
		
//		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","ebook");
                String x = this.limbTfdbPimil;
		connection = DriverManager.getConnection("jdbc:postgresql://bookprod-wf-db02.a.fsglobal.net:5432/p239","bookscan", x);
		return connection;
		}catch(Exception e){ 
			System.out.println(e);
			e.printStackTrace();
                        logLimbUploadMsg(e.toString());
		}
        return connection;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        srcPathLabel = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        openDestFolder = new javax.swing.JButton();
        javax.swing.JButton setDirDest = new javax.swing.JButton();
        javax.swing.JButton setDir = new javax.swing.JButton();
        openSourceFolder = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        sourceLabel = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        destinationLabel = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        filesListBox = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        filesListBoxDest = new javax.swing.JList<>();
        runButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        saveProperties = new javax.swing.JButton();
        radioNonIA = new javax.swing.JRadioButton();
        radioIA = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        javax.swing.JButton setDirPostLimbSrcPdf = new javax.swing.JButton();
        postLimbSourceJpeg = new javax.swing.JTextField();
        postLimbSourceTiff = new javax.swing.JTextField();
        openSourceFolderPostLimbPdf = new javax.swing.JButton();
        saveProperties1 = new javax.swing.JButton();
        runButton1 = new javax.swing.JButton();
        cancelButton1 = new javax.swing.JButton();
        postLimbSourcePdf = new javax.swing.JTextField();
        postLimbSourcePreProcessTiff = new javax.swing.JTextField();
        postLimbSourceAltoXml = new javax.swing.JTextField();
        postLimbDestAltoXml = new javax.swing.JTextField();
        postLimbDestPdf = new javax.swing.JTextField();
        postLimbDestTiff = new javax.swing.JTextField();
        postLimbDestJpeg = new javax.swing.JTextField();
        postLimbDestPreProcessTiff = new javax.swing.JTextField();
        postLimbDestPdfComplete = new javax.swing.JTextField();
        javax.swing.JButton setDirPostLimbSrcTiff = new javax.swing.JButton();
        openSourceFolderPostLimbTiff = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbSrcJpeg = new javax.swing.JButton();
        openSourceFolderPostLimbJpeg = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbSrcAltoXml = new javax.swing.JButton();
        openSourceFolderPostLimbAltoXml = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbSrcPreProcessTiff = new javax.swing.JButton();
        openSourceFolderPostLimbPreProcessTiff = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbDestPdf = new javax.swing.JButton();
        openDestFolderPostLimbPdf = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbDestTiff = new javax.swing.JButton();
        openDestFolderPostLimbTiff = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbDestJpeg = new javax.swing.JButton();
        openDestFolderPostLimbJpeg = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbDestAltoXml = new javax.swing.JButton();
        openDestFolderPostLimbAltoXml = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbDestPreProcessTiff = new javax.swing.JButton();
        openDestFolderPostLimbPreProcessTiff = new javax.swing.JButton();
        javax.swing.JButton setDirPostLimbDestPdfComplete = new javax.swing.JButton();
        openDestFolderPostLimbPdfComplete = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        threadCountDropdown = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        postLimbDestPdfError = new javax.swing.JTextField();
        javax.swing.JButton setDirPostLimbDestPdfError = new javax.swing.JButton();
        openDestFolderPostLimbPdfError = new javax.swing.JButton();
        runButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        statusAll = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Jacobs Tool IA and Non-IA Books - d9.26.17");

        openDestFolder.setText("Open Folder");
        openDestFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDestFolderActionPerformed(evt);
            }
        });

        setDirDest.setText("Change Output Location");
        setDirDest.setToolTipText("");
        setDirDest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirDestActionPerformed(evt);
            }
        });

        setDir.setText("Change Source Location");
        setDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirActionPerformed(evt);
            }
        });

        openSourceFolder.setText("Open Folder");
        openSourceFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSourceFolderActionPerformed(evt);
            }
        });

        sourceLabel.setText("Source Dir");
        sourceLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceLabelActionPerformed(evt);
            }
        });
        jScrollPane3.setViewportView(sourceLabel);

        destinationLabel.setText("Destination Dir");
        destinationLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destinationLabelActionPerformed(evt);
            }
        });
        jScrollPane4.setViewportView(destinationLabel);

        jScrollPane1.setViewportView(filesListBox);

        jScrollPane2.setViewportView(filesListBoxDest);

        runButton.setText("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Close");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveProperties.setText("Remember Settings");
        saveProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePropertiesActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioNonIA);
        radioNonIA.setText("Non IA Books");

        buttonGroup1.add(radioIA);
        radioIA.setText("IA Books");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(setDir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openSourceFolder)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane4)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(runButton)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(setDirDest)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openDestFolder)))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(319, 319, 319)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioNonIA)
                            .addComponent(radioIA)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(cancelButton))
                    .addComponent(saveProperties))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cancelButton)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(setDir)
                                .addComponent(openSourceFolder))
                            .addGap(34, 34, 34)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(radioIA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(radioNonIA)
                            .addGap(48, 48, 48)
                            .addComponent(saveProperties))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(setDirDest)
                                .addComponent(openDestFolder))
                            .addGap(34, 34, 34)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(runButton)))))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Prep XML Metadata for Publish (Jacobs Tool)", jPanel1);

        jButton1.setText("run test limbdb");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane5.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane6.setViewportView(jTextArea2);

        jButton2.setText("run test tfdb");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane8.setViewportView(jTextArea3);

        jButton3.setText("Show ready in both limbdb and tfdb");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(120, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(144, 144, 144)
                .addComponent(jButton3)
                .addGap(56, 56, 56))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(200, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Limb OCR Post-processing test", jPanel3);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Limb OCR Post-Processing");

        setDirPostLimbSrcPdf.setText("Change");
        setDirPostLimbSrcPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbSrcPdfActionPerformed(evt);
            }
        });

        postLimbSourceJpeg.setText("postLimbSourceJpeg");
        postLimbSourceJpeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbSourceJpegActionPerformed(evt);
            }
        });

        postLimbSourceTiff.setText("postLimbSourceTiff");
        postLimbSourceTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbSourceTiffActionPerformed(evt);
            }
        });

        openSourceFolderPostLimbPdf.setText("Open and View");
        openSourceFolderPostLimbPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSourceFolderPostLimbPdfActionPerformed(evt);
            }
        });

        saveProperties1.setText("Remember Settings");
        saveProperties1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveProperties1ActionPerformed(evt);
            }
        });

        runButton1.setText("Run");
        runButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButton1ActionPerformed(evt);
            }
        });

        cancelButton1.setText("Close");
        cancelButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButton1ActionPerformed(evt);
            }
        });

        postLimbSourcePdf.setText("postLimbSourcePdf");
        postLimbSourcePdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbSourcePdfActionPerformed(evt);
            }
        });

        postLimbSourcePreProcessTiff.setText("postLimbSourcePreProcessTiff");
        postLimbSourcePreProcessTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbSourcePreProcessTiffActionPerformed(evt);
            }
        });

        postLimbSourceAltoXml.setText("postLimbSourceAltoXml");
        postLimbSourceAltoXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbSourceAltoXmlActionPerformed(evt);
            }
        });

        postLimbDestAltoXml.setText("postLimbDestAltoXml");
        postLimbDestAltoXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbDestAltoXmlActionPerformed(evt);
            }
        });

        postLimbDestPdf.setText("postLimbDestPdf");
        postLimbDestPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbDestPdfActionPerformed(evt);
            }
        });

        postLimbDestTiff.setText("postLimbDestTiff");
        postLimbDestTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbDestTiffActionPerformed(evt);
            }
        });

        postLimbDestJpeg.setText("postLimbDestJpeg");
        postLimbDestJpeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbDestJpegActionPerformed(evt);
            }
        });

        postLimbDestPreProcessTiff.setText("postLimbSourcePreProcessTiff");
        postLimbDestPreProcessTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbDestPreProcessTiffActionPerformed(evt);
            }
        });

        postLimbDestPdfComplete.setText("postLimbDestArchive");
        postLimbDestPdfComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbDestPdfCompleteActionPerformed(evt);
            }
        });

        setDirPostLimbSrcTiff.setText("Change");
        setDirPostLimbSrcTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbSrcTiffActionPerformed(evt);
            }
        });

        openSourceFolderPostLimbTiff.setText("Open and View");
        openSourceFolderPostLimbTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSourceFolderPostLimbTiffActionPerformed(evt);
            }
        });

        setDirPostLimbSrcJpeg.setText("Change");
        setDirPostLimbSrcJpeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbSrcJpegActionPerformed(evt);
            }
        });

        openSourceFolderPostLimbJpeg.setText("Open and View");
        openSourceFolderPostLimbJpeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSourceFolderPostLimbJpegActionPerformed(evt);
            }
        });

        setDirPostLimbSrcAltoXml.setText("Change");
        setDirPostLimbSrcAltoXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbSrcAltoXmlActionPerformed(evt);
            }
        });

        openSourceFolderPostLimbAltoXml.setText("Open and View");
        openSourceFolderPostLimbAltoXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSourceFolderPostLimbAltoXmlActionPerformed(evt);
            }
        });

        setDirPostLimbSrcPreProcessTiff.setText("Change");
        setDirPostLimbSrcPreProcessTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbSrcPreProcessTiffActionPerformed(evt);
            }
        });

        openSourceFolderPostLimbPreProcessTiff.setText("Open and View");
        openSourceFolderPostLimbPreProcessTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSourceFolderPostLimbPreProcessTiffActionPerformed(evt);
            }
        });

        setDirPostLimbDestPdf.setText("Change");
        setDirPostLimbDestPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbDestPdfActionPerformed(evt);
            }
        });

        openDestFolderPostLimbPdf.setText("Open and View");
        openDestFolderPostLimbPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDestFolderPostLimbPdfActionPerformed(evt);
            }
        });

        setDirPostLimbDestTiff.setText("Change");
        setDirPostLimbDestTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbDestTiffActionPerformed(evt);
            }
        });

        openDestFolderPostLimbTiff.setText("Open and View");
        openDestFolderPostLimbTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDestFolderPostLimbTiffActionPerformed(evt);
            }
        });

        setDirPostLimbDestJpeg.setText("Change");
        setDirPostLimbDestJpeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbDestJpegActionPerformed(evt);
            }
        });

        openDestFolderPostLimbJpeg.setText("Open and View");
        openDestFolderPostLimbJpeg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDestFolderPostLimbJpegActionPerformed(evt);
            }
        });

        setDirPostLimbDestAltoXml.setText("Change");
        setDirPostLimbDestAltoXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbDestAltoXmlActionPerformed(evt);
            }
        });

        openDestFolderPostLimbAltoXml.setText("Open and View");
        openDestFolderPostLimbAltoXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDestFolderPostLimbAltoXmlActionPerformed(evt);
            }
        });

        setDirPostLimbDestPreProcessTiff.setText("Change");
        setDirPostLimbDestPreProcessTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbDestPreProcessTiffActionPerformed(evt);
            }
        });

        openDestFolderPostLimbPreProcessTiff.setText("Open and View");
        openDestFolderPostLimbPreProcessTiff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDestFolderPostLimbPreProcessTiffActionPerformed(evt);
            }
        });

        setDirPostLimbDestPdfComplete.setText("Change");
        setDirPostLimbDestPdfComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbDestPdfCompleteActionPerformed(evt);
            }
        });

        openDestFolderPostLimbPdfComplete.setText("Open and View");
        openDestFolderPostLimbPdfComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDestFolderPostLimbPdfCompleteActionPerformed(evt);
            }
        });

        jLabel2.setText("Completed LIMB TIFF");

        jLabel4.setText("Completed LIMB PDF");

        jLabel5.setText("Completed LIMB JPEG");

        jLabel6.setText("Completed LIMB ALTO");

        jLabel7.setText("HOTFOLDER PreProcess TIFF");

        jLabel8.setText("S3 PDF");

        jLabel9.setText("S3 TIFF");

        jLabel10.setText("S3 JPEG");

        jLabel11.setText("S3 ALTO");

        jLabel12.setText("S3 PreProcess TIFF");

        jLabel13.setText("PDFComplete (send to publish)");

        jLabel14.setText("(parent dir containing hotfolders)");

        threadCountDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20" }));
        threadCountDropdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threadCountDropdownActionPerformed(evt);
            }
        });

        jLabel15.setText("Number of Thread (parallel)");

        jLabel16.setText("PDF Errors (1k size files)");

        postLimbDestPdfError.setText("postLimbDestArchive");
        postLimbDestPdfError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postLimbDestPdfErrorActionPerformed(evt);
            }
        });

        setDirPostLimbDestPdfError.setText("Change");
        setDirPostLimbDestPdfError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDirPostLimbDestPdfErrorActionPerformed(evt);
            }
        });

        openDestFolderPostLimbPdfError.setText("Open and View");
        openDestFolderPostLimbPdfError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDestFolderPostLimbPdfErrorActionPerformed(evt);
            }
        });

        runButton2.setText("Open Log File");
        runButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(threadCountDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(99, 99, 99)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel14))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(postLimbDestPdfError)
                            .addComponent(postLimbDestPreProcessTiff, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                            .addComponent(postLimbDestAltoXml)
                            .addComponent(postLimbDestJpeg)
                            .addComponent(postLimbDestTiff)
                            .addComponent(postLimbDestPdf)
                            .addComponent(postLimbSourcePreProcessTiff)
                            .addComponent(postLimbSourceAltoXml)
                            .addComponent(postLimbSourceJpeg)
                            .addComponent(postLimbDestPdfComplete, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(postLimbSourceTiff, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(postLimbSourcePdf, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addComponent(setDirPostLimbSrcPdf)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(openSourceFolderPostLimbPdf))
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(setDirPostLimbSrcTiff)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(openSourceFolderPostLimbTiff))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(setDirPostLimbDestPdf)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(openDestFolderPostLimbPdf))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(setDirPostLimbDestTiff)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(openDestFolderPostLimbTiff))))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(setDirPostLimbSrcJpeg)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(openSourceFolderPostLimbJpeg))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(setDirPostLimbSrcAltoXml)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(openSourceFolderPostLimbAltoXml))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(setDirPostLimbSrcPreProcessTiff)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(openSourceFolderPostLimbPreProcessTiff))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(setDirPostLimbDestJpeg)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(openDestFolderPostLimbJpeg))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(setDirPostLimbDestAltoXml)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(openDestFolderPostLimbAltoXml))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(setDirPostLimbDestPreProcessTiff)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(openDestFolderPostLimbPreProcessTiff))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(setDirPostLimbDestPdfComplete)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(openDestFolderPostLimbPdfComplete)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(runButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(runButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap())
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                .addComponent(cancelButton1)
                                                .addGap(21, 21, 21))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                .addComponent(saveProperties1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(setDirPostLimbDestPdfError)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(openDestFolderPostLimbPdfError)
                                .addContainerGap())))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(threadCountDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postLimbSourcePdf, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setDirPostLimbSrcPdf)
                    .addComponent(openSourceFolderPostLimbPdf)
                    .addComponent(saveProperties1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postLimbSourceTiff, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setDirPostLimbSrcTiff)
                    .addComponent(openSourceFolderPostLimbTiff)
                    .addComponent(jLabel2)
                    .addComponent(runButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbSourceJpeg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbSrcJpeg)
                            .addComponent(openSourceFolderPostLimbJpeg)
                            .addComponent(jLabel5))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbSourceAltoXml, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbSrcAltoXml)
                            .addComponent(openSourceFolderPostLimbAltoXml)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbSourcePreProcessTiff, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbSrcPreProcessTiff)
                            .addComponent(openSourceFolderPostLimbPreProcessTiff)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbDestPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbDestPdf)
                            .addComponent(openDestFolderPostLimbPdf)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbDestTiff, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbDestTiff)
                            .addComponent(openDestFolderPostLimbTiff)
                            .addComponent(jLabel9))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbDestJpeg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbDestJpeg)
                            .addComponent(openDestFolderPostLimbJpeg)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbDestAltoXml, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbDestAltoXml)
                            .addComponent(openDestFolderPostLimbAltoXml)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbDestPreProcessTiff, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbDestPreProcessTiff)
                            .addComponent(openDestFolderPostLimbPreProcessTiff)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(postLimbDestPdfComplete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbDestPdfComplete)
                            .addComponent(openDestFolderPostLimbPdfComplete)
                            .addComponent(jLabel13)
                            .addComponent(cancelButton1))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(postLimbDestPdfError, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setDirPostLimbDestPdfError)
                            .addComponent(openDestFolderPostLimbPdfError)))
                    .addComponent(runButton1))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Admin - Run", jPanel6);

        statusAll.setColumns(20);
        statusAll.setRows(5);
        jScrollPane7.setViewportView(statusAll);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1193, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Status of All", jPanel7);

        jTabbedPane1.addTab("Limb OCR Post-Processing", jTabbedPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(srcPathLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(srcPathLabel)
                .addGap(45, 45, 45))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Limb OCR Post-Processing");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void logErrors(String msgs, File destDir){
        try{
            File errorFile = new File(destDir.getAbsolutePath()+"/Errors.txt");
            FileWriter fw = new FileWriter(errorFile,true);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            String dateStamp = dateFormat.format(cal.getTime());   
            
            if(msgs==null || msgs.equals(""))
                fw.append(dateStamp + "\r\nNo errors\r\n");
            else
                fw.append(dateStamp + "\r\n" + msgs+"\r\n");
             
            fw.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    private void logBookProcessed(String bookName, File destDir)throws Exception{
        
        String fileName = destDir.getName() + "_converted.txt";
        
        File f = new File(destDir.getAbsoluteFile() + "/" + fileName);
        FileWriter fw = new FileWriter(f, true);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Calendar cal = Calendar.getInstance();
        String dateStamp = dateFormat.format(cal.getTime());   
            
        fw.append( bookName + " \t" + dateStamp + "\r\n");
             
        fw.close();
       
    }

    public static synchronized void logLimbUploadMsg(String msgs){
        try{
            
            File errorFile = new File("LimbUploadLog.txt");
            FileWriter fw = new FileWriter(errorFile,true);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSSSSSSS");
            Calendar cal = Calendar.getInstance();
            String dateStamp = dateFormat.format(cal.getTime());   
             
            fw.append(dateStamp + "  " + Thread.currentThread().getName() + "\r\n" + msgs+"\r\n");
            
            //todo remove
            System.out.println(dateStamp + "  " + Thread.currentThread().getName() + "\r\n" + msgs+"\r\n");
            
            fw.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    

    
    private void savePropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePropertiesActionPerformed

        Properties p = new Properties();
        try{
            p.load(new FileReader("jt.properties"));//put in same dir as executable jar or inside root project in netbeans
            p.setProperty("destinationDir", destinationLabel.getText());
            p.setProperty("sourceDir", sourceLabel.getText());
            String isIABooks = p.getProperty("isIABooks");
            if(radioIA.isSelected()){
                p.setProperty("isIABooks", "true");
            }else{
                p.setProperty("isIABooks", "false");
            }
        
            p.store(new FileWriter(new File("jt.properties")), null);
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error opening jt.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_savePropertiesActionPerformed

    
    private void closeWindow(){
        Properties p = new Properties();
        try{
            p.load(new FileReader("jt.properties"));//put in same dir as executable jar or inside root project in netbeans
            p.setProperty("lastTab", String.valueOf(jTabbedPane1.getSelectedIndex()));
            
            p.store(new FileWriter(new File("jt.properties")), null);
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error opening jt.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
         

        System.exit(0);
    }
    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
       // add your handling code here:
        isNonIA = true;
        if(radioIA.isSelected())
            isNonIA = false;
        else
            isNonIA = true;
        
        String msg = "Confirm the following locations.\n\n Source directory--> " + sourceLabel.getText() + "\n"
                + " Destination directory--> " + destinationLabel.getText();
        
        if(JOptionPane.CANCEL_OPTION == JOptionPane.showConfirmDialog(this,  msg, "Continue?", JOptionPane.OK_CANCEL_OPTION))
            return;
            
        String sourcePath = sourceLabel.getText();
        String destinationPath = destinationLabel.getText();
        File sourceDir = new File(sourcePath);
        File[] sourceFiles = sourceDir.listFiles();
        File destinationDir = new File(destinationPath);
        //File[] destinationFiles = destinationDir.listFiles();
         
        //check if empty dest dir
        /*if(!isDestEmpty()){
            if(JOptionPane.CANCEL_OPTION == JOptionPane.showConfirmDialog(this,  msg, "Continue?", JOptionPane.OK_CANCEL_OPTION)){
                return;
            }
        }*/
        String msgs = "";
        boolean hadWarning = false;
        boolean hadError = false;
        for(File inFile : sourceFiles){
            msgs = "";
            try{
                
                msgs = processFile(inFile, destinationDir);
                if(msgs.contains("Error")){
                    hadError = true;
                }else  if(msgs.contains("Warning") || msgs.contains("WARNING")){
                    hadWarning = true;
                }
            
                    
                logBookProcessed( inFile.getName() , destinationDir );
                
            }catch(Exception e){
                msgs += e.getMessage()+"\r\n";
            }
            if(!msgs.equals(""))
                logErrors(msgs, destinationDir);
        }
        if(hadWarning == false && hadError == false){
            JOptionPane.showMessageDialog(this, "Complete with no errors");
        }else if(hadError == false){
            JOptionPane.showMessageDialog(this, "Complete with warnings.  Please see Errors.txt file in " + destinationLabel.getText());
        }else{
            JOptionPane.showMessageDialog(this, "Complete with errors.  Please see Errors.txt file." + destinationLabel.getText());
        }
        
        populateListBox(sourceLabel, filesListBox);
        populateListBox(destinationLabel, filesListBoxDest);
               
    }//GEN-LAST:event_runButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        closeWindow();
       
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void openDestFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDestFolderActionPerformed

        try{
            Desktop.getDesktop().open(new File(destinationLabel.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openDestFolderActionPerformed

    private void setDirDestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirDestActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser(destinationLabel.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            destinationLabel.setText(dir.getAbsolutePath());

            populateListBox(destinationLabel, filesListBoxDest);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirDestActionPerformed

    private void destinationLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinationLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_destinationLabelActionPerformed

    private void sourceLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sourceLabelActionPerformed

    private void openSourceFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSourceFolderActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(sourceLabel.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openSourceFolderActionPerformed

    private void setDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser(sourceLabel.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            sourceLabel.setText(dir.getAbsolutePath());

            populateListBox(sourceLabel, filesListBox);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = null;
 
            //works local connection = DriverManager.getConnection(url,"sa","x");
            connection = getLimbConnection();//DriverManager.getConnection(url, "LimbAdmin", "x");
            //connection = DriverManager.getConnection(url);
            /*
			   * 
			   
SELECT TOP 1000 [DbId]
      ,[Skipped]
      ,[Finished]
      ,[Project]
      ,[Rank]
      ,[Serialized]
      ,[Template]
      ,[DefaultWorkingFolder]
      ,[WorkingFolder]
      ,[ErrorMessage]
      ,[FolderOut]
      ,[OutPutFolderPath]
      ,[CreationDate]
      ,[FinishedDate]
      ,[Folder]
      ,[IsPdfSource]
      ,[RelativeFolder]
      ,[DocumentName]
      ,[PagesCount]
      ,[Highlight]
      ,[CreatedBy]
      ,[UserWorkingOnJob]
      ,[HotFolderId]
      ,[ImportStatus]
      ,[ImageProcStatus]
      ,[AutoQCStatus]
      ,[DocumentValidationStatus]
      ,[SampleStatus]
      ,[StructureStatus]
      ,[OcrStatus]
      ,[ExportStatus]
      ,[PublishStatus]
      ,[MessagesCount]
      ,[StatusDescription]
      ,[AlreadyExtracted]
      ,[AlreadyPreviewed]
      ,[CanBeReprocessed]
      ,[PagesCountAll]
      ,[IsMultiPageTiff]
      ,[FolderAlreadyLoaded]
      ,[DeleteFolder]
      ,[DbStatus]
      ,[DefaultOutputFolder]
      ,[Starting]
      ,[PauseJob]
      ,[SegmentationStatus]
  FROM [LimbServer.LimbDb].[dbo].[WorkflowKflows]
             */
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select [DbId] "
                    + " 	      ,[Skipped]	 "
                    + " 	      ,[Finished]	 "
                    + " 	      ,[Project]	 "
                    + " 	      ,[Rank]	 "
                    + " 	      ,[Serialized]	 "
                    + " 	      ,[Template]	 "
                    + " 	      ,[DefaultWorkingFolder]	 "
                    + " 	      ,[WorkingFolder]	 "
                    + " 	      ,[ErrorMessage]	 "
                    + " 	      ,[FolderOut]	 "
                    + " 	      ,[OutPutFolderPath]	 "
                    + " 	      ,[CreationDate]	 "
                    + " 	      ,[FinishedDate]	 "
                    + " 	      ,[Folder]	 "
                    + " 	      ,[IsPdfSource]	 "
                    + " 	      ,[RelativeFolder]	 "
                    + " 	      ,[DocumentName]	 "
                    + " 	      ,[PagesCount]	 "
                    + " 	      ,[Highlight]	 "
                    + " 	      ,[CreatedBy]	 "
                    + " 	      ,[UserWorkingOnJob]	 "
                    + " 	      ,[HotFolderId]	 "
                    + " 	      ,[ImportStatus]	 "
                    + " 	      ,[ImageProcStatus]	 "
                    + " 	      ,[AutoQCStatus]	 "
                    + " 	      ,[DocumentValidationStatus]	 "
                    + " 	      ,[SampleStatus]	 "
                    + " 	      ,[StructureStatus]	 "
                    + " 	      ,[OcrStatus]	 "
                    + " 	      ,[ExportStatus]	 "
                    + " 	      ,[PublishStatus]	 "
                    + " 	      ,[MessagesCount]	 "
                    + " 	      ,[StatusDescription]	 "
                    + " 	      ,[AlreadyExtracted]	 "
                    + " 	      ,[AlreadyPreviewed]	 "
                    + " 	      ,[CanBeReprocessed]	 "
                    + " 	      ,[PagesCountAll]	 "
                    + " 	      ,[IsMultiPageTiff]	 "
                    + " 	      ,[FolderAlreadyLoaded]	 "
                    + " 	      ,[DeleteFolder]	 "
                    + " 	      ,[DbStatus]	 "
                    + " 	      ,[DefaultOutputFolder]	 "
                    + " 	      ,[Starting]	 "
                    + " 	      ,[PauseJob]	 "
                    + " 	      ,[SegmentationStatus]	 "
                    + " from dbo.WorkflowKflows where finished = 1 order by finisheddate");
            int x = 0;
            String outputstr = "";
            while (rs.next()) {
                x++;
                if (x == 2) {
                    break;
                    //System.out.println("Pause");
                }

                for (int z = 1; z < 45; z++) {
                    outputstr = outputstr + "row " + x + " col " + z + "  " + rs.getString(z) + "\n";
                }
            }

            jTextArea1.setText(outputstr);
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
 
		try{
		Class.forName("org.postgresql.Driver");
		Connection connection = null;

		connection = getTfdbConnection();   
		/////////////////
		Statement s = connection.createStatement();
		 ResultSet rs = s.executeQuery("select * from site");
		           int x = 0;
            String outputstr = "";
            while (rs.next()) {
                x++;
                 

                for (int z = 1; z < 5; z++) {
                    outputstr = outputstr + "row " + x + " col " + z + "  " + rs.getString(z) + "\n";
                }
            }
            jTextArea2.setText(outputstr);
		connection.close();
		}catch(Exception e){ 
			System.out.println(e);
			e.printStackTrace();
		}
        
    }//GEN-LAST:event_jButton2ActionPerformed

   
    volatile int doneThreadCount = 0;//has to be at object level to access inside of inner class
    synchronized private void setDoneThreadCount(int c){
        doneThreadCount = c;
    }
    synchronized private int getDoneThreadCount(){
        return doneThreadCount;
    }
    
    synchronized private void decramentDoneThreadCount(){
        doneThreadCount--;
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Connection limbC = getLimbConnection();
        Connection tfC = getTfdbConnection();
        
        
        
        //tn,filename
        Map<String, String> todoPdfReadyTns = new HashMap<>(); 
        try{
        //get from tfdb report "PDF Date but no Release date"
        
            Statement s = tfC.createStatement();
            ResultSet rs = s.executeQuery("select tn, pdf_ready, filename, num_of_pages, scanned_by, owning_institution from TF_IN_PROC_PDF_DATE_NO_REL_DAT");
                      
            while (rs.next()) {
                todoPdfReadyTns.put(rs.getString(3),rs.getString(1));      
                
                 //System.out.println("b"+rs.getString(3));
            }
        }catch(Exception e){
            System.out.println(e);  
            JacobsTool.logLimbUploadMsg(e.toString());
            e.printStackTrace();
        }
      
        List<String> matchTns = new ArrayList<>();
        
        try{
            Statement s = limbC.createStatement();
            ResultSet rs = s.executeQuery("select [Finished], [relativefolder] as hotpath, [documentname] tn "
                    + " from dbo.WorkflowKflows where [finished] = 1  " );
                
            String all = "";
            while (rs.next()) {
                
                //what if tn is not in pdfReady list?
                //Decided to just not process book if not pdfReady.
                //This makes sense since it will insure that pdf files and tfdb db is in sync
                String fileTn = rs.getString(3);
                if(fileTn!= null && todoPdfReadyTns.containsKey(fileTn) == true){
                    matchTns.add(fileTn);      
                    all = all + fileTn + "\n";
                }
                //hack temp todo remove
                //todoTns.add("SE-1014724");
            }
            jTextArea3.setText(all);
            
        }catch(Exception e){
            System.out.println(e);
            JacobsTool.logLimbUploadMsg(e.toString());
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void runButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButton2ActionPerformed
        // TODO add your handling code here:
        // File errorFile = new File("LimbUploadLog.txt");
        try{
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "LimbUploadLog.txt");
            pb.start();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }//GEN-LAST:event_runButton2ActionPerformed

    private void openDestFolderPostLimbPdfErrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDestFolderPostLimbPdfErrorActionPerformed

        try{
            Desktop.getDesktop().open(new File(postLimbDestPdfError.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openDestFolderPostLimbPdfErrorActionPerformed

    private void setDirPostLimbDestPdfErrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbDestPdfErrorActionPerformed
        // TODO add your handling code here:

        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbDestPdfError.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbDestPdfError.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbDestPdfErrorActionPerformed

    private void postLimbDestPdfErrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbDestPdfErrorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbDestPdfErrorActionPerformed

    private void threadCountDropdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threadCountDropdownActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_threadCountDropdownActionPerformed

    private void openDestFolderPostLimbPdfCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDestFolderPostLimbPdfCompleteActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbDestPdfComplete.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openDestFolderPostLimbPdfCompleteActionPerformed

    private void setDirPostLimbDestPdfCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbDestPdfCompleteActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbDestPdfComplete.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbDestPdfComplete.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbDestPdfCompleteActionPerformed

    private void openDestFolderPostLimbPreProcessTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDestFolderPostLimbPreProcessTiffActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbDestPreProcessTiff.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openDestFolderPostLimbPreProcessTiffActionPerformed

    private void setDirPostLimbDestPreProcessTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbDestPreProcessTiffActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbDestPreProcessTiff.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbDestPreProcessTiff.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbDestPreProcessTiffActionPerformed

    private void openDestFolderPostLimbAltoXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDestFolderPostLimbAltoXmlActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbDestAltoXml.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openDestFolderPostLimbAltoXmlActionPerformed

    private void setDirPostLimbDestAltoXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbDestAltoXmlActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbDestAltoXml.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbDestAltoXml.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbDestAltoXmlActionPerformed

    private void openDestFolderPostLimbJpegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDestFolderPostLimbJpegActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbDestJpeg.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openDestFolderPostLimbJpegActionPerformed

    private void setDirPostLimbDestJpegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbDestJpegActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbDestJpeg.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbDestJpeg.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbDestJpegActionPerformed

    private void openDestFolderPostLimbTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDestFolderPostLimbTiffActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbDestTiff.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openDestFolderPostLimbTiffActionPerformed

    private void setDirPostLimbDestTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbDestTiffActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbDestTiff.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbDestTiff.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbDestTiffActionPerformed

    private void openDestFolderPostLimbPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDestFolderPostLimbPdfActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbDestPdf.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }

    }//GEN-LAST:event_openDestFolderPostLimbPdfActionPerformed

    private void setDirPostLimbDestPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbDestPdfActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbDestPdf.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbDestPdf.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbDestPdfActionPerformed

    private void openSourceFolderPostLimbPreProcessTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSourceFolderPostLimbPreProcessTiffActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbSourcePreProcessTiff.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openSourceFolderPostLimbPreProcessTiffActionPerformed

    private void setDirPostLimbSrcPreProcessTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbSrcPreProcessTiffActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbSourcePreProcessTiff.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbSourcePreProcessTiff.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbSrcPreProcessTiffActionPerformed

    private void openSourceFolderPostLimbAltoXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSourceFolderPostLimbAltoXmlActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbSourceAltoXml.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openSourceFolderPostLimbAltoXmlActionPerformed

    private void setDirPostLimbSrcAltoXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbSrcAltoXmlActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbSourceAltoXml.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbSourceAltoXml.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbSrcAltoXmlActionPerformed

    private void openSourceFolderPostLimbJpegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSourceFolderPostLimbJpegActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbSourceJpeg.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openSourceFolderPostLimbJpegActionPerformed

    private void setDirPostLimbSrcJpegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbSrcJpegActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbSourceJpeg.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbSourceJpeg.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbSrcJpegActionPerformed

    private void openSourceFolderPostLimbTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSourceFolderPostLimbTiffActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbSourceTiff.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openSourceFolderPostLimbTiffActionPerformed

    private void setDirPostLimbSrcTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbSrcTiffActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbSourceTiff.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbSourceTiff.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbSrcTiffActionPerformed

    private void postLimbDestPdfCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbDestPdfCompleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbDestPdfCompleteActionPerformed

    private void postLimbDestPreProcessTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbDestPreProcessTiffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbDestPreProcessTiffActionPerformed

    private void postLimbDestJpegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbDestJpegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbDestJpegActionPerformed

    private void postLimbDestTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbDestTiffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbDestTiffActionPerformed

    private void postLimbDestPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbDestPdfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbDestPdfActionPerformed

    private void postLimbDestAltoXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbDestAltoXmlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbDestAltoXmlActionPerformed

    private void postLimbSourceAltoXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbSourceAltoXmlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbSourceAltoXmlActionPerformed

    private void postLimbSourcePreProcessTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbSourcePreProcessTiffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbSourcePreProcessTiffActionPerformed

    private void postLimbSourcePdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbSourcePdfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbSourcePdfActionPerformed

    private void cancelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton1ActionPerformed
        // TODO add your handling code here:
        closeWindow();
    }//GEN-LAST:event_cancelButton1ActionPerformed

    private void runButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButton1ActionPerformed
        logLimbUploadMsg("STARTING LIMB OUTPUT PROCESSING");
        runButton1.setEnabled(false);

        String msg = "Ready to zip and upload files to s3?";

        if(JOptionPane.CANCEL_OPTION == JOptionPane.showConfirmDialog(this,  msg, "Continue?", JOptionPane.OK_CANCEL_OPTION)){
            runButton1.setEnabled(true);
            return;
        }
        
        Connection c = null;
        Connection tfdbC =null;

        try {

            c = getLimbConnection();
            tfdbC = getTfdbConnection();

        }catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            logLimbUploadMsg(e.toString());
        }

        LimbOutputFileUtil util = new LimbOutputFileUtil();
        util.clearAllProperty();//clear out all book status from prior run
        util.setS3Pimil(s3Pimil);
        util.initWorkTodo(postLimbSourcePdf.getText(), postLimbSourceTiff.getText(), postLimbSourceJpeg.getText(), postLimbSourceAltoXml.getText(), postLimbSourcePreProcessTiff.getText(), postLimbDestPdf.getText(), postLimbDestTiff.getText(), postLimbDestJpeg.getText(), postLimbDestAltoXml.getText(), postLimbDestPreProcessTiff.getText(),  postLimbDestPdfComplete.getText(), c, tfdbC);

        /////////////run in background threads

        int threadCount = Integer.parseInt((String)threadCountDropdown.getSelectedItem());
        setDoneThreadCount( threadCount);
        
        for(int x = 0; x< threadCount; x++){

            SwingWorker worker = new SwingWorker<String, String>() {
                @Override
                public String doInBackground() {
                    logLimbUploadMsg("Thread starting. ID " + Thread.currentThread().getName());
                    String msg = null;
                    String tn;
                    while(true){
                        tn = util.getNextPdfToProcess();
                        if(tn == null){
                            logLimbUploadMsg("Thread got NULL tn to process" );
                            break;
                        }
 
                        
                        //Preprocess tiff, use temp location for zip files so they don't go inside of HOT folders
                        String preprocessTiffHotPath = null;
                        java.sql.Timestamp limbStartDate = null;
                        java.sql.Timestamp limbCompleteDate = null;
                        String hotFolderZipPath = postLimbSourcePreProcessTiff.getText() + "/jt_zip_working_folder";
                        try{                        
                            File tmpF = new File(hotFolderZipPath);
                            if(tmpF.exists()==false){
                                tmpF.mkdir();
                            }
                            preprocessTiffHotPath = postLimbSourcePreProcessTiff.getText() + "/" + tn;// no holdfolder now.  util.getPdfList().get(tn)[0];//get hotfolder from map
                            limbStartDate = (java.sql.Timestamp)util.getPdfList().get(tn)[1];
                            limbCompleteDate = (java.sql.Timestamp)util.getPdfList().get(tn)[2];
                            
                        }catch(Exception e){
                             logLimbUploadMsg("Error in processing TN " + tn + " \nError getting or processing TM's data from limbdb...maybe null finishdate or hotfolderpath???\n " + e.toString());
                             continue;
                        }
                            
                            
                        
                        //START LIMB POSTPROCESS STEPS
                        //check to see if the pdf is 1k size, which signifies a limb processing error
                        util.setProperty(tn, util.STATE_PDF_ERRORCHECK);
                        publish(tn);
                        logLimbUploadMsg(tn + " started pdf check for errors - 1k ");
                        String hadPdfError = util.doPdfErrorCheck(  postLimbSourcePdf.getText(), tn);
                        if(hadPdfError != null && !hadPdfError.equals("1k size")){
                            logLimbUploadMsg(hadPdfError);//1k size - is not processing failure
                            continue;//done, get next book
                        }else{
                            logLimbUploadMsg(tn + " completed pdf check for errors - 1k ");
                        }
                        
                        
                        
                        if(hadPdfError != null){
                            
                            util.setProperty(tn, util.STATE_PDF_ERROR_TRANSFERING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started pdf process errors - 1k");
                            msg = util.doPdfErrorHandle( postLimbSourcePdf.getText(), postLimbDestPdfError.getText(), tn);
                            
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed pdf process errors - 1k");
                            }
                            
                            
                            
                        }else if(hadPdfError == null){

                            
                            util.setProperty(tn, util.STATE_PDF_TRANSFERING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started pdf transfer");
                            msg = util.doPdfTransfer( postLimbSourcePdf.getText(), postLimbDestPdf.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed pdf transfer" );
                            }

                            util.setProperty(tn, util.STATE_TIFF_ZIPPING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started tiff zip");
                            msg = util.doZipTiff( postLimbSourceTiff.getText(), postLimbSourceTiff.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed tiff zip");
                            }

                            util.setProperty(tn, util.STATE_TIFF_TRANSFERING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started tiff transfer");
                            msg = util.doTiffTransfer( postLimbSourceTiff.getText(), postLimbDestTiff.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed tiff transfer" );
                            }

                            util.setProperty(tn, util.STATE_JPEG_ZIPPING);
                            publish(tn );
                            logLimbUploadMsg(tn + " started jpeg zip");
                            msg = util.doZipJpeg( postLimbSourceJpeg.getText(), postLimbSourceJpeg.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed jpeg zip");
                            }

                            util.setProperty(tn, util.STATE_JPEG_TRANSFERING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started jpeg transfer");
                            msg = util.doJpegTransfer( postLimbSourceJpeg.getText(), postLimbDestJpeg.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed jpeg transfer" );
                            }

                            util.setProperty(tn, util.STATE_ALTOXML_ZIPPING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started alto zip");
                            msg = util.doZipAltoXml( postLimbSourceAltoXml.getText(), postLimbSourceAltoXml.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed alto zip");
                            }

                            util.setProperty(tn, util.STATE_ALTOXML_TRANSFERING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started alto transfer");
                            msg = util.doAltoXmlTransfer( postLimbSourceAltoXml.getText(), postLimbDestAltoXml.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed alto transfer" );
                            }

                            String trimtn = util.getPdfReadyList().get(tn);
                            util.setProperty(tn, util.STATE_PDFCOMPLETE_TRANSFERING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started pdf transfer to PDFComplete");
                            msg = util.doPdfTransferPdfComplete( postLimbSourcePdf.getText(), postLimbDestPdfComplete.getText(), tn, trimtn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed pdf transfer to PDFComplete");
                            }
                         
                            
                            //Preprocess tiff, use temp location for zip files so they don't go inside of HOT folders
                            util.setProperty(tn, util.STATE_PREPROCESSTIFF_ZIPPING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started preprocess-tiff zip");
                            msg = util.doZipPreprocessTiff( preprocessTiffHotPath, hotFolderZipPath, tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed preprocess-tiff zip");
                            }

                            util.setProperty(tn, util.STATE_PREPROCESSTIFF_TRANSFERING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started preprocess-tiff transfer");
                            msg = util.doPreprocessTiffTransfer( hotFolderZipPath, postLimbDestPreProcessTiff.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed preprocess-tiff transfer" );
                            }


                            //update tfdb ocr complete datecomplete
                            util.setProperty(tn, util.STATE_TFDB_UPDATE);
                            this.publish(tn);
                            logLimbUploadMsg(tn + " started tfdb ocr-complete update");
                            msg = util.doTfdbUpdate( tn, util.getPdfReadyList().get(tn), limbStartDate, limbCompleteDate);//get trimmed tn, not use filenametn
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed tfdb ocr-complete update");
                            }


                            //////////////////////deletes  (if no pdf 1k filesize error)
    
                            util.setProperty(tn, util.STATE_PDF_DELETING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started pdf delete");
                            msg = util.doDeletePdf( postLimbSourcePdf.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed pdf delete");
                            }

                            util.setProperty(tn, util.STATE_TIFF_DELETING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started tiff delete");
                            msg = util.doDeleteTiff( postLimbSourceTiff.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed tiff delete");
                            }

                            util.setProperty(tn, util.STATE_JPEG_DELETING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started jpeg delete");
                            msg = util.doDeleteJpeg( postLimbSourceJpeg.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed jpeg delete");
                            }  
                            

                            util.setProperty(tn, util.STATE_ALTOXML_DELETING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started alto delete");
                            msg = util.doDeleteAltoXml( postLimbSourceAltoXml.getText(), tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed alto delete");
                            }


                            //delete from hotfolders and corresponding zips   
                            util.setProperty(tn, util.STATE_PREPROCESSTIFF_DELETING);
                            publish(tn);
                            logLimbUploadMsg(tn + " started preprocess-tiff delete");
                            msg = util.doDeletePreprocessTiff( preprocessTiffHotPath, hotFolderZipPath, tn);
                            if(msg!=null){
                                logLimbUploadMsg(msg);
                                continue;//done, get next book
                            }else{
                                logLimbUploadMsg(tn + " completed preprocess-tiff delete");
                            }

                        
                        
                        }
                        
                        publish(tn );

                        util.setProcessingDone(tn);
                        logLimbUploadMsg(tn + " DONE");
                    }

                    publish(tn );
                    decramentDoneThreadCount();
                    if(getDoneThreadCount() == 0){
                        runButton1.setEnabled(true);
                    }
                    logLimbUploadMsg("Thread done. ID " + Thread.currentThread().getName());
                    logLimbUploadMsg("Threads still running = " + getDoneThreadCount() );
                    return "Thread done";
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
                    util.showStatusAll(statusAll);

                }

            };

            worker.execute();
        }

        
    }//GEN-LAST:event_runButton1ActionPerformed

    private void saveProperties1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveProperties1ActionPerformed
        // TODO add your handling code here:

        Properties p = new Properties();
        try{
            p.load(new FileReader("jt.properties"));//put in same dir as executable jar or inside root project in netbeans
            p.setProperty("postLimb.pdfSourceDir", postLimbSourcePdf.getText());
            p.setProperty("postLimb.jpegSourceDir", postLimbSourceJpeg.getText());
            p.setProperty("postLimb.tiffSourceDir", postLimbSourceTiff.getText());
            p.setProperty("postLimb.altoXmlSourceDir", postLimbSourceAltoXml.getText());
            p.setProperty("postLimb.preProcessTiffSourceDir", postLimbSourcePreProcessTiff.getText());

            p.setProperty("postLimb.pdfDestinationDir", postLimbDestPdf.getText());
            p.setProperty("postLimb.jpegDestinationDir", postLimbDestJpeg.getText());
            p.setProperty("postLimb.tiffDestinationDir", postLimbDestTiff.getText());
            p.setProperty("postLimb.altoXmlDestinationDir", postLimbDestAltoXml.getText());
            p.setProperty("postLimb.preProcessTiffDestinationDir", postLimbDestPreProcessTiff.getText());
            p.setProperty("postLimb.pdfCompleteDestinationDir", postLimbDestPdfComplete.getText());
            p.setProperty("postLimb.pdfErrorDestinationDir", postLimbDestPdfError.getText());

            p.setProperty("postLimb.threadCount", (String) threadCountDropdown.getSelectedItem());

            p.store(new FileWriter(new File("jt.properties")), null);

        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error opening jt.properties file\n\nError Info:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Looking in: " + System.getProperty("user.dir"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveProperties1ActionPerformed

    private void openSourceFolderPostLimbPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSourceFolderPostLimbPdfActionPerformed
        // TODO add your handling code here:

        try{
            Desktop.getDesktop().open(new File(postLimbSourcePdf.getText()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Cannot open folder..." + e.getMessage());
        }
    }//GEN-LAST:event_openSourceFolderPostLimbPdfActionPerformed

    private void postLimbSourceTiffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbSourceTiffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbSourceTiffActionPerformed

    private void postLimbSourceJpegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postLimbSourceJpegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_postLimbSourceJpegActionPerformed

    private void setDirPostLimbSrcPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDirPostLimbSrcPdfActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser( postLimbSourcePdf.getText());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = fc.showDialog(this, "Select Folder");
        if (retVal == JFileChooser.APPROVE_OPTION) {

            File dir = fc.getSelectedFile();
            postLimbSourcePdf.setText(dir.getAbsolutePath());

            // populateListBox(destinationLabel1, filesListBoxDest1);
        } else {
            //cancel
        }
    }//GEN-LAST:event_setDirPostLimbSrcPdfActionPerformed
 
    
    class iaSelectColorListRenderer extends DefaultListCellRenderer
    {
        //private HashMap theChosen = new HashMap();
        List<List<String>> searchResults;
  
        //pass in searchresults matrix from other class
        public iaSelectColorListRenderer( List<List<String>> searchResults )
        {
            this.searchResults = searchResults;
        }
 
    }
   /*
    
    public void initIdJlist(){
        JList list = idList;
        list.setModel(new DefaultListModel());
        list.setCellRenderer(new CheckboxListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 
      // Add a mouse listener to handle changing selection
 
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                JList<CheckboxListItem> list
                        = (JList<CheckboxListItem>) event.getSource();

                // Get index of item clicked
                Point p = event.getPoint();
                int index = list.locationToIndex(event.getPoint());
                if(p.x < 22){ 
                    CheckboxListItem item = (CheckboxListItem) list.getModel()
                        .getElementAt(index);
                       // Toggle selected state
                    item.setSelected(!item.isSelected());
                    
                }else{
                    //query and display
                     CheckboxListItem item = (CheckboxListItem) list.getModel()
                        .getElementAt(index);
                     String id = item.getLabel();
                     String number = item.getNumber();
                     List<String> row = iaProcessing.displaySelectedBookMetadata(number, id);
                     
                     tfTitle.setText(row.get(2));
                     tfImageCount.setText(row.get(3));
                     tfLanguage.setText(row.get(4));
                     tfDate.setText(row.get(5));
                     tfSubject.setText(row.get(6));
                     tfDescription.setText(row.get(7));
                }

             

                // Repaint cell
                list.repaint(list.getCellBounds(index, index));
            }
        });

    }  
*/
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JacobsTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JacobsTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JacobsTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JacobsTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JacobsTool jt = new JacobsTool();
                 
              
                
                jt.setVisible(true);
                
            }
        });
    }

    private void populateListBox(JTextField dirField, JList list ){
        try{
        File dir = new File(dirField.getText());
                      
            File[] files = dir.listFiles();
            ListModel<String> m = list.getModel();
            DefaultListModel listModel = new DefaultListModel();
            
            for(File f : files){
                
                listModel.addElement(f.getName());   
            }
            
            list.setModel(listModel);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Problem reading directory: " + dirField.getText());
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    private boolean isDestEmpty(){
        File f = new File(destinationLabel.getText());
        File[] files = f.listFiles();
        if (!f.isDirectory() || files.length!=0)
             return false;
        else
            return true;
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

    /*
    INPUT

R:\Internet Archives\Elder Shaver Downloads\In Process
\2016_04_28_IA_Florida_Mar_2016\airforceregiste1962wash_0 
Contains airforceregiste1962wash_0.pdf and airforceregiste1962wash_0_meta.xml


OUTPUT
    no drps used
1. (Spence says these are not needed) 
 
2.  
R:\Internet Archives\Elder Shaver Downloads\In Process\2016_04_28_IA_Florida_Mar_2016_converted\dcms
\assets\airforceregiste1962wash_0\PRESERVATION_MASTER\
Contains airforceregiste1962wash_0.pdf

3.  
R:\Internet Archives\Elder Shaver Downloads\In Process\2016_04_28_IA_Florida_Mar_2016_converted\dcms
\metadata\ldssip
Contains all *.sip files in same dir (airforceregiste1962wash_0.sip.xml)

4. 
R:\Internet Archives\Elder Shaver Downloads\In Process\2016_04_04_British Columbia_3_Mar_converted
Contains 2016_04_04_British Columbia_3_Mar_converted.txt of all books
0000000000	4/1/2016 17:00
1000islandhouses00alex	4/1/2016 17:00
10thcensus0112unit	4/1/2016 17:00
10thcensus1096unit	4/1/2016 17:01
10thcensus1289unit	4/28/2016 15:36
12thcensusofpopu1372unit	4/1/2016 17:02
12thcensusofpopu1373unit	4/1/2016 17:03
12thcensusofpopu1546unit	4/1/2016 17:04
12thcensusofpopu1608unit	4/1/2016 17:05
13thcensus1910po1309unit	4/1/2016 17:06
13thcensus1910po1310unit	4/1/2016 17:08
13thcensus1910po1475unit	4/1/2016 17:09
13thcensus1910po1527unit	4/1/2016 17:10
14thcensusofpopu1507unit	4/1/2016 17:11
14thcensusofpopu1508unit	4/1/2016 17:13

     */
    PDDocument doc = null;
    //inDir //R:\Internet Archives\Elder Shaver Downloads\In Process\2016_04_28_IA_Florida_Mar_2016\airforceregiste1962wash_0 
    // outputDir R:\Internet Archives\Elder Shaver Downloads\In Process\2016_04_04_British Columbia_3_Mar_converted\
    private String processFile(File inDir, File outputDir)  {
        boolean isPublicDomain = false;//temp hault of copy protected books...solution to just separate out into diff folder
        File origCopyrightProtectedPdf = null;
        File origCopyrightProtectedSip = null;  
        
        String msgs = "";
        String inNameFolder = inDir.getName(); //name of book folder..use it to rename if files don't match

        File metaInputFile = null;
        File pdfInputFile = null;
        
        if(isNonIA == false){
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
                    msgs += "WARNING, no pdf, copying _text.pdf to: " + inDir.getAbsolutePath() + "\r\n";
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
                        msgs += "WARNING, no pdf, copying .pdf_text to: " + inDir.getAbsolutePath() + "\r\n";
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
                        msgs += "WARNING, no matching folder and pdf name, copying " + fInFiles[x].getName() + " to " + inDir.getAbsolutePath()+ "\r\n";
                        break;
                    }
                }
            }
        }else{
            pdfInputFile = inDir;//pdf gets passed in instead of folder containing it
        }
        
        //still no input files
        if(isNonIA == false && metaInputFile == null){
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
        if(isNonIA == false){
            
         
            try{
                //  PdfReader pdfr = new PdfReader(pdfInputFile.getAbsolutePath());
                File ff = pdfInputFile;
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            doc = PDDocument.load(ff); 
                        }catch(IOException ioe ){
                            System.out.println("Error, ignore");
                        }
                    }
                };
                 
                Timer timer = new Timer();
                timer.schedule(tt, 0);//run now
                try{
                    Thread.sleep(10000);
                }catch(InterruptedException e){
                    //pageCount = x;// 
                    timer.cancel();
                }
                //doc = PDDocument.load(pdfInputFile);
                try{
                    pageCount = doc.getNumberOfPages();//if not set (0), then code below will not use it and uses imagecount in xml instead
                }catch(Exception e){
                    
                }
                if(doc != null)
                    doc.close();
            }catch(Exception e){
                msgs += "WARNING, cannot get page count from pdf: " + pdfInputFile.getName() + ".  Using count in xml \r\n";             
            }
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
        if(isNonIA){
            pdfOutputDir = new File(pdfOutputDir.getAbsoluteFile() + "/" + inNameFolder.substring(0, inNameFolder.length()-4));//trim .pdf off folder
        }else{
            pdfOutputDir = new File(pdfOutputDir.getAbsoluteFile() + "/" + inNameFolder);
        }
        pdfOutputDir.mkdir();
        String newPdfFileName = pdfOutputDir.getName();//same as inNameFolder - use in xml output also in case pdf name was updated by code and diff from input metadata
        pdfOutputDir = new File(pdfOutputDir.getAbsoluteFile()  + "/PRESERVATION_MASTER" );
        pdfOutputDir.mkdir();
         
        File sipOutputDir = new File(outputDir.getAbsoluteFile() + "/dcms/");//all in batch go to same dir
        sipOutputDir.mkdir();
        sipOutputDir = new File(sipOutputDir.getAbsoluteFile() + "/metadata/");//all in batch go to same dir
        sipOutputDir.mkdir();
        
        if(isNonIA == false){
            sipOutputDir = new File(sipOutputDir.getAbsoluteFile() + "/ldssip/");//all in batch go to same dir
            sipOutputDir.mkdir();
        }
      
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
        if(isNonIA){
            sipOutputFile = new File(sipOutputDir + "/" + inNameFolder.substring(0,inNameFolder.length()-4) + ".sip.xml");//trimm off .pdf
        }else{
            sipOutputFile = new File(sipOutputDir + "/" + inNameFolder + ".sip.xml");
        }
        
        if (isNonIA == false) {
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
            
                //todo need to also get tag licenceurl and check it for "public".
                //Then if neigher licenceurl or possible-copyright-status have "public", then it isPublicDomain=false
        
                //get licenseurl value
                String licenseUrl = inTagsValues.get("licenseurl");
                String propertyRight = inTagsValues.get(translationKeys[7]);
                if (propertyRight != null && ("NOT_IN_COPYRIGHT".equalsIgnoreCase(propertyRight)  ||  propertyRight.toLowerCase().contains("public domain"))) {
                    inTagsValues.put(translationKeys[7], "Public");
                    isPublicDomain = true;
                }else if(propertyRight == null && licenseUrl!=null && licenseUrl.toLowerCase().contains("public domain")){
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
                    /*if(mdValues[mdIndex][1] != null) {
                    mdValues[mdIndex][1] += "; " + inTagsValues.get(k);//concatinate prev value
                }else{*/
                    mdValues[mdIndex][1] = inTagsValues.get(k);//value using input tag key
                    //}

                    /*if(k.contains("***")){
                    //dont increment mdIndex
                }else{*/
                    mdIndex++;
                    //  }
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
        } else {
             //non IA books get from tfdb xml
             //write to sipOutputFile;
             
	    try {

                URL url = new URL("https://bookscan.ldschurch.org/bookscan/xmlMetadata/getTn/" + inNameFolder.substring(0, inNameFolder.length()-4) );
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    return msgs + "Error Could not get xml metadata for " + inNameFolder.substring(0, inNameFolder.length()-4) + " - Failed : HTTP error code : "
                            + conn.getResponseCode() + "\r\n";
                }

                
                //BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream()));

                BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream(), "UTF-8"));

                String output;
                String sipFileContent = "";
                //System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    sipFileContent += output;
                }
                
                //have to parse xml from tfdb in order to get dcterms:accessRights
                String determs_accessRights = "";
                int accRiStart = sipFileContent.indexOf("<dcterms:accessRights>");
                int accRiEnd = sipFileContent.indexOf("</dcterms:accessRights>");
               // String ttt = sipFileContent.substring(accRiStart + 22, accRiEnd);
                if(accRiStart != -1 && accRiStart != -1){
                    if(sipFileContent.substring(accRiStart+22, accRiEnd).equalsIgnoreCase("public")){
                        isPublicDomain = true;
                    }
                }
                
                //isPublicDomain = true;
                 
                
                
                conn.disconnect();
                
                //write to sip xml file
          
                origCopyrightProtectedSip = sipOutputFile;//save for possible move of file below
                
                Writer fw = new BufferedWriter(new OutputStreamWriter(  new FileOutputStream(sipOutputFile), "UTF-8"));
                
                fw.write(sipFileContent);
                fw.close();

            } catch (MalformedURLException e) {
                return msgs +  "Error Could not get xml metadata for " + inNameFolder.substring(0, inNameFolder.length()-4) + " - Error processing sip xml - "
                            + e.getMessage() + "\r\n";
                

            } catch (IOException e) {

                return msgs +  "Error Could not get xml metadata for " + inNameFolder.substring(0, inNameFolder.length()-4) + " - Error processing sip xml - IO Exception - "
                            + e.getMessage() + "\r\n";
            }

	
        }
        
        /* this works in java 1.7 - changes for webapp on 1.6 temporarily
        try{
            //move files if copy protected
            if (isPublicDomain == false) {
                Path pdfPath = origCopyrightProtectedPdf.toPath();
                String pdfPathStr = origCopyrightProtectedPdf.getAbsolutePath();
                Path sipPath = origCopyrightProtectedSip.toPath();
                String sipPathStr = origCopyrightProtectedSip.getAbsolutePath();

                pdfPathStr = pdfPathStr.replace("dcms", "dcms-copyprotected");
                sipPathStr = sipPathStr.replace("dcms", "dcms-copyprotected");
                File pdfDestFile = new File(pdfPathStr);
                File sipDestFile = new File(sipPathStr);
                File f1 = pdfDestFile.getParentFile();
                f1.mkdirs();
                File f2 = sipDestFile.getParentFile();
                f2.mkdirs();
              
                Files.move(pdfPath, pdfDestFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.move(sipPath, sipDestFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
               
                deleteFolder(new File(pdfPath.getParent().getParent().toFile().getAbsolutePath()));
                deleteFolder(new File(sipPath.toFile().getAbsolutePath()));
                 
            }

        } catch (Exception e){
            return msgs +  "Error while moving copyprotected files to dcms-copyprotected directory - "
                            + e.getMessage() + "\r\n";
        }*/
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
                 
            }

        } catch (Exception e){
            return msgs +  "Error while moving copyprotected files to dcms-copyprotected directory - "
                            + e.getMessage() + "\r\n";
        }
        return msgs;
    }

    
    private void deleteFolder(File folder) {
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
    
    private boolean arrayContains(String[][] mdValues, String v){
        for(int x = 0; x< mdValues.length; x++){
            if(v.equalsIgnoreCase(mdValues[x][0]))
                return true;
        }
        return false;
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
    
    private static void printNode(NodeList nodeList) {

        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                // get node name and value
                System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                System.out.println("Node Value =" + tempNode.getTextContent());

                if (tempNode.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();

                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());

                    }

                }

                if (tempNode.hasChildNodes()) {

                    // loop again if has child nodes
                    printNode(tempNode.getChildNodes());

                }

                System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

            }

        }
    }

  
    //array[0]=key array[1]=value
    private String[] getKeyValueFromTagLine(String txt){
        int startTag1 = txt.indexOf("<")+1;
        int endTag1 = txt.indexOf(">")-1;
        int vStart = endTag1+2;
        int vEnd = txt.indexOf("<",vStart) ;
        String key = txt.substring(startTag1,endTag1 +1);
        String value = "";
        if(vEnd != -1){
            //if -1 then empty tag
            value = txt.substring(vStart,vEnd );
        }
        
        String[] ret = new String[2];
        ret[0] = key;
        ret[1] = value;
        return ret;
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton cancelButton1;
    private javax.swing.JTextField destinationLabel;
    private javax.swing.JList<String> filesListBox;
    private javax.swing.JList<String> filesListBoxDest;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JButton openDestFolder;
    private javax.swing.JButton openDestFolderPostLimbAltoXml;
    private javax.swing.JButton openDestFolderPostLimbJpeg;
    private javax.swing.JButton openDestFolderPostLimbPdf;
    private javax.swing.JButton openDestFolderPostLimbPdfComplete;
    private javax.swing.JButton openDestFolderPostLimbPdfError;
    private javax.swing.JButton openDestFolderPostLimbPreProcessTiff;
    private javax.swing.JButton openDestFolderPostLimbTiff;
    private javax.swing.JButton openSourceFolder;
    private javax.swing.JButton openSourceFolderPostLimbAltoXml;
    private javax.swing.JButton openSourceFolderPostLimbJpeg;
    private javax.swing.JButton openSourceFolderPostLimbPdf;
    private javax.swing.JButton openSourceFolderPostLimbPreProcessTiff;
    private javax.swing.JButton openSourceFolderPostLimbTiff;
    private javax.swing.JTextField postLimbDestAltoXml;
    private javax.swing.JTextField postLimbDestJpeg;
    private javax.swing.JTextField postLimbDestPdf;
    private javax.swing.JTextField postLimbDestPdfComplete;
    private javax.swing.JTextField postLimbDestPdfError;
    private javax.swing.JTextField postLimbDestPreProcessTiff;
    private javax.swing.JTextField postLimbDestTiff;
    private javax.swing.JTextField postLimbSourceAltoXml;
    private javax.swing.JTextField postLimbSourceJpeg;
    private javax.swing.JTextField postLimbSourcePdf;
    private javax.swing.JTextField postLimbSourcePreProcessTiff;
    private javax.swing.JTextField postLimbSourceTiff;
    private javax.swing.JRadioButton radioIA;
    private javax.swing.JRadioButton radioNonIA;
    private javax.swing.JButton runButton;
    private javax.swing.JButton runButton1;
    private javax.swing.JButton runButton2;
    private javax.swing.JButton saveProperties;
    private javax.swing.JButton saveProperties1;
    private javax.swing.JTextField sourceLabel;
    private javax.swing.JLabel srcPathLabel;
    private javax.swing.JTextArea statusAll;
    private javax.swing.JComboBox<String> threadCountDropdown;
    // End of variables declaration//GEN-END:variables
}
