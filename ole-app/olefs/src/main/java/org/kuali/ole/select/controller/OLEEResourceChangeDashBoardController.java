package org.kuali.ole.select.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.form.OLEEResourceChangeDashboardForm;
import org.kuali.ole.select.gokb.*;
import org.kuali.ole.service.OLEEResourceHelperService;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
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
 * Created by srirams on 18/9/14.
 */
@Controller
@RequestMapping(value = "/oleEResourceChangeDashBoardController")

public class OLEEResourceChangeDashBoardController extends UifControllerBase {

    private OLEEResourceSearchService oleEResourceSearchService = null;

    private OLEEResourceHelperService oleeResourceHelperService = null;

    private BusinessObjectService businessObjectService = null;

    public OLEEResourceSearchService getOleEResourceSearchService() {
        if (oleEResourceSearchService == null) {
            oleEResourceSearchService = GlobalResourceLoader.getService(OLEConstants.OLEEResourceRecord.ERESOURSE_SEARCH_SERVICE);
        }
        return oleEResourceSearchService;
    }
    public OLEEResourceHelperService getOleeResourceHelperService() {
        if(oleeResourceHelperService == null) {
            oleeResourceHelperService = new OLEEResourceHelperService();
        }
        return oleeResourceHelperService;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEEResourceChangeDashboardForm();
    }

    /**
     *  This method takes the initial request when click on Change DashBoard
     * @param form
     * @param request
     * @param response
     * @return
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form ,BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm)form;
        List<OleGokbReview> gokbReviewList = (List<OleGokbReview>) KRADServiceLocator.getBusinessObjectService().findAll(OleGokbReview.class);
        List<OleGokbChangeLog> oleGokbChangeLogs =  (List<OleGokbChangeLog>) getBusinessObjectService().findAll(OleGokbChangeLog.class);
        eResourceChangeDashboardForm.setOleGokbChangeLogs(oleGokbChangeLogs);
        eResourceChangeDashboardForm.setOleGokbReviewList(gokbReviewList);
        eResourceChangeDashboardForm.setOleGokbReviews(gokbReviewList);
        List<String> dates = new ArrayList<>();
        List<String> detailList = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<String> eresources = new ArrayList<>();
        List<String> origins = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for(OleGokbReview oleGokbReview : eResourceChangeDashboardForm.getOleGokbReviewList()){
            String date = oleGokbReview.getReviewDate().toString();
            String details = oleGokbReview.getDetails();
            String type = oleGokbReview.getType();
            String resource = oleGokbReview.getOleERSIdentifier();
           /* String origin = oleGokbReview.getOrigin();
            String title = oleGokbReview.getTitle();*/
            if (StringUtils.isNotEmpty(date))
                dates.add(date);
            if (StringUtils.isNotEmpty(type))
                types.add(type);
            if (StringUtils.isNotEmpty(details))
                detailList.add(details);
            if(StringUtils.isNotEmpty(resource))
                eresources.add(resource);
           /* if(StringUtils.isNotEmpty(origin))
                origins.add(origin);
            if(StringUtils.isNotEmpty(title))
                titles.add(title);*/
        }
        eResourceChangeDashboardForm.setDate(dates);
        eResourceChangeDashboardForm.setType(types);
        eResourceChangeDashboardForm.setDetail(detailList);
        eResourceChangeDashboardForm.setEresource(eresources);
        eResourceChangeDashboardForm.setOrigin(origins);
        eResourceChangeDashboardForm.setTitle(titles);
        return super.start(eResourceChangeDashboardForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=submit")
    public ModelAndView submit(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm)form;
        List<OleGokbReview> oleGokbReviews = eResourceChangeDashboardForm.getOleGokbReviews();
        ListIterator<OleGokbReview> oleGokbReviewListIterator = oleGokbReviews.listIterator();
        while (oleGokbReviewListIterator.hasNext()){
             OleGokbReview oleGokbReview = oleGokbReviewListIterator.next();
            OLEEResourceRecordDocument oleeResourceRecordDocument = getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class, oleGokbReview.getOleERSIdentifier());
            if(oleGokbReview.isApprove()){
                if(oleGokbReview.getOleERSIdentifier()!=null){
                    List<OleGokbTipp> oleGokbTipps = new ArrayList<>();
                    OleGokbTipp oleGokbTipp = getBusinessObjectService().findBySinglePrimaryKey(OleGokbTipp.class, oleGokbReview.getGokbTippId());
                    oleGokbTipps.add(oleGokbTipp);
                    getOleeResourceHelperService().addTippToEresource(oleeResourceRecordDocument, oleGokbTipps, "");
                    createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(), "New E-Instance", "E-Instance added to OLE");
                }
            }
            if(oleGokbReview.isClear()){
                OleGokbChangeLog oleGokbChangeLog = createChangeLog(oleeResourceRecordDocument.getOleERSIdentifier(),oleGokbReview.getType(), oleGokbReview.getDetails());
                getBusinessObjectService().delete(oleGokbReview);
                oleGokbReviewListIterator.remove();
                eResourceChangeDashboardForm.getOleGokbChangeLogs().add(oleGokbChangeLog);
            }
        }
        return getUIFModelAndView(eResourceChangeDashboardForm);
    }

    private OleGokbChangeLog createChangeLog(String eResId, String type, String details){
        OleGokbChangeLog oleGokbChangeLog = new OleGokbChangeLog();
        oleGokbChangeLog.setType(type);
        oleGokbChangeLog.setDetails(details);
        oleGokbChangeLog.setOleERSIdentifier(eResId);
        oleGokbChangeLog.setChangeLogDate(new Timestamp(System.currentTimeMillis()));
        oleGokbChangeLog.setOrigin("Manual");
        getBusinessObjectService().save(oleGokbChangeLog);
        return oleGokbChangeLog;
    }


    @RequestMapping(params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm)form;
        return super.navigate(eResourceChangeDashboardForm, result, request, response);
    }

    /**
     * This method filter the results
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(params = "methodToCall=filterResults")
    public ModelAndView filterResults(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm)form;
        String selectedDate = eResourceChangeDashboardForm.getSelectedDate();
        String selectedDetails = eResourceChangeDashboardForm.getSelectedDetails();
        String selectedType = eResourceChangeDashboardForm.getSelectedType();
        String selectedEresource = eResourceChangeDashboardForm.getSelectedEresource();
        /*String selectedOrigin = eResourceChangeDashboardForm.getSelectedOrigin();
        String selectedTitle = eResourceChangeDashboardForm.getSelectedTitle();*/
        List<OleGokbReview> gokbReviewList = new ArrayList<>();
        List<OleGokbReview> oleGokbReviews = eResourceChangeDashboardForm.getOleGokbReviewList();
        for (OleGokbReview oleGokbReview : oleGokbReviews) {
            if (StringUtils.isNotEmpty(oleGokbReview.getReviewDate().toString()) && oleGokbReview.getReviewDate().toString().equalsIgnoreCase(selectedDate)) {
                gokbReviewList.add(oleGokbReview);
                eResourceChangeDashboardForm.setSelectedDate("");
                eResourceChangeDashboardForm.setOleGokbReviews(gokbReviewList);
            }
            else if (StringUtils.isNotEmpty(oleGokbReview.getDetails()) && oleGokbReview.getDetails().equalsIgnoreCase(selectedDetails)) {
                gokbReviewList.add(oleGokbReview);
                eResourceChangeDashboardForm.setSelectedDetails("");
                eResourceChangeDashboardForm.setOleGokbReviews(gokbReviewList);
            }
            else if (StringUtils.isNotEmpty(oleGokbReview.getType()) && oleGokbReview.getType().equalsIgnoreCase(selectedType)) {
                gokbReviewList.add(oleGokbReview);
                eResourceChangeDashboardForm.setSelectedType("");
                eResourceChangeDashboardForm.setOleGokbReviews(gokbReviewList);
            }
            else if(StringUtils.isNotEmpty(oleGokbReview.getOleERSIdentifier()) && oleGokbReview.getOleERSIdentifier().equalsIgnoreCase(selectedEresource)) {
                gokbReviewList.add(oleGokbReview);
                eResourceChangeDashboardForm.setSelectedEresource("");
                eResourceChangeDashboardForm.setOleGokbReviews(gokbReviewList);
            }
        }
        return getUIFModelAndView(eResourceChangeDashboardForm);
    }


    @RequestMapping(params = "methodToCall=clear")
    public ModelAndView clear(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm)form;
        eResourceChangeDashboardForm.setSelectedEresource("");
        eResourceChangeDashboardForm.setSelectedDate("");
        eResourceChangeDashboardForm.setSelectedType("");
        eResourceChangeDashboardForm.setSelectedDetails("");
        eResourceChangeDashboardForm.setSelectedOrigin("");
        return getUIFModelAndView(eResourceChangeDashboardForm);
    }

    @RequestMapping(params = "methodToCall=selectTab")
    public ModelAndView selectTab(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm)form;
        return super.navigate(eResourceChangeDashboardForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=approve")
    public ModelAndView approve(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm)form;

        return super.navigate(eResourceChangeDashboardForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=archive")
    public ModelAndView archive(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceChangeDashboardForm eResourceChangeDashboardForm = (OLEEResourceChangeDashboardForm)form;
        if(eResourceChangeDashboardForm.getArchivedDate()!=null){
            List<OleGokbChangeLog> oleGokbChangeLogs = (List<OleGokbChangeLog>)getBusinessObjectService().findAll(OleGokbChangeLog.class);
            for(OleGokbChangeLog gokbChangeLog :oleGokbChangeLogs){
                if(gokbChangeLog.getChangeLogDate().before(eResourceChangeDashboardForm.getArchivedDate())){
                OleGokbArchive archive = new OleGokbArchive();
                archive.setOleERSIdentifier(gokbChangeLog.getOleERSIdentifier());
                archive.setOrigin(gokbChangeLog.getOrigin());
                archive.setDetails(gokbChangeLog.getDetails());
                archive.setGokbTippId(gokbChangeLog.getGokbTippId());
                archive.setType(gokbChangeLog.getType());
                archive.setArchivedDate(new Timestamp(System.currentTimeMillis()));
                getBusinessObjectService().save(archive);
                getBusinessObjectService().delete(gokbChangeLog);
                }
            }
        }
        return getUIFModelAndView(eResourceChangeDashboardForm);
    }

}
