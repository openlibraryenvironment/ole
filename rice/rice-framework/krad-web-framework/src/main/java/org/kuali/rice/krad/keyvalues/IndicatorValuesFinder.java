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
package org.kuali.rice.krad.keyvalues;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class returns list of boolean key value pairs.
 *
 *
 */
public class IndicatorValuesFinder extends KeyValuesBase {

	public static final IndicatorValuesFinder INSTANCE = new IndicatorValuesFinder();

	protected static final List<KeyValue> ACTIVE_LABELS;
	static {
		final List<KeyValue> activeLabels = new ArrayList<KeyValue>(3);
        activeLabels.add(new ConcreteKeyValue(KRADConstants.YES_INDICATOR_VALUE, "Yes"));
        activeLabels.add(new ConcreteKeyValue(KRADConstants.NO_INDICATOR_VALUE, "No"));
        activeLabels.add(new ConcreteKeyValue("", "Both"));

        ACTIVE_LABELS = Collections.unmodifiableList(activeLabels);
	}

    @Override
	public List<KeyValue> getKeyValues() {
        return ACTIVE_LABELS;
    }
}
