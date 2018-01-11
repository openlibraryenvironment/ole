--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/3.0.11/db.changelog-20171227.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/3.0.11/db.changelog-20171227.xml::OLE_DS_ITM_T_MYSQL::ole
ALTER TABLE OLE_DLVR_RQST_HSTRY_REC_T CHANGE `OLE_ITEM_ID` `OLE_ITEM_BARCODE` VARCHAR(35)
/

ALTER TABLE OLE_DLVR_RQST_HSTRY_REC_T ADD COLUMN `OLE_ITEM_ID` VARCHAR(35)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DS_ITM_T_MYSQL', 'ole', 'org/kuali/ole/3.0.11/db.changelog-20171227.xml', NOW(), 1, '7:b341d40451d06164446f928ba0678acb', 'sql (x2)', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
