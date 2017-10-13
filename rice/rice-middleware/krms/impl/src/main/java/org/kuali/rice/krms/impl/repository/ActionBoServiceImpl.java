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
package org.kuali.rice.krms.impl.repository;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.impl.util.KrmsImplConstants.PropertyNames;

/**
 * Implementation of the interface for accessing KRMS repository Action related
 * business objects.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class ActionBoServiceImpl implements ActionBoService {

    private BusinessObjectService businessObjectService;

	/**
	 * This overridden method creates a KRMS Action in the repository.
	 */
	@Override
	public ActionDefinition createAction(ActionDefinition action) {
		if (action == null){
	        throw new IllegalArgumentException("action is null");
		}
		final String actionNameKey = action.getName();
		final String actionNamespaceKey = action.getNamespace();
		final ActionDefinition existing = getActionByNameAndNamespace(actionNameKey, actionNamespaceKey);
		if (existing != null){
            throw new IllegalStateException("the action to create already exists: " + action);			
		}	
		
		ActionBo bo = ActionBo.from(action);
		businessObjectService.save(bo);
		
		return ActionBo.to(bo);
	}

	/**
	 * This overridden method updates an existing Action in the repository.
	 */
	@Override
	public void updateAction(ActionDefinition action) {
		if (action == null){
	        throw new IllegalArgumentException("action is null");
		}

		// must already exist to be able to update
		final String actionIdKey = action.getId();
		final ActionBo existing = businessObjectService.findBySinglePrimaryKey(ActionBo.class, actionIdKey);
        if (existing == null) {
            throw new IllegalStateException("the action does not exist: " + action);
        }
        final ActionDefinition toUpdate;
        if (existing.getId().equals(action.getId())) {
            toUpdate = action;
        } else {
            // if passed in id does not match existing id, correct it
            final ActionDefinition.Builder builder = ActionDefinition.Builder.create(action);
            builder.setId(existing.getId());
            toUpdate = builder.build();
        }
     
		// copy all updateable fields to bo
        ActionBo boToUpdate = ActionBo.from(toUpdate);

		// delete any old, existing attributes
		Map<String,String> fields = new HashMap<String,String>(1);
		fields.put(PropertyNames.Action.ACTION_ID, toUpdate.getId());
		businessObjectService.deleteMatching(ActionAttributeBo.class, fields);
        
		// update the action and create new attributes
        businessObjectService.save(boToUpdate);
	}

	/**
	 * This overridden method retrieves an Action from the repository.
	 */
	@Override
	public ActionDefinition getActionByActionId(String actionId) {
		if (StringUtils.isBlank(actionId)){
            throw new IllegalArgumentException("action ID is null or blank");			
		}
		ActionBo bo = businessObjectService.findBySinglePrimaryKey(ActionBo.class, actionId);
		return ActionBo.to(bo);
	}

	/**
	 * This overridden method retrieves an Action from the repository.
	 */
	@Override
	public ActionDefinition getActionByNameAndNamespace(String name, String namespace) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank");
        }
        if (StringUtils.isBlank(namespace)) {
            throw new IllegalArgumentException("namespace is blank");
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("namespace", namespace);

        ActionBo myAction = businessObjectService.findByPrimaryKey(ActionBo.class, Collections.unmodifiableMap(map));
        return ActionBo.to(myAction);
	}

	/**
	 * This overridden method retrieves a List of Actions associated with a Rule.
	 */
	@Override
	public List<ActionDefinition> getActionsByRuleId(String ruleId) {
		if (StringUtils.isBlank(ruleId)){
            throw new IllegalArgumentException("ruleId is null or blank");			
		}
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
		List<ActionBo> bos = (List<ActionBo>) businessObjectService.findMatchingOrderBy(ActionBo.class, map, "sequenceNumber", true);
		return convertListOfBosToImmutables(bos);
	}

	/**
	 * This overridden method retrieves a specific Action associated with a Rule.
	 */
	@Override
	public ActionDefinition getActionByRuleIdAndSequenceNumber(String ruleId, Integer sequenceNumber) {
		if (StringUtils.isBlank(ruleId)) {
            throw new IllegalArgumentException("ruleId is null or blank");
		}
		if (sequenceNumber == null) {
            throw new IllegalArgumentException("sequenceNumber is null");
		}
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ruleId", ruleId);
        map.put("sequenceNumber", sequenceNumber);
		ActionBo bo = businessObjectService.findByPrimaryKey(ActionBo.class, map);
		return ActionBo.to(bo);
	}

	/**
	 * This method retrieves an ActionAttributeBo by id
	 *
	 * @see org.kuali.rice.krms.impl.repository.ActionBoService#getActionsByRuleId(java.lang.String)
	 */
	public ActionAttributeBo getActionAttributeById(String attrId) {
		if (StringUtils.isBlank(attrId)){
            return null;			
		}
        return businessObjectService.findBySinglePrimaryKey(ActionAttributeBo.class, attrId);
	}

    /**
     * Sets the businessObjectService attribute value.
     *
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Converts a List<ActionBo> to an Unmodifiable List<Action>
     *
     * @param actionBos a mutable List<ActionBo> to made completely immutable.
     * @return An unmodifiable List<Action>
     */
    List<ActionDefinition> convertListOfBosToImmutables(final Collection<ActionBo> actionBos) {
    	if (actionBos == null) { return Collections.emptyList(); }
        ArrayList<ActionDefinition> actions = new ArrayList<ActionDefinition>();
        for (ActionBo bo : actionBos) {
            ActionDefinition action = ActionBo.to(bo);
            actions.add(action);
        }
        return Collections.unmodifiableList(actions);
    }


}
