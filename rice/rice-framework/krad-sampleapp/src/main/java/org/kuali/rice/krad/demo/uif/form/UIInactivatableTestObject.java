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
package org.kuali.rice.krad.demo.uif.form;

import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * Test data object that implements Inactivatable
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UIInactivatableTestObject extends UITestObject implements Inactivatable {
    private static final long serialVersionUID = 5546600234913206443L;

    private boolean active;

    public UIInactivatableTestObject() {
        super();
    }

    public UIInactivatableTestObject(String field1, String field2, String field3, String field4, boolean active) {
        super(field1, field2, field3, field4);

        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
