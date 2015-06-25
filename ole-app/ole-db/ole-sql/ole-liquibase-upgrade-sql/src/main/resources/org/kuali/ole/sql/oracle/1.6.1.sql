-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: org/kuali/ole/1.6.1/db.changelog-20150505.xml
-- *********************************************************************

-- Lock Database
-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_LOAD_KRCR_NMSPC_T_oracle::ole
INSERT INTO KRCR_NMSPC_T (NMSPC_CD, NM, ACTV_IND, APPL_ID, OBJ_ID) VALUES ('OLE-AR', 'AR', 'Y', 'OLE', 'OLE-AR')
/

INSERT INTO KRCR_NMSPC_T (NMSPC_CD, NM, ACTV_IND, APPL_ID, OBJ_ID) VALUES ('OLE-LD', 'LD', 'Y', 'OLE', 'OLE-LD')
/

INSERT INTO KRCR_NMSPC_T (NMSPC_CD, NM, ACTV_IND, APPL_ID, OBJ_ID) VALUES ('OLE-BC', 'BC', 'Y', 'OLE', 'OLE-BC')
/

INSERT INTO KRCR_NMSPC_T (NMSPC_CD, NM, ACTV_IND, APPL_ID, OBJ_ID) VALUES ('OLE-CAB', 'CAB', 'Y', 'OLE', 'OLE-CAB')
/

INSERT INTO KRCR_NMSPC_T (NMSPC_CD, NM, ACTV_IND, APPL_ID, OBJ_ID) VALUES ('OLE-CAM', 'CAM', 'Y', 'OLE', 'OLE-CAM')
/

INSERT INTO KRCR_NMSPC_T (NMSPC_CD, NM, ACTV_IND, APPL_ID, OBJ_ID) VALUES ('OLE-EC', 'EC', 'Y', 'OLE', 'OLE-EC')
/

INSERT INTO KRCR_NMSPC_T (NMSPC_CD, NM, ACTV_IND, APPL_ID, OBJ_ID) VALUES ('OLE-ENDOW', 'ENDOW', 'Y', 'OLE', 'OLE-ENDOW')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRCR_NMSPC_T_oracle', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 1, '7:4bda620bd35ccd1f1ff2bf99d852855b', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_LOAD_KRCR_CMPNT_T_oracle::ole
INSERT INTO KRCR_CMPNT_T (NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES ('OLE-AR', 'All', 'OLE61', '1', 'All', 'Y')
/

INSERT INTO KRCR_CMPNT_T (NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES ('OLE-LD', 'All', 'OLE62', '1', 'All', 'Y')
/

INSERT INTO KRCR_CMPNT_T (NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES ('OLE-BC', 'All', 'OLE63', '1', 'All', 'Y')
/

INSERT INTO KRCR_CMPNT_T (NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES ('OLE-CAB', 'All', 'OLE64', '1', 'All', 'Y')
/

INSERT INTO KRCR_CMPNT_T (NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES ('OLE-CAM', 'All', 'OLE65', '1', 'All', 'Y')
/

INSERT INTO KRCR_CMPNT_T (NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES ('OLE-EC', 'All', 'OLE66', '1', 'All', 'Y')
/

INSERT INTO KRCR_CMPNT_T (NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES ('OLE-ENDOW', 'All', 'OLE67', '1', 'All', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRCR_CMPNT_T_oracle', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 2, '7:28b7f8ba98cf0713bbb72e6f3229d39a', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_LOAD_KRCR_PARM_T_oracle::ole
INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-AR', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-16', '1', 'CONFG', 'N', 'Y/N indicator showing if the AR module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-LD', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-17', '1', 'CONFG', 'N', 'Y/N indicator showing if the LD module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-BC', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-18', '1', 'CONFG', 'N', 'Y/N indicator showing if the BC module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-CAB', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-19', '1', 'CONFG', 'N', 'Y/N indicator showing if the CAB module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-CAM', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-20', '1', 'CONFG', 'N', 'Y/N indicator showing if the CAM module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-PURAP', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-21', '1', 'CONFG', 'N', 'Y/N indicator showing if the PURAP module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-FP', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-22', '1', 'CONFG', 'N', 'Y/N indicator showing if the FP module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-EC', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-23', '1', 'CONFG', 'N', 'Y/N indicator showing if the EC module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-PDP', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-24', '1', 'CONFG', 'N', 'Y/N indicator showing if the PDP module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-CG', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-25', '1', 'CONFG', 'N', 'Y/N indicator showing if the CG module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-ENDOW', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-26', '1', 'CONFG', 'N', 'Y/N indicator showing if the ENDOW module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-GL', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-27', '1', 'CONFG', 'N', 'Y/N indicator showing if the GL module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-SELECT', 'All', 'OLTP_LOCKOUT_ACTIVE_IND', 'OLECNTRB65-28', '1', 'CONFG', 'N', 'Y/N indicator showing if the SELECT module is currently unavailable for Online Transaction Processing (OLTP). Y means the module is currently locked and cannot be used. N means the module is unlocked and available for use.', 'A', 'OLE')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRCR_PARM_T_oracle', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 3, '7:56a1d2b70ad21502b3c3b049d0dfc27d', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_LOAD_KRIM_ROLE_MBR_T_oracle::ole
INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD) VALUES ('OLE1612', 'OLE1612', 'OLE55', 'olequickstart', 'P')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRIM_ROLE_MBR_T_oracle', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 4, '7:5d352f4446963117d1a91e453d1d820c', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_KRIM_ROLE_PERM_T::ole
UPDATE KRIM_ROLE_PERM_T SET ROLE_ID = 'OLE55' WHERE ROLE_PERM_ID = 'OLEMI9598-RLPRM3'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_ROLE_PERM_T', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 5, '7:efe411ab6d4ada36ce6006050cfbedd5', 'update', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_DLVR_CIRC_RECORDS::ole
ALTER TABLE ole.OLE_DLVR_CIRC_RECORD ADD ITM_LOCN VARCHAR2(100)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_CIRC_RECORDS', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 6, '7:6a8c65f4bd93a634389c8d98f08cf84e', 'addColumn', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_DLVR_CIRC_RECORDT::ole
ALTER TABLE ole.OLE_DLVR_CIRC_RECORD ADD HLDNG_LOCN VARCHAR2(100)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_CIRC_RECORDT', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 7, '7:57556d35fa2e017457b08d6e969285c4', 'addColumn', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_DLVR_RQST_HSTRY_REC_T_CRTE_DT_TIME::ole
ALTER TABLE ole.OLE_DLVR_RQST_HSTRY_REC_T ADD CRTE_DT_TIME date
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_RQST_HSTRY_REC_T_CRTE_DT_TIME', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 8, '7:15649203da8dced86be68718c629c7c6', 'addColumn', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_DLVR_RQST_HSTRY_REC_T_CRTE_DT_TIME_NOT_NULL_CONSTRAINT::ole
ALTER TABLE ole.OLE_DLVR_RQST_HSTRY_REC_T MODIFY CRTE_DT_TIME NOT NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_RQST_HSTRY_REC_T_CRTE_DT_TIME_NOT_NULL_CONSTRAINT', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 9, '7:f722c13041876f8eeae357d94f6d42d0', 'addNotNullConstraint', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_RNWL_HISTORY_T::ole
CREATE TABLE ole.OLE_RNWL_HISTORY_T (RNWL_HSTRY_ID VARCHAR2(40), LOAN_ID VARCHAR2(40), PTRN_BARCODE VARCHAR2(100), ITEM_BARCODE VARCHAR2(100), ITEM_UUID VARCHAR2(100), OPRTR_ID VARCHAR2(100), RNWD_DT TIMESTAMP, RNWL_DUE_DT TIMESTAMP, VER_NBR DECIMAL(8), OBJ_ID VARCHAR2(36))
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_RNWL_HISTORY_T', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 10, '7:ae7241eea7fa9cab8b81c5843e26397a', 'createTable', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_RNWL_HISTORY_T_PK::ole
ALTER TABLE ole.OLE_RNWL_HISTORY_T ADD CONSTRAINT OLE_RNWL_HISTORY_PK PRIMARY KEY (RNWL_HSTRY_ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_RNWL_HISTORY_T_PK', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 11, '7:a51dc0cd39846d8ac6e1fb34eda89271', 'addPrimaryKey', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_RNWL_HISTORY_S::ole
CREATE SEQUENCE ole.OLE_RNWL_HISTORY_S START WITH 1 INCREMENT BY 1 ORDER
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_RNWL_HISTORY_S', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 12, '7:a26be6fcc0444ec5d99cfe5fcc31dcda', 'createSequence', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_PUR_POBA_T::ole
CREATE TABLE ole.OLE_PUR_POBA_T (FDOC_NBR VARCHAR2(10), OBJ_ID VARCHAR2(36), VER_NBR DECIMAL(8), LOAN_ID VARCHAR2(40), OLE_POBA_ID DECIMAL(10), UPLD_FILE_NM VARCHAR2(60), UPLD_TYPE VARCHAR2(40), POBA_START_DT date, POBA_START_TM VARCHAR2(40), POBA_PRNCPL_NM VARCHAR2(50))
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_PUR_POBA_T', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 13, '7:a9b8b8cd4d8eaca70e5ea63c125a764e', 'createTable', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.6.1/db.changelog-20150505.xml::OLE_POBA_ID::ole
CREATE SEQUENCE ole.OLE_POBA_ID START WITH 1000 INCREMENT BY 1 ORDER
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_POBA_ID', 'ole', 'org/kuali/ole/1.6.1/db.changelog-20150505.xml', SYSTIMESTAMP, 14, '7:95eef2c3e2ec8efcd5c9ec8539ddff9b', 'createSequence', '', 'EXECUTED', '3.2.0')
/

-- Release Database Lock
-- Release Database Lock
