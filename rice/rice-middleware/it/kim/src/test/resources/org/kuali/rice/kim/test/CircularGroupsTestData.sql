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

INSERT INTO KRIM_GRP_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,GRP_ID,GRP_NM,VER_NBR, GRP_DESC)
  VALUES ('Y','1','ADDL_GROUPS_TESTS','ea9e0001-7e6b-0001-97b6-ed716fdaf540','g101','Group A',1, 'Test Group A')
/
INSERT INTO KRIM_GRP_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,GRP_ID,GRP_MBR_ID,VER_NBR)
  VALUES ('p1','P','ea9e0001-7e6b-0002-97b6-ed716fdaf540','g101','g101p1',1)
/

-- Create role "Group B" with members "p3", "Group A"

INSERT INTO KRIM_GRP_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,GRP_ID,GRP_NM,VER_NBR, GRP_DESC)
  VALUES ('Y','1','ADDL_GROUPS_TESTS','ea9e0001-7e6b-0003-97b6-ed716fdaf540','g102','Group B',1, 'Test Group B')
/
INSERT INTO KRIM_GRP_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,GRP_ID,GRP_MBR_ID,VER_NBR)
  VALUES ('p3','P','ea9e0001-7e6b-0004-97b6-ed716fdaf540','g102','g102p3',1)
/
INSERT INTO KRIM_GRP_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,GRP_ID,GRP_MBR_ID,VER_NBR)
  VALUES ('g101','G','ea9e0001-7e6b-0005-97b6-ed716fdaf540','g102','g102ga',1)
/

-- Create role "Group C" with member "p5" and "Group B"

INSERT INTO KRIM_GRP_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,GRP_ID,GRP_NM,VER_NBR, GRP_DESC)
  VALUES ('Y','1','ADDL_GROUPS_TESTS','ea9e0001-7e6b-0006-97b6-ed716fdaf540','g103','Group C',1, 'Test Group C')
/
INSERT INTO KRIM_GRP_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,GRP_ID,GRP_MBR_ID,VER_NBR)
  VALUES ('p5','P','ea9e0001-7e6b-0007-97b6-ed716fdaf540','g103','g103p5',1)
/
INSERT INTO KRIM_GRP_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,GRP_ID,GRP_MBR_ID,VER_NBR)
  VALUES ('g102','G','ea9e0001-7e6b-0008-97b6-ed716fdaf540','g103','g103gb',1)
/

--- Assign Group C as a member of Group A to create a circular membership graph
INSERT INTO KRIM_GRP_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,GRP_ID,GRP_MBR_ID,VER_NBR)
  VALUES ('g103','G','ea9e0001-7e6b-0009-97b6-ed716fdaf540','g101','g101gc',1)
/
