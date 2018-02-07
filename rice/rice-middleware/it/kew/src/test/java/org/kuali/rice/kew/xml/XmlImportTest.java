/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.xml;

import org.junit.Test;
import org.kuali.rice.kew.test.KEWTestCase;


public class XmlImportTest extends KEWTestCase {

	/*public void testCanParseWhiteSpace() throws Exception {
		//XmlImportTest_white_space_test.xml contains white space that will make things choke if not 
		//properly handled
		this.loadXmlFile("XmlImportTest_white_space_test.xml");
		
	}

	public void testCantParseReturnChar() throws Exception {
		this.loadXmlFile("XmlImportTest_white_space_return_char_test.xml");
	}*/
	
	@Test public void testRuleTemplate() throws Exception{
		this.loadXmlFile("RuleTemplate_test_white_space.xml");		
		this.loadXmlFile("RuleTemplate_test_newline.xml");
	}
	
	@Test public void testDocumentType() throws Exception{
		this.loadXmlFile("DocumentType_white_space_test.xml");
		this.loadXmlFile("DocumentType_newline_test.xml");
	}
	
	@Test public void testRule() throws Exception{
		this.loadXmlFile("Rule_white_space_test.xml");
		this.loadXmlFile("Rule_newline_test.xml");
		
	}
	
}
