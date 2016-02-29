package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.form.OlePatronRequestedRecordsForm;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .OlePatronRequestedRecordsController invokes Transaction document Controller and returns instance of Transaction document service.
 */
@Controller
@RequestMapping(value = "/patronRequestedRecord")
public class OlePatronRequestedRecordsController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    private static final Logger LOG = Logger.getLogger(OlePatronRequestedRecordsController.class);

    @Override
    protected OlePatronRequestedRecordsForm createInitialForm(HttpServletRequest request) {
        return new OlePatronRequestedRecordsForm();
    }

    /**
     * This method takes the initial request when click on patronRequestedRecord Screen.
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
        LOG.debug("Start -- Start Method of OleTemporaryCirculationRecordsForm");
        OlePatronRequestedRecordsForm olePatronRequestedRecordsForm = (OlePatronRequestedRecordsForm) form;
        return super.start(olePatronRequestedRecordsForm, result, request, response);
    }

    /**
     * This method persist to viewing all the requested items records.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=viewRequestedRecords")
    public ModelAndView viewRequestedRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {

        OlePatronRequestedRecordsForm olePatronRequestedRecordsForm = (OlePatronRequestedRecordsForm) form;
        String patronId = request.getParameter("patronId");
        Map patron = new HashMap();
        patron.put(OLEConstants.OlePatron.PATRON_ID, patronId);
        OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patron);
        if (olePatronDocument != null) {
            EntityBo entity = olePatronDocument.getEntity();
            EntityNameBo entityName = entity.getNames().get(0);
            String firstName = entityName.getFirstName();
            String lastName = entityName.getLastName();
            String name = firstName + " " + lastName;
            olePatronRequestedRecordsForm.setPatronName(name);
            olePatronRequestedRecordsForm.setOlePatronDocument(olePatronDocument);
            OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
            List<OleDeliverRequestBo> oleDeliverRequestBos = oleDeliverRequestDocumentHelperService.getRequestedItems(patronId);
            for(OleDeliverRequestBo deliverRequestBo : oleDeliverRequestBos){
                if(deliverRequestBo.getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.RECALL)){
                    CircUtilController circUtilController = new CircUtilController();
                    OleLoanDocument oleLoanDocument = circUtilController.getLoanDocument(deliverRequestBo.getItemId());
                    if(oleLoanDocument != null)
                       deliverRequestBo.setRecallDueDate(oleLoanDocument.getLoanDueDate());
                }
                if(deliverRequestBo.getItemStatus().contains(OLEConstants.OleDeliverRequest.INTRANSIT)){
                    CircUtilController circUtilController = new CircUtilController();
                    org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord itemRecord = circUtilController.getItemRecordByBarcode(deliverRequestBo.getItemId());
                    deliverRequestBo.setInTransitDate(itemRecord.getEffectiveDate());
                }
            }
            olePatronRequestedRecordsForm.setRequestBos(oleDeliverRequestBos);
        } else {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.ERROR_PATRON_NOT_FOUND);
        }
        return getUIFModelAndView(olePatronRequestedRecordsForm, "OlePatronRequestedRecordPage");
    }
}
