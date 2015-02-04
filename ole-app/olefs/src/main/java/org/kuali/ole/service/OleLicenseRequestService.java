package org.kuali.ole.service;

import org.kuali.ole.select.bo.OleAgreementDocumentMetadata;
import org.kuali.ole.select.bo.OleLicenseRequestBo;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JuliyaMonica.S
 * Date: 8/2/12
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OleLicenseRequestService {

    public List<OleAgreementDocumentMetadata> processIngestAgreementDocuments(List<OleAgreementDocumentMetadata> oleAgreementDocs);

    public List<OleAgreementDocumentMetadata> processCheckInAgreementDocuments(List<OleAgreementDocumentMetadata> oleAgreementDocs);

    public File downloadAgreementDocumentFromDocstore(OleAgreementDocumentMetadata oleAgreementDocumentMetadata);

    public String getAgreementContent(String uuid);

    public String ingestAgreementContent(String content);

    public List<OleLicenseRequestBo> findLicenseRequestByCriteria(Map<String, String> criteria)throws Exception;

    // public String getLicenseRequestByRequisitionDocNum(String reqDocNum);

    public OleLicenseRequestBo getLicenseRequestFromDocumentContent(String documentContent);

    public boolean deleteAgreementDocument(OleAgreementDocumentMetadata metadata);

    public boolean validateDate (Date documentDate, String fromDate, String toDate)throws Exception;

    public int getLicenseAttachments(String licenseId);
}