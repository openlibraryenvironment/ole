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
package org.kuali.rice.location.framework.campus;

import java.util.Comparator;

import org.kuali.rice.location.api.campus.CampusType;

public class CampusTypeComparator implements Comparator<CampusType> {

	private CampusTypeComparator() {
	}
	
	public static final Comparator<CampusType> INSTANCE = new CampusTypeComparator();
	
    @Override
	public int compare(CampusType o1, CampusType o2) {

        CampusType obj1 = o1;
        CampusType obj2 = o2;

        return obj1.getCode().compareTo(obj2.getCode());
    }

}
