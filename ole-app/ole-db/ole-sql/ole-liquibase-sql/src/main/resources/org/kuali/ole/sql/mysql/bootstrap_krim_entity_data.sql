--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: bootstrap_krim_entity_data.xml
--  *********************************************************************

--  Lock Database
--  Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_T::ole
INSERT INTO KRIM_ENTITY_T (ENTITY_ID, OBJ_ID, ACTV_IND) VALUES ('olequickstart', 'olequickstart', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_T', 'ole', 'bootstrap_krim_entity_data.xml', NOW(), 1, '7:0e065d55fff1d6fabc247fe74e133b5d', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset bootstrap_krim_entity_data.xml::KRIM_PRNCPL_T::ole
INSERT INTO KRIM_PRNCPL_T (PRNCPL_ID, OBJ_ID, PRNCPL_NM, ENTITY_ID, ACTV_IND) VALUES ('olequickstart', 'olequickstart', 'ole-quickstart', 'olequickstart', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_PRNCPL_T', 'ole', 'bootstrap_krim_entity_data.xml', NOW(), 2, '7:52b4a625f466646cdb0bff18df7e9592', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_ENT_TYP_T::ole
INSERT INTO KRIM_ENTITY_ENT_TYP_T (ENT_TYP_CD, ENTITY_ID, OBJ_ID, ACTV_IND) VALUES ('PERSON', 'olequickstart', 'olequickstart-PERSON', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_ENT_TYP_T', 'ole', 'bootstrap_krim_entity_data.xml', NOW(), 3, '7:abf22c29d72c8cdd2b82ccaf310ef5b7', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_NM_T::ole
INSERT INTO KRIM_ENTITY_NM_T (entity_nm_id, obj_id, entity_id, nm_typ_cd, first_nm, middle_nm, last_nm, suffix_nm, title_nm, dflt_ind, actv_ind) VALUES ('olequickstart', 'olequickstart', 'olequickstart', 'PRFR', 'ole', '', 'quickstart', '', '', 'Y', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_NM_T', 'ole', 'bootstrap_krim_entity_data.xml', NOW(), 4, '7:744c4b4fd182ce76a9b0eac8fd7c3466', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
