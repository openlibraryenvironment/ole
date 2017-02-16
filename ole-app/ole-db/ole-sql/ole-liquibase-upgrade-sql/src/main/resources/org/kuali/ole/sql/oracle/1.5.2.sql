-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: org/kuali/ole/1.5.2/db.changelog-20140728.xml
-- *********************************************************************

-- Lock Database
-- Changeset org/kuali/ole/1.5.2/db.changelog-20140728.xml::OLE_DS_ITEM_T::ole
ALTER TABLE ole.OLE_DS_ITEM_T ADD NUM_OF_RENEW NUMBER(10)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DS_ITEM_T', 'ole', 'org/kuali/ole/1.5.2/db.changelog-20140728.xml', SYSTIMESTAMP, 1, '7:93503c8c1298520974abba5e9f3e2e2f', 'addColumn', '', 'EXECUTED', '3.2.0')
/

-- Changeset org/kuali/ole/1.5.2/db.changelog-20140728.xml::OLE_LOAD_KRCR_PARM_T_oracle::ole
INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'ITEM_STATUS_FOR_RET_LOAN', 'OLE6516', '1', 'CONFG', 'LOANED|LOST', 'This is for retrieving all the item', 'A', 'OLE')
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'MY_ACCOUNT_URL', 'OLE6517', '1', 'CONFG', 'http://dev.oleproject.org/olefs/portal.do?channelTitle=MyAccount&channelUrl=http://dev.oleproject.org/olefs/ole-kr-krad/myaccountcontroller?viewId=RenewalItemView&methodToCall=start', 'This is for retrieving all the item', 'A', 'OLE')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRCR_PARM_T_oracle', 'ole', 'org/kuali/ole/1.5.2/db.changelog-20140728.xml', SYSTIMESTAMP, 2, '7:0ae9b2a7b842cd20a66af2d894f0f839', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Release Database Lock
-- Release Database Lock
