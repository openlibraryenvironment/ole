package org.kuali.ole.deliver.lookup;

import org.eclipse.jdt.internal.eval.GlobalVariable;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType;
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenship;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.entity.EntityDefaultQueryResults;
import org.kuali.rice.kim.api.identity.entity.EntityQueryResults;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierType;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographics;
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalQueryResults;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.api.identity.residency.EntityResidency;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.identity.visa.EntityVisa;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
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