package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PP7788
 * Date: 11/29/12
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoundwithDocFieldKeyValueFinder extends UifKeyValuesFinderBase {

    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        BoundwithForm boundwithForm = (BoundwithForm) viewModel;
        List<KeyValue> options = new ArrayList<KeyValue>();
        //TODO: Get drop-down values dynamically by parsing DocumentConfig.xml file
        if (boundwithForm.getSearchParams().getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            options.add(new ConcreteKeyValue("all", "ALL"));
            options.add(new ConcreteKeyValue("Title_search", "Title"));
            options.add(new ConcreteKeyValue("Author_search", "Author"));
            options.add(new ConcreteKeyValue("PublicationDate_search", "Publication Date"));
        } else if (boundwithForm.getSearchParams().getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase(OLEConstants.HOLDING_DOC_TYPE)) {
            options.add(new ConcreteKeyValue("all", "ALL"));
            options.add(new ConcreteKeyValue("Location_search", "Location"));
            options.add(new ConcreteKeyValue("CallNumber_search", "Call Number"));
        } else if (boundwithForm.getSearchParams().getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase(OLEConstants.ITEM_DOC_TYPE)) {
            options.add(new ConcreteKeyValue("all", "ALL"));
            options.add(new ConcreteKeyValue("Location_search", "Location"));
            options.add(new ConcreteKeyValue("CallNumber_search", "Call Number"));
            options.add(new ConcreteKeyValue("ItemBarcode_search", "Barcode"));
        }
        return options;
    }
}
