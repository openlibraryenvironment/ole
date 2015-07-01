package org.kuali.ole.deliver.lookup;

import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.LookupForm;

import javax.jws.WebParam;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 11/22/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleEmploymentStatusLookupableImpl extends LookupableImpl{
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleEmploymentStatusLookupableImpl.class);

    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        Collection<?> searchResults = null;

        try {
            Class entityEmploymentStatusBo = Class.forName("org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo") ;
            Map<String, String> nonBlankSearchCriteria = processSearchCriteria(form, searchCriteria);
            if (nonBlankSearchCriteria == null) {
                return new ArrayList<Object>();
            }
            try {
                Integer searchResultsLimit = null;

                if (!unbounded) {
                    searchResultsLimit = LookupUtils.getSearchResultsLimit(entityEmploymentStatusBo, form);
                }

                if (LookupUtils.hasExternalBusinessObjectProperty(entityEmploymentStatusBo, nonBlankSearchCriteria)) {
                    Map<String, String> eboSearchCriteria = adjustCriteriaForNestedEBOs(nonBlankSearchCriteria, unbounded);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Passing these results into the lookup service: " + eboSearchCriteria);
                    }
                    searchResults = getLookupService().findCollectionBySearchHelper(entityEmploymentStatusBo, eboSearchCriteria,
                            unbounded, searchResultsLimit);
                    generateLookupResultsMessages(form, eboSearchCriteria, searchResults, unbounded);
                } else {
                    searchResults = getLookupService().findCollectionBySearchHelper(entityEmploymentStatusBo,
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