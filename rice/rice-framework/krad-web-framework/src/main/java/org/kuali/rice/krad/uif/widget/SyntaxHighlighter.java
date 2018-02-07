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

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Header;
import org.kuali.rice.krad.uif.view.View;

import java.io.Serializable;

/**
 * Widget that renders text syntax highlighted
 *
 * <p>
 * The widget renders a div with a header. In the div the source code text will be added in pre tags with the
 * specified plugin class that is needed for the plugin to alter the text.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "syntaxHighlighter-bean", parent = "Uif-SyntaxHighlighter")
public class SyntaxHighlighter extends WidgetBase {

    private Header header;
    private String sourceCode;
    private String pluginCssClass;
    private boolean allowCopy;
    private boolean showCopyConfirmation;
    
    public SyntaxHighlighter() {
        super();
        allowCopy = true;
        showCopyConfirmation = false;
    }

    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);
    }

    @BeanTagAttribute(name="header")
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     * The text to render with syntax highlighting
     *
     * @return String
     */
    @BeanTagAttribute(name="sourceCode")
    public String getSourceCode() {
        return sourceCode;
    }

    /**
     * Setter for the source code text
     *
     * @param sourceCode
     */
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    /**
     * The class that will be set on the pre tags
     *
     * <p>
     * The class is used by the prettify plugin to identify text to highlight and to specify type of highlighting.
     * </p>
     *
     * @return String
     */
    @BeanTagAttribute(name="pluginCssClass")
    public String getPluginCssClass() {
        return pluginCssClass;
    }

    /**
     * Setter for the plugin css class
     *
     * @param pluginCssClass
     */
    public void setPluginCssClass(String pluginCssClass) {
        this.pluginCssClass = pluginCssClass;
    }

    /**
     * Indicates if the ZeroClipboard copy functionality must be added
     *
     * <p>
     * When copy is allowed a copy button will be shown when the mouse hovers over the syntax highlighter. This button
     * will be hidden the otherwise to avoid obstructing some of the displayed code.
     * </p>
     *
     * @return  boolean
     */
    public boolean isAllowCopy() {
        return allowCopy;
    }

    /**
     * Setter for the allow copy flag
     *
     * @param allowCopy
     */
    public void setAllowCopy(boolean allowCopy) {
        this.allowCopy = allowCopy;
    }

    /**
     * Indicates if a confirmation dialog must be shown after copy action
     *
     * @return boolean
     */
    public boolean isShowCopyConfirmation() {
        return showCopyConfirmation;
    }

    /**
     * Setter for the show copy confirmation dialog flag
     *
     * @param showCopyConfirmation
     */
    public void setShowCopyConfirmation(boolean showCopyConfirmation) {
        this.showCopyConfirmation = showCopyConfirmation;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        SyntaxHighlighter syntaxHighlighterCopy = (SyntaxHighlighter) component;

        if (this.header != null) {
            syntaxHighlighterCopy.setHeader((Header) this.header.copy());
        }

        syntaxHighlighterCopy.setSourceCode(this.getSourceCode());
        syntaxHighlighterCopy.setPluginCssClass(this.getPluginCssClass());
        syntaxHighlighterCopy.setAllowCopy(this.isAllowCopy());
        syntaxHighlighterCopy.setShowCopyConfirmation(this.isShowCopyConfirmation());
    }
}
