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

The re-factor work break the big script into 8 modules, they work fine as scripts, but they have not 
been converted into groovy(plugin 2.0 style) project yet, so if you import them into a groovy project, 
it may have errors

Pre-request:

install groovy-1.7.0, set GROOVY_HOME to the installed directory(example: /java/tools/groovy/groovy-1.7.0)

List of modules:

ojb2jpa.groovy, this is the main script, contains all configurations and global resource handles. 

jpa_makeup.groovy, major help script, for cleaning up existing annotations before generating BOs and adding @transient after generating BOs  

AnnoationHandler.groovy, major script with annotation functions, handles all annotation details.

ConversionUtils.groovy, utility functions for conversion. 

MetaDataHandler.groovy, contains functions for loading OJB meta data into memory

CustomerTypeHanlder.groovy, contains functions for handling annotations of all customer type converters  

MySQLHandler.groovy, handles mySQL sequence and ORM file(optional) 

PersistenceFleHandler.groovy: handles creating persistence.xml 

main script: ojb2jpa.groovy
	configuration: 
		1, change "run option properties" to config what you want to do
		2, change "File Path Properties" to locate source files you want to convert
		3, change "persistence detail properties" to config output options
		
    usage: >groovy -cp ./scripts/jpaconversion ./scripts/jpaconversion/ojb2jpa.groovy
    
help script: jpa_makeup.groovy(for clean existing annotations and add @TRANSIENT)
  
    usage: groovy -cp ./scripts/jpaconversion ./scripts/jpaconversion/jpa_makeup.groovy [CLEAN | TRANSIENT]
    
If you want to run it in a batch, the order should be:

groovy -cp ./scripts/jpaconversion ./scripts/jpaconversion/jpa_makeup.groovy CLEAN
groovy -cp ./scripts/jpaconversion ./scripts/jpaconversion/ojb2jpa.groovy
groovy -cp ./scripts/jpaconversion ./scripts/jpaconversion/jpa_makeup.groovy TRANSIENT

************************Detailed description about the conversion scripts*************************************
The ojb2jpa.groovy script performs some conversion tasks to help 
convert your Rice client from OJB persistence model to JPA
The scripts perform the following tasks:
- generates a persistence.xml file in the META-INF directory
- generates the JPA annotated BO POJO source files
- generates composite primary key class source files 
- generates mysql sql scripts for creating sequence generation tables
- generates orm.xml files for mysql sequence annotation overrides 
- clean up existing annotations from BOs

Note: The generated files have not been verified to be 100% correct.
They are intended to provide a head start in converting from OJB to JPA

/*** Properties Local to your local environment ***
 ***    YOU NEED TO CHANGE THESE OPTIONS        ***
 ***    GROUPED IN 3 SECTIONS                   ***/
 /**********************************************************************************************/
 /*1,  run option properties , changing these props will decide what tasks to perform          */
  /**********************************************************************************************/
getXML_MYSQL_PerApp = false,     /*true means the script will genreate persistence.xml, MySQL sequence script and orm file per application*/  
getXML_MYSQL_PerModule = false   /*true means the script will generate persistence.xml, MySQL sequence script and orm file per module, under the name module_persistence.xml, etc.*/
pkClasses = false           /*true means the script will generate composite primary key class source files */
createBOs = false            /*true means the script will generates the JPA annotated BO POJO source files*/
scanForConfigFiles = false  /*true means the script will search the resource repository for the OJB config files*/
                            /*false means you will provide the path of the OJB config file(s) in the file path properties section*/ 
clean = false               /*true means the script will clean up the backup files 
dry = false                 /*true means performing a dry run, output to the console, do not actually create the files. */
verbose = false             /*true means printing debugging message to console*/  
 
/**********************************************************************************************/
/* 2, File Path Properties, changing them will help to locate the ojb config files and source */
/*    code you want to convert, also it will config the outupt file path and names            */
/**********************************************************************************************/
projHome = '/java/projects/play/rice-1.1.0'      /*project root*/
ojbMappingPattern = ~/.*OJB|ojb.*-.*xml/         /*OJB config name pattern*/											  
resourceDir = '/impl/src/main/resources/'        /*OJB config file root dir*/

/* if you know the path of the OJB config files, it is recommended to put it here, this will save some time 
   from searching them */
   
def repositories = [
		'/java/projects/play/rice-1.1.0/impl/src/main/resources/org/kuali/rice/ken/config/OJB-repository-ken.xml'
		]
def sourceDirectories = [
		'/impl/src/main/java/'
		]
		
metaInf = 'META-INF/'          /*resourceDir + metaInf is the output dir for persistence.xml, mysql sequence script and orm.xml*/
                                                 
persistenceXmlFilename = 'persistence.xml'	               /*persistence.xml file name at application level*/
mysqlScriptFile = 'mysqlSequenceFix.sql'                   /*mySQL sequence table script file name at application level*/
mysqlOrmFile = 'orm.xml'                                   /*mySQL sequence orm.xml file name at application level*/

/**********************************************************************************************/
/* other misc. config properties
/**********************************************************************************************/
def persistenceUnitName = "rice"                     /*persistence unit name*/
def schemaName = "RICE110DEV"                        /*the schema name of rice or client database*/
def backupExtension = ".backup"                      /*backup file ext*/
def logger = JPAConversionHandlers.bo_log            /*define default log, pointing jpa_bo.log defined in JPAConversionHandlers*/


/**********************************************************************************************/
/* JPAConversionHandlers
/**********************************************************************************************/
This is a global resource locator, pointing to other scripts to complete conversion tasks.
It also config some logs for logging some important information in the process:
1,  jpa_bo.log: log important information about mapped super class, customer Type, bi-directional relationships, 
			    java.sql.date/timestampe conversion found in the process 
2,  jpa_error.log: log errors and exceptions caught in the process
3,  jpa_cpk.log: same info as jpa_bo.log but is captured in cpk genreation. 
4,  jpa_info.log: not really being used now(holding some duplicated info) 


/*************  Dependencies *********************/
NOTE:  For the generated code with JPA2.0 features(such as CASCADETYPE.REFRESH) and dependencies on rice-1.1.x(such as composite 
primary key id classes need CompositePrimaryKeyBase from 1.1 ), you will need update the class path before making them compiled.

/************* newly added major features **********/ 
0, have generated Composite Primary Key classes extend CompositePrimaryKeyBase which implements the equals() and hashCode() methods.
1, modify auto-xxx and proxy translation into JPA CascadeType annotation according to KULRICE-3882
2, handle generated sequence using sequence generator
3, join column referencing with multi columns 
4, add constructor with parameters for CPK class
5, Log OneToOne, OneToMany and ManyToOne bidirectional relationships 
6, Log possible mapped super classes and AttributeOverrides
7, change all java.sql.Date/TimeStamp classes to java.util.Date, and annotated as @Temporal(TemporalType.TIMESTAMP)
8, import customer Types to cpk classes 
9, annotate the id fields inside CPK class, but keep annotation of the same fields in the entity class
10, add support to @orderBy
11, add a script jpa_makeup to clean existing annotations before annotation, and add @Transient after annotation 
12, converted and handled all customer types

There should be more, need to find them and document.


