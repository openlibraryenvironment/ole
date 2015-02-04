package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.select.service.impl.OleExposedWebServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * OlePaymentMethodKeyValue returns PaymentMethodId and PaymentMethodName
 */
public class OlePaymentMethodKeyValue extends KeyValuesBase {
    /**
     * This method will retrieve Payment method maintenance document from rice 1
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        OleExposedWebServiceImpl oleExposedWebService = (OleExposedWebServiceImpl) SpringContext.getBean("oleExposedWebService");

        String paymentMethod = oleExposedWebService.getPaymentMethod();
        String methods = paymentMethod.substring(0, paymentMethod.length() - 1);
        String[] method = methods.split(",");
        keyValues.add(new ConcreteKeyValue("", ""));
        for (int i = 0; i < method.length; i++) {
            keyValues.add(new ConcreteKeyValue(method[i], method[i]));
        }
        return keyValues;
    }

    /**
     * Gets the webservice URL using PropertyUtil.
     *
     * @return String
     */
    public String getURL() {
        String url = ConfigContext.getCurrentContextConfig().getProperty("oleExposedWebService.url");
        return url;
    }
}
