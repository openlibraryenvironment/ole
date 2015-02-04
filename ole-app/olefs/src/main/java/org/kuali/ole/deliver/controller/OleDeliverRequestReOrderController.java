package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.form.OleDeliverRequestReOrderForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;

import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;

import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/15/12
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/deliverRequestController")
public class OleDeliverRequestReOrderController extends UifControllerBase {
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
    private LoanProcessor loanProcessor = new LoanProcessor();
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }


    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        return oleDeliverRequestDocumentHelperService;
    }

    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

    public LoanProcessor getLoanProcessor() {
        return loanProcessor;
    }

    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    private static final Logger LOG = Logger.getLogger(OleDeliverRequestReOrderController.class);

    /**
     * This method creates new OleDeliverRequestReOrderForm form
     *
     * @param request
     * @return OleDeliverRequestReOrderForm
     */
    @Override
    protected OleDeliverRequestReOrderForm createInitialForm(HttpServletRequest request) {
        return new OleDeliverRequestReOrderForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside start method");
        OleDeliverRequestReOrderForm oleDeliverRequestForm = (OleDeliverRequestReOrderForm) form;
        return super.start(oleDeliverRequestForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside search method");
        OleDeliverRequestReOrderForm oleDeliverRequestForm = (OleDeliverRequestReOrderForm) form;
        oleDeliverRequestForm.setMessage(null);
        String itemId = oleDeliverRequestForm.getItemId();
        String itemUuid = oleDeliverRequestForm.getItemUuid();
        if (itemUuid == null || (itemUuid != null && itemUuid.isEmpty())) {
            try {


                org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                SearchResponse searchResponse = null;
                search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, itemId), ""));


                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));


                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        String fieldName = searchResultField.getFieldName();
                        String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";

                        if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                            itemUuid = fieldValue;
                        }
                    }
                }


                // itemUuid = itemUUIDMap.get(OLEConstants.ITEM_UUID);

            } catch (Exception e) {
                LOG.error("Exception", e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        Map<String, String> itemMap = new HashMap<String, String>();
        itemMap.put(OLEConstants.ITEM_UUID, itemUuid);
        List<OleDeliverRequestBo> itemList = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, itemMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        if (itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                oleDeliverRequestDocumentHelperService.processItem(itemList.get(i));
               /* itemList.get(i).setOleItemSearch(oleDeliverRequestDocumentHelperService.getItemDetails(itemList.get(i).getItemUuid()));*/
            }
        }
        if (itemList.size() == 0) {
            oleDeliverRequestForm.setMessage(OLEConstants.OleDeliverRequest.NO_PENDING_REQUEST);
        }
        oleDeliverRequestForm.setDeliverRequestBos(itemList);
        return getUIFModelAndView(oleDeliverRequestForm, "DeliverRequestSearchPage");
    }

    @RequestMapping(params = "methodToCall=reOrder")
    public ModelAndView reOrder(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside reOrder method");
        OleDeliverRequestReOrderForm oleDeliverRequestForm = (OleDeliverRequestReOrderForm) form;
        List<OleDeliverRequestBo> itemList = (List<OleDeliverRequestBo>) oleDeliverRequestForm.getDeliverRequestBos();

        OleDeliverRequestDocumentHelperServiceImpl service = new OleDeliverRequestDocumentHelperServiceImpl();
        if (itemList.size() > 0) {
            String message = service.validateQueuePosition(itemList);
            if (!message.equals(OLEConstants.OleDeliverRequest.REORDER_SUCCESS)) {
                oleDeliverRequestForm.setMessage(message);
            } else {
                KRADServiceLocator.getBusinessObjectService().save(itemList);
                oleDeliverRequestForm.setDeliverRequestBos(null);
                oleDeliverRequestForm.setMessage(message);
            }
        }
        return getUIFModelAndView(oleDeliverRequestForm, "DeliverRequestSearchPage");
    }

    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleDeliverRequestReOrderForm oleDeliverRequestForm = (OleDeliverRequestReOrderForm) form;
        ((OleDeliverRequestReOrderForm) form).setMessage(null);
        super.refresh(form, result, request, response);
        return search(form, result, request, response);
    }

}