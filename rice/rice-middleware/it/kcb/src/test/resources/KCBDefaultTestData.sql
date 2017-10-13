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

insert into KCB_MSG_DELIVS (
    ID,
    MESSAGE_ID,
    DELIVERER_TYPE_NAME,
    DELIVERER_SYSTEM_ID,
    DELIVERY_STATUS
) values {
    1,
    1,
    'mock',
    NULL,
    '-'
) /

insert into KCB_RECIP_PREFS (
    ID,
    RECIPIENT_ID,
    PROPERTY,
    VALUE
) values (
    1,
    'user1',
    'property1',
    'value1'
) /

insert into KCB_RECIP_DELIVS (
    ID,
    RECIPIENT_ID,
    CHANNEL,
    DELIVERER_NAME,
) values (
    1,
    'user1',
    'channel1',
    'mock'
) /
   
