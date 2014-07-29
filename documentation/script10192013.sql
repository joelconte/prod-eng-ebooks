DROP view S_01_Ready_scan; 
create or replace view  S_01_Ready_scan as 
SELECT Book.TN, Book.partner_Lib_Call_#, Book.record_number, Book.title, Book.scan_num_of_pages, Book.scan_machine_id, Book.Scan_start_Date, Book.scan_Image_Auditor, Book.scan_IA_start_Date, Book.Files_Sent_to_Orem, Book.requesting_location, Book.scanned_by
FROM (Book LEFT JOIN S_TF_Problems ON Book.TN=S_TF_Problems.TN) LEFT JOIN S_TF_Solution_Found ON Book.TN=S_TF_Solution_Found.TN
WHERE (((Book.Scan_start_Date) Is  Null)  And ((S_TF_Problems.TN) Is Null) And ((S_TF_Solution_Found.TN) Is Null) And   ( (Book.Date_Released) Is Null ) And
((Book.Files_Sent_to_Orem) Is Null))  and files_received_by_orem is null And   Book.Date_loaded Is Null AND Book.DNP Is Null 
ORDER BY Book.Scan_start_Date; 

DROP view S_01B_scan_in_prog; 
create or replace view  S_01B_scan_in_prog as 
SELECT Book.TN,  Book.partner_Lib_Call_#, Book.record_number, Book.title, Book.scan_num_of_pages, Book.scan_machine_id, Book.Scan_start_Date, Book.scan_Image_Auditor, Book.scan_IA_start_Date, Book.Files_Sent_to_Orem,  Book.requesting_location, Book.scanned_by
FROM (Book LEFT JOIN S_TF_Problems ON Book.TN=S_TF_Problems.TN) LEFT JOIN S_TF_Solution_Found ON Book.TN=S_TF_Solution_Found.TN
WHERE (((Book.Scan_start_Date) Is not  Null)   And ((S_TF_Problems.TN) Is Null) And ((S_TF_Solution_Found.TN) Is Null) And ((Book.Scan_complete_Date) Is Null) And  ( (Book.Date_Released) Is Null ) And
((Book.Files_Sent_to_Orem) Is Null))  and files_received_by_orem is null And   Book.Date_loaded Is Null  AND Book.DNP Is Null 
ORDER BY Book.Scan_start_Date;


DROP view S_02_Ready_image_audit; 
create or replace view  S_02_Ready_image_audit as 
SELECT Book.TN,  Book.partner_Lib_Call_#, Book.record_number, Book.title, Book.scan_num_of_pages, Book.scan_machine_id, Book.Scan_start_Date, Book.scan_Image_Auditor, Book.scan_IA_start_Date, Book.Files_Sent_to_Orem,  Book.requesting_location, Book.scanned_by
FROM (Book LEFT JOIN S_TF_Problems ON Book.TN=S_TF_Problems.TN) LEFT JOIN S_TF_Solution_Found ON Book.TN=S_TF_Solution_Found.TN
WHERE ( ((Book.Scan_complete_Date) Is Not Null)   And ((S_TF_Problems.TN) Is Null) And ((S_TF_Solution_Found.TN) Is Null) And ((Book.Scan_IA_Start_Date) Is Null) And 
((Book.Files_Sent_to_Orem) Is Null))  and files_received_by_orem is null And  Book.Date_Released Is Null  And   Book.Date_loaded Is Null AND Book.DNP Is Null 
ORDER BY Book.Scan_start_Date;


DROP view S_03_image_auditing_in_prog;
create or replace view  S_03_image_auditing_in_prog as 
SELECT Book.TN, Book.partner_Lib_Call_#, Book.record_number, Book.title,  Book.scan_Image_Auditor, Book.scan_num_of_pages, Book.scan_IA_start_Date, Book.Site, Book.scan_Machine_id,  Book.requesting_location, Book.scanned_by 
FROM Book
WHERE  Book.scan_Ia_start_date Is not Null  and  Book.scan_Ia_complete_date Is Null And 
Book.Files_Sent_to_Orem Is Null and files_received_by_orem is null And  Book.Date_Released Is Null And   Book.Date_loaded Is Null AND Book.DNP Is Null  ;


DROP view S_04_processed_ready_orem; 
create or replace view  S_04_processed_ready_orem as 
SELECT Book.TN, Book.partner_Lib_Call_#, Book.record_number, Book.title,  Book.scan_num_of_pages, Book.Scan_IA_Start_Date, Book.Remarks_from_Scan_Center , Book.Files_Sent_to_Orem,  Book.requesting_location, Book.scanned_by  
FROM (Book LEFT JOIN S_TF_Problems ON Book.TN = S_TF_Problems.TN) LEFT JOIN S_TF_Solution_Found ON Book.TN = S_TF_Solution_Found.TN
WHERE (Book.Scan_IA_complete_Date Is Not Null AND ((Book.Files_Sent_to_Orem) Is Null)   AND 
((S_TF_Problems.TN) Is Null) AND ((S_TF_Solution_Found.TN) Is Null))  and files_received_by_orem is null And  Book.Date_Released Is Null  And   Book.Date_loaded Is Null AND Book.DNP Is Null 
ORDER BY Book.Scan_IA_Start_Date, Book.Site DESC;





DROP TABLE IaBookMetadata CASCADE CONSTRAINTS;


PROMPT Creating Table IaBookMetadata ...
CREATE TABLE IaBookMetadata (
  TN varchar2(255 CHAR),
  Secondary_Identifier varchar2(255 CHAR) unique, --used for iaBookMetadatas with tn from non-olib sources like internet archive
  OCLC_number varchar2(255 CHAR) unique, --worldcat
  ISBN_ISSN varchar2(255 CHAR) unique,
  Title  varchar2(2047 CHAR),
  Author varchar2(255 CHAR),
  Property_right varchar2(255 CHAR), 
  publication_type varchar2(255 CHAR), 
  Call_# varchar2(255 CHAR),
  Partner_lib_Call_# varchar2(255 CHAR),
  Priority_Item CHAR(1) DEFAULT 'F' check (priority_item IN ('T', 'F')),
  Withdrawn CHAR(1) DEFAULT 'F' check (Withdrawn IN ('T', 'F')),
  Digital_Copy_Only  CHAR(1) DEFAULT 'F' check (Digital_Copy_Only  IN ('T', 'F')),
  Media_Type varchar2(255 CHAR),
  Metadata_Complete TIMESTAMP,
  Batch_Class varchar2(255 CHAR),
  Language varchar2(255 CHAR),
  Remarks_from_Scan_Center varchar2(2047 CHAR),  --COLUMN renamed
  Remarks_about_book  varchar2(2047 CHAR),
  Record_Number varchar2(255 CHAR),
  Requesting_location varchar2(255 CHAR),  --site requesting metadata and probably will do the scan
  Owning_institution varchar2(255 CHAR),  --book owner
  Scanned_by varchar2(255 CHAR),  --scan location
  Scan_Operator varchar2(255 CHAR), --COMMENT NEW COLUMN
  Scan_Machine_id varchar2(255 CHAR), --COMMENT NEW COLUMN
  Scan_Metadata_Complete TIMESTAMP, --COMMENT NEW COLUMN
  Location varchar2(255 CHAR), --tiff folder location
  Scan_Start_Date TIMESTAMP,       --COMMENT NEW COLUMN
  Scan_Complete_Date TIMESTAMP,
  Scan_Image_Auditor varchar2(255 CHAR), --COMMENT NEW COLUMN
  Scan_IA_Start_Date TIMESTAMP,        --COMMENT NEW COLUMN
  Scan_IA_Complete_Date TIMESTAMP,    --COMMENT NEW COLUMN
  Files_Sent_to_Orem TIMESTAMP,       --COMMENT NEW COLUMN
  Scan_Num_of_Pages NUMBER(11,0),       --COMMENT NEW COLUMN
  Num_of_pages NUMBER(11,0),
  Files_Received_by_Orem TIMESTAMP,
  Image_Audit varchar2(255 CHAR),
  IA_Start_Date TIMESTAMP,
  IA_Complete_Date TIMESTAMP,
  Imported_by varchar2(255 CHAR),
  Imported_Date TIMESTAMP,
  Kofaxed_by varchar2(255 CHAR),
  Kofax_Start_date TIMESTAMP,
  PDF_Ready TIMESTAMP,
  Date_Released TIMESTAMP,
  Compression_Code varchar2(255 CHAR),
  Loaded_by varchar2(255 CHAR),
  Date_Loaded TIMESTAMP,
  Collection varchar2(255 CHAR),
  DNP varchar2(255 CHAR),
  DNP_deleted_off_line varchar2(255 CHAR),         --new column
  TN_Change_History varchar2(255 CHAR),
  PDF_Orem_Archived_Date TIMESTAMP,
  PDF_Orem_Drive_Serial_# varchar2(255 CHAR),
  PDF_Orem_Drive_Name varchar2(255 CHAR),
  PDF_Copy2_Archived_Date TIMESTAMP,
  PDF_Copy2_Drive_Serial_# varchar2(255 CHAR),
  PDF_Copy2_Drive_Name varchar2(255 CHAR),
  Tiff_Orem_Archived_Date TIMESTAMP,
  Tiff_Orem_Drive_Serial_# varchar2(255 CHAR),
  Tiff_Orem_Drive_Name varchar2(255 CHAR),
  Tiff_Copy2_Archived_Date TIMESTAMP,
  Tiff_Copy2_Drive_Serial_# varchar2(255 CHAR),
  Tiff_Copy2_Drive_Name varchar2(255 CHAR),
  PDF_Sent_to_Load TIMESTAMP,
  Site varchar2(255 CHAR),  --kofax site
  URL varchar2(255 CHAR),
  PID varchar2(255 CHAR),
  Pages_Online NUMBER(11,0),
Subject varchar2(4000 CHAR),
filmno varchar2(255 CHAR) , 
Pages_physical_description varchar2(255 CHAR) , 
Summary varchar2(2047 CHAR) , 
dgsno varchar2(255 CHAR)  , 
Date_original timestamp,
Publisher_original varchar2(255 CHAR),
Sent_to_scan timestamp
);
 
PROMPT Creating Primary Key Constraint PK_IaBookMetadata on table IaBookMetadata ... 
ALTER TABLE IaBookMetadata
ADD CONSTRAINT PK_IaBookMetadata PRIMARY KEY
(
  TN
)
ENABLE
;

ALTER TABLE IaBookMetadata  add constraint fk_site_IaBookMetadata  foreign key(Site) references SITE(ID);
ALTER TABLE  IaBookMetadata  add constraint fk_site_IaBookMetadata2 foreign key(Scanned_by) references SITE(ID);
ALTER TABLE IaBookMetadata  add constraint fk_site_IaBookMetadata3 foreign key(Requesting_Location) references SITE(ID);
ALTER TABLE IaBookMetadata add constraint fk_site_IaBookMetadata4 foreign key(owning_institution) references SITE(ID);
ALTER TABLE IaBookMetadata   add constraint fk_media_IaBookMetadata  foreign key(media_type) references MEDIA(ID);
