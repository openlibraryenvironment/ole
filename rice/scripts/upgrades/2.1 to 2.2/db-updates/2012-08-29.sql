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
--     KULRICE-6676 - external message tables
--

-------------------------------------------------------------------------
-- krad_msg_t
-------------------------------------------------------------------------
CREATE TABLE krad_msg_t
(
	nmspc_cd VARCHAR2(20) NOT NULL,
	cmpnt_cd VARCHAR2(100) NOT NULL,
	msg_key VARCHAR2(100) NOT NULL,
	loc VARCHAR2(255) NOT NULL,
	obj_id VARCHAR2(36) NOT NULL,
	ver_nbr DECIMAL(8) DEFAULT 1 NOT NULL,
	msg_desc VARCHAR2(255),
	txt VARCHAR2(4000)
)
/

ALTER TABLE krad_msg_t
    ADD CONSTRAINT krad_msg_tC1
PRIMARY KEY (nmspc_cd,cmpnt_cd,msg_key,loc)
/

ALTER TABLE krad_msg_t
    ADD CONSTRAINT krad_msg_tC2
UNIQUE (obj_id)
/
