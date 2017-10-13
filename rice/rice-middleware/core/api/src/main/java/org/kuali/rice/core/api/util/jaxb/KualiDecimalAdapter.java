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

import org.kuali.rice.core.api.util.type.KualiDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Marshall/unmarshall a joda-time DateTime object.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiDecimalAdapter extends XmlAdapter<String, KualiDecimal> {

	@Override
	public String marshal(KualiDecimal decimal) throws Exception {
		return decimal == null ? null : decimal.toString();
	}

	@Override
	public KualiDecimal unmarshal(String decimal) throws Exception {
		return decimal == null ? null : new KualiDecimal(decimal);
	}
	
}
