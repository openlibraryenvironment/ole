-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: org/kuali/ole/2.1/db.changelog-20151123.xml
-- *********************************************************************

-- Lock Database
-- Changeset org/kuali/ole/2.1/db.changelog-20151123.xml::OLE_KRCR_PARM_T::ole
UPDATE KRCR_PARM_T SET VAL = 'PO + 0 $' WHERE PARM_NM = 'REENCUMBER_RECURRING_ORDERS'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRCR_PARM_T', 'ole', 'org/kuali/ole/2.1/db.changelog-20151123.xml', SYSTIMESTAMP, 1, '7:4b8bac07b079429600f713e4b495a922', 'update', '', 'EXECUTED', '3.2.0')
/

-- Release Database Lock
-- Release Database Lock
