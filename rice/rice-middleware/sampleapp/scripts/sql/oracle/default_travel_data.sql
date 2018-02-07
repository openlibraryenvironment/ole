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

TRUNCATE TABLE TRV_ACCT_EXT DROP STORAGE
/
INSERT INTO TRV_ACCT_EXT (ACCT_NUM,ACCT_TYPE)
  VALUES ('a1','IAT')
/
INSERT INTO TRV_ACCT_EXT (ACCT_NUM,ACCT_TYPE)
  VALUES ('a14','CAT')
/
INSERT INTO TRV_ACCT_EXT (ACCT_NUM,ACCT_TYPE)
  VALUES ('a2','EAT')
/
INSERT INTO TRV_ACCT_EXT (ACCT_NUM,ACCT_TYPE)
  VALUES ('a3','IAT')
/
INSERT INTO TRV_ACCT_EXT (ACCT_NUM,ACCT_TYPE)
  VALUES ('a6','CAT')
/
INSERT INTO TRV_ACCT_EXT (ACCT_NUM,ACCT_TYPE)
  VALUES ('a8','EAT')
/
INSERT INTO TRV_ACCT_EXT (ACCT_NUM,ACCT_TYPE)
  VALUES ('a9','CAT')
/
TRUNCATE TABLE TRV_ACCT_TYPE DROP STORAGE
/
INSERT INTO TRV_ACCT_TYPE (ACCT_TYPE,ACCT_TYPE_NAME)
  VALUES ('CAT','Clearing Account Type')
/
INSERT INTO TRV_ACCT_TYPE (ACCT_TYPE,ACCT_TYPE_NAME)
  VALUES ('EAT','Expense Account Type')
/
INSERT INTO TRV_ACCT_TYPE (ACCT_TYPE,ACCT_TYPE_NAME)
  VALUES ('IAT',' Income Account Type')
/

INSERT INTO trvl_traveler_dtl_t (id, EMP_PRINCIPAL_ID, CUST_NBR, FIRST_NM,
LAST_NM, ADDR_LINE_1, ADDR_LINE_2, CITY_NM, POSTAL_STATE_CD, POSTAL_CD,
COUNTRY_CD, EMAIL_ADDR, PHONE_NBR, LIABILITY_INSURANCE, TRAVELER_TYP_CD, ACTV_IN)
VALUES (10000, 'earl', '101', 'Earl', 'Jones', '101 Waverider St.', 'Apt 104', 'Huntington Beach',
'CA', '92649', 'US', 'earl@kuali.org', '949 494-3712', 'true', 'EMP', 'true')
/

INSERT INTO trvl_traveler_dtl_t (id, EMP_PRINCIPAL_ID, CUST_NBR, FIRST_NM, LAST_NM, ADDR_LINE_1
, CITY_NM, POSTAL_STATE_CD, POSTAL_CD, COUNTRY_CD, EMAIL_ADDR, PHONE_NBR, LIABILITY_INSURANCE,
 TRAVELER_TYP_CD, ACTV_IN)
 VALUES (10001, 'edna', '102', 'Edna', 'Smith', '1331 Sussex Place', 'North Tustin', 'CA', '92705', 'US',
 'edna@kuali.org', '714 633-9013', 'true', 'EMP', 'true')
 /

INSERT INTO trvl_trip_typ_t (CODE, NM) VALUES ('EMP', 'Employee')
/
INSERT INTO trvl_trip_typ_t(CODE, NM) VALUES ('NON', 'Non-Employee')
/


