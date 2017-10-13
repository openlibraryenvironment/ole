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


--
-- KULRICE-8539: Apply SQL for new KRMS tables to the 2.2 master db
--
-- KULRICE-7367: Implement KRMS Type-Type Relations feature
-- KULRICE-7368: Implement KRMS Natural Language Translation feature
-- KULRICE-7369: Implement KRMS Reference Object Bindings feature
--


create sequence krms_ref_obj_krms_obj_s increment by 1 start with 10000 nomaxvalue nocycle nocache order
/
create sequence krms_typ_reln_s increment by 1 start with 10000 nomaxvalue nocycle nocache order
/
create sequence krms_nl_usage_s increment by 1 start with 10000 nomaxvalue nocycle nocache order
/
create sequence krms_nl_tmpl_s increment by 1 start with 10000 nomaxvalue nocycle nocache order
/
create sequence krms_nl_tmpl_attr_s increment by 1 start with 10000 nomaxvalue nocycle nocache order
/
create sequence krms_nl_usage_attr_s increment by 1 start with 10000 nomaxvalue nocycle nocache order
/

CREATE  TABLE krms_nl_usage_t(
  nl_usage_id VARCHAR2(40) NOT NULL ,
  nm VARCHAR2(255) NOT NULL,
  nmspc_cd VARCHAR2(40)  NOT NULL,
  desc_txt VARCHAR2(255) NULL,
  actv VARCHAR2(1) DEFAULT 'Y' NOT NULL,
  ver_nbr NUMBER(8) DEFAULT 0 NOT NULL,
  PRIMARY KEY (nl_usage_id),
  CONSTRAINT krms_nl_usage_tc1 UNIQUE (nm, nmspc_cd)
)
/
CREATE  TABLE  krms_nl_usage_attr_t (
  nl_usage_attr_id VARCHAR2(40)  NOT NULL ,
  nl_usage_id VARCHAR2(40)  NOT NULL ,
  attr_defn_id VARCHAR2(40)  NOT NULL ,
  attr_val VARCHAR2(400) NULL ,
  ver_nbr NUMBER(8) DEFAULT 0  NOT NULL ,
  PRIMARY KEY (nl_usage_attr_id) ,
  CONSTRAINT krms_nl_usage_attr_tc1 UNIQUE (nl_usage_id, attr_defn_id),
  CONSTRAINT krms_nl_usage_attr_fk1
    FOREIGN KEY (nl_usage_id )
    REFERENCES krms_nl_usage_t (nl_usage_id ) ,
  CONSTRAINT krms_nl_usage_attr_fk2
    FOREIGN KEY (attr_defn_id )
    REFERENCES krms_attr_defn_t (attr_defn_id )
)
/
CREATE TABLE krms_nl_tmpl_t (
  nl_tmpl_id VARCHAR2(40) NOT NULL,
  lang_cd VARCHAR2(2) NOT NULL,
  nl_usage_id VARCHAR2(40) NOT NULL,
  typ_id VARCHAR2(40) NOT NULL,
  tmpl VARCHAR2(4000) NOT NULL,
  ver_nbr NUMBER(8) DEFAULT 0  NOT NULL,
  CONSTRAINT krms_nl_tmpl_fk1 FOREIGN KEY (nl_usage_id) REFERENCES krms_nl_usage_t (nl_usage_id),
  CONSTRAINT krms_typ_t FOREIGN KEY (typ_id) REFERENCES krms_typ_t (typ_id),
  PRIMARY KEY (nl_tmpl_id),
  CONSTRAINT krms_nl_tmpl_tc1 UNIQUE (lang_cd, nl_usage_id, typ_id)
)
/
CREATE  TABLE krms_typ_reln_t (
  TYP_RELN_ID VARCHAR2(40) NOT NULL ,
  FROM_TYP_ID VARCHAR2(40) NOT NULL ,
  TO_TYP_ID VARCHAR2(40) NOT NULL ,
  RELN_TYP VARCHAR2(40) NOT NULL ,
  SEQ_NO NUMBER(5) NOT NULL,
  VER_NBR NUMBER(8) DEFAULT '0' NOT NULL,
  ACTV VARCHAR2(1) DEFAULT 'Y' NOT NULL,
  PRIMARY KEY (TYP_RELN_ID) ,
  CONSTRAINT KRMS_TYP_RELN_TC1 UNIQUE (FROM_TYP_ID, TO_TYP_ID, RELN_TYP) ,
  CONSTRAINT KRMS_TYP_RELN_FK1 FOREIGN KEY (FROM_TYP_ID ) REFERENCES krms_typ_t (TYP_ID ),
  CONSTRAINT KRMS_TYP_RELN_FK2 FOREIGN KEY (TO_TYP_ID ) REFERENCES krms_typ_t (TYP_ID )
)
/
CREATE  TABLE krms_ref_obj_krms_obj_t(
  ref_obj_krms_obj_id VARCHAR2(40) NOT NULL,
  collection_nm VARCHAR2(40) NULL,
  krms_obj_id VARCHAR2(40) NOT NULL,
  krms_dscr_typ VARCHAR2(40) NOT NULL,
  ref_obj_id VARCHAR2(255) NOT NULL,
  ref_dscr_typ VARCHAR2(255) NOT NULL,
  nmspc_cd VARCHAR2(40)  NOT NULL,
  actv VARCHAR2(1) DEFAULT 'Y'  NOT NULL ,
  ver_nbr NUMBER(8) DEFAULT 0  NOT NULL,
  PRIMARY KEY (ref_obj_krms_obj_id),
  CONSTRAINT krms_ref_obj_krms_obj_tc1 UNIQUE (collection_nm, krms_obj_id, krms_dscr_typ, ref_obj_id, ref_dscr_typ, nmspc_cd)
)
/
CREATE  TABLE  krms_nl_tmpl_attr_t (
  nl_tmpl_attr_id VARCHAR2(40)  NOT NULL ,
  nl_tmpl_id VARCHAR2(40)  NOT NULL ,
  attr_defn_id VARCHAR2(40)  NOT NULL ,
  attr_val VARCHAR2(400) NULL ,
  ver_nbr NUMBER(8) DEFAULT 0  NOT NULL ,
  PRIMARY KEY (nl_tmpl_attr_id) ,
  CONSTRAINT krms_nl_tmpl_attr_tc1 UNIQUE (nl_tmpl_id, attr_defn_id),
  CONSTRAINT krms_nl_tmpl_attr_fk1
    FOREIGN KEY (nl_tmpl_id )
    REFERENCES krms_nl_tmpl_t (nl_tmpl_id ) ,
  CONSTRAINT krms_nl_tmpl_attr_fk2
    FOREIGN KEY (attr_defn_id )
    REFERENCES krms_attr_defn_t (attr_defn_id )
)
/