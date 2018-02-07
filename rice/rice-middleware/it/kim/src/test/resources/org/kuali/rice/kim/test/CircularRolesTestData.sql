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

INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR, DESC_TXT)
  VALUES ('Y','1','ADDL_ROLES_TESTS','ea9e0001-7e6b-102c-97b6-ed716fdaf540','r101','Role A',1, 'Test Role A')
/
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR)
  VALUES ('p1','P','7e2c0002-7e6c-102c-97b6-ed716fdaf540','r101','r101p1',1)
/

-- Create role "Role B" with members "p3", "g1", "Role A"

INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR, DESC_TXT)
  VALUES ('Y','1','ADDL_ROLES_TESTS','6afd0003-7e71-102c-97b6-ed716fdaf540','r102','Role B',1, 'Test Role B')
/
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR)
  VALUES ('p3','P','b6a00004-7e6c-102c-97b6-ed716fdaf540','r102','r102p3',1)
/
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR)
  VALUES ('g1','G','d9220005-7e6c-102c-97b6-ed716fdaf540','r102','r102g1',1)
/
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR)
  VALUES ('r101','R','d9ab0006-7e6c-102c-97b6-ed716fdaf540','r102','r102r101',1)
/

-- Create role "Role C" with member "p5" and "Role B"

INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR, DESC_TXT)
  VALUES ('Y','1','ADDL_ROLES_TESTS','6afd0007-7e71-102c-97b6-ed716fdaf541','r103','Role C',1, 'Test Role C')
/
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR)
  VALUES ('p7','P','b6a00008-7e6c-102c-97b6-ed716fdaf541','r103','r103p5',1)
/
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR)
  VALUES ('r102','R','d9ab0009-7e6c-102c-97b6-ed716fdaf540','r103','r103r102',1)
/

--- Assign Role C as a member of Role A to create a circular membership graph
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR)
  VALUES ('r103','R','d9ab0010-7e6c-102c-97b6-ed716fdaf540','r101','r101r103',1)
/
