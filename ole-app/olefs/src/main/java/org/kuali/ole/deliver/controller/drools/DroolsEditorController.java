package org.kuali.ole.deliver.controller.drools;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.kie.api.runtime.rule.AgendaGroup;
import org.kuali.ole.deliver.bo.drools.DroolsEditorBo;
import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;
import org.kuali.ole.deliver.drools.CustomAgendaFilter;
import org.kuali.ole.deliver.drools.DroolFileGenerator;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsKieEngine;
import org.kuali.ole.deliver.drools.rules.CheckoutDroolsFileGenerator;
import org.kuali.ole.deliver.drools.rules.GeneralChecksDroolFileGenerator;
import org.kuali.ole.deliver.form.drools.DroolEditorMaintenanceForm;
import org.kuali.ole.deliver.maintenance.drools.DroolEditorMaintaintainableImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceUtils;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.service.ViewHelperService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 7/8/15.
 */
@Controller
@RequestMapping(value = "/droolsEditorController")
public class DroolsEditorController extends MaintenanceDocumentController {
    Logger LOG = Logger.getLogger(DroolsEditorController.class);

    @Override
    protected MaintenanceDocumentForm createInitialForm(HttpServletRequest request) {
        return new DroolEditorMaintenanceForm();
    }

    public List<DroolFileGenerator> getDroolFileGenerators() {
        List<DroolFileGenerator> droolFileGenerators = new ArrayList<>();
        droolFileGenerators.add(new GeneralChecksDroolFileGenerator());
        droolFileGenerators.add(new CheckoutDroolsFileGenerator());
        return droolFileGenerators;
    }

    public void fireRules(List<Object> facts, String[] expectedRules, String agendaGroup) {
        KieSession session = DroolsKieEngine.getInstance().getSession();
        for (Iterator<Object> iterator = facts.iterator(); iterator.hasNext(); ) {
            Object fact = iterator.next();
            session.insert(fact);
        }

        if (null != expectedRules && expectedRules.length > 0) {
            session.fireAllRules(new CustomAgendaFilter(expectedRules));
        } else {
            Agenda agenda = session.getAgenda();
            AgendaGroup group = agenda.getAgendaGroup(agendaGroup);
            group.setFocus();
            session.fireAllRules();
        }
        session.dispose();
    }

    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        DroolEditorMaintenanceForm droolEditorMaintenanceForm = (DroolEditorMaintenanceForm) form;
        ModelAndView modelAndView = super.start(droolEditorMaintenanceForm, result, request, response);
        MaintenanceDocument document = droolEditorMaintenanceForm.getDocument();
        enableSection(droolEditorMaintenanceForm);
        document.getDocumentHeader().setDocumentDescription(getDocumentDescriptionForNew());
        return modelAndView;
    }

    @Override
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {


        DroolEditorMaintenanceForm droolEditorMaintenanceForm = (DroolEditorMaintenanceForm) form;
        MaintenanceDocument document = droolEditorMaintenanceForm.getDocument();
        DroolsEditorBo droolsEditorBo = (DroolsEditorBo) document.getNewMaintainableObject().getDataObject();

        ModelAndView modelAndView = null;
        modelAndView = super.route(droolEditorMaintenanceForm, result, request, response);
        if (!GlobalVariables.getMessageMap().hasErrors()) {
            generateFile(droolsEditorBo);
        }

        return modelAndView;
    }

    private boolean validateDocument(DroolsEditorBo droolsEditorBo) {
        boolean validContent = true;
        if(StringUtils.isNotBlank(droolsEditorBo.getEditorType())){
            if(CollectionUtils.isEmpty(getRuleBoFromCurrentDocumentByType(droolsEditorBo))){
                validContent = false;
            }
        }
        return validContent;
    }

    private List<DroolsRuleBo> getRuleBoFromCurrentDocumentByType(DroolsEditorBo droolsEditorBo){
        if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.GENERAL_CHECK)){
            return droolsEditorBo.getDroolsRuleBos();
        }else if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.CHECKOUT)){
            return droolsEditorBo.getCheckoutRuleBos();
        }else if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.CHECKIN)){
            return droolsEditorBo.getCheckinRuleBos();
        }else if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.NOTICE)){
            return droolsEditorBo.getNoticesRuleBos();
        }else if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.RENEW)){
            return droolsEditorBo.getRenewRuleBos();
        }
        return null;
    }

    private void generateFile(DroolsEditorBo droolsEditorBo) {
        List<DroolsRuleBo> droolsRuleBos = getRuleBoFromCurrentDocumentByType(droolsEditorBo);

        for (Iterator<DroolFileGenerator> iterator = getDroolFileGenerators().iterator(); iterator.hasNext(); ) {
            DroolFileGenerator droolFileGenerator = iterator.next();
            if (droolFileGenerator.isInterested(droolsEditorBo.getEditorType())) {
                try {
                    droolFileGenerator.generateFile(droolsRuleBos, droolsEditorBo.getFileName());
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }


    @Override
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DroolEditorMaintenanceForm droolEditorMaintenanceForm = (DroolEditorMaintenanceForm)form;
        ModelAndView modelAndView = super.maintenanceEdit(droolEditorMaintenanceForm, result, request, response);
        MaintenanceDocument document = droolEditorMaintenanceForm.getDocument();
        enableSection(droolEditorMaintenanceForm);
        droolEditorMaintenanceForm.setEditSection(true);
        droolEditorMaintenanceForm.setShowFooterSection(true);
        document.getDocumentHeader().setDocumentDescription(getDocumentDescriptionForEdit());
        return modelAndView;
    }

    @RequestMapping(params = "methodToCall=" + "addRule")
    public ModelAndView addRule(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized addRule method");
        DroolEditorMaintenanceForm droolEditorMaintenanceForm = (DroolEditorMaintenanceForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) droolEditorMaintenanceForm.getDocument();
        DroolsEditorBo droolsEditorBo = (DroolsEditorBo) document.getNewMaintainableObject().getDataObject();

        String selectedCollectionPath = droolEditorMaintenanceForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = droolEditorMaintenanceForm.getPostedView().getViewIndex().getCollectionGroupByPath(selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(droolEditorMaintenanceForm, addLinePath);
        DroolsRuleBo addedDroolRuleBo = (DroolsRuleBo) eventObject;
        boolean isUniqueRule = true;
        for (Iterator<DroolsRuleBo> iterator = droolsEditorBo.getDroolsRuleBos().iterator(); iterator.hasNext(); ) {
            DroolsRuleBo droolsRuleBo = iterator.next();
            if (addedDroolRuleBo.getRuleName().equalsIgnoreCase(droolsRuleBo.getRuleName())) {
                isUniqueRule = false;
            }
        }
        if (isUniqueRule) {
            super.addLine(droolEditorMaintenanceForm, result, request, response);
            if(validateDocument(droolsEditorBo)){
                droolEditorMaintenanceForm.setShowFooterSection(true);
            }else{
                droolEditorMaintenanceForm.setShowFooterSection(false);
            }
        }
        else{
            GlobalVariables.getMessageMap().putErrorForSectionId("DroolsEditorBo-MaintenanceView-ruleGridSection", "drools.rule.already.exists", "\"" + addedDroolRuleBo.getRuleName() + "\"");
        }

        return getUIFModelAndView(uifForm);
    }


    @RequestMapping(params = "methodToCall=" + "addEditor")
    public ModelAndView addEditor(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DroolEditorMaintenanceForm droolEditorMaintenanceForm= (DroolEditorMaintenanceForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) droolEditorMaintenanceForm.getDocument();
        DroolsEditorBo droolsEditorBo = (DroolsEditorBo) document.getNewMaintainableObject().getDataObject();
        if(droolEditorMaintenanceForm.isDisableAddEditor()){
            droolsEditorBo = new DroolsEditorBo();
            document.getNewMaintainableObject().setDataObject(droolsEditorBo);
            droolEditorMaintenanceForm.setDisableAddEditor(false);
            droolEditorMaintenanceForm.resetFlagValues();
        }else{
            if(isAllowedToCreateRule(droolsEditorBo)){
                droolEditorMaintenanceForm.setDisableAddEditor(true);
                enableSection(droolEditorMaintenanceForm);
            }else{
                showDialog("droolEditorConformationDialog",uifForm,request,response);
            }
        }
        return getUIFModelAndView(droolEditorMaintenanceForm);
    }

    private boolean isAllowedToCreateRule(DroolsEditorBo droolsEditorBo) {
        if (droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.GENERAL_CHECK)) {
            Map<String,String> editorParamMap = new HashMap<>();
            editorParamMap.put("editorType", droolsEditorBo.getEditorType());
            List<DroolsEditorBo> editorBoList = (List<DroolsEditorBo>) KRADServiceLocator.getBusinessObjectService().findMatching(DroolsEditorBo.class,editorParamMap);
            if(CollectionUtils.isNotEmpty(editorBoList)){
                return false;
            }
        }
        return true;
    }

    private String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return simpleDateFormat.format(new Date());
    }

    private String getDocumentDescriptionForNew() {
        return "New-GeneralCheckRule-" +
                GlobalVariables.getUserSession().getPrincipalName() + "-" +
                getDateString();

    }

    private String getDocumentDescriptionForEdit() {
        return "Edit-GeneralCheckRule-" +
                GlobalVariables.getUserSession().getPrincipalName() + "-" +
                getDateString();
    }

    private void enableSection(DroolEditorMaintenanceForm droolEditorMaintenanceForm){
        droolEditorMaintenanceForm.resetFlagValues();
        MaintenanceDocument document = (MaintenanceDocument) droolEditorMaintenanceForm.getDocument();
        DroolsEditorBo droolsEditorBo = (DroolsEditorBo) document.getNewMaintainableObject().getDataObject();
        /*Map<String,String> editorParamMap = new HashMap<>();
        editorParamMap.put("editorType",droolsEditorBo.getEditorType());
        List<DroolsEditorBo> editorBoList = (List<DroolsEditorBo>) KRADServiceLocator.getBusinessObjectService().findMatching(DroolsEditorBo.class,editorParamMap);
        if(CollectionUtils.isNotEmpty(editorBoList)){
            droolsEditorBo = editorBoList.get(0);
            document.getNewMaintainableObject().setDataObject(droolsEditorBo);
        }*/
        if(StringUtils.isNotBlank(droolsEditorBo.getEditorType())){
            if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.GENERAL_CHECK)){
                droolEditorMaintenanceForm.setShowGeneralCheckSection(true);
            }else if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.CHECKOUT)){
                droolEditorMaintenanceForm.setShowCheckoutSection(true);
            }else if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.CHECKIN)){
                droolEditorMaintenanceForm.setShowCheckinSection(true);
            }else if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.NOTICE)){
                droolEditorMaintenanceForm.setShowNoticeSection(true);
            }else if(droolsEditorBo.getEditorType().equalsIgnoreCase(DroolsConstants.EDITOR_TYPE.RENEW)){
                droolEditorMaintenanceForm.setShowRenewCheckSection(true);
            }
        }
    }



    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addFee")
    public ModelAndView addFee(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized addLine method");
        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }

        View view = uifForm.getPostedView();
        DroolEditorMaintaintainableImpl viewHelperService = (DroolEditorMaintaintainableImpl) view.getViewHelperService();
        viewHelperService.processCollectionAddLineForFine(view, uifForm, selectedCollectionPath);
        return getUIFModelAndView(uifForm);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=openDocumentToEdit")
    public ModelAndView openDocumentToEdit(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        DroolEditorMaintenanceForm droolEditorMaintenanceForm= (DroolEditorMaintenanceForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) droolEditorMaintenanceForm.getDocument();
        DroolsEditorBo droolsEditorBo = (DroolsEditorBo) document.getNewMaintainableObject().getDataObject();
        String editorType = droolsEditorBo.getEditorType();
        if(StringUtils.isNotBlank(editorType)){
            Map<String,String> editorParamMap = new HashMap<>();
            editorParamMap.put("editorType", editorType);
            List<DroolsEditorBo> editorBoList = (List<DroolsEditorBo>) KRADServiceLocator.getBusinessObjectService().findMatching(DroolsEditorBo.class,editorParamMap);
            if(CollectionUtils.isNotEmpty(editorBoList)){
                droolsEditorBo = editorBoList.get(0);
            }
        }

        request.setAttribute("editorType", editorType);
        request.setAttribute("editorId", droolsEditorBo.getEditorId());
        droolsEditorBo = new DroolsEditorBo();
        document.getNewMaintainableObject().setDataObject(droolsEditorBo);
        return maintenanceEdit(droolEditorMaintenanceForm,result,request,response);
    }

    @Override
    public ModelAndView deleteLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        DroolEditorMaintenanceForm droolEditorMaintenanceForm= (DroolEditorMaintenanceForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) droolEditorMaintenanceForm.getDocument();
        DroolsEditorBo droolsEditorBo = (DroolsEditorBo) document.getNewMaintainableObject().getDataObject();
        ModelAndView modelAndView = super.deleteLine(droolEditorMaintenanceForm, result, request, response);
        if(validateDocument(droolsEditorBo)){
            droolEditorMaintenanceForm.setShowFooterSection(true);
        }else{
            droolEditorMaintenanceForm.setShowFooterSection(false);
        }
        return modelAndView;
    }

    @Override
    protected void setupMaintenance(MaintenanceDocumentForm form, HttpServletRequest request, String maintenanceAction) {
        MaintenanceDocument document = form.getDocument();

        if (document == null) {
            document = getMaintenanceDocumentService()
                    .setupNewMaintenanceDocument(form.getDataObjectClassName(), form.getDocTypeName(),
                            maintenanceAction);

            form.setDocument(document);
            form.setDocTypeName(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        }

        form.setMaintenanceAction(maintenanceAction);

        String editorType = (String) request.getAttribute("editorType");
        Map parameterMap = new HashMap(request.getParameterMap());
        if(StringUtils.isNotBlank(editorType)){
            parameterMap.put("editorType", new String[]{editorType});
            parameterMap.put("viewTypeName", new String[]{"MAINTENANCE"});
            parameterMap.put("editorId", new String[]{(String)request.getAttribute("editorId")});
            parameterMap.put("returnLocation", new String[]{ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base")});
            parameterMap.put("methodToCall", new String[]{"maintenanceEdit"});
            parameterMap.put("dataObjectClassName", new String[]{"org.kuali.ole.deliver.bo.drools.DroolsEditorBo"});
        }

        getMaintenanceDocumentService().setupMaintenanceObject(document, maintenanceAction, parameterMap);

        if (KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction)) {
            MaintenanceUtils.checkForLockingDocument(document, false);
        }

        DocumentEntry entry = KRADServiceLocatorWeb.getDocumentDictionaryService()
                .getMaintenanceDocumentEntry(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        document.setDisplayTopicFieldInNotes(entry.getDisplayTopicFieldInNotes());
    }
}
