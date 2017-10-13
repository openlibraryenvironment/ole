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

/**
 * Content element that renders a non-breaking space HTML <code>&amp;nbsp;</code> tag
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "space-bean", parent = "Uif-Space")
public class Space extends ContentElementBase {
    private static final long serialVersionUID = 4655642965438419569L;

    public Space() {
        super();
    }

    /**
     * Indicates that this element renders itself and does not use a template
     *
     * <p>
     * Since this method returns true, the renderOutput property provides
     * the HTML string representing this element.
     * </p>
     *
     * @return true - this object renders itself
     * @see org.kuali.rice.krad.uif.component.Component#isSelfRendered()
     */
    @Override
    public boolean isSelfRendered() {
        return true;
    }

    /**
     * Provides the HTML string to be used to render a non-breaking space
     *
     * <p>The HTML for a Space element is <code>&amp;nbsp;</code></p>
     *
     * @return the HTML string for a non-breaking space
     * @see org.kuali.rice.krad.uif.component.Component#getRenderedHtmlOutput()
     */
    @Override
    public String getRenderedHtmlOutput() {
        return "&nbsp;";
    }
}
