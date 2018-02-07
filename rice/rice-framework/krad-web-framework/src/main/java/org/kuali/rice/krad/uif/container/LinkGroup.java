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
package org.kuali.rice.krad.uif.container;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;

/**
 * Special <code>Group</code> that presents a grouping on links, which can
 * also include nested groupings of links
 *
 * <p>
 * Generally this group outputs a list of <code>LinkField</code> instances, however
 * it can be configured to place separates between the fields and also delimiters
 * for the grouping
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "linkGroup-bean", parent = "Uif-LinkGroup"),
        @BeanTag(name = "linkSubGroup-bean", parent = "Uif-LinkSubGroup"),
        @BeanTag(name = "lookupView-resultActions-bean", parent = "Uif-LookupView-ResultActions")})
public class LinkGroup extends Group {
    private static final long serialVersionUID = -4173031543626881250L;

    private String groupBeginDelimiter;
    private String groupEndDelimiter;
    private String linkSeparator;
    private String emptyLinkGroupString;

    public LinkGroup() {
        super();
    }

    /**
     * String that will be rendered before the group of links are rendered
     *
     * <p>
     * If the list of links is empty, the start delimiter will not be
     * rendered but instead the #emptyLinkGroupString will be outputted
     * </p>
     *
     * e.g. '['
     *
     * @return group begin delimiter
     */
    @BeanTagAttribute(name = "groupBeginDelimiter")
    public String getGroupBeginDelimiter() {
        return groupBeginDelimiter;
    }

    /**
     * Setter for the group begin delimiter
     *
     * @param groupBeginDelimiter
     */
    public void setGroupBeginDelimiter(String groupBeginDelimiter) {
        this.groupBeginDelimiter = groupBeginDelimiter;
    }

    /**
     * String that will be rendered after the group of links are rendered
     *
     * <p>
     * If the list of links is empty, the end delimiter will not be
     * rendered but instead the #emptyLinkGroupString will be outputted
     * </p>
     *
     * e.g. ']'
     *
     * @return group end delimiter
     */
    @BeanTagAttribute(name = "groupEndDelimiter")
    public String getGroupEndDelimiter() {
        return groupEndDelimiter;
    }

    /**
     * Setter for the group end delimiter
     *
     * @param groupEndDelimiter
     */
    public void setGroupEndDelimiter(String groupEndDelimiter) {
        this.groupEndDelimiter = groupEndDelimiter;
    }

    /**
     * String that will be rendered between each rendered link
     *
     * e.g. '|'
     *
     * @return link separator
     */
    @BeanTagAttribute(name = "linkSeparator")
    public String getLinkSeparator() {
        return linkSeparator;
    }

    /**
     * Setter for the link separator
     *
     * @param linkSeparator
     */
    public void setLinkSeparator(String linkSeparator) {
        this.linkSeparator = linkSeparator;
    }

    /**
     * String that will be outputted when the list backing the
     * link group is empty
     *
     * @return empty group string
     */
    @BeanTagAttribute(name = "emptyLinkGroupString")
    public String getEmptyLinkGroupString() {
        return emptyLinkGroupString;
    }

    /**
     * Setter for the empty group string
     *
     * @param emptyLinkGroupString
     */
    public void setEmptyLinkGroupString(String emptyLinkGroupString) {
        this.emptyLinkGroupString = emptyLinkGroupString;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        LinkGroup linkGroupCopy = (LinkGroup) component;
        linkGroupCopy.setGroupBeginDelimiter(this.getGroupBeginDelimiter());
        linkGroupCopy.setGroupEndDelimiter(this.getGroupEndDelimiter());
        linkGroupCopy.setLinkSeparator(this.getLinkSeparator());
        linkGroupCopy.setEmptyLinkGroupString(this.getEmptyLinkGroupString());
    }

}
