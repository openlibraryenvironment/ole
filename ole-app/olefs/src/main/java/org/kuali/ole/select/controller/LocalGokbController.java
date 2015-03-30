package org.kuali.ole.select.controller;

import org.kuali.ole.select.form.LocalGokbForm;
import org.kuali.ole.select.gokb.service.GokbLocalService;
import org.kuali.ole.select.gokb.service.GokbRdbmsService;
import org.kuali.ole.select.gokb.service.impl.GokbLocalServiceImpl;
import org.kuali.ole.select.gokb.service.impl.GokbRdbmsServiceImpl;
import org.kuali.ole.select.gokb.OleGokbUpdateLog;
import org.kuali.ole.select.gokb.service.impl.GokbThread;
import org.kuali.ole.select.gokb.util.OleGokbXmlUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
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
import java.sql.Timestamp;
import java.util.*;


/**
 * Created by premkumarv on 12/18/14.
 */
@Controller
@RequestMapping(value = "/localGokbController")
public class LocalGokbController extends UifControllerBase {
    private GokbRdbmsService gokbRdbmsService;
    private GokbLocalService gokbLocalService;
    private BusinessObjectService businessObjectService;

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        UifFormBase uifFormBase = null;
        uifFormBase = new LocalGokbForm();
        return uifFormBase;
    }

    public GokbRdbmsService getGokbRdbmsService() {
        if (null == gokbRdbmsService) {
            return new GokbRdbmsServiceImpl();
        }
        return gokbRdbmsService;
    }

    public GokbLocalService getGokbLocalService() {
        if (null == gokbLocalService) {
            return new GokbLocalServiceImpl();
        }
        return gokbLocalService;
    }
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }



    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
       LocalGokbForm localGokbForm = (LocalGokbForm)form;
        return getUIFModelAndView(localGokbForm);
    }


    @RequestMapping(params = "methodToCall=initOrUpdateLocalGokb")
    public ModelAndView initOrUpdateLocalGokb(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        GokbThread gokbThread = new GokbThread();
        gokbThread.start();
        return getUIFModelAndView(getLocalGokbForm((LocalGokbForm) form));

    }

    @RequestMapping(params = "methodToCall=refreshStatus")
    public ModelAndView refreshStatus(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        LocalGokbForm localGokbForm = getLocalGokbForm((LocalGokbForm) form);

        return getUIFModelAndView(localGokbForm);

    }

    private LocalGokbForm getLocalGokbForm(LocalGokbForm form) {
        LocalGokbForm localGokbForm = (LocalGokbForm)form;
        List<OleGokbUpdateLog> oleGokbUpdateLogList = new ArrayList<OleGokbUpdateLog>();
        List<OleGokbUpdateLog> oleGokbUpdateLogs =  (List<OleGokbUpdateLog>) KRADServiceLocator.getBusinessObjectService().findAll(OleGokbUpdateLog.class);
        Collections.reverse(oleGokbUpdateLogs);
        for (OleGokbUpdateLog oleGokbUpdateLog : oleGokbUpdateLogs) {
            if(oleGokbUpdateLog.getId() != null) {
                localGokbForm.setId(oleGokbUpdateLog.getId().toString());
            }
            if(oleGokbUpdateLog.getNoOfPackages() != null){
                localGokbForm.setNoOfPackages(oleGokbUpdateLog.getNoOfPackages().toString());
            }
            if(oleGokbUpdateLog.getNoOfTipps() != null){
                localGokbForm.setNoOfTipps(oleGokbUpdateLog.getNoOfTipps().toString());
            }
            if(oleGokbUpdateLog.getNoOfTitles() != null) {
                localGokbForm.setNoOfTitles(oleGokbUpdateLog.getNoOfTitles().toString());
            }
            if(oleGokbUpdateLog.getNoOfOrganization() != null) {
                localGokbForm.setNoOfOrganization(oleGokbUpdateLog.getNoOfOrganization().toString());
            }
            if(oleGokbUpdateLog.getNoOfPlatforms() != null) {
                localGokbForm.setNoOfPlatforms(oleGokbUpdateLog.getNoOfPlatforms().toString());
            }
            if(oleGokbUpdateLog.getStatus() != null) {
                localGokbForm.setStatus(oleGokbUpdateLog.getStatus());
            }
            if(oleGokbUpdateLog.getStartTime() != null) {
                localGokbForm.setStartTime(oleGokbUpdateLog.getStartTime());
            }
            if(oleGokbUpdateLog.getEndTime() != null) {
                localGokbForm.setEndTime(oleGokbUpdateLog.getEndTime());
            }
           break;
        }
        oleGokbUpdateLogList.addAll(oleGokbUpdateLogs);
        localGokbForm.setOleGokbUpdateLogList(oleGokbUpdateLogList);
        return localGokbForm;
    }


}
