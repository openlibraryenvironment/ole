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

# -----------------------------------------------------------------------
# TRV_ACCT
# -----------------------------------------------------------------------
drop table if exists TRV_ACCT
/

CREATE TABLE TRV_ACCT (
  ACCT_NUM varchar(10) NOT NULL,
  ACCT_NAME varchar(50),
  ACCT_TYPE varchar(100),
  ACCT_FO_ID decimal(14,0),
  OBJ_ID varchar(36),
  VER_NBR decimal(8,0) DEFAULT 0,
  SUB_ACCT varchar(10),
  SUB_ACCT_NAME varchar(50),
  CREATE_DT datetime,
  SUBSIDIZED_PCT float,
  PRIMARY KEY (ACCT_NUM)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

# -----------------------------------------------------------------------
# TRV_ACCT_EXT
# -----------------------------------------------------------------------
drop table if exists TRV_ACCT_EXT
/

CREATE TABLE TRV_ACCT_EXT
(
      ACCT_NUM VARCHAR(10)
        , ACCT_TYPE VARCHAR(100)
    
    , CONSTRAINT TRV_ACCT_EXTP1 PRIMARY KEY(ACCT_NUM,ACCT_TYPE)



) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

# -----------------------------------------------------------------------
# TRV_ACCT_FO
# -----------------------------------------------------------------------
drop table if exists TRV_ACCT_FO
/

CREATE TABLE TRV_ACCT_FO
(
      ACCT_FO_ID DECIMAL(14)
        , ACCT_FO_USER_NAME VARCHAR(50) NOT NULL
    
    , CONSTRAINT TRV_ACCT_FOP1 PRIMARY KEY(ACCT_FO_ID)



) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

# -----------------------------------------------------------------------
# TRV_ACCT_TYPE
# -----------------------------------------------------------------------
drop table if exists TRV_ACCT_TYPE
/

CREATE TABLE TRV_ACCT_TYPE
(
      ACCT_TYPE VARCHAR(10)
        , ACCT_TYPE_NAME VARCHAR(50)
    
    , CONSTRAINT TRV_ACCT_TYPEP1 PRIMARY KEY(ACCT_TYPE)



) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

# -----------------------------------------------------------------------
# TRV_DOC_2
# -----------------------------------------------------------------------
drop table if exists TRV_DOC_2
/

CREATE TABLE TRV_DOC_2
(
      FDOC_NBR VARCHAR(14)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , FDOC_EXPLAIN_TXT VARCHAR(400)
        , REQUEST_TRAV VARCHAR(30) NOT NULL
        , TRAVELER VARCHAR(200)
        , ORG VARCHAR(60)
        , DEST VARCHAR(60)
    
    , CONSTRAINT TRV_DOC_2P1 PRIMARY KEY(FDOC_NBR)



) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

# -----------------------------------------------------------------------
# TRV_DOC_ACCT
# -----------------------------------------------------------------------
drop table if exists TRV_DOC_ACCT
/

CREATE TABLE TRV_DOC_ACCT
(
      DOC_HDR_ID VARCHAR(40)
        , ACCT_NUM VARCHAR(10)
    
    , CONSTRAINT TRV_DOC_ACCTP1 PRIMARY KEY(DOC_HDR_ID,ACCT_NUM)



) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/

# -----------------------------------------------------------------------
# TRV_FO_ID_S
# -----------------------------------------------------------------------
drop table if exists TRV_FO_ID_S
/

CREATE TABLE TRV_FO_ID_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE TRV_FO_ID_S auto_increment = 1000
/
ALTER TABLE TRV_ACCT
    ADD CONSTRAINT TRV_ACCT_FK1
    FOREIGN KEY (ACCT_FO_ID)
    REFERENCES TRV_ACCT_FO (ACCT_FO_ID)
/

# -----------------------------------------------------------------------------
# -- TRAV_DOC_2_ACCOUNTS
# -----------------------------------------------------------------------------
drop table if exists TRAV_DOC_2_ACCOUNTS
/
CREATE TABLE TRAV_DOC_2_ACCOUNTS
(
      FDOC_NBR VARCHAR(14)
        , ACCT_NUM VARCHAR(10)
        , CONSTRAINT TRAV_DOC_2_ACCOUNTSP1 PRIMARY KEY(FDOC_NBR,ACCT_NUM)
)
/

# -----------------------------------------------------------------------------
# -- TRAV_ATTACHMENT TEST TABLES
# -----------------------------------------------------------------------------
create table TRV_ATT_SAMPLE (attachment_id varchar2(30),
                              description varchar2(4000),
                              attachment_filename varchar2(300),
                              attachment_file_content_type varchar2(255),
                              attachment_file blob,
                              obj_id varchar2(36) not null,
                              ver_nbr number(8) default 0 not null,
                              primary key (attachment_id))
/
create table TRV_MULTI_ATT_SAMPLE (gen_id number(14,0) not null,
                              attachment_id varchar2(30),
                              description varchar2(4000),
                              attachment_filename varchar2(300),
                              attachment_file_content_type varchar2(255),
                              attachment_file blob,
                              obj_id varchar2(36) not null,
                              ver_nbr number(8) default 0 not null,
                              primary key (gen_id),
                              foreign key (attachment_id) references TRV_ATT_SAMPLE(attachment_id))
/

# -----------------------------------------------------------------------------
# Travel Approval
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS TRVL_ADV_T 
  (
    ID INT NOT NULL AUTO_INCREMENT ,
    FDOC_NBR VARCHAR(45) NULL ,
    FDOC_LINE_NBR INT NULL ,
    TVL_ADV_REQ DECIMAL NULL ,
    AR_CUST_ID VARCHAR(45) NULL ,
    AR_INV_DOC_NBR VARCHAR(45) NULL ,
    DUE_DT DATE NULL ,
    PYMT_MTHD VARCHAR(45) NULL ,
    FIN_COA_CD VARCHAR(45) NULL ,
    ACCOUNT_NBR VARCHAR(45) NULL ,
    SUB_ACCT_NBR VARCHAR(45) NULL ,
    FIN_OBJECT_CD VARCHAR(45) NULL ,
    FIN_SUB_OBJ_CD VARCHAR(45) NULL ,
    ADV_PMNT_RSN_CD VARCHAR(45) NULL ,
    TRVL_ADV_PLCY VARCHAR(45) NULL ,
    ADD_JUST VARCHAR(45) NULL ,
    TAX_RAM_NTF_DT DATE NULL ,
    OBJ_ID VARCHAR(45) NULL ,
    VER_NBR BIGINT NULL ,
    CONSTRAINT TRVL_ADV_T_TC0 UNIQUE (OBJ_ID),
    CONSTRAINT TRVL_ADV_T_TP1 PRIMARY KEY(ID)
  )
/

CREATE  TABLE IF NOT EXISTS TRVL_IMP_EXP_T
  (
    ID INT NOT NULL AUTO_INCREMENT ,
    FDOC_NBR VARCHAR(45) NULL ,
    FDOC_LINE_NBR INT NULL ,
    EXP_TYPE_CD VARCHAR(45) NULL ,
    EXP_PARENT_ID BIGINT NULL ,
    EXP_DESC VARCHAR(45) NULL ,
    EXP_DT DATE NULL ,
    EXP_AMT DECIMAL NULL ,
    CUR_RT DECIMAL NULL ,
    TEM_EXP_TYP_ID BIGINT NULL ,
    DV_EXP_CO_NM VARCHAR(45) NULL ,
    NON_REIMB_IND VARCHAR(45) NULL ,
    TAXABLE_IND VARCHAR(45) NULL ,
    MISG_RCPT_IND VARCHAR(45) NULL ,
    CONVERTED_AMT DECIMAL NULL ,
    RCPT_RQD_IND VARCHAR(45) NULL ,
    CARD_TYPE VARCHAR(45) NULL ,
    HIST_EXP_ID BIGINT NULL ,
    OBJ_ID VARCHAR(45) NULL ,
    VER_NBR BIGINT NULL ,
    CONSTRAINT TRVL_IMP_EXP_T_TC0 UNIQUE (OBJ_ID),
    CONSTRAINT TRVL_IMP_EXP_T_TP1 PRIMARY KEY(ID)
  )
/

CREATE  TABLE IF NOT EXISTS TRVL_EXP_T
  (
    ID INT NOT NULL AUTO_INCREMENT ,
    FDOC_NBR VARCHAR(45) NULL ,
    FDOC_LINE_NBR INT NULL ,
    EXP_TYPE_CD VARCHAR(45) NULL ,
    EXP_PARENT_ID BIGINT NULL ,
    EXP_DESC VARCHAR(45) NULL ,
    EXP_DT DATE NULL ,
    EXP_AMT DECIMAL NULL ,
    CUR_RT DECIMAL NULL ,
    TEM_EXP_TYP_ID BIGINT NULL ,
    DV_EXP_CO_NM VARCHAR(45) NULL ,
    NON_REIMB_IND VARCHAR(45) NULL ,
    TAXABLE_IND VARCHAR(45) NULL ,
    MISG_RCPT_IND VARCHAR(45) NULL ,
    CONVERTED_AMT DECIMAL NULL ,
    AIRFARE_SRC_CD VARCHAR(45) NULL ,
    CLASS_SVC_CODE VARCHAR(45) NULL ,
    MILEAGE_RT_ID INT NULL ,
    MILES INT NULL ,
    MILEAGE_OTHR_RT DECIMAL NULL ,
    RENTAL_CAR_INSURANCE VARCHAR(45) NULL ,
    OBJ_ID VARCHAR(45) NULL ,
    VER_NBR BIGINT NULL ,
    CONSTRAINT TRVL_EXP_T_TC0 UNIQUE (OBJ_ID),
    CONSTRAINT TRVL_EXP_T_TP1 PRIMARY KEY(ID)
  )
/

CREATE TABLE IF NOT EXISTS TRVL_PER_DIEM_T
    (
       ID                BIGINT(19) NOT NULL,
       TRIP_TYP_CD       VARCHAR(45) NULL,
       COUNTRY           VARCHAR(100) NULL,
       COUNTRY_NM        VARCHAR(100) NULL,
       PRI_DEST          VARCHAR(100) NULL,
       ACTV_IND          VARCHAR(1) NOT NULL,
       VER_NBR           DECIMAL(8, 0) DEFAULT 1 NOT NULL,
       OBJ_ID            VARCHAR(36) NOT NULL,
       CONSTRAINT TRVL_PER_DIEM_T_TC0 UNIQUE (OBJ_ID),
       CONSTRAINT TRVL_PER_DIEM_T_TP1 PRIMARY KEY(ID)
    )
/

CREATE  TABLE IF NOT EXISTS TRVL_TRANS_MD_T
  (
    CODE VARCHAR(45) NOT NULL,
    NAME VARCHAR(45) NOT NULL,
    ACTIVE VARCHAR(45) NOT NULL,
    OBJ_ID VARCHAR(45) NULL ,
    VER_NBR BIGINT NULL ,
    CONSTRAINT TRVL_TRANS_MD_T_TC0 UNIQUE (OBJ_ID),
    CONSTRAINT TRVL_TRANS_MD_T_TP1 PRIMARY KEY(CODE)
  )
/

CREATE  TABLE IF NOT EXISTS TRVL_TRANS_MD_DTL_T
  (
    FDOC_NBR VARCHAR(45) NULL ,
    TRANS_MODE_CD VARCHAR(45) NULL ,
    OBJ_ID VARCHAR(45) NULL ,
    VER_NBR BIGINT NULL ,
    CONSTRAINT TRVL_TRANS_MD_DTL_T_TC0 UNIQUE (OBJ_ID),
    CONSTRAINT TRVL_TRANS_MD_DTL_T_TP1 PRIMARY KEY(FDOC_NBR)
  )
/

CREATE TABLE IF NOT EXISTS TRVL_TRIP_TYP_T
    (
       CODE              		VARCHAR(3) 		NOT NULL,
       NM                		VARCHAR(40) 	NOT NULL,
       GEN_ENC_IND       		VARCHAR(1) 		NOT NULL,
       ENC_BAL_TYP       		VARCHAR(2) 		NULL,
       ENC_OBJ_CD        		VARCHAR(4) 		NULL,
       CONT_INFO_REQ_IND 		VARCHAR(1) 		NOT NULL,
       BLANKET_IND       		VARCHAR(1) 		NOT NULL,
       AUTO_TR_LIMIT			DECIMAL(19,2)  	NOT NULL,
       USE_PER_DIEM 			VARCHAR(1) 		NOT NULL,
       TA_REQUIRED 			VARCHAR(1) 		NOT NULL,
       PER_DIEM_CALC_METHOD 	VARCHAR(1) 		NOT NULL,
       ACTV_IND          		VARCHAR(1) 		NOT NULL,
       VER_NBR           		DECIMAL(8, 0) 	DEFAULT 1 NOT NULL,
       OBJ_ID            		VARCHAR(36) 	NOT NULL,
       CONSTRAINT TRVL_TRIP_TYP_T_TC0 UNIQUE (OBJ_ID),
       CONSTRAINT TRVL_TRIP_TYP_T_TP1 PRIMARY KEY(CODE)
    )
/

CREATE  TABLE IF NOT EXISTS TRVL_EM_CONT_T
  (
    ID INT NOT NULL AUTO_INCREMENT ,
    TRAVELER_DTL_ID INT NULL ,
    FDOC_NBR VARCHAR(45) NULL ,
    FDOC_LINE_NBR INT NULL ,
    CONT_REL_TYP_CD VARCHAR(45) NULL ,
    CONT_NM VARCHAR(45) NULL ,
    PHONE_NBR VARCHAR(45) NULL ,
    EMAIL_ADDR VARCHAR(45) NULL ,
    PRIMARY_IND VARCHAR(45) NULL ,
    OBJ_ID VARCHAR(45) NULL ,
    VER_NBR BIGINT NULL ,
    CONSTRAINT TRVL_EM_CONT_T_TC0 UNIQUE (OBJ_ID),
    CONSTRAINT TRVL_EM_CONT_T_TP1 PRIMARY KEY(ID)
  )
/

CREATE  TABLE IF NOT EXISTS  TRVL_TRAVELER_TYP_T
  (
    CODE VARCHAR(45) NOT NULL ,
    SRC_CODE VARCHAR(45) NULL ,
    NM VARCHAR(45) NULL ,
    ADVANCES_IND VARCHAR(45) NULL ,
    ACTV_IND VARCHAR(45) NULL ,
    OBJ_ID VARCHAR(45) NULL ,
    VER_NBR BIGINT NULL ,
    CONSTRAINT TRVL_TRAVELER_TYP_T_TC0 UNIQUE (OBJ_ID),
    CONSTRAINT TRVL_TRAVELER_TYP_T_TP1 PRIMARY KEY(CODE)
  )
/

CREATE  TABLE IF NOT EXISTS TRVL_TRAVELER_DTL_T
  (
    ID INT NOT NULL AUTO_INCREMENT ,
    DOC_NBR VARCHAR(45) NULL ,
    EMP_PRINCIPAL_ID VARCHAR(45) NULL ,
    CUST_NBR VARCHAR(45) NULL ,
    FIRST_NM VARCHAR(45) NULL ,
    LAST_NM VARCHAR(45) NULL ,
    ADDR_LINE_1 VARCHAR(45) NULL ,
    ADDR_LINE_2 VARCHAR(45) NULL ,
    CITY_NM VARCHAR(45) NULL ,
    POSTAL_STATE_CD VARCHAR(45) NULL ,
    POSTAL_CD VARCHAR(45) NULL ,
    COUNTRY_CD VARCHAR(45) NULL ,
    EMAIL_ADDR VARCHAR(45) NULL ,
    PHONE_NBR VARCHAR(45) NULL ,
    LIABILITY_INSURANCE VARCHAR(45) NULL ,
    TRAVELER_TYP_CD VARCHAR(45) NULL ,
    OBJ_ID VARCHAR(45) NULL ,
    CITIZENSHIP VARCHAR(45) NULL ,
    ACTV_IN VARCHAR(45) NULL ,
    VER_NBR BIGINT NULL ,
    CONSTRAINT TRVL_TRAVELER_DTL_T_TC0 UNIQUE (OBJ_ID),
    CONSTRAINT TRVL_TRAVELER_DTL_T_TP1 PRIMARY KEY(ID)
  )
/

CREATE TABLE IF NOT EXISTS TRVL_AUTH_DOC_T
  (
     FDOC_NBR                VARCHAR(14) NOT NULL,
     TRVL_ID                 VARCHAR(19) NULL,
     TRAVELER_DTL_ID         BIGINT(19) NULL,
     TEM_PROFILE_ID          BIGINT(19) NULL,
     TRIP_TYP_CD             VARCHAR(3) NULL,
     TRIP_BGN_DT             DATE NULL,
     TRIP_END_DT             DATE NULL,
     PRIMARY_DEST_ID         BIGINT(19) NULL,
     PRIMARY_DEST_NAME       VARCHAR(100) NULL,
     PRIMARY_DEST_CNTRY_ST   VARCHAR(100) NULL,
     PRIMARY_DEST_CNTY       VARCHAR(100) NULL,
     EXP_LMT                 DECIMAL(19, 2) DEFAULT 0 NULL,
     SPEC_CIRC_TA_WHY        VARCHAR(1) DEFAULT '0',
     SPEC_CIRC_TA            VARCHAR(255) NULL,
     SPEC_CIRC_TA_DOC_WHY    VARCHAR(1) DEFAULT '0',
     SPEC_CIRC_TA_DOC        VARCHAR(255) NULL,
     MEAL_WITHOUT_LODGING    VARCHAR(255) NULL,
     TRIP_DESC               VARCHAR(255) NULL,
     DELINQUENT_TR_EXCEPTION VARCHAR(1) NULL,
     PER_DIEM_ADJ            DECIMAL(19, 2) DEFAULT 0 NULL,
     AR_CUST_ID              VARCHAR(255) NULL,
     AR_INV_DOC_NBR          VARCHAR(255) NULL,
     CELL_PH_NUM             VARCHAR(20) NULL,
     RGN_FAMIL               VARCHAR(255) NULL,
     CTZN_CNTRY_CD           VARCHAR(255) NULL,
     TRPT_MODE_CD            VARCHAR(20) NULL,
     FDOC_NXT_EXP_NBR        DECIMAL(7, 0) NULL,
     VER_NBR                 DECIMAL(8, 0) DEFAULT 1 NOT NULL,
     OBJ_ID                  VARCHAR(36) NOT NULL,
     CONSTRAINT TRVL_AUTH_DOC_T_TC0 UNIQUE (OBJ_ID),
     CONSTRAINT TRVL_AUTH_DOC_T_TP1 PRIMARY KEY(FDOC_NBR)
  )
/

CREATE TABLE IF NOT EXISTS TRVL_PER_DIEM_ID_SEQ
(
	id bigint(19) not null auto_increment, primary key (id)
) ENGINE MyISAM
/

ALTER TABLE TRVL_PER_DIEM_ID_SEQ auto_increment = 1000
/

CREATE TABLE IF NOT EXISTS TRVL_ID_SEQ
(
	id bigint(19) not null auto_increment, primary key (id)
) ENGINE MyISAM
/




  




