package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by maheswarang on 5/19/15.
 */
public class OLEEResourceWorkflowKeyValue extends KeyValuesBase {
        public List getKeyValues() {
            List<KeyValue> keyValues = new ArrayList<KeyValue>();
            Collection<OLEAccessActivationConfiguration> oleAccessActivationConfigurations = KRADServiceLocator.getBusinessObjectService().findAll(OLEAccessActivationConfiguration.class);
            keyValues.add(new ConcreteKeyValue("",""));
            for (OLEAccessActivationConfiguration oleAccessActivationConfiguration : oleAccessActivationConfigurations) {
                if (oleAccessActivationConfiguration.isActive() && !oleAccessActivationConfiguration.getWorkflowType().equals("accessActivation")) {
                    keyValues.add(new ConcreteKeyValue(oleAccessActivationConfiguration.getAccessActivationConfigurationId(), oleAccessActivationConfiguration.getWorkflowName()));
                }
            }
            return keyValues;
        }

}

