/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kim.impl.identity;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.criteria.CountFlag;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.entity.EntityDefaultQueryResults;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierType;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoDefault;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kns.service.BusinessObjectMetaDataService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.lookup.CollectionIncomplete;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersonServiceImpl implements PersonService {

	private static Logger LOG = Logger.getLogger( PersonServiceImpl.class );
	protected static final String ENTITY_EXT_ID_PROPERTY_PREFIX = "externalIdentifiers.";
	protected static final String ENTITY_AFFILIATION_PROPERTY_PREFIX = "affiliations.";
	protected static final String ENTITY_TYPE_PROPERTY_PREFIX = "entityTypeContactInfos.";
	protected static final String ENTITY_EMAIL_PROPERTY_PREFIX = "entityTypeContactInfos.emailAddresses.";
	protected static final String ENTITY_PHONE_PROPERTY_PREFIX = "entityTypeContactInfos.phoneNumbers.";
	protected static final String ENTITY_ADDRESS_PROPERTY_PREFIX = "entityTypeContactInfos.addresses.";
	protected static final String ENTITY_NAME_PROPERTY_PREFIX = "names.";
	protected static final String PRINCIPAL_PROPERTY_PREFIX = "principals.";
	protected static final String ENTITY_EMPLOYEE_ID_PROPERTY_PREFIX = "employmentInformation.";
	// KULRICE-4442 Special handling for extension objects
	protected static final String EXTENSION = "extension";
	
	private IdentityService identityService;
	private RoleService roleService;
	private BusinessObjectMetaDataService businessObjectMetaDataService;
	private MaintenanceDocumentDictionaryService maintenanceDocumentDictionaryService;

	protected List<String> personEntityTypeCodes = new ArrayList<String>( 4 );
	// String that can be passed to the lookup framework to create an type = X OR type = Y criteria
	private String personEntityTypeLookupCriteria = null;
    
	protected Map<String,String> baseLookupCriteria = new HashMap<String,String>();
	protected Map<String,String> criteriaConversion = new HashMap<String,String>();
	protected ArrayList<String> personCachePropertyNames = new ArrayList<String>();
	{
		// init the criteria which will need to be applied to every lookup against
		// the identity data tables
		baseLookupCriteria.put( KIMPropertyConstants.Person.ACTIVE, "Y" );
		baseLookupCriteria.put( ENTITY_TYPE_PROPERTY_PREFIX + KRADPropertyConstants.ACTIVE, "Y" );
		
		// create the field mappings between the Person object and the KimEntity object
		criteriaConversion.put( KIMPropertyConstants.Person.ENTITY_ID, KIMPropertyConstants.Entity.ID);
		criteriaConversion.put( KIMPropertyConstants.Person.ACTIVE, PRINCIPAL_PROPERTY_PREFIX + KRADPropertyConstants.ACTIVE );
		criteriaConversion.put( KIMPropertyConstants.Person.PRINCIPAL_ID, PRINCIPAL_PROPERTY_PREFIX + KIMPropertyConstants.Person.PRINCIPAL_ID );
		criteriaConversion.put( KIMPropertyConstants.Person.PRINCIPAL_NAME, PRINCIPAL_PROPERTY_PREFIX + KIMPropertyConstants.Person.PRINCIPAL_NAME );
		criteriaConversion.put( KIMPropertyConstants.Person.FIRST_NAME, "names.firstName" );
		criteriaConversion.put( KIMPropertyConstants.Person.LAST_NAME, "names.lastName" );
		criteriaConversion.put( KIMPropertyConstants.Person.MIDDLE_NAME, "names.middleName" );
		criteriaConversion.put( KIMPropertyConstants.Person.EMAIL_ADDRESS, "entityTypeContactInfos.emailAddresses.emailAddress" );
		criteriaConversion.put( KIMPropertyConstants.Person.PHONE_NUMBER, "entityTypeContactInfos.phoneNumbers.phoneNumber" );
		criteriaConversion.put( KIMPropertyConstants.Person.ADDRESS_LINE_1, "entityTypeContactInfos.addresses.line1" );
		criteriaConversion.put( KIMPropertyConstants.Person.ADDRESS_LINE_2, "entityTypeContactInfos.addresses.line2" );
		criteriaConversion.put( KIMPropertyConstants.Person.ADDRESS_LINE_3, "entityTypeContactInfos.addresses.line3" );
		criteriaConversion.put( KIMPropertyConstants.Person.CITY, "entityTypeContactInfos.addresses.city" );
		criteriaConversion.put( KIMPropertyConstants.Person.STATE_CODE, "entityTypeContactInfos.addresses.stateProvinceCode" );
		criteriaConversion.put( KIMPropertyConstants.Person.POSTAL_CODE, "entityTypeContactInfos.addresses.postalCode" );
		criteriaConversion.put( KIMPropertyConstants.Person.COUNTRY_CODE, "entityTypeContactInfos.addresses.countryCode" );
		criteriaConversion.put( KIMPropertyConstants.Person.CAMPUS_CODE, "affiliations.campusCode" );
		criteriaConversion.put( KIMPropertyConstants.Person.AFFILIATION_TYPE_CODE, "affiliations.affiliationTypeCode" );
		criteriaConversion.put( KIMPropertyConstants.Person.EXTERNAL_IDENTIFIER_TYPE_CODE, "externalIdentifiers.externalIdentifierTypeCode" );
		criteriaConversion.put( KIMPropertyConstants.Person.EXTERNAL_ID, "externalIdentifiers.externalId" );		
		criteriaConversion.put( KIMPropertyConstants.Person.EMPLOYEE_TYPE_CODE, "employmentInformation.employeeTypeCode" );
		criteriaConversion.put( KIMPropertyConstants.Person.EMPLOYEE_STATUS_CODE, "employmentInformation.employeeStatusCode" );
		criteriaConversion.put( KIMPropertyConstants.Person.EMPLOYEE_ID, "employmentInformation.employeeId" );
		criteriaConversion.put( KIMPropertyConstants.Person.BASE_SALARY_AMOUNT, "employmentInformation.baseSalaryAmount" );
		criteriaConversion.put( KIMPropertyConstants.Person.PRIMARY_DEPARTMENT_CODE, "employmentInformation.primaryDepartmentCode" );

		personCachePropertyNames.add( KIMPropertyConstants.Person.PRINCIPAL_ID );
		personCachePropertyNames.add( KIMPropertyConstants.Person.PRINCIPAL_NAME );
		personCachePropertyNames.add( KIMPropertyConstants.Person.ENTITY_ID );
		personCachePropertyNames.add( KIMPropertyConstants.Person.FIRST_NAME );
		personCachePropertyNames.add( KIMPropertyConstants.Person.LAST_NAME );
		personCachePropertyNames.add( KIMPropertyConstants.Person.MIDDLE_NAME );
		personCachePropertyNames.add( KIMPropertyConstants.Person.CAMPUS_CODE );
		personCachePropertyNames.add( KIMPropertyConstants.Person.EMPLOYEE_ID );
		personCachePropertyNames.add( KIMPropertyConstants.Person.PRIMARY_DEPARTMENT_CODE );
	}

	
	/**
	 * @see org.kuali.rice.kim.api.identity.PersonService#getPerson(java.lang.String)
	 */
	public Person getPerson(String principalId) {
		if ( StringUtils.isBlank(principalId) ) {
			return null;
		}

		// get the corresponding principal
		final Principal principal = getIdentityService().getPrincipal( principalId );
		// get the identity
		if ( principal != null ) {
			final EntityDefault entity = getIdentityService().getEntityDefault(principal.getEntityId());
         	// convert the principal and identity to a Person
            // skip if the person was created from the DB cache
            if (entity != null ) {
                return convertEntityToPerson( entity, principal );
            }
		}
		return null;
	}

	protected PersonImpl convertEntityToPerson( EntityDefault entity, Principal principal ) {
		try {
			// get the EntityEntityType for the EntityType corresponding to a Person
			for ( String entityTypeCode : personEntityTypeCodes ) {
				EntityTypeContactInfoDefault entType = entity.getEntityType( entityTypeCode );
				// if no "person" identity type present for the given principal, skip to the next type in the list
				if ( entType == null ) {
					continue;
				}
				// attach the principal and identity objects
				// PersonImpl has logic to pull the needed elements from the KimEntity-related classes
				return new PersonImpl( principal, entity, entityTypeCode );
			}
			return null;
		} catch ( Exception ex ) {
			// allow runtime exceptions to pass through
			if ( ex instanceof RuntimeException ) {
				throw (RuntimeException)ex;
			}
			throw new RuntimeException( "Problem building person object", ex );
		}
	}
	
	
	/**
	 * @see org.kuali.rice.kim.api.identity.PersonService#getPersonByPrincipalName(java.lang.String)
	 */
	public Person getPersonByPrincipalName(String principalName) {
		if ( StringUtils.isBlank(principalName) ) {
			return null;
		}

		// get the corresponding principal
		final Principal principal = getIdentityService().getPrincipalByPrincipalName( principalName );
		// get the identity
		if ( principal != null ) {
            final EntityDefault entity = getIdentityService().getEntityDefault(principal.getEntityId());

            // convert the principal and identity to a Person
            if ( entity != null ) {
                return convertEntityToPerson( entity, principal );
            }
		}
		return null;
	}

	public Person getPersonByEmployeeId(String employeeId) {
		if ( StringUtils.isBlank( employeeId  ) ) {
			return null;
		}

		final List<Person> people = findPeople( Collections.singletonMap(KIMPropertyConstants.Person.EMPLOYEE_ID, employeeId) );
		if ( !people.isEmpty() ) {
			return people.get(0);

		}
		
	    // If no person was found above, check for inactive records
        EntityDefault entity = getIdentityService().getEntityDefaultByEmployeeId(employeeId);
        if (entity != null) {
            if ( !entity.getPrincipals().isEmpty() ) {
                Principal principal = getIdentityService().getPrincipal(entity.getPrincipals().get(0).getPrincipalId());
                if (principal != null) {
                    return convertEntityToPerson( entity, principal );
                }  
            }
        }

		return null;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.PersonService#findPeople(Map)
	 */
	public List<Person> findPeople(Map<String, String> criteria) {
		return findPeople(criteria, true);
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.PersonService#findPeople(java.util.Map, boolean)
	 */
	public List<Person> findPeople(Map<String, String> criteria, boolean unbounded) {
		List<Person> people = null;
		// protect from NPEs
		if ( criteria == null ) {
			criteria = Collections.emptyMap();
		}
		// make a copy so it can be modified safely in this method
		criteria = new HashMap<String, String>( criteria );
		
		// extract the role lookup parameters and then remove them since later code will not know what to do with them
		String roleName = criteria.get( "lookupRoleName" );
		String namespaceCode = criteria.get( "lookupRoleNamespaceCode" );
		criteria.remove("lookupRoleName");
		criteria.remove("lookupRoleNamespaceCode");
		if ( StringUtils.isNotBlank(namespaceCode) && StringUtils.isNotBlank(roleName) ) {
			Integer searchResultsLimit = org.kuali.rice.kns.lookup.LookupUtils.getSearchResultsLimit(PersonImpl.class);
			int searchResultsLimitInt = Integer.MAX_VALUE;
			if (searchResultsLimit != null) {
				searchResultsLimitInt = searchResultsLimit.intValue();
			}
			if ( LOG.isDebugEnabled() ) {
				LOG.debug("Performing Person search including role filter: " + namespaceCode + "/" + roleName );
			}
			if ( criteria.size() == 1 && criteria.containsKey(KIMPropertyConstants.Person.ACTIVE) ) { // if only active is specified
				if ( LOG.isDebugEnabled() ) {
					LOG.debug( "Only active criteria specified, running role search first" );
				}
				// in this case, run the role lookup first and pass those results to the person lookup
				Collection<String> principalIds = getRoleService().getRoleMemberPrincipalIds(namespaceCode, roleName,  Collections.<String, String>emptyMap());
				StringBuffer sb = new StringBuffer(principalIds.size()*15);
				Iterator<String> pi = principalIds.iterator();
				while ( pi.hasNext() ) {
					sb.append( pi.next() );
					if ( pi.hasNext() ) sb.append( '|' );
				}
				// add the list of principal IDs to the lookup so that only matching Person objects are returned
				criteria.put( KIMPropertyConstants.Person.PRINCIPAL_ID, sb.toString() );
				people = findPeopleInternal(criteria, false); // can allow internal method to filter here since no more filtering necessary				
			} else if ( !criteria.isEmpty() ) { // i.e., person criteria are specified
				if ( LOG.isDebugEnabled() ) {
					LOG.debug( "Person criteria also specified, running that search first" );
				}
				// run the person lookup first
				people = findPeopleInternal(criteria, true); // get all, since may need to be filtered
				// TODO - now check if these people have the given role
				// build a principal list
				List<String> principalIds = peopleToPrincipalIds( people );
				// get sublist of principals that have the given roles
				principalIds = getRoleService().getPrincipalIdSubListWithRole(principalIds, namespaceCode, roleName,  Collections.<String, String>emptyMap());
				// re-convert into people objects, wrapping in CollectionIncomplete if needed
				if ( !unbounded && principalIds.size() > searchResultsLimitInt ) {
					int actualResultSize = principalIds.size();
					// trim the list down before converting to people
					principalIds = new ArrayList<String>(principalIds).subList(0, searchResultsLimitInt); // yes, this is a little wasteful
					people = getPeople(principalIds); // convert the results to people
					people = new CollectionIncomplete<Person>( people.subList(0, searchResultsLimitInt), new Long(actualResultSize) );
				} else {
					people = getPeople(principalIds);
				}
			} else { // only role criteria specified
				if ( LOG.isDebugEnabled() ) {
					LOG.debug( "No Person criteria specified - only using role service." );
				}
				// run the role criteria to get the principals with the role
				Collection<String> principalIds = getRoleService().getRoleMemberPrincipalIds(namespaceCode, roleName,  Collections.<String, String>emptyMap());
				if ( !unbounded && principalIds.size() > searchResultsLimitInt ) {
					int actualResultSize = principalIds.size();
					// trim the list down before converting to people
					principalIds = new ArrayList<String>(principalIds).subList(0, searchResultsLimitInt); // yes, this is a little wasteful
					people = getPeople(principalIds); // convert the results to people
					people = new CollectionIncomplete<Person>( people.subList(0, searchResultsLimitInt), new Long(actualResultSize) );
				} else {
					people = getPeople(principalIds); // convert the results to people
				}
			}
		} else {
			if ( LOG.isDebugEnabled() ) {
				LOG.debug( "No Role criteria specified, running person lookup as normal." );
			}
			people = findPeopleInternal(criteria, unbounded);
		}
			
		// The following change is for KULRICE-5694 - It prevents duplicate rows from being returned for the 
		// person inquiry (In this case, duplicate meaning same entityId, principalId, and principalNm).  
		// This allows for multiple rows to be returned if an entityID has more then one principal name
		// or more than one principal ID.  
        Set<String> peopleNoDupsSet = new HashSet<String>();
        List<Person> peopleNoDupsList = new ArrayList<Person>();

	    for (Iterator<Person> iter = people.iterator(); iter.hasNext(); ) {
	        Person person = iter.next();
	        if (peopleNoDupsSet.add(person.getEntityId() + person.getPrincipalId() + person.getPrincipalName())) {
	            peopleNoDupsList.add(person);
	        }
	    }
	     
	    people.clear();
	    people.addAll(peopleNoDupsList);
		
	    return people;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Person> findPeopleInternal(Map<String,String> criteria, boolean unbounded ) {
		// convert the criteria to a form that can be used by the ORM layer

        //TODO convert this to the new criteria predicates
		Map<String,String> entityCriteria = convertPersonPropertiesToEntityProperties( criteria );

        Predicate predicate = PredicateUtils.convertMapToPredicate(entityCriteria);

        QueryByCriteria.Builder queryBuilder = QueryByCriteria.Builder.create();
        queryBuilder.setPredicates(predicate);
        
		if (!unbounded) {
			Integer searchResultsLimit = org.kuali.rice.kns.lookup.LookupUtils.getSearchResultsLimit(PersonImpl.class);
    		if (searchResultsLimit != null && searchResultsLimit >= 0) {
    			queryBuilder.setMaxResults(searchResultsLimit);
    			queryBuilder.setCountFlag(CountFlag.INCLUDE);
			}
		}
		
		List<Person> people = new ArrayList<Person>();

		EntityDefaultQueryResults qr = getIdentityService().findEntityDefaults( queryBuilder.build() );

        if (qr.getResults().size() > 0) {

            for ( EntityDefault e : qr.getResults() ) {
			    // get to get all principals for the identity as well
			    for ( Principal p : e.getPrincipals() ) {
			    	people.add( convertEntityToPerson( e, p ) );
			    }
		    }
        } else if (!qr.isMoreResultsAvailable() && entityCriteria.containsKey("principals.principalId")) {
            if (!(entityCriteria.containsKey(KIMPropertyConstants.Person.ACTIVE)) || (criteria.get(KIMPropertyConstants.Person.ACTIVE).equals("N"))) {
                String principalId =  entityCriteria.get("principals.principalId");
                try {
                    EntityDefault entityDefault = getIdentityService().getEntityDefaultByPrincipalId(principalId);
                    for ( Principal p : entityDefault.getPrincipals() ) {
                        if (!p.isActive()){
                            people.add( convertEntityToPerson(entityDefault, p ) );
                        }
                    }
                } catch ( Exception e ) {
                    LOG.info( "A principal Id of " + principalId + " dose not exist in the system");
                }
            }
        } else if (!qr.isMoreResultsAvailable() &&  entityCriteria.containsKey("principals.principalName")) {
            if (!(entityCriteria.containsKey(KIMPropertyConstants.Person.ACTIVE)) || (criteria.get(KIMPropertyConstants.Person.ACTIVE).equals("N"))) {
                String principalNm =  entityCriteria.get("principals.principalName");
                try {
                    EntityDefault entityDefault = getIdentityService().getEntityDefaultByPrincipalName(principalNm);
                    for ( Principal p : entityDefault.getPrincipals() ) {
                        if (!p.isActive()){
                            people.add( convertEntityToPerson(entityDefault, p ) );
                        }
                    }
                } catch ( Exception e ) {
                    LOG.info( "A principal name of " + principalNm + " dose not exist in the system");
                }
            }
        }
        return people;
	}

	public Map<String,String> convertPersonPropertiesToEntityProperties( Map<String,String> criteria ) {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug( "convertPersonPropertiesToEntityProperties: " + criteria );
		}
		boolean nameCriteria = false;
		boolean addressCriteria = false;
		boolean externalIdentifierCriteria = false;
		boolean affiliationCriteria = false;
		boolean affiliationDefaultOnlyCriteria = false;
		boolean phoneCriteria = false;
		boolean emailCriteria = false;
		boolean employeeIdCriteria = false;
		// add base lookups for all person lookups
		HashMap<String,String> newCriteria = new HashMap<String,String>();
		newCriteria.putAll( baseLookupCriteria );

		newCriteria.put( "entityTypeContactInfos.entityTypeCode", personEntityTypeLookupCriteria );

        if ( criteria != null ) {
			for ( String key : criteria.keySet() ) {
			    //check active radio button
	            if(key.equals(KIMPropertyConstants.Person.ACTIVE)) {
	                newCriteria.put(criteriaConversion.get(KIMPropertyConstants.Person.ACTIVE), criteria.get(KIMPropertyConstants.Person.ACTIVE));
	            } else {
	                // The following if statement enables the "both" button to work correctly.
	                if (!(criteria.containsKey(KIMPropertyConstants.Person.ACTIVE))) {
	                    newCriteria.remove( KIMPropertyConstants.Person.ACTIVE );
	                }
	            }
	            
				// if no value was passed, skip the entry in the Map
				if ( StringUtils.isEmpty( criteria.get(key) ) ) {
					continue;
				}
				// check if the value needs to be encrypted
				// handle encrypted external identifiers
				if ( key.equals( KIMPropertyConstants.Person.EXTERNAL_ID ) && StringUtils.isNotBlank(criteria.get(key)) ) {
					// look for a ext ID type property
					if ( criteria.containsKey( KIMPropertyConstants.Person.EXTERNAL_IDENTIFIER_TYPE_CODE ) ) {
						String extIdTypeCode = criteria.get(KIMPropertyConstants.Person.EXTERNAL_IDENTIFIER_TYPE_CODE);
						if ( StringUtils.isNotBlank(extIdTypeCode) ) {
							// if found, load that external ID Type via service
							EntityExternalIdentifierType extIdType = getIdentityService().getExternalIdentifierType(extIdTypeCode);
							// if that type needs to be encrypted, encrypt the value in the criteria map
							if ( extIdType != null && extIdType.isEncryptionRequired() ) {
								try {
                                    if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
									    criteria.put(key,
										    	CoreApiServiceLocator.getEncryptionService().encrypt(criteria.get(key))
										    	);
                                    }
								} catch (GeneralSecurityException ex) {
									LOG.error("Unable to encrypt value for external ID search of type " + extIdTypeCode, ex );
								}								
							}
						}
					}
				}
				
				// convert the property to the Entity data model
				String entityProperty = criteriaConversion.get( key );
				if ( entityProperty != null ) {
					newCriteria.put( entityProperty, criteria.get( key ) );
				} else {
					entityProperty = key;
					// just pass it through if no translation present
					newCriteria.put( key, criteria.get( key ) );
				}
				// check if additional criteria are needed based on the types of properties specified
				if ( isNameEntityCriteria( entityProperty ) ) {
					nameCriteria = true;
				}
				if ( isExternalIdentifierEntityCriteria( entityProperty ) ) {
					externalIdentifierCriteria = true;
				}
				if ( isAffiliationEntityCriteria( entityProperty ) ) {
					affiliationCriteria = true;
				}
				if ( isAddressEntityCriteria( entityProperty ) ) {
					addressCriteria = true;
				}
				if ( isPhoneEntityCriteria( entityProperty ) ) {
					phoneCriteria = true;
				}
				if ( isEmailEntityCriteria( entityProperty ) ) {
					emailCriteria = true;
				}
				if ( isEmployeeIdEntityCriteria( entityProperty ) ) {
					employeeIdCriteria = true;
				}				
				// special handling for the campus code, since that forces the query to look
				// at the default affiliation record only
				if ( key.equals( "campusCode" ) ) {
					affiliationDefaultOnlyCriteria = true;
				}
			} 
			
			if ( nameCriteria ) {
				newCriteria.put( ENTITY_NAME_PROPERTY_PREFIX + "active", "Y" );
				newCriteria.put( ENTITY_NAME_PROPERTY_PREFIX + "defaultValue", "Y" );
				//newCriteria.put(ENTITY_NAME_PROPERTY_PREFIX + "nameCode", "PRFR");//so we only display 1 result
			}
			if ( addressCriteria ) {
				newCriteria.put( ENTITY_ADDRESS_PROPERTY_PREFIX + "active", "Y" );
				newCriteria.put( ENTITY_ADDRESS_PROPERTY_PREFIX + "defaultValue", "Y" );
			}
			if ( phoneCriteria ) {
				newCriteria.put( ENTITY_PHONE_PROPERTY_PREFIX + "active", "Y" );
				newCriteria.put( ENTITY_PHONE_PROPERTY_PREFIX + "defaultValue", "Y" );
			}
			if ( emailCriteria ) {
				newCriteria.put( ENTITY_EMAIL_PROPERTY_PREFIX + "active", "Y" );
				newCriteria.put( ENTITY_EMAIL_PROPERTY_PREFIX + "defaultValue", "Y" );
			}
			if ( employeeIdCriteria ) {
				newCriteria.put( ENTITY_EMPLOYEE_ID_PROPERTY_PREFIX + "active", "Y" );
				newCriteria.put( ENTITY_EMPLOYEE_ID_PROPERTY_PREFIX + "primary", "Y" );
			}
			if ( affiliationCriteria ) {
				newCriteria.put( ENTITY_AFFILIATION_PROPERTY_PREFIX + "active", "Y" );
			}
			if ( affiliationDefaultOnlyCriteria ) {
				newCriteria.put( ENTITY_AFFILIATION_PROPERTY_PREFIX + "defaultValue", "Y" );
			} 
        }   
		
		if ( LOG.isDebugEnabled() ) {
			LOG.debug( "Converted: " + newCriteria );
		}
		return newCriteria;		
	}

	protected boolean isNameEntityCriteria( String propertyName ) {
		return propertyName.startsWith( ENTITY_NAME_PROPERTY_PREFIX );
	}
	protected boolean isAddressEntityCriteria( String propertyName ) {
		return propertyName.startsWith( ENTITY_ADDRESS_PROPERTY_PREFIX );
	}
	protected boolean isPhoneEntityCriteria( String propertyName ) {
		return propertyName.startsWith( ENTITY_PHONE_PROPERTY_PREFIX );
	}
	protected boolean isEmailEntityCriteria( String propertyName ) {
		return propertyName.startsWith( ENTITY_EMAIL_PROPERTY_PREFIX );
	}
	protected boolean isEmployeeIdEntityCriteria( String propertyName ) {
		return propertyName.startsWith( ENTITY_EMPLOYEE_ID_PROPERTY_PREFIX );
	}
	protected boolean isAffiliationEntityCriteria( String propertyName ) {
		return propertyName.startsWith( ENTITY_AFFILIATION_PROPERTY_PREFIX );
	}
	protected boolean isExternalIdentifierEntityCriteria( String propertyName ) {
		return propertyName.startsWith( ENTITY_EXT_ID_PROPERTY_PREFIX );
	}
	
	/**
	 * Get the entityTypeCode that can be associated with a Person.  This will determine
	 * where EntityType-related data is pulled from within the KimEntity object.  The codes
	 * in the list will be examined in the order present.
	 */
	public List<String> getPersonEntityTypeCodes() {
		return this.personEntityTypeCodes;
	}

	public void setPersonEntityTypeCodes(List<String> personEntityTypeCodes) {
		this.personEntityTypeCodes = personEntityTypeCodes;
		personEntityTypeLookupCriteria = null;
		for ( String entityTypeCode : personEntityTypeCodes ) {
			if ( personEntityTypeLookupCriteria == null ) {
				personEntityTypeLookupCriteria = entityTypeCode;
			} else {
				personEntityTypeLookupCriteria = personEntityTypeLookupCriteria + "|" + entityTypeCode;
			}
		}
	}

	
	protected List<Person> getPeople( Collection<String> principalIds ) {
		List<Person> people = new ArrayList<Person>( principalIds.size() );
		for ( String principalId : principalIds ) {
			people.add( getPerson(principalId) );
		}
		return people;
	}
	
	protected List<String> peopleToPrincipalIds( List<Person> people ) {
		List<String> principalIds = new ArrayList<String>();
		
		for ( Person person : people ) {
			principalIds.add( person.getPrincipalId() );
		}
		
		return principalIds;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.PersonService#getPersonByExternalIdentifier(java.lang.String, java.lang.String)
	 */
	public List<Person> getPersonByExternalIdentifier(String externalIdentifierTypeCode, String externalId) {
		if (StringUtils.isBlank( externalIdentifierTypeCode ) || StringUtils.isBlank( externalId ) ) {
			return null;
		}
		Map<String,String> criteria = new HashMap<String,String>( 2 );
		criteria.put( KIMPropertyConstants.Person.EXTERNAL_IDENTIFIER_TYPE_CODE, externalIdentifierTypeCode );
		criteria.put( KIMPropertyConstants.Person.EXTERNAL_ID, externalId );
		return findPeople( criteria );
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.PersonService#updatePersonIfNecessary(java.lang.String, org.kuali.rice.kim.api.identity.Person)
	 */
    public Person updatePersonIfNecessary(String sourcePrincipalId, Person currentPerson ) {
        if (currentPerson  == null // no person set
                || !StringUtils.equals(sourcePrincipalId, currentPerson.getPrincipalId() ) // principal ID mismatch
                || currentPerson.getEntityId() == null ) { // syntheticially created Person object
            Person person = getPerson( sourcePrincipalId );
            // if a synthetically created person object is present, leave it - required for property derivation and the UI layer for
            // setting the principal name
            if ( person == null ) {
                if ( currentPerson != null && currentPerson.getEntityId() == null ) {
                    return currentPerson;
                }
            }
            // if both are null, create an empty object for property derivation
            if ( person == null && currentPerson == null ) {
            	try {
            		return new PersonImpl();
            	} catch ( Exception ex ) {
            		LOG.error( "unable to instantiate an object of type: " + getPersonImplementationClass() + " - returning null", ex );
            		return null;
            	}
            }
            return person;
        }
        // otherwise, no need to change the given object
        return currentPerson;
    }

    /**
     * Builds a map containing entries from the passed in Map that do NOT represent properties on an embedded
     * Person object.
     */
    private Map<String,String> getNonPersonSearchCriteria( BusinessObject bo, Map<String,String> fieldValues) {
        Map<String,String> nonUniversalUserSearchCriteria = new HashMap<String,String>();
        for ( String propertyName : fieldValues.keySet() ) {
            if (!isPersonProperty(bo, propertyName)) {
                nonUniversalUserSearchCriteria.put(propertyName, fieldValues.get(propertyName));
            }
        }
        return nonUniversalUserSearchCriteria;
    }


    private boolean isPersonProperty(BusinessObject bo, String propertyName) {
        try {
        	if ( ObjectUtils.isNestedAttribute( propertyName ) // is a nested property
            		&& !StringUtils.contains(propertyName, "add.") ) {// exclude add line properties (due to path parsing problems in PropertyUtils.getPropertyType)
        		Class<?> type = PropertyUtils.getPropertyType(bo, ObjectUtils.getNestedAttributePrefix( propertyName ));
        		// property type indicates a Person object
        		if ( type != null ) {
        			return Person.class.isAssignableFrom(type);
        		}
        		LOG.warn( "Unable to determine type of nested property: " + bo.getClass().getName() + " / " + propertyName );
        	}
        } catch (Exception ex) {
        	if ( LOG.isDebugEnabled() ) {
        		LOG.debug("Unable to determine if property on " + bo.getClass().getName() + " to a person object: " + propertyName, ex );
        	}
        }
        return false;
    }
    
    /**
     * @see org.kuali.rice.kim.api.identity.PersonService#resolvePrincipalNamesToPrincipalIds(org.kuali.rice.krad.bo.BusinessObject, java.util.Map)
     */
    @SuppressWarnings("unchecked")
	public Map<String,String> resolvePrincipalNamesToPrincipalIds(BusinessObject businessObject, Map<String,String> fieldValues) {
    	if ( fieldValues == null ) {
    		return null;
    	}
    	if ( businessObject == null ) {
    		return fieldValues;
    	}
    	StringBuffer resolvedPrincipalIdPropertyName = new StringBuffer();
    	// save off all criteria which are not references to Person properties
    	// leave person properties out so they can be resolved and replaced by this method
        Map<String,String> processedFieldValues = getNonPersonSearchCriteria(businessObject, fieldValues);
        for ( String propertyName : fieldValues.keySet() ) {        	
            if (	!StringUtils.isBlank(fieldValues.get(propertyName))  // property has a value
            		&& isPersonProperty(businessObject, propertyName) // is a property on a Person object
            		) {
            	// strip off the prefix on the property
                String personPropertyName = ObjectUtils.getNestedAttributePrimitive( propertyName );
                // special case - the user ID 
                if ( StringUtils.equals( KIMPropertyConstants.Person.PRINCIPAL_NAME, personPropertyName) ) {
                    Class targetBusinessObjectClass = null;
                    BusinessObject targetBusinessObject = null;
                    resolvedPrincipalIdPropertyName.setLength( 0 ); // clear the buffer without requiring a new object allocation on each iteration
                	// get the property name up until the ".principalName"
                	// this should be a reference to the Person object attached to the BusinessObject                	
                	String personReferenceObjectPropertyName = ObjectUtils.getNestedAttributePrefix( propertyName );
                	// check if the person was nested within another BO under the master BO.  If so, go up one more level
                	// otherwise, use the passed in BO class as the target class
                    if ( ObjectUtils.isNestedAttribute( personReferenceObjectPropertyName ) ) {
                        String targetBusinessObjectPropertyName = ObjectUtils.getNestedAttributePrefix( personReferenceObjectPropertyName );
                        targetBusinessObject = (BusinessObject)ObjectUtils.getPropertyValue( businessObject, targetBusinessObjectPropertyName );
                        if (targetBusinessObject != null) {
                            targetBusinessObjectClass = targetBusinessObject.getClass();
                            resolvedPrincipalIdPropertyName.append(targetBusinessObjectPropertyName).append(".");
                        } else {
                            LOG.error("Could not find target property '"+propertyName+"' in class "+businessObject.getClass().getName()+". Property value was null.");
                        }
                    } else { // not a nested Person property
                        targetBusinessObjectClass = businessObject.getClass();
                        targetBusinessObject = businessObject;
                    }
                    
                    if (targetBusinessObjectClass != null) {
                    	// use the relationship metadata in the KNS to determine the property on the
                    	// host business object to put back into the map now that the principal ID
                    	// (the value stored in application tables) has been resolved
                        String propName = ObjectUtils.getNestedAttributePrimitive( personReferenceObjectPropertyName );
                        DataObjectRelationship rel = getBusinessObjectMetaDataService().getBusinessObjectRelationship( targetBusinessObject, propName );
                        if ( rel != null ) {
                            String sourcePrimitivePropertyName = rel.getParentAttributeForChildAttribute(KIMPropertyConstants.Person.PRINCIPAL_ID);
                            resolvedPrincipalIdPropertyName.append(sourcePrimitivePropertyName);
                        	// get the principal - for translation of the principalName to principalId
                            String principalName = fieldValues.get( propertyName );
                        	Principal principal = getIdentityService().getPrincipalByPrincipalName( principalName );
                            if (principal != null ) {
                                processedFieldValues.put(resolvedPrincipalIdPropertyName.toString(), principal.getPrincipalId());
                            } else {
                                processedFieldValues.put(resolvedPrincipalIdPropertyName.toString(), null);
                                try {
                                    // if the principalName is bad, then we need to clear out the Person object
                                    // and base principalId property
                                    // so that their values are no longer accidentally used or re-populate
                                    // the object
                                    ObjectUtils.setObjectProperty(targetBusinessObject, resolvedPrincipalIdPropertyName.toString(), null );
                                    ObjectUtils.setObjectProperty(targetBusinessObject, propName, null );
                                    ObjectUtils.setObjectProperty(targetBusinessObject, propName + ".principalName", principalName );
                                } catch ( Exception ex ) {
                                    LOG.error( "Unable to blank out the person object after finding that the person with the given principalName does not exist.", ex );
                                }
                            }
                        } else {
                        	LOG.error( "Missing relationship for " + propName + " on " + targetBusinessObjectClass.getName() );
                        }
                    } else { // no target BO class - the code below probably will not work
                        processedFieldValues.put(resolvedPrincipalIdPropertyName.toString(), null);
                    }
                }
            // if the property does not seem to match the definition of a Person property but it
            // does end in principalName then...
            // this is to handle the case where the user ID is on an ADD line - a case excluded from isPersonProperty()
            } else if (propertyName.endsWith("." + KIMPropertyConstants.Person.PRINCIPAL_NAME)){
                // if we're adding to a collection and we've got the principalName; let's populate universalUser
                String principalName = fieldValues.get(propertyName);
                if ( StringUtils.isNotEmpty( principalName ) ) {
                    String containerPropertyName = propertyName;
                    if (containerPropertyName.startsWith(KRADConstants.MAINTENANCE_ADD_PREFIX)) {
                        containerPropertyName = StringUtils.substringAfter( propertyName, KRADConstants.MAINTENANCE_ADD_PREFIX );
                    }
                    // get the class of the object that is referenced by the property name
                    // if this is not true then there's a principalName collection or primitive attribute 
                    // directly on the BO on the add line, so we just ignore that since something is wrong here
                    if ( ObjectUtils.isNestedAttribute( containerPropertyName ) ) {
                    	// the first part of the property is the collection name
                        String collectionName = StringUtils.substringBefore( containerPropertyName, "." );
                        // what is the class held by that collection?
                        // JHK: I don't like this.  This assumes that this method is only used by the maintenance
                        // document service.  If that will always be the case, this method should be moved over there.
                        Class<? extends BusinessObject> collectionBusinessObjectClass = getMaintenanceDocumentDictionaryService()
                        		.getCollectionBusinessObjectClass(
                        				getMaintenanceDocumentDictionaryService()
                        						.getDocumentTypeName(businessObject.getClass()), collectionName);
                        if (collectionBusinessObjectClass != null) {
                            // we are adding to a collection; get the relationships for that object; 
                        	// is there one for personUniversalIdentifier?
                            List<DataObjectRelationship> relationships =
                            		getBusinessObjectMetaDataService().getBusinessObjectRelationships( collectionBusinessObjectClass );
                            // JHK: this seems like a hack - looking at all relationships for a BO does not guarantee that we get the right one
                            // JHK: why not inspect the objects like above?  Is it the property path problems because of the .add. portion?
                            for ( DataObjectRelationship rel : relationships ) {
                            	String parentAttribute = rel.getParentAttributeForChildAttribute( KIMPropertyConstants.Person.PRINCIPAL_ID );
                            	if ( parentAttribute == null ) {
                            		continue;
                            	}
                                // there is a relationship for personUserIdentifier; use that to find the universal user
                            	processedFieldValues.remove( propertyName );
                        		String fieldPrefix = StringUtils.substringBeforeLast( StringUtils.substringBeforeLast( propertyName, "." + KIMPropertyConstants.Person.PRINCIPAL_NAME ), "." );
                                String relatedPrincipalIdPropertyName = fieldPrefix + "." + parentAttribute;
                                // KR-683 Special handling for extension objects
                         	 	if(EXTENSION.equals(StringUtils.substringAfterLast(fieldPrefix, ".")) && EXTENSION.equals(StringUtils.substringBefore(parentAttribute, ".")))
                         	 	{
                         	 		relatedPrincipalIdPropertyName = fieldPrefix + "." + StringUtils.substringAfter(parentAttribute, ".");
                         	 	}
                                String currRelatedPersonPrincipalId = processedFieldValues.get(relatedPrincipalIdPropertyName);
                                if ( StringUtils.isBlank( currRelatedPersonPrincipalId ) ) {
                                	Principal principal = getIdentityService().getPrincipalByPrincipalName( principalName );
                                	if ( principal != null ) {
                                		processedFieldValues.put(relatedPrincipalIdPropertyName, principal.getPrincipalId());
                                	} else {
                                		processedFieldValues.put(relatedPrincipalIdPropertyName, null);
                                	}
                                }
                            } // relationship loop
                        } else {
                        	if ( LOG.isDebugEnabled() ) {
                        		LOG.debug( "Unable to determine class for collection referenced as part of property: " + containerPropertyName + " on " + businessObject.getClass().getName() );
                        	}
                        }
                    } else {
                    	if ( LOG.isDebugEnabled() ) {
                    		LOG.debug( "Non-nested property ending with 'principalName': " + containerPropertyName + " on " + businessObject.getClass().getName() );
                    	}
                    }
                }
            }
        }
        return processedFieldValues;
    }
	
	// OTHER METHODS

	protected IdentityService getIdentityService() {
		if ( identityService == null ) {
			identityService = KimApiServiceLocator.getIdentityService();
		}
		return identityService;
	}

	protected RoleService getRoleService() {
		if ( roleService == null ) {
			roleService = KimApiServiceLocator.getRoleService();
		}
		return roleService;
	}


	public Class<? extends Person> getPersonImplementationClass() {
		return PersonImpl.class;
	}
	
	protected BusinessObjectMetaDataService getBusinessObjectMetaDataService() {
		if ( businessObjectMetaDataService == null ) {
			businessObjectMetaDataService = KNSServiceLocator.getBusinessObjectMetaDataService();
		}
		return businessObjectMetaDataService;
	}

	protected MaintenanceDocumentDictionaryService getMaintenanceDocumentDictionaryService() {
		if ( maintenanceDocumentDictionaryService == null ) {
			maintenanceDocumentDictionaryService = KNSServiceLocator.getMaintenanceDocumentDictionaryService();
		}
		return maintenanceDocumentDictionaryService;
	}
}
