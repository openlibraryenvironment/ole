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
	
	new ConversionUtils().generateFile(path+persistenceXmlFilename, persistXml);
}