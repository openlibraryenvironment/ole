--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/3.0.16/db.changelog-20190305.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/3.0.16/db.changelog-20190305.xml::OLE_KRIM_PERM_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1013', 'OLELOAN1013', '1', '56', 'OLE-DLVR', 'Override Renew', 'Allows users to override and Renew an Item.', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_PERM_T_mysql', 'ole', 'org/kuali/ole/3.0.16/db.changelog-20190305.xml', NOW(), 1, '7:debc1265d1f9c60a28ad8938a56c8d6c', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
