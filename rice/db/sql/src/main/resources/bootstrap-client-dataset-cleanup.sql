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

-- #####################
-- # Sample App Tables #
-- #####################

-- drop all sample application tables

-- drop table recipes_t
-- /
-- drop table recipe_categories_t
-- /
-- drop table recipe_ingredients_t
-- /

-- SQL Maven plugin does not know how to handle SQL files containing only comments

select sysdate from dual
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

-- delete all sample app tables
DECLARE
   CURSOR tables_cursor IS
      SELECT table_name
         FROM user_tables
         WHERE
            table_name like 'BK#_%' escape '#' OR
            table_name like 'TRAV#_%' escape '#' OR
            table_name like 'TRV#_%' escape '#' OR
            table_name like 'TRVL#_%' escape '#' OR
            table_name = 'ACCT_DD_ATTR_DOC' OR
            table_name = 'TST_SEARCH_ATTR_INDX_TST_DOC_T'
         ORDER BY table_name;
BEGIN
   FOR r IN tables_cursor LOOP
      execute immediate 'DROP TABLE '||r.table_name||' CASCADE CONSTRAINTS';
   END LOOP;
END;
/

-- delete all sample app sequences
DECLARE
   CURSOR sequences_cursor IS
      SELECT sequence_name
         FROM user_sequences
         WHERE
            sequence_name like 'ACCT#_%S' escape '#' OR
            sequence_name like 'BK#_%S' escape '#' OR
            sequence_name like 'TRAV#_%S' escape '#' OR
            sequence_name like 'TRVL#_%SEQ' escape '#' OR
            sequence_name like 'TRV#_%S' escape '#'
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
