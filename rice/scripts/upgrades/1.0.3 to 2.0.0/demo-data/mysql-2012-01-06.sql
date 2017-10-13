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
-- Adds some permissions and a role for testing KIM authorization in the sample app
--

INSERT INTO KRIM_ROLE_T VALUES ('KRSAP10003', uuid(), 1, 'Sample App Admin', 'KR-SAP', 'Test role for the sample app', '1', 'Y', now());

INSERT INTO KRIM_ROLE_MBR_T VALUES ('KRSAP10003', 1, uuid(), 'KRSAP10003', 'dev1', 'P', null, null, now());

INSERT INTO KRIM_ROLE_T VALUES ('KRSAP10004', uuid(), 1, 'Sample App Users', 'KR-SAP', 'Test role for the sample app', '1', 'Y', now());

INSERT INTO KRIM_ROLE_MBR_T VALUES ('KRSAP10004', 1, uuid(), 'KRSAP10004', 'admin', 'P', null, null, now());

INSERT INTO KRIM_ROLE_MBR_T VALUES ('KRSAP10005', 1, uuid(), 'KRSAP10004', 'dev1', 'P', null, null, now());

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10003', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "View Group"), 'KR-SAP', 'View Kitchen Sink Group', 'Allows users to view the group in kitchen sink page 9.', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP882', uuid(), 1, 'KRSAP10003', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP883', uuid(), 1, 'KRSAP10003', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '51', 'UifCompView-SecureGroupView');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1003', uuid(), 1, 'KRSAP10003', 'KRSAP10003', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10004', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "View Group"), 'KR-SAP', 'View Kitchen Sink Page', 'Allows users to view page 9 in the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP884', uuid(), 1, 'KRSAP10004', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP885', uuid(), 1, 'KRSAP10004', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '51', 'UifCompView-Page9');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1004', uuid(), 1, 'KRSAP10004', 'KRSAP10004', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10005', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "Edit Group"), 'KR-SAP', 'Edit Kitchen Sink Group', 'Allows users to edit the group in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP886', uuid(), 1, 'KRSAP10005', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP887', uuid(), 1, 'KRSAP10005', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '51', 'UifCompView-SecureGroupEdit');


INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1005', uuid(), 1, 'KRSAP10003', 'KRSAP10005', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10006', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "View Field"), 'KR-SAP', 'View Kitchen Sink Field', 'Allows users to view the field in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP888', uuid(), 1, 'KRSAP10006', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Field'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP889', uuid(), 1, 'KRSAP10006', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Field'), '6', 'field6');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1006', uuid(), 1, 'KRSAP10003', 'KRSAP10006', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10007', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "Edit Field"), 'KR-SAP', 'Edit Kitchen Sink Field', 'Allows users to edit the field in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP890', uuid(), 1, 'KRSAP10007', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Field'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP891', uuid(), 1, 'KRSAP10007', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Field'), '6', 'field7');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1007', uuid(), 1, 'KRSAP10003', 'KRSAP10007', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10008', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "View Field"), 'KR-SAP', 'View Kitchen Sink Field Group', 'Allows users to view the field group in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP892', uuid(), 1, 'KRSAP10008', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Field'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP893', uuid(), 1, 'KRSAP10008', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Field'), '50', 'UifCompView-SecureFieldGroup1');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1008', uuid(), 1, 'KRSAP10003', 'KRSAP10008', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10009', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "Edit Field"), 'KR-SAP', 'Edit Kitchen Sink Field Group', 'Allows users to edit the field group in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP894', uuid(), 1, 'KRSAP10009', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Field'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP895', uuid(), 1, 'KRSAP10009', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Field'), '50', 'UifCompView-SecureFieldGroup2');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1009', uuid(), 1, 'KRSAP10003', 'KRSAP10009', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10010', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "Perform Action"), 'KR-SAP', 'Perform Kitchen Sink Action', 'Allows users to perform the save action in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP896', uuid(), 1, 'KRSAP10010', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Action'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP897', uuid(), 1, 'KRSAP10010', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Action'), '48', 'save');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1010', uuid(), 1, 'KRSAP10003', 'KRSAP10010', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10011', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "View Widget"), 'KR-SAP', 'View Kitchen Sink Widget', 'Allows users to view the quickfinder widget in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP898', uuid(), 1, 'KRSAP10011', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Widget'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP899', uuid(), 1, 'KRSAP10011', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Widget'), '52', 'UifCompView-SecureWidget');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1011', uuid(), 1, 'KRSAP10003', 'KRSAP10011', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10012', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "View Line"), 'KR-SAP', 'View Kitchen Sink Line', 'Allows users to view the collection line in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP900', uuid(), 1, 'KRSAP10012', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP901', uuid(), 1, 'KRSAP10012', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '49', 'list1');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1012', uuid(), 1, 'KRSAP10003', 'KRSAP10012', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10013', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "Edit Line"), 'KR-SAP', 'Edit Kitchen Sink Line', 'Allows users to edit the collection line in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP902', uuid(), 1, 'KRSAP10013', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP903', uuid(), 1, 'KRSAP10013', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Group'), '49', 'list2');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1013', uuid(), 1, 'KRSAP10003', 'KRSAP10013', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10014', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "View Line Field"), 'KR-SAP', 'View Kitchen Sink Line Field', 'Allows users to view the collection line field in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP904', uuid(), 1, 'KRSAP10014', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Field'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP905', uuid(), 1, 'KRSAP10014', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Field'), '49', 'list3');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP906', uuid(), 1, 'KRSAP10014', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Field'), '6', 'field2');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1014', uuid(), 1, 'KRSAP10003', 'KRSAP10014', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10015', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "Edit Line Field"), 'KR-SAP', 'Edit Kitchen Sink Line Field', 'Allows users to edit the collection line field in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP907', uuid(), 1, 'KRSAP10015', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Field'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP908', uuid(), 1, 'KRSAP10015', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Field'), '49', 'list3');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP909', uuid(), 1, 'KRSAP10015', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Field'), '6', 'field3');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1015', uuid(), 1, 'KRSAP10003', 'KRSAP10015', 'Y');

INSERT INTO KRIM_PERM_T VALUES ('KRSAP10016', uuid(), 1, (select perm_tmpl_id from KRIM_PERM_TMPL_T where nmspc_cd = "KR-KRAD" and nm = "Perform Line Action"), 'KR-SAP', 'Perform Kitchen Sink Line Action', 'Allows users to perform the delete line action in page 9 of the kitchen sink', 'Y');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP910', uuid(), 1, 'KRSAP10016', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Action'), '47', 'UifCompView*');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP911', uuid(), 1, 'KRSAP10016', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Action'), '49', 'list4');

INSERT INTO KRIM_PERM_ATTR_DATA_T VALUES ('KRSAP912', uuid(), 1, 'KRSAP10016', (select kim_typ_id from krim_typ_t where NMSPC_CD='KR-KRAD' and NM='View Line Action'), '48', 'delete');

INSERT INTO KRIM_ROLE_PERM_T VALUES ('KRSAP1016', uuid(), 1, 'KRSAP10003', 'KRSAP10016', 'Y');


