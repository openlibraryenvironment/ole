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

--   -------------------------------------------------- 
--   Created On : Wednesday, 19 May, 2011 
--   DBMS       : Oracle 
--   ------------------------

--  Drop Tables, Stored Procedures and Views
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_ORDER_ENTRY_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_ORDER_ENTRY_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'BK_ORDER_ENTRY_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE BK_ORDER_ENTRY_S'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_ORDER_DOC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_ORDER_DOC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_BOOK_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_BOOK_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_BOOK_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_BOOK_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'BK_BOOK_ID_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE BK_BOOK_ID_S'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_BOOK_AUTHOR_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_BOOK_AUTHOR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_AUTHOR_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_AUTHOR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'BK_AUTHOR_ID_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE BK_AUTHOR_ID_S'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_AUTHOR_ACCOUNT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_AUTHOR_ACCOUNT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_ADDRESS_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_ADDRESS_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BK_ADDRESS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BK_ADDRESS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'BK_ADDRESS_ID_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE BK_ADDRESS_ID_S'; END IF;
END;
/



--  Create Tables 
CREATE TABLE bk_order_entry_t  
(
	bk_order_entry_id INTEGER  NOT NULL , 
	doc_hdr_id VARCHAR2 (14) NOT NULL , 
	book_id INTEGER  NOT NULL , 
	quantity SMALLINT  NOT NULL , 
	unit_price DECIMAL (15, 2) , 
	discount DECIMAL (5, 2) , 
	total_price DECIMAL (15, 2) , 
	obj_id VARCHAR2 (36) NOT NULL , 
	ver_nbr DECIMAL (8) DEFAULT  1 NOT NULL , 
	PRIMARY KEY (bk_order_entry_id) 
)
/


CREATE  SEQUENCE  bk_order_entry_s
	START WITH 1
	INCREMENT BY 1
/

CREATE TABLE bk_order_doc_t  
(
	doc_hdr_id INTEGER  NOT NULL , 
	obj_id VARCHAR2 (36) NOT NULL , 
	ver_nbr DECIMAL (8) DEFAULT  1 NOT NULL , 
	PRIMARY KEY (doc_hdr_id) 
)
/

CREATE TABLE bk_book_typ_t  
(
	typ_cd VARCHAR2 (40) NOT NULL , 
	nm VARCHAR2 (100) , 
	desc_txt VARCHAR2 (255) , 
	actv_ind VARCHAR2 (1) DEFAULT  'Y' , 
	obj_id VARCHAR2 (36) NOT NULL , 
	ver_nbr DECIMAL (8) DEFAULT  1 NOT NULL , 
	PRIMARY KEY (typ_cd) 
)
/

CREATE TABLE bk_book_t  
(
	book_id INTEGER  NOT NULL , 
	title VARCHAR2 (100) , 
	author VARCHAR2 (100) , 
	typ_cd VARCHAR2 (40) , 
	isbn VARCHAR2 (17) , 
	publisher VARCHAR2 (100) , 
	pub_date DATE  , 
	obj_id VARCHAR2 (36) NOT NULL , 
	ver_nbr DECIMAL (8) DEFAULT  1 NOT NULL , 
	price DECIMAL (15, 2) , 
	rating INTEGER  , 
	PRIMARY KEY (book_id) 
)
/

CREATE  SEQUENCE  bk_book_id_s
	START WITH 1
	INCREMENT BY 1
/

CREATE TABLE bk_book_author_t  
(
	book_id INTEGER  NOT NULL , 
	author_id INTEGER  NOT NULL , 
	PRIMARY KEY (book_id, author_id) 
)
/


CREATE TABLE bk_author_t  
(
	author_id INTEGER  NOT NULL , 
	nm VARCHAR2 (100) , 
	address VARCHAR2 (200) , 
	email VARCHAR2 (50) , 
	phone_nbr VARCHAR2 (20) , 
	actv_ind VARCHAR2 (1) DEFAULT  'Y' , 
	obj_id VARCHAR2 (36) NOT NULL , 
	ver_nbr DECIMAL (8) DEFAULT  1 NOT NULL , 
	PRIMARY KEY (author_id) 
)
/

CREATE  SEQUENCE  bk_author_id_s
	START WITH 1
	INCREMENT BY 1
/

CREATE TABLE bk_author_account_t  
(
	author_id INTEGER  NOT NULL , 
	account_number VARCHAR2 (50) , 
	bank_name VARCHAR2 (100) , 
	PRIMARY KEY (author_id) 
)
/


CREATE TABLE bk_address_typ_t  
(
	addr_typ VARCHAR2 (40) NOT NULL , 
	desc_txt VARCHAR2 (255) , 
	actv_ind VARCHAR2 (1) DEFAULT  'Y' , 
	obj_id VARCHAR2 (36) NOT NULL , 
	ver_nbr DECIMAL (8) DEFAULT  1 NOT NULL , 
	PRIMARY KEY (addr_typ) 
)
/

CREATE TABLE bk_address_t  
(
	address_id INTEGER  NOT NULL , 
	author_id INTEGER  , 
	addr_typ VARCHAR2 (40) , 
	street1 VARCHAR2 (50) , 
	street2 VARCHAR2 (50) , 
	city VARCHAR2 (50) , 
	provience VARCHAR2 (50) , 
	country VARCHAR2 (50) , 
	actv_ind VARCHAR2 (1) DEFAULT  'Y' ,
	obj_id VARCHAR2 (36) NOT NULL , 
	ver_nbr DECIMAL (8) DEFAULT  1 NOT NULL , 
	PRIMARY KEY (address_id) 
)
/

CREATE  SEQUENCE  bk_address_id_s
	START WITH 1
	INCREMENT BY 1
/

--  Create Foreign Key Constraints 
ALTER TABLE bk_order_entry_t 
ADD 
CONSTRAINT bk_order_entry_t_bk_book_t FOREIGN KEY (book_id) REFERENCES bk_book_t (book_id) 
/
ALTER TABLE bk_order_entry_t 
ADD
CONSTRAINT bk_order_entry_t_krns_do_ADV1 FOREIGN KEY (doc_hdr_id) REFERENCES krns_doc_hdr_t (doc_hdr_id) 
/