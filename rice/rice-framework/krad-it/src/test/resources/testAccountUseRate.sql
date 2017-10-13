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

insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('1', 'a1', '1.5', {ts '2010-01-01 00:00:00'}, {ts '2011-01-01 00:00:00'});
insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('2', 'a2', '1.5', {ts '2010-01-01 00:00:00'}, {ts '2011-01-01 00:00:00'});
insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('3', 'a2', '1.5', {ts '2010-03-01 00:00:00'}, {ts '2011-01-01 00:00:00'});
insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('4', 'a2', '1.5', {ts '2012-01-01 00:00:00'}, {ts '2013-01-01 00:00:00'});
insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('5', 'a3', '1.5', {ts '2010-01-01 00:00:00'}, {ts '2010-06-01 00:00:00'});
insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('6', 'a4', '1.5', null, {ts '2011-01-01 00:00:00'});
insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('7', 'a5', '1.5', {ts '2010-01-01 00:00:00'}, null);
insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('8', 'a6', '1.5', null, null);
insert into trv_acct_use_rt_t (id, acct_num, rate, actv_frm_dt, actv_to_dt) values ('9', 'b1', '3', {ts '2010-01-01 12:30:00'}, {ts '2010-06-01 15:30:00'});
