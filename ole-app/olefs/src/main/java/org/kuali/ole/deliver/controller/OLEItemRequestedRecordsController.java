package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.form.OLEItemRequestedRecordsForm;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
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
 * Created by chenchulakshmig on 1/23/15.
 */
@Controller
@RequestMapping(value = "/itemRequestedRecord")
public class OLEItemRequestedRecordsController extends UifControllerBase {

    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEItemRequestedRecordsForm();
    }

    @RequestMapping(params = "methodToCall=viewRequestedRecords")
    public ModelAndView viewRequestedRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEItemRequestedRecordsForm oleItemRequestedRecordsForm = (OLEItemRequestedRecordsForm) form;
        String itemBarcode = request.getParameter(OLEConstants.OleDeliverRequest.ITEM_BARCODE);
        if (StringUtils.isNotBlank(itemBarcode)) {
            Map itemMap = new HashMap();
            itemMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, itemBarcode);
            List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemMap);
            if (CollectionUtils.isNotEmpty(deliverRequestBos)) {
                for (int i = 0; i < deliverRequestBos.size(); i++) {
                    getOleDeliverRequestDocumentHelperService().processItem(deliverRequestBos.get(i));
                }
                oleItemRequestedRecordsForm.setRequestBos(deliverRequestBos);
            }
        }
        return getUIFModelAndView(oleItemRequestedRecordsForm, "OLEItemRequestedRecordPage");
    }
}
