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
package org.kuali.rice.krad.web.form;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.inquiry.Inquirable;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.uif.view.InquiryView;

/**
 * Form class for <code>InquiryView</code> screens
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class InquiryForm extends UifFormBase {
    private static final long serialVersionUID = 4733144086378429410L;

    private String dataObjectClassName;
    private Object dataObject;

    private boolean redirectedInquiry;

    public InquiryForm() {
        setViewTypeName(ViewType.INQUIRY);

        redirectedInquiry = false;
    }

    /**
     * Picks out business object name from the request to get retrieve a
     * lookupable and set properties
     */
    @Override
    public void postBind(HttpServletRequest request) {
        super.postBind(request);

        if (StringUtils.isBlank(getDataObjectClassName())) {
            setDataObjectClassName(((InquiryView) getView()).getDataObjectClassName().getName());
        }
    }

    /**
     * Class name of the data object the inquiry will display
     *
     * <p>
     * Used to set the data object class for the <code>Inquirable</code> which
     * is then used to perform the inquiry query
     * </p>
     *
     * @return String class name
     */
    public String getDataObjectClassName() {
        return this.dataObjectClassName;
    }

    /**
     * Setter for the inquiry data object class name
     *
     * @param dataObjectClassName
     */
    public void setDataObjectClassName(String dataObjectClassName) {
        this.dataObjectClassName = dataObjectClassName;
    }

    /**
     * Result data object for inquiry that will be display with the view
     *
     * @return Object object instance containing the inquiry data
     */
    public Object getDataObject() {
        return this.dataObject;
    }

    /**
     * Setter for the inquiry data object
     *
     * @param dataObject
     */
    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    /**
     * Indicates whether the requested was redirected from the inquiry framework due to an external object
     * request. This prevents the framework from performing another redirect check
     *
     * @return boolean true if request was a redirect, false if not
     */
    public boolean isRedirectedInquiry() {
        return redirectedInquiry;
    }

    /**
     * Setter for the redirected request indicator
     *
     * @param redirectedInquiry
     */
    public void setRedirectedInquiry(boolean redirectedInquiry) {
        this.redirectedInquiry = redirectedInquiry;
    }

    /**
     * <code>Inquirable</code>  instance that will be used to perform
     * the inquiry
     *
     * @return Inquirable instance
     */
    public Inquirable getInquirable() {
        return (Inquirable) getView().getViewHelperService();
    }

}
