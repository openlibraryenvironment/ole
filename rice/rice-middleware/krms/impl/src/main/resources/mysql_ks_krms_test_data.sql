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

/*
Insert Statement
Source : MySQL documentation
*/
/*SELECT * FROM krms_agenda_t
SELECT * FROM krms_rule_t
SELECT * FROM krms_cntxt_t
SELECT * FROM krms_agenda_itm_t
SELECT * FROM krms_cntxt_vld_agenda_typ_t
SELECT * FROM krms_prop_t
SELECT * FROM krms_prop_parm_t
SELECT * FROM krms_cmpnd_prop_props_t
SELECT * FROM krms_typ_t
SELECT * FROM krms_typ_attr_t
SELECT * FROM krms_term_t
SELECT * FROM krms_term_spec_t
SELECT * FROM krms_term_rslvr_t
SELECT * FROM krms_term_parm_t
SELECT * FROM krms_ctgry_t
SELECT * FROM krms_cntxt_vld_term_spec_t*/

DELETE FROM krms_term_parm_t WHERE term_parm_id IN (20000, 20001);
DELETE FROM krms_term_t WHERE term_id IN (20000, 20001);
DELETE FROM krms_cntxt_vld_agenda_typ_t WHERE cntxt_vld_agenda_id IN (20000);
DELETE FROM krms_cntxt_vld_rule_typ_t WHERE cntxt_vld_rule_id IN (20000, 20001, 20002);
DELETE FROM krms_cntxt_vld_term_spec_t WHERE cntxt_term_spec_prereq_id IN (20000, 20001, 20002);
DELETE FROM krms_term_spec_t WHERE term_spec_id IN (20000, 20001, 20002);
DELETE FROM krms_prop_parm_t WHERE prop_parm_id IN (20000, 20001, 20002, 20003, 20004, 20005);
DELETE FROM krms_agenda_itm_t WHERE agenda_itm_id IN (20000, 20001);    
DELETE FROM krms_agenda_t WHERE agenda_id IN (20000);
DELETE FROM krms_rule_t WHERE rule_id IN (20000, 20001);    
DELETE FROM krms_cmpnd_prop_props_t WHERE cmpnd_prop_id IN (20000, 20001, 20002);
DELETE FROM krms_prop_t WHERE prop_id IN (20000, 20001, 20002);    
DELETE FROM krms_cntxt_t WHERE cntxt_id IN (20000);
DELETE FROM krms_typ_reln_t WHERE typ_reln_id IN (20000, 20001, 20002, 20003, 20004, 20005, 20006);
DELETE FROM krms_typ_t WHERE typ_id IN (20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20009, 20010);
DELETE FROM krms_ctgry_t WHERE ctgry_id IN (20000);

INSERT INTO krms_ctgry_t (ctgry_id, nm, nmspc_cd, ver_nbr)
    VALUES (20000, 'Course Requirements', 'KR-RULE-TEST', 0);    
    
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20000, 'Must have successfully completed a  minimum of {n} course(s) from {courses}.', 'KR-RULE-TEST', 'propositionTypeService', 'Y', 0);
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20001, 'Permission of {org} required.', 'KR-RULE-TEST', 'propositionTypeService', 'Y', 0);
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20002, 'Must have successfully completed {course}.', 'KR-RULE-TEST', 'propositionTypeService', 'Y', 0);
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20003, 'Must be concurrently enrolled in {course}.', 'KR-RULE-TEST', 'propositionTypeService', 'Y', 0);
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20007, 'Must have successfully completed all courses from {courses}.', 'KR-RULE-TEST', 'propositionTypeService', 'Y', 0);
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20008, 'Must not have successfully completed {course}.', 'KR-RULE-TEST', 'propositionTypeService', 'Y', 0);
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20009, 'Must not have successfully completed any course from {courses}.', 'KR-RULE-TEST', 'propositionTypeService', 'Y', 0);

INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20004, 'Student Eligibility and Prerequisites.', 'KR-RULE-TEST', 'ruleTypeService', 'Y', 0);
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20005, 'Corequisites.', 'KR-RULE-TEST', 'ruleTypeService', 'Y', 0);
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20006, 'Antirequisites.', 'KR-RULE-TEST', 'ruleTypeService', 'Y', 0);
    
INSERT INTO krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
    VALUES (20010, 'Course Requirements', 'KR-RULE-TEST', 'agendaTypeService', 'Y', 0);    

INSERT INTO krms_typ_reln_t (typ_reln_id, from_typ_id, to_typ_id, reln_typ, seq_no, ver_nbr, actv)
    VALUES (20000, '20004', '20002', 'A', 1, 0, 'Y');
INSERT INTO krms_typ_reln_t (typ_reln_id, from_typ_id, to_typ_id, reln_typ, seq_no, ver_nbr, actv)
    VALUES (20001, '20004', '20007', 'A', 2, 0, 'Y');
INSERT INTO krms_typ_reln_t (typ_reln_id, from_typ_id, to_typ_id, reln_typ, seq_no, ver_nbr, actv)
    VALUES (20002, '20004', '20000', 'A', 3, 0, 'Y');
INSERT INTO krms_typ_reln_t (typ_reln_id, from_typ_id, to_typ_id, reln_typ, seq_no, ver_nbr, actv)
    VALUES (20003, '20004', '20001', 'A', 4, 0, 'Y');
INSERT INTO krms_typ_reln_t (typ_reln_id, from_typ_id, to_typ_id, reln_typ, seq_no, ver_nbr, actv)
    VALUES (20004, '20005', '20003', 'A', 1, 0, 'Y');
INSERT INTO krms_typ_reln_t (typ_reln_id, from_typ_id, to_typ_id, reln_typ, seq_no, ver_nbr, actv)
    VALUES (20005, '20006', '20008', 'A', 1, 0, 'Y');
INSERT INTO krms_typ_reln_t (typ_reln_id, from_typ_id, to_typ_id, reln_typ, seq_no, ver_nbr, actv)
    VALUES (20006, '20006', '20009', 'A', 2, 0, 'Y');

INSERT INTO krms_cntxt_t (cntxt_id, nmspc_cd, nm, typ_id, actv, ver_nbr, desc_txt)
    VALUES (20000, 'KR-RULE-TEST', 'Course Requirements', 'T1004', 'Y', 0, 'Course Requirements');
    
INSERT INTO krms_cntxt_vld_agenda_typ_t (cntxt_vld_agenda_id, cntxt_id, agenda_typ_id, ver_nbr)
    VALUES (20000, '20000', '20010', 0);
    
INSERT INTO krms_cntxt_vld_rule_typ_t (cntxt_vld_rule_id, cntxt_id, rule_typ_id, ver_nbr)
    VALUES (20000, '20000', '20004', 0);
INSERT INTO krms_cntxt_vld_rule_typ_t (cntxt_vld_rule_id, cntxt_id, rule_typ_id, ver_nbr)
    VALUES (20001, '20000', '20005', 0);
INSERT INTO krms_cntxt_vld_rule_typ_t (cntxt_vld_rule_id, cntxt_id, rule_typ_id, ver_nbr)
    VALUES (20002, '20000', '20006', 0);            

INSERT INTO krms_agenda_t (agenda_id, nm, cntxt_id, init_agenda_itm_id, typ_id, actv, ver_nbr)
    VALUES (20000, 'GEOG123 Course Requirements', '20000', '20000', 'T1004', 'Y', 0);

INSERT INTO krms_rule_t (rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
    VALUES (20000, 'KR-RULE-TEST', 'GEOG123 Prerequisites', 20004, NULL, 'Y',0,'Student Eligibility and Prerequisites for GEOG123');
INSERT INTO krms_rule_t (rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
    VALUES (20001, 'KR-RULE-TEST', 'GEOG123 Corequisites', 20005, NULL, 'Y', 0, 'Corequisites for GEOG123');    
    
INSERT INTO krms_agenda_itm_t (agenda_itm_id, rule_id, sub_agenda_id, agenda_id, ver_nbr, when_true, when_false, always)
    VALUES (20001, 20001, NULL, 20000, 1, NULL, NULL, NULL);
INSERT INTO krms_agenda_itm_t (agenda_itm_id, rule_id, sub_agenda_id, agenda_id, ver_nbr, when_true, when_false, always)
    VALUES (20000, 20000, NULL, 20000, 1, 20001, NULL, NULL);

INSERT INTO krms_prop_t (prop_id, desc_txt, typ_id, dscrm_typ_cd, cmpnd_op_cd, rule_id, ver_nbr, cmpnd_seq_no)
    VALUES (20000, 'Must have successfully completed a  minimum of 1 course(s) from (GEOG123,MATH140)', 20000, 'S', NULL, 20000, 1, NULL);
INSERT INTO krms_prop_t (prop_id,desc_txt, typ_id, dscrm_typ_cd, cmpnd_op_cd, rule_id, ver_nbr, cmpnd_seq_no)
    VALUES (20001, 'Must meet 1 of the following', NULL, 'C', '|', 20000, 1, NULL);    
INSERT INTO krms_prop_t (prop_id,desc_txt, typ_id, dscrm_typ_cd, cmpnd_op_cd, rule_id, ver_nbr, cmpnd_seq_no)
    VALUES (20002, 'Permission of Geography Dept required', 20001, 'S', NULL, 20000, 1, NULL);    
    
INSERT INTO krms_cmpnd_prop_props_t (cmpnd_prop_id, prop_id)
    VALUES (20001, 20000);
INSERT INTO krms_cmpnd_prop_props_t (cmpnd_prop_id, prop_id)
    VALUES (20001, 20002);
    
UPDATE krms_rule_t SET prop_id:='20001' WHERE rule_id = '20000';

INSERT INTO krms_prop_parm_t (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
    VALUES (20000, 20000, 20000, 'T', 1, 1);
INSERT INTO krms_prop_parm_t (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
    VALUES (20001, 20000, '1', 'C', 2, 1);
INSERT INTO krms_prop_parm_t (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
    VALUES (20002, 20000, '>', 'O', 3, 1);        
INSERT INTO krms_prop_parm_t (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
    VALUES (20003, 20002, 20001, 'T', 1, 1);
INSERT INTO krms_prop_parm_t (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
    VALUES (20004, 20002, 'true', 'C', 2, 1);
INSERT INTO krms_prop_parm_t (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
    VALUES (20005, 20002, '=', 'O', 3, 1);    

INSERT INTO krms_term_spec_t (term_spec_id, nm, typ, actv, ver_nbr, desc_txt, nmspc_cd)
    VALUES (20000, 'Number of Courses in list', 'java.lang.Integer', 'Y', 1, 'Number of courses in list for student', 'KR-RULE-TEST');
INSERT INTO krms_term_spec_t (term_spec_id, nm, typ, actv, ver_nbr, desc_txt, nmspc_cd)
    VALUES (20001, 'Organizational Permission', 'java.lang.Boolean', 'Y', 1, 'Organizational permission for student', 'KR-RULE-TEST');
INSERT INTO krms_term_spec_t (term_spec_id, nm, typ, actv, ver_nbr, desc_txt, nmspc_cd)
    VALUES (20002, 'Completed Course', 'java.lang.Boolean', 'Y', 1, 'Completed Course for student', 'KR-RULE-TEST');
    
INSERT INTO krms_cntxt_vld_term_spec_t (cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
    VALUES (20000, '20000', '20000', 'N');
INSERT INTO krms_cntxt_vld_term_spec_t (cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
    VALUES (20001, '20000', '20001', 'N');
INSERT INTO krms_cntxt_vld_term_spec_t (cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
    VALUES (20002, '20000', '20002', 'N');

INSERT INTO krms_term_t (term_id, term_spec_id, ver_nbr, desc_txt)
    VALUES (20000, 20000, 1, 'Number of completed Courses in list for student.');
INSERT INTO krms_term_t (term_id, term_spec_id, ver_nbr, desc_txt)
    VALUES (20001, 20001, 1, 'Organizational Permission for student.');
    
INSERT INTO krms_term_parm_t (term_parm_id, term_id, nm, val, ver_nbr)
    VALUES (20000, 20000, 'GEOG124 Requirements list', 'GEOG123,MATH140', 1);
INSERT INTO krms_term_parm_t (term_parm_id, term_id, nm, val, ver_nbr)
    VALUES (20001, 20001, 'Geography Department ID', '53', 1);