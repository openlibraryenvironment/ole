package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.form.OLESearchForm;
import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jayabharathreddy on 2/11/15.
 */
public class SearchDocTypeKeyvalueFinder extends UifKeyValuesFinderBase {

    DocumentSearchConfig documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();

    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        OLESearchForm searchForm = (OLESearchForm) viewModel;
        super.setAddBlankOption(false);
        List<KeyValue> options = new ArrayList<>();
        String docType = null;
        docType = searchForm.getDocType();


        Collection<DocTypeConfig> docTypeConfigs = documentSearchConfig.getDocTypeConfigs();
        if (docType.equalsIgnoreCase(OLEConstants.HOLDING_DOC_TYPE) || docType.equalsIgnoreCase(OLEConstants.ITEM_DOC_TYPE)) {
            for (DocTypeConfig type : docTypeConfigs) {
                if (type.getName().equalsIgnoreCase(OLEConstants.HOLDING_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
                if (type.getName().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
                if (type.getName().equalsIgnoreCase(OLEConstants.ITEM_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
            }
        } else if (docType.equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            for (DocTypeConfig type : docTypeConfigs) {
                if (type.getName().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
                if (type.getName().equalsIgnoreCase(OLEConstants.HOLDING_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
                if (type.getName().equalsIgnoreCase(OLEConstants.E_HOLDINGS_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
                if (type.getName().equalsIgnoreCase(OLEConstants.ITEM_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
            }
        } else if (docType.equalsIgnoreCase(OLEConstants.E_HOLDINGS_DOC_TYPE)) {
            for (DocTypeConfig type : docTypeConfigs) {
                if (type.getName().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
                if (type.getName().equalsIgnoreCase(OLEConstants.E_HOLDINGS_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
            }
        } else if (docType.equalsIgnoreCase(OLEConstants.LICENSE_DOC_TYPE)) {
            for (DocTypeConfig type : docTypeConfigs) {
                if (type.getName().equalsIgnoreCase(OLEConstants.LICENSE_DOC_TYPE)) {
                    options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
                }
            }
        }
        return options;
    }
}
