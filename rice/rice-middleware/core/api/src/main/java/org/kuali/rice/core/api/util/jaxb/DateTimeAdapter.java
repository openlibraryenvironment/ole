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
package org.kuali.rice.core.api.util.jaxb;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;

/**
 * Marshall/unmarshall a joda-time DateTime object.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DateTimeAdapter extends XmlAdapter<Calendar, DateTime> {

	@Override
	public Calendar marshal(DateTime dateTime) throws Exception {
		if (dateTime == null) {
		    return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateTime.getMillis());
		return calendar;
	}

	@Override
	public DateTime unmarshal(Calendar calendar) throws Exception {
		return calendar == null ? null : new DateTime(calendar.getTimeInMillis());
	}
	
}
