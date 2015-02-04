--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/1.5.1/db.changelog-20141114229.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/1.5.1/db.changelog-20141114229.xml::OLE_DLVR_PTRN_BILL_FEE_TYP_T::ole
ALTER TABLE ole.OLE_DLVR_PTRN_BILL_FEE_TYP_T ADD DUE_DT_TIME datetime NULL
/

ALTER TABLE ole.OLE_DLVR_PTRN_BILL_FEE_TYP_T ADD CHECK_OUT_DT_TIME datetime NULL
/

ALTER TABLE ole.OLE_DLVR_PTRN_BILL_FEE_TYP_T ADD CHECK_IN_DT_TIME datetime NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_PTRN_BILL_FEE_TYP_T', 'ole', 'org/kuali/ole/1.5.1/db.changelog-20141114229.xml', NOW(), 1, '7:5494e310342ff485eea5f98be5e811a3', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.1/db.changelog-20141114229.xml::OLE_DLVR_TEMP_CIRC_RECORD::ole
ALTER TABLE ole.OLE_DLVR_TEMP_CIRC_RECORD ADD DUE_DT_TIME datetime NULL
/

ALTER TABLE ole.OLE_DLVR_TEMP_CIRC_RECORD ADD CHECK_OUT_DT_TIME datetime NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_TEMP_CIRC_RECORD', 'ole', 'org/kuali/ole/1.5.1/db.changelog-20141114229.xml', NOW(), 2, '7:2e0207d39b8f652c1c9a54f67f0d5ddb', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.1/db.changelog-20141114229.xml::OLE_AP_INV_T::ole
ALTER TABLE ole.OLE_AP_INV_T ADD VNDR_FRGN_INV_AMT DECIMAL NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_AP_INV_T', 'ole', 'org/kuali/ole/1.5.1/db.changelog-20141114229.xml', NOW(), 3, '7:3dcdba72cc365702b73a449a2e67931e', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
