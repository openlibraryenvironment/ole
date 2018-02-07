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

-- ---- If you should want to clean out your KRMS tables:
-- delete from  krms_cntxt_vld_rule_t ;
-- delete from  krms_cntxt_vld_func_t ;
-- delete from  krms_term_spec_ctgry_t ;
-- delete from  krms_func_ctgry_t ;
-- delete from  krms_ctgry_t ;
-- delete from  krms_func_parm_t ;
-- delete from  krms_func_t ;
-- delete from  krms_term_parm_t ;
-- delete from  krms_term_rslvr_parm_spec_t ;
-- delete from  krms_term_t ;
-- delete from  krms_cntxt_vld_term_spec_t ;
-- delete from  krms_term_rslvr_input_spec_t ;
-- delete from  krms_term_rslvr_attr_t ;
-- delete from  krms_term_rslvr_t ;
-- delete from  krms_term_spec_t ;
-- delete from  krms_prop_parm_t ;
-- delete from  krms_cmpnd_prop_props_t ;
-- delete from  krms_agenda_attr_t ;
-- delete from  krms_cntxt_vld_actn_t ;
-- delete from  krms_cntxt_vld_agenda_t ;
-- delete from  krms_cntxt_attr_t ;
-- delete from  krms_rule_attr_t ;
-- update krms_agenda_itm_t set when_true=null;
-- update krms_agenda_itm_t set when_false=null;
-- update krms_agenda_itm_t set always=null;
-- delete from  krms_agenda_itm_t ;
-- delete from  krms_actn_attr_t ;
-- delete from  krms_actn_t ;
-- delete from  krms_typ_attr_t ;
-- delete from  krms_attr_defn_t ;
-- delete from  krms_agenda_t ;
-- update krms_rule_t set prop_id=null;
-- delete from  krms_prop_t ;
-- delete from  krms_rule_t ;
-- delete from  krms_typ_t where typ_id not in ('1000','1001');
-- delete from  krms_cntxt_t ;
-- delete from krcr_nmspc_t where obj_id = '5a83c912-94b9-4b4d-ac3f-88c53380a4a3';

---- KRMS test namespace
insert into krcr_nmspc_t (nmspc_cd, obj_id, nm, appl_id) values ('KRMS_TEST', '5a83c912-94b9-4b4d-ac3f-88c53380a4a3', 'Kuali Rules Test', 'RICE');


-- misc category
insert into krms_ctgry_t (ctgry_id, nm, nmspc_cd) values ('CAT01', 'misc', 'KRMS_TEST');



-- add a PeopleFlow attribute to the PeopleFlow types
insert into krms_attr_defn_t (ATTR_DEFN_ID, NM, NMSPC_CD, LBL, CMPNT_NM, DESC_TXT)
values ('1000', 'peopleFlowId', 'KR-RULE', 'PeopleFlow', null,
'An identifier for a PeopleFlow')
;
insert into krms_typ_attr_t (TYP_ATTR_ID, SEQ_NO, TYP_ID, ATTR_DEFN_ID) values ('1000', 1, '1000', '1000');
insert into krms_typ_attr_t (TYP_ATTR_ID, SEQ_NO, TYP_ID, ATTR_DEFN_ID) values ('1001', 1, '1001', '1000');

--
-- TermResolver taking 1 campus code parameter
--

insert into krms_term_spec_t
(term_spec_id, nmspc_cd, nm, typ, desc_txt, actv, ver_nbr)
values ('TERMSPEC_999', 'KRMS_TEST', 'campusSize', 'java.lang.Integer', 'Size in # of students of the campus', 'Y', 1)
;

insert into krms_typ_t
(typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
values ('T999', 'TermResolver', 'KRMS_TEST', null, 'Y', 1)
;

insert into krms_term_rslvr_t
(term_rslvr_id, nmspc_cd, nm, typ_id, output_term_spec_id, actv, ver_nbr)
values ('TERMRSLVR_999', 'KRMS_TEST', 'campusSizeResolver', 'T999','TERMSPEC_999', 'Y', 1)
;

insert into krms_term_rslvr_parm_spec_t
(term_rslvr_parm_spec_id, term_rslvr_id, nm, ver_nbr)
values ('TRPARM_999', 'TERMRSLVR_999', 'Campus Code', 1)
;

insert into krms_term_t
(term_id, term_spec_id, desc_txt, ver_nbr)
values ('TERM_999', 'TERMSPEC_999', 'Bloomington Campus Size', 1);

insert into krms_term_parm_t
(term_parm_id, term_id, nm, val, ver_nbr)
values ('TPARM_999', 'TERM_999', 'Campus Code', 'BL', 1)
;







insert into krms_attr_defn_t
(attr_defn_id, nm, nmspc_cd, lbl, actv, ver_nbr)
values('Q44001', 'Context1Qualifier', 'KRMS_TEST', 'Context 1 Qualifier', 'Y', 1)
;

insert into krms_attr_defn_t
(attr_defn_id, nm, nmspc_cd, lbl, actv, ver_nbr)
values('Q33001', 'Event', 'KRMS_TEST', 'Event Name', 'Y', 1)
;

insert into krms_typ_t
(typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
values ('T2', 'CAMPUS', 'KRMS_TEST', 'myCampusService', 'Y', 1)
;

insert into krms_typ_t
(typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
values ('T3', 'KrmsActionResolverType', 'KRMS_TEST', 'testActionTypeService', 'Y', 1)
;

insert into krms_typ_t
(typ_id, nm, nmspc_cd, actv, ver_nbr)
values ('T4', 'CONTEXT', 'KRMS_TEST',  'Y', 1)
;

insert into krms_typ_attr_t
(typ_attr_id, seq_no, typ_id, attr_defn_id, actv, ver_nbr)
values ('T4A', 1, 'T4', 'Q44001', 'Y', 1)
;

insert into krms_typ_t
(typ_id, nm, nmspc_cd, actv, ver_nbr)
values ('T5', 'AGENDA', 'KRMS_TEST',  'Y', 1)
;


insert into krms_typ_t
(typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr)
values ('T6', 'Campus Agenda', 'KRMS_TEST', 'campusAgendaTypeService', 'Y', 1)
;


insert into krms_cntxt_t
(cntxt_id, nmspc_cd, nm, typ_id, actv, ver_nbr)
values ('CONTEXT1','KRMS_TEST', 'Context1', 'T4', 'Y', 1)
;

insert into krms_cntxt_t
(cntxt_id, nmspc_cd, nm, typ_id, actv, ver_nbr)
values ('CONTEXT_NO_PERMISSION','KRMS_TEST_VOID', 'Context with no premissions', 'T4', 'Y', 1)
;

insert into krms_cntxt_attr_t
(cntxt_attr_id, cntxt_id, attr_val, attr_defn_id, ver_nbr)
values('C1ATTR1', 'CONTEXT1', 'BLAH', 'Q44001', 1)
;

insert into krms_cntxt_vld_actn_t
(cntxt_vld_actn_id, cntxt_id, actn_typ_id, ver_nbr)
values ('CONTEXT1T3', 'CONTEXT1', 'T3', 1)
;

insert into krms_cntxt_vld_actn_t
(cntxt_vld_actn_id, cntxt_id, actn_typ_id, ver_nbr)
values ('CONTEXT1ACTN1000', 'CONTEXT1', '1000', 1)
;

insert into krms_cntxt_vld_actn_t
(cntxt_vld_actn_id, cntxt_id, actn_typ_id, ver_nbr)
values ('CONTEXT1ACTION1001', 'CONTEXT1', '1001', 1)
;

insert into krms_rule_t
(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R201', 'KRMS_TEST', 'Rule1', 'T2', null, 'Y', 1, 'stub rule lorem ipsum')
;

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P101', 'is campus bloomington', null, 'S','R201',1)
;

update krms_rule_t
set prop_id = 'P101' where rule_id = 'R201'
;

insert into krms_term_spec_t
(term_spec_id, nm, nmspc_cd,  typ, actv, ver_nbr)
values ('TERMSPEC_001', 'campusCodeTermSpec', 'KRMS_TEST', 'java.lang.String', 'Y', 1);

insert into krms_term_t
(term_id, term_spec_id, desc_txt, ver_nbr)
values ('TERM_001', 'TERMSPEC_001', 'Campus Code', 1);

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('101A', 'P101', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('101C', 'P101', 'BL', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('101B', 'P101', '=', 'O', 3, 1)
;

insert into krms_actn_t
(actn_id, nm, nmspc_cd, desc_txt, typ_id, rule_id, seq_no, ver_nbr)
values ( 'action2001', 'testAction', 'KRMS_TEST', 'Action Stub for Testing', 'T3', 'R201', 1, 1)
;

insert into krms_agenda_t
(agenda_id, nm, cntxt_id, init_agenda_itm_id, typ_id, actv, ver_nbr)
values ( 'AGENDA301', 'My Fabulous Agenda', 'CONTEXT1', null, 'T6', 'Y', 1)
;

insert into krms_agenda_itm_t
(agenda_itm_id, rule_id, agenda_id, ver_nbr)
VALUES('AGENDA301ITEM1', 'R201', 'AGENDA301', 1)
;

update krms_agenda_t set INIT_AGENDA_ITM_ID = 'AGENDA301ITEM1' where agenda_id = 'AGENDA301'
;


insert into krms_term_spec_t
(term_spec_id, nmspc_cd, nm, typ, actv, ver_nbr)
values ('TERM001', 'KRMS_TEST', 'campusCode', 'T2', 'Y', 1)
;

-- next item

insert into krms_rule_t
(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R2', 'KRMS_TEST', 'Rule2', 'T2', null, 'Y', 1, 'Frog specimens bogus rule foo')
;

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P2', 'is campus bloomington', null, 'S','R2',1)
;

update krms_rule_t
set prop_id = 'P2' where rule_id = 'R2'
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('2A', 'P2', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('2C', 'P2', 'BL', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('2B', 'P2', '=', 'O', 3, 1)
;

insert into krms_agenda_itm_t
(agenda_itm_id, rule_id, agenda_id, ver_nbr)
VALUES('AGENDA301ITEM2', 'R2', 'AGENDA301', 1)
;

update krms_agenda_itm_t
SET when_true = 'AGENDA301ITEM2' WHERE agenda_itm_id = 'AGENDA301ITEM1'
;

-- next item

insert into krms_rule_t
(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R3', 'KRMS_TEST', 'Rule3', 'T2', null, 'Y', 1, 'Bloomington campus code rule')
;

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P3', 'is campus bloomington', null, 'S','R3',1)
;

update krms_rule_t
set prop_id = 'P3' where rule_id = 'R3'
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('3A', 'P3', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('3C', 'P3', 'BL', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('3B', 'P3', '=', 'O', 3, 1)
;

insert into krms_agenda_itm_t
(agenda_itm_id, rule_id, agenda_id, ver_nbr)
VALUES('AGENDA301ITEM3', 'R3', 'AGENDA301', 1)
;
--
update krms_agenda_itm_t
SET always = 'AGENDA301ITEM3' WHERE agenda_itm_id = 'AGENDA301ITEM2'
;

-- next item

insert into krms_rule_t
(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R4', 'KRMS_TEST', 'Rule4', 'T2', null, 'Y', 1, 'check for possible BBQ ingiter hazard')
;

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P4', 'is campus bloomington', null, 'S','R4',1)
;

update krms_rule_t
set prop_id = 'P4' where rule_id = 'R4'
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('4A', 'P4', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('4C', 'P4', 'BL', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('4B', 'P4', '=', 'O', 3, 1)
;

insert into krms_agenda_itm_t
(agenda_itm_id, rule_id, agenda_id, ver_nbr)
VALUES('AGENDA301ITEM4', 'R4', 'AGENDA301', 1)
;
--
update krms_agenda_itm_t
SET always = 'AGENDA301ITEM4' WHERE agenda_itm_id = 'AGENDA301ITEM3'
;

-- next item

insert into krms_rule_t
(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R5', 'KRMS_TEST', 'Rule5', 'T2', null, 'Y', 1, 'remembered to wear socks')
;

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P5', 'is campus bloomington', null, 'S','R5',1)
;

update krms_rule_t
set prop_id = 'P5' where rule_id = 'R5'
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('5A', 'P5', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('5C', 'P5', 'BL', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('5B', 'P5', '=', 'O', 3, 1)
;

insert into krms_agenda_itm_t
(agenda_itm_id, rule_id, agenda_id, ver_nbr)
VALUES('AGENDA301ITEM5', 'R5', 'AGENDA301', 1)
;

update krms_agenda_itm_t
SET when_false = 'AGENDA301ITEM5' WHERE agenda_itm_id = 'AGENDA301ITEM1'
;

-- next item

insert into krms_rule_t
(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R6', 'KRMS_TEST', 'Rule6', 'T2', null, 'Y', 1, 'good behavior at carnival')
;

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P6', 'is campus bloomington', null, 'S','R6',1)
;

update krms_rule_t
set prop_id = 'P6' where rule_id = 'R6'
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('6A', 'P6', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('6C', 'P6', 'BL', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('6B', 'P6', '=', 'O', 3, 1)
;

insert into krms_agenda_itm_t
(agenda_itm_id, rule_id, agenda_id, ver_nbr)
VALUES('AGENDA301ITEM6', 'R6', 'AGENDA301', 1)
;
--
update krms_agenda_itm_t
SET always = 'AGENDA301ITEM6' WHERE agenda_itm_id = 'AGENDA301ITEM1'
;



--
-- next item
--

insert into krms_rule_t
(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R7', 'KRMS_TEST', 'Rule7', 'T2', null, 'Y', 1, 'is KRMS in da haus')
;

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P7', 'is campus bloomington', null, 'S','R7',1)
;

update krms_rule_t
set prop_id = 'P7' where rule_id = 'R7'
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('7A', 'P7', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('7C', 'P7', 'BL', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('7B', 'P7', '=', 'O', 3, 1)
;

insert into krms_agenda_itm_t
(agenda_itm_id, rule_id, agenda_id, ver_nbr)
VALUES('AGENDA301ITEM7', 'R7', 'AGENDA301', 1)
;
--
update krms_agenda_itm_t
SET when_false = 'AGENDA301ITEM7' WHERE agenda_itm_id = 'AGENDA301ITEM3'
;

--
-- rule with a compound proposition
--
insert into krms_rule_t
(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R421', 'KRMS_TEST', 'CmpdTestRule', 'T2', null, 'Y', 1, 'For testing compound props')
;

insert into krms_prop_t
(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, cmpnd_op_cd, ver_nbr)
values ('P421A', 'a compound prop', null, 'C','R421', 'a', 1)
;

update krms_rule_t
set prop_id = 'P421A' where rule_id = 'R421'
;

insert into krms_term_spec_t
(term_spec_id, nmspc_cd, nm, typ, actv, ver_nbr)
values ('TERMSPEC_002', 'KRMS_TEST', 'bogusFundTermSpec', 'java.lang.String', 'Y', 1);

insert into krms_term_t
(term_id, term_spec_id, desc_txt, ver_nbr)
values ('TERM_002', 'TERMSPEC_002', 'Fund Name', 1);


-- 2nd level prop
insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P421B', 'a simple child to a compound prop', null, 'S','R421', 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421B1', 'P421B', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421B2', 'P421B', 'Muir', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421B3', 'P421B', '=', 'O', 3, 1)
;

insert into krms_cmpnd_prop_props_t
(cmpnd_prop_id, prop_id, seq_no)
values ('P421A', 'P421B', 1);

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P421C', '2nd simple child to a compound prop ', null, 'S','R421', 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421C1', 'P421C', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421C2', 'P421C', 'Revelle', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421C3', 'P421C', '=', 'O', 3, 1)
;

insert into krms_cmpnd_prop_props_t
(cmpnd_prop_id, prop_id, seq_no)
values ('P421A', 'P421C', 1);


insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P421D', '3nd simple child to a compound prop ', null, 'S','R421', 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421D1', 'P421D', 'TERM_001', 'T', 1, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421D2', 'P421D', 'Warren', 'C', 2, 1)
;

insert into krms_prop_parm_t
(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('421D3', 'P421D', '=', 'O', 3, 1)
;

insert into krms_cmpnd_prop_props_t
(cmpnd_prop_id, prop_id, seq_no)
values ('P421A', 'P421D', 1);


--
-- start of new agendas (AGENDA002, AGENDA003) and their associated items
--



--  AGENDA 002
insert into krms_agenda_t (agenda_id, nm, cntxt_id, init_agenda_itm_id, typ_id, actv, ver_nbr)
values ('AGENDA002', 'SimpleAgendaCompoundProp', 'CONTEXT1', null, 'T5', 'Y', 1);

insert into krms_agenda_itm_t (AGENDA_ITM_ID, RULE_ID, AGENDA_ID, VER_NBR)
values ('AGENDA002ITEM1', 'R421', 'AGENDA002', 1);

update krms_agenda_t set INIT_AGENDA_ITM_ID = 'AGENDA002ITEM1' where AGENDA_ID = 'AGENDA002';

--  AGENDA 003 stuff

insert into krms_term_spec_t (TERM_SPEC_ID, NM, TYP, ACTV, VER_NBR, DESC_TXT, nmspc_cd)
values ('TERMSPEC_003', 'PO Value', 'T6', 'Y', 1, 'Purchase Order Value', 'KRMS_TEST');

insert into krms_term_spec_t (TERM_SPEC_ID, NM, TYP, ACTV, VER_NBR, DESC_TXT, nmspc_cd)
values ('TERMSPEC_004', 'PO Item Type', 'T1', 'Y', 1, 'Purchased Item Type', 'KRMS_TEST');

insert into krms_term_spec_t (TERM_SPEC_ID, NM, TYP, ACTV, VER_NBR, DESC_TXT, nmspc_cd)
values ('TERMSPEC_005', 'Account', 'T1', 'Y', 1, 'Charged To Account', 'KRMS_TEST');

insert into krms_term_spec_t (TERM_SPEC_ID, NM, TYP, ACTV, VER_NBR, DESC_TXT, nmspc_cd)
values ('TERMSPEC_006', 'Occasion', 'T1', 'Y', 1, 'Special Event', 'KRMS_TEST');

insert into krms_cntxt_vld_term_spec_t(cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
values ('CTSPID001', 'CONTEXT1', 'TERMSPEC_001', 'N');

insert into krms_cntxt_vld_term_spec_t(cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
values ('CTSPID002', 'CONTEXT1', 'TERMSPEC_002', 'N');

insert into krms_cntxt_vld_term_spec_t(cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
values ('CTSPID003', 'CONTEXT1', 'TERMSPEC_003', 'N');

insert into krms_cntxt_vld_term_spec_t(cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
values ('CTSPID004', 'CONTEXT1', 'TERMSPEC_004', 'N');

insert into krms_cntxt_vld_term_spec_t(cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
values ('CTSPID005', 'CONTEXT1', 'TERMSPEC_005', 'N');

insert into krms_cntxt_vld_term_spec_t(cntxt_term_spec_prereq_id, cntxt_id, term_spec_id, prereq)
values ('CTSPID006', 'CONTEXT1', 'TERMSPEC_006', 'N');


insert into krms_term_t(term_id,TERM_SPEC_ID, DESC_TXT, VER_NBR)values ('TERM_003', 'TERMSPEC_003', 'PO Value', 1);
insert into krms_term_t(term_id,TERM_SPEC_ID, DESC_TXT, VER_NBR)values ('TERM_004', 'TERMSPEC_004', 'PO Item Type', 1);
insert into krms_term_t(term_id,TERM_SPEC_ID, DESC_TXT, VER_NBR)values ('TERM_005', 'TERMSPEC_005', 'Account', 1);
insert into krms_term_t(term_id,TERM_SPEC_ID, DESC_TXT, VER_NBR)values ('TERM_006', 'TERMSPEC_006', 'Occasion', 1);

--
-- big fin rule
--
insert into krms_rule_t(rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
values ('R500', 'KRMS_TEST', 'Going Away Party for Travis', 'T2', null, 'Y', 1, 'Does PO require my approval');

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, cmpnd_op_cd, ver_nbr)
values ('P500', 'is purchase special', null, 'C','R500', '&', 1);

update krms_rule_t set prop_id = 'P500' where rule_id = 'R500';


-- 2nd level prop

-- is it expensive
insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P501A', 'is purchase order value large', null, 'S','R500', 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('501A1', 'P501A', 'TERM_003', 'T', 1, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('501A2', 'P501A', '5500.00', 'C', 2, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('501A3', 'P501A', '>', 'O', 3, 1);

insert into krms_cmpnd_prop_props_t(cmpnd_prop_id, prop_id, seq_no)
values ('P500', 'P501A', 1);

-- is it controlled
insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, cmpnd_op_cd, ver_nbr)
values ('P502', 'is purchased item controlled', null, 'C','R500', '|', 1);

insert into krms_cmpnd_prop_props_t(cmpnd_prop_id, prop_id, seq_no)
values ('P500', 'P502', 2);

-- is it special
insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P503', 'is it for a special event', null, 'C','R500', 1);

insert into krms_cmpnd_prop_props_t(cmpnd_prop_id, prop_id, seq_no)
values ('P500', 'P503', 3);

---- controlled 3rd level props -----

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P502A', 'is item purchased animal', null, 'S','R500', 1);

insert into krms_cmpnd_prop_props_t(cmpnd_prop_id, prop_id, seq_no)
values ('P502', 'P502A', 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502A1', 'P502A', 'TERM_004', 'T', 1, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502A2', 'P502A', 'ANIMAL', 'C', 2, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502A3', 'P502A', '=', 'O', 3, 1);



insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P502B', 'is purchased item radioactive', null, 'S','R500', 1);

insert into krms_cmpnd_prop_props_t(cmpnd_prop_id, prop_id, seq_no)
values ('P502', 'P502B', 2);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502B1', 'P502B', 'TERM_004', 'T', 1, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502B2', 'P502B', 'RADIOACTIVE', 'C', 2, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502B3', 'P502B', '=', 'O', 3, 1);


insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P502C', 'is it medicinal', null, 'S','R500', 1);

insert into krms_cmpnd_prop_props_t(cmpnd_prop_id, prop_id, seq_no)
values ('P502', 'P502C', 2);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502C1', 'P502C', 'TERM_004', 'T', 1, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502C2', 'P502C', 'ALCOHOL BEVERAGE', 'C', 2, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('502C3', 'P502C', '=', 'O', 3, 1);


-- is it special 3rd level props

insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P503A', 'charged to Kuali', null, 'S','R500', 1);

insert into krms_cmpnd_prop_props_t(cmpnd_prop_id, prop_id, seq_no)
values ('P503', 'P503A', 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('503A1', 'P503A', 'TERM_005', 'T', 1, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('503A2', 'P503A', 'KUALI SLUSH FUND', 'C', 2, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('503A3', 'P503A', '=', 'O', 3, 1);



insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
values ('P503B', 'Party at Travis House', null, 'S','R500', 1);

insert into krms_cmpnd_prop_props_t(cmpnd_prop_id, prop_id, seq_no)
values ('P503', 'P503B', 2);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('503B1', 'P503B', 'TERM_006', 'T', 1, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('503B2', 'P503B', 'Christmas Party', 'C', 2, 1);

insert into krms_prop_parm_t(prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
values ('503B3', 'P503B', '=', 'O', 3, 1);


--  AGENDA 003
insert into krms_agenda_t (agenda_id, nm, cntxt_id, init_agenda_itm_id, typ_id, actv, ver_nbr)
values ('AGENDA003', 'One Big Rule', 'CONTEXT1', null, 'T5', 'Y', 1);

insert into krms_agenda_itm_t (AGENDA_ITM_ID, RULE_ID, AGENDA_ID, VER_NBR)
values ('AGENDA003ITEM1', 'R500', 'AGENDA003', 1);

update krms_agenda_t set INIT_AGENDA_ITM_ID = 'AGENDA003ITEM1' where AGENDA_ID = 'AGENDA003';



-- SQL for test CampusAgendaType:

insert into krms_cntxt_vld_agenda_t
(cntxt_vld_agenda_id, cntxt_id, agenda_typ_id, ver_nbr)
values ('CONTEXT1T6', 'CONTEXT1', 'T6', 1)
;

-- add a db-only attribute to CampusAgendaType
insert into krms_attr_defn_t (ATTR_DEFN_ID, NM, NMSPC_CD, LBL, CMPNT_NM, DESC_TXT)
values ('Q9900', 'Optional Test Attribute', 'KRMS_TEST', 'label', null,
'this is an optional attribute for testing')
;
insert into krms_typ_attr_t (TYP_ATTR_ID, SEQ_NO, TYP_ID, ATTR_DEFN_ID) values ('T6A', 2, 'T6', 'Q9900');

-- add our campus attribute to CampusAgendaType
insert into krms_attr_defn_t (ATTR_DEFN_ID, NM, NMSPC_CD, LBL, CMPNT_NM, DESC_TXT)
values ('Q9901', 'Campus', 'KRMS_TEST', 'campus label', null, 'the campus which this agenda is valid for')
;
insert into krms_typ_attr_t (TYP_ATTR_ID, SEQ_NO, TYP_ID, ATTR_DEFN_ID) values ('T6B', 1, 'T6', 'Q9901');


-- General validation action type w/ message attribute
insert into krms_typ_t (typ_id, nm, nmspc_cd, srvc_nm, actv, ver_nbr) values('T9', 'Validation Action', 'KRMS_TEST', 'validationActionTypeService', 'Y', 1);
insert into krms_attr_defn_t (attr_defn_id, nm, nmspc_cd, lbl, actv, cmpnt_nm, ver_nbr, desc_txt) values ('QQ8806', 'Action Message', 'KRMS_TEST', 'Action Message', 'Y', null, 1, 'Message validation action returns');
insert into krms_typ_attr_t (typ_attr_id, seq_no, typ_id, attr_defn_id, actv, ver_nbr) values ('T9M', 3, 'T9', 'QQ8806', 'Y', 1);
-- make it valid in our test context
insert into krms_cntxt_vld_actn_t (cntxt_vld_actn_id, cntxt_id, actn_typ_id, ver_nbr) values ('CONTEXT1T9', 'CONTEXT1', 'T9', 1);


--
-- additional agenda item template:
--

-- insert into krms_rule_t
-- (rule_id, nmspc_cd, nm, typ_id, prop_id, actv, ver_nbr, desc_txt)
-- values ('R${ID}', 'KRMS_TEST', 'Rule${ID}', 'T2', null, 'Y', 1, 'Bloomington Campus Code Rule')
-- ;
--
-- insert into krms_prop_t(prop_id, desc_txt, typ_id, dscrm_typ_cd, rule_id, ver_nbr)
-- values ('P${ID}', 'is campus bloomington', null, 'S','R${ID}',1)
-- ;
--
-- update krms_rule_t
-- set prop_id = 'P${ID}' where rule_id = 'R${ID}'
-- ;
--
-- insert into krms_prop_parm_t
-- (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
-- values ('${ID}A', 'P${ID}', 'TERM_001', 'T', 1, 1)
-- ;
--
-- insert into krms_prop_parm_t
-- (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
-- values ('${ID}C', 'P${ID}', 'BL', 'C', 2, 1)
-- ;
--
-- insert into krms_prop_parm_t
-- (prop_parm_id, prop_id, parm_val, parm_typ_cd, seq_no, ver_nbr)
-- values ('${ID}B', 'P${ID}', '=', 'O', 3, 1)
-- ;
--
-- insert into krms_agenda_itm_t
-- (agenda_itm_id, rule_id, agenda_id, ver_nbr)
-- VALUES('AGENDA301ITEM${ID}', 'R${ID}', 'AGENDA301', 1)
-- ;
--
-- update krms_agenda_itm_t
-- SET when_true = 'AGENDA301ITEM${ID}' WHERE agenda_itm_id = 'AGENDA301ITEM${PARENT_ID}'
-- ;


