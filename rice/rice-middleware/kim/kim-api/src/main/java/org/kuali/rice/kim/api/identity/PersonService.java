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
package org.kuali.rice.kim.api.identity;

import java.util.List;
import java.util.Map;

import org.kuali.rice.krad.bo.BusinessObject;

/**
 * 
 * This service acts as a facade on the entity information that is provided
 * through the IdentityService and provides a "person-centric" view of that 
 * entity data.
 * 
 * <p>In general, the Person object flattens out the various related pieces of
 * entity data into a denormalized view on that data.  In many cases, that
 * data will have a defined "default" value in the entity data model.  This
 * default data is what the Person object will be constructed with.  For
 * example, an entity can have more than one name, but one of those names
 * is flagged as the default.
 * 
 * <p>This service will do it's best to construct valid Person objects even
 * for entities that don't have an entity type of "PERSON".  In those cases
 * not all of the attributes on the Person object will be populated.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface PersonService {

	/**
	 * Retrieve a single Person object by Principal ID.
	 */
	Person getPerson( String principalId );
	
	/**
	 * Retrieve a person by an arbitrary external identifier.  This method could
	 * potentially return multiple results as there is no guarantee of uniqueness
	 * for external identifiers.
	 * 
	 * @param externalIdentifierTypeCode Type of external identifier to search for.
	 * @param externalId The external identifier.
	 * @return List of Person objects.
	 */
	List<Person> getPersonByExternalIdentifier( String externalIdentifierTypeCode, String externalId );
	
	/**
	 * Gets a single Person by their principal name (user ID).
	 */
	Person getPersonByPrincipalName( String principalName );
	
	/**
	 * Gets a single Person by their employee id.
	 */
	Person getPersonByEmployeeId( String employeeId ); 
	
	/**
	 * Perform an unbounded search for person records.
	 */
	List<Person> findPeople( Map<String, String> criteria );

	/**
	 * Perform a Person lookup.  If bounded, it will follow the configured KNS lookup limit.
	 */
	List<Person> findPeople( Map<String, String> criteria, boolean unbounded );
	
	/**
	 * Get the class object which points to the class used by the underlying implementation.
	 * 
	 * This can be used by implementors who may need to construct Person objects without wishing to bind their code
	 * to a specific implementation.
	 */
	Class<? extends Person> getPersonImplementationClass();
	
	/**
     * This method takes a map on its way to populate a business object and replaces all 
     * user identifiers with their corresponding universal users
	 */
	Map<String, String> resolvePrincipalNamesToPrincipalIds( BusinessObject businessObject, Map<String, String> fieldValues );
	
    /**
     * Compares the Principal ID passed in with that in the Person object.  If they are the same, it returns the
     * original object.  Otherwise, it pulls the Person from KIM based on the sourcePrincipalId.
     */
	Person updatePersonIfNecessary(String sourcePrincipalId, Person currentPerson );

}
