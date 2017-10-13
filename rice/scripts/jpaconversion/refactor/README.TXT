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

The refactor work break the big scripts into 8 modules, they work fine as scripts, but they have not 
been converted into groovy project yet,  so if you import them into a groovy project, it may have errors

main script: ojb2jpa.groovy

	configuration: 
		1, change "run option properties" to do what you want to do
		2, change "File Path Properties" to decide source files you want to convert
		3, change "persistence detail properties" to decide output
		
    usage: >groovy -cp ./scripts/jpaconversion/refactor ./scripts/jpaconversion/refactor/ojb2jpa.groovy
    
help script: jpa_makeup.groovy(for clean existing annotations and add @TRANSIENT)
  
    usage: groovy -cp ./scripts/jpaconversion/refactor ./scripts/jpaconversion/refactor/jpa_makeup.groovy [CLEAN | TRANSIENT]

more details are coming soon...