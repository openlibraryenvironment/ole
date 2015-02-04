package org.kuali.ole.module.purap.document.authorization;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.rice.krad.document.Document;

/**
 * Created by chenchulakshmig on 10/15/14.
 */
public class PlatformDocumentPresentationController extends PurchasingAccountsPayableDocumentPresentationController {

    @Override
    public boolean canCopy (Document document) {
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) document;
        if (super.canCopy(document)) {
            if (!StringUtils.equalsIgnoreCase(olePlatformRecordDocument.getDocumentHeader().getWorkflowDocument().getStatus().name(), PurapConstants.PlatformStatuses.APPDOC_INITIATE)) {
                return true;
            }
        }
        return false;

    }
}
