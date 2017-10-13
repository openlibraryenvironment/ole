--
-- Copyright 2005-2014 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--




-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-02-27.sql
-- 



--
-- KULRICE-6842: Don't allow requests for null principals or null groups or null principal types
-- or null roles.
--

ALTER TABLE
  KRIM_GRP_MBR_T
MODIFY
   (
    MBR_ID VARCHAR2(40) NOT NULL,
    GRP_ID VARCHAR2(40) NOT NULL,
    MBR_TYP_CD CHAR(1) NOT NULL
   )
/

ALTER TABLE
   KRIM_ROLE_MBR_T
MODIFY
   (
    MBR_ID VARCHAR2(40) NOT NULL,
    ROLE_ID VARCHAR2(40) NOT NULL,
    MBR_TYP_CD CHAR(1) NOT NULL
   )
/


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-03-05.sql
-- 


UPDATE KREW_DOC_TYP_T SET LBL = 'Undefined' WHERE LBL is null
/
ALTER TABLE KREW_DOC_TYP_T MODIFY (LBL NOT NULL)
/


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-03-13.sql
-- 


--
-- KULRICE-6916 KRIM_ENTITY_CACHE_T.PRSN_NM is too small
--

ALTER TABLE krim_entity_cache_t MODIFY (PRSN_NM VARCHAR2(255))
/


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-03-20.sql
-- 


--
-- KULRICE-5931 increase PLCY_VAL in order to store extended Recall notification configuration
--

ALTER TABLE krew_doc_typ_plcy_reln_t MODIFY (PLCY_VAL VARCHAR2(1024))
/


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-03-28.sql
-- 


-- KULRICE-5931

-- add 'appDocStatus' attr definition
INSERT INTO KRIM_ATTR_DEFN_T VALUES ((select (max(to_number(KIM_ATTR_DEFN_ID)) + 1) from KRIM_ATTR_DEFN_T where KIM_ATTR_DEFN_ID is not NULL and regexp_like(KIM_ATTR_DEFN_ID, '^[1-9][0-9]{0,3}$') ), sys_guid(), 1, 'appDocStatus', null, 'Y', 'KR-WKFLW', 'org.kuali.rice.kim.bo.impl.KimAttributes')
/

-- assign it to 'Document Type & Routing Node or State' type
INSERT INTO KRIM_TYP_ATTR_T VALUES ((select (max(to_number(KIM_TYP_ATTR_ID)) + 1) from KRIM_TYP_ATTR_T where KIM_TYP_ATTR_ID is not NULL and regexp_like(KIM_TYP_ATTR_ID, '^[1-9][0-9]{0,3}$') ), sys_guid(), 1, 'a', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-SYS' and NM='Document Type & Routing Node or State'), (select KIM_ATTR_DEFN_ID from KRIM_ATTR_DEFN_T where NMSPC_CD='KR-WKFLW' and NM='appDocStatus'), 'Y')
/

-- create Recall permission template
INSERT INTO KRIM_PERM_TMPL_T VALUES ((select (max(to_number(perm_tmpl_id)) + 1) from krim_perm_tmpl_t where perm_tmpl_id is not NULL and regexp_like(perm_tmpl_id, '^[1-9][0-9]{0,3}$') ), sys_guid(), 1, 'KR-WKFLW', 'Recall Document', null, (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-SYS' and NM='Document Type & Routing Node or State'), 'Y')
/



-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-04-04.sql
--


-- KULRICE-6784 Add index and constraint on KREW_RULE_ATTR_T.NM
alter table KREW_RULE_ATTR_T add constraint KREW_RULE_ATTR_TC1 unique(NM)
/


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-04-11.sql
--


-- insert Recall permission for initiators
INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM) values ((select (max(to_number(PERM_ID)) + 1) from KRIM_PERM_T where PERM_ID is not NULL and regexp_like(PERM_ID, '^[1-9][0-9]{0,3}$') ), sys_guid(), 1, (select PERM_TMPL_ID from KRIM_PERM_TMPL_T where NMSPC_CD = 'KR-WKFLW' and NM = 'Recall Document'), 'KR-WKFLW', 'Recall Document')
/
-- define document type wildcard for permission
INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) values ((select (max(to_number(ATTR_DATA_ID)) + 1) from KRIM_PERM_ATTR_DATA_T where ATTR_DATA_ID is not NULL and regexp_like(ATTR_DATA_ID, '^[1-9][0-9]{0,3}$') ), sys_guid(), 1, (select PERM_ID from KRIM_PERM_T where NMSPC_CD = 'KR-WKFLW' and NM='Recall Document'), (select KIM_TYP_ID from KRIM_PERM_TMPL_T where NMSPC_CD = 'KR-WKFLW' and NM = 'Recall Document'), (select KIM_ATTR_DEFN_ID from KRIM_ATTR_DEFN_T where NMSPC_CD = 'KR-WKFLW' and NM = 'documentTypeName'), '*')
/
-- associate Recall permission with initiator derived role
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) values ((select (max(to_number(ROLE_PERM_ID)) + 1) from KRIM_ROLE_PERM_T where ROLE_PERM_ID is not NULL and regexp_like(ROLE_PERM_ID, '^[1-9][0-9]{0,3}$') ), sys_guid(), 1, (select ROLE_ID from KRIM_ROLE_T where ROLE_NM = 'Initiator' and NMSPC_CD = 'KR-WKFLW'), (select PERM_ID from KRIM_PERM_T where NMSPC_CD = 'KR-WKFLW' and NM='Recall Document'), 'Y')
/



-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-04-12.sql
--


-- create a Kim Type wired to the documentRouterRoleTypeService permission-derived role service
INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD) values ((select (max(to_number(KIM_TYP_ID)) + 1) from KRIM_TYP_T where KIM_TYP_ID is not NULL and regexp_like(KIM_TYP_ID, '^[1-9][0-9]{0,3}$') ), sys_guid(), 1, 'Derived Role: Permission (Route Document)', 'documentRouterRoleTypeService', 'Y', 'KR-WKFLW')
/
-- define the Route Document derived role
INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND) values ((select (max(to_number(ROLE_ID)) + 1) from KRIM_ROLE_T where ROLE_ID is not NULL and regexp_like(ROLE_ID, '^[1-9][0-9]{0,3}$') ), sys_guid(), 1, 'Document Router', 'KR-WKFLW', 'This role derives its members from users with the Route Document permission for a given document type.', (select KIM_TYP_ID from KRIM_TYP_T where NM = 'Derived Role: Permission (Route Document)' and NMSPC_CD = 'KR-WKFLW'), 'Y')
/



-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-04-16b.sql
--



-- KULRICE-6964: Update Rice default From email address

update KRCR_PARM_T set val='rice.test@kuali.org'
where nmspc_cd='KR-WKFLW' and cmpnt_cd='Mailer' and parm_nm='FROM_ADDRESS' and appl_id='KUALI'
  and val = 'quickstart@localhost'
/


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-04-16.sql
--


-- KULRICE-7128 wire kim type attribute 'qualifierResolverProvidedIdentifier' to Responsibility type
Insert into krim_typ_attr_t
(KIM_TYP_ATTR_ID,
OBJ_ID,
VER_NBR,
SORT_CD,
KIM_TYP_ID,
KIM_ATTR_DEFN_ID,
ACTV_IND)
VALUES
  ((select (max(to_number(KIM_TYP_ATTR_ID)) + 1) from  krim_typ_attr_t where KIM_TYP_ATTR_ID is not NULL and regexp_like(KIM_TYP_ATTR_ID, '^[1-9][0-9]{0,3}$') ),
  '69FA55ACC2EE2598E0404F8189D86880',
  1,
  'e',
  7,
  46,
  'Y')
/



-- -- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- -- 2012-04-19b.sql
-- --
--
--
-- create table TRV_ATT_SAMPLE (attachment_id varchar2(30),
--                               description varchar2(4000),
--                               attachment_filename varchar2(300),
--                               attachment_file_content_type varchar2(255),
--                               attachment_file blob,
--                               obj_id varchar2(36) not null,
--                               ver_nbr number(8) default 0 not null,
--                               primary key (attachment_id))
-- /
-- create table TRV_MULTI_ATT_SAMPLE (gen_id number(14,0) not null,
--                               attachment_id varchar2(30),
--                               description varchar2(4000),
--                               attachment_filename varchar2(300),
--                               attachment_file_content_type varchar2(255),
--                               attachment_file blob,
--                               obj_id varchar2(36) not null,
--                               ver_nbr number(8) default 0 not null,
--                               primary key (gen_id),
--                               foreign key (attachment_id) references TRV_ATT_SAMPLE(attachment_id))
-- /


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-04-19.sql
--


CREATE TABLE KRNS_MAINT_DOC_ATT_LST_T  (
    ATT_ID      VARCHAR2(40) NOT NULL,
	DOC_HDR_ID	VARCHAR2(14) NOT NULL,
	ATT_CNTNT 	blob NOT NULL,
	FILE_NM   	VARCHAR2(150) NULL,
	CNTNT_TYP 	VARCHAR2(255) NULL,
	OBJ_ID    	VARCHAR2(36) NOT NULL,
	VER_NBR   	NUMBER(8) DEFAULT 0 NOT NULL,
	PRIMARY KEY(ATT_ID),
	CONSTRAINT KRNS_MAINT_DOC_ATT_LST_FK1 foreign key (DOC_HDR_ID) references KRNS_MAINT_DOC_T (DOC_HDR_ID)
)
/
ALTER TABLE KRNS_MAINT_DOC_ATT_LST_T
	ADD CONSTRAINT KRNS_MAINT_DOC_ATT_LST_TC0
	UNIQUE (OBJ_ID)
/
CREATE INDEX KRNS_MAINT_DOC_ATT_LST_TI1 on KRNS_MAINT_DOC_ATT_LST_T (DOC_HDR_ID)
/
CREATE SEQUENCE KRNS_MAINT_DOC_ATT_S INCREMENT BY 1 START WITH 10000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/





-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-04-25.sql
--


update krms_attr_defn_t set nm='actionTypeCode' where attr_defn_id='1004'
/
update krms_attr_defn_t set nm='actionMessage' where attr_defn_id='1005'
/
update krms_attr_defn_t set nm='ruleTypeCode' where attr_defn_id='1001'
/

delete from krms_typ_attr_t where ATTR_DEFN_ID = '1002'
/
delete from krms_typ_attr_t where ATTR_DEFN_ID = '1003'
/
delete from krms_attr_defn_t where ATTR_DEFN_ID = '1002'
/
delete from krms_attr_defn_t where ATTR_DEFN_ID = '1003'
/


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-05-11.sql
--


insert into krim_perm_t
(perm_id, perm_tmpl_id, nmspc_cd, nm, desc_txt, actv_ind, ver_nbr, obj_id)
values ((select (max(to_number(perm_id)) + 1) from krim_perm_t where perm_id is not NULL and regexp_like(perm_id, '^[1-9][0-9]{0,3}$') ),
        (select perm_tmpl_id from krim_perm_tmpl_t where nm = 'Send Ad Hoc Request' and nmspc_cd = 'KR-NS'),
        'KR-SYS','Send Complete Request Kuali Document','Authorizes users to send Complete ad hoc requests for Kuali Documents','Y',1,sys_guid())
/

insert into krim_perm_attr_data_t
(attr_data_id, perm_id, kim_typ_id, kim_attr_defn_id, attr_val, ver_nbr, obj_id)
values ((select (max(to_number(attr_data_id)) + 1) from krim_perm_attr_data_t where attr_data_id is not NULL and regexp_like(attr_data_id, '^[1-9][0-9]{0,3}$') ),
        (select perm_id from krim_perm_t where nm = 'Send Complete Request Kuali Document' and nmspc_cd = 'KR-SYS'),
        (select kim_typ_id from krim_typ_t where nm = 'Ad Hoc Review' and nmspc_cd = 'KR-WKFLW'),
        (select kim_attr_defn_id from krim_attr_defn_t where nm = 'documentTypeName'), 'KualiDocument',1,sys_guid())
/


insert into krim_perm_attr_data_t
(attr_data_id, perm_id, kim_typ_id, kim_attr_defn_id, attr_val, ver_nbr, obj_id)
values ((select (max(to_number(attr_data_id)) + 1) from krim_perm_attr_data_t where attr_data_id is not NULL and regexp_like(attr_data_id, '^[1-9][0-9]{0,3}$') ),
        (select perm_id from krim_perm_t where nm = 'Send Complete Request Kuali Document' and nmspc_cd = 'KR-SYS'),
        (select kim_typ_id from krim_typ_t where nm = 'Ad Hoc Review' and nmspc_cd = 'KR-WKFLW'),
        (select kim_attr_defn_id from krim_attr_defn_t where nm = 'actionRequestCd'), 'C',1,sys_guid())
/

insert into krim_role_perm_t
(role_perm_id, role_id, perm_id, actv_ind, ver_nbr, obj_id)
values ((select (max(to_number(role_perm_id)) + 1) from krim_role_perm_t where role_perm_id is not NULL and regexp_like(role_perm_id, '^[1-9][0-9]{0,3}$') ),
        (select role_id from krim_role_t where role_nm = 'Document Opener' and nmspc_cd = 'KR-NS'),
        (select perm_id from krim_perm_t where nm = 'Send Complete Request Kuali Document' and nmspc_cd = 'KR-SYS'),
        'Y', 1, sys_guid())
/

insert into krim_role_perm_t
(role_perm_id, role_id, perm_id, actv_ind, ver_nbr, obj_id)
values ((select (max(to_number(role_perm_id)) + 1) from krim_role_perm_t where role_perm_id is not NULL and regexp_like(role_perm_id, '^[1-9][0-9]{0,3}$') ),
        (select role_id from krim_role_t where role_nm = 'Initiator or Reviewer' and nmspc_cd = 'KR-WKFLW'),
        (select perm_id from krim_perm_t where nm = 'Edit Kuali ENROUTE Document Route Status Code R' and nmspc_cd = 'KUALI'),
        'Y', 1, sys_guid())
/

insert into krim_role_perm_t
(role_perm_id, role_id, perm_id, actv_ind, ver_nbr, obj_id)
values ((select (max(to_number(role_perm_id)) + 1) from krim_role_perm_t where role_perm_id is not NULL and regexp_like(role_perm_id, '^[1-9][0-9]{0,3}$') ),
        (select role_id from krim_role_t where role_nm = 'Initiator or Reviewer' and nmspc_cd = 'KR-WKFLW'),
        (select perm_id from krim_perm_t where nm = 'Cancel Document' and nmspc_cd = 'KUALI'),
        'Y', 1, sys_guid())
/


-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-05-17.sql
-- 



-- KULRICE-7237: KRNS_NTE_T is selected by a field with no indexes - full table scan every time
create index KRNS_NTE_TI1 on KRNS_NTE_T (RMT_OBJ_ID)
/



-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-05-21.sql
-- 


update krim_perm_attr_data_t set ATTR_VAL='org.kuali.rice.core.web.impex.IngesterAction' where ATTR_VAL='org.kuali.rice.kew.batch.web.IngesterAction'
/





-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-05-24.sql
-- 



--
--  KULRICE-7377: KREW_RTE_NODE_T still defines DOC_TYP_ID as NUMBER(19)
--

-- rename the old table
ALTER TABLE KREW_RTE_NODE_T RENAME TO OLD_KREW_RTE_NODE_T
/

-- redefine it with the correct column types
CREATE TABLE KREW_RTE_NODE_T
(
   RTE_NODE_ID varchar2(40),
   DOC_TYP_ID varchar2(40),
   NM varchar2(255) NOT NULL,
   TYP varchar2(255) NOT NULL,
   RTE_MTHD_NM varchar2(255),
   RTE_MTHD_CD varchar2(2),
   FNL_APRVR_IND decimal(1),
   MNDTRY_RTE_IND decimal(1),
   ACTVN_TYP varchar2(250),
   BRCH_PROTO_ID varchar2(40),
   VER_NBR decimal(8) DEFAULT 0
        ,
   CONTENT_FRAGMENT varchar2(4000),
   GRP_ID varchar2(40),
   NEXT_DOC_STAT varchar2(64)
)
/

-- copy over the data, casting the column that changed
INSERT INTO KREW_RTE_NODE_T(
RTE_NODE_ID
        , DOC_TYP_ID
        , NM
        , TYP
        , RTE_MTHD_NM
        , RTE_MTHD_CD
        , FNL_APRVR_IND
        , MNDTRY_RTE_IND
        , ACTVN_TYP
        , BRCH_PROTO_ID
        , VER_NBR
        , CONTENT_FRAGMENT
        , GRP_ID
        , NEXT_DOC_STAT
)
SELECT RTE_NODE_ID
        , cast(DOC_TYP_ID AS varchar2(40))
        , NM
        , TYP
        , RTE_MTHD_NM
        , RTE_MTHD_CD
        , FNL_APRVR_IND
        , MNDTRY_RTE_IND
        , ACTVN_TYP
        , BRCH_PROTO_ID
        , VER_NBR
        , CONTENT_FRAGMENT
        , GRP_ID
        , NEXT_DOC_STAT
FROM OLD_KREW_RTE_NODE_T
/

-- -- drop the old one
-- DROP TABLE OLD_KREW_RTE_NODE_T CASCADE CONSTRAINTS PURGE
-- /

--
-- add back all the constraints and indexes
--


DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_constraints WHERE constraint_name = 'KREW_RTE_NODE_TP1';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'ALTER TABLE OLD_KREW_RTE_NODE_T DROP CONSTRAINT KREW_RTE_NODE_TP1 CASCADE';
	END IF;
END;
/

ALTER TABLE KREW_RTE_NODE_T
    ADD CONSTRAINT KREW_RTE_NODE_TP1
PRIMARY KEY (RTE_NODE_ID)
/


DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM all_indexes WHERE index_name ='KREW_RTE_NODE_TI4';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'DROP INDEX KREW_RTE_NODE_TI4';
	END IF;
END;
/

CREATE INDEX KREW_RTE_NODE_TI4 ON KREW_RTE_NODE_T(DOC_TYP_ID)
/


DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM all_indexes WHERE index_name ='KREW_RTE_NODE_TI3';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'DROP INDEX KREW_RTE_NODE_TI3';
	END IF;
END;
/

CREATE INDEX KREW_RTE_NODE_TI3 ON KREW_RTE_NODE_T(BRCH_PROTO_ID)
/


DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM all_indexes WHERE index_name ='KREW_RTE_NODE_TI2';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'DROP INDEX KREW_RTE_NODE_TI2';
	END IF;
END;
/

CREATE INDEX KREW_RTE_NODE_TI2 ON KREW_RTE_NODE_T
(
  DOC_TYP_ID,
  FNL_APRVR_IND
)
/


DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM all_indexes WHERE index_name ='KREW_RTE_NODE_TI1';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'DROP INDEX KREW_RTE_NODE_TI1';
	END IF;
END;
/

CREATE INDEX KREW_RTE_NODE_TI1 ON KREW_RTE_NODE_T
(
  NM,
  DOC_TYP_ID
)
/



-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
-- 2012-05-25.sql
-- 



--
--  KULRICE-7375: Rice master data source has KREW_DOC_TYP_PROC_T.INIT_RTE_NODE_ID still defined as a NUMBER
--

-- rename the old table
ALTER TABLE KREW_DOC_TYP_PROC_T RENAME TO OLD_KREW_DOC_TYP_PROC_T
/

-- redefine it with the correct column types
CREATE TABLE KREW_DOC_TYP_PROC_T
(
   DOC_TYP_PROC_ID varchar2(40),
   DOC_TYP_ID varchar2(40) NOT NULL,
   INIT_RTE_NODE_ID varchar2(40),
   NM varchar2(255) NOT NULL,
   INIT_IND decimal(1) DEFAULT 0  NOT NULL,
   VER_NBR decimal(8) DEFAULT 0
)
/

-- copy over the data, casting the column that changed
INSERT INTO KREW_DOC_TYP_PROC_T (
   DOC_TYP_PROC_ID
   , DOC_TYP_ID
   , INIT_RTE_NODE_ID
   , NM
   , INIT_IND
   , VER_NBR
)
SELECT DOC_TYP_PROC_ID
   , DOC_TYP_ID
   , cast(INIT_RTE_NODE_ID AS varchar2(40))
   , NM
   , INIT_IND
   , VER_NBR
FROM OLD_KREW_DOC_TYP_PROC_T
/

-- -- drop the old one
-- DROP TABLE OLD_KREW_DOC_TYP_PROC_T CASCADE CONSTRAINTS PURGE
-- /

--
-- add back all the constraints and indexes
--


DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_constraints WHERE constraint_name = 'KREW_DOC_TYP_PROC_TP1';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'ALTER TABLE OLD_KREW_DOC_TYP_PROC_T DROP CONSTRAINT KREW_DOC_TYP_PROC_TP1 CASCADE';
	END IF;
END;
/

ALTER TABLE KREW_DOC_TYP_PROC_T
    ADD CONSTRAINT KREW_DOC_TYP_PROC_TP1
PRIMARY KEY (DOC_TYP_PROC_ID)
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM all_indexes WHERE index_name ='KREW_DOC_TYP_PROC_TI3';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'DROP INDEX KREW_DOC_TYP_PROC_TI3';
	END IF;
END;
/

CREATE INDEX KREW_DOC_TYP_PROC_TI3 ON KREW_DOC_TYP_PROC_T(NM)
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM all_indexes WHERE index_name ='KREW_DOC_TYP_PROC_TI2';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'DROP INDEX KREW_DOC_TYP_PROC_TI2';
	END IF;
END;
/

CREATE INDEX KREW_DOC_TYP_PROC_TI2 ON KREW_DOC_TYP_PROC_T(INIT_RTE_NODE_ID)
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM all_indexes WHERE index_name ='KREW_DOC_TYP_PROC_TI1';
	IF temp > 0 THEN EXECUTE IMMEDIATE
		'DROP INDEX KREW_DOC_TYP_PROC_TI1';
	END IF;
END;
/

CREATE INDEX KREW_DOC_TYP_PROC_TI1 ON KREW_DOC_TYP_PROC_T(DOC_TYP_ID)
/