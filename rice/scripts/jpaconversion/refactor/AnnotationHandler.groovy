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
			def cpkConstructorArgs = ""
			def cpkConstructorBody = ""
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
				c.pkClassIdText += handle_pkClass_Info(packageName, cpkClassName)
	        }
	        
	        classAnnotation += "@Entity\n@Table(name=\"${c.tableName}\")"
	        text = addImport(text, "Entity")
	        text = addImport(text, "Table")
	        text = addImport(text, "CascadeType")                    
	        text = annotate(text, "public class", classAnnotation)
	        c.fields.values().each {
	            f ->
					//println("**********************PK\t" + f.name + f.primarykey);
	                def annotation = ""
	                def nullable = '' 
	                if (f.primarykey) {
	                    annotation += "@Id\n\t"
	                    text = addImport(text, "Id")
	                    if (c.compoundPrimaryKey) {
	                        def temp = new String(text).trim()
	                        temp = temp.replaceFirst("(private|protected).*(\\b${f.name})(\\s)*;", "ABCD1\$0ABCD2")
	                        if (temp.indexOf('ABCD1') > -1 && temp.indexOf('ABCD2') > -1) {
	                            temp = temp[temp.indexOf('ABCD1') + 5 .. -1]
	                            temp = temp[0 .. temp.indexOf('ABCD2') - 1]
                                cpkFieldText += "\t@Column(name=\"${f.column}\")\n"
	                            cpkFieldText += "\t\t${temp.trim()}\n\t"
	                            temp = temp[temp.indexOf(' ') .. -1].trim()
	                            temp = temp[0 .. temp.indexOf(' ')].trim()
	                            def temp2 = f.name[0].toUpperCase() + f.name[1 .. -1]
								cpkConstructorArgs += "${temp} ${temp2}," 
								cpkConstructorBody += "${f.name} = ${temp2};\n\t\t"
	                            cpkGetterText += "\tpublic ${temp} get${temp2}() { return ${f.name}; }\n\t"
	                        }
	                    }   
						//println("*****************text \n" + cpkFieldText)
	                }
	                if(f.sequenceName){
						annotation += "@GeneratedValue(generator=\"${f.sequenceName}\")\n\t"
						annotation += "@GenericGenerator(name=\"${f.sequenceName}\", strategy=\"org.hibernate.id.enhanced.SequenceStyleGenerator\", " + 
								"\n\t\t parameters={@Parameter(name=\"sequence_name\",value=\"${f.sequenceName}\"), "  +
								"\n\t\t\t\t @Parameter(name=\"value_column\",value=\"id\"))\n\t"
						text = addImport(text,"GeneratedValue")
						text = addImportHibernate(text,"GenericGenerator")
						text = addImportHibernate(text,"Parameter")
						
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
	                if (f.conversion?.toString()?.size() > 0){
	                	GlobalVariables.type_handler.handleTypes(f.conversion, annotation, text)
	                }
	                if (f.nullable) nullable = ", nullable=${f.nullable}"
	                annotation += "@Column(name=\"${f.column}\"${nullable})"
	                text = addImport(text, "Column")                    
	                text = annotate(text, "(private|protected).*(\\b${f.name})((\\s)*|(\\s)+.*);", annotation)
	        }
			try{
	        c.referenceDescriptors.values().each {
	            rd ->
					//if(rd != null)
					//	print "\n***********CLASSREFS************\t" + rd.classRef
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
					//def class_ref = rd.classRef
	                rd.foreignKeys.eachWithIndex {
	                    fk, i ->
	                    println "\n***********CLASSREFS************\t" + rd.classRef + "\treferencing ----\t" + fk.fieldRef
	                        def fkColumn
	                        fk.fieldRef ? (fkColumn = c.fields[fk.fieldRef].column) : (fkColumn = findFieldIdRef(c.fields, fk.fieldIdRef).column)
							if(size > 1)
							{
								def ref_colmn = findReferencedColumnName(classes, rd.classRef.toString(), fk.fieldRef.toString())
								annotation += "@JoinColumn(name=\"${fkColumn}\", referencedColumnName=\"${fkColumn}\""
							}
							else
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
				c.pkClassIdText += handle_pkClass(cpkClassName, cpkFieldText, cpkGetterText, cpkConstructorArgs, cpkConstructorBody)
				
	        }
	        
	        if (!dry) {            
                if (!pkClassesOnly) {
					c.pkClassIdText = ""
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

def addImportHibernate(javaText, importText) {
	importText = "import org.hibernate.annotations.${importText};"
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
	else ret = ['true', null, ''].contains(descriptor.autoRetrieve)? "FetchType.EAGER":"FetchType.LAZY" 
    "fetch=${ret}"
}

/*
Given a reference descriptor or a class descriptor, return a comma separated list of JPA cascade types.
*/
def determineCascadeTypes(descriptor) {
	def DEFAULTS = "CascadeType.REFRESH, CascadeType.DETACH"
    def ret = []
	ret.add(DEFAULTS);
	
    //if (['true', null, ''].contains(descriptor.autoRetrieve)) ret.add("CascadeType.PERSIST")
    if (['true', 'object'].contains(descriptor.autoDelete)) ret.add("CascadeType.REMOVE")
    if (['true', 'object'].contains(descriptor.autoUpdate)) ret.add("CascadeType.MERGE, CascadeType.PERSIST")
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

def handle_pkClass_Info(packageName, cpkClassName){
	def  text = """/*
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
	
	text
}


def handle_pkClass(cpkClassName, cpkFieldText, cpkGetterText, cpkConstructorArgs, cpkConstructorBody){
	
	def ret = """
	${cpkFieldText}
		public ${cpkClassName}() {}
	${handle_pkClass_constructor(cpkClassName, cpkConstructorArgs, cpkConstructorBody)}
	${cpkGetterText} 
	}
	"""
	
	ret
}

def handle_pkClass_defaultConstructor(cpkClassName, cpkFieldText, cpkGetterText){
	
	def ret = """
	${cpkFieldText}
		    public ${cpkClassName}() {}
	
	${cpkGetterText} 
	
	}
	"""
	
	ret
}
def handle_pkClass_constructor(cpkClassName, cpkConstructorArgs, cpkConstructorBody){
	
	def ret = ""
	if(cpkConstructorArgs.length() > 0){
		def args = cpkConstructorArgs.substring(0, cpkConstructorArgs.length() - 1 );
		ret += "\tpublic ${cpkClassName}(${args})\n\t\t{\n\t\t${cpkConstructorBody}}"
	}
	//println("result**********\n" + ret);
	ret
}

def findReferencedColumnName(java.util.LinkedHashMap classes, String classRef, String fieldRef){
	println "***********looking for ref column**************\t" + classRef + "\t" + fieldRef
	def ret = ""
	try{
		classes.values().each {
			this_class -> 
			if(this_class.className.equals(classRef)){
				Field fd = this_class.fields.get(fieldRef)
				if(fd != null)
					ret = fd.column	
			}
		}
	}
	catch(Exception e){
		println("exception------------" + e.getMessage())
	}
	println "***********got ref column**************\t" + ret
	
	ret
}