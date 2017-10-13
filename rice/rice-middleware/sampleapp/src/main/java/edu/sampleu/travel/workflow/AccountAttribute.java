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
package edu.sampleu.travel.workflow;

import edu.sampleu.travel.bo.TravelAccount;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.identity.PrincipalName;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.AbstractRoleAttribute;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.kns.workflow.WorkflowUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resolves FO's using the accounts associated with the document XPath xpath =
 * KualiWorkflowUtils.getXPath(documentContent.getDocument()); List<String> qualifiers = new ArrayList<String>(); NodeList
 * accountNums = (NodeList)xstreamSafeEval(xpath, "//edu.sampleu.travel.workflow.bo.TravelAccount/number",
 * documentContent.getDocument(), XPathConstants.NODESET); for (int i = 0; i < accountNums.getLength(); i++) { Node accountNum =
 * accountNums.item(i); String accuntNumVal = accountNum.getNodeValue(); }
 */
public class AccountAttribute extends AbstractRoleAttribute {
    private static final RoleName FISCAL_OFFICER_ROLE = new RoleName(AccountAttribute.class.getName(), "FO", "Fiscal Officer");
    private static final List<RoleName> ROLES;
    static {
        List<RoleName> tmp = new ArrayList<RoleName>(1);
        tmp.add(FISCAL_OFFICER_ROLE);
        ROLES = Collections.unmodifiableList(tmp);
    }


    public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
        List<String> qualifiedRoleNames = new ArrayList<String>();
        XPath xpath = WorkflowUtils.getXPath(documentContent.getDocument());
        // This xpath stream needs updating when the TravelAccount is being updated
        NodeList accountNums = (NodeList) xstreamSafeEval(xpath, "/documentContent/applicationContent/org.kuali.rice.krad.workflow.KualiDocumentXmlMaterializer/document/travelAccounts/vector/default/elementData/edu.sampleu.travel.bo.TravelAccount/number", documentContent.getDocument(), XPathConstants.NODESET);
        for (int i = 0; i < accountNums.getLength(); i++) {
            Node accountNum = accountNums.item(i);
            String accuntNumVal = accountNum.getTextContent();
            qualifiedRoleNames.add(accuntNumVal);
        }
        return qualifiedRoleNames;
    }


    public List<RoleName> getRoleNames() {
        return ROLES;
    }


    public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
        String accountNum = qualifiedRole;
        TravelAccount account = new TravelAccount();
        account.setNumber(accountNum);
        account = (TravelAccount) KRADServiceLocator.getBusinessObjectService().retrieve(account);
        if (account == null) {
            throw new RuntimeException("Account " + accountNum + " does not exist!");
        }
        ResolvedQualifiedRole qualRole = new ResolvedQualifiedRole();
        qualRole.setAnnotation("Account " + accountNum + " FO");
        qualRole.setQualifiedRoleLabel("Fiscal Officer Account " + accountNum);
        List<Id> ids = new ArrayList<Id>();
        ids.add(new PrincipalName(account.getFiscalOfficer().getUserName()));
        qualRole.setRecipients(ids);
        return qualRole;
    }


    /**
     * This method will do a simple XPath.evaluate, while wrapping your xpathExpression with the xstreamSafe function. It assumes a
     * String result, and will return such. If an XPathExpressionException is thrown, this will be re-thrown within a
     * RuntimeException.
     * 
     * @param xpath A correctly initialized XPath instance.
     * @param xpathExpression Your XPath Expression that needs to be wrapped in an xstreamSafe wrapper and run.
     * @param item The document contents you will be searching within.
     * @return The string value of the xpath.evaluate().
     */
    public static final Object xstreamSafeEval(XPath xpath, String xpathExpression, Object item, QName returnType) {
        String xstreamSafeXPath = new StringBuilder(WorkflowUtils.XSTREAM_SAFE_PREFIX).append(xpathExpression).append(WorkflowUtils.XSTREAM_SAFE_SUFFIX).toString();
        try {
            return xpath.evaluate(xstreamSafeXPath, item, returnType);
        }
        catch (XPathExpressionException e) {
            throw new RuntimeException("XPathExpressionException occurred on xpath: " + xstreamSafeXPath, e);
        }
    }
    }
