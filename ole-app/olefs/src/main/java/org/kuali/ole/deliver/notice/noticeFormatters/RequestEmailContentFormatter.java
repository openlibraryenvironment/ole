package org.kuali.ole.deliver.notice.noticeFormatters;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.util.CollectionUtil;
import org.codehaus.plexus.util.FileUtils;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 6/25/15.
 */
public abstract class RequestEmailContentFormatter {
    private static final Logger LOG = Logger.getLogger(RequestExpirationEmailContentFormatter.class);
    private OlePatronHelperService olePatronHelperService;
    private BusinessObjectService businessObjectService;

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronHelperService(OlePatronHelperService olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
    }


    public String generateMailContentForPatron(List<OleDeliverRequestBo> oleDeliverRequestBos, OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        List<OleNoticeBo> noticeBos = initialiseOleNoticeBos(oleDeliverRequestBos, oleNoticeContentConfigurationBo);
        String noticeHtmlContent = null;
        if (CollectionUtils.isNotEmpty(noticeBos)) {
            noticeHtmlContent = generateHTML(noticeBos, oleNoticeContentConfigurationBo);
        }
        return noticeHtmlContent;
    }



    public List<OleNoticeBo> initialiseOleNoticeBos(List<OleDeliverRequestBo> oleDeliverRequestBos,OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        List<OleNoticeBo> oleNoticeBos = new ArrayList<>();
        if(oleDeliverRequestBos!=null && oleDeliverRequestBos.size()>0){
            OlePatronDocument olePatron = getOlePatron(oleDeliverRequestBos.get(0));
            for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos){
                try{
                    OleNoticeBo oleNoticeBo = new OleNoticeBo();
                    oleNoticeBo.setTitle(oleNoticeContentConfigurationBo.getNoticeTitle());
                    oleNoticeBo.setNoticeTitle(oleNoticeContentConfigurationBo.getNoticeTitle());
                    oleNoticeBo.setNoticeType(oleNoticeContentConfigurationBo.getNoticeType());
                    oleNoticeBo.setNoticeName(oleNoticeContentConfigurationBo.getNoticeTitle());
                    setPatronInfo(olePatron, oleNoticeBo);
                    setNoticeBodyAndContent(oleNoticeBo, oleNoticeContentConfigurationBo.getNoticeTitle(),
                            oleNoticeContentConfigurationBo.getNoticeBody(), oleNoticeContentConfigurationBo.getNoticeFooterBody());
                    setItemInfo(oleNoticeBo, oleDeliverRequestBo);
                    setCirculationDeskInfo(oleNoticeBo, oleDeliverRequestBo);
                    setRequestInformation(oleDeliverRequestBo, oleNoticeBo);
                    processCustomNoticeInfo(oleDeliverRequestBo, oleNoticeBo);
                    oleNoticeBos.add(oleNoticeBo);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return oleNoticeBos;
    }

    public OleNoticeBo setPatronInfo(OlePatronDocument olePatronDocument,OleNoticeBo oleNoticeBo){
        if(olePatronDocument != null) {
            String patronName = olePatronDocument.getPatronName();
            oleNoticeBo.setPatronName(patronName != null ? patronName : "");
            String preferredAddress = olePatronDocument.getPreferredAddress();
            oleNoticeBo.setPatronAddress(preferredAddress != null ? preferredAddress : "");
            String emailAddress = olePatronDocument.getEmail();
            oleNoticeBo.setPatronEmailAddress(emailAddress != null ? emailAddress : "");
            String phoneNumber = olePatronDocument.getPhoneNumber();
            oleNoticeBo.setPatronPhoneNumber(phoneNumber != null ? phoneNumber : "");
        }
        return oleNoticeBo;
    }

    public  OleNoticeBo setNoticeBodyAndContent(OleNoticeBo oleNoticeBo,String body,String bodyContent,String noticeSpecificFooterContent){
        oleNoticeBo.setNoticeTitle(body);
        oleNoticeBo.setNoticeSpecificContent(bodyContent);
        oleNoticeBo.setNoticeSpecificFooterContent(noticeSpecificFooterContent);
        return  oleNoticeBo;
    }

    public void setItemInfo(OleNoticeBo oleNoticeBo,OleDeliverRequestBo oleDeliverRequestBo){
        oleNoticeBo.setTitle((oleDeliverRequestBo.getTitle() != null ? oleDeliverRequestBo.getTitle() : ""));
        oleNoticeBo.setAuthor((oleDeliverRequestBo.getAuthor() != null ? oleDeliverRequestBo.getAuthor() : ""));
        oleNoticeBo.setEnumeration((oleDeliverRequestBo.getEnumeration() != null ? oleDeliverRequestBo.getEnumeration() : ""));
        oleNoticeBo.setChronology(oleDeliverRequestBo.getChronology() != null ? oleDeliverRequestBo.getChronology() : "");
        oleNoticeBo.setVolumeNumber((oleDeliverRequestBo.getVolumeNumber() != null ? oleDeliverRequestBo.getAuthor() : ""));
        oleNoticeBo.setItemCallNumber(oleDeliverRequestBo.getCallNumber() != null ? oleDeliverRequestBo.getCallNumber() : "");
        oleNoticeBo.setItemCallNumberPrefix(oleDeliverRequestBo.getCallNumberPrefix()!=null ? oleDeliverRequestBo.getCallNumberPrefix() : "");
        oleNoticeBo.setItemTypeDesc(oleDeliverRequestBo.getItemTypeDesc() != null ? oleDeliverRequestBo.getItemTypeDesc() : "");
        oleNoticeBo.setCopyNumber(oleDeliverRequestBo.getCopyNumber() != null ? oleDeliverRequestBo.getCopyNumber() : "");
        oleNoticeBo.setItemId(oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "");
        String locationName = getLocationName(oleDeliverRequestBo.getShelvingLocation());
        oleNoticeBo.setItemShelvingLocation((locationName != null ? locationName : ""));
    }

    private void setCirculationDeskInfo(OleNoticeBo oleNoticeBo, OleDeliverRequestBo oleDeliverRequestBo) {
        OleCirculationDesk olePickUpLocation = oleDeliverRequestBo.getOlePickUpLocation();
        oleNoticeBo.setCirculationDeskName(olePickUpLocation != null ? olePickUpLocation.getCirculationDeskPublicName() != null ?olePickUpLocation.getCirculationDeskPublicName():""  : "");
        oleNoticeBo.setCirculationDeskReplyToEmail(olePickUpLocation != null ? olePickUpLocation.getReplyToEmail()!=null ?olePickUpLocation.getReplyToEmail():"" : "");
    }

    private void setRequestInformation(OleDeliverRequestBo oleDeliverRequestBo, OleNoticeBo oleNoticeBo) {
        oleNoticeBo.setExpiredOnHoldDate(oleDeliverRequestBo.getHoldExpirationDate());
        oleNoticeBo.setNewDueDate(oleDeliverRequestBo.getNewDueDate());
        oleNoticeBo.setOriginalDueDate(oleDeliverRequestBo.getOriginalDueDate());
    }

    protected String getLocationName(String code) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("locationCode", code);
        List<OleLocation> oleLocation = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, criteria);

        return oleLocation.size() == 1 ? oleLocation.get(0).getLocationName() : "";
    }


    public String generateHTML(List<OleNoticeBo> oleNoticeBos, OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        StringWriter htmlContent = new StringWriter();
        Configuration cfg = new Configuration();
        try {

            String noticeTemplateDirPath = System.getProperty("notice.template.dir");
            File noticeTemplateDir = null;

            if(null == noticeTemplateDirPath){
                noticeTemplateDir = processFTL();
            } else {
                noticeTemplateDir = new File(noticeTemplateDirPath);
            }
            cfg.setDirectoryForTemplateLoading(noticeTemplateDir);

            Template template = cfg.getTemplate("request-notice.ftl");

            Map<String, Object> input = new HashMap<>();
            input.put("oleNoticeContentConfigurationBo", oleNoticeContentConfigurationBo);
            input.put("oleNoticeBo", oleNoticeBos.get(0));
            input.put("oleNoticeBos", oleNoticeBos);

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

    private File processFTL() throws URISyntaxException, IOException {
        File noticeTemplateDir;URI noticeTemplateURI = getClass().getResource("request-notice.ftl").toURI();
        File noticeTemplate = new File(noticeTemplateURI);

        URI itemInfoTemplateURI = getClass().getResource("request-itemInfo.ftl").toURI();
        File itemInfoTemplate = new File(itemInfoTemplateURI);

        String tempDir = System.getProperty("java.io.tmpdir");
        File destinationDirectory = new File(tempDir);
        FileUtils.copyFileToDirectory(noticeTemplate, destinationDirectory);
        FileUtils.copyFileToDirectory(itemInfoTemplate, destinationDirectory);
        noticeTemplateDir = destinationDirectory;
        return noticeTemplateDir;
    }

    protected abstract void processCustomNoticeInfo(OleDeliverRequestBo oleDeliverRequestBo, OleNoticeBo oleNoticeBo);

    protected abstract OlePatronDocument getOlePatron(OleDeliverRequestBo oleDeliverRequestBo);

    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
