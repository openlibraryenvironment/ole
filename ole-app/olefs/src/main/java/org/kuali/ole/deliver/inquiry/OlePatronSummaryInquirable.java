package org.kuali.ole.deliver.inquiry;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;

import java.util.Map;

/**
 * OlePatronSummaryInquirable provides URL to download the failed patron attachment and filename.
 */
public class OlePatronSummaryInquirable extends KualiInquirableImpl {
    public Object object;

    /**
     * This method will retrieve the data object using the parameters
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
     * This method will create the url by appending the olePatronSummaryId to call the download method in the controller
     *
     * @return url
     */
    public String getUrl() {
        OlePatronIngestSummaryRecord olePatronIngestSummaryRecord = (OlePatronIngestSummaryRecord) object;
        String url = OLEConstants.FAILED_PATRON_ATTACHMENT_DOWNLOAD_URL + olePatronIngestSummaryRecord.getOlePatronSummaryId();
        return url;
    }

    /**
     * This will return the file name of the error xml otherwise it will return null
     *
     * @return fileName
     */
    public String getFileName() {
        OlePatronIngestSummaryRecord olePatronIngestSummaryRecord = (OlePatronIngestSummaryRecord) object;
        if (olePatronIngestSummaryRecord.getPatronFailedCount() != null && olePatronIngestSummaryRecord.getPatronFailedCount() > 0)
            return olePatronIngestSummaryRecord.getOlePatronSummaryId() + OLEConstants.FAILED_PATRON_RECORD_NAME;
        else
            return null;
    }
}
