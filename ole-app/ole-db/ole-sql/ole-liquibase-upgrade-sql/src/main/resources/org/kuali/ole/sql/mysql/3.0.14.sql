--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/3.0.14/db.changelog-20180810.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/3.0.14/db.changelog-20180810.xml::KRIM_MYSQL_HIGH_DENSITY_TRIGGERS_DROP::ole
DROP TRIGGER IF EXISTS HIGH_DEN_STRG_TR_INSERT
/

DROP TRIGGER IF EXISTS HIGH_DEN_STRG_TR_UPDATE
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_MYSQL_HIGH_DENSITY_TRIGGERS_DROP', 'ole', 'org/kuali/ole/3.0.14/db.changelog-20180810.xml', NOW(), 1, '7:8a8fdd0a4123027cc908ec8f46017e93', 'sql (x2)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0.14/db.changelog-20180810.xml::OLE_DS_DOC_FIELD_T_delete::ole
DELETE FROM OLE_DS_DOC_FIELD_T  WHERE NAME IN ('HighDensityStorage_Module_display','HighDensityStorage_Row_display','HighDensityStorage_Shelf_display','HighDensityStorage_Tray_display')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DS_DOC_FIELD_T_delete', 'ole', 'org/kuali/ole/3.0.14/db.changelog-20180810.xml', NOW(), 2, '7:d9acb680bf912a03a887af5e053c90e1', 'delete', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0.14/db.changelog-20180810.xml::OLE_DS_ITEM_T_DROP::ole
ALTER TABLE OLE_DS_ITEM_T DROP FOREIGN KEY HIGH_DENSITY_CONSTRAINT
/

ALTER TABLE OLE_DS_ITEM_T DROP COLUMN HIGH_DENSITY_STORAGE_ID
/

DROP TABLE ole.OLE_DS_HIGH_DENSITY_STORAGE_T
/

DROP TABLE ole.OLE_DS_HIGH_DENSITY_STORAGE_S
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DS_ITEM_T_DROP', 'ole', 'org/kuali/ole/3.0.14/db.changelog-20180810.xml', NOW(), 3, '7:825422b190adf3be544916ce7f3eb632', 'dropForeignKeyConstraint, dropColumn, dropTable (x2)', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
