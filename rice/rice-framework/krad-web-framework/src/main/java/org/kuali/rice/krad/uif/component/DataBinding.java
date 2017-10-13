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
 * Components that bind to a model (hold model data) should implement this
 * interface
 *
 * <p>
 * Provides access to the <code>BindingInfo</code> object for the component that
 * contains binding configuration
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DataBinding {

	/**
	 * Returns the <code>BindingInfo</code> instance that is configured for the
	 * component
	 *
	 * @return BindingInfo
	 * @see org.kuali.rice.krad.uif.component.BindingInfo
	 */
	public BindingInfo getBindingInfo();

    /**
     * Setter for the binding info instance
     *
     * @param bindingInfo
     */
    public void setBindingInfo(BindingInfo bindingInfo);

	/**
	 * Name of the property (relative to the parent object) the component binds
	 * to
	 *
	 * @return String property name
	 */
	public String getPropertyName();
}
