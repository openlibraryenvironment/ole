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
package org.kuali.rice.krad.uif.control;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;

/**
 * Represents a HTML Hidden control, generally rendered as an input control of
 * type 'hidden'. This is used to hold a value that the user will not see but
 * will be posted back with the form
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "hiddenControl-bean", parent = "Uif-HiddenControl")
public class HiddenControl extends ControlBase {
	private static final long serialVersionUID = -8428898303430923425L;

	public HiddenControl() {
       super();
	}

}
