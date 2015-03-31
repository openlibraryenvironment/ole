package org.kuali.ole.select.document;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.document.OleTransactionalDocumentBase;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.service.OLEPlatformService;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.document.Copyable;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

import java.sql.Date;

/**
 * Created by chenchulakshmig on 9/10/14.
 * This class is the document class for Ole Platform Record Document
 */

public class OLEPlatformRecordDocument extends OleTransactionalDocumentBase implements Copyable {

    private OLEPlatformService olePlatformService;

    public OLEPlatformService getOlePlatformService() {
        if (olePlatformService == null) {
            olePlatformService = GlobalResourceLoader.getService(OLEConstants.PLATFORM_SERVICE);
        }
        return olePlatformService;
    }

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEPlatformRecordDocument.class);

    private String name;

    private String olePlatformId ;

    private String statusId;

    private String statusName;

    private Integer gokbId;

    private String platformProviderName;

    private Integer platformProviderId;

    private String vendorLink;

    private String vendorId;

    private Integer vendorHeaderGeneratedIdentifier;

    private Integer vendorDetailAssignedIdentifier;

    private boolean active;

    private String software;

    private boolean branded;

    private String brandingNote;

    private boolean linkResolver;

    private String linkResolverNote;

    private boolean gokbFlag;

    private boolean saveValidationFlag;

    private boolean platformProviderFlag;

    private VendorDetail vendorDetail;

    private OLEPlatformStatus olePlatformStatus;

    private String gokbPlatformStatus;

    private List<OLEPlatformGeneralNote> generalNotes = new ArrayList<>();

    private List<OLEPlatformAdminUrl> adminUrls = new ArrayList<>();

    private List<OLEPlatformEventLog> eventLogs = new ArrayList<>();

    private List<OLEPlatformEventLog> filterEventLogs = new ArrayList<>();

    private List<OLEPlatformVariantTitle> variantTitles = new ArrayList<>();

    private List<OLEEResourceRecordDocument> linkedEResources = new ArrayList<>();

    private Timestamp gokbLastUpdatedDate;

    public OLEPlatformRecordDocument() {
        this.setActive(true);
        this.setBranded(true);
        this.setLinkResolver(true);
        getGeneralNotes().add(new OLEPlatformGeneralNote());
        getVariantTitles().add(new OLEPlatformVariantTitle());
    }

    public String getOlePlatformId() {
        return olePlatformId;
    }

    public void setOlePlatformId(String olePlatformId) {
        this.olePlatformId = olePlatformId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        if (getStatusId() != null) {
            OLEPlatformStatus platformStatus = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OLEPlatformStatus.class, statusId);
            if (platformStatus != null) {
                return platformStatus.getPlatformStatusName();
            }
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getGokbId() {
        return gokbId;
    }

    public void setGokbId(Integer gokbId) {
        this.gokbId = gokbId;
    }

    public String getPlatformProviderName() {
        /*if (vendorDetail != null) {
            return vendorDetail.getVendorName();
        }*/
        return platformProviderName;
    }

    public void setPlatformProviderName(String platformProviderName) {
        this.platformProviderName = platformProviderName;
    }

    public Integer getPlatformProviderId() {
        return platformProviderId;
    }

    public void setPlatformProviderId(Integer platformProviderId) {
        this.platformProviderId = platformProviderId;
    }

    public String getVendorLink() {
        String oleurl = ConfigContext.getCurrentContextConfig().getProperty("ole.url");
        String url = oleurl + "/kr/inquiry.do?methodToCall=start&amp;businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&amp;vendorHeaderGeneratedIdentifier=" + vendorHeaderGeneratedIdentifier + "&amp;vendorDetailAssignedIdentifier="
                + vendorDetailAssignedIdentifier;
        return url;
    }

    public void setVendorLink(String vendorLink) {
        this.vendorLink = vendorLink;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(Integer vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }

    public Integer getVendorDetailAssignedIdentifier() {
        return vendorDetailAssignedIdentifier;
    }

    public void setVendorDetailAssignedIdentifier(Integer vendorDetailAssignedIdentifier) {
        this.vendorDetailAssignedIdentifier = vendorDetailAssignedIdentifier;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public boolean isBranded() {
        return branded;
    }

    public void setBranded(boolean branded) {
        this.branded = branded;
    }

    public String getBrandingNote() {
        return brandingNote;
    }

    public void setBrandingNote(String brandingNote) {
        this.brandingNote = brandingNote;
    }

    public boolean isLinkResolver() {
        return linkResolver;
    }

    public void setLinkResolver(boolean linkResolver) {
        this.linkResolver = linkResolver;
    }

    public String getLinkResolverNote() {
        return linkResolverNote;
    }

    public void setLinkResolverNote(String linkResolverNote) {
        this.linkResolverNote = linkResolverNote;
    }

    public VendorDetail getVendorDetail() {
        return vendorDetail;
    }

    public void setVendorDetail(VendorDetail vendorDetail) {
        this.vendorDetail = vendorDetail;
    }

    public OLEPlatformStatus getOlePlatformStatus() {
        return olePlatformStatus;
    }

    public void setOlePlatformStatus(OLEPlatformStatus olePlatformStatus) {
        this.olePlatformStatus = olePlatformStatus;
    }

    public List<OLEPlatformGeneralNote> getGeneralNotes() {
        return generalNotes;
    }

    public void setGeneralNotes(List<OLEPlatformGeneralNote> generalNotes) {
        this.generalNotes = generalNotes;
    }

    public List<OLEPlatformAdminUrl> getAdminUrls() {
        return adminUrls;
    }

    public void setAdminUrls(List<OLEPlatformAdminUrl> adminUrls) {
        this.adminUrls = adminUrls;
    }

    public List<OLEPlatformEventLog> getEventLogs() {
        return eventLogs;
    }

    public void setEventLogs(List<OLEPlatformEventLog> eventLogs) {
        this.eventLogs = eventLogs;
    }

    public List<OLEPlatformEventLog> getFilterEventLogs() {
        return filterEventLogs;
    }

    public void setFilterEventLogs(List<OLEPlatformEventLog> filterEventLogs) {
        this.filterEventLogs = filterEventLogs;
    }

    public List<OLEEResourceRecordDocument> getLinkedEResources() {
        return linkedEResources;
    }

    public void setLinkedEResources(List<OLEEResourceRecordDocument> linkedEResources) {
        this.linkedEResources = linkedEResources;
    }

    public boolean isGokbFlag() {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(this.getOlePlatformId())) {
            return getOlePlatformService().getGokbLinkFlag(this.olePlatformId);
        }
        return false;
    }

    public void setGokbFlag(boolean gokbFlag) {
        this.gokbFlag = gokbFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSaveValidationFlag() {
        return saveValidationFlag;
    }

    public void setSaveValidationFlag(boolean saveValidationFlag) {
        this.saveValidationFlag = saveValidationFlag;
    }

    public List<OLEPlatformVariantTitle> getVariantTitles() {
        return variantTitles;
    }

    public void setVariantTitles(List<OLEPlatformVariantTitle> variantTitles) {
        this.variantTitles = variantTitles;
    }

    public void setResultDetails(DocumentSearchResult searchResult, List<OLESearchCondition> oleSearchEresources) {
        List<DocumentAttribute> documentAttributes = searchResult.getDocumentAttributes();
        for (DocumentAttribute docAttribute : documentAttributes) {
            String name = docAttribute.getName();
            if (OLEConstants.PLATFORM_RESULT_FIELDS.contains(name)) {
                if (name.equals(OLEConstants.PLATFORM_NAME)) {
                    name = OLEConstants.PLATFORM_NAME;
                }
                Method getMethod;
                try {
                    if (name.equals(OLEConstants.GOKB_ID)) {
                        getMethod = getSetMethod(OLEPlatformRecordDocument.class, name, new Class[]{Integer.class});
                        getMethod.invoke(this, Integer.parseInt(docAttribute.getValue().toString()));
                    } else {
                        getMethod = getSetMethod(OLEPlatformRecordDocument.class, name, new Class[]{String.class});
                        getMethod.invoke(this, docAttribute.getValue().toString());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private Method getSetMethod(Class targetClass, String attr, Class[] objectAttributes) throws Exception {
        Method method = targetClass.getMethod("set" + StringUtils.capitalize(attr), objectAttributes);
        return method;
    }

    public String getGokbPlatformStatus() {
        return gokbPlatformStatus;
    }

    public void setGokbPlatformStatus(String gokbPlatformStatus) {
        this.gokbPlatformStatus = gokbPlatformStatus;
    }

    @Override
    public void processAfterRetrieve() {
        super.processAfterRetrieve();
        LOG.debug("Inside OLEPlatformRecordDocument processAfterRetrieve");
        if (this.getVendorHeaderGeneratedIdentifier() != null && this.getVendorDetailAssignedIdentifier() != null) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID, this.getVendorHeaderGeneratedIdentifier());
            vendorMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID, this.getVendorDetailAssignedIdentifier());
            List<VendorDetail> vendorDetails = (List<VendorDetail>) KRADServiceLocator.getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
            if (CollectionUtils.isNotEmpty(vendorDetails)) {
                this.setPlatformProviderName(vendorDetails.get(0).getVendorName());
            }
        }
        if (this.getOlePlatformId() != null) {
            Map<String, String> platformMap = new HashMap<String, String>();
            platformMap.put(OLEConstants.PLATFORM_ID, this.getOlePlatformId());
            List<OLEEResourceInstance> oleeResourceInstances = (List<OLEEResourceInstance>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEEResourceInstance.class, platformMap);
            if (oleeResourceInstances != null && oleeResourceInstances.size() > 0) {
                Map avoidingDuplicateMap = new HashMap<>();
                for (OLEEResourceInstance oleeResourceInstance : oleeResourceInstances) {
                    if (oleeResourceInstance.getStatus() != null && oleeResourceInstance.getStatus().equals(OLEConstants.OleHoldings.ACTIVE)) {
                        oleeResourceInstance.getOleERSDocument().setActiveTitlesCount(oleeResourceInstance.getOleERSDocument().getActiveTitlesCount() + 1);
                        avoidingDuplicateMap.put(oleeResourceInstance.getOleERSIdentifier(), oleeResourceInstance.getOleERSDocument());
                    }
                }
                getLinkedEResources().addAll(avoidingDuplicateMap.values());
            }
        }
    }


    @Override
    public boolean getAllowsCopy() {
        boolean allowsCopy = true;
        return allowsCopy;
    }

    @Override
    public void toCopy() throws WorkflowException {
        super.toCopy();
        this.setOlePlatformId(null);
        this.setGokbId(null);
        this.setGokbFlag(false);
        this.setLinkedEResources(new ArrayList<OLEEResourceRecordDocument>());
        for (OLEPlatformGeneralNote olePlatformGeneralNote : this.getGeneralNotes()) {
            olePlatformGeneralNote.setGeneralNoteId(null);
        }
        for (OLEPlatformVariantTitle olePlatformVariantTitle : this.getVariantTitles()) {
            olePlatformVariantTitle.setVariantTitleId(null);
        }
        for (OLEPlatformAdminUrl olePlatformAdminUrl : this.getAdminUrls()) {
            olePlatformAdminUrl.setPlatformAdminUrlId(null);
        }
        this.setEventLogs(new ArrayList<OLEPlatformEventLog>());
        this.setObjectId(null);
        this.setVersionNumber(null);
    }

    @Override
    public void prepareForSave(KualiDocumentEvent event) {
        // TODO Auto-generated method stub
        // first populate, then call super
        super.prepareForSave(event);
        Map platformMap = new HashMap<>();
        platformMap.put(OLEConstants.OLE_PLATFORM_ID, this.getOlePlatformId());
        OLEPlatformRecordDocument platformRecordDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEPlatformRecordDocument.class, platformMap);
        String status = null;
        Integer gokbId = null;
        if (platformRecordDocument != null) {
            status = platformRecordDocument.getStatusName();
            gokbId = platformRecordDocument.getGokbId();
        }
        OLEPlatformEventLog platformEventLog = new OLEPlatformEventLog();
        if (status == null) {
            platformEventLog.setSaveFlag(true);
            platformEventLog.setLogTypeId("3");
            platformEventLog.setEventReportedDate(new Date(System.currentTimeMillis()));
            platformEventLog.setEventUserId(GlobalVariables.getUserSession().getPrincipalId());
            platformEventLog.setEventNote(OLEConstants.OLEEResourceRecord.STATUS_IS + getStatusName());
            this.getEventLogs().add(platformEventLog);
        } else if (!status.equals(getStatusName())) {
            platformEventLog.setSaveFlag(true);
            platformEventLog.setLogTypeId("3");
            platformEventLog.setEventReportedDate(new Date(System.currentTimeMillis()));
            platformEventLog.setEventUserId(GlobalVariables.getUserSession().getPrincipalId());
            platformEventLog.setEventNote(OLEConstants.OLEEResourceRecord.STATUS_FROM + status + OLEConstants.OLEEResourceRecord.STATUS_TO + getStatusName());
            this.getEventLogs().add(platformEventLog);
        }
        if ((this.getGokbId() != null && gokbId == null) || (this.getGokbId() != null && gokbId != null && !gokbId.equals(this.getGokbId()))) {
            platformEventLog = new OLEPlatformEventLog();
            platformEventLog.setSaveFlag(true);
            platformEventLog.setLogTypeId("3");
            platformEventLog.setEventReportedDate(new Date(System.currentTimeMillis()));
            platformEventLog.setEventUserId(GlobalVariables.getUserSession().getPrincipalId());
            platformEventLog.setEventNote("Platform Record is linked with gokb " + this.getGokbId() + " manually.");
            this.getEventLogs().add(platformEventLog);
        }
    }

    public Timestamp getGokbLastUpdatedDate() {
        return gokbLastUpdatedDate;
    }

    public void setGokbLastUpdatedDate(Timestamp gokbLastUpdatedDate) {
        this.gokbLastUpdatedDate = gokbLastUpdatedDate;
    }

    public boolean isPlatformProviderFlag() {
        return platformProviderFlag;
    }

    public void setPlatformProviderFlag(boolean platformProviderFlag) {
        this.platformProviderFlag = platformProviderFlag;
    }
}
