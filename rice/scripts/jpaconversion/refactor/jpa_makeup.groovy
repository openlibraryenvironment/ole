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
/************************************************************************************************
 * IF WANT TO RUN AGAINT MULTI ojb config file:
 * set resourceHome = '$resource_root' for your project
 * for example for rice: resourceHome = '/java/projects/play/rice-1.1.0/impl/src/main/resources
 * set the ojbMappingPattern to your project's ojb file name pattern
 **********************************************************************************************/

import java.util.ArrayList;

def ojbMappingPattern = ~/.*OJB.*repository.*xml/
def resourceHome = '/java/projects/play/rice-1.1.0/impl/src/main/resources/org/kuali/rice/krad'
def srcHome = '/java/projects/play/rice-1.1.0/impl/src/main/java'
def sourceDirectories = []
def repositories = []
def classes = []
def files = []

class GlobalRes{
	public static conversion_util = new ConversionUtils();
}


if(args.size() == 0)
{println("USAGE:\t>groovy ~/jpaconversion/jpa_makeup.groovy [CLEAN | TRANSIENT]");
	System.exit(0)
}

GlobalRes.conversion_util.getRespositoryFiles(resourceHome, ojbMappingPattern, repositories, sourceDirectories)

println 'Found '+repositories.size().toString()+' OJB mapping files:'

repositories.each {println it}

loadClasses(repositories, classes)

findExistingBOs(srcHome, classes, files )

if("CLEAN".equals(args[0]))
	removeAnnotatonLine(files)

else if("TRANSIENT".equals(args[0]))
	addTransient(files)


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

def loadClasses(repositories, classes){
	
	repositories.each {
		repository -> 
		println 'Parsing repository file: '+repository.toString()
		def xml = new XmlParser().parse(new File(repository))
		def classDescriptors = xml['class-descriptor']
		
		classDescriptors.each { 
			cd -> 
			//def classDescriptor = new ClassDescriptor()
			println("********get class:\t" + cd.'@class')
			classes.add(cd.'@class')
		}
	}
		
}

def findExistingBOs(srcHome, classes, files){
	classes.each{
		cls->   def file = srcHome + '/'+ cls.replaceAll("\\.", "/") + ".java"
		
		println("************find file:\t" + file)
		
		files.add(file)
		}
	}

def removeAnnotatonLine(files){
	
	def javaFile
	def backupFile
	
	files.each{
		file-> println("************working on file:\t" + file)
		if (new File(file).exists()) {
			javaFile = new File(file)
			backupFile = new File(file + '.backup')		
			def text = ""
			def skip = "false"
			//scan file by line or convert file to list of lines....
			javaFile.eachLine{
				line->
				
				def cur = line.toString().trim();
				
				if(skip.equals("true"))
				{
					cur = "@" + cur;
					println("checking this line:\t" + cur + "\t");
				}
				if(!cur.startsWith("@")){
					if(!skip.equals("true")){
						//println("******get this line*****\t" + line)
						text = text + "\n" + line.toString();
						}
					else{
						println("******skip this line by skip*****\t" + line);
						}
					skip = "false"
					}
				else{
					println("******skip this line by @*****\t" + line);
					if(cur.startsWith("@Override") || cur.startsWith("public"))
					{
						//need make a condition builder method to evaluate all exceptions from a list
						text = text + "\n" + line.toString();
						skip = "false"		
						}
					else if(cur.startsWith("@Transient"))
					{
						skip = "false"
						}
					
					else if(cur.endsWith(","))
					{
						skip = "true"
					}
					else {skip = "false"}
					}
			}
			GlobalRes.conversion_util.generateFile(file, text);
		}
	}
}

def addTransient(files){
	
	def javaFile
	def backupFile
	
	files.each{
		file-> println("************working on file:\t" + file)
		if (new File(file).exists()) {
			javaFile = new File(file)
			backupFile = new File(file + '.backup')		
			ArrayList lines = new File(file).readLines()
			def text = ""
			
			lines.each{line->
				println(line.toString())
				def cur = line.toString().trim()
				if((cur.endsWith(";") )&&(cur.startsWith("private") || cur.startsWith("protected")))
				{
					def pre = (lines.get(lines.indexOf(line) - 1)).toString().trim();
					if(!pre.startsWith("@") && !pre.endsWith(")") && !pre.endsWith(",")){
						text = text + "\n" + "@Transient";
						}
				}
				text = text + "\n" + line; 
				}
			generateFile(file, text);
		}
	}
	
	}







