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

-- ############################################################################
-- # This file will clean up and remove data from the database to prepare the #
-- # "bootstrap" dataset which essentially clears out any test or "fake" data #
-- # that an implementer would not need in their database.					  #
-- #																		  #
-- # This includes fake principals and entities in KIM as well as document    #
-- # types that exist for testing and/or demonstration purposes.			  #
-- ############################################################################
--
-- # IMPORTANT! ###############################################################
-- # The demo-server-dataset-cleanup.sql should be run against the database   # 
-- # prior to this script!													  #
-- ############################################################################

-- Disable constraints for the duration of this script
DECLARE 
   CURSOR constraint_cursor IS 
      SELECT table_name, constraint_name 
         FROM user_constraints 
         WHERE constraint_type = 'R'
           AND status = 'ENABLED';
BEGIN 
   FOR r IN constraint_cursor LOOP
      execute immediate 'ALTER TABLE '||r.table_name||' DISABLE CONSTRAINT '||r.constraint_name; 
   END LOOP; 
END;
/

-- ##############
-- # KEW Tables #
-- ##############

-- Document Types

delete from krew_doc_typ_t where doc_typ_nm='TravelAccountMaintenanceDocument'
/
delete from krew_doc_typ_t where doc_typ_nm='FiscalOfficerMaintenanceDocument'
/
delete from krew_doc_typ_t where doc_typ_nm='eDoc.Example1.ParentDoctype'
/
delete from krew_doc_typ_t where doc_typ_nm='SampleThinClientDocument'
/
delete from krew_doc_typ_t where doc_typ_nm='eDoc.Example1Doctype'
/
delete from krew_doc_typ_t where doc_typ_nm='TravelRequest'
/
delete from krew_doc_typ_t where doc_typ_nm like 'Recipe%'
/
delete from krew_doc_typ_t where doc_typ_nm='OfficeOfAffirmativeAction'
/
delete from krew_doc_typ_t where doc_typ_nm='DFAC_OAA.WorkgroupTypeDoctype'
/
delete from krew_doc_typ_t where doc_typ_nm='OfferRequest'
/
delete from krew_doc_typ_t where doc_typ_nm='SearchStatus'
/
delete from krew_doc_typ_t where doc_typ_nm='VacancyNotice'
/
delete from krew_doc_typ_t where doc_typ_nm='WaiverRequest'
/
delete from krew_doc_typ_t where doc_typ_nm='LoadTest'
/
delete from krew_doc_typ_t where doc_typ_nm='InterviewRequest'
/
delete from krew_doc_typ_attr_t where DOC_TYP_ID not in (select doc_typ_id from KREW_DOC_TYP_T)
/
delete from krew_doc_typ_plcy_reln_t where DOC_TYP_ID not in (select doc_typ_id from KREW_DOC_TYP_T)
/
delete from krew_doc_typ_proc_t where DOC_TYP_ID not in (select doc_typ_id from KREW_DOC_TYP_T)
/
delete from krew_rte_node_t where DOC_TYP_ID not in (select doc_typ_id from KREW_DOC_TYP_T)
/
delete from krew_rte_node_lnk_t where from_rte_node_id not in (select rte_node_id from krew_rte_node_t)
/
delete from krew_rte_node_cfg_parm_t where rte_node_id not in (select rte_node_id from krew_rte_node_t)
/
delete from krew_rte_brch_proto_t where RTE_BRCH_PROTO_ID not in (select rte_brch_proto_id from krew_rte_node_t)
/


-- Rule Attributes

delete from krew_rule_attr_t where nm='DestinationAttribute'
/
delete from krew_rule_attr_t where nm='EmployeeAttribute'
/
delete from krew_rule_attr_t where nm='AccountAttribute'
/
delete from krew_rule_attr_t where nm='TravelAccountDocumentAccountNumberAttribute'
/
delete from krew_rule_attr_t where nm='EDL.Campus.Example'
/
delete from krew_rule_attr_t where nm='FiscalOfficer'
/
delete from krew_rule_attr_t where nm='LoadTestActionListAttribute'
/
delete from krew_rule_attr_t where nm='XMLSearchableAttribute_CaseInsensitive'
/
delete from krew_rule_attr_t where nm='XMLSearchableAttributeStdLong'
/
delete from krew_rule_attr_t where nm='XMLSearchableAttributeStdFloat'
/
delete from krew_rule_attr_t where nm='XMLSearchableAttributeStdCurrency'
/
delete from krew_rule_attr_t where nm='XMLSearchableAttributeStdDateTime'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='DFAC.CampusAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='DFAC.CampusSearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='DFAC.SchoolAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='DFAC.SchoolSearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='DFAC.ViceChancellorforAcademicAffairsandDeanOfFacultiesAdHocNetworkIdRoleAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='DFAC.AffirmativeActionOfficerAdHocNetworkIdRoleAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='EDLDepartmentSearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='InitiatorAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='EDLOAASearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='EDLSchoolAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='DFAC_OAASearchStatusInformalOfferSearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='EDLSchoolSearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='EDLExpectedStartDateSearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='EDLSalaryGradeSearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='EDLTotalApplicantsSearchAttribute'
/
DELETE FROM KREW_RULE_ATTR_T WHERE NM ='EDLTotalAMFsReceievedSearchAttribute'
/

-- Rule Templates

delete from krew_rule_tmpl_t where nm='TravelRequest-DestinationRouting'
/
delete from krew_rule_tmpl_t where nm='TravelRequest-TravelerRouting'
/
delete from krew_rule_tmpl_t where nm='TravelRequest-SupervisorRouting'
/
delete from krew_rule_tmpl_t where nm='TravelRequest-AccountRouting'
/
delete from krew_rule_tmpl_t where nm='eDoc.Example1.Node1'
/
delete from krew_rule_tmpl_t where nm='Ack1Template'
/
delete from krew_rule_tmpl_t where nm='Ack2Template'
/
delete from krew_rule_tmpl_t where nm='WorkflowDocument2Template'
/
delete from krew_rule_tmpl_t where nm='WorkflowDocument3Template'
/
delete from krew_rule_tmpl_t where nm='DFAC.AffirmativeActionOfficer.AdHoc'
/
delete from krew_rule_tmpl_t where nm='DFAC.ViceChancellorforAcademicAffairsandDeanOfFaculties.AdHoc'
/
delete from krew_rule_tmpl_t where nm='InitiatorRoleAcknowledgement'
/
delete from krew_rule_tmpl_t where nm='AffirmativeActionOfficer'
/
delete from krew_rule_tmpl_t where nm='OfficeOfAffirmativeAction-SchoolAcknowledgementRouting'
/
delete from krew_rule_tmpl_t where nm='ViceChancellorforAcademicAffairsandDeanOfFaculties'
/
delete from krew_rule_tmpl_t where nm='OfficeOfAffirmativeAction-SchoolRouting'
/

-- Rules

delete from krew_rule_t where rule_tmpl_id is not null and rule_tmpl_id not in (select rule_tmpl_id from krew_rule_tmpl_t)
/
delete from krew_rule_t where doc_typ_nm is not null and doc_typ_nm not in (select doc_typ_nm from krew_doc_typ_t)
/
delete from KREW_RULE_TMPL_ATTR_T where rule_tmpl_id is not null and rule_tmpl_id not in (select rule_tmpl_id from krew_rule_tmpl_t)
/

-- EDL

delete from krew_edl_assctn_t
/
delete from krew_edl_def_t
/
delete from krcr_style_t where nm='eDoc.Example1.Style'
/
delete from krcr_style_t where nm='InterviewRequestStyle'
/
delete from krcr_style_t where nm='OfferRequestStyle'
/
delete from krcr_style_t where nm='SearchStatusStyle'
/
delete from krcr_style_t where nm='VacancyNoticeStyle'
/
delete from krcr_style_t where nm='WaiverRequest_xsl'
/

-- User Options

delete from krew_usr_optn_t
/

-- ##############
-- # KSB Tables #
-- ##############

-- no KSB data needs to be dealt with for the bootstrap 
-- dataset (it's all handled by the client and demo cleanup files)

-- ##############
-- # KNS Tables #
-- ##############

delete from KRLC_CMP_T
/
delete from KRCR_NMSPC_T where NMSPC_CD='KR-SAP'
/
delete from KRCR_PARM_T where NMSPC_CD='KR-SAP'
/

-- ##############
-- # KEN Tables #
-- ##############

-- leave only the 'KEW' Channel which is used for action list notification

delete from KREN_CHNL_T where NM = 'Kuali Rice Channel'
/
delete from KREN_CHNL_T where NM = 'Library Events Channel'
/
delete from KREN_CHNL_T where NM = 'Overdue Library Books'
/
delete from KREN_CHNL_T where NM = 'Concerts Coming to Campus'
/
delete from KREN_CHNL_T where NM = 'University Alerts'
/

-- delete all channel subscriptions

delete from kren_chnl_subscrp_t
/

-- delete all producers
delete from kren_prodcr_t
/
delete from kren_chnl_prodcr_t
/

-- delete recipient data

delete from kren_recip_deliv_t
/
delete from kren_recip_list_t
/
delete from kren_recip_prefs_t
/

-- delete reviewer configuration

delete from kren_rvwer_t
/

-- ##############
-- # KIM Tables #
-- ##############

-- currently, the only built-in external identifier type is the TAX id

delete from krim_ext_id_typ_t where ext_id_typ_cd != 'TAX'
/

-- delete all groups except WorkflowAdmin and NotificationAdmin (they're referenced from Document Types)

delete from krim_grp_t where grp_nm not in ('WorkflowAdmin', 'NotificationAdmin')
/
delete from krim_grp_attr_data_t where grp_id not in (select grp_id from krim_grp_t)
/
delete from krim_grp_mbr_t where grp_id not in (select grp_id from krim_grp_t)
/

-- delete all entity and principal data except for principalID/entityID = 1 which is the 'kr' system user
-- and principalID=admin/entityID = 1100 which is the 'admin' user
-- also keep the 'notsys' user, as it is required for KEN
-- also keep the 'guest' user (entity_id='KR1000', prncpl_nm='guest'), it is required for guest user access (KULRICE-8349)

delete from krim_entity_addr_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_afltn_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_bio_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_ctznshp_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_email_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_emp_info_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_ent_typ_t where entity_id not in ('1', '1100', '1131', 'KR1000')
/
delete from krim_entity_ext_id_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_nm_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_phone_t where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_priv_pref_t where entity_id not in ('1', '1100', '1131')
/
delete from KRIM_ENTITY_ETHNIC_T where entity_id not in ('1', '1100', '1131')
/
delete from KRIM_ENTITY_RESIDENCY_T where entity_id not in ('1', '1100', '1131')
/
delete from KRIM_ENTITY_VISA_T where entity_id not in ('1', '1100', '1131')
/
delete from krim_entity_t where entity_id not in ('1', '1100', '1131', 'KR1000')
/

delete from krim_prncpl_t where prncpl_id not in ('1', 'admin', 'notsys', 'guest')
/

delete from krim_role_perm_t where role_perm_id = '856'
/

-- delete the assignment of the recall from routing permission for KULRICE-7687
delete from krim_role_perm_t where role_perm_id = '862'
/

-- #####################
-- # Sample App Tables #
-- #####################

-- drop all sample application tables and data

drop table trav_doc_2_accounts
/
drop table trv_acct
/
drop table trv_acct_ext
/
drop table trv_acct_fo
/
drop table trv_acct_type
/
drop table trv_doc_2
/
drop table trv_doc_acct
/
drop table TRV_ACCT_USE_RT_T
/
drop table TRV_MULTI_ATT_SAMPLE
/
drop table TRV_ATT_SAMPLE
/
drop sequence trv_fo_id_s
/

delete from krim_role_perm_t where perm_id in (select perm_id from krim_perm_t where nmspc_cd = 'KR-SAP')
/
delete from krim_perm_attr_data_t where perm_id in (select perm_id from krim_perm_t where nmspc_cd = 'KR-SAP')
/
delete from krim_perm_t where nmspc_cd = 'KR-SAP'
/
delete from krim_role_mbr_t where role_id in (select role_id from krim_role_t where nmspc_cd = 'KR-SAP')
/
delete from krim_role_t where nmspc_cd = 'KR-SAP'
/

drop table kr_kim_test_bo
/
drop table TST_SEARCH_ATTR_INDX_TST_DOC_T
/
delete from ACCT_DD_ATTR_DOC
/


-- clean out KRMS sample data

delete from  krms_cntxt_vld_rule_typ_t where cntxt_vld_rule_id like 'T%'
/
delete from  krms_cntxt_vld_func_t where cntxt_vld_func_id like 'T%'
/
delete from  krms_term_spec_ctgry_t where term_spec_id like 'T%' OR ctgry_id like 'T%'
/
delete from  krms_func_ctgry_t where func_id like 'T%' OR ctgry_id like 'T%'
/
delete from  krms_ctgry_t where ctgry_id like 'T%'
/
delete from  krms_func_parm_t where func_parm_id like 'T%'
/
delete from  krms_func_t where func_id like 'T%'
/
delete from  krms_term_parm_t where term_parm_id like 'T%'
/
delete from  krms_term_rslvr_parm_spec_t where term_rslvr_parm_spec_id like 'T%'
/
delete from  krms_term_t where term_id like 'T%'
/
delete from  krms_cntxt_vld_term_spec_t
/
delete from  krms_term_rslvr_input_spec_t
/
delete from  krms_term_rslvr_attr_t where term_rslvr_attr_id like 'T%'
/
delete from  krms_term_rslvr_t where term_rslvr_id like 'T%'
/
delete from  krms_term_spec_t where term_spec_id like 'T%'
/
delete from  krms_prop_parm_t where prop_parm_id like 'T%'
/
delete from  krms_cmpnd_prop_props_t
/
delete from  krms_agenda_attr_t where agenda_attr_id like 'T%'
/
delete from  krms_cntxt_vld_actn_typ_t
/
delete from  krms_cntxt_vld_agenda_typ_t
/
delete from  krms_cntxt_attr_t where cntxt_attr_id like 'T%'
/
delete from  krms_rule_attr_t where rule_attr_id like 'T%'
/
update krms_agenda_itm_t set when_true=null
/
update krms_agenda_itm_t set when_false=null
/
update krms_agenda_itm_t set always=null
/
delete from  krms_agenda_itm_t where agenda_itm_id like 'T%'
/
delete from  krms_actn_attr_t
/
delete from  krms_actn_t where actn_id like 'T%'
/
delete from  krms_typ_attr_t where typ_attr_id like 'T%'
/
delete from  krms_attr_defn_t where attr_defn_id like 'T%'
/
delete from  krms_agenda_t where agenda_id like 'T%'
/
update krms_rule_t set prop_id=null
/
delete from  krms_prop_t where prop_id like 'T%'
/
delete from  krms_rule_t where rule_id like 'T%'
/
delete from  krms_typ_t where typ_id like 'T%'
/
delete from  krms_cntxt_t
/
delete from krcr_nmspc_t where obj_id = '5a83c912-94b9-4b4d-ac3f-88c53380a4a3'
/

-- KULRICE-7427 Slightly modified version of Kellers SQL script

BEGIN EXECUTE IMMEDIATE 'DROP TABLE ACCT_DD_ATTR_DOC'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_ADDRESS_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_ADDRESS_TYP_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_AUTHOR_ACCOUNT_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_AUTHOR_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_BOOK_AUTHOR_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_AUTHOR_ACCOUNT_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_ORDER_DOC_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_ORDER_ENTRY_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_BOOK_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE BK_BOOK_TYP_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE KREW_HLP_T'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE BK_ADDRESS_ID_S'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE BK_AUTHOR_ID_S'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE BK_BOOK_ID_S'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE BK_ORDER_ENTRY_S'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE KREW_HLP_S'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE KRMS_CNTXT_TERM_SPEC_PREREQ_S'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;
/
UPDATE krim_perm_t SET nm = REPLACE( nm, 'Apprive', 'Approve' ) WHERE nm LIKE '%Apprive%'
/
DELETE FROM krcr_nmspc_t WHERE nm IN ( 'KR-RULE-TEST', 'KR-SAP' )
/
DELETE FROM krcr_cmpnt_set_t
/
DELETE FROM krcr_drvd_cmpnt_t
/
DELETE FROM krew_typ_attr_t
/
DELETE FROM krew_typ_t
/
DELETE FROM krns_maint_doc_t
/
DELETE FROM krns_doc_hdr_t
/
DELETE FROM krew_attr_defn_t
/
DELETE FROM KREW_RULE_RSP_T
/
DELETE FROM KREW_RULE_EXT_VAL_T
/
DELETE FROM KREW_RULE_EXT_T WHERE rule_id != '1044'
/
DELETE FROM KREW_RULE_EXPR_T
/


-- Re-enable constraints
DECLARE 
   CURSOR constraint_cursor IS 
      SELECT table_name, constraint_name 
         FROM user_constraints 
         WHERE constraint_type = 'R'
           AND status <> 'ENABLED';
BEGIN 
   FOR r IN constraint_cursor LOOP
      execute immediate 'ALTER TABLE '||r.table_name||' ENABLE CONSTRAINT '||r.constraint_name; 
   END LOOP; 
END;
/
