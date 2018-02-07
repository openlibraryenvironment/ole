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

CREATE TABLE krms_ref_obj_krms_obj_s (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
;
CREATE TABLE krms_typ_reln_s (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
;
CREATE TABLE krms_nl_usage_s (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
;
CREATE TABLE krms_nl_tmpl_s (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
;

CREATE TABLE krms_nl_tmpl_attr_s (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
;

CREATE TABLE krms_nl_usage_attr_s (
  id bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
;
CREATE  TABLE krms_nl_usage_t(
  nl_usage_id VARCHAR(40) NOT NULL , 
  nm VARCHAR(255) NOT NULL, 
  nmspc_cd VARCHAR(40)  NOT NULL, 
  desc_txt VARCHAR(255) NULL,
  actv VARCHAR(1) DEFAULT 'Y'  NOT NULL ,   
  ver_nbr DECIMAL(8,0) DEFAULT 0  NOT NULL,
  PRIMARY KEY (nl_usage_id),
  UNIQUE INDEX krms_nl_usage_tc1 (nm ASC, nmspc_cd ASC)
) ENGINE = InnoDB
;
CREATE  TABLE  krms_nl_usage_attr_t (
  nl_usage_attr_id VARCHAR(40)  NOT NULL ,
  nl_usage_id VARCHAR(40)  NOT NULL ,
  attr_defn_id VARCHAR(40)  NOT NULL ,
  attr_val VARCHAR(400) NULL ,
  ver_nbr DECIMAL(8,0) DEFAULT 0  NOT NULL ,
  PRIMARY KEY (nl_usage_attr_id) ,
  UNIQUE INDEX krms_nl_usage_attr_tc1 (nl_usage_id ASC, attr_defn_id ASC),
  CONSTRAINT krms_nl_usage_attr_fk1
    FOREIGN KEY (nl_usage_id )
    REFERENCES krms_nl_usage_t (nl_usage_id ) ,
  CONSTRAINT krms_nl_usage_attr_fk2
    FOREIGN KEY (attr_defn_id )
    REFERENCES krms_attr_defn_t (attr_defn_id )
) ENGINE = InnoDB
;
CREATE TABLE krms_nl_tmpl_t (
  nl_tmpl_id VARCHAR(40) NOT NULL,
  lang_cd VARCHAR(2) NOT NULL,
  nl_usage_id VARCHAR(40) NOT NULL,
  typ_id VARCHAR(40) NOT NULL,
  tmpl VARCHAR(4000) NOT NULL,
  ver_nbr DECIMAL(8,0) DEFAULT 0  NOT NULL,
  CONSTRAINT krms_nl_tmpl_fk1 FOREIGN KEY (nl_usage_id) REFERENCES krms_nl_usage_t (nl_usage_id),
  CONSTRAINT krms_typ_t FOREIGN KEY (typ_id) REFERENCES krms_typ_t (typ_id),
  PRIMARY KEY (nl_tmpl_id),
  UNIQUE INDEX krms_nl_tmpl_tc1 (lang_cd ASC, nl_usage_id ASC, typ_id ASC)
) ENGINE = InnoDB
;
CREATE  TABLE krms_typ_reln_t (
  TYP_RELN_ID VARCHAR(40) NOT NULL ,
  FROM_TYP_ID VARCHAR(40) NOT NULL ,
  TO_TYP_ID VARCHAR(40) NOT NULL ,
  RELN_TYP VARCHAR(40) NOT NULL ,
  SEQ_NO decimal(5,0) NOT NULL,
  VER_NBR decimal(8,0) NOT NULL DEFAULT '0', 
  ACTV VARCHAR(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (TYP_RELN_ID) ,
  UNIQUE INDEX KRMS_TYP_RELN_TC1 (FROM_TYP_ID ASC, TO_TYP_ID ASC, RELN_TYP ASC) ,
  CONSTRAINT KRMS_TYP_RELN_FK1 FOREIGN KEY (FROM_TYP_ID ) REFERENCES krms_typ_t (TYP_ID ),
  CONSTRAINT KRMS_TYP_RELN_FK2 FOREIGN KEY (TO_TYP_ID ) REFERENCES krms_typ_t (TYP_ID )
) ENGINE = InnoDB
;
CREATE  TABLE krms_ref_obj_krms_obj_t(
  ref_obj_krms_obj_id VARCHAR(40) NOT NULL,
  collection_nm VARCHAR(40) NULL,
  krms_obj_id VARCHAR(40) NOT NULL,
  krms_dscr_typ VARCHAR(40) NOT NULL,
  ref_obj_id VARCHAR(255) NOT NULL,
  ref_dscr_typ VARCHAR(255) NOT NULL,
  nmspc_cd VARCHAR(40)  NOT NULL,
  actv VARCHAR(1) DEFAULT 'Y'  NOT NULL ,   
  ver_nbr DECIMAL(8,0) DEFAULT 0  NOT NULL,
  PRIMARY KEY (ref_obj_krms_obj_id),
  UNIQUE INDEX krms_ref_obj_krms_obj_tc1 (collection_nm ASC, krms_obj_id ASC, krms_dscr_typ ASC, ref_obj_id ASC, ref_dscr_typ ASC, nmspc_cd ASC)
) ENGINE = InnoDB
;
CREATE  TABLE  krms_nl_tmpl_attr_t (
  nl_tmpl_attr_id VARCHAR(40)  NOT NULL ,
  nl_tmpl_id VARCHAR(40)  NOT NULL ,
  attr_defn_id VARCHAR(40)  NOT NULL ,
  attr_val VARCHAR(400) NULL ,
  ver_nbr DECIMAL(8,0) DEFAULT 0  NOT NULL ,
  PRIMARY KEY (nl_tmpl_attr_id) ,
  UNIQUE INDEX krms_nl_tmpl_attr_tc1 (nl_tmpl_id ASC, attr_defn_id ASC),
  CONSTRAINT krms_nl_tmpl_attr_fk1
    FOREIGN KEY (nl_tmpl_id )
    REFERENCES krms_nl_tmpl_t (nl_tmpl_id ) ,
  CONSTRAINT krms_nl_tmpl_attr_fk2
    FOREIGN KEY (attr_defn_id )
    REFERENCES krms_attr_defn_t (attr_defn_id )
) ENGINE = InnoDB
;