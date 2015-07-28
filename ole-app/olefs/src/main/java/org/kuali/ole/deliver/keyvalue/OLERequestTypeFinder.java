package org.kuali.ole.deliver.keyvalue;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestType;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 7/15/13
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERequestTypeFinder extends KeyValuesBase {

    private LoanProcessor loanProcessor = new LoanProcessor();

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> requestKeyLabels = new ArrayList<KeyValue>();
        Collection<OleDeliverRequestType> oleDeliverRequestTypeCollection = KRADServiceLocator.getBusinessObjectService().findAll(OleDeliverRequestType.class);
        String requestTypeCode = validateDefaultRequestType();
        if (StringUtils.isNotBlank(requestTypeCode)) {
            requestKeyLabels.add(new ConcreteKeyValue(requestTypeCode, requestTypeCode));
            for (OleDeliverRequestType requestType : oleDeliverRequestTypeCollection) {
                if (!requestTypeCode.equalsIgnoreCase(requestType.getRequestTypeCode()) && requestType.isActive()) {
                    requestKeyLabels.add(new ConcreteKeyValue(requestType.getRequestTypeCode(), requestType.getRequestTypeCode()));
                }
            }
        } else {
            requestKeyLabels.add(new ConcreteKeyValue("", ""));
            for (OleDeliverRequestType requestType : oleDeliverRequestTypeCollection) {
                if (requestType.isActive()) {
                    requestKeyLabels.add(new ConcreteKeyValue(requestType.getRequestTypeCode(), requestType.getRequestTypeCode()));
                }
            }
        }
        return requestKeyLabels;
    }

    private String validateDefaultRequestType() {
        String requestTypeCode = null;
        String parameterValue = loanProcessor.getParameter(OLEConstants.DEFAULT_REQUEST_TYPE);
        if (StringUtils.isNotBlank(parameterValue)) {
            Map<String, String> requestTypeMap = new HashMap<>();
            requestTypeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, parameterValue);
            Collection<OleDeliverRequestType> oleDeliverRequestTypeCollection = KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
            if (CollectionUtils.isNotEmpty(oleDeliverRequestTypeCollection)) {
                for (OleDeliverRequestType oleDeliverRequestType : oleDeliverRequestTypeCollection) {
                    if (oleDeliverRequestType.isActive()) {
                        requestTypeCode = parameterValue;
                    }
                }
            }
        }
        return requestTypeCode;
    }

}

