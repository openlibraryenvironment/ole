package org.kuali.ole.docstore.engine.service.storage.rdbms;

import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.License;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.LicenseRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RdbmsLicenseDocumentManager extends RdbmsAbstarctDocumentManager {
    private static final Logger LOG = Logger.getLogger(RdbmsLicenseDocumentManager.class);

    protected abstract void setLicenseInfo(Object object, LicenseRecord licenseRecord);

    protected abstract void setLicenseInfoForUpdate(Object object, LicenseRecord licenseRecord);

    protected abstract License getLicenseInfo(LicenseRecord licenseRecord);

    @Override
    public void create(Object object) {
        License license = (License) object;
        LicenseRecord licenseRecord = new LicenseRecord();
        licenseRecord.setCreatedBy(license.getCreatedBy());
        licenseRecord.setDateCreated(createdDate());
        setLicenseInfo(object, licenseRecord);
//        licenseRecord.setContent(license.getByteContent());
        getBusinessObjectService().save(licenseRecord);
        license.setId(DocumentUniqueIDPrefix.getPrefixedId(licenseRecord.getUniqueIdPrefix(), licenseRecord.getLicenseId()));
    }

    @Override
    public void update(Object object) {
        License license = (License) object;
        LicenseRecord licenseRecord = getExistingLicense(license.getId());
        if (licenseRecord == null) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.LICENSE_ID_NOT_FOUND, DocstoreResources.LICENSE_ID_NOT_FOUND);
            docstoreException.addErrorParams("licenseId", DocumentUniqueIDPrefix.getDocumentId(license.getId()));
            throw docstoreException;
        }
        licenseRecord.setUpdatedBy(license.getUpdatedBy());
        licenseRecord.setDateUpdated(createdDate());
        setLicenseInfoForUpdate(object, licenseRecord);
        getBusinessObjectService().save(licenseRecord);
    }

    private LicenseRecord getExistingLicense(String licenseId) {
        Map map = new HashMap();
        map.put("licenseId", DocumentUniqueIDPrefix.getDocumentId(licenseId));
        LicenseRecord licenseRecord = (LicenseRecord) getBusinessObjectService().findByPrimaryKey(LicenseRecord.class, map);
        return licenseRecord;
    }

    @Override
    public List<Object> retrieve(List<String> licenseIds) {
        List<Object> licenses = new ArrayList<>();
        for (String licenseId : licenseIds) {
            licenses.add((License) retrieve(licenseId));
        }
        return licenses;
    }

    @Override
    public void delete(String licenseId) {
        Map map = new HashMap();
        map.put("licenseId", DocumentUniqueIDPrefix.getDocumentId(licenseId));
        getBusinessObjectService().deleteMatching(LicenseRecord.class, map);
    }

    @Override
    public Object retrieve(String licenseId) {
        LicenseRecord licenseRecord = getExistingLicense(licenseId);
        if (licenseRecord == null) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.LICENSE_ID_NOT_FOUND, DocstoreResources.LICENSE_ID_NOT_FOUND);
            docstoreException.addErrorParams("licenseId", DocumentUniqueIDPrefix.getDocumentId(licenseId));
            throw docstoreException;
        }
        LOG.info("licenseId = " + licenseId);
        License license = getLicenseInfo(licenseRecord);
        license.setId(DocumentUniqueIDPrefix.getPrefixedId(licenseRecord.getUniqueIdPrefix(), licenseRecord.getLicenseId()));
        license.setCreatedBy(licenseRecord.getCreatedBy());
        license.setUpdatedBy(licenseRecord.getUpdatedBy());
        if (licenseRecord.getDateCreated() != null) {
            license.setCreatedOn(licenseRecord.getDateCreated().toString());
        }
        if (licenseRecord.getDateUpdated() != null) {
            license.setUpdatedOn(licenseRecord.getDateUpdated().toString());
        }
//        license.setByteContent(licenseRecord.getContent());
        //TODO uniqueidprefix
        //TODO content
        return license;
    }

    @Override
    public Object retrieveTree(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void validate(Object object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteVerify(String bibId) {

    }
}
