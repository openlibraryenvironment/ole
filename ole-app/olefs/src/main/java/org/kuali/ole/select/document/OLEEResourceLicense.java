package org.kuali.ole.select.document;

import org.kuali.ole.select.bo.OleLicenseRequestBo;
import org.kuali.ole.service.OleLicenseRequestService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: juliyamonicas
 * Date: 6/27/13
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceLicense extends PersistableBusinessObjectBase {

    private String oleEResourceLicenseId;
    private String oleERSIdentifier;
    private String oleLicenseRequestId;
    private String licenseDocumentNumber;
    private String attachments;
    private String licenseStartDate;
    private String licenseEndDate;
    private String licensor;
    private String documentDescription;
    private DocumentRouteHeaderValue documentRouteHeaderValue = new DocumentRouteHeaderValue();
    private OleLicenseRequestService oleLicenseRequestService;
    private OleLicenseRequestBo oleLicenseRequestBo;
    private OLEEResourceRecordDocument oleERSDocument = new OLEEResourceRecordDocument();
    private String attachmentString = "No Attachments";

    public String getLicensor() {
        return licensor;
    }

    public void setLicensor(String licensor) {
        this.licensor = licensor;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getAttachmentString() {
        return attachmentString;
    }

    public void setAttachmentString(String attachmentString) {
        this.attachmentString = attachmentString;
    }

    public String getOleEResourceLicenseId() {
        return oleEResourceLicenseId;
    }

    public void setOleEResourceLicenseId(String oleEResourceLicenseId) {
        this.oleEResourceLicenseId = oleEResourceLicenseId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getOleLicenseRequestId() {
        return oleLicenseRequestId;
    }

    public void setOleLicenseRequestId(String oleLicenseRequestId) {
        this.oleLicenseRequestId = oleLicenseRequestId;
    }

    public String getLicenseDocumentNumber() {
        return licenseDocumentNumber;
    }

    public void setLicenseDocumentNumber(String licenseDocumentNumber) {
        this.licenseDocumentNumber = licenseDocumentNumber;
    }


    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getLicenseStartDate() {
        return licenseStartDate;
    }

    public void setLicenseStartDate(String licenseStartDate) {
        this.licenseStartDate = licenseStartDate;
    }

    public String getLicenseEndDate() {
        return licenseEndDate;
    }

    public void setLicenseEndDate(String licenseEndDate) {
        this.licenseEndDate = licenseEndDate;
    }

    public DocumentRouteHeaderValue getDocumentRouteHeaderValue() {
        return documentRouteHeaderValue;
    }

    public void setDocumentRouteHeaderValue(DocumentRouteHeaderValue documentRouteHeaderValue) {
        this.documentRouteHeaderValue = documentRouteHeaderValue;
    }

    public OleLicenseRequestBo getOleLicenseRequestBo() {
        if (oleLicenseRequestBo == null && documentRouteHeaderValue.getDocumentId() != null &&
                documentRouteHeaderValue != null) {
            oleLicenseRequestBo = getOleLicenseRequestService().getLicenseRequestFromDocumentContent(documentRouteHeaderValue.getDocContent());
        }
        return oleLicenseRequestBo;
    }

    public void setOleLicenseRequestBo(OleLicenseRequestBo oleLicenseRequestBo) {
        this.oleLicenseRequestBo = oleLicenseRequestBo;
    }

    public OleLicenseRequestService getOleLicenseRequestService() {
        if (oleLicenseRequestService == null) {
            oleLicenseRequestService = GlobalResourceLoader.getService("oleLicenseRequestService");
        }
        return oleLicenseRequestService;
    }

    public OLEEResourceRecordDocument getOleERSDocument() {
        return oleERSDocument;
    }

    public void setOleERSDocument(OLEEResourceRecordDocument oleERSDocument) {
        this.oleERSDocument = oleERSDocument;
    }

    public String getAttachmentDisplay() {
        String attachmentLinkLabel = "";
        List<String> lists = new ArrayList<>();
        lists.add(getDocumentRouteHeaderValue().getDocumentId());

        for (String list : lists) {
            int i = getOleLicenseRequestService().getLicenseAttachments(getDocumentRouteHeaderValue().getDocumentId());
            if (i > 1) {
                attachmentLinkLabel = "Multiple Attachments";
            } else if (i == 1) {
                attachmentLinkLabel = "View Attachments";
            } else if (i == 0) {
                attachmentLinkLabel = "No Attachments";
            } else {
            }
        }

        return attachmentLinkLabel;

    }

    public String getUrl() {
        String link = "";
        List<String> list = new ArrayList<>();
        list.add(getDocumentRouteHeaderValue().getDocumentId());
        for (String l : list) {
            int i = getOleLicenseRequestService().getLicenseAttachments(getDocumentRouteHeaderValue().getDocumentId());
            if (i > 1) {
                link ="oleLicenseRequest?methodToCall=getDocument&amp;viewId=OLEEresourceAgreementView&amp;documentClass=org.kuali.rice.krad.web.form.MaintenanceDocumentForm&amp;docId=" + getDocumentRouteHeaderValue().getDocumentId();
            }
            else if (i == 1) {
                link = "oleLicenseRequest?methodToCall=downloadDocument&amp;documentClass=org.kuali.rice.krad.web.form.MaintenanceDocumentForm&amp;docId=" + getDocumentRouteHeaderValue().getDocumentId() + "&amp;command=displayDocSearchView&amp;selectedLineIndex=0";
            }
            else {
            }
        }

        return link;

    }
}