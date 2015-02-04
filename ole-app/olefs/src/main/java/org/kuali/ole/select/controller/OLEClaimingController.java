package org.kuali.ole.select.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.kuali.ole.OLEKeyConstants;
import org.kuali.ole.batch.service.OLEClaimNoticeService;
import org.kuali.ole.select.bo.OLEClaimingAddress;
import org.kuali.ole.select.bo.OLEClaimingByTitle;
import org.kuali.ole.select.bo.OLEClaimingByVendor;
import org.kuali.ole.select.bo.OLEPOClaimHistory;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.form.OLEAddTitlesToInvoiceForm;
import org.kuali.ole.select.form.OLEClaimingForm;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/18/14
 * Time: 7:11 PM
 * To change oleClaimingByTitle template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/oleClaimingController")
public class OLEClaimingController extends UifControllerBase {

    /**
     * oleClaimingByTitle method converts UifFormBase to OLEClaimingForm
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
        OLEClaimingForm oleClaimingForm = (OLEClaimingForm)form;
        String poItemList = request.getParameter("poItemList");
        String poItemListSelection = request.getParameter("poItemListSelection");
        String titleListSelection = request.getParameter("titleListSelection");
        String vendorListSelection = request.getParameter("vendorListSelection");
        String[] poListArray = poItemList!=null ? poItemList.split("~") : new String[0];
        String[] poListSelectionArray = poItemListSelection!=null ? poItemListSelection.split("~") : new String[0];
        String[] titleListSelectionArray = titleListSelection!=null ? titleListSelection.split("~") : new String[0];
        String[] vendorListSelectionArray = vendorListSelection!=null ? vendorListSelection.split("~") : new String[0];
        HashMap<String,List<OLEClaimingByTitle>> hashMap = new HashMap<>();
        List<OLEClaimingByTitle> claimTitles = null;
        for (int i = 1; i < poListArray.length; i++) {
            if (poListArray[i].equals("true")) {
                if (hashMap.containsKey( vendorListSelectionArray[i])) {
                    claimTitles = hashMap.get(vendorListSelectionArray[i]);
                    OLEClaimingByTitle oleClaimingByTitle = new OLEClaimingByTitle();
                    oleClaimingByTitle.setTitle(titleListSelectionArray[i]);
                    oleClaimingByTitle.setPoItemId(poListSelectionArray[i]);
                    claimTitles.add(oleClaimingByTitle);
                    hashMap.put(vendorListSelectionArray[i],claimTitles);
                }
                else {
                    claimTitles = new ArrayList<>();
                    OLEClaimingByTitle oleClaimingByTitle = new OLEClaimingByTitle();
                    oleClaimingByTitle.setTitle(titleListSelectionArray[i]);
                    oleClaimingByTitle.setPoItemId(poListSelectionArray[i]);
                    claimTitles.add(oleClaimingByTitle);
                    hashMap.put(vendorListSelectionArray[i],claimTitles);
                }
            }
        }
        Iterator<Map.Entry<String,List<OLEClaimingByTitle>>> entries = hashMap.entrySet().iterator();
        List<OLEClaimingByVendor> oleClaimingByVendors = new ArrayList<>();
        while (entries.hasNext()) {
            Map.Entry<String,List<OLEClaimingByTitle>> entry = entries.next();
            List<OLEClaimingByTitle> oleClaimingByTitles = entry.getValue();
            OLEClaimingByVendor oleClaimingByVendor = new OLEClaimingByVendor();
            oleClaimingByVendor.setVendorName(entry.getKey());
            oleClaimingByVendor.setOleClaimingByTitles(oleClaimingByTitles);
            oleClaimingByVendors.add(oleClaimingByVendor);
        }
        oleClaimingForm.setOleClaimingByVendors(oleClaimingByVendors);
        return super.start(oleClaimingForm, result, request, response);
    }
    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEClaimingForm();
    }

    @RequestMapping(params = "methodToCall=claim")
    public ModelAndView claim(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        OLEClaimingForm oleClaimingForm = (OLEClaimingForm) form;
        OLEClaimNoticeService oleClaimNoticeService = new OLEClaimNoticeService();
        List<OLEClaimingByVendor>  oleClaimingByVendors = oleClaimingForm.getOleClaimingByVendors();
        for(OLEClaimingByVendor oleClaimingByVendor:  oleClaimingByVendors){
            for(OLEClaimingByTitle oleClaimingByTitle : oleClaimingByVendor.getOleClaimingByTitles()){
                Map map = new HashMap();
                map.put("itemIdentifier", oleClaimingByTitle.getPoItemId());
                OlePurchaseOrderItem olePurchaseOrderItem = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderItem.class,map);
                if(olePurchaseOrderItem.getClaimCount()!=null){
                    Integer count = olePurchaseOrderItem.getClaimCount().intValue();
                    count++;
                    olePurchaseOrderItem.setClaimCount(new KualiInteger(count));
                }else {
                    olePurchaseOrderItem.setClaimCount(new KualiInteger(1));
                }
                // TODO olePurchaseOrderItem.setClaimDate();
                String emailAddress="";
                String claimInterval="";
                OlePurchaseOrderDocument olePurchaseOrderDocument = olePurchaseOrderItem.getPurapDocument();
                VendorDetail vendorDetail =olePurchaseOrderDocument.getVendorDetail();
                java.util.Date actionDate=null;
                if( vendorDetail.getVendorContacts()!=null &&  vendorDetail.getVendorContacts().size()>0){
                    emailAddress = vendorDetail.getVendorContacts().get(0).getVendorContactEmailAddress();
                    claimInterval = vendorDetail.getClaimInterval();
                }
                if((emailAddress==null || emailAddress.isEmpty() ) && vendorDetail.getVendorAddresses()!=null &&  vendorDetail.getVendorAddresses().size()>0){
                    emailAddress = vendorDetail.getVendorAddresses().get(0).getVendorAddressEmailAddress();
                }
                if (StringUtils.isNotBlank(claimInterval)) {
                    boolean actIntvlFlag = isNumber(claimInterval);
                    if (actIntvlFlag) {
                        Integer actIntvl = Integer.parseInt(claimInterval);
                        actionDate = DateUtils.addDays(new java.util.Date(), actIntvl);
                        olePurchaseOrderItem.setClaimDate(new Date(actionDate.getTime()));
                    }
                }  else{
                    olePurchaseOrderItem.setClaimDate(null);
                }
                oleClaimNoticeService.updateOleClaimingByTitle(oleClaimingByTitle, olePurchaseOrderItem, olePurchaseOrderDocument);
                OLEClaimingAddress toAddress = oleClaimNoticeService.updateClaimingToAddress(olePurchaseOrderDocument);
                OLEClaimingAddress fromAddress = oleClaimNoticeService.updateClaimingFromAddress(olePurchaseOrderDocument);
                oleClaimingByVendor.setToAddress(toAddress);
                oleClaimingByVendor.setFromAddress(fromAddress);
                oleClaimingByVendor.setVendorTransmissionFormatDetail(oleClaimingByTitle.getVendorTransmissionFormatDetail());
                oleClaimingByVendor.setEmailAddress(emailAddress);
                OLEPOClaimHistory claimHistory = new OLEPOClaimHistory();
                claimHistory.setClaimCount(olePurchaseOrderItem.getClaimCount().intValue());
                claimHistory.setClaimDate(new Date(System.currentTimeMillis()));
                claimHistory.setReqItemId(olePurchaseOrderItem.getCopyList().get(0).getReqItemId());
                claimHistory.setOperator(GlobalVariables.getUserSession().getPrincipalName());
                olePurchaseOrderItem.getClaimHistories().add(claimHistory);
                KRADServiceLocator.getBusinessObjectService().save(olePurchaseOrderItem);
            }
            try{
                oleClaimNoticeService.generatePdf(oleClaimingByVendor);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //GlobalVariables.getMessageMap().putInfo(OLEKeyConstants.CLAIM_SUCCESS_MSG,OLEKeyConstants.CLAIM_SUCCESS_MSG);
        oleClaimingForm.setSuccessMsg("Claimed Successfully");
        return getUIFModelAndView(oleClaimingForm);
    }

    @RequestMapping(params = "methodToCall=cancelProcess")
    public ModelAndView cancelProcess(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEClaimingForm oleClaimingForm = (OLEClaimingForm) form;
        oleClaimingForm.setCancelBox(true);
        return getUIFModelAndView(oleClaimingForm);
    }

    private static boolean isNumber(String actionInterval) {
        String actStr = actionInterval;
        for (int i = 0; i < actStr.length(); i++) {
            if (!Character.isDigit(actStr.charAt(i)))
                return false;
        }
        return true;
    }

}
