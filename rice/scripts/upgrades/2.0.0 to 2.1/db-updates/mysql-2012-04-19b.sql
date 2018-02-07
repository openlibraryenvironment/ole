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

create table TRV_ATT_SAMPLE (attachment_id varchar(30),
                              description varchar(4000),
                              attachment_filename varchar(300),
                              attachment_file_content_type varchar(255),
                              attachment_file longblob,
                              obj_id varchar(36) not null,
                              ver_nbr decimal(8) default 0 not null,
                              primary key (attachment_id));

create table TRV_MULTI_ATT_SAMPLE (gen_id decimal(14,0) not null,
                              attachment_id varchar(30),
                              description varchar(4000),
                              attachment_filename varchar(300),
                              attachment_file_content_type varchar(255),
                              attachment_file longblob,
                              obj_id varchar(36) not null,
                              ver_nbr decimal(8) default 0 not null,
                              primary key (gen_id),
                              foreign key (attachment_id) references TRV_ATT_SAMPLE(attachment_id));