select * from book where scanned_by = 'Mid-Continent Public Library';
update book set scanned_by = 'Midwest Genealogy Center' where scanned_by = 'Mid-Continent Public Library';

select * from book where site = 'Mid-Continent Public Library';
update book set site  = 'Midwest Genealogy Center' where site  = 'Mid-Continent Public Library'

select * from book where owning_institution = 'Mid-Continent Public Library';
update book set owning_institution = 'Midwest Genealogy Center' where owning_institution = 'Mid-Continent Public Library'

select * from book where Requesting_Location = 'Mid-Continent Public Library';
update book set Requesting_Location = 'Midwest Genealogy Center' where Requesting_Location = 'Mid-Continent Public Library'

select * from bookMETADATA where owning_institution = 'Mid-Continent Public Library';
update bookMETADATA  set owning_institution = 'Midwest Genealogy Center' where owning_institution = 'Mid-Continent Public Library'

select * from bookMETADATA where Requesting_Location = 'Mid-Continent Public Library';
update bookMETADATA  set Requesting_Location = 'Midwest Genealogy Center' where Requesting_Location = 'Mid-Continent Public Library'

select * from users where primary_Location = 'Mid-Continent Public Library';
update users set primary_location = 'Midwest Genealogy Center' where  primary_location = 'Mid-Continent Public Library'

delete from site where id = 'Mid-Continent Public Library'


**********************************

INSERT INTO propertyright  (ID) values ( 'Copyright Protected');
update book set property_right = 'Copyright Protected' where property_right = 'Copy Protected';
delete from propertyright where id =  'Copy Protected';

**********************************

select count(*) from book where date_loaded is not null and scan_complete_date is null;
1  update book set scan_complete_date = files_received_by_orem where scan_complete_date is null
2  update book set scan_complete_date = date_loaded where scan_complete_date is null

select count(*) from book where files_received_by_orem is not null and scan_complete_date is null

3 Also run update (after updating scan_complete_date above):  update book set scan_start_date = scan_complete_date where scan_start_date is null

******************
INSERT INTO publication_type (ID) values ( 'Book');