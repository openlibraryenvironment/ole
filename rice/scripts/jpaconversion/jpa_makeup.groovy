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

//For Rice
def ojbMappingPattern = ~/.*OJB.*repository.*xml/
def projHome='/java/projects/rice-1.1.0'
//def resourceHome='/impl/src/main/resources/ //this will return all ojb config files
def resourceHome='/impl/src/main/resources/org/kuali/rice/'
def srcHome='/impl/src/main/java'
//def resourceHome = '/java/projects/play/rice-1.1.0/impl/src/main/resources/org/kuali/rice/kns'
//def srcHome = '/java/projects/play/rice-1.1.0/impl/src/main/java'
//For KFS test
//def ojbMappingPattern = ~/.*OJB|ojb.*-.*xml/
//def resourceHome = '/java/projects/kfs-jpa-ref/work/src/org/kuali/kfs/fp/'
//def srcHome = '/java/projects/kfs-jpa-ref/work/src'

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

GlobalRes.conversion_util.getOJBConfigFiles(projHome, resourceHome, ojbMappingPattern, repositories)//, sourceDirectories)

println 'Found '+repositories.size().toString()+' OJB mapping files:'

repositories.each {println it}

loadClasses(repositories, classes)

findExistingBOs(projHome + srcHome, classes, files)

if("CLEAN".equals(args[0]))
	removeAnnotatonLine(files)

else if("TRANSIENT".equals(args[0]))
	addTransient(files)

def loadClasses(repositories, classes){
	
	repositories.each {
		repository -> 
		println 'Parsing repository file: '+repository.toString()
		def xml = new XmlParser().parse(new File(repository))
		def classDescriptors = xml['class-descriptor']
		
		classDescriptors.each { 
			cd -> 
			//def classDescriptor = new ClassDescriptor()
			//println("********get class:\t" + cd.'@class')
			classes.add(cd.'@class')
		}
	}	
}

def findExistingBOs(srcDir, classes, files){
	classes.each{
		cls->   def file = srcDir + '/'+ cls.replaceAll("\\.", "/") + ".java"
		
		println("************find file:\t" + file)
		
		files.add(file)
		}
	}

def removeAnnotatonLine(files){
	
	def javaFile
	def backupFile
	
	files.each{
		file-> println("\n************working on file:\t" + file)
		if (new File(file).exists()) {
			javaFile = new File(file)
			backupFile = new File(file + '.backup')		
			def text = ""
			def append_next = false
			def append_text = ""
			def skip = "false"
			//scan file by line or convert file to list of lines....
			javaFile.eachLine{
				line->
				
				def cur = line.toString().trim();
				
				if(append_next){
					cur = append_text + cur
					}
				
				if(skip.equals("true"))
				{
					cur = "@" + cur;
					println("checking this line:\t" + cur + "\t");
				}
				if(!cur.startsWith("@") && !cur.startsWith("//@")){
					if(!skip.equals("true")){
						//println("******get this line*****\t" + line)
						text = text + "\n" + line.toString();
						}
					else{
						println("******skip this line by skip marker*****\t" + line);
						}
					skip = "false"
					}
				else{
					println("****** possible to skip this line by @ and //@*****\t" + line);
					if(cur.startsWith("@Override") || 
							cur.startsWith("@SuppressWarnings")|| 
							cur.startsWith("@Deprecated")||
							cur.startsWith("public"))
					{
						//need make a condition builder method to evaluate all exceptions from a list
						text = text + "\n" + line.toString();
						skip = "false"		
						}
					else if(cur.startsWith("@Transient"))
					{
						if(cur.length() > "@Transient".length() )
							text = text + "\n" + cur.substring("@Transient".length());
						skip = "false"
					}
					else if(cur.endsWith(","))
					{
						skip = "true"
					}
					else if(cur.contains("@GenericGenerator") || cur.contains("@JoinColumns") 
							|| cur.contains("@AttributeOverrides") || cur.contains("@NamedQueries"))
					{
						if(!cur.contains("})")){
							append_next = true;
							append_text = cur
							skip = "true"
						}
						else{
							skip = "false"
							append_next = false
							append_text = ''
						}
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
	def importText = "import javax.persistence.Transient;"
	
	files.each{
		file-> println("************working on file:\t" + file)
		if (new File(file).exists()) {
			javaFile = new File(file)
			backupFile = new File(file + '.backup')		
			ArrayList lines = new File(file).readLines()
			def text = ""
			
			lines.each{line->
				//println(line.toString())
				def cur = line.toString().trim()
				if(cur.find(~/;\s*\/\/\s*/)){
					println "+++++++++++++++found at " + cur
					cur = cur.substring(0, cur.indexOf("//"));
					cur = cur.trim();
					println "+++++++++++++++chopped at " + cur
				}
				if((cur.endsWith(";")) && (cur.startsWith("private") || cur.startsWith("protected")))
				{
					def pre = (lines.get(lines.indexOf(line) - 1)).toString().trim();
					if(!pre.startsWith("@") && !pre.endsWith(")") && !pre.endsWith(",") && !cur.contains(" static ")){
						text = text + "\n\t" + "@Transient";
						println("adding annotation @Transient to**********\n\t " + cur);
					}
				}
				text = text + "\n" + line; 
			}
			if (!text.contains(importText)) {
				text = text.replaceFirst("package.*;", "\$0\n" + importText)
			}
			GlobalRes.conversion_util.generateFile(file, text);
		}
	}
}

//need a function to delete the emplty lines before license, between package and import, and after the class impl
def removeEmptyLines(){
	
	}