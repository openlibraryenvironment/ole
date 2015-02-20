package org.kuali.ole.deliver.calendar.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.ole.deliver.calendar.service.OleCalendarService;
import org.kuali.ole.deliver.calendar.service.impl.OleCalendarDocumentServiceImpl;
import org.kuali.ole.deliver.calendar.service.impl.OleCalendarServiceImpl;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceUtils;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.MaintenanceDocumentService;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
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
 * Created with IntelliJ IDEA.
 * User: arjuns
 * Date: 7/21/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/OleCalendarController")
public class OleCalendarController extends MaintenanceDocumentController {

    private static final Logger LOG = Logger.getLogger(OleCalendarController.class);
    private OleCalendarService oleCalendarService;


    /**
     * Setups a new <code>MaintenanceDocumentView</code> with the edit maintenance
     * action
     */
    @RequestMapping(params = "methodToCall=" + KRADConstants.Maintenance.METHOD_TO_CALL_EDIT)
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.maintenanceEdit(form, result, request, response);
    }



    @RequestMapping(params = "methodToCall=" + "maintenanceDelete")
    public ModelAndView maintenanceDelete(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug(" Inside maintenanceDelete ");
        setupMaintenanceForDelete(form, request, OLEConstants.OlePatron.OLE_PATRON_DELETE);
        return getUIFModelAndView(form);
    }

    /**
     * This method returns the instance of olePatronMaintenanceDocumentService
     *
     * @return olePatronMaintenanceDocumentService(MaintenanceDocumentService)
     */
    @Override
    protected MaintenanceDocumentService getMaintenanceDocumentService() {
        return GlobalResourceLoader.getService(OLEConstants.OLE_CALENDAR_DOC_SERVICE);
    }

    /**
     * This method populates confirmation to delete the document.
     *
     * @param form
     * @param request
     * @param maintenanceAction
     */
    protected void setupMaintenanceForDelete(MaintenanceDocumentForm form, HttpServletRequest request, String maintenanceAction) {
        LOG.debug(" Inside setupMaintenanceForDelete ");
        MaintenanceDocument document = form.getDocument();
        if (document == null) {
            document = getMaintenanceDocumentService()
                    .setupNewMaintenanceDocument(form.getDataObjectClassName(), form.getDocTypeName(),
                            maintenanceAction);

            form.setDocument(document);
            form.setDocTypeName(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        }
        form.setMaintenanceAction(maintenanceAction);
        OleCalendarDocumentServiceImpl oleCalendarDocumentService = (OleCalendarDocumentServiceImpl) getMaintenanceDocumentService();
        oleCalendarDocumentService.setupMaintenanceObjectForDelete(document, maintenanceAction, request.getParameterMap());
        MaintenanceUtils.checkForLockingDocument(document, false);
    }

    /**
     * Performs the blanket approve workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be blanket approved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOG.debug(" Inside route method of patron maintenance controller ");
        ModelAndView modelAndView;
        MaintenanceDocumentForm mainForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        boolean isNew = document.isNew();
        getOleCalendarService().generalInfoValidation(oleCalendar, isNew);
        ModelAndView modelAndView1 = super.blanketApprove(mainForm, result, request, response);//changed
        getOleCalendarService().assignEndDate(oleCalendar);
        OleCalendar oldCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        getOleCalendarService().convert12HrsFormat(oldCalendar);
        return modelAndView1;
    }


    @RequestMapping(params = "methodToCall=calendarPopUp")
    public ModelAndView calendarPopUp(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm mainForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        getOleCalendarService().validateCalendarDocument(oleCalendar);
        MaintenanceUtils.checkForLockingDocument(document, false);
        return getUIFModelAndView(mainForm);
    }

    @RequestMapping(params = "methodToCall=cancelOperation")
    public ModelAndView cancelOperation(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm mainForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument)mainForm.getDocument();
        OleCalendar oleCalendar = (OleCalendar)maintenanceDocument.getNewMaintainableObject().getDataObject();
        oleCalendar.setCancelOperationFlag(true);
        oleCalendar.setCancelOperationEndDateFlag(true);
        return getUIFModelAndView(mainForm);
    }

    /**
     * This method returns the instance of olePatronMaintenanceDocumentService
     *
     * @return olePatronMaintenanceDocumentService(MaintenanceDocumentService)
     */
    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug(" Inside route method of patron maintenance controller ");
        ModelAndView modelAndView;
        MaintenanceDocumentForm mainForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        oleCalendar.setCancelOperationFlag(true);
//      Concat of 23:59:59 to EndDate while setting endDate manually
        if( oleCalendar.getEndDate()!= null && !oleCalendar.getEndDate().toString().equals("")){
        String endDateString = oleCalendar.getEndDate().toString();
        String endDateSplit[] = endDateString.split(" ");
        oleCalendar.setEndDate(Timestamp.valueOf(endDateSplit[0]+" "+"23:59:59"));
        }
//      Concat of 23:59:59 to EndDate while setting endDate manually
        oleCalendar.sortCalendarWeek(oleCalendar); //added for OLE-5381
        boolean isNew = document.isNew();
        getOleCalendarService().generalInfoValidation(oleCalendar, isNew);
        ModelAndView modelAndView1 = super.route(mainForm, result, request, response);//changed
        getOleCalendarService().assignEndDate(oleCalendar);
        OleCalendar oldCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        getOleCalendarService().convert12HrsFormat(oldCalendar);
        return modelAndView1;
    }

    @RequestMapping(params = "methodToCall=exceptionDateMethod")
    public ModelAndView exceptionDateMethod(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {

        MaintenanceDocumentForm mainForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();

        if (oleCalendar.getExceptionDayChecking().equals("Holiday")) {
            oleCalendar.setHideTime(false);
        } else {
            oleCalendar.setHideTime(true);
        }
        return getUIFModelAndView(mainForm);
    }


    @RequestMapping(params = "methodToCall=" + "deleteDocument")
    public ModelAndView deleteDocument(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOG.debug(" Inside deleteDocument ");
        MaintenanceDocument document = form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getDocumentDataObject();
        getOleCalendarService().deleteCalendarDocument(oleCalendar);
        if(GlobalVariables.getMessageMap().getErrorCount()>0){
            return getUIFModelAndView(form);
        }
        return back(form, result, request, response);
    }


    public OleCalendarService getOleCalendarService() {
        if(oleCalendarService==null){
            oleCalendarService = new OleCalendarServiceImpl();
        }
        return oleCalendarService;
    }

    public void setOleCalendarService(OleCalendarService oleCalendarService) {
        this.oleCalendarService = oleCalendarService;
    }
    @RequestMapping(params = "methodToCall=" + "calendarWeekDelete")
    public ModelAndView calendarWeekDelete(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        String selectedLineIndex = form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX);
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        oleCalendar.getOleCalendarWeekDeleteList().add(oleCalendar.getOleCalendarWeekList().get(Integer.parseInt(selectedLineIndex)));
        return deleteLine(form, result, request, response);
    }
    @RequestMapping(params = "methodToCall=" + "exceptionDayDelete")
    public ModelAndView exceptionDayDelete(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        String selectedLineIndex = form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX);
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        oleCalendar.getOleCalendarExceptionDateDeleteList().add(oleCalendar.getOleCalendarExceptionDateList().get(Integer.parseInt(selectedLineIndex)));
        return deleteLine(form, result, request, response);
    }
    @RequestMapping(params = "methodToCall=" + "exceptionPeriodListDelete")
    public ModelAndView exceptionPeriodListDelete(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        String selectedLineIndex = form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX);
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        oleCalendar.getOleCalendarExceptionPeriodDeleteList().add(oleCalendar.getOleCalendarExceptionPeriodList().get(Integer.parseInt(selectedLineIndex)));
        if(!oleCalendar.getOleCalendarExceptionPeriodList().get(Integer.parseInt(selectedLineIndex)).getExceptionPeriodType().equals("Holiday")){
            oleCalendar.getOleCalendarExceptionPeriodDeleteWeekList().addAll(oleCalendar.getOleCalendarExceptionPeriodList().get(Integer.parseInt(selectedLineIndex)).getOleCalendarExceptionPeriodWeekList());
        }
        return deleteLine(form, result, request, response);
    }
    @RequestMapping(params = "methodToCall=" + "exceptionPeriodWeekListDelete")
    public ModelAndView exceptionPeriodWeekListDelete(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        Map<String,String> actionParameters = form.getActionParameters();
        String subCollectionIndex = actionParameters.get(UifParameters.SELECTED_LINE_INDEX);
        String mainCollectionIndex = StringUtils.substringBefore(StringUtils.substringAfter(actionParameters.get(UifParameters.SELLECTED_COLLECTION_PATH), "["), "]");
        OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek = oleCalendar.getOleCalendarExceptionPeriodList().get(Integer.parseInt(mainCollectionIndex)).getOleCalendarExceptionPeriodWeekList().get(Integer.parseInt(subCollectionIndex));
        if(oleCalendar.getCalendarId()!=null){
            oleCalendar.getOleCalendarExceptionPeriodDeleteWeekList().add(oleCalendarExceptionPeriodWeek);    //inr list delete
            oleCalendar.getOleCalendarExceptionPeriodList().get(Integer.parseInt(mainCollectionIndex)).getOleCalendarExceptionPeriodWeekList().remove(Integer.parseInt(subCollectionIndex));
            return getUIFModelAndView(form);
        }else{
            oleCalendar.getOleCalendarExceptionPeriodDeleteWeekList().add(oleCalendarExceptionPeriodWeek);
            return deleteLine(form, result, request, response);
        }
    }
    @RequestMapping(params = "methodToCall=" + "exceptionPeriodListAdd")
    public ModelAndView exceptionPeriodListAdd(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        String selectedLineIndex = form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX);
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        List<OleCalendarExceptionPeriod>  oleCalendarExceptionPeriods=oleCalendar.getOleCalendarExceptionPeriodList();
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(form, addLinePath);
        OleCalendarExceptionPeriod oleCalendarExceptionPeriod=(OleCalendarExceptionPeriod)eventObject;
        boolean isValid=true;
        if(oleCalendarExceptionPeriod!=null){

                if(oleCalendarExceptionPeriod.getBeginDate()==null ||
                        oleCalendarExceptionPeriod.getBeginDate()!=null && oleCalendarExceptionPeriod.getBeginDate().toString().equalsIgnoreCase("")){
                    GlobalVariables.getMessageMap().putErrorForSectionId("ExceptionPeriod", OLEConstants.CALENDAR_PERIOD_BEGIN_DATE_EMPTY);
                    isValid=false;
                }
                if(isValid && oleCalendarExceptionPeriod.getEndDate()==null ||
                        oleCalendarExceptionPeriod.getEndDate()!=null && oleCalendarExceptionPeriod.getEndDate().toString().equalsIgnoreCase("")){
                    GlobalVariables.getMessageMap().putErrorForSectionId("ExceptionPeriod",OLEConstants.CALENDAR_PERIOD_END_DATE_EMPTY);
                    isValid=false;
                }

                if (isValid && oleCalendarExceptionPeriod.getBeginDate() != null && !oleCalendarExceptionPeriod.getBeginDate().toString().equalsIgnoreCase("")
                        && !oleCalendarExceptionPeriod.getEndDate().toString().equalsIgnoreCase("") && oleCalendarExceptionPeriod.getEndDate() != null) {

                    if(isValid && oleCalendarExceptionPeriod.getBeginDate().after(oleCalendarExceptionPeriod.getEndDate())){
                        GlobalVariables.getMessageMap().putErrorForSectionId("ExceptionPeriod", OLEConstants.CALENDAR_BEGIN_END_DATE);
                        isValid=false;
                    }
                    if(oleCalendarExceptionPeriods!=null){
                        for (OleCalendarExceptionPeriod calendarExceptionPeriod : oleCalendarExceptionPeriods) {
                            if (validateDateRange(calendarExceptionPeriod.getBeginDate(), calendarExceptionPeriod.getEndDate(), oleCalendarExceptionPeriod.getBeginDate())) {
                                GlobalVariables.getMessageMap().putErrorForSectionId("ExceptionPeriod",OLEConstants.CALENDAR_PERIOD_RANGE_OVERLAP_ERROR);
                                isValid=false;
                                break;
                            }
                            if (validateDateRange(calendarExceptionPeriod.getBeginDate(), calendarExceptionPeriod.getEndDate(), oleCalendarExceptionPeriod.getEndDate())) {
                                GlobalVariables.getMessageMap().putErrorForSectionId("ExceptionPeriod", OLEConstants.CALENDAR_PERIOD_RANGE_OVERLAP_ERROR);
                                isValid=false;
                                break;
                            }
                        }
                    }

                }
            if (isValid) {
                View view = form.getPostedView();
                view.getViewHelperService().processCollectionAddLine(view, form, selectedCollectionPath);
            } else {
                return getUIFModelAndView(form);
            }

        }
        return getUIFModelAndView(form);
    }

    private boolean validateDateRange(Date fromDate ,Date toDate ,Date date){
        if(date.after(fromDate) && date.before(toDate) || DateUtils.isSameDay(fromDate,date) || DateUtils.isSameDay(toDate,date)){
            return true;
        } else {
            return false;
        }
    }

}
