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

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!                                                   !!
!!  IMPORTANT NOTICE                                 !!
!!                                                   !!
!!  PLEASE READ BEFORE UPGRADING YOUR SCHEMA         !!
!!                                                   !!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

There was an omission in the previous set of upgrade scripts provided to
migrate from the 2.1.0 to the 2.2.0 schema.  Depending on how your 2.2.0
database was built, you may have prerequisite steps to perform before running
the upgrade scripts in this folder.

If the origin of your 2.2.0 database was:

1) An upgraded 2.1.0 database:

If your Rice 2.2.0 database was upgraded from the 2.1.0 schema using the
upgrade scripts, you will first need to run the appropriate "missing prereqs"
script from this folder (note that there is no such script needed for oracle
Rice client schemas), and then you can proceed with the update scripts
also found in this folder.

2) The Impex dataset for the Rice 2.2.0 database:

If your Rice 2.2.0 database was built from the Impex dataset provided with
Rice, or derived from that dataset, then you do not need to perform this
additional step, and you can directly upgrade your database using the update
scripts in this folder.