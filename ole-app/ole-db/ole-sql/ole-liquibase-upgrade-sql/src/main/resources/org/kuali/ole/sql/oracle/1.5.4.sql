-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: org/kuali/ole/1.5.4/db.changelog-20140909.xml
-- *********************************************************************

-- Lock Database
-- Changeset org/kuali/ole/1.5.4/db.changelog-20140909.xml::OLE_LOAD_KRCR_PARM_T_oracle::ole
INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'RENEW_INFORMATION_INDICATOR_FOR_VUFIND_SERVICES', 'OLE6518', '1', 'CONFG', 'N', 'This is for deciding whether the renewable value is needed in the getCheckedOutItems and lookupUser service response or not ', 'A', 'OLE')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRCR_PARM_T_oracle', 'ole', 'org/kuali/ole/1.5.4/db.changelog-20140909.xml', SYSTIMESTAMP, 1, '7:82385aaade681ee5f584e4211ac389c1', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.5.4/db.changelog-20140909.xml::OLE_DLVR_LOAN_NOTICE_DATE_INDX::ole
CREATE INDEX ole.NTC_TO_SND_DT_INDX ON ole.OLE_DLVR_LOAN_NOTICE_T(NTC_TO_SND_DT)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_LOAN_NOTICE_DATE_INDX', 'ole', 'org/kuali/ole/1.5.4/db.changelog-20140909.xml', SYSTIMESTAMP, 2, '7:3157f66aa521796a9a2669feb9bafbc6', 'createIndex', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.5.4/db.changelog-20140909.xml::OLE_DLVR_LOAN_NOTICE_TYP_INDX::ole
CREATE INDEX ole.NTC_TYP_INDX ON ole.OLE_DLVR_LOAN_NOTICE_T(NTC_TYP)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_LOAN_NOTICE_TYP_INDX', 'ole', 'org/kuali/ole/1.5.4/db.changelog-20140909.xml', SYSTIMESTAMP, 3, '7:9b0fbb5ccd0deb988c2930a8e8ad4294', 'createIndex', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.5.4/db.changelog-20140909.xml::OLE_DLVR_LOAN_NOTICE_PTRN_INDX::ole
CREATE INDEX ole.PTRN_ID_INDX ON ole.OLE_DLVR_LOAN_NOTICE_T(PTRN_ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_LOAN_NOTICE_PTRN_INDX', 'ole', 'org/kuali/ole/1.5.4/db.changelog-20140909.xml', SYSTIMESTAMP, 4, '7:b8d9796a8027b4ab3877f9ccf86efc32', 'createIndex', '', 'EXECUTED', '3.2.0')
/

-- Release Database Lock
-- Release Database Lock
