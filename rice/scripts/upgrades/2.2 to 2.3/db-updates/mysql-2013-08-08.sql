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

-- updating permissions for edit document KULRICE-9643
delete from krim_perm_attr_data_t where perm_id = (select distinct a.perm_id from krim_perm_t a,krim_perm_tmpl_t b where a.perm_tmpl_id = b.perm_tmpl_id and b.nmspc_cd = 'KR-NS' and b.nm = 'Edit Document' and a.nmspc_cd = 'KUALI' and a.nm = 'Edit Kuali ENROUTE Document Node Name PreRoute');

delete from krim_role_perm_t where perm_id = (select distinct a.perm_id from krim_perm_t a,krim_perm_tmpl_t b where a.perm_tmpl_id = b.perm_tmpl_id and b.nmspc_cd = 'KR-NS' and b.nm = 'Edit Document' and a.nmspc_cd = 'KUALI' and a.nm = 'Edit Kuali ENROUTE Document Node Name PreRoute');

delete from krim_perm_t where nmspc_cd = 'KUALI' and nm = 'Edit Kuali ENROUTE Document Node Name PreRoute' and perm_tmpl_id = (select perm_tmpl_id from krim_perm_tmpl_t where nmspc_cd = 'KR-NS' and nm = 'Edit Document');

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) values ((select PERM_ID from (select (max(cast(PERM_ID as decimal)) + 1) as PERM_ID from KRIM_PERM_T where PERM_ID is not NULL and PERM_ID REGEXP '^[1-9][0-9]*$' and cast(PERM_ID as decimal) < 10000) as tmptable), uuid(), 1, (select PERM_TMPL_ID from KRIM_PERM_TMPL_T where NMSPC_CD = 'KR-NS' and NM = 'Edit Document'), 'KUALI', 'Edit Kuali ENROUTE Document Route Status Code I', 'Allows users to edit Kuali documents that are in INITIATED status.','Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) values ((select ATTR_DATA_ID from (select (max(cast(ATTR_DATA_ID as decimal)) + 1) as ATTR_DATA_ID from KRIM_PERM_ATTR_DATA_T where ATTR_DATA_ID is not NULL and ATTR_DATA_ID REGEXP '^[1-9][0-9]*$' and cast(ATTR_DATA_ID as decimal) < 10000) as tmptable), uuid(), 1, (select PERM_ID from KRIM_PERM_T where NMSPC_CD = 'KUALI' and NM='Edit Kuali ENROUTE Document Route Status Code I'), (select KIM_TYP_ID from KRIM_PERM_TMPL_T where NMSPC_CD = 'KR-NS' and NM = 'Edit Document'), (select KIM_ATTR_DEFN_ID from KRIM_ATTR_DEFN_T where NMSPC_CD = 'KR-WKFLW' and NM = 'documentTypeName'), 'KualiDocument');

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) values ((select ATTR_DATA_ID from (select (max(cast(ATTR_DATA_ID as decimal)) + 1) as ATTR_DATA_ID from KRIM_PERM_ATTR_DATA_T where ATTR_DATA_ID is not NULL and ATTR_DATA_ID REGEXP '^[1-9][0-9]*$' and cast(ATTR_DATA_ID as decimal) < 10000) as tmptable), uuid(), 1, (select PERM_ID from KRIM_PERM_T where NMSPC_CD = 'KUALI' and NM='Edit Kuali ENROUTE Document Route Status Code I'), (select KIM_TYP_ID from KRIM_PERM_TMPL_T where NMSPC_CD = 'KR-NS' and NM = 'Edit Document'), (select KIM_ATTR_DEFN_ID from KRIM_ATTR_DEFN_T where NMSPC_CD = 'KR-WKFLW' and NM = 'routeStatusCode'), 'I');

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) values ((select ROLE_PERM_ID from (select (max(cast(ROLE_PERM_ID as decimal)) + 1) as ROLE_PERM_ID from KRIM_ROLE_PERM_T where ROLE_PERM_ID is not NULL and ROLE_PERM_ID REGEXP '^[1-9][0-9]*$' and cast(ROLE_PERM_ID as decimal) < 10000) as tmptable), uuid(), 1, (select ROLE_ID from KRIM_ROLE_T where ROLE_NM = 'Initiator' and NMSPC_CD = 'KR-WKFLW'), (select PERM_ID from KRIM_PERM_T where NMSPC_CD = 'KUALI' and NM='Edit Kuali ENROUTE Document Route Status Code I'), 'Y');

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) values ((select PERM_ID from (select (max(cast(PERM_ID as decimal)) + 1) as PERM_ID from KRIM_PERM_T where PERM_ID is not NULL and PERM_ID REGEXP '^[1-9][0-9]*$' and cast(PERM_ID as decimal) < 10000) as tmptable), uuid(), 1, (select PERM_TMPL_ID from KRIM_PERM_TMPL_T where NMSPC_CD = 'KR-NS' and NM = 'Edit Document'), 'KUALI', 'Edit Kuali ENROUTE Document Route Status Code S', 'Allows users to edit Kuali documents that are in SAVED status.','Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) values ((select ATTR_DATA_ID from (select (max(cast(ATTR_DATA_ID as decimal)) + 1) as ATTR_DATA_ID from KRIM_PERM_ATTR_DATA_T where ATTR_DATA_ID is not NULL and ATTR_DATA_ID REGEXP '^[1-9][0-9]*$' and cast(ATTR_DATA_ID as decimal) < 10000) as tmptable), uuid(), 1, (select PERM_ID from KRIM_PERM_T where NMSPC_CD = 'KUALI' and NM='Edit Kuali ENROUTE Document Route Status Code S'), (select KIM_TYP_ID from KRIM_PERM_TMPL_T where NMSPC_CD = 'KR-NS' and NM = 'Edit Document'), (select KIM_ATTR_DEFN_ID from KRIM_ATTR_DEFN_T where NMSPC_CD = 'KR-WKFLW' and NM = 'documentTypeName'), 'KualiDocument');

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) values ((select ATTR_DATA_ID from (select (max(cast(ATTR_DATA_ID as decimal)) + 1) as ATTR_DATA_ID from KRIM_PERM_ATTR_DATA_T where ATTR_DATA_ID is not NULL and ATTR_DATA_ID REGEXP '^[1-9][0-9]*$' and cast(ATTR_DATA_ID as decimal) < 10000) as tmptable), uuid(), 1, (select PERM_ID from KRIM_PERM_T where NMSPC_CD = 'KUALI' and NM='Edit Kuali ENROUTE Document Route Status Code S'), (select KIM_TYP_ID from KRIM_PERM_TMPL_T where NMSPC_CD = 'KR-NS' and NM = 'Edit Document'), (select KIM_ATTR_DEFN_ID from KRIM_ATTR_DEFN_T where NMSPC_CD = 'KR-WKFLW' and NM = 'routeStatusCode'), 'S');

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) values ((select ROLE_PERM_ID from (select (max(cast(ROLE_PERM_ID as decimal)) + 1) as ROLE_PERM_ID from KRIM_ROLE_PERM_T where ROLE_PERM_ID is not NULL and ROLE_PERM_ID REGEXP '^[1-9][0-9]*$' and cast(ROLE_PERM_ID as decimal) < 10000) as tmptable), uuid(), 1, (select ROLE_ID from KRIM_ROLE_T where ROLE_NM = 'Initiator' and NMSPC_CD = 'KR-WKFLW'), (select PERM_ID from KRIM_PERM_T where NMSPC_CD = 'KUALI' and NM='Edit Kuali ENROUTE Document Route Status Code S'), 'Y');