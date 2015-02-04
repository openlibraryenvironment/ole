package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.ItemBillHelperService;
import org.kuali.ole.deliver.form.ItemBillForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;

import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
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
 * Date: 9/7/12
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/itemPatronBill")
public class ItemBillController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(ItemBillController.class);
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }


    /**
     * This method creates new ItemBillform
     *
     * @param request
     * @return ItemBillForm
     */
    @Override
    protected ItemBillForm createInitialForm(HttpServletRequest request) {
        return new ItemBillForm();
    }

    /**
     * This method takes the initial request when click on  Item Bill link
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        ItemBillForm itemBillForm = (ItemBillForm) form;
        return super.start(itemBillForm, result, request, response);
    }

    /**
     * This method retrieves all bill information for particular item barcode
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=submit")
    public ModelAndView submitItemBarcode(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        ItemBillForm itemBillForm = (ItemBillForm) form;
        String itemBarcode = itemBillForm.getItemBarcode();
        if (itemBarcode.equals("") || itemBarcode == null) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.ENTR_ITM_BAR);
            return getUIFModelAndView(itemBillForm);
        } else if (!(itemBarcode.equals("")) || itemBarcode != null) {

            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
            SearchResponse searchResponse = null;
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, itemBarcode), ""));


            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));

            try {


                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);


            } catch (Exception ex) {
                ex.printStackTrace();
            }


            if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ITM_BAR_NOT_AVAL);
                return getUIFModelAndView(itemBillForm);
            }
        }
        Map itemBarcodeMap = new HashMap();
        itemBarcodeMap.put("itemBarcode", itemBarcode);
        List<FeeType> feeTypeList = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, itemBarcodeMap);
        if (feeTypeList.size() == 0) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.BILL_NOT_AVAI_BAR);
            return getUIFModelAndView(itemBillForm);
        }
        ItemBillHelperService itemBillHelperService = new ItemBillHelperService();
        List<FeeType> feeTypes = itemBillHelperService.getItemBillDetails(itemBarcode);
        itemBillForm.setFeeType(itemBillHelperService.getFeeTypeDetails(feeTypes != null && feeTypes.size() > 0 ? feeTypes.get(0) : null));
        itemBillForm.setFeeTypes(feeTypes);
        return getUIFModelAndView(itemBillForm, "ItemViewPage");
    }

    /**
     * This method will clear all elements
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=clear")
    public ModelAndView clearItemBarcode(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        ItemBillForm itemBillForm = (ItemBillForm) form;
        itemBillForm.setItemBarcode("");
        return getUIFModelAndView(itemBillForm, "ItemViewPage");

    }

    /**
     * Just returns as if return with no value was selected.
     */
    @Override
    @RequestMapping(params = "methodToCall=back")
    public ModelAndView back(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        return super.back(form, result, request, response);
    }


}
