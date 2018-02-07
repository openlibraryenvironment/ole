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
package org.kuali.rice.kew.xml;

import org.apache.commons.lang.StringEscapeUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class handles the xml stack of elements and makes
 * it easier to run a transformation on certain elements.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public abstract class AbstractTransformationFilter extends XMLFilterImpl {

	
	// The list which helps keep track of where we are in the XML 
	// hierarchy as the stream is being processed
	private List<String> groupXmlStack = new ArrayList<String>();
	
	/**
	 * 
	 * This method allows you to modify the element passed in.  The returned element
	 * will be pushed into a "super.startElement(uri, localName, qName, atts)" call.
	 * 
	 * @param currentElement
	 * @return
	 */
	public abstract CurrentElement transformStartElement(CurrentElement currentElement) throws SAXException;
	
	/**
	 * 
	 * This method allows you to modify the element passed in.  The returned element
	 * will be pushed into a "super.endElement(uri, localName, qName" call.
	 * 
	 * @param currentElement
	 * @return
	 */
	public abstract CurrentElement transformEndElement(CurrentElement currentElement) throws SAXException;
		
	/*
	 * Build a Map that maps elements we intend to transform to their corresponding transformed value.
	 * The keys in this Map are "hierarchically-qualified" representations of the elements of concern.
	 * 
	 * For example, if "group" is a child of "groups", which is in turn a child of the root
	 * element "data", then it is represented as "data.groups.group" in the Map.
	 */
	public abstract List<KeyValue> getElementTransformationList();	

	/**
	 * 
	 * This method returns the element that we should start transforming at.
	 * So, if we had:
	 * <data>
	 *   <groups>
	 *     <group>
	 *     
	 * We might want to start transforming at the group level. 
	 * In that case the startingElement = "group"
	 * 
	 * @return
	 */
	public abstract String getStartingElementPath();
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		// Push the element onto the stack
		if (groupXmlStack.isEmpty()){			
				groupXmlStack.add(localName);					
		}
		else {
			// Push a child element by appending localName to the value of the top element in the stack
			groupXmlStack.add(groupXmlStack.get(groupXmlStack.size()-1) + "." + localName);
		}
		
		// Fetch the current element from the top of the stack
		String currentElementKey = groupXmlStack.get(groupXmlStack.size()-1);
		CurrentElement currentElement = new CurrentElement(currentElementKey,uri, localName, qName, atts);
				
		// Transform elements of concern:
		if (getElementTransformationList().contains(new ConcreteKeyValue(getTrimmedCurrentElementKey(currentElementKey), uri))){
			CurrentElement transformedElement = this.transformStartElement(currentElement);			
			super.startElement(transformedElement.getUri(), transformedElement.getLocalName(), transformedElement.getqName(), transformedElement.getAttributes());
		}
		else {
			// Pass other elements through as they are
			super.startElement(uri, localName, qName, atts);
		}
    }
		
	protected String getTrimmedCurrentElementKey(String currentElementKey){
		return currentElementKey.replaceFirst(StringEscapeUtils.escapeJava(this.getStartingElementPath()+"."), "");
	}
    @Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// Fetch the current element from the top of the stack
		String currentElementKey = groupXmlStack.get(groupXmlStack.size()-1);
		CurrentElement currentElement = new CurrentElement(currentElementKey,uri, localName, qName);
		
		if (getElementTransformationList().contains(new ConcreteKeyValue(getTrimmedCurrentElementKey(currentElementKey), uri))){
			CurrentElement transformedElement = this.transformEndElement(currentElement);			
			super.endElement(transformedElement.getUri(), transformedElement.getLocalName(), transformedElement.getqName());
		}		
		else {
			// Pass other elements through as they are
			super.endElement(uri, localName, qName);
		}
		
		// Pop the element from the stack if it's not empty
		if (!groupXmlStack.isEmpty()){
			groupXmlStack.remove(currentElementKey);			
		}
    }

	public class CurrentElement {
		String nameKey;
		String uri;
		String localName; 
		String qName; 
		Attributes attributes;
		
		public CurrentElement(){}
				
		public CurrentElement(String nameKey, String uri, String localName,
				String qName) {
			super();
			this.nameKey = nameKey;
			this.uri = uri;
			this.localName = localName;
			this.qName = qName;
		}

		public CurrentElement(String nameKey,String uri, String localName, String qName,
				Attributes attributes) {
			super();
			this.nameKey = nameKey;
			this.uri = uri;
			this.localName = localName;
			this.qName = qName;
			this.attributes = attributes;
		}


		public String getUri() {
			return this.uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		public String getLocalName() {
			return this.localName;
		}
		public void setLocalName(String localName) {
			this.localName = localName;
		}
		public String getqName() {
			return this.qName;
		}
		public void setqName(String qName) {
			this.qName = qName;
		}
		public Attributes getAttributes() {
			return this.attributes;
		}
		public void setAttributes(Attributes attributes) {
			this.attributes = attributes;
		}

		public String getNameKey() {
			return this.nameKey;
		}

		public void setNameKey(String nameKey) {
			this.nameKey = nameKey;
		}		
	}
	
}
