package org.kuali.ole.web;

import com.google.common.io.CharStreams;
import gov.loc.repository.bagit.utilities.FormatHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.docstore.common.document.License;
import org.kuali.ole.docstore.common.document.LicenseAttachment;
import org.kuali.ole.docstore.common.document.Licenses;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.utility.CompressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 2/27/14
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseRestServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LicenseRestServlet.class);
    private CompressUtils compressUtils = new CompressUtils();
    DocstoreService ds = BeanLocator.getDocstoreService();
    private static String responseUrl = "documentrest/license/";
    private String extractFilePath = FileUtils.getTempDirectoryPath() + File.separator + "bagit";

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/xml;charset=UTF-8");
        String result = "";
        Licenses licenses = null;
        try {
            ArrayList<File> files = extractBagFilesFromRequest(req, resp);
            for(File file : files) {
                if (file.getName().equalsIgnoreCase("licenses.xml")) {
                    String licensesXml = FileUtils.readFileToString(file);
                    licenses = (Licenses) Licenses.deserialize(licensesXml);
                    for(License license : licenses.getLicenses()) {
                        if(!license.getFormat().equals("onixpl")) {
                            LicenseAttachment licenseAttachment = (LicenseAttachment) license;
                            licenseAttachment.setFilePath(file.getParent());
                        }
                    }
                }
            }
            ds.createLicenses(licenses);
            compressUtils.deleteFiles(files);
            File extractFile = new File(extractFilePath);
            extractFile.delete();
        } catch (Exception e) {
            LOG.error("EXception : ", e);
        }
        StringBuffer ids = new StringBuffer();
        for(License license : licenses.getLicenses()) {
            ids.append(license.getId());
            ids.append("/");
        }

        out.write(responseUrl+ ids.substring(0, (ids.length()-1)));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/xml;charset=UTF-8");
        String result = "";
        try {
           result = retrieveLicense(req);
            out.print(result);
        } catch (DocstoreException de) {
            LOG.error("Exception :", de);
            out.print(DocstoreExceptionProcessor.toXml(de));
        } catch (Exception e) {
            LOG.error("Exception :", e);
            out.print(e);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/xml;charset=UTF-8");
        String result = "";

        try {
            result = deleteLicense(req);
            out.print(result);
        } catch (DocstoreException de) {
            LOG.error("Exception :", de);
            out.print(DocstoreExceptionProcessor.toXml(de));
        } catch (Exception e) {
            LOG.error("Exception :", e);
            out.print(e);
        }
    }

    private String deleteLicense(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String licenseId = getIdFromPathInfo(req.getPathInfo());
        ds.deleteLicense(licenseId);
        return "Success";
    }



    private String retrieveLicense(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String id = getIdFromPathInfo(req.getPathInfo());
        if (id.contains("licenseIds")) {
            String[] splitString = id.split("=");
            String[] licenseIds = splitString[1].split(",");
            List<String> licenseIdList = new ArrayList<String>();
            for (String bibId : licenseIds) {
                licenseIdList.add(bibId);
            }
            Licenses licenses = null;
            licenses = ds.retrieveLicenses(licenseIdList);
            return Licenses.serialize(licenses);

        } else {
            License license = ds.retrieveLicense(id);
            return license.serialize(license);
        }

    }


    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/xml;charset=UTF-8");
        String result = "";
        try {
            if (req.getPathInfo().startsWith("/trees")) {
                result = updateLicenses(req);
            } else {
                result = updateLicense(req);
            }
            out.print(result);
        } catch (DocstoreException de) {
            LOG.error("Exception :", de);
            out.print(DocstoreExceptionProcessor.toXml(de));
        } catch (Exception e) {
            LOG.error("Exception :", e);
            out.print(e);
        }
    }

    private String updateLicense(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());

        License license = new License();
        License licenseObj = (License) license.deserialize(requestBody);
        ds.updateLicense(licenseObj);
        return responseUrl + licenseObj.getId();

    }

    private String updateLicenses(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        Licenses licenses = (Licenses) Licenses.deserialize(requestBody);
        ds.updateLicenses(licenses);
        return "";
    }


    private ArrayList<File> extractBagFilesFromRequest(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        File targetDir = null;
        try {
            File file = null;
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
            Iterator items = new ServletFileUpload(fileItemFactory).parseRequest(req).iterator();
            while (items.hasNext()) {
                FileItem item = (FileItem) items.next();
                file = new File(FileUtils.getTempDirectory(), item.getName());
                item.write(file);
            }
            targetDir = compressUtils.extractZippedBagFile(file.getAbsolutePath(), extractFilePath);
            LOG.info("extractedBagFileLocation " + targetDir);
        } catch (IOException e) {
            LOG.error("IOException", e);
//            sendResponseBag(res, e.getMessage(), "failure");
        } catch (FormatHelper.UnknownFormatException unknownFormatException) {
            LOG.error("unknownFormatException", unknownFormatException);
//            sendResponseBag(res, unknownFormatException.getMessage(), "failure");
        }
        return compressUtils.getAllFilesList(targetDir);
    }

    private String getIdFromPathInfo(String pathInfo) {
        String id = "";
        if (StringUtils.isNotEmpty(pathInfo)) {
            int length = pathInfo.length();
            if (pathInfo.endsWith("/")) {
                pathInfo = pathInfo.substring(0, length - 1);
            }
            id = pathInfo.substring(pathInfo.lastIndexOf("/") + 1);
        }
        return id;
    }

}
