package org.kuali.ole.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.codehaus.plexus.util.FileUtils;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.service.NoticeMailContentFormatter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by gopalp on 2/18/16.
 */
public class PatronBillContentFormatter{

    protected String getBaseFTLTemplate() {
        return "PatronBill.ftl";
    }

    protected List<String> getFTLList(){
        List<String> fileNames = new ArrayList<>();
        fileNames.add("PatronBillItemView.ftl");
        fileNames.add("PatronBill.ftl");
        return fileNames;
    }

    public String generateHTML(PatronBillViewBo patronBillViewBo) {
        StringWriter htmlContent = new StringWriter();
        Configuration cfg = new Configuration();
        try {

            String noticeTemplateDirPath = System.getProperty("notice.template.dir");
            File noticeTemplateDir = null;
            if (null == noticeTemplateDirPath) {
                noticeTemplateDir = processFTL();
            } else {
                noticeTemplateDir = new File(noticeTemplateDirPath);
            }
            cfg.setDirectoryForTemplateLoading(noticeTemplateDir);
            Template template = cfg.getTemplate(getBaseFTLTemplate());
            Map<String, Object> input = new HashMap<>();
            input.put("patronBillViewBo", patronBillViewBo);
            template.process(input, htmlContent);
        } catch (TemplateException e) {
            e.printStackTrace();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return htmlContent.toString();
    }

    private File processFTL() throws URISyntaxException, IOException {
        File noticeTemplateDir;
        List<File> templateFiles = new ArrayList<>();
        for (Iterator<String> iterator = getFTLList().iterator(); iterator.hasNext(); ) {
            String fileName = iterator.next();
            URI templateURI = getClass().getResource(fileName).toURI();
            File fileTemplate = new File(templateURI);
            templateFiles.add(fileTemplate);
        }


        String tempDir = System.getProperty("java.io.tmpdir");
        File destinationDirectory = new File(tempDir);

        for (Iterator<File> iterator = templateFiles.iterator(); iterator.hasNext(); ) {
            File noticeTemplate = iterator.next();
            FileUtils.copyFileToDirectory(noticeTemplate, destinationDirectory);
        }

        noticeTemplateDir = destinationDirectory;
        return noticeTemplateDir;
    }


    public static void main(String[] args){

        PatronBillViewBo patronBillViewBo  = new PatronBillViewBo();
        patronBillViewBo.setPatronAddress("address");
        patronBillViewBo.setPatronId("123456");
        patronBillViewBo.setPatronName("Mahesh");

        PatronBillItemView patronBillItemView = new PatronBillItemView();
        patronBillItemView.setBillNumber("1");
        patronBillItemView.setFeeType("feetype");
        patronBillItemView.setItemBarcode("barcode");
        patronBillItemView.setRefundAmount("refund");
        List<PatronBillItemView> patronBillItemViewList = new ArrayList<PatronBillItemView>();
        patronBillItemViewList.add(patronBillItemView);
        patronBillItemViewList.add(patronBillItemView);
        patronBillViewBo.setPatronBillItemViewList(patronBillItemViewList);
        PatronBillContentFormatter patronBillContentFormatter = new PatronBillContentFormatter();
        patronBillContentFormatter.generateHTML(patronBillViewBo);


    }
}
