package org.kuali.ole.deliver.executor;

import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.framework.support.krms.RulesEngineExecutor;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krms.api.engine.*;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * OleLicenseRulesEngineExecutor validates rules from krms and executes people flow action.
 */
public class OleDeliverRequestEngineExecutor implements RulesEngineExecutor {
    private List<OleDeliverRequestBo> recallList = new ArrayList<OleDeliverRequestBo>();
    private List<OleDeliverRequestBo> holdList = new ArrayList<OleDeliverRequestBo>();
    private List<OleDeliverRequestBo> pageList = new ArrayList<OleDeliverRequestBo>();
    private List<OleDeliverRequestBo> requestsByBorrower = new ArrayList<OleDeliverRequestBo>();
    private BusinessObjectService businessObjectService;
    private static final Logger LOG = Logger.getLogger(OleDeliverRequestEngineExecutor.class);

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            return KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

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
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        HashMap<String, Object> agendaValue = new HashMap<String, Object>();
        agendaValue.put(OLEConstants.NAME_NM, OLEConstants.REQUEST_AGENDA_NM);
        List<AgendaBo> agendaBos = (List<AgendaBo>) KRADServiceLocator.getBusinessObjectService().findMatching(AgendaBo.class, agendaValue);
        if (agendaBos != null && agendaBos.size() > 0) {
            AgendaBo agendaBo = agendaBos.get(0);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.AGENDA_NAME, agendaBo.getName());
            List<MatchBo> matchBos = (List<MatchBo>) KRADServiceLocator.getBusinessObjectService().findMatching(MatchBo.class, map);

            SelectionCriteria selectionCriteria =
                    SelectionCriteria.createCriteria(null, getSelectionContext(agendaBo.getContext().getName()),
                            getAgendaContext(OLEConstants.REQUEST_AGENDA_NM));

            ExecutionOptions executionOptions = new ExecutionOptions();
            executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);

            Facts.Builder factBuilder = Facts.Builder.create();

            String docContent = routeContext.getDocument().getDocContent();

            String borrowerType =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.PATRON_TYPE_NAME);
            String itemType =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.ITEM_TYPE_NM);
            String requestTypeId =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID);
            String requestTypeCd =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD);
            String location =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.SHELVING_LOCATION);
            String itemCollection =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.ITEM_COLLECTION);
            String itemLibrary =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.ITEM_LIBRARY);
            String itemCampus =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.ITEM_CAMPUS);
            String itemInstitution =
                    getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.ITEM_INSTITUTION);
            String itemUUID = getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.OleDeliverRequest.ITEM_UUID);
            String borrowerId = getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.OleDeliverRequest.BORROWER_ID);
            String itemId = getElementValue(docContent, OLEConstants.DATA_OBJECT + OLEConstants.OleDeliverRequest.ITEM_ID);
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap.put(OLEConstants.ITEM_UUID, itemUUID);
            if (requestTypeId.equals("1") || (requestTypeId.equals("2"))) {
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "1");
                recallList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "2");
                recallList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            } else if (requestTypeId.equals("3") || requestTypeId.equals("4")) {
                //  holdList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "3");
                holdList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "4");
                holdList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            } else if (requestTypeId.equals("5") || requestTypeId.equals("6")) {
                // pageList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "5");
                pageList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "6");
                pageList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            }
            Map<String, String> requestByBorrower = new HashMap<String, String>();
            requestByBorrower.put(OLEConstants.OleDeliverRequest.BORROWER_ID, borrowerId);
            requestsByBorrower = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestByBorrower);
            HashMap<String, Object> termValues = new HashMap<String, Object>();
            termValues.put(OLEConstants.BORROWER_TYPE, borrowerType);
            termValues.put(OLEConstants.ITEM_TYPE, itemType);
            termValues.put(OLEConstants.LOCATION, location);
            termValues.put(OLEConstants.ITEM_SHELVING, location);
            termValues.put(OLEConstants.ITEM_COLLECTION, itemCollection);
            termValues.put(OLEConstants.ITEM_LIBRARY, itemLibrary);
            termValues.put(OLEConstants.ITEM_CAMPUS, itemCampus);
            termValues.put(OLEConstants.ITEM_INSTITUTION, itemInstitution);
            termValues.put(OLEConstants.MAX_NO_OF_RECALL_REQUEST, new Integer(recallList.size()) + 1);
            termValues.put(OLEConstants.MAX_NO_OF_HOLD_REQUEST, new Integer(holdList.size()) + 1);
            termValues.put(OLEConstants.MAX_NO_OF_PAGE_REQUEST, new Integer(pageList.size()) + 1);
            // termValues.put("maxNumberOfRequestByBorrower",requestsByBorrower.size());
            termValues.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, requestTypeId);
            termValues.put(OLEConstants.REQUEST_TYPE, requestTypeCd);
            termValues.put(OLEConstants.PATRON_ID_POLICY,borrowerId);
            termValues.put(OLEConstants.ITEM_ID_POLICY,itemId);

            for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
                MatchBo matchBo = matchBoIterator.next();
                factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
            }


            engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
            dataCarrierService.removeData(borrowerId+itemId);
            List<String> errorMessage = (List<String>) engineResult.getAttribute(OLEConstants.ERROR_ACTION);
            StringBuffer failures = new StringBuffer();
            if (errorMessage != null && errorMessage.size() > 0) {
                int i = 1;
                for (String errMsg : errorMessage) {
                    failures.append(i++ + ". " + errMsg + OLEConstants.BREAK);
                }
            }
            if (!failures.toString().isEmpty()) {
                GlobalVariables.getMessageMap().putError("Error", failures.toString());

            }

        }
        return engineResult;  //To change body of implemented methods use File | Settings | File Templates.
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
            LOG.error("Exception", e);
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
        selector.put(OLEConstants.NAMESPACE_CODE, OLEConstants.OLE_NAMESPACE);
        selector.put(OLEConstants.NAME, contextName);
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
        selector.put(OLEConstants.NAME, agendaName);
        return selector;
    }


    private void populateRequestList(String requestTypeId, String itemId) {


    }


}
