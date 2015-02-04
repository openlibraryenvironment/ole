package org.kuali.ole.select.executor;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.ole.select.bo.OleAgreementMethod;
import org.kuali.ole.select.bo.OleLicenseRequestType;
import org.kuali.ole.select.bo.OleLicenseRequestWorkflowType;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.framework.support.krms.RulesEngineExecutor;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.engine.*;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * LicenseRulesEngineExecutor validates rules from krms and executes people flow action.
 */
public class LicenseRulesEngineExecutor implements RulesEngineExecutor {

    /**
     * This method validates rules from krms and execute people flow action
     *
     * @param routeContext
     * @param engine
     * @return EngineResults
     */
    @Override
    public EngineResults execute(RouteContext routeContext, Engine engine) {
        EngineResults engineResult = null;
        HashMap<String, Object> agendaValue = new HashMap<String, Object>();
        agendaValue.put("nm", OLEConstants.OleLicenseRequest.LICENSE_AGENDA_NM);
        List<AgendaBo> agendaBos = (List<AgendaBo>) KRADServiceLocator.getBusinessObjectService().findMatching(AgendaBo.class, agendaValue);
        if (agendaBos != null && agendaBos.size() > 0) {
            AgendaBo agendaBo = agendaBos.get(0);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("AGENDA_NAME", agendaBo.getName());
            List<MatchBo> matchBos = (List<MatchBo>) KRADServiceLocator.getBusinessObjectService().findMatching(MatchBo.class, map);

            SelectionCriteria selectionCriteria =
                    SelectionCriteria.createCriteria(null, getSelectionContext(agendaBo.getContext().getName()),
                            getAgendaContext(OLEConstants.OleLicenseRequest.LICENSE_AGENDA_NM));

            ExecutionOptions executionOptions = new ExecutionOptions();
            executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);

            Facts.Builder factBuilder = Facts.Builder.create();

            String docContent = routeContext.getDocument().getDocContent();

            String licenseRequestWorkflowTypeCode =
                    getElementValue(docContent, "//newMaintainableObject/dataObject/licenseRequestWorkflowTypeCode");

            String agreementMethodId =
                    getElementValue(docContent, "//newMaintainableObject/dataObject/agreementMethodId");
            String licenseRequestTypeId =
                    getElementValue(docContent, "//newMaintainableObject/dataObject/licenseRequestTypeId");
            String oleLicenseRequestStatusCode =
                    getElementValue(docContent, "//newMaintainableObject/dataObject/oleLicenseRequestStatus/code");
            String owner =
                    getElementValue(docContent, "//newMaintainableObject/dataObject/assignee");

            String licenseType = "";
            String workflowName = "";
            String agreementMethod = "";
            if (licenseRequestTypeId != null && !licenseRequestTypeId.isEmpty()) {
                licenseType = getLicenseType(licenseRequestTypeId);
            }
            if (licenseRequestWorkflowTypeCode != null && !licenseRequestWorkflowTypeCode.isEmpty()) {
                workflowName = getWorkFlowName(licenseRequestWorkflowTypeCode);
            }
            if (agreementMethodId != null && !agreementMethodId.isEmpty()) {
                agreementMethod = getAgreementMethod(agreementMethodId);
            }
            HashMap<String, Object> termValues = new HashMap<String, Object>();
            if (licenseRequestWorkflowTypeCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.SIGNATORY_ONLY) ||
                    licenseRequestWorkflowTypeCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.REVIEW_ONLY)
                    || licenseRequestWorkflowTypeCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.APPROVE_ONLY)
                    && !oleLicenseRequestStatusCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.LICENSE_COMPLETE)) {
                termValues.put("licenseType", licenseType);
                termValues.put("agreementMethod", agreementMethod);
                termValues.put("workflowName", workflowName);
                if (owner != null && !owner.isEmpty()) {
                    changeLicenseMangerRoleToOwner(owner);
                }
            }

            for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
                MatchBo matchBo = matchBoIterator.next();
                factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
            }


            engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
        }
        return engineResult;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getAgreementMethod(String agreementMethodId) {
        OleAgreementMethod agreementMethod = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleAgreementMethod.class, agreementMethodId);
        return agreementMethod.getAgreementMethodName();
    }

    /**
     * This method returns LicenseType using licenseRequestTypeId.
     *
     * @param licenseRequestTypeId
     * @return oleLicenseRequestType
     */
    private String getLicenseType(String licenseRequestTypeId) {
        OleLicenseRequestType oleLicenseRequestType = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleLicenseRequestType.class, licenseRequestTypeId);
        return oleLicenseRequestType.getName();
    }

    private String getWorkFlowName(String licenseRequestWorkflowTypeCode) {
        OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleLicenseRequestWorkflowType.class, licenseRequestWorkflowTypeCode);
        return oleLicenseRequestWorkflowType.getName();
    }

    /**
     * This method returns ElementValue using docContent and xpathExpression..
     *
     * @param docContent
     * @param xpathExpression
     * @return value
     */
    private String getElementValue(String docContent, String xpathExpression) {
        try {
            Document document = XmlHelper.trimXml(new ByteArrayInputStream(docContent.getBytes()));

            XPath xpath = XPathHelper.newXPath();
            String value = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);

            return value;

        } catch (Exception e) {
            throw new RiceRuntimeException();
        }
    }

    /**
     * This method returns SelectionContext using contextName.
     *
     * @param contextName
     * @return Map
     */
    protected Map<String, String> getSelectionContext(String contextName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(OLEConstants.NAMESPACE_CODE_SELECTOR, OLEConstants.OLE_NAMESPACE);
        selector.put(OLEConstants.NAME_SELECTOR, contextName);
        return selector;
    }

    /**
     * This method returns AgendaContext using agendaName..
     *
     * @param agendaName
     * @return Map
     */
    protected Map<String, String> getAgendaContext(String agendaName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(OLEConstants.NAME_SELECTOR, agendaName);
        return selector;
    }

    /**
     * This method change the license role using owner.
     *
     * @param owner
     */
    private void changeLicenseMangerRoleToOwner(String owner) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ROLE_NM", OLEConstants.OleLicenseRequest.LICENSE_MNGR_ROLE_NM);
        List<RoleBo> roleBos = (List<RoleBo>) KRADServiceLocator.getBusinessObjectService().findMatching(RoleBo.class, map);
        List<RoleMemberBo> roleMemberBos = roleBos != null && roleBos.size() > 0 ? roleBos.get(0).getMembers() : new ArrayList<RoleMemberBo>();
        RoleMemberBo roleMemberBo = roleMemberBos.size() > 0 ? roleMemberBos.get(0) : null;
        if (roleMemberBo != null) {
            roleMemberBo.setMemberId(owner);
            KRADServiceLocator.getBusinessObjectService().save(roleMemberBo);
        }
    }
}
