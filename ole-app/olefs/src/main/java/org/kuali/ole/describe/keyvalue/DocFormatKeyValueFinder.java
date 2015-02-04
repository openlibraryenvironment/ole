package org.kuali.ole.describe.keyvalue;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.describe.bo.OleCountryCodes;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.ole.describe.form.DocumentConfigForm;
import org.kuali.ole.describe.form.ImportBibForm;
import org.kuali.ole.describe.form.WorkbenchForm;
import org.kuali.ole.describe.service.DocstoreHelperService;
import org.kuali.ole.docstore.common.document.config.DocFormatConfig;
import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.config.*;
import org.kuali.ole.integration.cg.dto.HashMapElement;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.PaymentTermType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KeyValuesService;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 7/3/14
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocFormatKeyValueFinder extends UifKeyValuesFinderBase {

    public static final Logger LOG = LoggerFactory.getLogger(DocFormatKeyValueFinder.class);

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        DocumentConfigForm documentConfigForm= (DocumentConfigForm) model;
        String docFormatType= documentConfigForm.getDocFieldDocType();
            List<KeyValue> options = new ArrayList<KeyValue>();
        if(docFormatType!=null){
        HashMap hashMap =new HashMap();
        hashMap.put("name",docFormatType);
        DocTypeConfig docTypeConfig = (DocTypeConfig) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(DocTypeConfig.class,hashMap);
        if(docTypeConfig!=null ){//&& docTypeConfig.getId()!=null
            Integer docTypeId= docTypeConfig.getId();
            HashMap hashMapDocFormat =new HashMap();
            hashMapDocFormat.put("docTypeId",docTypeId);

        Collection<DocFormatConfig> docFormatConfigs = KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(DocFormatConfig.class,hashMapDocFormat,"name",true);

        options.add(new ConcreteKeyValue("", ""));

        for (DocFormatConfig type : docFormatConfigs) {
            options.add(new ConcreteKeyValue(type.getName(), type.getLabel()));
        }
        }

    }
        return options;
    }


}
