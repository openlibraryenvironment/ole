package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/26/13
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceSearchFinder extends UifKeyValuesFinderBase {
    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        List<KeyValue> ersSearchoptions = new ArrayList<KeyValue>();

        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_TITLE, OLEConstants.OLEEResourceRecord.ERESOURCE_RECORD_NAME));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_ISBN, OLEConstants.OLEEResourceRecord.ERESOURCE_ISBN_ISSN_VALUE));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_OCLC, OLEConstants.OLEEResourceRecord.ERESOURCE_OCLC_VALUE));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_PUBLISHER, OLEConstants.OLEEResourceRecord.ERESOURCE_PUBLISHER_VALUE));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_PLATFORM_PROVIDER, OLEConstants.OLEEResourceRecord.ERESOURCE_PLATFORM_PROV_NAME));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, OLEConstants.OLEEResourceRecord.ERESOURCE_RECORD_ID));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_DOC_NUMB, OLEConstants.OLEEResourceRecord.ERESOURCE_DOC_ID));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_PO_ID, OLEConstants.OLEEResourceRecord.ERESOURCE_PO_NUMBER));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_INVOICE_NO, OLEConstants.OLEEResourceRecord.ERESOURCE_INVOICE_NUMBER));
        ersSearchoptions.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.ERESOURCE_LICENSE_REQ_STATUS, OLEConstants.OLEEResourceRecord.ERESOURCE_SUB_WORKFLOW_STATUS));

        return ersSearchoptions;
    }
}
