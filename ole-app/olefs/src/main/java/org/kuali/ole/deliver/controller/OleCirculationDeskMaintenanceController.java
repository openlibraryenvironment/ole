package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskFeeType;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.bo.OleFeeType;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/6/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/oleCirculationDeskMaintenance")
public class OleCirculationDeskMaintenanceController extends MaintenanceDocumentController {

    /**
     * Default method for controller that setups a new
     * <code>MaintenanceView</code> with the default new action
     */
    @RequestMapping(params = "methodToCall=" + KRADConstants.Maintenance.METHOD_TO_CALL_NEW)
    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        return super.start(form, result, request, response);
    }


    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleCirculationDesk oleCirculationDesk = (OleCirculationDesk) document.getNewMaintainableObject().getDataObject();
        if(!CollectionUtils.isNotEmpty(oleCirculationDesk.getOleCirculationDeskLocations())){
            oleCirculationDesk.setOleCirculationDeskLocations(new ArrayList<OleCirculationDeskLocation>());
        }
        oleCirculationDesk.getOleCirculationDeskLocations().clear();
        if(CollectionUtils.isNotEmpty(oleCirculationDesk.getOleCirculationDeskLocationList())){
            oleCirculationDesk.getOleCirculationDeskLocations().addAll(oleCirculationDesk.getOleCirculationDeskLocationList());
        }
        if(CollectionUtils.isNotEmpty(oleCirculationDesk.getOlePickupCirculationDeskLocations())){
            oleCirculationDesk.getOleCirculationDeskLocations().addAll(oleCirculationDesk.getOlePickupCirculationDeskLocations());
        }
        return super.route(form, result, request, response);
    }

    /**
     * This method populates date of the eventlog object thereby adding to the existing list.
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addCirculationLine")
    public ModelAndView addCirculationLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();

        OleCirculationDesk oleCirculationDesk = (OleCirculationDesk) document.getNewMaintainableObject().getDataObject();

        List<OleCirculationDeskLocation> oleCirculationDeskLocations = oleCirculationDesk.getOleCirculationDeskLocationList();
        /*OleCirculationDeskLocation oleCirculationDeskLocation=oleCirculationDeskLocations.get(0);
        if(oleCirculationDeskLocation.getCirculationDeskLocationId()==null || oleCirculationDeskLocation.getCirculationLocationCode()==null){
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Invalid Location");
        }*/
        oleCirculationDesk.setErrorMessage(null);
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();

        Object eventObject = ObjectPropertyUtils.getPropertyValue(form, addLinePath);
        OleCirculationDeskLocation oleCirculationDeskLocation = (OleCirculationDeskLocation) eventObject;
        String fullLocationPath = oleCirculationDeskLocation.getCirculationFullLocationCode();
        String[] locations = fullLocationPath.split("/");
        Map<String, String> map = new HashMap<String, String>();
        if (locations.length > 1) {
            map.put("locationCode", locations[locations.length - 1]);
        } else {
            map.put("locationCode", locations[0]);
        }
        List<OleLocation> oleLocations = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class, map);
        if (oleLocations.size() == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId("OleCirculationDesk-Locations", OLEConstants.OleCirculationDesk.OLE_INVALID_CIRCULATION_DESK_LOCATION);
            return getUIFModelAndView(form);
        }
        OleLocation oleLocation = null;
        if (oleLocations.size() > 0) {
            oleLocation = oleLocations.get(0);
            oleCirculationDeskLocation.setCirculationLocationCode(oleLocation.getLocationCode());
            oleCirculationDeskLocation.setCirculationDeskLocation(oleLocation.getLocationId());
            oleCirculationDeskLocation.setOleCirculationDesk(oleCirculationDesk);
        }
        for (OleCirculationDeskLocation oleCirculationDeskLocation1 : oleCirculationDeskLocations) {
            if (oleCirculationDeskLocation1.getCirculationDeskLocation().equalsIgnoreCase(oleCirculationDeskLocation.getCirculationDeskLocation())) {
                oleCirculationDeskLocation.setCirculationDeskLocation(null);
                oleCirculationDeskLocation.setCirculationLocationCode(null);

                GlobalVariables.getMessageMap().putErrorForSectionId("OleCirculationDesk-Locations", OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_DUPLICATE_ERROR);
                return getUIFModelAndView(form);
            }
        }


        View view = form.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, form, selectedCollectionPath);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=addCirculationFeeType")
    public ModelAndView addCirculationFeeType(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();

        OleCirculationDesk oleCirculationDesk = (OleCirculationDesk) document.getNewMaintainableObject().getDataObject();

        List<OleCirculationDeskFeeType> oleFeeTypeList = oleCirculationDesk.getOleCirculationDeskFeeTypeList();
        oleCirculationDesk.setErrorMessage(null);
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();

        Object eventObject = ObjectPropertyUtils.getPropertyValue(form, addLinePath);
        OleCirculationDeskFeeType oleFeeType = (OleCirculationDeskFeeType) eventObject;
        String feeTypeCode = oleFeeType.getFeeTypeCode();
        String feeTypeId = oleFeeType.getFeeTypeId();
        Map<String, String> map = new HashMap<String, String>();
        map.put("feeTypeCode",feeTypeCode);
        List<OleFeeType> oleFeeTypes = (List<OleFeeType>)KRADServiceLocator.getBusinessObjectService().findMatching(OleFeeType.class,map);
        if (oleFeeTypes.size() == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId("OleCirculationDesk-FeeType", OLEConstants.OleCirculationDesk.OLE_INVALID_FEE_TYPE);
            return getUIFModelAndView(form);
        }
        OleFeeType oleFeeType1 = null;
        if(oleFeeTypes.size()>0){
            oleFeeType1 = oleFeeTypes.get(0);
            oleFeeType.setFeeTypeCode(oleFeeType1.getFeeTypeCode());
            oleFeeType.setFeeTypeId(oleFeeType1.getFeeTypeId());
            oleFeeType.setOleCirculationDesk(oleCirculationDesk);
        }

        for (OleCirculationDeskFeeType oleCirculationDeskFeeType : oleFeeTypeList) {
            if (oleCirculationDeskFeeType.getFeeTypeCode().equalsIgnoreCase(oleFeeType.getFeeTypeCode())) {
                oleFeeType.setFeeTypeCode(null);


                GlobalVariables.getMessageMap().putErrorForSectionId("OleCirculationDesk-Locations", OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_DUPLICATE_ERROR);
                return getUIFModelAndView(form);
            }
        }



        View view = form.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, form, selectedCollectionPath);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=addPickupCirculationLine")
    public ModelAndView addPickupCirculationLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();

        OleCirculationDesk oleCirculationDesk = (OleCirculationDesk) document.getNewMaintainableObject().getDataObject();

        List<OleCirculationDeskLocation> oleCirculationDeskLocations = oleCirculationDesk.getOlePickupCirculationDeskLocations();
        /*OleCirculationDeskLocation oleCirculationDeskLocation=oleCirculationDeskLocations.get(0);
        if(oleCirculationDeskLocation.getCirculationDeskLocationId()==null || oleCirculationDeskLocation.getCirculationLocationCode()==null){
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Invalid Location");
        }*/
        oleCirculationDesk.setErrorMessage(null);
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();

        Object eventObject = ObjectPropertyUtils.getPropertyValue(form, addLinePath);
        OleCirculationDeskLocation oleCirculationDeskLocation = (OleCirculationDeskLocation) eventObject;
        String fullLocationPath = oleCirculationDeskLocation.getCirculationFullLocationCode();
        String[] locations = fullLocationPath.split("/");
        Map<String, String> map = new HashMap<String, String>();
        if (locations.length > 1) {
            map.put("locationCode", locations[locations.length - 1]);
        } else {
            map.put("locationCode", locations[0]);
        }
        List<OleLocation> oleLocations = (List<OleLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class, map);
        if (oleLocations.size() == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId("OleCirculationDesk-Pickup-Locations", OLEConstants.OleCirculationDesk.OLE_INVALID_CIRCULATION_DESK_LOCATION);
            return getUIFModelAndView(form);
        }
        OleLocation oleLocation = null;
        if (oleLocations.size() > 0) {
            oleLocation = oleLocations.get(0);
            oleCirculationDeskLocation.setCirculationLocationCode(oleLocation.getLocationCode());
            oleCirculationDeskLocation.setCirculationDeskLocation(oleLocation.getLocationId());
            oleCirculationDeskLocation.setCirculationPickUpDeskLocation(fullLocationPath);
            oleCirculationDeskLocation.setOleCirculationDesk(oleCirculationDesk);
        }
        for (OleCirculationDeskLocation oleCirculationDeskLocation1 : oleCirculationDeskLocations) {
            if (oleCirculationDeskLocation1.getCirculationDeskLocation().equalsIgnoreCase(oleCirculationDeskLocation.getCirculationDeskLocation())) {
                oleCirculationDeskLocation.setCirculationDeskLocation(null);
                oleCirculationDeskLocation.setCirculationLocationCode(null);

                GlobalVariables.getMessageMap().putErrorForSectionId("OleCirculationDesk-Pickup-Locations", OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_DUPLICATE_ERROR);
                return getUIFModelAndView(form);
            }
        }


        View view = form.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, form, selectedCollectionPath);
        return getUIFModelAndView(form);
    }

}

