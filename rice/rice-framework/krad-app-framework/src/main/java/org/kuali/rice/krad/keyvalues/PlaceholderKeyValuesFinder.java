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

import org.kuali.rice.core.api.util.KeyValue;

import java.util.Collections;
import java.util.List;

/**
 * KeyValuesFinder that has no values and serves only as a placeholder in DD
 * when dynamically populating field values programmatically.
 */
public class PlaceholderKeyValuesFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        return Collections.emptyList();
    }
}