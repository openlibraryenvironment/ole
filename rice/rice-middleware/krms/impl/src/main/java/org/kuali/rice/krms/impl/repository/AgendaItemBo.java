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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinitionContract;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinitionContract;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.kuali.rice.krms.impl.repository.AgendaItemBo.COPY_OF_TEXT;

/**
 * Agenda Item business object
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AgendaItemBo extends PersistableBusinessObjectBase implements AgendaItemDefinitionContract {

    public static final String COPY_OF_TEXT = "Copy of ";
    private static final String KRMS_AGENDA_ITM_S = "KRMS_AGENDA_ITM_S";

	private String id;
	private String agendaId;
	private String ruleId;
	private String subAgendaId;
	private String whenTrueId;
	private String whenFalseId;
	private String alwaysId;
	
	private RuleBo rule;
	
	private AgendaItemBo whenTrue;
	private AgendaItemBo whenFalse;
	private AgendaItemBo always;

    private static SequenceAccessorService sequenceAccessorService;
	
	public String getUl(AgendaItemBo firstItem) {
		return ("<ul>" + getUlHelper(firstItem) + "</ul>");
	}
	
	public String getUlHelper(AgendaItemBo item) {
		StringBuilder sb = new StringBuilder();
		sb.append("<li>" + ruleId + "</li>");
		if (whenTrue != null) {
			sb.append("<ul><li>when true</li><ul>");
			sb.append(getUlHelper(whenTrue));
			sb.append("</ul></ul>");
		}
		if (whenFalse != null) {
			sb.append("<ul><li>when false</li><ul>");
			sb.append(getUlHelper(whenFalse));
			sb.append("</ul></ul>");
		}
		if (always != null) {
			sb.append(getUlHelper(always));
		}
		return sb.toString();
	}

    public String getRuleText() {
        StringBuilder resultBuilder = new StringBuilder();
        if (getRule() != null) {
            if (StringUtils.isBlank(getRule().getName())) {
                resultBuilder.append("- unnamed rule -");
            } else {
                resultBuilder.append(getRule().getName());
            }
            if (!StringUtils.isBlank(getRule().getDescription())) {
                resultBuilder.append(": ");
                resultBuilder.append(getRule().getDescription());
            }
            // add a description of the action configured on the rule, if there is one
            if (!CollectionUtils.isEmpty(getRule().getActions())) {
                resultBuilder.append("   [");
                ActionBo action = getRule().getActions().get(0);

                KrmsTypeDefinition krmsTypeDefn =
                        KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService().getTypeById(action.getTypeId());

                resultBuilder.append(krmsTypeDefn.getName());
                resultBuilder.append(": ");
                resultBuilder.append(action.getName());

                if (getRule().getActions().size() > 1) {
                    resultBuilder.append(" ... ");
                }
                resultBuilder.append("]");
            }
        } else {
            throw new IllegalStateException();
        }
        return resultBuilder.toString();
    }

//	def List<AgendaItemBo> alwaysList
//	def List<AgendaItemBo> whenTrueList
//	def List<AgendaItemBo> whenFalseList
	
	public List<AgendaItemBo> getAlwaysList() {
		List<AgendaItemBo> results = new ArrayList<AgendaItemBo>();
		
		AgendaItemBo currentNode = this;
		while (currentNode.always != null) {
			results.add(currentNode.always);
			currentNode = currentNode.always;
		}
		
		return results;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the agendaId
	 */
	public String getAgendaId() {
		return this.agendaId;
	}

	/**
	 * @param agendaId the agendaId to set
	 */
	public void setAgendaId(String agendaId) {
		this.agendaId = agendaId;
	}

	/**
	 * @return the ruleId
	 */
	public String getRuleId() {
		return this.ruleId;
	}

	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * @return the subAgendaId
	 */
	public String getSubAgendaId() {
		return this.subAgendaId;
	}

	/**
	 * @param subAgendaId the subAgendaId to set
	 */
	public void setSubAgendaId(String subAgendaId) {
		this.subAgendaId = subAgendaId;
	}


	/**
	 * @return the whenTrueId
	 */
	public String getWhenTrueId() {
		return this.whenTrueId;
	}

	/**
	 * @param whenTrueId the whenTrueId to set
	 */
	public void setWhenTrueId(String whenTrueId) {
		this.whenTrueId = whenTrueId;
	}

	/**
	 * @return the whenFalseId
	 */
	public String getWhenFalseId() {
		return this.whenFalseId;
	}

	/**
	 * @param whenFalseId the whenFalseId to set
	 */
	public void setWhenFalseId(String whenFalseId) {
		this.whenFalseId = whenFalseId;
	}

	/**
	 * @return the alwaysId
	 */
	public String getAlwaysId() {
		return this.alwaysId;
	}

	/**
	 * @param alwaysId the alwaysId to set
	 */
	public void setAlwaysId(String alwaysId) {
		this.alwaysId = alwaysId;
	}

	/**
	 * @return the whenTrue
	 */
	public AgendaItemBo getWhenTrue() {
		return this.whenTrue;
	}

	/**
	 * @param whenTrue the whenTrue to set
	 */
	public void setWhenTrue(AgendaItemBo whenTrue) {
		this.whenTrue = whenTrue;
        if (whenTrue != null) {
            setWhenTrueId(whenTrue.getId());
        } else {
            setWhenTrueId(null);
        }
	}

	/**
	 * @return the whenFalse
	 */
	public AgendaItemBo getWhenFalse() {
		return this.whenFalse;
	}

	/**
	 * @param whenFalse the whenFalse to set
	 */
	public void setWhenFalse(AgendaItemBo whenFalse) {
		this.whenFalse = whenFalse;
        if (whenFalse != null) {
            setWhenFalseId(whenFalse.getId());
        } else {
            setWhenFalseId(null);
        }
	}

	/**
	 * @return the always
	 */
	public AgendaItemBo getAlways() {
		return this.always;
	}

	/**
	 * @param always the always to set
	 */
	public void setAlways(AgendaItemBo always) {
		this.always = always;
        if (always != null) {
            setAlwaysId(always.getId());
        } else {
            setAlwaysId(null);
        }
	}
	
    /**
     * @return the rule
     */
    public RuleBo getRule() {
        return this.rule;
    }

    @Override
    public AgendaDefinitionContract getSubAgenda() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param rule the rule to set
     */
    public void setRule(RuleBo rule) {
        this.rule = rule;
        if (rule != null) {
            setRuleId(rule.getId());
        } else {
            setRuleId(null);
        }
    }

	
    /**
	* Converts a mutable bo to it's immutable counterpart
	* @param bo the mutable business object
	* @return the immutable object
	*/
   static AgendaItemDefinition to(AgendaItemBo bo) {
	   if (bo == null) { return null; }
	   AgendaItemDefinition.Builder builder = AgendaItemDefinition.Builder.create(bo);
	   
	   return builder.build();
   }

   /**
	* Converts a immutable object to it's mutable bo counterpart
	* @param im immutable object
	* @return the mutable bo
	*/
   static AgendaItemBo from(AgendaItemDefinition im) {
	   if (im == null) { return null; }

	   AgendaItemBo bo = new AgendaItemBo();
	   bo.id = im.getId();
	   bo.agendaId = im.getAgendaId();
	   bo.ruleId = im.getRuleId();
	   bo.subAgendaId = im.getSubAgendaId();
	   bo.whenTrueId = im.getWhenTrueId();
	   bo.whenFalseId = im.getWhenFalseId();
	   bo.alwaysId = im.getAlwaysId();
       bo.versionNumber = im.getVersionNumber();

       bo.rule = RuleBo.from(im.getRule());
       bo.whenTrue = AgendaItemBo.from(im.getWhenTrue());
       bo.whenFalse = AgendaItemBo.from(im.getWhenFalse());
       bo.always = AgendaItemBo.from(im.getAlways());
	   
	   return bo;
   }

    /**
     * Returns a copy of this AgendaItem
     * @param copiedAgenda the new Agenda that the copied AgendiaItem will be associated with
     * @param oldRuleIdToNew Map<String, RuleBo> mapping of old rule id to the new RuleBo
     * @param dts DateTimeStamp to append to the copied AgendaItem name
     * @return AgendaItemBo copy of this AgendaItem with new id and name
     */
    public AgendaItemBo copyAgendaItem(AgendaBo copiedAgenda,  Map<String, RuleBo> oldRuleIdToNew,
            Map<String, AgendaItemBo> oldAgendaItemIdToNew, List<AgendaItemBo> copiedAgendaItems, final String dts) {
        // Use deepCopy and update all the ids.
        AgendaItemBo copiedAgendaItem = (AgendaItemBo) ObjectUtils.deepCopy(this);
        copiedAgendaItem.setId(getNewId());
        copiedAgendaItem.setAgendaId(copiedAgenda.getId());

        oldAgendaItemIdToNew.put(this.getId(), copiedAgendaItem);

        // Don't create another copy of a rule that we have already copied.
        if (!oldRuleIdToNew.containsKey(this.getRuleId())) {
            if (this.getRule() != null) {
                copiedAgendaItem.setRule(this.getRule().copyRule(COPY_OF_TEXT + this.getRule().getName() + " " + dts));
                oldRuleIdToNew.put(this.getRuleId(), copiedAgendaItem.getRule());
            }
        } else {
            copiedAgendaItem.setRule(oldRuleIdToNew.get(this.getRuleId()));
        }

        if (copiedAgendaItem.getWhenFalse() != null) {
            if (!oldAgendaItemIdToNew.containsKey(this.getWhenFalseId())) {
                copiedAgendaItem.setWhenFalse(this.getWhenFalse().copyAgendaItem(copiedAgenda, oldRuleIdToNew, oldAgendaItemIdToNew, copiedAgendaItems, dts));
                oldAgendaItemIdToNew.put(this.getWhenFalseId(), copiedAgendaItem.getWhenFalse());
                copiedAgendaItems.add(copiedAgendaItem.getWhenFalse());
            } else {
                copiedAgendaItem.setWhenFalse(oldAgendaItemIdToNew.get(this.getWhenFalseId()));
            }
        }

        if (copiedAgendaItem.getWhenTrue() != null) {
            if (!oldAgendaItemIdToNew.containsKey(this.getWhenTrueId())) {
                copiedAgendaItem.setWhenTrue(this.getWhenTrue().copyAgendaItem(copiedAgenda, oldRuleIdToNew, oldAgendaItemIdToNew, copiedAgendaItems, dts));
                oldAgendaItemIdToNew.put(this.getWhenTrueId(), copiedAgendaItem.getWhenTrue());
                copiedAgendaItems.add(copiedAgendaItem.getWhenTrue());
            } else {
                copiedAgendaItem.setWhenTrue(oldAgendaItemIdToNew.get(this.getWhenTrueId()));
            }
        }

        if (copiedAgendaItem.getAlways() != null) {
            if (!oldAgendaItemIdToNew.containsKey(this.getAlwaysId())) {
                copiedAgendaItem.setAlways(this.getAlways().copyAgendaItem(copiedAgenda, oldRuleIdToNew, oldAgendaItemIdToNew, copiedAgendaItems, dts));
                oldAgendaItemIdToNew.put(this.getAlwaysId(), copiedAgendaItem.getAlways());
                copiedAgendaItems.add(copiedAgendaItem.getAlways());
            } else {
                copiedAgendaItem.setAlways(oldAgendaItemIdToNew.get(this.getAlwaysId()));
            }
        }
        return copiedAgendaItem;
    }


    /**
     * Set the SequenceAccessorService, useful for testing.
     * @param sas SequenceAccessorService to use for getNewId()
     */
    public static void setSequenceAccessorService(SequenceAccessorService sas) {
        sequenceAccessorService = sas;
    }

    /**
     * Returns the next available AgendaItem id.
     * @return String the next available id
     */
    private static String getNewId() {
        if (sequenceAccessorService == null) {
            // we don't assign to sequenceAccessorService to preserve existing behavior
            return KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber(KRMS_AGENDA_ITM_S, AgendaItemBo.class) + "";
        }
        Long id = sequenceAccessorService.getNextAvailableSequenceNumber(KRMS_AGENDA_ITM_S, AgendaItemBo.class);
        return id.toString();
    }
}