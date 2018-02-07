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

-- CONDITIONALLY ADD THESE AGAIN, as the boostrap server cleanup script may have nuked them

INSERT INTO krim_entity_t (entity_id, obj_id, ver_nbr, actv_ind, last_updt_dt)
  SELECT 'KR1000', uuid(), 1, 'Y', now()
    FROM dual
    WHERE NOT EXISTS (SELECT * FROM krim_entity_t WHERE entity_id = 'KR1000')
;

INSERT INTO krim_entity_ent_typ_t (ent_typ_cd, entity_id, obj_id, ver_nbr, actv_ind, last_updt_dt)
  SELECT 'PERSON', 'KR1000', uuid(), 1, 'Y', now()
    FROM dual
    WHERE NOT EXISTS (SELECT * FROM krim_entity_ent_typ_t WHERE ent_typ_cd = 'PERSON' AND entity_id = 'KR1000')
;

INSERT INTO krim_prncpl_t (prncpl_id, obj_id, ver_nbr, prncpl_nm, entity_id, prncpl_pswd, actv_ind, last_updt_dt)
  SELECT 'guest', uuid(), 1, 'guest', 'KR1000', '', 'Y', now()
    FROM dual
    WHERE NOT EXISTS (SELECT * FROM krim_prncpl_t WHERE prncpl_id = 'guest' AND entity_id = 'KR1000')
;

INSERT INTO krim_role_t (role_id, obj_id, ver_nbr, role_nm, nmspc_cd, desc_txt, kim_typ_id, actv_ind, last_updt_dt)
  SELECT 'KR1000', uuid(), 1, 'GuestRole', 'KUALI', 'This role is used for no login guest users.', '1', 'Y', now()
    FROM dual
    WHERE NOT EXISTS (SELECT * FROM krim_role_t WHERE role_id = 'KR1000')
;

INSERT INTO krim_role_mbr_t (role_mbr_id, ver_nbr, obj_id, role_id, mbr_id, mbr_typ_cd, actv_frm_dt, actv_to_dt, last_updt_dt)
  SELECT 'KR1000', 1, uuid(), 'KR1000', 'guest', 'P', null, null, now()
    FROM dual
    WHERE NOT EXISTS (SELECT * FROM krim_role_mbr_t WHERE role_mbr_id = 'KR1000' AND role_id = 'KR1000')
;
