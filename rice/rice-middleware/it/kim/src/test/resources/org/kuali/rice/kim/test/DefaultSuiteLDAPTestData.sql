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

insert into KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD)
values ('KR-SYS','Config','KIM_TO_LDAP_FIELD_MAPPINGS','672e0c94-597e-11e1-8f11-1b8e78ae00f5',1,'CONFG','entityId=uid;principalId=uid;principalName=uid;givenName=sn;principals.principalName=uid;principals.principalId=uid;principals.active=eduPersonAffiliation;lastName=sn;firstName=givenName;employmentInformation.employeeStatus=uid.*;employmentInformation.employeeId=uid;names.lastName=sn;names.firstName=givenName;','Valid values for the organization rule attribute.','A')
/
insert into KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD)
values ('KR-SYS','Config','KIM_TO_LDAP_VALUE_MAPPINGS','76dbb10a-597e-11e1-8f11-1b8e78ae00f5',1,'CONFG','principals.active.Y=staff,faculty,employee,student;principals.active.N=alum,affiliate;','Valid values for the organization rule attribute.','A')
/
insert into KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD)
values ('KR-SYS','Config','KIM_TO_LDAP_UNMAPPED_FIELDS','877cc9a4-597e-11e1-8f11-1b8e78ae00f5',1,'CONFG',null,'Valid values for the organization rule attribute.','A')
/