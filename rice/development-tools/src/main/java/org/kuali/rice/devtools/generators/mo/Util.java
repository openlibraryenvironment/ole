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
package org.kuali.rice.devtools.generators.mo;

import java.util.Arrays;
import java.util.List;

/**
 * A class with general utility constants and methods used by the Model Object generator.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
final class Util {
	
	static final String VERSION_NUMBER_FIELD = "versionNumber";
	static final String OBJECT_ID_FIELD = "objectId";
	
	static final String CONSTANTS_CLASS_NAME = "Constants";
	static final String ROOT_ELEMENT_NAME_FIELD = "ROOT_ELEMENT_NAME";
	static final String TYPE_NAME_FIELD = "TYPE_NAME";
	static final String TYPE_NAME_SUFFIX = "Type";
	static final String HASH_CODE_EQUALS_EXCLUDE_FIELD = "HASH_CODE_EQUALS_EXCLUDE";
	static final String COMMON_ELEMENTS_CLASS = "CommonElements";
	static final String FUTURE_ELEMENTS_FIELD = "FUTURE_ELEMENTS";
	static final String CONSTANTS_CLASS_JAVADOC = "Defines some internal constants used on this class.";
	
	static final String ELEMENTS_CLASS_NAME = "Elements";
	static final String ELEMENTS_CLASS_JAVADOC = "A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.";
	static final List<String> COMMON_ELEMENTS = Arrays.asList(VERSION_NUMBER_FIELD, OBJECT_ID_FIELD);
	
	static final String BUILDER_CLASS_NAME = "Builder";
	
	static String toLowerCaseFirstLetter(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}
	
	static String toUpperCaseFirstLetter(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
	
	static String toConstantsVariable(String fieldName) {
		StringBuilder constantVariable = new StringBuilder();
		// just to be safe
		fieldName = toLowerCaseFirstLetter(fieldName);
		StringBuilder segAccum = new StringBuilder();
		for (char character : fieldName.toCharArray()) {
			if (Character.isUpperCase(character)) {
				constantVariable.append(segAccum.toString().toUpperCase());
				constantVariable.append("_");
				segAccum = new StringBuilder();
			}
			segAccum.append(character);
		}
		// do the last bit
		constantVariable.append(segAccum.toString().toUpperCase());
		return constantVariable.toString();
	}
	
	static String generateGetterName(String fieldName, boolean is) {
		return (is ? "is" : "get") + Util.toUpperCaseFirstLetter(fieldName);
	}
	
	static String generateGetter(String fieldName, boolean is) {
		return generateGetterName(fieldName, is) + "()";
	}
	
	static String generateSetterName(String fieldName) {
		return "set" + Util.toUpperCaseFirstLetter(fieldName);
	}
	
	static String generateSetter(String fieldName, String valueToSet) {
		return generateSetterName(fieldName) + "(" + valueToSet + ")";
	} 
	
	
	static boolean isCommonElement(String fieldName) {
		return COMMON_ELEMENTS.contains(fieldName);
	}
		
	static String generateBuilderJavadoc(String simpleClassName, String simpleContractClassName) {
		return "A builder which can be used to construct {@link " + simpleClassName + "} instances.  " +
			"Enforces the constraints of the {@link " + simpleContractClassName + "}.";
	}

}
