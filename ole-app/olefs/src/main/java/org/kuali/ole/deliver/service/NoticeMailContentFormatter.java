package org.kuali.ole.deliver.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.FileUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 4/6/15.
 */
public abstract class NoticeMailContentFormatter {
    private static final Logger LOG = Logger.getLogger(NoticeMailContentFormatter.class);
    private ParameterValueResolver parameterValueResolver;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    private OlePatronHelperServiceImpl olePatronHelperService;
    private CircDeskLocationResolver circDeskLocationResolver;

    public OlePatronHelperService getOlePatronHelperService() {
        if (olePatronHelperService == null)
            olePatronHelperService = new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronHelperService(OlePatronHelperServiceImpl olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    public String generateMailContentForPatron(List<OleLoanDocument> oleLoanDocuments, OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        List<OleNoticeBo> noticeBos = initialiseOleNoticeBos(oleLoanDocuments, oleNoticeContentConfigurationBo);
        String noticeHtmlContent = generateHTML(noticeBos, oleNoticeContentConfigurationBo);
        return noticeHtmlContent;
    }

    public List<OleNoticeBo> initialiseOleNoticeBos(List<OleLoanDocument> oleLoanDocuments, OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        List<OleNoticeBo> oleNoticeBos = new ArrayList<>();
        if (oleLoanDocuments != null && oleLoanDocuments.size() > 0) {
            OlePatronDocument olePatron = oleLoanDocuments.get(0).getOlePatron();
            for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
                OleNoticeBo oleNoticeBo = new OleNoticeBo();
                oleNoticeBo.setTitle(oleNoticeContentConfigurationBo.getNoticeTitle());
                oleNoticeBo.setNoticeTitle(oleNoticeContentConfigurationBo.getNoticeTitle());
                oleNoticeBo.setNoticeName(oleNoticeContentConfigurationBo.getNoticeTitle());
                oleNoticeBo.setNoticeType(oleNoticeContentConfigurationBo.getNoticeType());
                setPatronInfo(olePatron, oleNoticeBo);
                setNoticeBodyAndContent(oleNoticeBo, oleNoticeContentConfigurationBo.getNoticeTitle(), oleNoticeContentConfigurationBo.getNoticeBody(), oleNoticeContentConfigurationBo.getNoticeFooterBody());
                setItemInfo(oleNoticeBo, oleLoanDocument);
                processCustomNoticeInfo(oleLoanDocument, oleNoticeBo);
                oleNoticeBos.add(oleNoticeBo);
            }
        }
        return oleNoticeBos;
    }

    public OleNoticeBo setPatronInfo(OlePatronDocument olePatronDocument, OleNoticeBo oleNoticeBo) {
        String patronName = olePatronDocument.getPatronName();
        String preferredAddress = olePatronDocument.getPreferredAddress();
        String email = olePatronDocument.getEmail();
        String phoneNumber = olePatronDocument.getPhoneNumber();
        oleNoticeBo.setPatronName(patronName != null ? patronName : "");
        oleNoticeBo.setPatronAddress(preferredAddress != null ? preferredAddress: "");
        oleNoticeBo.setPatronEmailAddress(email != null ? email : "");
        oleNoticeBo.setPatronPhoneNumber(phoneNumber != null ? phoneNumber : "");
        return oleNoticeBo;
    }


    public OleNoticeBo setNoticeBodyAndContent(OleNoticeBo oleNoticeBo, String body, String bodyContent, String noticeSpecificFooterContent) {
        oleNoticeBo.setNoticeTitle(body);
        oleNoticeBo.setNoticeSpecificContent(bodyContent);
        oleNoticeBo.setNoticeSpecificFooterContent(noticeSpecificFooterContent);
        return oleNoticeBo;
    }

    public void setItemInfo(OleNoticeBo oleNoticeBo, OleLoanDocument oleLoanDocument) {
        oleNoticeBo.setTitle((oleLoanDocument.getTitle() != null ? oleLoanDocument.getTitle() : ""));
        oleNoticeBo.setAuthor((oleLoanDocument.getAuthor() != null ? oleLoanDocument.getAuthor() : ""));
        oleNoticeBo.setEnumeration((oleLoanDocument.getEnumeration() != null ? oleLoanDocument.getEnumeration() : ""));
        oleNoticeBo.setChronology(oleLoanDocument.getChronology() != null ? oleLoanDocument.getChronology() : "");
        oleNoticeBo.setVolumeNumber((oleLoanDocument.getItemVolumeNumber() != null ? oleLoanDocument.getAuthor() : ""));
        oleNoticeBo.setItemCallNumber(oleLoanDocument.getItemCallNumber() != null ? oleLoanDocument.getItemCallNumber() : "");
        oleNoticeBo.setItemCallNumberPrefix(oleLoanDocument.getItemCallNumberPrefix()!=null ? oleLoanDocument.getItemCallNumberPrefix() : "");
        oleNoticeBo.setItemTypeDesc(oleLoanDocument.getItemTypeDesc() != null ? oleLoanDocument.getItemTypeDesc() : "");
        oleNoticeBo.setCopyNumber(oleLoanDocument.getItemCopyNumber() != null ? oleLoanDocument.getItemCopyNumber() : "");
        oleNoticeBo.setDueDateString(oleLoanDocument.getLoanDueDate() != null ? (oleLoanDocument.getLoanDueDate().toString() != null ? oleLoanDocument.getLoanDueDate().toString() : "") : "");
        oleNoticeBo.setItemId(oleLoanDocument.getItemId() != null ? oleLoanDocument.getItemId() : "");
        oleNoticeBo.setItemShelvingLocation((getLocationName(oleLoanDocument.getItemLocation()) != null ? getLocationName(oleLoanDocument.getItemLocation()) : ""));
        oleNoticeBo.setCheckInDate(oleLoanDocument.getCheckInDate() != null ? (StringUtils.isNotBlank(oleLoanDocument.getCheckInDate().toString()) ? oleLoanDocument.getCheckInDate().toString() : "") : "");
        oleNoticeBo.setMissingPieceNote(StringUtils.isNotBlank(oleLoanDocument.getMissingPieceNote()) ? oleLoanDocument.getMissingPieceNote() : "");
        oleLoanDocument.setOleCirculationDesk(getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()));
        if (oleLoanDocument.getOleCirculationDesk() != null) {
            oleNoticeBo.setCirculationDeskName(oleLoanDocument.getOleCirculationDesk().getCirculationDeskPublicName());
        }
        setLocationInforamtion(oleNoticeBo, oleLoanDocument.getItemFullLocation());
    }


    private ParameterValueResolver getParameterInstance() {
        if (null == parameterValueResolver) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

    private OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (null == oleDeliverRequestDocumentHelperService) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }


    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

    protected SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE + " " + RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
    }

    protected String getLocationName(String code) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("locationCode", code);
        List<OleLocation> oleLocation = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, criteria);

        return oleLocation.size() == 1 ? oleLocation.get(0).getLocationName() : "";
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    private BusinessObjectService businessObjectService;

    public String generateHTML(List<OleNoticeBo> oleNoticeBos, OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
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
            if(destinationDirectory.isDirectory()) {
                FileUtils.copyFileToDirectory(noticeTemplate, destinationDirectory);
            }
        }

        noticeTemplateDir = destinationDirectory;
        return noticeTemplateDir;
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

    public OleNoticeBo setLocationInforamtion(OleNoticeBo oleNoticeBo, String itemFullLocation) {
        Map<String, String> locationMap = getCircDeskLocationResolver().getLocationMap(itemFullLocation);
        oleNoticeBo.setItemInstitution(getLocationName(locationMap.get(OLEConstants.ITEM_INSTITUTION)));
        oleNoticeBo.setItemCampus(getLocationName(locationMap.get(OLEConstants.ITEM_CAMPUS)));
        oleNoticeBo.setItemCollection(getLocationName(locationMap.get(OLEConstants.ITEM_COLLECTION)));
        oleNoticeBo.setItemLibrary(getLocationName(locationMap.get(OLEConstants.ITEM_LIBRARY)));
        oleNoticeBo.setItemLocation(getLocationName(locationMap.get(OLEConstants.ITEM_SHELVING)));
        oleNoticeBo.setItemShelvingLocation(getLocationName(locationMap.get(OLEConstants.ITEM_SHELVING)));
        return oleNoticeBo;
    }


    protected abstract void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo);

    protected List<String> getFTLList(){
        List<String> fileNames = new ArrayList<>();

        fileNames.add("notice.ftl");
        fileNames.add("itemInfo.ftl");

        return fileNames;
    }

    protected String getBaseFTLTemplate() {
        return "notice.ftl";
    }
}
