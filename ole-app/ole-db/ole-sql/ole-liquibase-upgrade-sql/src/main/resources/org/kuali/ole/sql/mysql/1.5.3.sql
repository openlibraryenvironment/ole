--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/1.5.3/db.changelog-20140806.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_BAT_PRCS_PRFLE_CNST_S::ole
INSERT INTO OLE_BAT_PRCS_PRFLE_CNST_S values ()
/

set @last_id = last_insert_id()
/

INSERT INTO OLE_BAT_PRCS_PRFLE_CNST_T (OLE_USR_DEF_VAL_ID, ATT_NM, ATT_VAL, DATA_TYPE, BAT_PRCS_PRF_ID, DEF_VAL, OBJ_ID, VER_NBR) VALUES (@last_id, 'deliveryBuildingRoomNumber', '001', 'OrderImport', '6', 'default', '1', '1')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_BAT_PRCS_PRFLE_CNST_S', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 1, '7:4b26ddfe73780ae0f4309d5e8a1c095c', 'sql (x3)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_PUR_PO_LOAD_SUM_T::ole
ALTER TABLE ole.OLE_PUR_PO_LOAD_SUM_T ADD BAT_PRCS_PRF_ID VARCHAR(40) NULL
/

ALTER TABLE OLE_PUR_PO_LOAD_SUM_T DROP FOREIGN KEY OLE_PUR_PO_LOAD_SUM_TR1
/

ALTER TABLE ole.OLE_PUR_PO_LOAD_SUM_T DROP COLUMN PRFL_ID
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_PUR_PO_LOAD_SUM_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 2, '7:c8ed4178c13eb61a95b10641743836b6', 'addColumn, dropForeignKeyConstraint, dropColumn', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_DLVR_CIRC_RECORD::ole
ALTER TABLE ole.OLE_DLVR_CIRC_RECORD MODIFY BIB_TIT VARCHAR(4000)
/

CREATE INDEX OLE_DLVR_CIRC_RECORD_TI1 ON ole.OLE_DLVR_CIRC_RECORD(LOAN_TRAN_ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_CIRC_RECORD', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 3, '7:2a923d29e6859748053b72feb6abfa3e', 'modifyDataType, createIndex', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::KRCR_PARM_T_cancellation::ole
UPDATE KRCR_PARM_T SET VAL = 'Your requested item(s) has been cancelled because it has become unavailable, either because it has been reported missing or is needed for course reserve. You may request another copy if you still need this item. Please contact the library if you need further details.' WHERE parm_nm = 'CANCELLATION_BODY'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRCR_PARM_T_cancellation', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 4, '7:c22da5597bf60f8e2a0521157e2297a6', 'update', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::KRCR_PARM_T_helplink::ole
UPDATE KRCR_PARM_T SET VAL = 'customerLoadInputFileType=/reference/webhelp/AG/content/ch21s18.html;procurementCardInputFileType=/reference/webhelp/AG/content/ch21s18.html;collectorFlatFileInputFileType=/reference/webhelp/AG/content/ch21s18.html;collectorXmlInputFileType=/reference/webhelp/AG/content/ch21s18.html;enterpriseFeederFileSetType=/reference/webhelp/AG/content/ch21s18.html;laborEnterpriseFeederFileSetType=/reference/webhelp/AG/content/ch21s18.html;assetBarcodeInventoryInputFileType=/reference/webhelp/AG/content/ch21s18.html;paymentInputFileType=/reference/webhelp/AG/content/ch21s18.html' WHERE parm_nm = 'BATCH_UPLOAD_HELP_URL'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRCR_PARM_T_helplink', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 5, '7:fdff1796dac7d02687a2d8faea2c90dd', 'update', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::KREW_DOC_TYP_T_helpLink::ole
UPDATE KREW_DOC_TYP_T SET HELP_DEF_URL = '${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch08s03.html' WHERE DOC_TYP_NM = 'CampusTypeMaintenanceDocument'
/

UPDATE KREW_DOC_TYP_T SET HELP_DEF_URL = '${ole.externalizable.help.url}/reference/webhelp/ADMIN/content/ch04s09.html' WHERE DOC_TYP_NM = 'ComponentMaintenanceDocument'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KREW_DOC_TYP_T_helpLink', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 6, '7:26204fac28a53c161b3c692d67b3c943', 'update (x2)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::KRIM_PERM_T::ole
UPDATE KRIM_PERM_T SET DESC_TXT = 'Allows users to view Serial Receiving record from Holdings record.', NM = 'Show Serials Receiving Record From Holding' WHERE perm_id = 'OLE10193'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_PERM_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 7, '7:8223a1ab6f5f3252018be18f9d395d27', 'update', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::KRIM_ROLE_PERM_T::ole
UPDATE KRIM_ROLE_PERM_T SET ROLE_ID = 'OLE10086' WHERE role_perm_id = 'OLE10712'
/

UPDATE KRIM_ROLE_PERM_T SET ROLE_ID = 'OLE10086' WHERE role_perm_id = 'OLE12154'
/

UPDATE KRIM_ROLE_PERM_T SET ROLE_ID = 'OLE10086' WHERE role_perm_id = 'OLE12223'
/

DELETE FROM KRIM_ROLE_PERM_T  WHERE role_perm_id = 'OLE750'
/

DELETE FROM KRIM_ROLE_PERM_T  WHERE role_perm_id = 'OLE12155'
/

DELETE FROM KRIM_ROLE_PERM_T  WHERE role_perm_id = 'OLE12224'
/

DELETE FROM KRIM_ROLE_PERM_T  WHERE role_perm_id = 'OLE12225'
/

DELETE FROM KRIM_ROLE_PERM_T  WHERE role_perm_id = 'OLE12226'
/

DELETE FROM KRIM_ROLE_PERM_T  WHERE role_perm_id = 'OLE12227'
/

DELETE FROM KRIM_ROLE_PERM_T  WHERE role_perm_id = 'OLE12228'
/

DELETE FROM KRIM_ROLE_PERM_T  WHERE perm_id = 'OLE10297'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ROLE_PERM_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 8, '7:294858a1bbc78e5ded93f61086bb15d7', 'update (x3), delete (x8)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_DS_ITEM_T::ole
ALTER TABLE ole.OLE_DS_ITEM_T ADD CHECK_OUT_DATE_TIME TIMESTAMP NULL
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DS_ITEM_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 9, '7:985d4709b7b6452044844dbe345b5a56', 'addColumn', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::KRIM_PERM_T_delete::ole
DELETE FROM KRIM_PERM_T  WHERE perm_id = 'OLE10297'
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_PERM_T_delete', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 10, '7:5a07e633194a381ee9b4009b3d52acae', 'delete', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_LINK_PURAP_DONOR_T::ole
CREATE INDEX OLE_LINK_PURAP_DONOR_TI1 ON ole.OLE_LINK_PURAP_DONOR_T(REQ_ITEM_ID)
/

CREATE INDEX OLE_LINK_PURAP_DONOR_TI2 ON ole.OLE_LINK_PURAP_DONOR_T(FDOC_NBR)
/

CREATE INDEX OLE_LINK_PURAP_DONOR_TI3 ON ole.OLE_LINK_PURAP_DONOR_T(PO_ITM_ID)
/

CREATE INDEX OLE_LINK_PURAP_DONOR_TI4 ON ole.OLE_LINK_PURAP_DONOR_T(RCVNG_LN_ITM_ID)
/

CREATE INDEX OLE_LINK_PURAP_DONOR_TI5 ON ole.OLE_LINK_PURAP_DONOR_T(RCVNG_COR_ITM_ID)
/

CREATE INDEX OLE_LINK_PURAP_DONOR_TI6 ON ole.OLE_LINK_PURAP_DONOR_T(DONOR_ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LINK_PURAP_DONOR_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 11, '7:2d49c0d4cb6a5dec44f976da7739e184', 'createIndex (x6)', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::GL_PENDING_ENTRY_T::ole
CREATE INDEX GL_PENDING_ENTRY_TI7 ON ole.GL_PENDING_ENTRY_T(FDOC_NBR)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('GL_PENDING_ENTRY_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 12, '7:83a21489ac261a261d68156855849daa', 'createIndex', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_DLVR_PTRN_BILL_T::ole
CREATE INDEX OLE_DLVR_PTRN_BILL_TI1 ON ole.OLE_DLVR_PTRN_BILL_T(OLE_PTRN_ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_PTRN_BILL_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 13, '7:858bf73d47e0cc320b98bdfaa93c0d1f', 'createIndex', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_DLVR_ADD_T::ole
CREATE INDEX OLE_DLVR_ADD_TI1 ON ole.OLE_DLVR_ADD_T(OLE_PTRN_ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_ADD_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 14, '7:ff253f55c90389ab916a9e9bc2b8393d', 'createIndex', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_ERES_REQ_T::ole
CREATE INDEX OLE_ERES_REQ_TI1 ON ole.OLE_ERES_REQ_T(OLE_PTRN_ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_ERES_REQ_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 15, '7:131719ff3188b381b3b8d7a3d13fb12a', 'createIndex', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_DLVR_RQST_T::ole
CREATE INDEX OLE_DLVR_RQST_TI1 ON ole.OLE_DLVR_RQST_T(LOAN_TRAN_ID)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_DLVR_RQST_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 16, '7:1acba73625ce0d1ae20864e127bc5ad0', 'createIndex', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/1.5.3/db.changelog-20140806.xml::OLE_AP_INV_ACCT_T::ole
CREATE INDEX OLE_AP_INV_ACCT_TI2 ON ole.OLE_AP_INV_ACCT_T(FIN_COA_CD)
/

CREATE INDEX OLE_AP_INV_ACCT_TI3 ON ole.OLE_AP_INV_ACCT_T(FIN_COA_CD, ACCOUNT_NBR)
/

CREATE INDEX OLE_AP_INV_ACCT_TI4 ON ole.OLE_AP_INV_ACCT_T(FIN_COA_CD, ACCOUNT_NBR, SUB_ACCT_NBR)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_AP_INV_ACCT_T', 'ole', 'org/kuali/ole/1.5.3/db.changelog-20140806.xml', NOW(), 17, '7:d8fadb7cc88a5308ae322968c9032ee1', 'createIndex (x3)', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
