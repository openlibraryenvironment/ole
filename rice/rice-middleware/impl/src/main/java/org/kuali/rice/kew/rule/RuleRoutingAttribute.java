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
package org.kuali.rice.kew.rule;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A {@link WorkflowRuleAttribute} which is used to route a rule based on the
 * {@link DocumentType} of the rule which is created.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleRoutingAttribute implements WorkflowRuleAttribute {

	private static final long serialVersionUID = -8884711461398770563L;

	private static final String DOC_TYPE_NAME_PROPERTY = "docTypeFullName";//doc_type_name
    private static final String DOC_TYPE_NAME_KEY = "docTypeFullName";

    private static final String LOOKUPABLE_CLASS = "org.kuali.rice.kew.doctype.bo.DocumentType";//DocumentTypeLookupableImplService//org.kuali.rice.kew.doctype.bo.DocumentType
    private static final String DOC_TYPE_NAME_LABEL = "Document type name";

    private static final String DOC_TYPE_NAME_XPATH = "//newMaintainableObject/businessObject/docTypeName";
    private static final String DOC_TYPE_NAME_DEL_XPATH = "//newMaintainableObject/businessObject/delegationRule/docTypeName";

    private String doctypeName;
    private List<Row> rows;
    private boolean required;

    public RuleRoutingAttribute(String docTypeName) {
        this();
        setDoctypeName(docTypeName);
    }

    public RuleRoutingAttribute() {
        buildRows();
    }

    private void buildRows() {
        rows = new ArrayList<Row>();

        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field(DOC_TYPE_NAME_LABEL, "", Field.TEXT, false, DOC_TYPE_NAME_PROPERTY, "", false, false, null, LOOKUPABLE_CLASS));
        //fields.add(new Field(DOC_TYPE_NAME_LABEL, "", Field.TEXT, false, DOC_TYPE_NAME_KEY, "", false, false, null, LOOKUPABLE_CLASS));
        rows.add(new Row(fields));
    }

    @Override
    public boolean isMatch(DocumentContent docContent, List<RuleExtension> ruleExtensions) {
	setDoctypeName(getRuleDocumentTypeFromRuleExtensions(ruleExtensions));
        DocumentTypeService service = (DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE);
        
		try {
			String docTypeName = getDocTypNameFromXML(docContent);
            if (docTypeName.equals(getDoctypeName())) {
                return true;
            }
            DocumentType documentType = service.findByName(docTypeName);
            while (documentType != null && documentType.getParentDocType() != null) {
                documentType = documentType.getParentDocType();
                if(documentType.getName().equals(getDoctypeName())){
                    return true;
                }
            }
		} catch (XPathExpressionException e) {
			throw new WorkflowRuntimeException(e);
		}
		
		
        if (ruleExtensions.isEmpty()) {
            return true;
        }
        return false;
    }

    protected String getRuleDocumentTypeFromRuleExtensions(List<RuleExtension> ruleExtensions) {
	    for (RuleExtension extension : ruleExtensions) {
            if (extension.getRuleTemplateAttribute().getRuleAttribute().getResourceDescriptor().equals(getClass().getName())) {
                for (Map.Entry<String, String> extensionValue : extension.getExtensionValuesMap().entrySet()) {
                    String key = extensionValue.getKey();
                    String value = extensionValue.getValue();
                    if (key.equals(DOC_TYPE_NAME_KEY)) {
                        return value;
                    }
                }
            }
        }
	    return null;
    }

    @Override
    public List getRuleRows() {
        return rows;
    }

    @Override
    public List getRoutingDataRows() {
        return rows;
    }

    @Override
    public String getDocContent() {
        if (!org.apache.commons.lang.StringUtils.isEmpty(getDoctypeName())) {
            return "<ruleRouting><doctype>" + getDoctypeName() + "</doctype></ruleRouting>";
        } else {
            return "";
        }
    }
  

	private String getDocTypNameFromXML(DocumentContent docContent) throws XPathExpressionException {
		XPath xPath = XPathHelper.newXPath();
		String docTypeName = xPath.evaluate(DOC_TYPE_NAME_XPATH, docContent.getDocument());
				
		if (StringUtils.isBlank(docTypeName)) {
			docTypeName = xPath.evaluate(DOC_TYPE_NAME_DEL_XPATH, docContent.getDocument());
			
			if (StringUtils.isBlank(docTypeName)) {
				throw new WorkflowRuntimeException("Could not locate Document Type Name on the document: " + 
						docContent.getRouteContext().getDocument().getDocumentId());
			}
		} 
		return docTypeName;
	}


    public List<RuleRoutingAttribute> parseDocContent(DocumentContent docContent) {
        try {
            Document doc2 = (Document) XmlHelper.buildJDocument(new StringReader(docContent.getDocContent()));
            
            List<RuleRoutingAttribute> doctypeAttributes = new ArrayList<RuleRoutingAttribute>();
            Collection<Element> ruleRoutings = XmlHelper.findElements(doc2.getRootElement(), "docTypeName");
            List<String> usedDTs = new ArrayList<String>();
            for (Iterator<Element> iter = ruleRoutings.iterator(); iter.hasNext();) {
                Element ruleRoutingElement = (Element) iter.next();

                //Element docTypeElement = ruleRoutingElement.getChild("doctype");
                Element docTypeElement = ruleRoutingElement;
                String elTxt = docTypeElement.getText();
                if (docTypeElement != null && !usedDTs.contains(elTxt)) {
                	usedDTs.add(elTxt);
                    doctypeAttributes.add(new RuleRoutingAttribute(elTxt));
                }
            }

            return doctypeAttributes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List getRuleExtensionValues() {
        List extensions = new ArrayList();

        if (!org.apache.commons.lang.StringUtils.isEmpty(getDoctypeName())) {
            RuleExtensionValue extension = new RuleExtensionValue();
            extension.setKey(DOC_TYPE_NAME_KEY);
            extension.setValue(getDoctypeName());
            extensions.add(extension);
        }

        return extensions;
    }

    @Override
    public List<RemotableAttributeError> validateRoutingData(Map paramMap) {
        List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();
        setDoctypeName((String) paramMap.get(DOC_TYPE_NAME_PROPERTY));
        if (isRequired() && org.apache.commons.lang.StringUtils.isEmpty(getDoctypeName())) {
            errors.add(RemotableAttributeError.Builder.create("routetemplate.ruleroutingattribute.doctype.invalid", "doc type is not valid.").build());
        }

        if (!org.apache.commons.lang.StringUtils.isEmpty(getDoctypeName())) {
            DocumentTypeService service = (DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE);
            DocumentType documentType = service.findByName(getDoctypeName());
            if (documentType == null) {
                errors.add(RemotableAttributeError.Builder.create("routetemplate.ruleroutingattribute.doctype.invalid", "doc type is not valid").build());
            }
        }
        return errors;
    }

    @Override
    public List<RemotableAttributeError> validateRuleData(Map paramMap) {
        return validateRoutingData(paramMap);
    }

    public String getDoctypeName() {
        return this.doctypeName;
    }

    public void setDoctypeName(String docTypeName) {
        this.doctypeName = docTypeName;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }
}
