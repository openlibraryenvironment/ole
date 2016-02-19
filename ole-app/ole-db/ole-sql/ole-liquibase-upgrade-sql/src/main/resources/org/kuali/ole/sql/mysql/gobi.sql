--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/gobi/db.changelog-gobi.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/gobi/db.changelog-gobi.xml::GOBI_ADDR_MAPR_T::ole
CREATE TABLE ole.GOBI_ADDR_MAPR_T (ID INT NULL, SUB_ACCOUNT VARCHAR(100) NULL, BUILDING_CODE VARCHAR(100) NULL, ROOM_NUMBER VARCHAR(100) NULL, VER_NBR DECIMAL(8) NULL, OBJ_ID VARCHAR(36) NULL)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('GOBI_ADDR_MAPR_T', 'ole', 'org/kuali/ole/gobi/db.changelog-gobi.xml', NOW(), 1, '7:c6f95872deca125c9cdccd10b8299e4a', 'createTable', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/gobi/db.changelog-gobi.xml::GOBI_ADDR_MAPR_T_PK::ole
ALTER TABLE ole.GOBI_ADDR_MAPR_T ADD PRIMARY KEY (ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('GOBI_ADDR_MAPR_T_PK', 'ole', 'org/kuali/ole/gobi/db.changelog-gobi.xml', NOW(), 2, '7:96abdcc7fec57df11c52ed11f4dccad8', 'addPrimaryKey', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/gobi/db.changelog-gobi.xml::GOBI_ADDR_MAPR_S::ole
CREATE TABLE ole.GOBI_ADDR_MAPR_S (ID BIGINT(19) NULL)
/

ALTER TABLE ole.GOBI_ADDR_MAPR_S ADD PRIMARY KEY (ID)
/

ALTER TABLE ole.GOBI_ADDR_MAPR_S MODIFY ID BIGINT(19) AUTO_INCREMENT
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('GOBI_ADDR_MAPR_S', 'ole', 'org/kuali/ole/gobi/db.changelog-gobi.xml', NOW(), 3, '7:e92622d8013e3b00168e610662dd7af4', 'createTable, addPrimaryKey, addAutoIncrement', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
