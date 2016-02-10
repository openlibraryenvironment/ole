--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/3.0/db.changelog-20160208.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/3.0/db.changelog-20160208.xml::OLE_CAT_SHVLG_SCHM_T_UPDATE::ole
UPDATE OLE_CAT_SHVLG_SCHM_T SET SHVLG_SCHM_CD = 'EIGHT', SHVLG_SCHM_ID = '10' WHERE SHVLG_SCHM_ID = 8
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_CAT_SHVLG_SCHM_T_UPDATE', 'ole', 'org/kuali/ole/3.0/db.changelog-20160208.xml', NOW(), 1, '7:07bcfea29f570366d060d68cbb549c8f', 'update', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0/db.changelog-20160208.xml::OLE_CAT_SHVLG_SCHM_T_INSERT1::ole
INSERT INTO OLE_CAT_SHVLG_SCHM_T (OBJ_ID,ROW_ACT_IND,SHVLG_SCHM_CD,SHVLG_SCHM_ID,SHVLG_SCHM_NM,SRC,SRC_DT,VER_NBR) VALUES ('fb8119a1-eafb-4150-ad72-0f9c67e7d7df','Y','SIX',8,'Shelved separately','MFHD 852 1st Indicator: http://www.loc.gov/marc/holdings/hd852.html','2012-03-22 00:00:00',1)
/

INSERT INTO OLE_CAT_SHVLG_SCHM_T (OBJ_ID,ROW_ACT_IND,SHVLG_SCHM_CD,SHVLG_SCHM_ID,SHVLG_SCHM_NM,SRC,SRC_DT,VER_NBR) VALUES ('fb8119a1-eafb-4150-ad72-0f9c67e7d7dg','Y','SEVEN',9,'Source specified in subfield $2','MFHD 852 1st Indicator: http://www.loc.gov/marc/holdings/hd852.html','2012-03-22 00:00:00',1)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_CAT_SHVLG_SCHM_T_INSERT1', 'ole', 'org/kuali/ole/3.0/db.changelog-20160208.xml', NOW(), 2, '7:1cf7eba53d543d0c6c10d8a3d9e7c94c', 'sql (x2)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0/db.changelog-20160208.xml::PUR_REQS_T_BILL_PHN_NBR_NULL::ole
ALTER TABLE PUR_REQS_T MODIFY BILL_PHN_NBR VARCHAR(20) NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('PUR_REQS_T_BILL_PHN_NBR_NULL', 'ole', 'org/kuali/ole/3.0/db.changelog-20160208.xml', NOW(), 4, '7:0c66f770f71c621ab63c8ba4c67c8929', 'dropNotNullConstraint', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
