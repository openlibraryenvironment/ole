--
-- Copyright 2005-2013 The Kuali Foundation
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

INSERT INTO KRIM_ROLE_MBR_T(ROLE_MBR_ID, VER_NBR, OBJ_ID, ROLE_ID, MBR_ID, MBR_TYP_CD)
VALUES('2000', 1, '62311426-7dfb-102c-97b6-ed716fdaf540', '63', '1', 'G')
;
insert into krim_perm_t (perm_id, obj_id, ver_nbr, perm_tmpl_id, nmspc_cd, nm, desc_txt, actv_ind)
values ('1652','4348B3EDA0204A9A82D11801A0B5BF89',1,'4', 'KR-NS', 'Blanket Approve Document', 'Allow blanket approvals of documents', 'Y')
;
insert into krim_perm_attr_data_t (attr_data_id, obj_id, ver_nbr, perm_id, kim_typ_id, kim_attr_defn_id, attr_val)
values ('1000', '9A6B69E11DA1477FB0FD899A3C746A17', 1, '1652', '3', '13', 'RiceDocument')
;
insert into krim_role_perm_t (role_perm_id, obj_id, ver_nbr, role_id, perm_id, actv_ind)
values ('2112', '7FE9BCE73E2748FEB56DB358F0FFA84F', 1, '63', '1652', 'Y')
;

/* borrowed from db/impex/master/target/classes/sql/mysql/KRNS_NTE_TYP_T.sql (which exists when a maven impex has run)
to ensure that org.kuali.rice.krad.service.NoteServiceTest.testNoteSave_LargePersonId passes
 */
INSERT INTO KRNS_NTE_TYP_T (ACTV_IND,NTE_TYP_CD,OBJ_ID,TYP_DESC_TXT,VER_NBR)
  VALUES ('Y','BO','53680C68F5A9AD9BE0404F8189D80A6C','DOCUMENT BUSINESS OBJECT',1)
;

/* TODO: shoud not have sample app test data here */
INSERT INTO TRV_ACCT_TYPE (ACCT_TYPE,ACCT_TYPE_NAME)
  VALUES ('EAT','Expense Account Type')
;