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
-- KULRICE-4794
-- The following statements will change the DOC_HDR_ID from a decimal to a VARCHAR(40) on TRV_DOC_ACCT. 
--

ALTER TABLE TRV_DOC_ACCT RENAME TO OLD_TRV_DOC_ACCT
/
CREATE TABLE TRV_DOC_ACCT(
      DOC_HDR_ID VARCHAR2(40) NOT NULL,
      ACCT_NUM VARCHAR2(10) NOT NULL
)
/
INSERT INTO TRV_DOC_ACCT SELECT * FROM OLD_TRV_DOC_ACCT
/
DROP TABLE OLD_TRV_DOC_ACCT
/
ALTER TABLE TRV_DOC_ACCT ADD CONSTRAINT TRV_DOC_ACCTP1 PRIMARY KEY (DOC_HDR_ID,ACCT_NUM)
/