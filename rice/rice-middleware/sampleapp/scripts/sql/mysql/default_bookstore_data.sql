--
-- Copyright 2005-2014 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--



TRUNCATE TABLE bk_order_entry_t
/
INSERT INTO bk_order_entry_t (bk_order_entry_id,doc_hdr_id,book_id,quantity,unit_price,discount,total_price,obj_id,ver_nbr)
 VALUES ('1','1','1','1','100','10','90','6bbbdb82-d614-49c6-8716-4234e72f9f5e','1')
/
INSERT INTO bk_order_entry_t (bk_order_entry_id,doc_hdr_id,book_id,quantity,unit_price,discount,total_price,obj_id,ver_nbr)
 VALUES ('2','2','2','1','100','10','90','febbdb54-d614-49c6-8716-4234e72f9f5e','1')
/

TRUNCATE TABLE bk_order_doc_t
/
INSERT INTO bk_order_doc_t (doc_hdr_id,obj_id,ver_nbr)
  VALUES (1,'a1','a1')
/
INSERT INTO bk_order_doc_t (doc_hdr_id,obj_id,ver_nbr)
  VALUES (1,'a1','a1')
/

TRUNCATE TABLE bk_book_typ_t
/
INSERT INTO bk_book_typ_t (typ_cd,nm,desc_txt,actv_ind,obj_id,ver_nbr)
  VALUES ('ROM', 'Romantic', 'Romantic Books', 'Y', '6bbbdb82-d614-49c2-8716-4234e72f9f5e', 1)
/
INSERT INTO bk_book_typ_t (typ_cd,nm,desc_txt,actv_ind,obj_id,ver_nbr)
  VALUES ('SCI-FI', 'Science Fiction', 'Science Fiction Story', 'Y', '482b3394-0327-4e93-bd80-c5dc3b2a9e1f', 1)
/
 

TRUNCATE TABLE bk_book_t
/
INSERT INTO bk_book_t (book_id,title,author,typ_cd,isbn,publisher,pub_date,obj_id,ver_nbr,price,rating)
  VALUES ('1','i See','','ROM','9781402894626','Rupa Publishers Ltd.','2008-09-16 13:05:00','482b3394-0327-4e93-bd80-c5dc3b2a9e34','1','34.43','87')
/
INSERT INTO bk_book_t (book_id,title,author,typ_cd,isbn,publisher,pub_date,obj_id,ver_nbr,price,rating)
  VALUES ('2','Galactico','','SCI-FI','9781402894634','Rupa Publishers Ltd.','2008-09-16 20:05:00','482b3394-0327-4ee5-bd80-c5dc3b2a9e34','1','12.43','90')
/

TRUNCATE TABLE bk_book_author_t
/
INSERT INTO bk_book_author_t (book_id,author_id)
  VALUES ('1','1')
/
INSERT INTO bk_book_author_t (book_id,author_id)
  VALUES ('1','2')
/
INSERT INTO bk_book_author_t (book_id,author_id)
  VALUES ('2','1')
/

TRUNCATE TABLE bk_author_t
/
INSERT INTO bk_author_t (author_id,nm,address,email,phone_nbr,actv_ind,obj_id,ver_nbr)
  VALUES ('1','Roshan Mahanama','','roshan@jimail.com','123-123-1233','Y','a03ad608-84fa-4c89-8410-0a91ed56cb66', 1)
/
INSERT INTO bk_author_t (author_id,nm,address,email,phone_nbr,actv_ind,obj_id,ver_nbr)
  VALUES ('2','James Franklin','','jfranklin@jimail.com','999-433-4323','Y','a03ad608-84fa-4c89-8410-0a91ed56cb32', 1)
/

TRUNCATE TABLE bk_author_account_t
/
INSERT INTO bk_author_account_t (author_id,account_number,bank_name)
  VALUES ('1','123123123123123','Money Deposit Bank Ltd')
/
INSERT INTO bk_author_account_t (author_id,account_number,bank_name)
  VALUES ('2','123123456456456','Money Deposit Bank Ltd')
/

TRUNCATE TABLE bk_address_typ_t
/
INSERT INTO bk_address_typ_t (addr_typ,desc_txt,actv_ind,obj_id,ver_nbr)
  VALUES ('Office', 'Official Address', 'Y', 'a03ad608-84fa-4c89-8410-0a91ed56cb66', 1)
/
INSERT INTO bk_address_typ_t (addr_typ,desc_txt,actv_ind,obj_id,ver_nbr)
  VALUES ('Residence', 'Residential Address', 'Y', 'b8190679-7cfe-49c9-bd99-6b264f700f0d', 1)
/

TRUNCATE TABLE bk_address_t
/
INSERT INTO bk_address_t (address_id,author_id,addr_typ,street1,street2,city,provience,country,actv_ind,obj_id,ver_nbr)
  VALUES ('1','1','Residence','Strt1R','Strt2R','CityR','ProvinceR','CountryR','Y', 'b8190679-7cfe-49c9-bd99-6b264f700f0d', 1)
/
INSERT INTO bk_address_t (address_id,author_id,addr_typ,street1,street2,city,provience,country,actv_ind,obj_id,ver_nbr)
  VALUES ('2','1','Office','Strt1O','Strt2O','CityO','ProvinceO','CountryO','Y', 'b8190679-7cfe-49c9-bd99-6b264f700f03', 1)
/
