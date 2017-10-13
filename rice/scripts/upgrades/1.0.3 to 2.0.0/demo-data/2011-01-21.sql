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

ALTER TABLE TRV_ACCT ADD (SUB_ACCT VARCHAR2(10))
/
ALTER TABLE TRV_ACCT ADD (SUB_ACCT_NAME VARCHAR2(50))
/
ALTER TABLE TRV_ACCT ADD (CREATE_DT DATE)
/
ALTER TABLE TRV_ACCT ADD (SUBSIDIZED_PCT FLOAT)
/
update trv_acct set sub_acct = concat(acct_num, '-sub'),
    sub_acct_name = concat('Sub Account for ', acct_name),
    create_dt=SYSTIMESTAMP,
    subsidized_pct = dbms_random.value(1,100) 
/