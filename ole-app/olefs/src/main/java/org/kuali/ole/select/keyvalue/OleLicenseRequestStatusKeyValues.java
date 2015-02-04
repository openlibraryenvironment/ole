package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OleLicenseRequestStatus;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OleLicenseRequestStatusKeyValues is the value finder class for the LicenseRequestStatus in the License Request Document
 */
public class OleLicenseRequestStatusKeyValues extends KeyValuesBase {
    private boolean blankOption;

    /**
     * Gets the blankOption attribute.
     *
     * @return Returns the blankOption
     */
    public boolean isBlankOption() {
        return this.blankOption;
    }

    /**
     * Sets the blankOption attribute value.
     *
     * @param blankOption The blankOption to set.
     */
    public void setBlankOption(boolean blankOption) {
        this.blankOption = blankOption;
    }

    /**
     * Gets the keyValues attribute.
     *
     * @return Returns the keyValues
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleLicenseRequestStatus> licenseRequestStatus = KRADServiceLocator.getBusinessObjectService().findAll(OleLicenseRequestStatus.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OleLicenseRequestStatus licenseRequestStatusObj : licenseRequestStatus) {
            keyValues.add(new ConcreteKeyValue(licenseRequestStatusObj.getName(), licenseRequestStatusObj.getName()));
        }
        return keyValues;
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
}

