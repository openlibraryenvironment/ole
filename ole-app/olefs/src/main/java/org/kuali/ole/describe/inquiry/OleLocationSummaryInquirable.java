package org.kuali.ole.describe.inquiry;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleLocationIngestSummaryRecord;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;

import java.util.Map;

/**
 * The OleLocationSummaryInquirable class is used to return the failed Url and Filename of the ingest summary record.
 */
public class OleLocationSummaryInquirable extends KualiInquirableImpl {
    public Object object;

    /**
     * This method returns an object based on the parameters passed to it.
     *
     * @param parameters
     * @return object
     */
    @Override
    public Object retrieveDataObject(Map parameters) {
        object = super.retrieveDataObject(parameters);
        return object;
    }

    /**
     * This method returns an url string to download an attachment based on the OleLocationSummaryId
     *
     * @return url
     */
    public String getUrl() {
        OleLocationIngestSummaryRecord oleLocationIngestSummaryRecord = (OleLocationIngestSummaryRecord) object;
        String url = OLEConstants.FAILED_LOCATION_ATTACHMENT_DOWNLOAD_URL + oleLocationIngestSummaryRecord.getOleLocationSummaryId();
        return url;
    }

    /**
     * This method returns an null string or a fileName string from the OleLocationIngestSummaryRecord object
     *
     * @return null / fileName string
     */
    public String getFileName() {
        OleLocationIngestSummaryRecord oleLocationIngestSummaryRecord = (OleLocationIngestSummaryRecord) object;
        if (oleLocationIngestSummaryRecord.getOleLocationFailedCount() != null && oleLocationIngestSummaryRecord.getOleLocationFailedCount() > 0)
            return oleLocationIngestSummaryRecord.getOleLocationSummaryId() + OLEConstants.FAILED_LOCATION_RECORD_NAME;
        else
            return null;
    }
}
