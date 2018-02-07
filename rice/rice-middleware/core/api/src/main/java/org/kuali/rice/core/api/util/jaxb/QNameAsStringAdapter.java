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

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

/**
 * Marshall/unmarshall {@link QName} to and from a String value.  If you do
 * not use this adapter then you might end up with xml like the following:
 * 
 * <p>{@code
 * <serviceName xmlns:ns2="TEST">ns2:myService</serviceName>
 * }
 * 
 * <p>Instead of:
 * 
 * <p>{@code
 * <serviceName>{TEST}myService</serviceName>
 * }
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class QNameAsStringAdapter extends XmlAdapter<String, QName> {

	@Override
	public String marshal(QName qName) throws Exception {
		return qName == null ? null : qName.toString();
	}

	@Override
	public QName unmarshal(String qNameValue) throws Exception {
		return qNameValue == null ? null : QName.valueOf(qNameValue);
	}
}
