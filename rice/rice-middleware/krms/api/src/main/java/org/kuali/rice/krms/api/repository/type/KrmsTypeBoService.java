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
package org.kuali.rice.krms.api.repository.type;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import java.util.List;

public interface KrmsTypeBoService {

    /**
     * This will create a {@link KrmsTypeDefinition} exactly like the parameter passed in.
     *
     * @param krmsType - KrmsType
     * @throws IllegalArgumentException if the krmsType is
     * +
     * @throws IllegalStateException if the KrmsType already exists in the system
     */
    KrmsTypeDefinition createKrmsType(KrmsTypeDefinition krmsType)
        throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This will update an existing {@link KrmsTypeDefinition}
     *
     * @param krmsType - KrmsType
     * @throws IllegalArgumentException if the krmsType is null
     * @throws IllegalStateException if the KrmsType does not exist in the system
     */
    KrmsTypeDefinition updateKrmsType(KrmsTypeDefinition krmsType)
        throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Lookup a krms type based on the given id.
     *
     * @param id the given KRMS type id
     * @return a KRMS KrmsType object with the given id.  A null reference is returned if an invalid or
     *         non-existant id is supplied.
     */
    KrmsTypeDefinition getTypeById(String id)
        throws RiceIllegalArgumentException;

    /**
     * Get a krms type object based on name and namespace
     *
     * @param namespaceCode the given type namespace
     * @param name the given type name
     * 
     * @return A krms type object with the given namespace and name if one with that name and namespace
     *         exists.  Otherwise, null is returned.
     *
     * @throws IllegalArgumentException if the given namespaceCode or name is a null or blank value
     * @throws IllegalStateException if multiple krms types exist with the same name and namespace
     */
    KrmsTypeDefinition getTypeByName( String namespaceCode, String name)
        throws RiceIllegalArgumentException, RiceIllegalStateException;

   /**
     * Returns all KRMS types that for a given namespace.
     *
     * @return all KRMS types for a namespace
     * @throws IllegalArgumentException if the given namespaceCode is a null or blank value
     */
    List<KrmsTypeDefinition> findAllTypesByNamespace(String namespaceCode)
        throws RiceIllegalArgumentException;
    
   /**
     * Returns all KRMS types that for a given serviceName.
     *
     * @return all KRMS types for a serviceName
     * @throws IllegalArgumentException if the given serviceName is a null or blank value
     */
    List<KrmsTypeDefinition> findAllTypesByServiceName(String serviceName)
        throws RiceIllegalArgumentException;

    /**
     * Returns all KRMS types
     *
     * @return all KRMS types
     */
    List<KrmsTypeDefinition> findAllTypes();

    /**
     * Returns all agenda types for the given contextId.
     *
     * @param contextId the id of the context
     * @return all agenda types for the given contextId
     * @throws IllegalArgumentException if the given contextId is a null or blank value
     */
    List<KrmsTypeDefinition> findAllAgendaTypesByContextId(String contextId)
        throws RiceIllegalArgumentException;

    /**
     * Return the agenda type by agendaItemId for the given contextId.
     *
     * @param agendaTypeId the id of the agendaType
     * @param contextId the id of the context
     * @return agendaType or null if none is found
     * @throws IllegalArgumentException if the given agendaTypeId or contextId is a null or blank value
     */
    KrmsTypeDefinition getAgendaTypeByAgendaTypeIdAndContextId(String agendaTypeId,String contextId)
        throws RiceIllegalArgumentException;

    /**
     * Returns all rule types for the given contextId.
     *
     * @param contextId the id of the context
     * @return all rule types for the given contextId
     * @throws IllegalArgumentException if the given contextId is a null or blank value
     */
    List<KrmsTypeDefinition> findAllRuleTypesByContextId(String contextId)
        throws RiceIllegalArgumentException;

    /**
     * Return the rule type by ruleItemId for the given contextId.
     *
     * @param ruleTypeId the id of the ruleType
     * @param contextId the id of the context
     * @return ruleType or null if none is found
     * @throws IllegalArgumentException if the given ruleTypeId or contextId is a null or blank value
     */
    KrmsTypeDefinition getRuleTypeByRuleTypeIdAndContextId(String ruleTypeId, String contextId)
        throws RiceIllegalArgumentException;

    /**
     * Returns all action types for the given contextId.
     *
     * @param contextId the id of the context
     * @return all action types for the given contextId
     * @throws IllegalArgumentException if the given contextId is a null or blank value
     */
    List<KrmsTypeDefinition> findAllActionTypesByContextId(String contextId)
        throws RiceIllegalArgumentException;

    /**
     * Return the action type by actionItemId for the given contextId.
     *
     * @param actionTypeId the id of the actionType
     * @param contextId the id of the context
     * @return actionType or null if none is found
     * @throws IllegalArgumentException if the given actionTypeId or contextId is a null or blank value
     */
    KrmsTypeDefinition getActionTypeByActionTypeIdAndContextId(String actionTypeId, String contextId)
        throws RiceIllegalArgumentException;

    /**
     * Retrieves an attribute definition for the given id.
     *
     * @param attributeDefinitionId the id of the attribute definition to retrieve
     *
     * @return the attribute definition matching the given id, or null if no corresponding attribute definition could
     * be found with the given id value
     * @throws IllegalArgumentException if the given attributeDefinitionId is a null or blank value
     */
    KrmsAttributeDefinition getAttributeDefinitionById(String attributeDefinitionId)
            throws RiceIllegalArgumentException;

    /**
     * Retrieves an attribute definition for the given namespace code and name.
     *
     * @param namespaceCode the namespace under which to locate the attribute definition
     * @param name the name of the attribute definition to retrieve
     *
     * @return the attribute definition matching the give namespace code and name, or null if no corresponding attribute
     * definition could be located
     *
     * @throws RiceIllegalArgumentException if the given namespaceCode or name is a null or blank value
     */
    KrmsAttributeDefinition getAttributeDefinitionByName(String namespaceCode, String name
    ) throws RiceIllegalArgumentException;
}
