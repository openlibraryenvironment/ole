package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.form.OlePatronLoanedRecordsForm;
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
 * .OlePatronLoanedRecordsController invokes Transaction document Controller and returns instance of Transaction document service.
 */
@Controller
@RequestMapping(value = "/patronLoanedRecord")
public class OlePatronLoanedRecordsController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    private static final Logger LOG = Logger.getLogger(org.kuali.ole.deliver.controller.OlePatronLoanedRecordsController.class);
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;

    @Override
    protected OlePatronLoanedRecordsForm createInitialForm(HttpServletRequest request) {
        return new OlePatronLoanedRecordsForm();
    }

    /**
     * This method takes the initial request when click on Loaned item Screen.
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
        OlePatronLoanedRecordsForm olePatronLoanedRecordsForm = (OlePatronLoanedRecordsForm) form;
        return super.start(olePatronLoanedRecordsForm, result, request, response);
    }

    /**
     * This method persist to viewing all the loaned items records.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=viewLoanedRecords")
    public ModelAndView viewLoanedRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {

        OlePatronLoanedRecordsForm olePatronLoanedRecordsForm = (OlePatronLoanedRecordsForm) form;
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
            olePatronLoanedRecordsForm.setPatronName(name);
            olePatronLoanedRecordsForm.setOlePatronDocument(olePatronDocument);
            LoanProcessor loanProcessor = new LoanProcessor();
            try {
                olePatronDocument.setOleLoanDocuments(getOleLoanDocumentsFromSolrBuilder().getPatronLoanedItemBySolr(olePatronDocument
                        .getOlePatronId(), null));
            } catch (Exception e) {
                LOG.error("Exception while setting loan documents", e);
            }
            List<OleLoanDocument> oleLoanDocuments = olePatronDocument.getOleLoanDocuments();
            olePatronLoanedRecordsForm.setLoanDocuments(oleLoanDocuments);
        } else {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.ERROR_PATRON_NOT_FOUND);
        }
        return getUIFModelAndView(olePatronLoanedRecordsForm, "OlePatronLoanedRecordPage");
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
