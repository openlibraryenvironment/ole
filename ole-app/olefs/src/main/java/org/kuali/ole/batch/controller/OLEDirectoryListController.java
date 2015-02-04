package org.kuali.ole.batch.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLECourtesyNotice;
import org.kuali.ole.batch.bo.OLEListFileLocation;
import org.kuali.ole.batch.form.OLEDeliverNoticeForm;
import org.kuali.ole.batch.form.OLEDirectoryListForm;
import org.kuali.ole.batch.service.OLEDeliverNoticeService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.util.GlobalVariables;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 6/11/14.
 */
@Controller
@RequestMapping(value = "/oleDirectoryListController")
public class OLEDirectoryListController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OLEDeliverNoticeController.class);

    @Override
    protected OLEDirectoryListForm createInitialForm(HttpServletRequest request) {
        return new OLEDirectoryListForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLEDirectoryListForm oleDirectoryListForm = (OLEDirectoryListForm)form;
        String filePath= request.getParameter("filePath");
        oleDirectoryListForm = populateListDirectoryForm(oleDirectoryListForm,filePath);
        //oleDeliverNoticeForm =  oleNoticeService.populateOLEDeliverNoticeForm(oleDeliverNoticeForm);
        return getUIFModelAndView(oleDirectoryListForm, "OLEListDirectoryViewPage");

    }

    @RequestMapping(params = "methodToCall=downloadFile")
    public ModelAndView downloadFile(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
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

    public OLEDirectoryListForm populateListDirectoryForm(OLEDirectoryListForm oleDirectoryListForm,String path){

        List<File> fileList= new ArrayList<File>();
        String directoryLocation = path;
        if (directoryLocation != null && !directoryLocation.trim().isEmpty()) {
            directoryLocation = ConfigContext.getCurrentContextConfig().getProperty("staging.directory")+ "/" + directoryLocation +"/";
            LOG.info("PDF LOCATION : " + directoryLocation);
            File directory = new File(directoryLocation);
            if(directory.exists()){
                File[] fList = directory.listFiles();
                if (fList != null && fList.length > 0) {
                    for (File file : fList) {
                        if (file.isFile()) {
                            fileList.add(file);
                        }
                    }
                }
                if(fileList.size()<= 0){
                    GlobalVariables.getMessageMap().putError(path,OLEConstants.OLEBatchProcess.NO_FILES);
                }
            }else{
                GlobalVariables.getMessageMap().putError(path,OLEConstants.OLEBatchProcess.INVALID_FILE_PATH);
            }
        }else{
            GlobalVariables.getMessageMap().putError("Path",OLEConstants.OLEBatchProcess.INVALID_FILE_PATH);
        }

        oleDirectoryListForm.setOleListFileLocationList(generateFileList(fileList));
        return oleDirectoryListForm;
    }

    public List<OLEListFileLocation> generateFileList(List<File> fileList) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        List<OLEListFileLocation> oleListFileLocations = new ArrayList<OLEListFileLocation>();
        OLECourtesyNotice oleCourtesyNotice ;
        for(File file :fileList){
            OLEListFileLocation oleListFileLocation = new OLEListFileLocation();
            oleListFileLocation.setFileName(file.getName());
            oleListFileLocation.setFileLocation(file.getAbsolutePath());
            oleListFileLocation.setFileSize(calculateFileSize(file.length()));
            oleListFileLocation.setFileLastModified(sdf.format(file.lastModified()));
            oleListFileLocations.add(oleListFileLocation);
        }
        LOG.info("No of Files : " +oleListFileLocations.size());
        return oleListFileLocations;
    }

    public String calculateFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
