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

/* File Path Properties */
def projHome = '/java/projects/play/rice-1.1.0'
def srcRootDir = '/impl/src/main/java/'
def resourceDir = '/impl/src/main/resources/'
def metainf = 'META-INF/'
def schemaName = "RICE110DEV"

def repositories = [
    //'../kcb/src/main/resources/OJB-repository-kcb.xml'
    //'../ken/src/main/resources/OJB-repository-ken.xml' 
    //'../kew/src/main/resources/org/kuali/workflow/ojb/OJB-repository-kew.xml'
    //'../kim/src/main/resources/OJB-repository-kim.xml'
    //'../kns/src/main/resources/OJB-repository-kns.xml',
    //'../ksb/src/main/resources/OJB-repository-ksb.xml'
    //'../kns/src/test/resources/repository.xml'
  //  '../impl/src/main/resources/org/kuali/rice/ken/config/OJB-repository-ken.xml'
    '/java/projects/play/rice-1.1.0/impl/src/main/resources/org/kuali/rice/krad/config/OJB-repository-krad.xml'
]

def sourceDirectories = [
    //'/kcb/src/main/java/', 
    //'/ken/src/main/java/',
    //'/kew/src/main/java/',
    //'/kim/src/main/java/',
    //'/kns/src/main/java/',
    //'/ksb/src/main/java/',
    //'/kns/src/test/java/'
    //'../ken-api/src/main/java/', 
    //'../kew-api/src/main/java/'
    //'../kim-api/src/main/java/'
    //'../kns-api/src/main/java/'
    //'../ksb-api/src/main/java/'
    '/impl/src/main/java/'
]

/* run option properties */
def persistenceXml = false
def mysql = false
def pkClasses = false
def clean = false
def dry = false
def verbose = false
def createBOs = true

/* persistence detail properties */
def persistenceUnitName = "rice"
def persistenceXmlFilename = 'persistence.xml'	
	
def scanForConfigFiles = false
def ojbMappingPattern = ~/.*OJB.*repository.*xml/

def backupExtension = ".backup"
def logger = new Logger("errors.log")
def classes = [:]


//   Search for OJB Mapping Files
if (scanForConfigFiles){
	println 'Scanning for files'
    getRespositoryFiles(projHome, resourceDir, ojbMappingPattern, repositories)
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

	loadMetaData(repositories, classes, logger)
	println ''
	println 'First pass completed, metadata captured.'

	//for persistence.xml
	if (persistenceXml) {
		println ''
		println 'Generating persistence.xml...'
    	generatePersistenceXML(classes, persistenceUnitName, persistenceXmlFilename, projHome+resourceDir+metainf)
	} 

	//for handling sequence in mysql
	if (mysql) {
		println ''
		println 'Generating MySQL sequence annotations...'
		generateMySQLSequence(classes, schemaName)
	}

	//clean back up
	if (clean) {
		println ''
		println 'Cleaning up backup files...'
		cleanBackupFiles(classes, sourceDirectories, projHome, backupExtension)
	} 

	//generate  sounre code for bo in JPA style
	if (createBOs)	{
		println ''
		println 'Generating Business Object POJOs with JPA Annotations...'
		generateJPABO(classes, sourceDirectories, projHome, dry, verbose, backupExtension, logger,  false)
	}
	// generate composite primary key classes
	if (pkClasses){
		println ''
		println 'Generating Composite Primary Key Classes...'
		generateJPABO(classes, sourceDirectories, projHome, dry, verbose, backupExtension, logger,  pkClasses)
	}
}
else
{
	println 'Aborting script.'
}

def generateJPABO(classes, sourceDirectories, projHome, dry, verbose, backupExtension, logger, pkClassesOnly){
	/*
	The second pass iterates over all of the class descriptors found above and generates JPA annotations.
	*/
	classes.values().each {
	    c ->     
	        println 'Class Name: '+c.className.toString()
	        def javaFile
	        def backupFile
	        sourceDirectories.each {
	            dir ->
	                def file = projHome + dir + c.className.replaceAll("\\.", "/") + ".java"
	                if (new File(file).exists()) {
	                    javaFile = new File(file)
	                    backupFile = new File(file + backupExtension)
	                    c.cpkFilename = projHome + dir + c.className.replaceAll("\\.", "/") + "Id.java"
	                }
	        }
	        
	        if (!javaFile) {
	            logger.log "${javaFile} does not exist.  Can not annotate ${c.className}.  Please check that its source directory is included in this script."
	            return
	        }
	        
	        def classAnnotation = ""
	        def text = javaFile.text
	        def cpkFieldText = ""
	        def cpkGetterText = ""
	        def packageName  = c.className[0..c.className.lastIndexOf('.') - 1]
	        def cpkClassName = c.className[c.className.lastIndexOf('.') + 1..-1] + "Id"
	        
	        if (!dry) {
		        if (backupFile.exists()) {
		            backupFile.delete()
		        }
		        backupFile << text
		        backupFile << "\n"
	        }
	        
	        if (c.compoundPrimaryKey) {
	            classAnnotation += "@IdClass(${c.className}Id.class)\n"
	            text = addImport(text, "IdClass")
	            logger.log "Please check generated compound primary key class [${c.className}Id.java]"
	            c.pkClassIdText += """/*
 * Copyright 2005-2008 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ${packageName};

import java.io.Serializable;
import javax.persistence.Column;
import org.kuali.rice.krad.bo.CompositePrimaryKeyBase;

/**
 * This Compound Primary Class has been generated by the rice ojb2jpa Groovy script.  Please
 * note that there are no setter methods, only getters.  This is done purposefully as cpk classes
 * can not change after they have been created.  Also note they require a public no-arg constructor.
 * The equals() and hashCode() methods are inherited from CompositPrimaryKeyBase. 
 */
public class ${cpkClassName} extends CompositePrimaryKeyBase implements Serializable {
"""
	        }
	        
	        classAnnotation += "@Entity\n@Table(name=\"${c.tableName}\")"
	        text = addImport(text, "Entity")
	        text = addImport(text, "Table")
	        text = addImport(text, "CascadeType")                    
	        text = annotate(text, "public class", classAnnotation)
	        c.fields.values().each {
	            f ->
					println("**********************PK\t" + f.name + f.primarykey);
	                def annotation = ""
	                def nullable = '' 
	                if (f.primarykey) {
	                    annotation += "@Id\n\t"
	                    text = addImport(text, "Id")
	                    if (c.compoundPrimaryKey) {
	                        def temp = new String(text).trim()
	                        temp = temp.replaceFirst("(private|protected).*(\\b${f.name})(\\s)*;", "ABCD1\$0ABCD2")
	                        if (temp.indexOf('ABCD1') > -1 && temp.indexOf('ABCD2') > -1) {
								println("**********************working here" );
	                            temp = temp[temp.indexOf('ABCD1') + 5 .. -1]
	                            temp = temp[0 .. temp.indexOf('ABCD2') - 1]
                                cpkFieldText += "    @Column(name=\"${f.column}\")\n"
	                            cpkFieldText += "    ${temp.trim()}\n"
	                            temp = temp[temp.indexOf(' ') .. -1].trim()
	                            temp = temp[0 .. temp.indexOf(' ')].trim()
	                            def temp2 = f.name[0].toUpperCase() + f.name[1 .. -1]
	                            cpkGetterText += "    public ${temp} get${temp2}() { return ${f.name}; }\n\n"
	                        }
	                    }   
						println("*****************text \t" + cpkFieldText)
	                }
	                if (f.locking) {
	                    annotation += "@Version\n\t"
	                    text = addImport(text, "Version")
	                }
	                if (['clob', 'blob'].contains(f.jdbcType?.toLowerCase())) {
	                    annotation += "@Lob\n\t@Basic(fetch=FetchType.LAZY)\n\t"
	                    text = addImport(text, "Lob")
	                    text = addImport(text, "Basic")
	                    text = addImport(text, "FetchType")
	                }
//	                if (f.jdbcType?.equalsIgnoreCase("date")) {
//	                    annotation += "@Temporal(TemporalType.DATE)\n\t"
//	                    text = addImport(text, "Temporal")                    
//	                    text = addImport(text, "TemporalType")
//	                }
	                if (f.conversion?.toString()?.size() > 0){
		                if (f.conversion.contains("OjbCharBooleanConversion")){
	                		annotation += "@Type(type=\"yes_no\")\n\t"
		                	text = addOtherImport(text, "org.hibernate.annotations.Type")
	                	} else if (f.conversion.contains("OjbCharBooleanFieldTFConversion")) {
	                		annotation += "@Type(type=\"true_false\")\n\t"
	    		            text = addOtherImport(text, "org.hibernate.annotations.Type")
	                	} else if (f.conversion.contains("OjbCharBooleanFieldAIConversion")) {
	                		annotation += "@Type(type=\"rice_active_inactive\")\n\t"
		    		        text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiCharBooleanAIType")
		                } else if (f.conversion.contains("OjbKualiHashFieldConversion")) {
		                	annotation += "@Type(type=\"rice_hash\")\n\t"
		                	text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiHashType")
		                } else if (f.conversion.contains("OjbKualiEncryptDecryptFieldConversion")) {
		                	annotation += "@Type(type=\"rice_encrypt_decrypt\")\n\t"
		                	text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiEncryptDecryptUserType")
		                } else if (f.conversion.contains("OjbKualiDecimalFieldConversion")) {
		                	annotation += "@Type(type=\"rice_decimal\")\n\t"
		                	text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiDecimalFieldType")
		                } else if (f.conversion.contains("OjbDecimalKualiPercentFieldConversion")) {
		                	annotation += "@Type(type=\"rice_decimal_percent\")\n\t"
		                	text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiDecimalPercentFieldType")
		                } else if (f.conversion.contains("OjbDecimalPercentageFieldConversion")) {
		                	annotation += "@Type(type=\"rice_decimal_percentage\")\n\t"
		                	text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiDecimalPercentageFieldType")
		                } else if (f.conversion.contains("OjbKualiIntegerFieldConversion")) {
		                	annotation += "@Type(type=\"rice_integer\")\n\t"
		                	text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiIntegerFieldType")
		                } else if (f.conversion.contains("OjbKualiPercentFieldConversion")) {
		                	annotation += "@Type(type=\"rice_integer_percent\")\n\t"
		                	text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiIntegerPercentFieldType")
		                } else if (f.conversion.contains("OjbKualiIntegerPercentageFieldConversion")) {
		                	annotation += "@Type(type=\"rice_integer_percentage\")\n\t"
		                	text = addOtherImport(text, "org.kuali.rice.krad.util.HibernateKualiIntegerPercentageFieldType")
		                } else {
	                		println "UNHANDLED CONVERSION FOUND "+f.column
	                		println "conversion="+f.conversion
	                		println "name="+f.name
	                	}
	                }
	                //if (f.jdbcType?.equalsIgnoreCase("timestamp")) {
	                //    annotation += "@Temporal(TemporalType.TIMESTAMP)\n\t"
	                //    text = addImport(text, "Temporal")                    
	                //    text = addImport(text, "TemporalType")
	                //}
	                if (f.nullable) nullable = ", nullable=${f.nullable}"
	                annotation += "@Column(name=\"${f.column}\"${nullable})"
	                text = addImport(text, "Column")                    
	                text = annotate(text, "(private|protected).*(\\b${f.name})((\\s)*|(\\s)+.*);", annotation)
	        }
			try{
	        c.referenceDescriptors.values().each {
	            rd ->
	                def annotation = ""
	                annotation += determineOneOrManyToOne(classes, c, rd, logger)
	                text = addImport(text, annotation[1..-1])
	                def cascade = determineCascadeTypes(rd)
                    def readOnlyFK = ", insertable=false, updatable=false"
	                
	                annotation += "(${determineFetchType(rd)}"
	                text = addImport(text, "FetchType")
	                if (cascade) annotation += ", ${cascade}" 
	                annotation += ")\n\t"
	                def size = rd.foreignKeys.size()
	                if (size > 1) {
	                    annotation += "@JoinColumns({"
	                    text = addImport(text, "JoinColumns")
	                }
	                rd.foreignKeys.eachWithIndex {
	                    fk, i ->
	                        def fkColumn
	                        fk.fieldRef ? (fkColumn = c.fields[fk.fieldRef].column) : (fkColumn = findFieldIdRef(c.fields, fk.fieldIdRef).column)
	                        annotation += "@JoinColumn(name=\"${fkColumn}\""
	                        if (matchColumn(fkColumn, c.fields)) annotation += readOnlyFK
	                        annotation += ")"
	                        text = addImport(text, "JoinColumn")
	                        if (i < size - 1) annotation += ", "
	                }
	                if (size > 1) annotation += "})"
	                text = annotate(text, "(private|protected).*(\\b${rd.name})(\\s)*;", annotation)
	        }
				}

		catch(Exception e){
			
			println(e.getMessage());
		}
	        c.collectionDescriptors.values().each {
	            cd ->
	                def annotation = ""
	                def error
	                def annotationName = determineOneOrManyToMany(classes, c, cd)
	                text = addImport(text, annotationName[1..-1])
	                def cascade = determineCascadeTypes(cd)
	                if (cd.manyToMany) {
	                    annotation += annotationName
	                    annotation += "(${determineFetchType(cd)}"
	                    if (cascade) annotation += ", ${cascade}" 
	                    annotation += ")"
	                    annotation += "@JoinTable(name=\"${cd.indirectionTable}\", \n\t"
	                    annotation += "           joinColumns=@JoinColumn(name=\"${cd.fkPointingToThisClassColumn[0]}\"), \n\t"
	                    annotation += "           inverseJoinColumns=@JoinColumn(name=\"${cd.fkPointingToElementClassColumn[0]}\"))"
	                    text = addImport(text, "JoinTable")
	                    text = addImport(text, "JoinColumn")
	                    text = addImport(text, "FetchType")
	                } else {
	                    def mappedBy
	                    def keys = []
	                    def columns = []
	                    def ifks = []
	                    cd.inverseForeignKeys.each {
	                        ifk ->
	                            def f
	                            ifk.fieldRef ? (f = c.fields[ifk.fieldRef]) : (f = findFieldIdRef(c.fields, ifk.fieldIdRef))
	                            if (f?.name) keys.add(f.name)
	                            if (f?.column) columns.add(f.column)
	                            ifks.add(ifk.fieldRef)
	                    }
	                    def rdClass = classes[cd.elementClassRef]
	                    if (!rdClass) {                         
	                        error = "When determining mappedBy, class descriptor for [${cd.elementClassRef}] does not have a table defined. Please check it."
	                    } else {
		                    rdClass.referenceDescriptors.values().each {
		                        rd -> 
		                            def rdKeys = []
		                            rd.foreignKeys.each {
		                                fk ->
		                                    def fkName
		                                    fk.fieldRef ? (fkName = rdClass.fields[fk.fieldRef]?.name) : (fkName = findFieldIdRef(rdClass.fields, fk.fieldIdRef)?.name)
		                                    rdKeys.add(fkName)
		                            }
		                            if (!keys.isEmpty() && keys.containsAll(rdKeys) && rdKeys.containsAll(keys)) {
		                                mappedBy = rd.name
		                            } else if (!ifks.isEmpty() && ifks.containsAll(rdKeys) && rdKeys.containsAll(ifks)) {
	                                    mappedBy = rd.name	                                
		                            }
		                    }
		                    if (!mappedBy) {
		                        mappedBy = "ERROR: See log"
		                        error = "Uni-directional one-to-manys not yet supported by JPA.  Please add a reference back to ${cd.name} in ${rdClass.className}"
		                    }
	                    }
	                    annotation += annotationName
	                    def comma = false
	                    if (determineFetchType(cd) == "FetchType.LAZY") {
	                        annotation += "(${determineFetchType(cd)}"
	                        comma = true
	                    } else {
	                        annotation += "("
	                    }
	                    if (cascade) {
	                        if (comma) {
	                            annotation += ", "
	                        }
	                        annotation += "${cascade}"
	                        comma = true
	                    }
                        if (comma) {
                            annotation += ", "
                        }
	                    annotation += "\n           targetEntity=${cd.elementClassRef}.class"
	                    if (mappedBy) annotation += ", mappedBy=\"${mappedBy}\""
	                    annotation += ")"
	                    text = addImport(text, "FetchType")
	                }
	                text = annotate(text, "(private|protected).*(\\b${cd.name})(\\s)*;", annotation)
	                if (error) logger.log error                                
	        }        
	
	        text = cleanText(text)
	        
	        if (c.compoundPrimaryKey) {
	            c.pkClassIdText += """
${cpkFieldText}
    public ${cpkClassName}() {}

${cpkGetterText} 

}
"""
	        }
	        
	        if (!dry) {            
                if (!pkClassesOnly) {
		            if (javaFile.exists()) {
		                javaFile.delete()
		            }
		            javaFile << text
		            javaFile << "\n"
                } else {
                    if (c.compoundPrimaryKey) {
	                    def cpkFile = new File(c.cpkFilename)
	                    if (cpkFile.exists()) {
	                        cpkFile.delete()
	                    }
	                    cpkFile << c.pkClassIdText
	                    cpkFile << "\n"
                    }
                }
	        }
	        
	        if (dry || verbose) {
	            if (c.compoundPrimaryKey) {
	                println "${c.pkClassIdText}\n"
	            }
	            if (!pkClassesOnly) {
	                println "${text}\n\n"
	            }
	        }
	}
}

/*
This function add the given annotation before the given java statement in the java source file.
*/
def annotate(javaText, before, annotation) {
    def indent = ""
    if (before != "public class") indent = "\t"
    javaText = javaText.replaceFirst(before, annotation + "\n${indent}\$0")
    javaText 
}

/*
This function cleans up the java source a bit.
*/
def cleanText(javaText) {
    javaText = javaText.replaceFirst("package.*;", "\$0\n")
    javaText 
}

/*
This function adds the import statement to the java source file.
*/
def addImport(javaText, importText) {
    importText = "import javax.persistence.${importText};"
    if (!javaText.contains(importText)) {
        javaText = javaText.replaceFirst("package.*;", "\$0\n" + importText)
    }
    javaText 
}

def addOtherImport(javaText, importText) {
    importText = "import ${importText};"
    if (!javaText.contains(importText)) {
        javaText = javaText.replaceFirst("package.*;", "\$0\n" + importText)
    }
    javaText 
}

/*
Given a reference descriptor or a class descriptor, return a JPA fetch types.
*/
def determineFetchType(descriptor) {
    def ret = "FetchType.EAGER"
    if (['true'].contains(descriptor.proxy)) ret = "FetchType.LAZY" 
    "fetch=${ret}"
}

/*
Given a reference descriptor or a class descriptor, return a comma separated list of JPA cascade types.
*/
def determineCascadeTypes(descriptor) {
    def ret = []
    if (['true', null, ''].contains(descriptor.autoRetrieve)) ret.add("CascadeType.PERSIST")
    if (['true', 'link', 'object'].contains(descriptor.autoDelete)) ret.add("CascadeType.REMOVE")
    if (['true', 'link', 'object'].contains(descriptor.autoUpdate)) ret.add("CascadeType.MERGE")
    ret.size > 0 ? ("cascade={${ret.join(', ')}}") : null
}

/*
This function will look at the collection descriptor and determine if it should be a 'one to many' or 
a 'many to many' relationship.
*/
def determineOneOrManyToMany(classes, parent, cd) {
    def ret = "@OneToMany"
    cd.manyToMany = false
    if (cd.indirectionTable) { ret = "@ManyToMany"; cd.manyToMany = true }
    ret
}

/*
This function will look find the class that the given reference descriptor references.    Then, it will iterate
over all of its collection descriptors and determine if any of their inverse foreign key set exactly matches
the foreign key set of the given reference descriptor.    If true, the relationship is 'many to one' otherwise it
is 'one to one'.
*/
def determineOneOrManyToOne(classes, parent, rd, logger) {
    def ret = "@OneToOne"
    def c = classes[rd.classRef]
    def anonymous = false
    rd.foreignKeys.each {
        fk ->
            def fkfield
            fk.fieldRef ? (fkfield = parent.fields[fk.fieldRef]) : (fkfield = findFieldIdRef(parent.fields, fk.fieldIdRef))
            if (fkfield.access == 'anonymous') {
                anonymous = true 
                logger.log "Found anonymous reference-descriptor... defaulting to One-To-One for [${c.className}]"
            }
    }                
    if (!anonymous) {    
        c.collectionDescriptors.values().each {
            cd ->
                def keys = []
                cd.inverseForeignKeys.each {
                    ifk ->
                        def ifkName
                        ifk.fieldRef ? (ifkName = parent.fields[ifk.fieldRef]?.name) : (ifkName = findFieldIdRef(parent.fields, ifk.fieldIdRef)?.name)
                        keys.add(ifkName)
                }                
                def fkName
                def rdKeys = []
                rd.foreignKeys.each {
                    fk ->
                        fk.fieldRef ? (fkName = parent.fields[fk.fieldRef].name) : (fkName = findFieldIdRef(parent.fields, fk.fieldIdRef).name)
                        rdKeys.add(fkName)
                }                
                if (keys.containsAll(rdKeys) && rdKeys.containsAll(keys)) ret = "@ManyToOne"
        }    
    }
    ret
}

/*
This function iterates over the given fields looking for a field with the given id.    If found, returns the 
field otherwise null.
*/
def findFieldIdRef(fields, fieldIdRef) {
    def ret
    fields.values().each {
        f -> if (f.id == fieldIdRef) ret = f 
    }
    ret
}

/*
This function checks to see if there is a field with the given column name and that the field 
is not an anonymous field. 
*/
def matchColumn(column, fields) {
    def ret = false
    fields.values().each {
        f -> 
        if (f.column.equalsIgnoreCase(column) && f.access != 'anonymous') ret = true 
    }
    ret
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

def echoMessage(repositories){
	
	println("*************starting***************");
	
	}

def loadMetaData(repositories, classes, logger){
	
	repositories.each {
		repository -> 
		println 'Parsing repository file: '+repository.toString()
		def xml = new XmlParser().parse(new File(repository))
		def classDescriptors = xml['class-descriptor']
		
		classDescriptors.each { 
			cd -> 
			def classDescriptor = new ClassDescriptor()
			if (!cd.'@table') {
				logger.log "Class descriptor for [${cd.'@class'}] does not have a table defined. Please check it." 
				return
			}
			classDescriptor.tableName = cd.'@table'.toUpperCase()	
			classDescriptor.className = cd.'@class'
			
			def fieldDescriptors = cd['field-descriptor']
			fieldDescriptors.each {
				fd ->
				def field = new Field()
				field.id = fd.'@id'
				field.name = fd.'@name'
				field.column = fd.'@column'
				field.jdbcType = fd.'@jdbc-type'
				field.primarykey = (fd.'@primarykey' == "true")
				field.nullable = fd.'@nullable'
				field.indexed = fd.'@indexed'
				field.autoincrement = (fd.'@autoincrement' == "true")
				field.sequenceName = fd.'@sequence-name'
				field.locking = fd.'@locking'
				field.conversion = fd.'@conversion' 
				field.access = fd.'@access'
				classDescriptor.fields[field.name] = field
				if (field.primarykey) {
					classDescriptor.primaryKeys.add(field)
				}
			}
			
			def referenceDescriptors = cd['reference-descriptor']
			referenceDescriptors.each {
				rd ->
				def referenceDescriptor = new ReferenceDescriptor()
				referenceDescriptor.name = rd.'@name'
				referenceDescriptor.classRef = rd.'@class-ref'
				referenceDescriptor.proxy = rd.'@proxy'
				referenceDescriptor.autoRetrieve = rd.'@auto-retrieve'
				referenceDescriptor.autoUpdate = rd.'@auto-update'
				referenceDescriptor.autoDelete = rd.'@auto-delete'
				
				def foreignKeys = rd['foreignkey']
				foreignKeys.each {
					fk ->
					def key = new Key()
					key.fieldRef = fk.'@field-ref'
					key.fieldIdRef = fk.'@field-id-ref'
					referenceDescriptor.foreignKeys.add(key)
				}
				
				classDescriptor.referenceDescriptors[referenceDescriptor.name] = referenceDescriptor
			} 
			
			def collectionDescriptors = cd['collection-descriptor']
			collectionDescriptors.each {
				colDesc ->
				def collectionDescriptor = new CollectionDescriptor()
				collectionDescriptor.name = colDesc.'@name'
				collectionDescriptor.collectionClass = colDesc.'@collection-class'
				collectionDescriptor.elementClassRef = colDesc.'@element-class-ref'
				collectionDescriptor.orderBy = colDesc.'@orderBy'
				collectionDescriptor.sort = colDesc.'@sort'
				collectionDescriptor.indirectionTable = colDesc.'@indirection-table'
				collectionDescriptor.proxy = colDesc.'@proxy'
				collectionDescriptor.autoRetrieve = colDesc.'@auto-retrieve'
				collectionDescriptor.autoUpdate = colDesc.'@auto-update'
				collectionDescriptor.autoDelete = colDesc.'@auto-delete'
				collectionDescriptor.fkPointingToThisClassColumn = colDesc['fk-pointing-to-this-class'].'@column'
				collectionDescriptor.fkPointingToElementClassColumn = colDesc['fk-pointing-to-element-class'].'@column'
				
				def inverseForeignKeys = colDesc['inverse-foreignkey']
				inverseForeignKeys.each {
					ifk ->
					def key = new Key()
					key.fieldRef = ifk.'@field-ref'
					key.fieldIdRef = ifk.'@field-id-ref'
					collectionDescriptor.inverseForeignKeys.add(key)
				}
				
				classDescriptor.collectionDescriptors[collectionDescriptor.name] = collectionDescriptor
			}                             
			classDescriptor.compoundPrimaryKey = (classDescriptor.primaryKeys.size > 1)
			classes[classDescriptor.className] = classDescriptor
		} 
	}
}

def generatePersistenceXML(classes, persistenceUnitName, persistenceXmlFilename, path) {

	def classesXml = ""
	classes.values().each {
		c ->     
		classesXml += "    <class>${c.className}</class>\n"
	}
	
	def persistXml = """<?xml version="1.0" encoding="UTF-8"?>
			
	<persistence 
	    version=\"1.0\" 
	    xmlns=\"http://java.sun.com/xml/ns/persistence\" 
	    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" 
	    xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd\">
	  
	  <persistence-unit name=\"${persistenceUnitName}\" transaction-type=\"RESOURCE_LOCAL\">
	${classesXml}  </persistence-unit>
	
	</persistence>
	"""
	
	generateFile(path+GlobalVariables.persistenceXmlFilename, persistXml);
	
}

def generateMySQLSequence(classes, schemaName){
	
	def orm = ""
	def sequences = []
	def scriptText = ""
	def ormText = ""

	  classes.values().each {
	      c ->     
	          c.fields.values().each {
	              f ->
	                  if (f.autoincrement && !sequences.contains(f.sequenceName)) {
	
	 scriptText += """CREATE TABLE ${f.sequenceName} (
		a INT NOT NULL AUTO_INCREMENT,
		PRIMARY KEY (a)
		) AUTO_INCREMENT=1000, ENGINE=MyISAM
		/
		"""
	                      sequences.add(f.sequenceName)
	                  }
	                  if (f.autoincrement) {
	                      def name = c.className[c.className.lastIndexOf('.')+1 .. -1]
	orm += """    <entity class=\"${c.className}\" name=\"${name}\">
	      <attributes>
	          <id name=\"${f.name}\">
	              <column name=\"${f.column}\"/>
	              <generated-value strategy=\"IDENTITY\"/>
	          </id>
	      </attributes>
	  </entity>
	"""                        
	                  }
	          }
	  }
	generateFile(GlobalVariables.projHome + GlobalVariables.mysqlScriptFile,   scriptText);
	
	println """<?xml version=\"1.0\" encoding=\"UTF-8\"?>
	<entity-mappings version=\"1.0\" xmlns=\"http://java.sun.com/xml/ns/persistence/orm\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence/orm orm_1_0.xsd\">
	  <persistence-unit-metadata>
	      <persistence-unit-defaults>
	          <schema>${schemaName}</schema>
	      </persistence-unit-defaults>
	  </persistence-unit-metadata>
	${orm}</entity-mappings>
	"""
	
	
}

def cleanBackupFiles(classes, sourceDirectories, projHome, backupExtension){
	/*
	 Remove the backup.java files.
	 */
	classes.values().each {
		c ->     
		def backupFile
		def file
		sourceDirectories.each {
			dir -> 
			file = projHome + dir + c.className.replaceAll("\\.", "/") + ".java" + backupExtension
			if (new File(file).exists()) {
				backupFile = new File(file)
			}
		}
		
		if (!backupFile) {
			logger.log "${backupFile} does not exist.  Can remove it."
			return
		}
		
		if (backupFile.exists()) {
			backupFile.delete()
			if (verbose) println "Deleting ${file}"
		}
	}
}


def getRespositoryFiles(String projHome, resourceDir, ojbMappingPattern, ArrayList repositories){
    repositories.clear()

    // local helpers
    def addRepository = { File f -> 
            repositories.add( f.getPath() );
            }

    def dir = new File(projHome+resourceDir)

    println 'directoryName='+dir.getPath()
    println 'ojbMappingPattern='+ojbMappingPattern

    dir.eachFileMatch(ojbMappingPattern, addRepository)
    dir.eachDirRecurse { File myFile ->
        myFile.eachFileMatch(ojbMappingPattern, addRepository)
        }

}

def stripeModuleName(String s){
	
	String name = s.substring(s.lastIndexOf("-") + 1, s.lastIndexOf("."));
	
	System.out.println(name);
	}

def generateFile(path, text){
	
	def persistFile = new File(path);
	def backupFile = new File(path + '.backup');
	if (persistFile.exists()){
		if (backupFile.exists()){
			backupFile.delete();
		}	
		persistFile.renameTo(backupFile);
		persistFile.delete();
	}
	
	persistFile << text
	persistFile << "\n"
	
}

class GlobalVariables{
	
	public static pa1="hellow"
	
	public static projHome = '/java/projects/play/rice-1.1.0'
	public static srcRootDir = '/impl/src/'
	public static resourceDir = '/impl/src/main/resources/META-INF/'	
	
	public static mysqlScriptFile = '/scripts/upgrades/mysqlSequenceFix.sql'
	public static mysqlOrmFile = 'project-orm.xml'	
	
	public static persistenceUnitName = "rice"
	public static persistenceXmlFilename = 'persistence.xml'	
	
	public static schemaName = "RICE110DEV"

	public static ojbMappingPattern = ~/.*OJB.*repository.*xml/
	
	public static backupExtension = ".backup"
	public static logger = new Logger("errors.log")
	public static classes = [:]
	
}
