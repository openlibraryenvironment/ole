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

--
--     KULRICE-8349 - guest user access
--

insert into krim_entity_t (ENTITY_ID, OBJ_ID, VER_NBR, ACTV_IND, LAST_UPDT_DT)
values ('KR1000', sys_guid(), 1, 'Y', sysdate)
/

insert into krim_entity_ent_typ_t (ENT_TYP_CD, ENTITY_ID, OBJ_ID, VER_NBR, ACTV_IND, LAST_UPDT_DT)
values ('PERSON', 'KR1000', sys_guid(), 1, 'Y', sysdate)
/

insert into krim_prncpl_t (PRNCPL_ID, OBJ_ID, VER_NBR, PRNCPL_NM, ENTITY_ID, PRNCPL_PSWD, ACTV_IND, LAST_UPDT_DT)
values ('guest', sys_guid(), 1, 'guest', 'KR1000', '', 'Y', sysdate)
/

insert into krim_role_t (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND, LAST_UPDT_DT)
values ('KR1000', sys_guid(), 1, 'GuestRole', 'KUALI', 'This role is used for no login guest users.', '1', 'Y', sysdate)
/

insert into krim_role_mbr_t (ROLE_MBR_ID, VER_NBR, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD, ACTV_FRM_DT, ACTV_TO_DT, LAST_UPDT_DT)
values ('KR1000', 1, sys_guid(), 'KR1000', 'guest', 'P', null, null, sysdate)
/
