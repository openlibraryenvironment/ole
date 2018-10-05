--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/3.0.15/db.changelog-20181005.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/3.0.15/db.changelog-20181005.xml::ALTER_OLE_DS_HOLDINGS_URI_T_URI_MYSQL::ole
ALTER TABLE OLE_DS_HOLDINGS_URI_T MODIFY URI VARCHAR(2400)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('ALTER_OLE_DS_HOLDINGS_URI_T_URI_MYSQL', 'ole', 'org/kuali/ole/3.0.15/db.changelog-20181005.xml', NOW(), 1, '7:c435f14d3061232d056ceb834492b356', 'sql', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0.15/db.changelog-20181005.xml::ALTER_OLE_DS_HOLDINGS_URI_T_TEXT_MYSQL::ole
ALTER TABLE OLE_DS_HOLDINGS_URI_T MODIFY TEXT VARCHAR(2400)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('ALTER_OLE_DS_HOLDINGS_URI_T_TEXT_MYSQL', 'ole', 'org/kuali/ole/3.0.15/db.changelog-20181005.xml', NOW(), 2, '7:54eacad949365876140c4d1ab784692d', 'sql', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
