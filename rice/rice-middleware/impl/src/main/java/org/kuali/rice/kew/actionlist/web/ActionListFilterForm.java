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
package org.kuali.rice.kew.actionlist.web;

import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.actionlist.ActionListFilter;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.krad.util.GlobalVariables;

import java.text.ParseException;
import java.util.List;


/**
 * Struts form class for ActionListFilterAction
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ActionListFilterForm extends KualiForm {

	private static final long serialVersionUID = -1149636352016711445L;
    private static String CREATE_DATE_FROM = "createDateFrom";
    private static String CREATE_DATE_TO = "createDateTo";
    private static String LAST_ASSIGNED_DATE_FROM = "lastAssignedDateFrom";
    private static String LAST_ASSIGNED_DATE_TO = "lastAssignedDateTo";
    private ActionListFilter filter;
    private String createDateFrom;
    private String createDateTo;
    private String lastAssignedDateTo;
    private String lastAssignedDateFrom;
    private String methodToCall = "";
    private String lookupableImplServiceName;
    private String lookupType;
    private String docTypeFullName;
    private List userWorkgroups;
    private String cssFile = "kuali.css";
    private String test = "";

    public String getTest(){
    	return test;
    }
    public void setTest(String test){
    	this.test = test;
    }

    public ActionListFilterForm() {
        filter = new ActionListFilter();
    }

    public String getCreateDateTo() {
        return createDateTo;
    }
    public void setCreateDateTo(String createDateTo) {
    	if(createDateTo == null){
    		createDateTo = "";
    	}
    	else{
    		this.createDateTo = createDateTo.trim();
    	}
    }
    public String getLastAssignedDateFrom() {
        return lastAssignedDateFrom;
    }
    public void setLastAssignedDateFrom(String lastAssignedDateFrom) {
    	if(lastAssignedDateFrom == null){
    		lastAssignedDateFrom = "";
    	}
    	else{
    		this.lastAssignedDateFrom = lastAssignedDateFrom.trim();
    	}
    }
    public String getCreateDateFrom() {
        return createDateFrom;
    }
    public void setCreateDateFrom(String createDate) {
        if(createDate == null){
        	createDate = "";
        }
        else{
        	this.createDateFrom = createDate.trim();
        }
    }

    public ActionListFilter getFilter() {
        return filter;
    }

    public String getMethodToCall() {
        return methodToCall;
    }

    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    public void setFilter(ActionListFilter filter) {
        this.filter = filter;
        if (filter.getCreateDateFrom() != null) {
            setCreateDateFrom(RiceConstants.getDefaultDateFormat().format(filter.getCreateDateFrom()));
        }
        if (filter.getCreateDateTo() != null) {
            setCreateDateTo(RiceConstants.getDefaultDateFormat().format(filter.getCreateDateTo()));
        }
        if (filter.getLastAssignedDateFrom() != null) {
            setLastAssignedDateFrom(RiceConstants.getDefaultDateFormat().format(filter.getLastAssignedDateFrom()));
        }
        if (filter.getLastAssignedDateTo() != null) {
            setLastAssignedDateTo(RiceConstants.getDefaultDateFormat().format(filter.getLastAssignedDateTo()));
        }
    }

    public String getLastAssignedDateTo() {
        return lastAssignedDateTo;
    }

    public void setLastAssignedDateTo(String lastAssignedDate) {
    	if(lastAssignedDate == null){
    		lastAssignedDate = "";
    	}
    	else{
    		this.lastAssignedDateTo = lastAssignedDate.trim();
    	}
    }

    public void validateDates() {
        //List errors = new ArrayList();
        //ActionErrors errors = new ActionErrors();
        if (getCreateDateFrom() != null && getCreateDateFrom().length() != 0) {
            try {
                RiceConstants.getDefaultDateFormat().parse(getCreateDateFrom());
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(CREATE_DATE_FROM, "general.error.fieldinvalid", "Create Date From");
            }
        }
        if (getCreateDateTo() != null && getCreateDateTo().length() != 0) {
            try {
                RiceConstants.getDefaultDateFormat().parse(getCreateDateTo());
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(CREATE_DATE_TO, "general.error.fieldinvalid", "Create Date To");
            }
        }
        if (getLastAssignedDateFrom() != null && getLastAssignedDateFrom().length() != 0) {
            try {
                RiceConstants.getDefaultDateFormat().parse(getLastAssignedDateFrom());
            } catch (ParseException e1) {
                GlobalVariables.getMessageMap().putError(LAST_ASSIGNED_DATE_FROM, "general.error.fieldinvalid", "Last Assigned Date From");
            }
        }
        if (getLastAssignedDateTo() != null && getLastAssignedDateTo().length() != 0) {
            try {
                RiceConstants.getDefaultDateFormat().parse(getLastAssignedDateTo());
            } catch (ParseException e1) {
                GlobalVariables.getMessageMap().putError(LAST_ASSIGNED_DATE_TO, "general.error.fieldinvalid", "Last Assigned Date To");
            }
        }
    }

    public ActionListFilter getLoadedFilter()/* throws ParseException*/ {
        try {
            if (getCreateDateFrom() != null && getCreateDateFrom().length() != 0) {
                filter.setCreateDateFrom(RiceConstants.getDefaultDateFormat().parse(getCreateDateFrom()));
            }
            if (getCreateDateTo() != null && getCreateDateTo().length() != 0) {
                filter.setCreateDateTo(RiceConstants.getDefaultDateFormat().parse(getCreateDateTo()));
            }
            if (getLastAssignedDateFrom() != null && getLastAssignedDateFrom().length() != 0) {
                filter.setLastAssignedDateFrom(RiceConstants.getDefaultDateFormat().parse(getLastAssignedDateFrom()));
            }
            if (getLastAssignedDateTo() != null && getLastAssignedDateTo().length() != 0) {
                filter.setLastAssignedDateTo(RiceConstants.getDefaultDateFormat().parse(getLastAssignedDateTo()));
            }
            if (getDocTypeFullName() != null && ! "".equals(getDocTypeFullName())) {
                filter.setDocumentType(getDocTypeFullName());
            }
        } catch (ParseException e) {
            //error caught and displayed in validateDates()
        }

        return filter;
    }

    public String getLookupableImplServiceName() {
        return lookupableImplServiceName;
    }

    public void setLookupableImplServiceName(String lookupableImplServiceName) {
        this.lookupableImplServiceName = lookupableImplServiceName;
    }

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getDocTypeFullName() {
        return docTypeFullName;
    }

    public void setDocTypeFullName(String docTypeFullName) {
        this.docTypeFullName = docTypeFullName;
    }

    public List getUserWorkgroups() {
        return userWorkgroups;
    }

    public void setUserWorkgroups(List userWorkgroups) {
        this.userWorkgroups = userWorkgroups;
    }

	public String getCssFile() {
		return this.cssFile;
	}

	public void setCssFile(String cssFile) {
		this.cssFile = cssFile;
	}

}
