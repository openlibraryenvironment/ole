/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.regex.Matcher
import java.io.File;
import java.util.regex.Pattern

/* Begin User Configurable Fields */

/* run option properties */
def persistenceXml = false
def mysql = false
def pkClasses = true
def clean = false
def dry = false
def verbose = false
def createBOs = true
def scanForConfigFiles = false

/* File Path Properties */
def projHome = '/java/projects/play/rice-1.1.0'
def srcRootDir = '/impl/src/main/java/'
def resourceDir = '/impl/src/main/resources/'
def metaInf = 'META-INF/'
def schemaName = "RICE110DEV"

def repositories = [
		'/java/projects/play/rice-1.1.0/impl/src/main/resources/org/kuali/rice/krad/config/OJB-repository-krad-test.xml'
		]

def sourceDirectories = [
		'/impl/src/main/java/'
		]

/* persistence detail properties */
def ojbMappingPattern = ~/.*OJB.*repository.*xml/
def persistenceUnitName = "rice"
def persistenceXmlFilename = 'persistence.xml'	
def mysqlScriptFile = '/scripts/upgrades/mysqlSequenceFix.sql'
def mysqlOrmFile = 'project-orm.xml'
def backupExtension = ".backup"
def logger = new Logger("errors.log")
def classes = [:]

//   Search for OJB Mapping Files
if (scanForConfigFiles){
	println 'Scanning for files'
    GlobalVariables.conversion_util.getRespositoryFiles(projHome, resourceDir, ojbMappingPattern, repositories)
}
println 'Found '+repositories.size().toString()+' OJB mapping files:'
repositories.each {println it}
println 'Convering the following '+sourceDirectories.size().toString()+' Source Directories:'
sourceDirectories.each {println it}

// give the user the opportunity to review and abort.
def actions = 'The script is set to: '
def something
if (dry) actions = 'The script will perform a dry run to: '
if (persistenceXml) {
	actions+=' Generate persistence.xml files.'
}
if (mysql) {
	actions+=' Generate MySQL sequence annotations.'
} 
if (clean) {
	actions+=' Clean up backup file.'
} 
if (createBOs){
	actions+=' Generate JPA annotated BO files.'
}
// Primary Key Classes need to be created AFTER the BO classes
if (pkClasses){
	actions+=' Generate Composite Primary Key Classes.'
}

System.in.withReader { reader ->
println()
println actions
print "Would you like to continue? (Y/N): "
something = reader.readLine()
println()
}


if (something.toString().toUpperCase().equals( 'Y'))
{
	/*
	The first pass over the OJB XML captures all of the metadata in groovy datastructures.
	*/

	GlobalVariables.metadata_handler.loadMetaData(repositories, classes, logger)
	println '\nFirst pass completed, metadata captured.'

	//for persistence.xml
	if (persistenceXml) {
		println '\nGenerating persistence.xml...'
		GlobalVariables.persistence_handler.generatePersistenceXML(classes, persistenceUnitName, persistenceXmlFilename, projHome+resourceDir+metaInf)
	} 

	//for handling sequence in mysql
	if (mysql) {
		println '\nGenerating MySQL sequence annotations...'
		GlobalVariables.mysql_handler.generateMySQLSequence(classes, schemaName, projHome, mysqlScriptFile, resourceDir+metaInf+mysqlOrmFile)
	}

	//clean back up
	if (clean) {
		println '\nCleaning up backup files...'
		GlobalVariables.conversion_util.cleanBackupFiles(classes, sourceDirectories, projHome, backupExtension, logger, verbose)
	} 

	//generate  sounre code for bo in JPA style
	if (createBOs)	{
		println '\nGenerating Business Object POJOs with JPA Annotations...'
		GlobalVariables.annotation_handler.generateJPABO(classes, sourceDirectories, projHome, dry, verbose, backupExtension, logger, false)
	}
	// generate composite primary key classes
	if (pkClasses){
		println '\nGenerating Composite Primary Key Classes...'
		GlobalVariables.annotation_handler.generateJPABO(classes, sourceDirectories, projHome, dry, verbose, backupExtension, logger, pkClasses)
	}
}
else
{
	println 'Aborting script.'
}

/*
This class is simply for logging basic messages.
*/
class Logger {
    def logFile
    def lineNumber = 1

    def Logger() {
        this("errors.log")
    }
    
    def Logger(file) {
        logFile = new File(file)
        if (logFile.exists()) {
            logFile.delete()
        }  
    }
    
    def log(message) {
        logFile << "${lineNumber}) ${message}" 
        logFile << "\n"
        lineNumber++
    }
}

/* 
Below are the class definitions responsible for holding the OJB metedata. 
*/

class Field {
    def id
    def name
    def column
    def jdbcType
    def primarykey
    def nullable
    def indexed
    def autoincrement
    def sequenceName
    def locking
    def conversion    
    def access
}

class Key {
    def fieldRef
    def fieldIdRef
}

class ReferenceDescriptor {
    def name
    def classRef
    def proxy
    def autoRetrieve
    def autoUpdate
    def autoDelete
    def foreignKeys = []
}

class CollectionDescriptor {
    def name
    def collectionClass
    def elementClassRef
    def orderBy
    def sort
    def indirectionTable
    def proxy
    def autoRetrieve
    def autoUpdate
    def autoDelete
    def fkPointingToThisClassColumn
    def fkPointingToElementClassColumn
    def inverseForeignKeys = []    
    def manyToMany
}

class ClassDescriptor {
    def compoundPrimaryKey = false
    def pkClassIdText = ""
    def cpkFilename = ""
    def tableName
    def className
    def primaryKeys = []
    def fields = [:]
    def referenceDescriptors = [:]
    def collectionDescriptors = [:]
}

class GlobalVariables{
	public static conversion_util = new ConversionUtils();
	public static metadata_handler = new MetaDataHandler();
	public static persistence_handler = new PersistenceFileHandler();
	public static mysql_handler = new MySQLHandler();
	public static type_handler = new CustomerTypeHandler();
	public static annotation_handler = new AnnotationHandler();
}