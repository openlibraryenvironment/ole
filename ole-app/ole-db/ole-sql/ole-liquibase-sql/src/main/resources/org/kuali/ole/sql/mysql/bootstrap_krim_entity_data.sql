--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: bootstrap_krim_entity_data.xml
--  *********************************************************************

--  Lock Database
--  Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_T::ole
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_T', 'ole', 'bootstrap_krim_entity_data.xml', NOW(), 1, '7:94acf77a9230ca8cc3e779587bccf4ae', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset bootstrap_krim_entity_data.xml::KRIM_PRNCPL_T::ole
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_PRNCPL_T', 'ole', 'bootstrap_krim_entity_data.xml', NOW(), 2, '7:d0f82650f07f1f6ef47e2f9bf15cf39c', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_ENT_TYP_T::ole
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_ENT_TYP_T', 'ole', 'bootstrap_krim_entity_data.xml', NOW(), 3, '7:b67a121310cd2d7861cbf25caef813e6', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset bootstrap_krim_entity_data.xml::KRIM_ENTITY_NM_T::ole
INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('KRIM_ENTITY_NM_T', 'ole', 'bootstrap_krim_entity_data.xml', NOW(), 4, '7:73893b9796b6bb6a75c3203d2202fc53', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
