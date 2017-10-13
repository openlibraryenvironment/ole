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
package org.kuali.rice.krad.uif.component;

/**
 * Extends <code>Ordered</code> interface to add setter for the order property
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Ordered extends org.springframework.core.Ordered {
	public static final int INITIAL_ORDER_VALUE = 0;

	/**
	 * Setter for the order value
	 *
	 * @param order
	 * @see org.springframework.core.Ordered.getOrder()
	 */
	public void setOrder(int order);
}
