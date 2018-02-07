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
package org.kuali.rice.krad.uif.view;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

import java.io.Serializable;

/**
 * A simple object that keeps track of various HistoryInformation
 *
 * TODO a variety of these settings are not used in the current implementation of breadcrumbs
 * and history, they may be removed later if they prove unuseful in future changes
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "historyEntry-bean", parent = "Uif-HistoryEntry")
public class HistoryEntry extends UifDictionaryBeanBase implements Serializable {
    private static final long serialVersionUID = -8310916657379268794L;

    private String viewId;
    private String pageId;
    private String title;
    private String url;
    private String formKey;

    public HistoryEntry() {
        super();
    }

    public HistoryEntry(String viewId, String pageId, String title, String url, String formKey) {
        super();

        this.viewId = viewId;
        this.pageId = pageId;
        this.title = title;
        this.url = url;
        this.formKey = formKey;
    }

    /**
     * The viewId of the view
     *
     * @return the viewId
     */
    @BeanTagAttribute(name="viewId")
    public String getViewId() {
        return this.viewId;
    }

    /**
     * @param viewId the viewId to set
     */
    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    /**
     * The pageId of the page on the view
     *
     * @return the pageId
     */
    @BeanTagAttribute(name="pageId")
    public String getPageId() {
        return this.pageId;
    }

    /**
     * @param pageId the pageId to set
     */
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    /**
     * The title of the view
     *
     * @return the title
     */
    @BeanTagAttribute(name="title")
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The url of this HistoryEntry
     *
     * @return the url
     */
    @BeanTagAttribute(name="url")
    public String getUrl() {
        return this.url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the formKey
     */
    @BeanTagAttribute(name="formKey")
    public String getFormKey() {
        return this.formKey;
    }

    /**
     * The formKey of the form in the view
     *
     * @param formKey the formKey to set
     */
    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        HistoryEntry historyEntryCopy = (HistoryEntry) component;
        historyEntryCopy.setViewId(this.getViewId());
        historyEntryCopy.setPageId(this.getPageId());
        historyEntryCopy.setTitle(this.getTitle());
        historyEntryCopy.setUrl(this.getUrl());
        historyEntryCopy.setFormKey(this.getFormKey());
    }
}
