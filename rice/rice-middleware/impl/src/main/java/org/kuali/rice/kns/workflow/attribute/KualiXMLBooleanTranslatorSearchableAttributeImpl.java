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
package org.kuali.rice.kns.workflow.attribute;

import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.w3c.dom.Element;

/**
 * This is an XML KEW search attribute that can be used where the XML of the attribute has an xpath expression that returns a
 * boolean. This attribute takes that boolean expression and translates it into true and false values based on the
 * {@link #getValueForXPathTrueEvaluation()} and {@link #getValueForXPathFalseEvaluation()} method's return variables.
 * 
 * NOTE: This will not longer be necessary if the version of xPath being used is every upgrade to 2.x or higher
 */
public class KualiXMLBooleanTranslatorSearchableAttributeImpl extends KualiXmlSearchableAttributeImpl {
	private static final long serialVersionUID = -4627314389844574461L;

	public static final String VALUE_FOR_TRUE = "Yes";
	public static final String VALUE_FOR_FALSE = "No";



	/**
	 * This overriden method does the translation of the given xPath expression from the XML definition of the attribute and
	 * translates it into the true and false values based on the {@link #getValueForXPathTrueEvaluation()} and
	 * {@link #getValueForXPathFalseEvaluation()} method's return variables
	 */
	@Override
	public Element getConfigXML(ExtensionDefinition extensionDefinition) {
		String[] xpathElementsToInsert = new String[3];
		xpathElementsToInsert[0] = "concat( substring('" + getValueForXPathTrueEvaluation() + "', number(not(";
		xpathElementsToInsert[1] = "))*string-length('" + getValueForXPathTrueEvaluation() + "')+1), substring('" + getValueForXPathFalseEvaluation() + "', number(";
		xpathElementsToInsert[2] = ")*string-length('" + getValueForXPathFalseEvaluation() + "')+1))";
		Element root = super.getConfigXML(extensionDefinition);
		return new KualiXmlAttributeHelper().processConfigXML(root, xpathElementsToInsert);
	}

	public String getValueForXPathTrueEvaluation() {
		return VALUE_FOR_TRUE;
	}

	public String getValueForXPathFalseEvaluation() {
		return VALUE_FOR_FALSE;
	}

}
