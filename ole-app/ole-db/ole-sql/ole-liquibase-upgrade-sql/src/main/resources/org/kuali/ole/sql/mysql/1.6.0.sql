--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/1.6.0/db.changelog-20141207.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_KRCR_PARM_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-SELECT', 'Select', 'REENCUMBER_RECURRING_ORDERS', 'OLE6575', '1', 'CONFG', 'PO,+ 0 $', 'This parameter is having option for to calculate either reencumber recurring Purchase Orders amounts or Invoice amounts, By default it calculate for PO', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-SELECT', 'Select', 'SYSTEM_USER', 'OLE6576', '1', 'CONFG', 'ole-quickstart', 'Default System User', 'A', 'KUALI')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-SELECT', 'Select', 'ACCOUNT_DOCUMENT_INTIATOR', 'OLE6577', '1', 'CONFG', 'ole-quickstart', 'Default Account Document Intiator', 'A', 'KUALI')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-SELECT', 'Select', 'NCIP_EMAILADDRESS_TYPE', 'OLE6578', '1', 'CONFG', 'electronic mail address', 'Parameter is used to set the value for NCIP Email Address Type', 'A', 'KUALI')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'PAYMENT_MODE_FORGIVE', 'OLE6579', '1', 'CONFG', 'Forgive', 'The forgive value can be configured.(e.g) SOAS needs Waived instead of Forgive. So Universities can have the value whatever is appropriate to them', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'PAYMENT_MODE_FORGIVE_MESSAGE', 'OLE6580', '1', 'CONFG', 'has been forgiven', 'The forgive message can be configured.(e.g) SOAS needs has been waived instead of has been forgiven. So Universities can have the value whatever is appropriate to them', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'DEFAULT_REQUEST_LEVEL', 'OLE6581', '1', 'CONFG', 'Item', 'This for setting the default request level while placing a request .Allowed values were Item and Title', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'TITLE_LEVEL_REQUEST_INDICATOR', 'OLE6582', '1', 'CONFG', 'Y', 'This is to decide whether the title request can be placed on an item or not.Allowed values were Y and N', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'DEFAULT_CIRCULATION_DESK', 'OLE6583', '1', 'CONFG', 'BL_EDUC', 'This parameter value wil be used as the default circulation desk value if the item owning location is not mapped to any circulation desk', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'SIP2_INSTITUTION_NAME', 'OLE6584', '1', 'CONFG', 'OLE', 'This parameter hold the Institution name for 3M machine', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'SIP2_REQUEST_TYPE', 'OLE6585', '1', 'CONFG', 'Hold/Hold Request', 'This parameter hold the request type for 3M machine', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'SIP2_OPERATOR_ID', 'OLE6586', '1', 'CONFG', 'olequickstart', 'This parameter hold the operator id for 3M machine to perform checking/checkout operation', 'A', 'OLE')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRCR_PARM_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 1, '7:06b6f632931f9a87fe42c2ebf640b574', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_KRIM_ENTITY_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_ENTITY_T (entity_id, obj_id, actv_ind) VALUES ('OLE100009', 'OLE100009', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ENTITY_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 2, '7:f294261a5a85e4f8bb20be99ebc9dbce', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_KRIM_ENTITY_NM_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_ENTITY_NM_T (entity_nm_id, obj_id, entity_id, nm_typ_cd, first_nm, middle_nm, last_nm, suffix_nm, title_nm, dflt_ind, actv_ind) VALUES ('OLE100009', 'OLE100009', 'OLE100009', 'PRFR', 'ROLLOVER', '', 'ROLLOVER', '', '', 'Y', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ENTITY_NM_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 3, '7:4d68c65ca02ef263aa0eb5a6df8d2a5e', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_KRIM_ENTITY_ENT_TYP_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_ENTITY_ENT_TYP_T (ent_typ_cd, entity_id, obj_id, actv_ind) VALUES ('PERSON', 'OLE100009', 'OLE100009', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ENTITY_ENT_TYP_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 4, '7:c584f109de87c45ae9db7305393c9679', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_KRIM_ENTITY_EMAIL_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_ENTITY_EMAIL_T (entity_email_id, obj_id, entity_id, ent_typ_cd, email_typ_cd, email_addr, dflt_ind, actv_ind) VALUES ('OLE100009', 'OLE100009', 'OLE100009', 'PERSON', 'WRK', 'rollover@kuali.org', 'Y', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ENTITY_EMAIL_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 5, '7:413fe88ffa797d1c57fb99d5cc2c0384', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_OLE_KRIM_ROLE_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) VALUES ('OLE10087', 'OLE100676', '1', 'OLE-ROLL-OVER', 'OLE-SELECT', 'Fiscal Year Roll Over.', '1', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_OLE_KRIM_ROLE_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 6, '7:5068e4b854ba3bfef5ec9e24550f00a3', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_OLE_KRIM_PERM_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLE10325', 'OLE70106', '1', '66', 'OLE-SELECT', 'RollOver Ingest', 'Permission for Fiscal Year Roll Over Ingest', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_OLE_KRIM_PERM_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 7, '7:cfb1bce6167bc49651e0e7b1b1c107b3', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_OLE_KRIM_PRNCPL_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_PRNCPL_T (prncpl_id, obj_id, prncpl_nm, entity_id, actv_ind) VALUES ('rollover', 'OLE100009', 'ole-rollover', 'OLE100009', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_OLE_KRIM_PRNCPL_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 8, '7:cc00bd60db1c46c9d7131724e5beffa7', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_OLE_KRIM_ROLE_PERM_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, ROLE_ID, PERM_ID, ACTV_IND) VALUES ('OLE12355', 'OLE70559', 'OLE10087', 'OLE10325', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_OLE_KRIM_ROLE_PERM_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 9, '7:011223e4f2ce98f656f5a9ff929cce2c', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAD_OLE_KRIM_ROLE_MBR_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_ROLE_MBR_T (role_mbr_id, obj_id, role_id, mbr_id, mbr_typ_cd) VALUES ('OLE10273', 'OLE10245', 'OLE10087', 'rollover', 'P')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_OLE_KRIM_ROLE_MBR_T_mysql', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 10, '7:a68c228d04871f4849241ed8738aa831', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAN_INTRANSIT_HISTORY_T::ole
CREATE TABLE ole.OLE_LOAN_INTRANSIT_HISTORY_T (ID VARCHAR(40) NULL, ITEM_BARCODE VARCHAR(40) NULL, ITEM_UUID VARCHAR(40) NULL, ITEM_RETURNED_DT TIMESTAMP NULL, OPERATOR VARCHAR(36) NULL, CIR_DESK_LOC VARCHAR(100) NULL, CIR_DESK_ROUTE_TO VARCHAR(100) NULL, VER_NBR DECIMAL(8) NULL, OBJ_ID VARCHAR(36) NULL)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAN_INTRANSIT_HISTORY_T', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 11, '7:b878192d7300f3642a142a8c2ca0038e', 'createTable', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAN_INTRANSIT_HISTORY_T_PK::ole
ALTER TABLE ole.OLE_LOAN_INTRANSIT_HISTORY_T ADD PRIMARY KEY (ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAN_INTRANSIT_HISTORY_T_PK', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 12, '7:4439136436a92a0311595b06eea74413', 'addPrimaryKey', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_LOAN_INTRANSIT_HISTORY_S::ole
CREATE TABLE ole.OLE_LOAN_INTRANSIT_HISTORY_S (ID VARCHAR(40) NULL)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAN_INTRANSIT_HISTORY_S', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 13, '7:449bb987329206f7492a831ff363ca0d', 'createTable', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_DS_DOC_FIELD_T::ole
INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('Title','357','9','4','N','Y','N','N','Y','Title_search','357','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('Author','358','9','4','N','Y','N','N','Y','Author_search','358','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('Publisher','359','9','4','N','Y','N','N','Y','Publisher_search','359','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('ISSN','360','9','4','N','Y','N','N','Y','ISSN_search','360','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('ISBN','361','9','4','N','Y','N','N','Y','ISBN_search','361','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('Format','362','9','4','N','Y','N','N','Y','Format_search','362','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('Language','363','9','4','N','Y','N','N','Y','Language_search','363','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('Date of Publication','364','9','4','N','Y','N','N','Y','PublicationDate_search','364','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('OCLC No','365','1','1','N','N','N','N','Y','mdf_035a','365','1')
/

INSERT INTO OLE_DS_DOC_FIELD_T (DISPLAY_LABEL,DOC_FIELD_ID,DOC_FORMAT_ID,DOC_TYPE_ID,IS_DISPLAY,IS_EXPORT,IS_FACET,IS_GLOBAL_EDIT,IS_SEARCH,NAME,OBJ_ID,VER_NBR) VALUES ('Bib Local Identifier','366','9','4','N','Y','N','N','Y','bibIdentifier','366','1')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DS_DOC_FIELD_T', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 14, '7:c3364f55f4d3aeef9eecd687e87433a0', 'sql (x10)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_CNCL_RSN_S::ole
INSERT INTO OLE_CNCL_RSN_S values ()
/

set @last_id = last_insert_id()
/

INSERT INTO OLE_CNCL_RSN_T (CNCL_RSN_ID, CNCL_RSN_NM, CNCL_RSN_TXT, ROW_ACT_IND, OBJ_ID, VER_NBR) VALUES (@last_id,'Library Cancelled','Library Cancelled','Y','3ee62d0c-125e-46a9-acd9-ace57d63dc37','1')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_CNCL_RSN_S', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 15, '7:ea57e2c2fa022f63bbed7d50d13a6b7c', 'sql (x3)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_DLVR_BATCH_JOB_T::ole
ALTER TABLE ole.OLE_DLVR_BATCH_JOB_T ADD PCK_UP_LOCN VARCHAR(100) NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_BATCH_JOB_T', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 16, '7:d932bb31c5a3275c019554bf32823c45', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_BAT_PRCS_PRF_T::ole
UPDATE OLE_BAT_PRCS_PRF_T SET BAT_PRCS_MATCH_PROFILE="matchBibs=false,noMatchBibs_addBibs=true,bibNotMatched_discardBib=false,bibNotMatched_addBib=true,bibMatched_addBib=false,bibMatched_discardBib=false,bibMatched_updateBib=true,matchHoldings=true,noMatchHoldings_discardHoldingsItems=true,noMatchHoldings_deleteAddHoldingsItems=false,noMatchHoldings_retainAddHoldingsItems=false,holdingsNotMatched_discardHoldings=false,holdingsNotMatched_addHoldings=true,holdingsNotMatched_addItems=false,holdingsMatched_addHoldings=false,holdingsMatched_addItems=false,holdingsMatched_discardHoldings=false,holdingsMatched_updateHoldings=true,matchItems=true,noMatchItem_discardItems=true,noMatchItem_deleteAddItems=false,noMatchItem_retainAddItems=false,itemNotMatched_discardItem=false,itemNotMatched_addItem=true,itemMatched_addItem=false,itemMatched_updateItem=true" WHERE BAT_PRCS_PRF_ID="3"
/

UPDATE OLE_BAT_PRCS_PRF_T SET BAT_PRCS_MATCH_PROFILE="matchBibs=false,noMatchBibs_addBibs=true,bibNotMatched_discardBib=false,bibNotMatched_addBib=true,bibMatched_addBib=false,bibMatched_discardBib=false,bibMatched_updateBib=true,matchHoldings=true,noMatchHoldings_discardHoldingsItems=true,noMatchHoldings_deleteAddHoldingsItems=false,noMatchHoldings_retainAddHoldingsItems=false,holdingsNotMatched_discardHoldings=false,holdingsNotMatched_addHoldings=true,holdingsNotMatched_addItems=false,holdingsMatched_addHoldings=false,holdingsMatched_addItems=false,holdingsMatched_discardHoldings=false,holdingsMatched_updateHoldings=true,matchItems=true,noMatchItem_discardItems=true,noMatchItem_deleteAddItems=false,noMatchItem_retainAddItems=false,itemNotMatched_discardItem=false,itemNotMatched_addItem=true,itemMatched_addItem=false,itemMatched_updateItem=true" WHERE BAT_PRCS_PRF_ID="21"
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_BAT_PRCS_PRF_T', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 17, '7:eed4405b70ab69090c0013fd1b80f3a7', 'sql (x2)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_DONOR_T::ole
ALTER TABLE OLE_DONOR_T MODIFY DONOR_PUBLIC_DISPLAY varchar(4000)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DONOR_T', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 18, '7:0950c6f9a4ef34ff4f956d96bc835942', 'sql', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::KRCR_PARM_T_updateParmName::ole
UPDATE KRCR_PARM_T SET PARM_NM = 'REQUEST_QUEUE' WHERE NMSPC_CD ='OLE-DLVR' and CMPNT_CD='Deliver' and PARM_NM='REQUEST QUEUE' and APPL_ID='KUALI'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRCR_PARM_T_updateParmName', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 19, '7:e75dff06befb2e024b1fc1eead054ca3', 'update', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::KRCR_PARM_T_deleteParm::ole
DELETE FROM KRCR_PARM_T  WHERE NMSPC_CD ='OLE-DLVR' and CMPNT_CD='Deliver' and PARM_NM='EXPIRED_BODY' and APPL_ID='KUALI'
/

DELETE FROM KRCR_PARM_T  WHERE NMSPC_CD ='OLE-DLVR' and CMPNT_CD='Deliver' and PARM_NM='COURTESY_BODY' and APPL_ID='KUALI'
/

DELETE FROM KRCR_PARM_T  WHERE NMSPC_CD ='OLE-DLVR' and CMPNT_CD='Deliver' and PARM_NM='OVERDUE_BODY' and APPL_ID='KUALI'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRCR_PARM_T_deleteParm', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 20, '7:2f16e8ebc2b59ab6edb45d5d4d39d16d', 'delete (x3)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_CRCL_DSK_T_replyToEmail::ole
ALTER TABLE ole.OLE_CRCL_DSK_T ADD REPLY_TO_EMAIL VARCHAR(100) NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_CRCL_DSK_T_replyToEmail', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 21, '7:f28712e3e4562b97dffdf32f2350adb6', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_DLVR_RQST_T::ole
ALTER TABLE ole.OLE_DLVR_RQST_T ADD RQST_LVL VARCHAR(40) NULL
/

ALTER TABLE ole.OLE_DLVR_RQST_T ADD BIB_ID VARCHAR(40) NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_RQST_T', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 22, '7:b66ee6e9d6c28faa98c3f76866bf6729', 'addColumn (x2)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_CRCL_PICKUP_DSK_LOCN::ole
ALTER TABLE ole.OLE_CRCL_DSK_LOCN_T ADD OLE_CRCL_PICKUP_DSK_LOCN VARCHAR(40) NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_CRCL_PICKUP_DSK_LOCN', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 23, '7:9a86ff36f768521300e16babc3196acb', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_DLVR_RQST_HSTRY_REC_T_ARCH_DT_TIME::ole
ALTER TABLE ole.OLE_DLVR_RQST_HSTRY_REC_T MODIFY ARCH_DT_TIME date
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_RQST_HSTRY_REC_T_ARCH_DT_TIME', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 24, '7:992d1bd4848aff4a05ab2e67dd15acf1', 'modifyDataType', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::ole_dlvr_loan_t_dropIndex::ole
DROP INDEX loan_itm_index ON ole.ole_dlvr_loan_t
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('ole_dlvr_loan_t_dropIndex', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 25, '7:3f21021d193a26da8c15767110521929', 'dropIndex', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::ole_dlvr_loan_t::ole
ALTER TABLE ole.ole_dlvr_loan_t ADD CONSTRAINT itm_id_const UNIQUE (itm_id)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('ole_dlvr_loan_t', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 26, '7:87bbf9c63e36f4de6238f2e70f9a56be', 'addUniqueConstraint', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_DLVR_ADD_T::ole
ALTER TABLE ole.OLE_DLVR_ADD_T ADD PTRN_DLVR_ADD VARCHAR(1) NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_ADD_T', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 27, '7:5d253b0650f8b4fbd898b6e909370505', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.6.0/db.changelog-20141207.xml::OLE_CRCL_DSK_T::ole
ALTER TABLE ole.OLE_CRCL_DSK_T ADD RQST_EXPIRTIN_DAYS DECIMAL(8) NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_CRCL_DSK_T', 'ole', 'org/kuali/ole/1.6.0/db.changelog-20141207.xml', NOW(), 28, '7:a421ca771b016ca9dbbd19d2b326adbf', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
