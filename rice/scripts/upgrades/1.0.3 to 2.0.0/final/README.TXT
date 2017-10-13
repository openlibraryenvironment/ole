====
    Copyright 2005-2014 The Kuali Foundation

    Licensed under the Educational Community License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.opensource.org/licenses/ecl2.php

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
====

/****************************************************************************************/
/*    Copyright 2005-2012 The Kuali Foundation
/*
/*    Licensed under the Educational Community License, Version 2.0 (the "License");
/*    you may not use this file except in compliance with the License.
/*    You may obtain a copy of the License at
/*
/*    http://www.opensource.org/licenses/ecl2.php
/*
/*    Unless required by applicable law or agreed to in writing, software
/*    distributed under the License is distributed on an "AS IS" BASIS,
/*    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/*    See the License for the specific language governing permissions and
/*    limitations under the License.
/****************************************************************************************/

Work was done on the final upgrade scripts so that they run without error.
For more information please see KULRICE-7992


/******************************************************/
/*       Upgrade Process from 1.0.3 to 2.0.0          */
/******************************************************/

If you are upgrading a standalone or bundled Oracle database:

    1. Run FIXED-update_final_oracle.sql
    2. Once data is verified, run cleanup_final_oracle.sql to drop temporary tables created during the upgrade

If you are upgrading a standalone or bundled MySql database:

    1. Run FIXED-update_final_mysql.sql
    2. Once data is verified, run cleanup_final_mysql.sql to drop temporary tables created during the upgrade

If you are upgrading an embedded Oracle database:

    1. Run update_client_final_oracle.sql

If you are upgrading an embedded MySql database:

    1. Run update_client_final_mysql.sql


/******************************************************/
/*  What changed from the original upgrade scripts?   */
/******************************************************/

    1. Demo and sample tables were removed from the scripts, along with any corresponding data.

    2. Any temp tables that used to be created & dropped as part of the upgrade are now dropped in cleanup_final_*.sql

    3. Constraints that used to be created as part of a table are now created separately.

    4. For KRIM table unique identifiers, the id that will be used is the next highest number in the table under
        10,000.  For Example, if ids 1, 2, 6, 10001, KFS123, and KC34 exist in KRIM_PERM_T, The next row created via
        the upgrade scripts will have an id of 7.


/******************************************************/
/*    Items to Consider                               */
/******************************************************/

    1. Are you upgrading to 2.1?  If so, read the following:
       https://svn.kuali.org/repos/rice/branches/rice-2.1/scripts/upgrades/2.0.0 to 2.1/final/README.TXT

    2. If your database is extremely large in production, leaving the temp tables to be dropped after the upgrade
       may not be feasible due to lack of database space.   For example, if your KREW_ACTN_RQST_T contains millions
       of rows, you may run out of space once that table is duplicated if it is not dropped before going on to the
       next large table.
