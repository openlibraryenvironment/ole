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
package org.kuali.rice.krms.framework.engine;

import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;

// TODO: remove?  ComparableTermBasedProposition does this generically
public class StringTermBasedProposition extends ComparableTermBasedProposition<String> {

	public StringTermBasedProposition(ComparisonOperator operator, Term term, String expectedValue) {
		super(operator, term, expectedValue);
	}	

}
