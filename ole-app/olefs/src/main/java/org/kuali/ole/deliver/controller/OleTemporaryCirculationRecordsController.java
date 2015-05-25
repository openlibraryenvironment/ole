package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleTemporaryCirculationHistory;
import org.kuali.ole.deliver.form.OleTemporaryCirculationRecordsForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
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
 * .OlePatronMaintenanceDocumentController invokes Transaction document Controller and returns instance of Transaction document service.
 */
@Controller
@RequestMapping(value = "/temporaryCirculationRecord")
public class OleTemporaryCirculationRecordsController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    private static final Logger LOG = Logger.getLogger(OleTemporaryCirculationRecordsController.class);
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;

    @Override
    protected OleTemporaryCirculationRecordsForm createInitialForm(HttpServletRequest request) {
        return new OleTemporaryCirculationRecordsForm();
    }

    /**
     * This method takes the initial request when click on temporaryCirculationRecord Screen.
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
        OleTemporaryCirculationRecordsForm oleTemporaryCirculationRecordsForm = (OleTemporaryCirculationRecordsForm) form;
        return super.start(oleTemporaryCirculationRecordsForm, result, request, response);
    }

    /**
     * This method persist to viewing all the temporary circulation history records.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=viewTempCircRecords")
    public ModelAndView viewTempCircRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {

        OleTemporaryCirculationRecordsForm oleTemporaryCirculationRecordsForm = (OleTemporaryCirculationRecordsForm) form;
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
            oleTemporaryCirculationRecordsForm.setPatronName(name);
            oleTemporaryCirculationRecordsForm.setOlePatronDocument(olePatronDocument);
            LoanProcessor loanProcessor = new LoanProcessor();
            try {
                olePatronDocument.setOleTemporaryCirculationHistoryRecords(getOleLoanDocumentsFromSolrBuilder()
                        .getPatronTemporaryCirculationHistoryRecords(olePatronDocument.getOlePatronId()));
            } catch (Exception e) {
                LOG.error("Exception while setting temporary circulation history records", e);
            }
            List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryRecords = olePatronDocument.getOleTemporaryCirculationHistoryRecords();
            oleTemporaryCirculationRecordsForm.setOleTemporaryCirculationHistoryRecords(oleTemporaryCirculationHistoryRecords);
        } else {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.ERROR_PATRON_NOT_FOUND);
        }
        return getUIFModelAndView(oleTemporaryCirculationRecordsForm, "OleTemporaryCirculationHistoryRecordPage");
    }

    private OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();
        }
        return oleLoanDocumentsFromSolrBuilder;
    }

    public void setOleLoanDocumentsFromSolrBuilder(OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder) {
        this.oleLoanDocumentsFromSolrBuilder = oleLoanDocumentsFromSolrBuilder;
    }
}
