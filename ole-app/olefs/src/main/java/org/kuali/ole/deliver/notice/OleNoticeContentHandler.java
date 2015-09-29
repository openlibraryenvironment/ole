package org.kuali.ole.deliver.notice;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.plexus.util.FileUtils;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 9/28/15.
 */
public class OleNoticeContentHandler {

    private BusinessObjectService businessObjectService;

    public String generateHTML(String noticeType, OleNoticeBo oleNoticeBo) {
        StringWriter htmlContent = new StringWriter();
        Configuration cfg = new Configuration();
        try {

            String noticeTemplateDirPath = System.getProperty("notice.template.dir");

            File noticeTemplateDir = null;
            if(null == noticeTemplateDirPath){
                URI uri = getClass().getResource("notice.ftl").toURI();
                File templateFile = new File(uri);
                String tempDir = System.getProperty("java.io.tmpdir");
                File destinationDirectory = new File(tempDir);
                FileUtils.copyFileToDirectory(templateFile, destinationDirectory);
                noticeTemplateDir = destinationDirectory;
            } else {
                noticeTemplateDir = new File(noticeTemplateDirPath);
            }
            cfg.setDirectoryForTemplateLoading(noticeTemplateDir);

            Template template = cfg.getTemplate("notice.ftl");

            Map<String, Object> input = new HashMap<>();

            input.put("title", noticeType);

            input.put("dueDate", oleNoticeBo.getDueDate().toString());
            input.put("oleNoticeBo", oleNoticeBo);

            template.process(input, htmlContent);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return htmlContent.toString();
    }

    public String getNoticeContent(String patronId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("patronId", patronId);
        List<OLEDeliverNoticeHistory> matching = (List<OLEDeliverNoticeHistory>) getBusinessObjectService().findMatching(OLEDeliverNoticeHistory.class, map);
        OLEDeliverNoticeHistory noticeHistory = matching.get(0);

        byte[] noticeContent = noticeHistory.getNoticeContent();
        String noticeContentString = new String(noticeContent);
        return noticeContentString;

    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
