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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This is a test Content Handler to upgrade Group 1.0.2 XML to Group 1.0.3.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class TestGroupNamespaceURIHandler implements ContentHandler{

	private ContentHandler parent;
	
	public TestGroupNamespaceURIHandler(ContentHandler parent) {
	    this.parent = parent;
  	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		parent.startElement(uri, localName, qName, atts);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		parent.endElement(uri, localName, qName);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		parent.characters(ch, start, length);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		parent.endDocument();
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		parent.endPrefixMapping(prefix);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		parent.ignorableWhitespace(ch, start, length);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data)
			throws SAXException {
		parent.processingInstruction(target, data);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		parent.setDocumentLocator(locator);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
		parent.skippedEntity(name);
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		parent.startDocument();
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		parent.startPrefixMapping(prefix, uri);
	}
}
