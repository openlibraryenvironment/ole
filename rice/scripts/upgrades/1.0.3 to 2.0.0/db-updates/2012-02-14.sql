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
-- KULRICE-6710: Drop krms_cntxt_vld_rule_t, krms_cntxt_vld_actn_t and krms_cntxt_vld_agenda_t tables
--

-- NOTE that these tables should have been renamed in the master db, but mysteriously still were present.
-- deleting here.  If you get errors that these tables and sequences don't exist, you can omit these statements
-- without concern.

drop table krms_cntxt_vld_actn_t
/
drop sequence krms_cntxt_vld_actn_s
/
drop table krms_cntxt_vld_agenda_t
/
drop sequence krms_cntxt_vld_agenda_s
/
drop table krms_cntxt_vld_rule_t
/
drop sequence krms_cntxt_vld_rule_s
/
