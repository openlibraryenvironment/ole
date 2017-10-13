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
package org.kuali.rice.kcb.test;

import org.kuali.rice.kcb.util.KCBConstants;

/**
 * Constants reflecting test data that is used in tests.
 * If tests or test data is updated, this file need to be updated
 * to be kept in sync.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class TestConstants {
    /**
     * Email deliverer property
     */
    public static final String EMAIL_DELIVERER_PROPERTY_VALUE = "kuali-kcb-testing@localhost.edu";
    /**
     * A valid deliverer name
     */
    public static final String VALID_DELIVERER_NAME = KCBConstants.MESSAGE_DELIVERY_TYPES.EMAIL_MESSAGE_DELIVERY_TYPE;
    /**
     * An invalid deliverer name 
     */
    public static final String NON_EXISTENT_DELIVERER_NAME = "BOGUS_DELIVERER";
    

	private TestConstants() {
		throw new UnsupportedOperationException("do not call");
	}
}
