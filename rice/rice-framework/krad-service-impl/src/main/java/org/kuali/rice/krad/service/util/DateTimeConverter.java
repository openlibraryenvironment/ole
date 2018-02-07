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
/**
 * This is a utility converter class for use with XStream serializers. This was written to deal with an issue
 * resulting from the use of JODA-Time where unmarshalling of XML was failing.
 * This applies to only DateTime.class fields.
 */
package org.kuali.rice.krad.service.util;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DateTimeConverter implements SingleValueConverter {
    public boolean canConvert(Class clazz) {
        return clazz.equals(DateTime.class);
    }

    @Override
    public String toString(Object obj) {
        return obj.toString();
    }

    @Override
    public Object fromString(String value) {
        return ISODateTimeFormat.dateTimeParser().parseDateTime(value);
    }
}
