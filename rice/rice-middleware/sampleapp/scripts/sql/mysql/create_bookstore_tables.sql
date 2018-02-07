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
--   Created On : Wednesday, 13 April, 2011 
--   DBMS       : MySql 
--   -------------------------------------------------- 


--  Drop Tables, Stored Procedures and Views 
DROP TABLE IF EXISTS bk_order_entry_t
/
DROP TABLE IF EXISTS bk_order_entry_s
/
DROP TABLE IF EXISTS bk_order_doc_t
/
DROP TABLE IF EXISTS bk_book_typ_t
/
DROP TABLE IF EXISTS bk_book_t
/
DROP TABLE IF EXISTS bk_book_id_s
/
DROP TABLE IF EXISTS bk_book_author_t
/
DROP TABLE IF EXISTS bk_author_t
/
DROP TABLE IF EXISTS bk_author_id_s
/
DROP TABLE IF EXISTS bk_author_account_t
/
DROP TABLE IF EXISTS bk_address_typ_t
/
DROP TABLE IF EXISTS bk_address_t
/
DROP TABLE IF EXISTS bk_address_id_s
/


# -----------------------------------------------------------------------
# bk_order_entry_t
# -----------------------------------------------------------------------
--  Create Tables 
CREATE TABLE bk_order_entry_t
(
	bk_order_entry_id BIGINT NOT NULL,
	doc_hdr_id VARCHAR(14) NOT NULL,
	book_id BIGINT NOT NULL,
	quantity SMALLINT NOT NULL,
	unit_price DECIMAL(15,2),
	discount DECIMAL(5,2),
	total_price DECIMAL(15,2),
	obj_id VARCHAR(36) NOT NULL,
	ver_nbr DECIMAL(8) NOT NULL DEFAULT 1,
	PRIMARY KEY (bk_order_entry_id),
	KEY (book_id)
) 
/

# -----------------------------------------------------------------------
# bk_order_entry_s
# -----------------------------------------------------------------------
CREATE TABLE bk_order_entry_s
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (id)
) 
/

# -----------------------------------------------------------------------
# bk_order_doc_t
# -----------------------------------------------------------------------
CREATE TABLE bk_order_doc_t
(
	doc_hdr_id BIGINT NOT NULL,
	obj_id VARCHAR(36) NOT NULL,
	ver_nbr DECIMAL(8) NOT NULL DEFAULT 1,
	PRIMARY KEY (doc_hdr_id),
	UNIQUE bk_order_doc_tc0(obj_id)
) 
/

# -----------------------------------------------------------------------
# bk_book_typ_t
# -----------------------------------------------------------------------
CREATE TABLE bk_book_typ_t
(
	typ_cd VARCHAR(40) NOT NULL,
	nm VARCHAR(100),
	desc_txt VARCHAR(255),
	actv_ind VARCHAR(1) DEFAULT 'Y',
	obj_id VARCHAR(36) NOT NULL,
	ver_nbr DECIMAL(8) NOT NULL DEFAULT 1,
	PRIMARY KEY (typ_cd),
	UNIQUE bk_book_typ_tc0(obj_id)
)  COMMENT='This tabel is use to store book types.'
/

# -----------------------------------------------------------------------
# bk_book_t
# -----------------------------------------------------------------------
CREATE TABLE bk_book_t
(
	book_id BIGINT NOT NULL,
	title VARCHAR(100),
	author VARCHAR(100),
	typ_cd VARCHAR(40),
	isbn VARCHAR(17),
	publisher VARCHAR(100),
	pub_date DATE,
	obj_id VARCHAR(36) NOT NULL,
	ver_nbr DECIMAL(8) NOT NULL DEFAULT 1,
	price DECIMAL(15,2),
	rating INTEGER,
	PRIMARY KEY (book_id),
	UNIQUE bk_book_tc0(obj_id)
)  COMMENT='This table is use to store book information.'
/

# -----------------------------------------------------------------------
# bk_book_id_s
# -----------------------------------------------------------------------
CREATE TABLE bk_book_id_s
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (id)
)  COMMENT='This table is use to store book ids.'
/

# -----------------------------------------------------------------------
# bk_book_author_t
# -----------------------------------------------------------------------
CREATE TABLE bk_book_author_t
(
	book_id BIGINT NOT NULL,
	author_id BIGINT NOT NULL,
	PRIMARY KEY (book_id, author_id)
) 
/

# -----------------------------------------------------------------------
# bk_author_t
# -----------------------------------------------------------------------
CREATE TABLE bk_author_t
(
	author_id BIGINT NOT NULL,
	nm VARCHAR(100),
	address VARCHAR(200),
	email VARCHAR(50),
	phone_nbr VARCHAR(20),
	actv_ind VARCHAR(1) DEFAULT 'Y',
	obj_id VARCHAR(36) NOT NULL,
	ver_nbr DECIMAL(8) NOT NULL DEFAULT 1,
	PRIMARY KEY (author_id),
	UNIQUE bk_author_tc0(obj_id)
) 
/

# -----------------------------------------------------------------------
# bk_author_id_s
# -----------------------------------------------------------------------
CREATE TABLE bk_author_id_s
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (id)
) 
/

# -----------------------------------------------------------------------
# bk_author_account_t
# -----------------------------------------------------------------------
CREATE TABLE bk_author_account_t
(
	author_id BIGINT NOT NULL,
	account_number VARCHAR(50),
	bank_name VARCHAR(100),
	PRIMARY KEY (author_id)
) 
/

# -----------------------------------------------------------------------
# bk_address_typ_t
# -----------------------------------------------------------------------
CREATE TABLE bk_address_typ_t
(
	addr_typ VARCHAR(40) NOT NULL,
	desc_txt VARCHAR(255),
	actv_ind VARCHAR(1) DEFAULT 'Y',
	obj_id VARCHAR(36) NOT NULL,
	ver_nbr DECIMAL(8) NOT NULL DEFAULT 1,
	PRIMARY KEY (addr_typ),
	UNIQUE bk_address_typ_tc0(obj_id)
)  COMMENT='This tabel is use to store book types.'
/

# -----------------------------------------------------------------------
# bk_address_t
# -----------------------------------------------------------------------
CREATE TABLE bk_address_t
(
	address_id BIGINT NOT NULL,
	author_id BIGINT,
	addr_typ VARCHAR(40),
	street1 VARCHAR(50),
	street2 VARCHAR(50),
	city VARCHAR(50),
	provience VARCHAR(50),
	country VARCHAR(50),
	actv_ind VARCHAR(1) DEFAULT 'Y',
	obj_id VARCHAR(36) NOT NULL,
	ver_nbr DECIMAL(8) NOT NULL DEFAULT 1,
	PRIMARY KEY (address_id),
	UNIQUE bk_address_tc0(obj_id)
) 
/

# -----------------------------------------------------------------------
# bk_address_id_s
# -----------------------------------------------------------------------
CREATE TABLE bk_address_id_s
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (id)
) 
/


--  Create Foreign Key Constraints 
ALTER TABLE bk_order_entry_t ADD CONSTRAINT bk_order_entry_t_bk_book_t 
	FOREIGN KEY (book_id) REFERENCES bk_book_t (book_id)
/

ALTER TABLE bk_order_entry_t ADD CONSTRAINT bk_order_entry_t_krns_doc_hdr_t
	FOREIGN KEY (doc_hdr_id) REFERENCES krns_doc_hdr_t (doc_hdr_id)
/
