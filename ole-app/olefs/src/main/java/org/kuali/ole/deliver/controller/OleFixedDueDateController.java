package org.kuali.ole.deliver.controller;


import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleFixedDateTimeSpan;
import org.kuali.ole.deliver.bo.OleFixedDueDate;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/19/12
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/oleFixedDueDate")
public class OleFixedDueDateController extends MaintenanceDocumentController {

    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleFixedDueDate oleNewFixedDueDate = (OleFixedDueDate) document.getNewMaintainableObject().getDataObject();
        DateTimeService dateTimeService = CoreApiServiceLocator.getDateTimeService();
        if (oleNewFixedDueDate.getOleFixedDateTimeSpanList() != null && oleNewFixedDueDate.getOleFixedDateTimeSpanList().size() > 0) {
            for (int i = 0; i < oleNewFixedDueDate.getOleFixedDateTimeSpanList().size(); i++) {
                java.util.Date fixedDueDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i).getFixedDueDate();
                java.util.Date fromDueDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i).getFromDueDate();
                java.util.Date toDueDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i).getToDueDate();
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
                Date newFrom = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i).getFromDueDate();
                String newFromDate = null;
                String newToDate = null;
                if (newFrom == null) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.FROM_DUE_DATE_FIELD, OLEConstants.FROM_DUE_DATE_MANDATORY);
                } else {
                    newFromDate = sdf1.format(newFrom);
                }
                Date newTo = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i).getToDueDate();
                if (newTo == null) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.TO_DUE_DATE_FIELD, OLEConstants.TO_DUE_DATE_MANDATORY);
                } else {
                    newToDate = sdf1.format(newTo);
                }
                if (fixedDueDate == null) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.FIXED_DUE_DATE_FIELD, OLEConstants.FIXED_DUE_DATE_MANDATORY);
                }
                if (fromDueDate == null || toDueDate == null || fixedDueDate == null) {
                    return getUIFModelAndView(form);
                }
                if (dateTimeService.dateDiff(newFrom, newTo, Boolean.FALSE) < 0) {
                    GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.FROM_DATE_LESS_TO_DATE);
                    return getUIFModelAndView(form);
                }
                String newTimeSpan = newFromDate + "-" + newToDate;
                (oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i)).setTimeSpan(newTimeSpan);

                for (int j = i + 1; j < oleNewFixedDueDate.getOleFixedDateTimeSpanList().size(); j++) {
                    java.util.Date oldFromDueDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(j).getFromDueDate();
                    java.util.Date oldToDueDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(j).getToDueDate();

                    java.util.Date fixedDueDate1 = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(j).getFixedDueDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date from = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i).getFromDueDate();
                    String fromDate = null;
                    String toDate = null;
                    if (from == null) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.FROM_DUE_DATE_FIELD, OLEConstants.FROM_DUE_DATE_MANDATORY);
                    } else {
                        fromDate = sdf.format(from);
                    }
                    Date to = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i).getToDueDate();
                    if (to == null) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.TO_DUE_DATE_FIELD, OLEConstants.TO_DUE_DATE_MANDATORY);
                    } else {
                        toDate = sdf.format(to);
                    }
                    //String toDate = sdf.format((oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i)).getToDueDate());
                    Date fixedDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i).getFixedDueDate();
                    if (fixedDate == null) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.FIXED_DUE_DATE_FIELD, OLEConstants.FIXED_DUE_DATE_MANDATORY);
                    }

                    if (from == null || to == null || fixedDate == null) {
                        return getUIFModelAndView(form);
                    }
                    if(dateTimeService.dateDiff(fixedDueDate,fixedDueDate1,Boolean.FALSE) ==0) {
                        GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.FIXED_DUE_DATE_DUPLICATE_FIELD);
                        return getUIFModelAndView(form);
                    }
                    if (dateTimeService.dateDiff(from, to, Boolean.FALSE) < 0) {
                        GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.FROM_DATE_LESS_TO_DATE);
                        return getUIFModelAndView(form);
                    }
                    int fromDueDateDiff = dateTimeService.dateDiff(oldFromDueDate,toDueDate,Boolean.FALSE);
                    int toDueDateDiff = dateTimeService.dateDiff(oldToDueDate,toDueDate,Boolean.FALSE);
                    int toDueDateDiff1 = dateTimeService.dateDiff(fromDueDate,oldToDueDate,Boolean.FALSE);
                    if(!(toDueDateDiff1 < 0)  && !(fromDueDateDiff < 0 && toDueDateDiff < 0)) {
                        GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.TIME_SPAN_OVERLAP);
                        return getUIFModelAndView(form);
                    }
                    String timeSpan = fromDate + "-" + toDate;
                    (oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(i)).setTimeSpan(timeSpan);
                }
            }
        } else {
            GlobalVariables.getMessageMap().putError(OLEConstants.FIXED_DUE_DATE_FIELD, OLEConstants.TIME_SPAN_MANDATORY);
            return getUIFModelAndView(form);
        }
        List<OleFixedDateTimeSpan> oleDeleteFixedDateTimeSpanList = oleNewFixedDueDate.getOleDeleteFixedDateTimeSpanList();
        for (OleFixedDateTimeSpan oleFixedDateTimeSpan : oleDeleteFixedDateTimeSpanList) {
            if (oleFixedDateTimeSpan.getOleFixedDateTimeSpanId() != null) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("OleFixedDateTimeSpanId", oleFixedDateTimeSpan.getOleFixedDateTimeSpanId());
                KRADServiceLocator.getBusinessObjectService().deleteMatching(OleFixedDateTimeSpan.class, map);
            }
        }
        return super.route(form, result, request, response);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=validateAddLine")
    public ModelAndView validateAddLine(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) maintenanceForm.getDocument();
        String selectedCollectionPath = maintenanceForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = maintenanceForm.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(maintenanceForm, addLinePath);
        OleFixedDateTimeSpan oleFixedDateTimeSpan = (OleFixedDateTimeSpan) eventObject;
        if(oleFixedDateTimeSpan.getFromDueDate()!=null && oleFixedDateTimeSpan.getToDueDate()!=null && oleFixedDateTimeSpan.getFixedDueDate()!=null)    {
            GlobalVariables.getMessageMap().removeAllErrorMessagesForProperty(OLEConstants.FROM_DUE_DATE_FIELD);
            OleFixedDueDate oleNewFixedDueDate = (OleFixedDueDate) maintenanceDocument.getNewMaintainableObject().getDataObject();
            java.util.Date newFromDueDate = oleFixedDateTimeSpan.getFromDueDate();
            java.util.Date newToDueDate = oleFixedDateTimeSpan.getToDueDate();
            java.util.Date newFixedDueDate = oleFixedDateTimeSpan.getFixedDueDate();
            DateTimeService dateTimeService = CoreApiServiceLocator.getDateTimeService();
            if(dateTimeService.dateDiff(newFromDueDate,newToDueDate,Boolean.FALSE) <0) {
                GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.FROM_DATE_LESS_TO_DATE);
                return getUIFModelAndView(form);
            }
            for (int dueDateCounter = 0; dueDateCounter < oleNewFixedDueDate.getOleFixedDateTimeSpanList().size(); dueDateCounter++) {
                java.util.Date fromDueDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(dueDateCounter).getFromDueDate();
                java.util.Date toDueDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(dueDateCounter).getToDueDate();
                java.util.Date fixedDueDate = oleNewFixedDueDate.getOleFixedDateTimeSpanList().get(dueDateCounter).getFixedDueDate();
                if(dateTimeService.dateDiff(newFixedDueDate,fixedDueDate,Boolean.FALSE) ==0) {
                    GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.FIXED_DUE_DATE_DUPLICATE_FIELD);
                    return getUIFModelAndView(form);
                }
                int fromDueDateDiff = dateTimeService.dateDiff(fromDueDate,newToDueDate,Boolean.FALSE);
                int toDueDateDiff = dateTimeService.dateDiff(toDueDate,newToDueDate,Boolean.FALSE);
                int toDueDateDiff1 = dateTimeService.dateDiff(newFromDueDate,toDueDate,Boolean.FALSE);
                if(!(toDueDateDiff1 < 0)  && !(fromDueDateDiff < 0 && toDueDateDiff < 0)) {
                    GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.TIME_SPAN_OVERLAP);
                    return getUIFModelAndView(form);
                }
            }
            ModelAndView modelAndView = super.addLine(form, result, request, response);
            return modelAndView;
        }else {
            if(oleFixedDateTimeSpan.getFromDueDate() == null)
                GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.FROM_DUE_DATE_MANDATORY);
            if(oleFixedDateTimeSpan.getToDueDate() == null)
                GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.TO_DUE_DATE_MANDATORY);
            if(oleFixedDateTimeSpan.getFixedDueDate() == null)
                GlobalVariables.getMessageMap().putErrorForSectionId("create_timeSpan", OLEConstants.FIXED_DUE_DATE_MANDATORY);
            return getUIFModelAndView(form);
        }
    }


    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=timeSpanDelete")
    public ModelAndView timeSpanDelete(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized deleteLine method");
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        String selectedLineIndex = form.getActionParamaterValue("selectedLineIndex");
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleFixedDueDate oleFixedDueDate = (OleFixedDueDate) document.getNewMaintainableObject().getDataObject();
        oleFixedDueDate.getOleDeleteFixedDateTimeSpanList().add(oleFixedDueDate.getOleFixedDateTimeSpanList().get(Integer.parseInt(selectedLineIndex)));
        return deleteLine(uifForm, result, request, response);
    }

    @Override
    @RequestMapping(params = "methodToCall=maintenanceCopy")
    public ModelAndView maintenanceCopy(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        setupMaintenance(form, request, KRADConstants.MAINTENANCE_COPY_ACTION);
        super.maintenanceCopy(form, result, request, response);
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleFixedDueDate oleNewFixedDueDate = (OleFixedDueDate) document.getNewMaintainableObject().getDataObject();
        List<OleFixedDateTimeSpan> oleDeleteFixedDateTimeSpanList = oleNewFixedDueDate.getOleFixedDateTimeSpanList();
        oleNewFixedDueDate.setFixedDueDateId(null);
        oleNewFixedDueDate.setObjectId(null);
        oleNewFixedDueDate.setVersionNumber(null);
        for (OleFixedDateTimeSpan oleFixedDateTimeSpan : oleDeleteFixedDateTimeSpanList) {
            oleFixedDateTimeSpan.setOleFixedDateTimeSpanId(null);
            oleFixedDateTimeSpan.setObjectId(null);
            oleFixedDateTimeSpan.setVersionNumber(null);
            oleFixedDateTimeSpan.setOleFixedDueDate(oleNewFixedDueDate);
        }
        return getUIFModelAndView(form);
    }
}