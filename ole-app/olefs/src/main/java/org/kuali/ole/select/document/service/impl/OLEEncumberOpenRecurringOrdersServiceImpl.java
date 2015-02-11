package org.kuali.ole.select.document.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.gl.GeneralLedgerConstants;
import org.kuali.ole.gl.businessobject.OriginEntryFull;
import org.kuali.ole.select.document.service.OLEEncumberOpenRecurringOrdersService;
import org.kuali.rice.kns.lookup.LookupResults;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 1/27/15
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEncumberOpenRecurringOrdersServiceImpl implements OLEEncumberOpenRecurringOrdersService {

    protected String batchFileDirectoryName;
    protected BusinessObjectService businessObjectService;

    public File[] getAllFileInBatchDirectory() {
        File[] returnFiles = null;
        if (new File(batchFileDirectoryName) != null) {
            returnFiles = new File(batchFileDirectoryName).listFiles();
    }
        return returnFiles;
    }

    public String getBatchFileDirectoryName() {
        return batchFileDirectoryName;
    }

    public void setBatchFileDirectoryName(String batchFileDirectoryName) {
        this.batchFileDirectoryName = batchFileDirectoryName;
    }

    protected BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }
}
