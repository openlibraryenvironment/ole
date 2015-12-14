package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.select.bo.OLEProblemType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLEProblemTypeKeyValues extends KeyValuesBase {

    private ParameterValueResolver parameterValueResolver ;

    public ParameterValueResolver getParameterValueResolver(){
        if(parameterValueResolver == null){
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValuesDefault = new ArrayList<KeyValue>();
        List<KeyValue> keyValuesNonDefault=new ArrayList<KeyValue>();
        String defaultProblemType = getParameterValueResolver().getParameter("OLE","OLE-SELECT","Select","DEFAULT_PROBLEM_TYPE");
        Collection<OLEProblemType> oleProblemTypes = KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OLEProblemType.class, OLEConstants.OLEProblemType.PRBLM_TYPE_NAME,true);
        for (OLEProblemType oleProblemType : oleProblemTypes) {
            if (oleProblemType.isActive()) {
                if(defaultProblemType!=null && defaultProblemType.equals(oleProblemType.getProblemTypeName()))
                    keyValuesDefault.add(new ConcreteKeyValue(oleProblemType.getProblemTypeId(), oleProblemType.getProblemTypeName()));
                else
                    keyValuesNonDefault.add(new ConcreteKeyValue(oleProblemType.getProblemTypeId(), oleProblemType.getProblemTypeName()));
            }
        }
        keyValuesDefault.addAll(keyValuesNonDefault);
        return keyValuesDefault;
    }
}
