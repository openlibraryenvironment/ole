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
package org.kuali.rice.krad.uif.element;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * ValidationMessages for logic and options specific to pages
 * TODO this class is currently a placeholder for possible future functionality (ex. flatValidationMessages)
 */
@BeanTag(name = "pageValidationMessages-bean", parent = "Uif-PageValidationMessages")
public class PageValidationMessages extends GroupValidationMessages {

    private boolean showPageSummaryHeader;

    /**
     * If true, shows the page summary header (message count header message in the message block).  Otherwise, this
     * header is not rendered.
     *
     * @return true if the header will show, false otherwise
     */
    @BeanTagAttribute(name = "showPageSummaryHeader")
    public boolean isShowPageSummaryHeader() {
        return showPageSummaryHeader;
    }

    /**
     * Set the page summary header to show or not show.
     *
     * @param showPageSummaryHeader
     */
    public void setShowPageSummaryHeader(boolean showPageSummaryHeader) {
        this.showPageSummaryHeader = showPageSummaryHeader;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        PageValidationMessages pageValidationMessagesCopy = (PageValidationMessages) component;
        pageValidationMessagesCopy.setShowPageSummaryHeader(this.showPageSummaryHeader);
    }
}
