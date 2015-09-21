package org.kuali.ole.docstore.engine.service.storage.rdbms;

import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;

/**
 * Created by pvsubrah on 7/17/15.
 */
public class ParameterValueResolver {
    private static ParameterValueResolver parameterValueResolver;

    private ParameterValueResolver(){

    }

    public static ParameterValueResolver getInstance(){
        if(null == parameterValueResolver){
            parameterValueResolver = new ParameterValueResolver();
        }
        return parameterValueResolver;
    }

    public String getParameter(String applicationId, String namespace, String componentId, String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(applicationId, namespace, componentId,parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);

        return parameter!=null?parameter.getValue():null;
    }

    public Boolean getParameterAsBoolean(String applicationId, String namespace, String componentId, String
            parameterName) {
        ParameterKey parameterKey = ParameterKey.create(applicationId, namespace, componentId,parameterName);
        Boolean parameterValueAsBoolean = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameterValueAsBoolean(parameterKey);

        return parameterValueAsBoolean == null ? false : parameterValueAsBoolean;
    }
}
