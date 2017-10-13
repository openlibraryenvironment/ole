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
package org.kuali.rice.kew.rule.web;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.web.ui.Row;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Struts ActionForm for the {@link RoutingReportAction}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoutingReportForm extends KualiForm {

	private static final long serialVersionUID = 509542372934250061L;

    private String ruleTemplateId;
    private String lookupableImplServiceName;
    private String documentType;
    private String reportType;

    // fields below used for document type report URL
    private String documentTypeParam;
    private String initiatorPrincipalId;
    private String documentContent;
    private String backUrl;
    private String showCloseButton;

    private String dateRef;
    private String effectiveHour;
    private String effectiveMinute;
    private String amPm;

    private List ruleTemplates;
    private List actionRequests;
    private Map fields;

    /*chb: 15Jan2009
    ruleTemplateAttributes is set by RoutingReportAction.loadRuleTemplateOnForm(...)
    */
    private List<Row> ruleTemplateAttributes;
    private List attributes;

    private boolean showFields;
    private boolean showViewResults;

    public RoutingReportForm() {
        attributes = new ArrayList();
        ruleTemplates = new ArrayList();
        actionRequests = new ArrayList();
        fields = new HashMap();
        ruleTemplateAttributes = new ArrayList();
    }

    @Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if (getReportType() != null && getReportType().equals(RoutingReportAction.DOC_TYPE_REPORTING)) {
            if (!org.apache.commons.lang.StringUtils.isEmpty(getDocumentType())) {
                DocumentType docType = getDocumentTypeService().findByName(getDocumentType());
                if (docType == null) {
                    ActionMessage error = new ActionMessage("routereport.documenttype.invalid");
                    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
                }
            }
        }
        return errors;
    }

    public String getLookupableImplServiceName() {
        return lookupableImplServiceName;
    }

    public void setLookupableImplServiceName(String lookupableImplServiceName) {
        this.lookupableImplServiceName = lookupableImplServiceName;
    }

    public List<Row> getRuleTemplateAttributes() {
        return ruleTemplateAttributes;
    }

    //chb: 15Jan2009 set in RoutingReportAction.loadRuleTemplateOnForm(...)
    public void setRuleTemplateAttributes(List<Row> ruleTemplateAttributes) {
        this.ruleTemplateAttributes = ruleTemplateAttributes;
    }

    public Map getFields() {
        return fields;
    }

    public void setFields(Map fields) {
        this.fields = fields;
    }

    public List getRuleTemplates() {
        return ruleTemplates;
    }

    public void setRuleTemplates(List ruleTemplates) {
        this.ruleTemplates = ruleTemplates;
    }

    public List<KeyValue> getHours() {
    	List<KeyValue> hours = new ArrayList<KeyValue>();
        hours.add(new ConcreteKeyValue("0", "12"));
        for (int i = 1; i < 12; i++) {
            hours.add(new ConcreteKeyValue(i + "", i + ""));
        }
        return hours;
    }
    public List<KeyValue> getMinutes() {
    	List<KeyValue> mins = new ArrayList<KeyValue>();
        for (int i = 0; i < 60; i++) {
            mins.add(new ConcreteKeyValue(i + "", ":" + (i < 10 ? "0" : "") + i + ""));
        }
        return mins;
    }

    public String getRuleTemplateId() {
        return ruleTemplateId;
    }

    public void setRuleTemplateId(String ruleTemplateId) {
        this.ruleTemplateId = ruleTemplateId;
    }

    public List getActionRequests() {
        return actionRequests;
    }

    public void setActionRequests(List actionRequests) {
        this.actionRequests = actionRequests;
    }

    public void setDocTypeFullName(String documentType) {
        this.documentType = documentType;
    }

    public void setDocTypeGroupName(String docTypeGroupName) {
        this.documentType = docTypeGroupName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public boolean isShowFields() {
        return showFields;
    }

    public void setShowFields(boolean showFields) {
        this.showFields = showFields;
    }

    public boolean isShowViewResults() {
        return showViewResults;
    }

    public void setShowViewResults(boolean showViewResults) {
        this.showViewResults = showViewResults;
    }

    private DocumentTypeService getDocumentTypeService() {
        return (DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE);
    }

    public String getDateRef() {
        return dateRef;
    }

    public void setDateRef(String dateRef) {
        this.dateRef = dateRef;
    }

    public String getEffectiveHour() {
        return effectiveHour;
    }

    public void setEffectiveHour(String effectiveHour) {
        this.effectiveHour = effectiveHour;
    }

    public String getEffectiveMinute() {
        return effectiveMinute;
    }

    public void setEffectiveMinute(String effectiveMinute) {
        this.effectiveMinute = effectiveMinute;
    }

    public String getAmPm() {
        return amPm;
    }

    public void setAmPm(String amPm) {
        this.amPm = amPm;
    }

    public String getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }

    public String getInitiatorPrincipalId() {
        return initiatorPrincipalId;
    }

    public void setInitiatorPrincipalId(String initiatorNetworkId) {
        this.initiatorPrincipalId = initiatorNetworkId;
    }

    public String getBackUrl() {
        if (StringUtils.isBlank(backUrl)) {
            return null;
        }
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public boolean isDisplayCloseButton() {
        return (KewApiConstants.DISPLAY_CLOSE_BUTTON_TRUE_VALUE.equals(getShowCloseButton()));
    }

    public String getShowCloseButton() {
        return showCloseButton;
    }

    public void setShowCloseButton(String showCloseButton) {
        this.showCloseButton = showCloseButton;
    }

    public String getDocumentTypeParam() {
        return documentTypeParam;
    }

    public void setDocumentTypeParam(String documentTypeParam) {
        this.documentTypeParam = documentTypeParam;
        this.documentType = documentTypeParam;
    }

}
