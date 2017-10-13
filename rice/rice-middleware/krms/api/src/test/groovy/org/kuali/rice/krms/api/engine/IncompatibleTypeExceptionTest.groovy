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
package org.kuali.rice.krms.api.engine

import org.junit.Test

/**
 * Tests the {@link IncompatibleTypeException} class. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class IncompatibleTypeExceptionTest {
		
	/**
	 * Tests the constructor that takes additionalMessage, value, and validTypes
	 */
	@Test
	void testIncompatibleTypeException_withAdditionalMessage() {
		String additionalMessage = "Houston, we have a problem."
		String value = "my value"
		Class[] validTypes = [ Boolean.class, Number.class ]
		def exception = new IncompatibleTypeException(additionalMessage, value, validTypes)
		assert value == exception.getValue()
		assert validTypes == exception.getValidTypes()
		assert "${additionalMessage} -> Type should have been one of [java.lang.Boolean, java.lang.Number] but was java.lang.String" == exception.getMessage() 		
	}
	
	@Test
	void testIncompatibleTypeException_nullValue() {
		String additionalMessage = "Houston, we have a problem."
		Class[] validTypes = [ Boolean.class, Number.class ]
		def exception = new IncompatibleTypeException(additionalMessage, null, validTypes)
		assert null == exception.getValue()
		assert validTypes == exception.getValidTypes()
		assert "${additionalMessage} -> Type should have been one of [java.lang.Boolean, java.lang.Number] but was null" == exception.getMessage()
	}
	
	@Test
	void testIncompatibleTypeException_nullValidTypes() {
		String additionalMessage = "Houston, we have a problem."
		String value = "my value"
		def exception = new IncompatibleTypeException(additionalMessage, value, null)
		assert value == exception.getValue()
		assert null == exception.getValidTypes()
		assert "${additionalMessage} -> Type was java.lang.String" == exception.getMessage() 		
	}
	
	@Test
	void testIncompatibleTypeException_emptyValidTypes() {
		String additionalMessage = "Houston, we have a problem."
		String value = "my value"
		def exception = new IncompatibleTypeException(additionalMessage, value, new Class[0])
		assert value == exception.getValue()
		assert new Class[0] == exception.getValidTypes()
		assert "${additionalMessage} -> Type was java.lang.String" == exception.getMessage()
	}
	
	@Test
	void testIncompatibleTypeException_emptyValidTypes_nullValue() {
		String additionalMessage = "Houston, we have a problem."
		def exception = new IncompatibleTypeException(additionalMessage, null, new Class[0])
		assert null == exception.getValue()
		assert new Class[0] == exception.getValidTypes()
		assert "${additionalMessage} -> Type was null" == exception.getMessage()
	}
	
	@Test
	void testIncompatibleTypeException_noAdditionalMessage() {
		String value = "my value"
		Class[] validTypes = [ Boolean.class, Number.class ]
		def assertException = { IncompatibleTypeException exceptionToAssert ->
			assert value == exceptionToAssert.getValue()
			assert validTypes == exceptionToAssert.getValidTypes()
			def expectedMessage = "Type should have been one of [java.lang.Boolean, java.lang.Number] but was java.lang.String"
			assert expectedMessage == exceptionToAssert.getMessage()
		}
		
		assertException new IncompatibleTypeException(value, validTypes)
		assertException new IncompatibleTypeException(null, value, validTypes)

	}
	
	@Test
	void testIncompatibleTypeException_noAdditionalMessage_nullValue() {
		Class[] validTypes = [ Boolean.class, Number.class ]
		def assertException = { IncompatibleTypeException exceptionToAssert ->
			assert null == exceptionToAssert.getValue()
			assert validTypes == exceptionToAssert.getValidTypes()
			def expectedMessage = "Type should have been one of [java.lang.Boolean, java.lang.Number] but was null"
			assert expectedMessage == exceptionToAssert.getMessage()
		}
		
		assertException new IncompatibleTypeException(null, validTypes)
		assertException new IncompatibleTypeException(null, null, validTypes)

	}
	
	@Test
	void testIncompatibleTypeException_noAdditionalMessage_nullValue_emptyValidTypes() {
		Class[] validTypes = []
		def assertException = { IncompatibleTypeException exceptionToAssert ->
			assert null == exceptionToAssert.getValue()
			assert validTypes == exceptionToAssert.getValidTypes()
			def expectedMessage = "Type was null"
			assert expectedMessage == exceptionToAssert.getMessage()
		}
		
		assertException new IncompatibleTypeException(null, validTypes)
		assertException new IncompatibleTypeException(null, null, validTypes)

	}
	
	@Test
	void testIncompatibleTypeException_noAdditionalMessage_nullValue_nullValidTypes() {
		def assertException = { IncompatibleTypeException exceptionToAssert ->
			assert null == exceptionToAssert.getValue()
			assert null == exceptionToAssert.getValidTypes()
			def expectedMessage = "Type was null"
			assert expectedMessage == exceptionToAssert.getMessage()
		}
		
		assertException new IncompatibleTypeException(null, null)
		assertException new IncompatibleTypeException(null, null, null)

	}
	
	@Test
	void testIncompatibleTypeException_noAdditionalMessage_nullValidTypes() {
		String value = "my value"
		def assertException = { IncompatibleTypeException exceptionToAssert ->
			assert value == exceptionToAssert.getValue()
			assert null == exceptionToAssert.getValidTypes()
			def expectedMessage = "Type was java.lang.String"
			assert expectedMessage == exceptionToAssert.getMessage()
		}
		
		assertException new IncompatibleTypeException(value, null)
		assertException new IncompatibleTypeException(null, value, null)

	}

}
