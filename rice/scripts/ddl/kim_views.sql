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

CREATE OR REPLACE VIEW krim_perm_v AS
SELECT 
      krim_perm_tmpl_t.nmspc_cd AS template_namespace_code
	, KRIM_PERM_TMPL_T.NM AS PERM_TEMPLATE_NAME
    , krim_perm_t.nmspc_cd AS perm_namespace_code
    , KRIM_PERM_T.NM      AS PERM_NAME
	, KRIM_TYP_T.NM   AS PERM_TYPE_NAME 
	, KRIM_TYP_T.SRVC_NM   AS PERM_TYPE_SERVICE_NAME
FROM KRIM_PERM_T KRIM_PERM_T 
		INNER JOIN KRIM_PERM_TMPL_T KRIM_PERM_TMPL_T 
		ON KRIM_PERM_T.PERM_TMPL_ID = KRIM_PERM_TMPL_T.PERM_TMPL_ID 
			INNER JOIN KRIM_TYP_T KRIM_TYP_T 
			ON KRIM_PERM_TMPL_T.KIM_TYP_ID = KRIM_TYP_T.KIM_TYP_ID
/
CREATE OR REPLACE VIEW krim_perm_attr_v AS
SELECT 
      krim_perm_tmpl_t.nmspc_cd AS template_namespace_code
	, KRIM_PERM_TMPL_T.NM AS TEMPLATE_NAME
    , krim_perm_t.nmspc_cd AS perm_namespace_code
    , KRIM_PERM_T.NM      AS PERM_NAME
	, KRIM_ATTR_DEFN_T.NM AS attribute_name 
	, KRIM_PERM_ATTR_DATA_T.ATTR_VAL AS attribute_value
FROM KRIM_PERM_T KRIM_PERM_T 
		INNER JOIN KRIM_PERM_ATTR_DATA_T KRIM_PERM_ATTR_DATA_T 
		ON KRIM_PERM_T.PERM_ID = KRIM_PERM_ATTR_DATA_T.TARGET_PRIMARY_KEY 
			INNER JOIN KRIM_ATTR_DEFN_T KRIM_ATTR_DEFN_T 
			ON KRIM_PERM_ATTR_DATA_T.KIM_ATTR_DEFN_ID = KRIM_ATTR_DEFN_T.KIM_ATTR_DEFN_ID 
				INNER JOIN KRIM_PERM_TMPL_T KRIM_PERM_TMPL_T 
				ON KRIM_PERM_T.PERM_TMPL_ID = KRIM_PERM_TMPL_T.PERM_TMPL_ID 
					INNER JOIN KRIM_TYP_T KRIM_TYP_T 
					ON KRIM_PERM_TMPL_T.KIM_TYP_ID = KRIM_TYP_T.KIM_TYP_ID
/
CREATE OR REPLACE VIEW krim_role_perm_v AS
SELECT KRIM_ROLE_T.NMSPC_CD             AS role_namespace
	, KRIM_ROLE_T.ROLE_NM              AS role_name
	, KRIM_TYP_T.NM              AS role_type_name
	, KRIM_PERM_TMPL_T.NM            AS perm_template_name
	, KRIM_PERM_T.NM                 AS perm_name
	, KRIM_ATTR_DEFN_T.NM       AS attribute_name
	, KRIM_PERM_ATTR_DATA_T.ATTR_VAL AS attribute_value
FROM KRIM_PERM_T KRIM_PERM_T
		INNER JOIN KRIM_PERM_TMPL_T KRIM_PERM_TMPL_T
		ON KRIM_PERM_T.PERM_TMPL_ID = KRIM_PERM_TMPL_T.PERM_TMPL_ID
			LEFT OUTER JOIN KRIM_PERM_ATTR_DATA_T KRIM_PERM_ATTR_DATA_T
			ON KRIM_PERM_T.PERM_ID = KRIM_PERM_ATTR_DATA_T.TARGET_PRIMARY_KEY
				INNER JOIN KRIM_ATTR_DEFN_T KRIM_ATTR_DEFN_T
				ON KRIM_PERM_ATTR_DATA_T.KIM_ATTR_DEFN_ID = KRIM_ATTR_DEFN_T.KIM_ATTR_DEFN_ID
					INNER JOIN KRIM_ROLE_PERM_T KRIM_ROLE_PERM_T
					ON KRIM_ROLE_PERM_T.PERM_ID = KRIM_PERM_T.PERM_ID
						RIGHT OUTER JOIN KRIM_ROLE_T KRIM_ROLE_T
						ON KRIM_ROLE_PERM_T.ROLE_ID = KRIM_ROLE_T.ROLE_ID
							LEFT OUTER JOIN KRIM_TYP_T KRIM_TYP_T
							ON KRIM_ROLE_T.KIM_TYP_ID = KRIM_TYP_T.KIM_TYP_ID
						ORDER BY KRIM_ROLE_T.NMSPC_CD
							, KRIM_ROLE_T.ROLE_NM
							, KRIM_TYP_T.NM
							, KRIM_PERM_T.PERM_ID
							, KRIM_PERM_TMPL_T.NM
							, KRIM_PERM_T.NM
/


CREATE OR REPLACE VIEW KRIM_ROLE_PRNCPL_V
AS
SELECT KRIM_ROLE_T.NMSPC_CD                 AS namespace_code
	, KRIM_ROLE_T.ROLE_NM                  AS role_name
	, KRIM_ROLE_T.ROLE_ID                  AS role_id
	, KRIM_PRNCPL_T.PRNCPL_NM           AS principal_name
    , KRIM_PRNCPL_T.PRNCPL_ID           AS principal_id
	, KRIM_ENTITY_NM_T.FIRST_NM AS first_name
	, KRIM_ENTITY_NM_T.LAST_NM  AS last_name
	, KRIM_ATTR_DEFN_T.NM           AS qualifier_name
	, KRIM_ROLE_MBR_ATTR_DATA_T.ATTR_VAL AS qualifier_value
FROM KRIM_ROLE_T KRIM_ROLE_T 
		LEFT OUTER JOIN KRIM_ROLE_MBR_T KRIM_ROLE_MBR_T 
		ON KRIM_ROLE_T.ROLE_ID = KRIM_ROLE_MBR_T.ROLE_ID 
			LEFT OUTER JOIN KRIM_ROLE_MBR_ATTR_DATA_T KRIM_ROLE_MBR_ATTR_DATA_T 
			ON KRIM_ROLE_MBR_T.ROLE_MBR_ID = KRIM_ROLE_MBR_ATTR_DATA_T.
			TARGET_PRIMARY_KEY 
				LEFT OUTER JOIN KRIM_ATTR_DEFN_T KRIM_ATTR_DEFN_T 
				ON KRIM_ROLE_MBR_ATTR_DATA_T.KIM_ATTR_DEFN_ID = KRIM_ATTR_DEFN_T.
				KIM_ATTR_DEFN_ID 
					LEFT OUTER JOIN KRIM_PRNCPL_T KRIM_PRNCPL_T 
					ON KRIM_ROLE_MBR_T.MBR_ID = KRIM_PRNCPL_T.PRNCPL_ID 
                       AND mbr_typ_cd = 'P'
						LEFT OUTER JOIN KRIM_ENTITY_NM_T KRIM_ENTITY_NM_T 
						ON KRIM_PRNCPL_T.ENTITY_ID = KRIM_ENTITY_NM_T.ENTITY_ID 
WHERE (KRIM_ENTITY_NM_T.DFLT_IND = 'Y')
ORDER BY namespace_code, role_name, principal_name, qualifier_name
/

CREATE OR REPLACE VIEW krim_role_grp_v AS
SELECT r.NMSPC_CD                 AS namespace_code
	, r.ROLE_NM                  AS role_name
    , g.NMSPC_CD                AS group_namespace
	, g.GRP_NM                  AS group_name
	, a.NM           AS qualifier_name
	, d.ATTR_VAL AS qualifier_value 
FROM KRIM_ROLE_MBR_T rm
     LEFT JOIN KRIM_ROLE_T r
		ON r.ROLE_ID = rm.ROLE_ID 
     LEFT JOIN KRIM_GRP_T g
        ON g.GRP_ID = rm.MBR_ID
     LEFT OUTER JOIN KRIM_ROLE_MBR_ATTR_DATA_T d
        ON d.TARGET_PRIMARY_KEY = rm.ROLE_MBR_ID
    LEFT OUTER JOIN KRIM_ATTR_DEFN_T a
        ON a.KIM_ATTR_DEFN_ID = d.KIM_ATTR_DEFN_ID 
WHERE rm.MBR_TYP_CD = 'G'
ORDER BY namespace_code, role_name, group_namespace, group_name, qualifier_name
/

CREATE OR REPLACE VIEW krim_role_role_v AS
SELECT r.NMSPC_CD                 AS namespace_code
	, r.ROLE_NM                  AS role_name
    , g.NMSPC_CD                AS member_role_namespace
	, g.role_NM                  AS member_role_name
	, a.NM           AS qualifier_name
	, d.ATTR_VAL AS qualifier_value 
FROM KRIM_ROLE_MBR_T rm
     LEFT JOIN KRIM_ROLE_T r
		ON r.ROLE_ID = rm.ROLE_ID 
     LEFT JOIN KRIM_role_T g
        ON g.role_ID = rm.MBR_ID
     LEFT OUTER JOIN KRIM_ROLE_MBR_ATTR_DATA_T d
        ON d.TARGET_PRIMARY_KEY = rm.ROLE_MBR_ID
    LEFT OUTER JOIN KRIM_ATTR_DEFN_T a
        ON a.KIM_ATTR_DEFN_ID = d.KIM_ATTR_DEFN_ID 
WHERE rm.MBR_TYP_CD = 'R'
ORDER BY namespace_code, role_name, member_role_namespace, member_role_name, qualifier_name
/

CREATE OR REPLACE VIEW krim_prncpl_v AS
SELECT 
	  KRIM_PRNCPL_T.PRNCPL_ID
    , KRIM_PRNCPL_T.PRNCPL_NM AS principal_name
	, KRIM_ENTITY_NM_T.FIRST_NM
	, KRIM_ENTITY_NM_T.LAST_NM
	, KRIM_ENTITY_AFLTN_T.AFLTN_TYP_CD
	, KRIM_ENTITY_AFLTN_T.CAMPUS_CD
	, KRIM_ENTITY_EMP_INFO_T.EMP_STAT_CD
	, KRIM_ENTITY_EMP_INFO_T.EMP_TYP_CD 
FROM KRIM_ENTITY_AFLTN_T KRIM_ENTITY_AFLTN_T 
		RIGHT OUTER JOIN KRIM_PRNCPL_T KRIM_PRNCPL_T 
		ON KRIM_ENTITY_AFLTN_T.ENTITY_ID = KRIM_PRNCPL_T.ENTITY_ID 
			RIGHT OUTER JOIN KRIM_ENTITY_EMP_INFO_T KRIM_ENTITY_EMP_INFO_T 
			ON KRIM_ENTITY_EMP_INFO_T.ENTITY_ID = KRIM_PRNCPL_T.ENTITY_ID 
				LEFT OUTER JOIN KRIM_ENTITY_NM_T KRIM_ENTITY_NM_T 
				ON KRIM_PRNCPL_T.ENTITY_ID = KRIM_ENTITY_NM_T.ENTITY_ID 
WHERE (KRIM_ENTITY_NM_T.DFLT_IND = 'Y') 
/

CREATE OR REPLACE VIEW KRIM_ROLE_V AS
SELECT r.ROLE_ID, r.NMSPC_CD AS namespace_code, r.ROLE_NM AS role_name, t.nm AS type_name, t.SRVC_NM AS service_name, t.KIM_TYP_ID AS role_type_id
    FROM KRIM_ROLE_T r, KRIM_TYP_T t
    WHERE t.KIM_TYP_ID = r.KIM_TYP_ID
      AND r.ACTV_IND = 'Y'
    ORDER BY namespace_code, role_name
/


CREATE OR REPLACE VIEW KRIM_ROLE_RSP_V AS
SELECT KRIM_ROLE_T.NMSPC_CD             AS role_namespace
	, KRIM_ROLE_T.ROLE_NM              AS role_name
	, KRIM_rsp_TMPL_T.NM            AS rsp_template_name
	, KRIM_rsp_T.NM                 AS rsp_name
	, KRIM_ATTR_DEFN_T.NM       AS attribute_name
	, KRIM_rsp_ATTR_DATA_T.ATTR_VAL AS attribute_value
FROM KRIM_rsp_T KRIM_rsp_T
    INNER JOIN KRIM_rsp_TMPL_T KRIM_rsp_TMPL_T
        ON KRIM_rsp_T.rsp_TMPL_ID = KRIM_rsp_TMPL_T.rsp_TMPL_ID
    LEFT OUTER JOIN KRIM_rsp_ATTR_DATA_T KRIM_rsp_ATTR_DATA_T
        ON KRIM_rsp_T.rsp_ID = KRIM_rsp_ATTR_DATA_T.TARGET_PRIMARY_KEY
    INNER JOIN KRIM_ATTR_DEFN_T KRIM_ATTR_DEFN_T
        ON KRIM_rsp_ATTR_DATA_T.KIM_ATTR_DEFN_ID = KRIM_ATTR_DEFN_T.KIM_ATTR_DEFN_ID
    INNER JOIN KRIM_ROLE_rsp_T KRIM_ROLE_rsp_T
        ON KRIM_ROLE_rsp_T.rsp_ID = KRIM_rsp_T.rsp_ID
    RIGHT OUTER JOIN KRIM_ROLE_T KRIM_ROLE_T
        ON KRIM_ROLE_rsp_T.ROLE_ID = KRIM_ROLE_T.ROLE_ID
ORDER BY role_namespace, role_name, rsp_template_name, rsp_name, attribute_name
/

CREATE OR REPLACE VIEW krim_rsp_attr_v AS
SELECT 
      krim_typ_t.NM      AS responsibility_type_name
	, KRIM_rsp_TMPL_T.NM AS rsp_TEMPLATE_NAME
    , KRIM_rsp_T.NM      AS rsp_NAME
    , krim_rsp_t.RSP_ID  AS rsp_id
	, KRIM_ATTR_DEFN_T.NM AS attribute_name 
	, KRIM_rsp_ATTR_DATA_T.ATTR_VAL AS attribute_value
FROM KRIM_rsp_T KRIM_rsp_T 
    INNER JOIN KRIM_rsp_ATTR_DATA_T KRIM_rsp_ATTR_DATA_T 
        ON KRIM_rsp_T.rsp_ID = KRIM_rsp_ATTR_DATA_T.TARGET_PRIMARY_KEY 
    INNER JOIN KRIM_ATTR_DEFN_T KRIM_ATTR_DEFN_T 
        ON KRIM_rsp_ATTR_DATA_T.KIM_ATTR_DEFN_ID = KRIM_ATTR_DEFN_T.KIM_ATTR_DEFN_ID 
    INNER JOIN KRIM_rsp_TMPL_T KRIM_rsp_TMPL_T 
        ON KRIM_rsp_T.rsp_TMPL_ID = KRIM_rsp_TMPL_T.rsp_TMPL_ID 
    INNER JOIN KRIM_TYP_T KRIM_TYP_T 
        ON KRIM_rsp_TMPL_T.KIM_TYP_ID = KRIM_TYP_T.KIM_TYP_ID
ORDER BY rsp_TEMPLATE_NAME, rsp_NAME, attribute_name
/
