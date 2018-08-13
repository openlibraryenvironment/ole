--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/3.0.12/db.changelog-20180419.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/3.0.12/db.changelog-20180419.xml::OLE_LOAD_KRCR_PARM_T_MYSQL::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-PURAP', 'All', 'ZERO_ENCUMBRANCE_CHECK', 'OLE101010', '1', 'CONFG', 'N', 'Indicator for checking Zero Encumbrance in AutoClosePO Job.', 'A', 'KUALI')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRCR_PARM_T_MYSQL', 'ole', 'org/kuali/ole/3.0.12/db.changelog-20180419.xml', NOW(), 1, '7:c04abe7c282c3ea10ee8cd4543f539cf', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
