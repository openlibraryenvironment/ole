package org.kuali.ole.batch.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.batch.form.OLEDeliverNoticeForm;
import org.kuali.ole.batch.service.OLEDeliverNoticeService;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 8/7/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/oleDeliverNoticeController")
public class OLEDeliverNoticeController extends UifControllerBase {
    private static final Logger LOG = Logger.getLogger(OLEDeliverNoticeController.class);
    @Override
    protected OLEDeliverNoticeForm createInitialForm(HttpServletRequest request) {
        return new OLEDeliverNoticeForm();
    }


    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      OLEDeliverNoticeForm oleDeliverNoticeForm = (OLEDeliverNoticeForm)form;
        OLEDeliverNoticeService oleNoticeService = new OLEDeliverNoticeService();
        oleDeliverNoticeForm =  oleNoticeService.populateOLEDeliverNoticeForm(oleDeliverNoticeForm);
        return getUIFModelAndView(oleDeliverNoticeForm, "OLEDeliverNoticeViewPage");

    }

    @RequestMapping(params = "methodToCall=downloadAttachment")
    public ModelAndView downloadAttachment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePath = request.getParameter("filePath");
        String fileName=request.getParameter("fileName");
        LOG.info("File Path : " +filePath);
        File file=new File(filePath);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setContentLength((int) file.length());
        InputStream fis = new FileInputStream(file);
        IOUtils.copy(fis, response.getOutputStream());
        response.getOutputStream().flush();
        return null;
    }


}
