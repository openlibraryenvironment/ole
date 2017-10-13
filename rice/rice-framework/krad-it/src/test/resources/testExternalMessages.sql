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

-- test db data for org.kuali.rice.krad.datadictionary.MessageBeanProcessorTest

insert into krad_msg_t values ('TEST', 'TestMessagesSimpleProperty', '#property1', 'en-US', '33333333', 1, null, 'ext p1 value');
insert into krad_msg_t values ('TEST', 'TestMessagesSimpleProperty', '#property2', 'en-US', '33333334', 1, null, 'ext p2 value');
insert into krad_msg_t values ('TEST', 'TestMessagesMessageKey', 'testMessageKey1', 'en-US', '33333335', 1, null, 'ext key p1 value');
insert into krad_msg_t values ('TEST', 'TestMessagesMessageKey', 'testMessageKey2', 'en-US', '33333336', 1, null, 'ext key p2 value');
insert into krad_msg_t values ('TEST', 'TestMessagesListMessageKey', 'testListMessageKey', 'en-US', '33333337', 1, null, 'ext list key value');
insert into krad_msg_t values ('TEST', 'TestMessagesMapMessageKey', 'testMapMessageKey', 'en-US', '33333338', 1, null, 'ext map key value');
insert into krad_msg_t values ('TEST', 'TestMessagesExpressionMerge', '#property1', 'en-US', '33333339', 1, null, 'Value ''{0}'' is invalid');
insert into krad_msg_t values ('TEST', 'TestMessagesExpressionMerge', '#property2', 'en-US', '33333340', 1, null, 'The {0} code should not equal {1}');
insert into krad_msg_t values ('TEST', 'TestMessagesKeyExprMerge', 'testMessageKey1', 'en-US', '33333341', 1, null, 'Expr {0} then expr {1}');
insert into krad_msg_t values ('TEST', 'TestMessagesOptions', '#summer.value', 'en-US', '33333342', 1, null, 'Ext Summer');
insert into krad_msg_t values ('TEST', 'TestMessagesOptions', '#spring.value', 'en-US', '33333343', 1, null, 'Ext Spring');
insert into krad_msg_t values ('KUALI', 'All', 'validation.test2.error', 'en-US', '33333344', 1, null, 'App Error found for {0}');
insert into krad_msg_t values ('TEST', 'All', 'validation.test2.error', 'en-US', '33333345', 1, null, 'Error found for {0}');
insert into krad_msg_t values ('TEST', 'TestComponent', 'validation.test2.error', 'en-US', '33333346', 1, null, 'Component Error found');
insert into krad_msg_t values ('TEST', 'TestLocales', 'message.key', 'en-US', '33333347', 1, null, 'English US Message');
insert into krad_msg_t values ('TEST', 'TestLocales', 'message.key', 'fr-CA', '33333348', 1, null, 'French CA Message');
insert into krad_msg_t values ('TEST', 'TestLocales', 'message.key', 'de', '33333349', 1, null, 'German Message');
insert into krad_msg_t values ('TEST', 'TestExpressionMessages', 'testMessageKey1', 'en-US', '33333350', 1, null, 'ext key p1 value');
insert into krad_msg_t values ('TEST', 'TestExpressionMessages', 'testMessageKey2', 'en-US', '33333351', 1, null, 'ext key p2 value');
insert into krad_msg_t values ('TEST', 'TestLocales', 'message.key2', 'en', '33333352', 1, null, 'English Message');
insert into krad_msg_t values ('TEST', 'TestLocales', 'message.key2', 'en-US', '33333353', 1, null, 'English US Message');
