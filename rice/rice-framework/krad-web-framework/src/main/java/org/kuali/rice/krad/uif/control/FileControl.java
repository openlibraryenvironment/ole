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
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * Represents a HTML File control, generally rendered as an input control with
 * type 'file'. Allows user to upload a file to the application
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "fileControl-bean", parent = "Uif-FileControl")
public class FileControl extends ControlBase implements SizedControl {
    private static final long serialVersionUID = -5919326390841646189L;

    private int size;

    public FileControl() {
        super();
    }

    /**
     * @see org.kuali.rice.krad.uif.control.SizedControl#getSize()
     */
    @BeanTagAttribute(name = "size")
    public int getSize() {
        return this.size;
    }

    /**
     * @see org.kuali.rice.krad.uif.control.SizedControl#setSize(int)
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        FileControl fileControlCopy = (FileControl) component;
        fileControlCopy.setSize(this.size);
    }
}
