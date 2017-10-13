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
package org.kuali.rice.krad.uif.layout;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;

/**
 * List layout manager is a layout manager for group types to output their items as either ordered or
 * unordered lists
 */
@BeanTags({@BeanTag(name = "listLayout-bean", parent = "Uif-ListLayout"),
        @BeanTag(name = "orderedListLayout-bean", parent = "Uif-OrderedListLayout")})
public class ListLayoutManager extends LayoutManagerBase {

    private static final long serialVersionUID = -8611267646944565117L;
    private boolean orderedList = false;

    /**
     * If true, this list layout is an ordered list (ol).  Otherwise, the the layout is an unordered list (ul)
     *
     * @return true if orderedList, false if unordered
     */
    @BeanTagAttribute(name = "orderedList")
    public boolean isOrderedList() {
        return orderedList;
    }

    /**
     * Set whether or not this is an orderedList
     *
     * @param orderedList
     */
    public void setOrderedList(boolean orderedList) {
        this.orderedList = orderedList;
    }
}
