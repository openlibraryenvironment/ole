--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/3.0.17/db.changelog-20190808.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/3.0.17/db.changelog-20190808.xml::ALTER_OLE_ASR_RQST_T_MYSQL::ole
ALTER TABLE OLE_ASR_RQST_T ADD COLUMN RQST_NOTE varchar(4000)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('ALTER_OLE_ASR_RQST_T_MYSQL', 'ole', 'org/kuali/ole/3.0.17/db.changelog-20190808.xml', NOW(), 1, '7:fe18baba1cbe9d76b5f312aea2863e10', 'sql', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
