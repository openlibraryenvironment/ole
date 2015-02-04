package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.api.OleDeliverRequestDefinition;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.form.OleMyAccountRequestForm;
import org.kuali.ole.deliver.service.OleMyAccountProcess;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * The OleMyAccountController is the controller class for processing all the actions that corresponds to the renewal by patron  functionality in OLE.
 * The request mapping tag takes care of mapping the individual action to the corresponding functions.
 */
@Controller
@RequestMapping(value = "/myaccountrequestcontroller")
public class OleMyAccountRequestController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OleMyAccountRequestController.class);

    private BusinessObjectService boService;
    private OleMyAccountProcess oleMyAccountProcess;
    private OlePatronHelperService olePatronHelperService = new OlePatronHelperServiceImpl();

    /**
     * This method creates new OleMyAccountRequestForm form
     *
     * @param request
     * @return OleMyAccountRequestForm
     */
    @Override
    protected OleMyAccountRequestForm createInitialForm(HttpServletRequest request) {
        return new OleMyAccountRequestForm();
    }

    /**
     * This method converts UifFormBase to OleMyAccountRequestForm
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
        OleMyAccountRequestForm oleMyAccountRequestForm = (OleMyAccountRequestForm) form;
        return super.start(oleMyAccountRequestForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=requestRecord")
    public ModelAndView requestRecord(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside requestRecord method");
        String olePatronId = request.getParameter("olePatronId");
        OleMyAccountRequestForm oleMyAccountRequestForm = (OleMyAccountRequestForm) form;
        try {
            List<OleDeliverRequestDefinition> oleDeliverRequestDefinitions = getOleMyAccountProcess().getPatronRequestItems(olePatronId);
            List<OleDeliverRequestBo> oleDeliverRequestBos = new ArrayList<OleDeliverRequestBo>();
            for (OleDeliverRequestDefinition oleDeliverRequestDefinition : oleDeliverRequestDefinitions) {
                OleDeliverRequestBo oleDeliverRequestBo = OleDeliverRequestBo.from(oleDeliverRequestDefinition);
                oleDeliverRequestBos.add(oleDeliverRequestBo);
            }
            oleMyAccountRequestForm.setOleDeliverRequestBos(oleDeliverRequestBos);
        } catch (Exception e) {
            LOG.error("request record from Myaccount------->" + e.getMessage(), e);
        }
        return getUIFModelAndView(oleMyAccountRequestForm, "OlePatronRequestRecordPage");
    }

    @RequestMapping(params = "methodToCall=cancelRequest")
    public ModelAndView cancelRequest(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside cancelRequest method");
        OleMyAccountRequestForm oleMyAccountRequestForm = (OleMyAccountRequestForm) form;
        String olePatronId = request.getParameter("olePatronId");
        List<OleDeliverRequestBo> oleDeliverRequestBo = oleMyAccountRequestForm.getOleDeliverRequestBos();
        List<OleDeliverRequestDefinition> oleDeliverRequestDefinitions = new ArrayList<OleDeliverRequestDefinition>();
        List<OleDeliverRequestDefinition> oleDeliverRequestDefinitionNew = new ArrayList<OleDeliverRequestDefinition>();
        try {
            for (OleDeliverRequestBo oleDeliverRequest : oleDeliverRequestBo) {
                if (oleDeliverRequest.isRequestFlag()) {
                    OleDeliverRequestDefinition oleDeliverRequestDefinition = OleDeliverRequestBo.to(oleDeliverRequest);
                    oleDeliverRequestDefinitions.add(oleDeliverRequestDefinition);
                } else {
                    OleDeliverRequestDefinition oleDeliverRequestDefinition = OleDeliverRequestBo.to(oleDeliverRequest);
                    oleDeliverRequestDefinitionNew.add(oleDeliverRequestDefinition);
                }
            }
            getOleMyAccountProcess().cancelRequest(oleDeliverRequestDefinitions);
            List<OleDeliverRequestBo> oleDeliverRequestBos = new ArrayList<OleDeliverRequestBo>();
            OleDeliverRequestBo oleDeliverRequestBoNew = new OleDeliverRequestBo();
            for (OleDeliverRequestDefinition oleDeliverRequestDefinition : oleDeliverRequestDefinitionNew) {
                oleDeliverRequestBoNew = OleDeliverRequestBo.from(oleDeliverRequestDefinition);
                oleDeliverRequestBos.add(oleDeliverRequestBoNew);
            }
            oleMyAccountRequestForm.setOleDeliverRequestBos(oleDeliverRequestBos);
        } catch (Exception e) {
            LOG.error("cancel request from Myaccount------->" + e.getMessage(), e);
        }
        return getUIFModelAndView(oleMyAccountRequestForm, "OlePatronRequestRecordPage");
    }

    public OleMyAccountProcess getOleMyAccountProcess() {

        if (oleMyAccountProcess == null)
            oleMyAccountProcess = new OleMyAccountProcess();
        return oleMyAccountProcess;
    }

}
