package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srirams on 12/9/15.
 */
public class HoldingsNoteKeyValueFinder extends UifKeyValuesFinderBase {

    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        List<KeyValue> options = new ArrayList<KeyValue>();
        String note = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                .DESCRIBE_COMPONENT, OLEConstants.HOLDINGS_DEFAULT_NOTE);

        if(Boolean.valueOf(note)){
            options.add(new ConcreteKeyValue("nonPublic", "Non-Public"));
            options.add(new ConcreteKeyValue("public", "Public"));
        }else{
            options.add(new ConcreteKeyValue("public", "Public"));
            options.add(new ConcreteKeyValue("nonPublic", "Non-Public"));
        }
        setAddBlankOption(false);
        return options;
    }

    public String getParameter(String applicationId, String namespace, String componentId, String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(applicationId, namespace, componentId, parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);

        return parameter!=null?parameter.getValue():null;
    }
}
