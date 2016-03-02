package org.kuali.ole.select.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.form.OLEEncumberedReportForm;
import org.kuali.ole.service.impl.OleLicenseRequestServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 12/6/13
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/searchDonorEncumberedReportController")
public class OLEEncumberedReportController extends UifControllerBase {

    private BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
    private static final Logger LOG = Logger.getLogger(OLEEResourceSearchController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(org.kuali.ole.OLEConstants.OLEEResourceRecord.CREATED_DATE_FORMAT);

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new OLEEncumberedReportForm();
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEEncumberedReportForm oleEncumberedReportForm = (OLEEncumberedReportForm) form;
        List<OLEDonor> oleDonorList = new ArrayList<>();
        List<OLELinkPurapDonor> oleLinkPurapDonors = new ArrayList<>();
        if (StringUtils.isEmpty(oleEncumberedReportForm.getDonorCode())) {
            oleLinkPurapDonors = (List<OLELinkPurapDonor>) KRADServiceLocator.getBusinessObjectService().findAll(OLELinkPurapDonor.class);
        } else {
            Map map = new HashMap();
            map.put(OLEConstants.DONOR_CODE, oleEncumberedReportForm.getDonorCode());
            List<OLEDonor> donorList = (List<OLEDonor>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEDonor.class, map);
            if (donorList != null && donorList.size() > 0) {
                oleLinkPurapDonors = (List<OLELinkPurapDonor>) KRADServiceLocator.getBusinessObjectService().findMatching(OLELinkPurapDonor.class, map);
            } else {
                oleEncumberedReportForm.setEncumberedReportDocumentList(null);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, org.kuali.ole.OLEConstants.DONOR_NOT_FOUND);
                return getUIFModelAndView(oleEncumberedReportForm);
            }
        }
        Map<String, List<Integer>> donorMap = new HashMap<>();
        for (OLELinkPurapDonor oleLinkPurapDonor : oleLinkPurapDonors) {
            Map poItemIdList = new HashMap();
            poItemIdList.put("itemIdentifier", oleLinkPurapDonor.getPoItemId());
            OlePurchaseOrderItem olePurchaseOrderItem = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderItem.class, poItemIdList);
            if (oleLinkPurapDonor.getDonorCode() != null && olePurchaseOrderItem != null && olePurchaseOrderItem.getItemIdentifier() != null) {
                Map map = new HashMap();
                map.put(org.kuali.ole.OLEConstants.DOC_NUM, olePurchaseOrderItem.getDocumentNumber());
                OlePurchaseOrderDocument olePurchaseOrderDocument = boService.findByPrimaryKey(OlePurchaseOrderDocument.class, map);
                if(olePurchaseOrderDocument.isPurchaseOrderCurrentIndicator()) {
                    if (!(oleEncumberedReportForm.getFromDate() == null && oleEncumberedReportForm.getToDate() == null)) {
                        Date purchaseOrderDate = olePurchaseOrderDocument.getPurchaseOrderCreateTimestamp();
                        try {
                            String begin = null;
                            if (oleEncumberedReportForm.getFromDate() != null) {
                                begin = dateFormat.format(oleEncumberedReportForm.getFromDate());
                            }
                            String end = null;
                            if (oleEncumberedReportForm.getToDate() != null) {
                                end = dateFormat.format(oleEncumberedReportForm.getToDate());
                            }
                            boolean isValid = false;
                            OleLicenseRequestServiceImpl oleLicenseRequestService = GlobalResourceLoader.getService(org.kuali.ole.OLEConstants.OleLicenseRequest.LICENSE_REQUEST_SERVICE);
                            isValid = oleLicenseRequestService.validateDate(purchaseOrderDate, begin, end);
                            if (isValid) {
                                if (donorMap.containsKey(oleLinkPurapDonor.getDonorCode())) {
                                    donorMap.get(oleLinkPurapDonor.getDonorCode()).add(olePurchaseOrderItem.getItemIdentifier());
                                } else {
                                    List<Integer> poItemIds = new ArrayList<>();
                                    poItemIds.add(olePurchaseOrderItem.getItemIdentifier());
                                    donorMap.put(oleLinkPurapDonor.getDonorCode(), poItemIds);
                                }
                            }
                        } catch (Exception e) {
                        LOG.error("Exception while calling the licenseRequest service" + e.getMessage());
                        throw new RuntimeException(e);
                      }
                    } else {
                        if (donorMap.containsKey(oleLinkPurapDonor.getDonorCode())) {
                            donorMap.get(oleLinkPurapDonor.getDonorCode()).add(olePurchaseOrderItem.getItemIdentifier());
                        } else {
                            List<Integer> poItemIds = new ArrayList<>();
                            poItemIds.add(olePurchaseOrderItem.getItemIdentifier());
                            donorMap.put(oleLinkPurapDonor.getDonorCode(), poItemIds);
                        }
                    }
                }
            }
        }
        for (Object donorKey : donorMap.keySet()) {
            OLEDonor oleDonor = new OLEDonor();
            oleDonor.setDonorCode((String) donorKey);
            Map map = new HashMap();
            map.put(OLEConstants.DONOR_CODE, oleDonor.getDonorCode());
            OLEDonor donor = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEDonor.class, map);
            if (donor != null) {
                oleDonor.setDonorAmount(donor.getDonorAmount() != null ? donor.getDonorAmount() : KualiDecimal.ZERO);
                oleDonor.setDonorNote(donor.getDonorNote());
                oleDonor.setDonorId(donor.getDonorId());
            }
            if (oleDonor.getDonorAmount() != null) {
                KualiDecimal encAmt = KualiDecimal.ZERO;
                KualiDecimal expAmt = KualiDecimal.ZERO;
                List<Integer> poLineItemIds = (ArrayList) donorMap.get((String) donorKey);
                for (Integer poLineItemId : poLineItemIds) {
                    map.clear();
                    map.put(org.kuali.ole.OLEConstants.BIB_ITEM_ID, poLineItemId);
                    OlePurchaseOrderItem olePurchaseOrderItem = boService.findByPrimaryKey(OlePurchaseOrderItem.class, map);
                    if (olePurchaseOrderItem.getItemOutstandingEncumberedAmount() != null) {
                        encAmt = encAmt.add(olePurchaseOrderItem.getItemOutstandingEncumberedAmount().divide(new KualiDecimal(olePurchaseOrderItem.getOleDonors().size())));
                    }
                    if (olePurchaseOrderItem.getItemInvoicedTotalAmount() != null) {
                        expAmt = expAmt.add(olePurchaseOrderItem.getItemInvoicedTotalAmount().divide(new KualiDecimal(olePurchaseOrderItem.getOleDonors().size())));
                    }
                }
                oleDonor.setEncumberedAmount(encAmt);
                oleDonor.setExpensedAmount(expAmt);
            }
            oleDonorList.add(oleDonor);
        }

        if (StringUtils.isEmpty(oleEncumberedReportForm.getDonorCode())) {
            List<OLEDonor> oleDonorsList =  (List<OLEDonor>) KRADServiceLocator.getBusinessObjectService().findAll(OLEDonor.class);
            List<OLEDonor> donorsListWithNoPo = new ArrayList<OLEDonor>();
            boolean donorAvailable=false;
            for(OLEDonor oLEDonor :oleDonorsList) {
                for(OLEDonor donor: oleDonorList) {
                    if(oLEDonor.getDonorCode().equals(donor.getDonorCode())) {
                        donorAvailable=true;
                        break;
                    }
                }
                if(!donorAvailable) {
                    donorsListWithNoPo.add(oLEDonor);
                }
                else {
                    donorAvailable=false;
                }
            }
            for(OLEDonor donorWithNoPo : donorsListWithNoPo) {
                donorWithNoPo.setDonorAmount(donorWithNoPo.getDonorAmount() != null ? donorWithNoPo.getDonorAmount() : KualiDecimal.ZERO);
                donorWithNoPo.setEncumberedAmount(new KualiDecimal(0));
                donorWithNoPo.setExpensedAmount(new KualiDecimal(0));
                oleDonorList.add(donorWithNoPo);
            }
        }
        if (oleDonorList.size() == 0) {
            OLEDonor oleDonor = new OLEDonor();
            oleDonor.setDonorCode((String) oleEncumberedReportForm.getDonorCode());
            Map map = new HashMap();
            map.put(OLEConstants.DONOR_CODE, oleDonor.getDonorCode());
            OLEDonor donor = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEDonor.class, map);
            donor.setDonorAmount(donor.getDonorAmount() != null ? donor.getDonorAmount() : KualiDecimal.ZERO);
            donor.setEncumberedAmount(new KualiDecimal(0));
            donor.setExpensedAmount(new KualiDecimal(0));
            oleDonorList.add(donor);
            oleEncumberedReportForm.setEncumberedReportDocumentList(oleDonorList);
          //  oleEncumberedReportForm.setEncumberedReportDocumentList(null);
          //  GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, org.kuali.ole.OLEConstants.NO_RECORD_FOUND);
        } else {
            oleEncumberedReportForm.setEncumberedReportDocumentList(oleDonorList);
        }
        return getUIFModelAndView(oleEncumberedReportForm);
    }

    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEEncumberedReportForm oleEncumberedReportForm = (OLEEncumberedReportForm) form;
        oleEncumberedReportForm.setDonorCode(null);
        oleEncumberedReportForm.setFromDate(null);
        oleEncumberedReportForm.setToDate(null);
        oleEncumberedReportForm.setEncumberedReportDocumentList(null);
        return getUIFModelAndView(oleEncumberedReportForm);
    }

    @RequestMapping(params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (org.apache.commons.lang.StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        return performRedirect(form, url, props);
    }

}
