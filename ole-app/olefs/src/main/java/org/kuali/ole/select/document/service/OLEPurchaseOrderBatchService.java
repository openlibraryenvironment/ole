package org.kuali.ole.select.document.service;

import org.kuali.ole.select.document.OLEPurchaseOrderBatchDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.rice.krad.UserSession;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 5/18/15
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OLEPurchaseOrderBatchService  {

    public void readFile(OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument, UserSession userSession, File file);

    public OlePurchaseOrderDocument createPurchaseOrderAmendmentDocument(Map map, String docNumber);

    public void writeFile();

    public List<String> createFileForPOBA(OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument) throws Exception;

    public void downloadCSV(List<String> poIds);

    public void createPOBADirectory();
}
