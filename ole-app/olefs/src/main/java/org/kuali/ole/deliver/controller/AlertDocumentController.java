package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.AlertConditionAndReceiverInformation;
import org.kuali.ole.alert.bo.AlertDocument;
import org.kuali.ole.alert.bo.AlertDocumentType;
import org.kuali.ole.alert.bo.AlertFieldValueMapping;
import org.kuali.ole.deliver.service.AlertDocumentService;
import org.kuali.ole.deliver.service.impl.AlertDocumentServiceImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by arunag on 8/22/14.
 */
@Controller
@RequestMapping(value = "/alertMaintenance")
public class AlertDocumentController extends MaintenanceDocumentController {

    private static final Logger LOG = Logger.getLogger(AlertDocumentController.class);







    @Override
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return super.save(form, result, request, response);
    }

    /**
     * To submit or route the patron maintenance document
     *
     * @param form document form base containing the document instance that will be routed
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        AlertDocument alertDocument = (AlertDocument)maintenanceDocument.getDocumentDataObject();
        if(alertDocument.getDocumentTypeName() == null || alertDocument.getDocumentTypeName().equalsIgnoreCase("")){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALRT_DOC_TYPE_NAME_FIELD,OLEConstants.EMPTY_DOCTYPENAME);
            getUIFModelAndView(form);
        }else if(validateDocTypeName(alertDocument)){
            getUIFModelAndView(form);
        }
        if(alertDocument.getAlertConditionAndReceiverInformations()==null || alertDocument.getAlertConditionAndReceiverInformations().size()==0){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALRT_DOC_TYPE_NAME_FIELD,OLEConstants.ACTION_EVENT);
            getUIFModelAndView(form);
        }



        return super.route(form, result, request, response);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=mapAddLine")
    public ModelAndView searchAddLine(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        boolean name = false;
        boolean type = false;
        boolean value = false;

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object alertDocumentObject = ObjectPropertyUtils.getPropertyValue(maintenanceForm, addLinePath);
        AlertFieldValueMapping alertDocumentFieldValueMapping = (AlertFieldValueMapping) alertDocumentObject;
        if(alertDocumentFieldValueMapping.getFieldName() == null || alertDocumentFieldValueMapping.getFieldName().equalsIgnoreCase("")){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALRT_DOC_ALRT_FIELD_VAL,OLEConstants.EMPTY_FIELDNAME);
            name=true;
        }
        if(alertDocumentFieldValueMapping.getFieldType() == null || alertDocumentFieldValueMapping.getFieldType().equalsIgnoreCase("")){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALRT_DOC_ALRT_FIELD_VAL,OLEConstants.EMPTY_FIELDTYPE);
            type=true;
        }
        if(alertDocumentFieldValueMapping.getFieldValue() == null || alertDocumentFieldValueMapping.getFieldValue().equalsIgnoreCase("") && !alertDocumentFieldValueMapping.getFieldType().equalsIgnoreCase("boolean")){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALRT_DOC_ALRT_FIELD_VAL,OLEConstants.EMPTY_FIELDVALUE);
            value=true;
        }
        if(name || type || value){
            return getUIFModelAndView(form);
        }
        if(alertDocumentFieldValueMapping.getFieldType().equalsIgnoreCase("Integer")){
            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher m = p.matcher(alertDocumentFieldValueMapping.getFieldValue());
            while (!m.find()) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALRT_DOC_ALRT_FIELD_VAL, OLEConstants.INVALID_INTEGER);
                return getUIFModelAndView(form);
            }
        }

        if(alertDocumentFieldValueMapping.getFieldType().equalsIgnoreCase("boolean")){
            if(!alertDocumentFieldValueMapping.getFieldValue().equalsIgnoreCase("Y") && !alertDocumentFieldValueMapping.getFieldValue().equalsIgnoreCase("N")){
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALRT_DOC_ALRT_FIELD_VAL, OLEConstants.INVALID_BOOLEAN);
                return getUIFModelAndView(form);
            }
        }


        return  super.addLine(form, result, request, response);
    }

    public boolean validateDocTypeName(AlertDocument alertDocument){
        boolean result=false;
        Map<String, String> criteria = new HashMap<String, String>();
        List<AlertDocumentType> alertDocumentTypeList;
                if(alertDocument.getDocumentTypeName()!=null && alertDocument.getDocumentClassName()!=null){
                    criteria.put("alertDocumentTypeName",alertDocument.getDocumentTypeName());
                    criteria.put("alertDocumentClass",alertDocument.getDocumentClassName());
                    alertDocumentTypeList =  (List<AlertDocumentType>) KRADServiceLocator.getBusinessObjectService().findMatching(AlertDocumentType.class, criteria);
                    if(alertDocumentTypeList!=null && alertDocumentTypeList.size()>0){
                        GlobalVariables.getMessageMap().putError(OLEConstants.ALRT_DOC_TYPE_NAME_FIELD,"doctype.already.exist");
                        result= true;
                    }else{
                        GlobalVariables.getMessageMap().putError(OLEConstants.ALRT_DOC_TYPE_NAME_FIELD,OLEConstants.INVALID_DOCTYPENAME );
                        result= true;
                    }
                }else if(alertDocument.getDocumentTypeName()==null && alertDocument.getDocumentClassName()!=null){
                    criteria = new HashMap<String,String>();
                    criteria.put("alertDocumentClass",alertDocument.getDocumentClassName());
                    alertDocumentTypeList =  (List<AlertDocumentType>) KRADServiceLocator.getBusinessObjectService()
                            .findMatching(AlertDocumentType.class, criteria);
                    if(alertDocumentTypeList!=null && alertDocumentTypeList.size()>0){
                        GlobalVariables.getMessageMap().putError(OLEConstants.ALRT_DOC_TYPE_NAME_FIELD,"doctype.already.exist");
                        result= true;
                    }else{
                        GlobalVariables.getMessageMap().putError(OLEConstants.ALRT_DOC_TYPE_NAME_FIELD,OLEConstants.INVALID_DOCTYPENAME );
                        result= true;
                    }
                }else if(alertDocument.getDocumentTypeName()!=null && alertDocument.getDocumentClassName()==null){
                    criteria = new HashMap<String,String>();
                    criteria.put("alertDocumentTypeName",alertDocument.getDocumentTypeName());
                    alertDocumentTypeList =  (List<AlertDocumentType>) KRADServiceLocator.getBusinessObjectService()
                            .findMatching(AlertDocumentType.class, criteria);
                    if(alertDocumentTypeList!=null && alertDocumentTypeList.size()>0){

                        GlobalVariables.getMessageMap().putError(OLEConstants.ALRT_DOC_TYPE_NAME_FIELD,"doctype.already.exist");

                        result= true;
                    }else{
                        GlobalVariables.getMessageMap().putError(OLEConstants.ALRT_DOC_TYPE_NAME_FIELD,OLEConstants.INVALID_DOCTYPENAME);
                        result= true;
                    }
                }

        return result;
    }


    @RequestMapping(params = "methodToCall=alertCondition")
    public ModelAndView alertCondition(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        boolean validateRole = false;
        boolean validateGroup = false;
        boolean validatePerson = false;


        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        AlertDocument alertDocument = (AlertDocument) document.getNewMaintainableObject().getDataObject();

        String selectedCollectionPath = maintenanceForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = maintenanceForm.getPostedView().getViewIndex().getCollectionGroupByPath(selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(maintenanceForm, addLinePath);

        AlertConditionAndReceiverInformation alertConditionAndReceiverInformation = (AlertConditionAndReceiverInformation) eventObject;
        if(StringUtils.isEmpty(alertConditionAndReceiverInformation.getRoleName()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getRoleId()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getGroupName()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getGroupId()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getPrincipalName()) && StringUtils.isEmpty(alertConditionAndReceiverInformation.getPrincipalId())){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ALERT_DOC_CONDITION, OLEConstants.ERROR_EMPTY_ROLE_PERSON_GROUP);
            return getUIFModelAndView(form);
        }
        AlertDocumentService alertDocumentService=new AlertDocumentServiceImpl();
        validateRole=alertDocumentService.validateRole(alertConditionAndReceiverInformation);
        if(validateRole){
            return getUIFModelAndView(form);
        }
        validateGroup=alertDocumentService.validateGroup(alertConditionAndReceiverInformation);
        if(validateGroup){
            return getUIFModelAndView(form);
        }
        validatePerson=alertDocumentService.validatePerson(alertConditionAndReceiverInformation);
        if(validatePerson){
            return getUIFModelAndView(form);
        }



        return  super.addLine(form, result, request, response);
    }


    }
