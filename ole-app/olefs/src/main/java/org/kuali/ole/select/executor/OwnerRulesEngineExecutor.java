package org.kuali.ole.select.executor;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.framework.support.krms.RulesEngineExecutor;
import org.kuali.rice.kew.impl.peopleflow.PeopleFlowBo;
import org.kuali.rice.kew.impl.peopleflow.PeopleFlowMemberBo;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.engine.*;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * OwnerRulesEngineExecutor validates rules from krms and executes people flow action.
 */
public class OwnerRulesEngineExecutor implements RulesEngineExecutor {

    /**
     * This method validates rules from krms and execute people flow action
     *
     * @param routeContext
     * @param engine
     * @return EngineResults
     */
    @Override
    public EngineResults execute(RouteContext routeContext, Engine engine) {

        HashMap<String, Object> agendaValue = new HashMap<String, Object>();
        agendaValue.put("nm", "OWNER_POLICY");
        List<AgendaBo> agendaBos = (List<AgendaBo>) KRADServiceLocator.getBusinessObjectService().findMatching(AgendaBo.class, agendaValue);
        AgendaBo agendaBo = agendaBos.get(0);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("AGENDA_NAME", agendaBo.getName());
        List<MatchBo> matchBos = (List<MatchBo>) KRADServiceLocator.getBusinessObjectService().findMatching(MatchBo.class, map);

        SelectionCriteria selectionCriteria =
                SelectionCriteria.createCriteria(null, getSelectionContext(agendaBo.getContext().getName()), getAgendaContext("OWNER_POLICY"));

        ExecutionOptions executionOptions = new ExecutionOptions();
        executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);

        Facts.Builder factBuilder = Facts.Builder.create();

        String docContent = routeContext.getDocument().getDocContent();

        String licenseRequestWorkflowTypeCode =
                getElementValue(docContent, "//newMaintainableObject/dataObject/licenseRequestWorkflowTypeCode");
        String owner =
                getElementValue(docContent, "//newMaintainableObject/dataObject/assignee");

        String oleLicenseRequestStatusCode =
                getElementValue(docContent, "//newMaintainableObject/dataObject/oleLicenseRequestStatus/code");

        HashMap<String, Object> termValues = new HashMap<String, Object>();
        if ((licenseRequestWorkflowTypeCode.equalsIgnoreCase("SIGO")) ||
                (licenseRequestWorkflowTypeCode.equalsIgnoreCase("RVWO"))
                || licenseRequestWorkflowTypeCode.equalsIgnoreCase("APPO") && !oleLicenseRequestStatusCode.equalsIgnoreCase("LC")) {
            termValues.put("owner", "route");
        } else {
            termValues.put("owner", "NIL");
        }

        for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
            MatchBo matchBo = matchBoIterator.next();
            factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
        }
        EngineResults engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
        String peopleFlowsSelected = (String) engineResult.getAttribute("peopleFlowsSelected");
        String peopleFlwid = peopleFlowsSelected != null ? peopleFlowsSelected.split(":")[1] : null;
        if (peopleFlwid != null) createPeopleFlowMemberToOwner(owner, peopleFlwid);
        return engineResult;  //
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
        selector.put(OLEConstants.NAMESPACE_CODE_SELECTOR, "OLE");
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
     * This method create people to owner using owner and  peopleFlwId.
     *
     * @param owner
     * @param peopleFlwId
     */
    private void createPeopleFlowMemberToOwner(String owner, String peopleFlwId) {
        PeopleFlowBo peopleFlow = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(PeopleFlowBo.class, peopleFlwId);
        peopleFlow.getMembers();
        PeopleFlowMemberBo peopleFlowMemberBo = peopleFlow.getMembers().get(0);
        peopleFlowMemberBo.setMemberId(owner);
        KRADServiceLocator.getBusinessObjectService().save(peopleFlowMemberBo);

    }

}
