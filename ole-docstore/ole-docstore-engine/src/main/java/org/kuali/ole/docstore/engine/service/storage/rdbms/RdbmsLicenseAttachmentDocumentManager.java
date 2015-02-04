package org.kuali.ole.docstore.engine.service.storage.rdbms;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.License;
import org.kuali.ole.docstore.common.document.LicenseAttachment;
import org.kuali.ole.docstore.engine.service.DocstoreServiceImpl;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.LicenseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsLicenseAttachmentDocumentManager extends RdbmsLicenseDocumentManager {

    private static final Logger LOG = LoggerFactory.getLogger(RdbmsLicenseAttachmentDocumentManager.class);

    private static RdbmsLicenseAttachmentDocumentManager rdbmsLicenseAttachmentDocumentManager = null;

    public static RdbmsLicenseAttachmentDocumentManager getInstance() {
        if (rdbmsLicenseAttachmentDocumentManager == null) {
            rdbmsLicenseAttachmentDocumentManager = new RdbmsLicenseAttachmentDocumentManager();
        }
        return rdbmsLicenseAttachmentDocumentManager;
    }

    @Override
    protected void setLicenseInfo(Object object, LicenseRecord licenseRecord) {
        LicenseAttachment licenseAttachment = (LicenseAttachment) object;
        licenseRecord.setFileName(licenseAttachment.getFileName());
        licenseRecord.setDocumentTitle(licenseAttachment.getDocumentTitle());
        licenseRecord.setDocumentMimeType(licenseAttachment.getDocumentMimeType());
        licenseRecord.setDocumentNote(licenseAttachment.getDocumentNote());
        licenseRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_LICENSE_ATTACHMENT);

        File file = new File(licenseAttachment.getFilePath() + File.separator + licenseAttachment.getFileName());
        try {
            licenseRecord.setContent(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            LOG.info("Exception " + e);
        }
    }

    @Override
    protected void setLicenseInfoForUpdate(Object object, LicenseRecord licenseRecord) {
        LicenseAttachment licenseAttachment = (LicenseAttachment) object;
        licenseRecord.setFileName(licenseAttachment.getFileName());
        licenseRecord.setDocumentTitle(licenseAttachment.getDocumentTitle());
        licenseRecord.setDocumentMimeType(licenseAttachment.getDocumentMimeType());
        licenseRecord.setDocumentNote(licenseAttachment.getDocumentNote());
    }

    @Override
    protected License getLicenseInfo(LicenseRecord licenseRecord) {
        LicenseAttachment licenseAttachment = new LicenseAttachment();
        licenseAttachment.setFileName(licenseRecord.getFileName());
        licenseAttachment.setDocumentTitle(licenseRecord.getDocumentTitle());
        licenseAttachment.setDocumentMimeType(licenseRecord.getDocumentMimeType());
        licenseAttachment.setDocumentNote(licenseRecord.getDocumentNote());
        licenseAttachment.setFilePath(FileUtils.getTempDirectoryPath() + File.separator + "license attachment" );
        File file = new File(licenseAttachment.getFilePath() + File.separator +licenseRecord.getFileName());
        LOG.info("file path in docstore : " + licenseAttachment.getFilePath());
        try {
            FileUtils.writeByteArrayToFile(file, licenseRecord.getContent());
        } catch (IOException e) {
            LOG.error("Exception : ", e);
        }
        return licenseAttachment;
    }
}
