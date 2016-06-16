package org.kuali.ole.select.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.DonorEncumberReportDAOService;
import org.kuali.ole.select.form.OLEEncumberedReportForm;
import org.kuali.ole.service.impl.OleLicenseRequestServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
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
    private DonorEncumberReportDAOService donorEncumberReportDAOService;

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new OLEEncumberedReportForm();
    }

    @RequestMapping(params="methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception{
        OLEEncumberedReportForm oleEncumberedReportForm = (OLEEncumberedReportForm) form;
        Map<String, Object> queryCriteriaMap=buildCriteria(oleEncumberedReportForm);
        boolean isValid=isValidDonor(oleEncumberedReportForm.getDonorCode());
        List<OLEDonor> oleDonorList;
        if (isValid) {
            oleDonorList = getDonorEncumberReportDAOService().getDonorEncumberList(queryCriteriaMap);
            if (oleDonorList.size()>0) {
                oleEncumberedReportForm.setEncumberedReportDocumentList(oleDonorList);
            } else {
                oleEncumberedReportForm.setEncumberedReportDocumentList(null);
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, org.kuali.ole.OLEConstants.NO_RECORD_FOUND);
            }
        } else {
            oleEncumberedReportForm.setEncumberedReportDocumentList(null);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, org.kuali.ole.OLEConstants.DONOR_NOT_FOUND);
        }
        return getUIFModelAndView(oleEncumberedReportForm);
    }

    private boolean isValidDonor(String donorCode){
        Map map = new HashMap();
        map.put(OLEConstants.DONOR_CODE, donorCode);
        List<OLEDonor> donorList = (List<OLEDonor>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEDonor.class, map);
        if(donorList.size()>0 || donorCode.equals("")){
            return true;
        }else{
            return false;
        }
    }

    private Map<String, Object> buildCriteria(OLEEncumberedReportForm oleEncumberedReportForm){
        Map<String, Object> queryCriteriaMap = new HashMap<>();
        queryCriteriaMap.put(OleSelectConstant.EncumberReportConstant.DONORCODE,oleEncumberedReportForm.getDonorCode());
        queryCriteriaMap.put(OleSelectConstant.EncumberReportConstant.FROM_DATE,oleEncumberedReportForm.getFromDate());
        queryCriteriaMap.put(OleSelectConstant.EncumberReportConstant.TO_DATE,incrementDate(oleEncumberedReportForm.getToDate()));
        return queryCriteriaMap;
    }

    private String incrementDate(Date toDate){
        String toDateStr=null;
        if (toDate!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(toDate);
            c.add(Calendar.DATE,1);//Incremented date, in sql if the input todate is passed it will fetch the record till the previous day,
            // so one day needs to be added inorder to bring the records till input todate
            toDateStr=sdf.format(c.getTime());
        }
        return toDateStr;
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

    public DonorEncumberReportDAOService getDonorEncumberReportDAOService() {
        if(donorEncumberReportDAOService==null){
            donorEncumberReportDAOService=SpringContext.getBean(DonorEncumberReportDAOService.class);
        }
        return donorEncumberReportDAOService;
    }

    public void setDonorEncumberReportDAOService(DonorEncumberReportDAOService donorEncumberReportDAOService) {
        this.donorEncumberReportDAOService = donorEncumberReportDAOService;
    }
}
