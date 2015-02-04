package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/12/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessIngestFile extends PersistableBusinessObjectBase {
    private MultipartFile inputFile;

    public MultipartFile getInputFile() {
        return inputFile;
    }

    public void setInputFile(MultipartFile inputFile) {
        this.inputFile = inputFile;
    }
}
