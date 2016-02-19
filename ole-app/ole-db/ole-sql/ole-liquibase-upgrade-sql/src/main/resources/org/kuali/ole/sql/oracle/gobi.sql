-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: org/kuali/ole/gobi/db.changelog-gobi.xml
-- *********************************************************************

-- Lock Database
-- Changeset org/kuali/ole/gobi/db.changelog-gobi.xml::GOBI_ADDR_MAPR_T::ole
CREATE TABLE ole.GOBI_ADDR_MAPR_T (ID NUMBER(10), SUB_ACCOUNT VARCHAR2(100), BUILDING_CODE VARCHAR2(100), ROOM_NUMBER VARCHAR2(100), VER_NBR DECIMAL(8), OBJ_ID VARCHAR2(36))
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('GOBI_ADDR_MAPR_T', 'ole', 'org/kuali/ole/gobi/db.changelog-gobi.xml', SYSTIMESTAMP, 1, '7:c6f95872deca125c9cdccd10b8299e4a', 'createTable', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/gobi/db.changelog-gobi.xml::GOBI_ADDR_MAPR_T_PK::ole
ALTER TABLE ole.GOBI_ADDR_MAPR_T ADD CONSTRAINT GOBI_ADDR_MAPR_T_PK PRIMARY KEY (ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('GOBI_ADDR_MAPR_T_PK', 'ole', 'org/kuali/ole/gobi/db.changelog-gobi.xml', SYSTIMESTAMP, 2, '7:96abdcc7fec57df11c52ed11f4dccad8', 'addPrimaryKey', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/gobi/db.changelog-gobi.xml::GOBI_ADDR_MAPR_S::ole
CREATE SEQUENCE ole.GOBI_ADDR_MAPR_S START WITH 1 INCREMENT BY 1 ORDER
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('GOBI_ADDR_MAPR_S', 'ole', 'org/kuali/ole/gobi/db.changelog-gobi.xml', SYSTIMESTAMP, 3, '7:abadefe8c1a8941343b1efda94796e9b', 'createSequence', '', 'EXECUTED', '3.2.0')
/

-- Release Database Lock
-- Release Database Lock
