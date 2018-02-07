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
package org.kuali.rice.krad.uif.widget;

import org.kuali.rice.krad.uif.component.Component;

/**
 * Components that provide a user interface function (besides the basic form
 * handing) should implement the widget interface
 *
 * <p>
 * Widgets generally provide a special function such as a calendar or navigation
 * element. The can render one or more <code>Field</code> instances and
 * generally have associated client side code
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Widget extends Component {

}
