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
package org.kuali.rice.krad.util.documentserializer;

import org.kuali.rice.krad.document.Document;

/**
 * This implementation of {@link PropertySerializabilityEvaluator} specifies that all properties of a document are serializable.
 *
 */
public class AlwaysTruePropertySerializibilityEvaluator extends PropertySerializabilityEvaluatorBase implements PropertySerializabilityEvaluator {

    /**
     * Does nothing, since we know everything's serializable
     */
	@Override
    public void initializeEvaluatorForDocument(Document document) {
    }

    /**
     * Trivially returns true
     *
     * @see org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator#isPropertySerializable(org.kuali.rice.krad.util.documentserializer.DocumentSerializationState, Object, java.lang.String, java.lang.Object)
     */
	@Override
    public boolean isPropertySerializable(SerializationState state, Object containingObject, String childPropertyName, Object childPropertyValue) {
        return true;
    }
}
