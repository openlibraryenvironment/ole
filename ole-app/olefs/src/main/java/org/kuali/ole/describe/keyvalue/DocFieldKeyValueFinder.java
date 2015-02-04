package org.kuali.ole.describe.keyvalue;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.ole.describe.form.GlobalEditForm;
import org.kuali.ole.describe.form.ImportBibForm;
import org.kuali.ole.describe.form.WorkbenchForm;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.config.*;
import org.kuali.ole.describe.service.DocstoreHelperService;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PP7788
 * Date: 11/29/12
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocFieldKeyValueFinder
        extends UifKeyValuesFinderBase {

    public static final Logger LOG = LoggerFactory.getLogger(DocFieldKeyValueFinder.class);

    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        List<KeyValue> options = new ArrayList<KeyValue>();
        SearchParams searchParams = null;
        if (viewModel instanceof WorkbenchForm) {
            WorkbenchForm workbenchForm = (WorkbenchForm) viewModel;
            searchParams = workbenchForm.getSearchParams();
            getOptions(workbenchForm.getDocType(), options);
        } else if (viewModel instanceof BoundwithForm) {
            BoundwithForm boundwithForm = (BoundwithForm) viewModel;
            searchParams = boundwithForm.getSearchParams();
            getOptions(boundwithForm.getDocType(), options);
        } else if (viewModel instanceof ImportBibForm) {
            ImportBibForm importBibForm = (ImportBibForm) viewModel;
            getOptionsImportBib(importBibForm, options);
        } else if (viewModel instanceof GlobalEditForm) {
            GlobalEditForm globalEditForm = (GlobalEditForm) viewModel;
            /*if((globalEditForm.getSearchType() != null &&!globalEditForm.getSearchType().equalsIgnoreCase("import")) &&
                    (globalEditForm.getGlobalEditRecords().size() > 0 && !globalEditForm.getDocType().equals(globalEditForm.getGlobalEditRecords().get(0).getDocType()))) {
                globalEditForm.getGlobalEditRecords().clear();
                globalEditForm.getGlobalEditMap().clear();
            }*/
            getOptions(globalEditForm.getDocType(), options);
        }
        return options;
    }

    private void getOptionsImportBib(ImportBibForm importBibForm, List<KeyValue> options) {
        //        if (importBibForm.getSearchParams().getDocType().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
        options.add(new ConcreteKeyValue("Title", "Title"));
        options.add(new ConcreteKeyValue("OCLCControlNumber", "OCLC Control Number"));
        options.add(new ConcreteKeyValue("Author", "Author"));
        options.add(new ConcreteKeyValue("ISBN", "ISBN"));
        options.add(new ConcreteKeyValue("ISSN", "ISSN"));
        options.add(new ConcreteKeyValue("LCCN", "LCCN"));
    }

    private void getOptions(String docType, List<KeyValue> options) {
        DocstoreHelperService docstoreHelperService = new DocstoreHelperService();
        DocumentConfig documentConfig = docstoreHelperService.getDocumentConfigObj();
        try {
            for (DocumentCategory documentCategory : documentConfig.getDocumentCategories()) {
                if (DocCategory.WORK.getDescription().equalsIgnoreCase(documentCategory.getName())) {
                    for (DocumentType documentType : documentCategory.getDocumentTypes()) {
                        if (StringUtils.isNotEmpty(docType) && docType.equalsIgnoreCase(documentType.getId())) {
                            if (DocType.BIB.getDescription().equalsIgnoreCase(documentType.getId())) {
                                addFieldsToOptions(documentType, options, "all");
                            } else {
                                addFieldsToOptions(documentType, options, DocFormat.OLEML.getDescription());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void addFieldsToOptions(DocumentType documentType, List<KeyValue> options, String docFormat) {
        options.add(new ConcreteKeyValue("all", "ALL"));
        for (DocumentFormat documentFormat : documentType.getDocumentFormats()) {
            if (docFormat.equalsIgnoreCase(documentFormat.getId())) {
                for (Field field : documentFormat.getFields()) {
                    if ((field.getId()).endsWith("_search")) {
                        options.add(new ConcreteKeyValue(field.getId(), field.getName()));
                    }
                }
            }
        }
    }


}
