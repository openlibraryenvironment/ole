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
def generateMySQLSequence(classes, schemaName, path, file1, file2){
	
	def orm = ""
	def sequences = []
	def scriptText = ""
	def ormText = ""
	def conversion_util = new ConversionUtils()
	
	def seqProcessed = 0;
	def ormEntry = 0;
	
	  classes.values().each {
	      c ->     
	          c.fields.values().each {
	              f ->
	                  if (f.autoincrement && !sequences.contains(f.sequenceName)) {
	                	  seqProcessed++
	 scriptText += """CREATE TABLE ${f.sequenceName} (
		a INT NOT NULL AUTO_INCREMENT,
		PRIMARY KEY (a)
		) AUTO_INCREMENT=1000, ENGINE=MyISAM
		/
		"""
	                      sequences.add(f.sequenceName)
	                  }
	                  if (f.autoincrement) {
						ormEntry++;
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
	
	println 'MySql script processed\t' + seqProcessed + '\tsequence'
	conversion_util.generateFile(path + file1,   scriptText);
	
	def orm_text = """<?xml version=\"1.0\" encoding=\"UTF-8\"?>
		<entity-mappings version=\"1.0\" xmlns=\"http://java.sun.com/xml/ns/persistence/orm\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence/orm orm_1_0.xsd\">
		  <persistence-unit-metadata>
		      <persistence-unit-defaults>
		          <schema>${schemaName}</schema>
		      </persistence-unit-defaults>
		  </persistence-unit-metadata>
		${orm}</entity-mappings>
		"""
	//println (orm_text)
	println 'MySql script processed\t' + ormEntry + '\tormEntries'
	conversion_util.generateFile(path + file2,  orm_text);
}
