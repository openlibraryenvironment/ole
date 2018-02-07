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
package org.kuali.rice.krms.api.engine;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

/**
 * Tests the {@link ExecutionOptions} class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class ExecutionOptionsTest {

	private ExecutionOptions options;
	
	@Before
	public void setUp() {
		options = new ExecutionOptions();
	}
	
	@Test
	public void testExecutionOptions() {
		assert options.getFlags().isEmpty();
		assert options.getOptions().isEmpty();
	}

	@Test
	public void testExecutionOptions_copyExecutionOptions() {
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true);
		options.setOption("myOption1", "myOption1Value");
		
		// now copy it
		ExecutionOptions executionOptions2 = new ExecutionOptions(options);
		def checkOptions2 = {
			assert executionOptions2.isFlagSet(ExecutionFlag.LOG_EXECUTION);
			assert executionOptions2.getFlag(ExecutionFlag.LOG_EXECUTION);
			assert executionOptions2.isOptionSet("myOption1");
			assert executionOptions2.getOption("myOption1") == "myOption1Value";
		}
		checkOptions2();
		
		// now try modifying original execution options and ensure it does not side-effect the new options
		options.setOption("newOption", "newOptionValue");
		options.setOption("myOption1", "newMyOption1Value");
		options.setFlag(ExecutionFlag.LOG_EXECUTION, false);
		options.setFlag(ExecutionFlag.CONTEXT_MUST_EXIST, true);
		
		// now check new options to make sure they weren't changed
		checkOptions2();
		assertFalse executionOptions2.isFlagSet(ExecutionFlag.CONTEXT_MUST_EXIST);
		assertFalse executionOptions2.isOptionSet("newOption");
	}
	
	@Test
	public void testExecutionOptions_copyExecutionOptionsNull() {
		assert options.getFlags().isEmpty();
		assert options.getOptions().isEmpty();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetFlag_null() {
		options.setFlag(null, true);
	}

	@Test
	public void testSetFlag() {
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true);
		assert options.isFlagSet(ExecutionFlag.LOG_EXECUTION);
		assert options.getFlag(ExecutionFlag.LOG_EXECUTION);
	}

	@Test
	public void testSetOption() {
		options.setOption("myOption", "myOptionValue");
		assert options.isOptionSet("myOption");
		assert "myOptionValue" == options.getOption("myOption");
		assert options.getOptions().size() == 1;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetOption_null() {
		options.setOption(null, "myOptionValue");
	}
	
	@Test
	public void testSetOption_nullValue() {
		options.setOption("myOption", null);
		assert options.isOptionSet("myOption");
		assertNull options.getOption("myOption");
		assert options.getOptions().size() == 1;
	}

	@Test
	public void testRemoveFlag() {
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true);
		assert options.isFlagSet(ExecutionFlag.LOG_EXECUTION);
		options.removeFlag(ExecutionFlag.LOG_EXECUTION);
		assertFalse options.isFlagSet(ExecutionFlag.LOG_EXECUTION);
		assert options.getFlags().isEmpty();
	}
	
	@Test
	public void testRemoveFlag_nonExistent() {
		// this flag should not be set, but should be a "no-op"
		options.removeFlag(ExecutionFlag.LOG_EXECUTION);
		assertFalse options.isFlagSet(ExecutionFlag.LOG_EXECUTION);
		assert options.getFlags().isEmpty();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRemoveFlag_null() {
		options.removeFlag(null);
	}

	@Test
	public void testRemoveOption() {
		options.setOption("option", "value");
		assert options.isOptionSet("option");
		options.removeOption("option");
		assertFalse options.isOptionSet("option");
		assert options.getOptions().isEmpty();
	}
	
	@Test
	public void testRemoveOption_nonExistent() {
		// this option should not be set, but should be a "no-op"
		options.removeOption("doesnotexist");
		assertFalse options.isOptionSet("doesnotexist");
		assert options.getOptions().isEmpty();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRemoveOption_null() {
		options.removeOption(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRemoveOption_emptyString() {
		options.removeOption("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRemoveOption_whitespace() {
		options.removeOption("  ");
	}

	@Test
	public void testGetFlag() {
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true);
		assert options.getFlag(ExecutionFlag.LOG_EXECUTION);
	}
	
	@Test
	public void testGetFlag_defaulted() {
		assert options.getFlag(ExecutionFlag.LOG_EXECUTION) == ExecutionFlag.LOG_EXECUTION.getDefaultValue();
		assert options.getFlag(ExecutionFlag.CONTEXT_MUST_EXIST) == ExecutionFlag.CONTEXT_MUST_EXIST.getDefaultValue();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetFlag_null() {
		options.getFlag(null);
	}

	@Test
	public void testGetOption() {
		options.setOption("myOption", "myValue");
		assert "myValue" == options.getOption("myOption");
	}

	@Test
	public void testGetOption_nonExistent() {
		assertNull options.getOption("doesnotexist");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetOption_null() {
		options.getOption(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetOption_emptyString() {
		options.getOption("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetOption_whitespace() {
		options.getOption(" ");
	}
		
	@Test
	public void testIsFlagSet() {
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true);
		assert options.isFlagSet(ExecutionFlag.LOG_EXECUTION);
	}
	
	@Test
	public void testIsFlagSet_nonExistent() {
		assertFalse options.isFlagSet(ExecutionFlag.CONTEXT_MUST_EXIST);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIsFlagSet_null() {
		options.isFlagSet(null);
	}

	@Test
	public void testIsOptionSet() {
		options.setOption("myOption", "myValue");
		assert options.isOptionSet("myOption");
	}

	@Test
	public void testIsOptionSet_nonExistent() {
		assertFalse options.isOptionSet("doesnotexist");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsOptionSet_null() {
		options.isOptionSet(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsOptionSet_emptyString() {
		options.isOptionSet("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIsOptionSet_whitespace() {
		options.isOptionSet(" ");
	}

	@Test
	public void testGetFlags() {
		assert options.getFlags().isEmpty();
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true);
		Map<ExecutionFlag, Boolean> flags = options.getFlags();
		assertFalse flags.isEmpty();
		assert flags.size() == 1;
		assert flags.get(ExecutionFlag.LOG_EXECUTION);
	}
		
	@Test(expected=UnsupportedOperationException.class)
	public void testGetFlags_unmodifiable() {
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true);
		Map<ExecutionFlag, Boolean> flags = options.getFlags();
		assert flags.size() == 1;
		
		// now try to modify the map
		flags.remove(ExecutionFlag.LOG_EXECUTION);
	}

	/**
	 * Tests that the Map is immutable once it's returned, i.e. if
	 * the state of ExecutionOptions is mutated, the external map
	 * should also not be modified.
	 */
	@Test
	public void testGetFlags_immutable() {
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true);
		Map<ExecutionFlag, Boolean> flags = options.getFlags();
		assert flags.size() == 1;
		
		// now modify the execution options by removing the flag
		options.removeFlag(ExecutionFlag.LOG_EXECUTION);
		assert options.getFlags().isEmpty();
		
		// our existing flags shoudl not have been modified
		assert flags.size() == 1;
	}

	@Test
	public void testGetOptions() {
		assert options.getOptions().isEmpty();
		options.setOption("myOption", "myOptionValue");
		Map<String, String> optionsMap = options.getOptions();
		assertFalse optionsMap.isEmpty();
		assert optionsMap.size() == 1;
		assert optionsMap.get("myOption");
	}
		
	@Test(expected=UnsupportedOperationException.class)
	public void testGetOptions_unmodifiable() {
		options.setOption("myOption", "myOptionValue");
		Map<String, String> optionsMap = options.getOptions();
		assert optionsMap.size() == 1;
		
		// now try to modify the map
		optionsMap.remove("myOption");
	}

	/**
	 * Tests that the Map is immutable once it's returned, i.e. if
	 * the state of ExecutionOptions is mutated, the external map
	 * should also not be modified.
	 */
	@Test
	public void testGetOptions_immutable() {
		options.setOption("myOption", "myOptionValue");
		Map<String, String> optionsMap = options.getOptions();
		assert optionsMap.size() == 1;
		
		// now modify the execution options by removing the option
		options.removeOption("myOption");
		assert options.getOptions().isEmpty();
		
		// our existing options map should not have been modified
		assert optionsMap.size() == 1;
	}

	
	@Test
	public void testCallChaining() {
		options.setFlag(ExecutionFlag.LOG_EXECUTION, true).
			setOption("myOption1", "myOptionValue1").
			setFlag(ExecutionFlag.CONTEXT_MUST_EXIST, false).
			setOption("myOption2", "myOptionValue2").
			setFlag(ExecutionFlag.LOG_EXECUTION, false).
			removeOption("myOption1").
			removeFlag(ExecutionFlag.CONTEXT_MUST_EXIST).
			setFlag(ExecutionFlag.CONTEXT_MUST_EXIST, true);
		assert options.getFlags().size() == 2;
		assert options.getOptions().size() == 1;
		assertFalse options.getFlag(ExecutionFlag.LOG_EXECUTION);
		assert options.getFlag(ExecutionFlag.CONTEXT_MUST_EXIST);
		assertFalse options.isOptionSet("myOption1");
		assert options.getOption("myOption1") == null;
		assert options.getOption("myOption2") == "myOptionValue2";
	}

}
