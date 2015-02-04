package org.kuali.ole.deliver.lookup;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 11/13/13
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCirculationDeskMappingLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCirculationDeskMappingLookupableImpl.class);
    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {

        if (searchCriteria != null && org.apache.commons.lang.StringUtils.isNotEmpty(searchCriteria.get(
                KIMPropertyConstants.Person.PRINCIPAL_NAME))) {
            searchCriteria.put(KIMPropertyConstants.Person.PRINCIPAL_NAME, searchCriteria.get(
                    KIMPropertyConstants.Person.PRINCIPAL_NAME).toLowerCase());
        }
        bounded = false;
        List<Person> persons = getPersonService().findPeople(searchCriteria, bounded);
        sortSearchResults(form, persons);

        return persons;
    }

    public PersonService getPersonService() {
        return KimApiServiceLocator.getPersonService();
    }

}
