package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.PersonServiceImpl;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleAssigneeKeyValues is the value finder class for the Owner/Assignee in the License Request Document
 */
public class OleAssigneeKeyValues extends KeyValuesBase {
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
        keyValues.add(new ConcreteKeyValue("", ""));
        Map<String, String> roleSearchCriteria = new HashMap<String, String>();
        roleSearchCriteria.put("lookupRoleNamespaceCode", OLEConstants.OleLicenseRequest.LICENSE_NMSPACE);
        roleSearchCriteria.put("lookupRoleName", OLEConstants.OleLicenseRequest.LICENSE_ASSIGNEE_ROLE);
        PersonServiceImpl personServiceImpl = (PersonServiceImpl) KIMServiceLocatorInternal.getService(KimApiServiceLocator.KIM_PERSON_SERVICE);
        List<Person> persons = personServiceImpl.findPeople(roleSearchCriteria);
        for (Person person : persons) {
            keyValues.add(new ConcreteKeyValue(person.getPrincipalId(), person.getPrincipalName()));
        }
        return keyValues;
    }

}

