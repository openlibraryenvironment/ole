package org.kuali.ole.deliver.lookup;

import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 11/22/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleEntityEmploymentTypeImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleEntityEmploymentTypeImpl.class);

    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        Collection<?> searchResults = null;

        try {
            Class entityEmploymentTypeBo= Class.forName("org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo") ;
            Map<String, String> nonBlankSearchCriteria = processSearchCriteria(form, searchCriteria);
            if (nonBlankSearchCriteria == null) {
                return new ArrayList<Object>();
            }
            try {
                Integer searchResultsLimit = null;

                if (!unbounded) {
                    searchResultsLimit = LookupUtils.getSearchResultsLimit(entityEmploymentTypeBo, form);
                }

                if (LookupUtils.hasExternalBusinessObjectProperty(entityEmploymentTypeBo, nonBlankSearchCriteria)) {
                    Map<String, String> eboSearchCriteria = adjustCriteriaForNestedEBOs(nonBlankSearchCriteria, unbounded);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Passing these results into the lookup service: " + eboSearchCriteria);
                    }

                    // add those results as criteria run the normal search (but with the EBO criteria added)
                    searchResults = getLookupService().findCollectionBySearchHelper(entityEmploymentTypeBo, eboSearchCriteria,
                            unbounded, searchResultsLimit);
                    generateLookupResultsMessages(form, eboSearchCriteria, searchResults, unbounded);
                } else {
                    searchResults = getLookupService().findCollectionBySearchHelper(entityEmploymentTypeBo,
                            nonBlankSearchCriteria, unbounded, searchResultsLimit);
                    generateLookupResultsMessages(form, nonBlankSearchCriteria, searchResults, unbounded);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error trying to perform search", e);
            } catch (InstantiationException e1) {
                throw new RuntimeException("Error trying to perform search", e1);
            }
            if(GlobalVariables.getMessageMap().getInfoMessages().size()!=0){

                GlobalVariables.getMessageMap().getInfoMessages().clear();

            }
            if (searchResults == null) {
                searchResults = new ArrayList<Object>();
            } else {
                sortSearchResults(form, (List<?>) searchResults);
            }



        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }        // removed blank search values and decrypt any encrypted search values
        return (List<?>) searchResults;
    }

}
