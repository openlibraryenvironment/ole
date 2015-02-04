package org.kuali.ole.docstore.engine.service.storage.rdbms;

import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.License;
import org.kuali.ole.docstore.common.document.LicenseOnixpl;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.LicenseRecord;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsLicenseOnixplDocumentManager extends RdbmsLicenseDocumentManager {

    private static RdbmsLicenseOnixplDocumentManager rdbmsLicenseOnixplDocumentManager = null;

    public static RdbmsLicenseOnixplDocumentManager getInstance() {
        if (rdbmsLicenseOnixplDocumentManager == null) {
            rdbmsLicenseOnixplDocumentManager = new RdbmsLicenseOnixplDocumentManager();
        }
        return rdbmsLicenseOnixplDocumentManager;
    }

    @Override
    protected void setLicenseInfo(Object object, LicenseRecord licenseRecord) {
        License license = (License) object;
        byte[] bytes = license.getContent().getBytes();
        licenseRecord.setContent(bytes);
        licenseRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_LICENSE_ONIXPL);
    }

    @Override
    protected void setLicenseInfoForUpdate(Object object, LicenseRecord licenseRecord) {
        License license = (License) object;
        byte[] bytes = license.getContent().getBytes();
        licenseRecord.setContent(bytes);
    }

    @Override
    protected License getLicenseInfo(LicenseRecord licenseRecord) {
        License license = new LicenseOnixpl();
        license.setFormat("onixpl");
        license.setContent(new String(licenseRecord.getContent()));
        return license;
    }
}
