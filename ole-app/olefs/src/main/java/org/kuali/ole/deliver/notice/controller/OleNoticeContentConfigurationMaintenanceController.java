package org.kuali.ole.deliver.notice.controller;

import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeFieldLabelMapping;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
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
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.collections.CollectionUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maheswarang on 10/6/15.
 */

@Controller
@RequestMapping(value = "/oleNoticeContentConfigurationMaintenance")
public class OleNoticeContentConfigurationMaintenanceController extends MaintenanceDocumentController {

    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = (OleNoticeContentConfigurationBo) document.getNewMaintainableObject().getDataObject();
        if(CollectionUtils.isNotEmpty(oleNoticeContentConfigurationBo.getOleNoticeFieldLabelMappings()) && oleNoticeContentConfigurationBo.getOleNoticeFieldLabelMappings().size() > 0){
            Map<String,String> oleNoticeConfigBoMap = new HashMap<>();
            oleNoticeConfigBoMap.put("oleNoticeContentConfigurationId",oleNoticeContentConfigurationBo.getOleNoticeContentConfigurationId());
            getBusinessObjectService().deleteMatching(OleNoticeFieldLabelMapping.class,oleNoticeConfigBoMap);
        }
        oleNoticeContentConfigurationBo.getOleNoticeFieldLabelMappings().clear();
        if(CollectionUtils.isNotEmpty(oleNoticeContentConfigurationBo.getOleNoticePatronFieldLabelMappings())){
            for(OleNoticeFieldLabelMapping oleNoticeFieldLabelMapping :oleNoticeContentConfigurationBo.getOleNoticePatronFieldLabelMappings() ){
                oleNoticeFieldLabelMapping.setBelongsTo("PATRON");
                oleNoticeContentConfigurationBo.getOleNoticeFieldLabelMappings().add(oleNoticeFieldLabelMapping);
            }
        }
        if(CollectionUtils.isNotEmpty(oleNoticeContentConfigurationBo.getOleNoticeItemFieldLabelMappings())){
            for(OleNoticeFieldLabelMapping oleNoticeFieldLabelMapping :oleNoticeContentConfigurationBo.getOleNoticeItemFieldLabelMappings() ){
                oleNoticeFieldLabelMapping.setBelongsTo("ITEM");
                oleNoticeContentConfigurationBo.getOleNoticeFieldLabelMappings().add(oleNoticeFieldLabelMapping);
            }

        }
        return super.route(form, result, request, response);
   }


    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = (OleNoticeContentConfigurationBo) document.getNewMaintainableObject().getDataObject();
        String noticeType = oleNoticeContentConfigurationBo.getNoticeType();
        GlobalVariables.getUserSession().addObject("noticeType",noticeType);
        return super.refresh(form,result,request,response);
    }

    @Override
    @RequestMapping(params = "methodToCall=maintenanceEdit")
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        MaintenanceDocumentForm maintenanceForm = form;
        setupMaintenance(form, request, KRADConstants.MAINTENANCE_EDIT_ACTION);
        MaintenanceDocument document =  maintenanceForm.getDocument();
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = (OleNoticeContentConfigurationBo) document.getNewMaintainableObject().getDataObject();
        String noticeType = oleNoticeContentConfigurationBo.getNoticeType();
        GlobalVariables.getUserSession().addObject("noticeType",noticeType);
        return getUIFModelAndView(form);
    }

}
