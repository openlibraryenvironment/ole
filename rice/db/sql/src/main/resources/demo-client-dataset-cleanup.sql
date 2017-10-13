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

-- #############################################################################
-- # demo-client-dataset-cleanup.sql                                           #
-- #                                                                           #
-- # This file will clean up and remove data from the client database to       # 
-- # ensure that it has the minimum amount of data required to function for    #
-- # the packaged demo dataset.                                                #
-- #############################################################################


-- ##############
-- # KSB Tables #
-- ##############

-- Quartz Data - delete all data from the quartz tables except krsb_qrtz_locks, jobs will get rescheduled when system starts

delete from krsb_qrtz_job_listeners
/
delete from krsb_qrtz_trigger_listeners
/
delete from krsb_qrtz_fired_triggers
/
delete from krsb_qrtz_simple_triggers
/
delete from krsb_qrtz_cron_triggers
/
delete from krsb_qrtz_blob_triggers
/
delete from krsb_qrtz_triggers
/
delete from krsb_qrtz_job_details
/
delete from krsb_qrtz_calendars 
/
delete from krsb_qrtz_paused_trigger_grps
/
delete from krsb_qrtz_scheduler_state 
/

-- Message Queue Tables - tables should be emptied

delete from krsb_bam_parm_t
/
delete from krsb_bam_t
/
delete from krsb_msg_pyld_t
/
delete from krsb_msg_que_t
/

-- ##############
-- # KNS Tables #
-- ##############

-- We need to clear all data from all of these tables with the exception of the KRNS_NTE_TYP_T table

delete from krns_adhoc_rte_actn_recip_t
/
delete from krns_att_t
/
delete from krns_lookup_rslt_t
/
delete from krns_lookup_sel_t
/
delete from krns_maint_doc_att_t
/
delete from krns_maint_doc_t
/
delete from krns_maint_lock_t
/
delete from krns_nte_t
/
delete from krns_pessimistic_lock_t
/
delete from krns_sesn_doc_t
/
delete from krns_doc_hdr_t
/

-- Disable constraints for the mass drops to follow
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

-- delete all non-client views, which at the moment is all views
DECLARE
   CURSOR views_cursor IS
      SELECT view_name
         FROM user_views
         WHERE view_name like 'KRIM#_%' escape '#'
         ORDER BY view_name;
BEGIN
   FOR r IN views_cursor LOOP
      execute immediate 'DROP VIEW '||r.view_name;
   END LOOP;
END;
/

-- delete all non-client tables, leaving the sample app tables alone for now
DECLARE
   CURSOR tables_cursor IS
      SELECT table_name
         FROM user_tables
         WHERE
            table_name like 'KRSB#_SVC#_%#_T' escape '#' OR
            table_name like 'KRCR#_%T' escape '#' OR
            table_name like 'KREN#_%T' escape '#' OR
            table_name like 'KREW#_%T' escape '#' OR
            table_name like 'KRIM#_%T' escape '#' OR
            table_name like 'KRLC#_%T' escape '#' OR
            table_name like 'KRMS#_%T' escape '#' OR
            table_name = 'KR_KIM_TEST_BO'
         ORDER BY table_name;
BEGIN
   FOR r IN tables_cursor LOOP
      execute immediate 'DROP TABLE '||r.table_name||' CASCADE CONSTRAINTS';
   END LOOP;
END;
/

-- delete all non-client sequences, leaving the sample app sequences alone
DECLARE
   CURSOR sequences_cursor IS
      SELECT sequence_name
         FROM user_sequences
         WHERE
            sequence_name like 'KRSB#_SVC#_%#_S' escape '#' OR
            sequence_name like 'KRCR#_%S' escape '#' OR
            sequence_name like 'KREN#_%S' escape '#' OR
            sequence_name like 'KREW#_%S' escape '#' OR
            sequence_name like 'KRIM#_%S' escape '#' OR
            sequence_name like 'KRLC#_%S' escape '#' OR
            sequence_name like 'KRMS#_%S' escape '#'
         ORDER BY sequence_name;
BEGIN
   FOR r IN sequences_cursor LOOP
      execute immediate 'DROP SEQUENCE '||r.sequence_name;
   END LOOP;
END;
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

