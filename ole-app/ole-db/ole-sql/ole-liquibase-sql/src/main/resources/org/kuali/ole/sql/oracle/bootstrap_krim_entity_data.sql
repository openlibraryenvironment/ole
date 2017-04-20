-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: bootstrap_krim_entity_data.xml
-- *********************************************************************

-- Lock Database
-- Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_T::ole
INSERT INTO KRIM_ENTITY_T (ENTITY_ID, OBJ_ID, ACTV_IND) VALUES ('gobiapi', 'gobiapi', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_T', 'ole', 'bootstrap_krim_entity_data.xml', SYSTIMESTAMP, 1, '7:d7db138d3f53b161c07e9742cd19da6b', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_entity_data.xml::KRIM_PRNCPL_T::ole
INSERT INTO KRIM_PRNCPL_T (PRNCPL_ID, OBJ_ID, PRNCPL_NM, ENTITY_ID, ACTV_IND) VALUES ('gobiapi', 'gobiapi', 'gobi-api', 'gobiapi', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_PRNCPL_T', 'ole', 'bootstrap_krim_entity_data.xml', SYSTIMESTAMP, 2, '7:777b34c90ad43fdeb78abb70fa317dbc', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_ENT_TYP_T::ole
INSERT INTO KRIM_ENTITY_ENT_TYP_T (ENT_TYP_CD, ENTITY_ID, OBJ_ID, ACTV_IND) VALUES ('PERSON', 'gobiapi', 'gobiapi-PERSON', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_ENT_TYP_T', 'ole', 'bootstrap_krim_entity_data.xml', SYSTIMESTAMP, 3, '7:cdf8c5a44c9a88df66c71fe38f28064d', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_NM_T::ole
INSERT INTO KRIM_ENTITY_NM_T (entity_nm_id, obj_id, entity_id, nm_typ_cd, first_nm, middle_nm, last_nm, suffix_nm, title_nm, dflt_ind, actv_ind) VALUES ('gobiapi', 'gobiapi', 'gobiapi', 'PRFR', 'gobi', '', 'api', '', '', 'Y', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_NM_T', 'ole', 'bootstrap_krim_entity_data.xml', SYSTIMESTAMP, 4, '7:9e5ae467b6aaa17940b5f68f800948e2', 'loadData', '', 'EXECUTED', '3.2.0')
/

-- Release Database Lock
-- Release Database Lock
