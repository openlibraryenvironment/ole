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
package org.kuali.rice.krms.impl.repository

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition

// NOTE: this class is temporarily renamed to get it out of the way.  See AgendaItemBo.java
public class AgendaItemBoFoo extends PersistableBusinessObjectBase {

//	public MetaClass getMetaClass() {
//		return super.getMetaClass();
//	}
	
	def String id
	def String agendaId
	def String ruleId
	def String subAgendaId
	def String whenTrueId
	def String whenFalseId
	def String alwaysId 
	
	def AgendaItemBo whenTrue;
	def AgendaItemBo whenFalse;
	def AgendaItemBo always;
	
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
	
//	public List<AgendaItemBo> getWhenTrueList() {
//		List<AgendaItemBo> results = new ArrayList<AgendaItemBo>();
//		
//		AgendaItemBo currentNode = this;
//		if (currentNode.whenTrue != null) {
//			results.add(currentNode.whenTrue);
//			currentNode = currentNode.whenTrue;
//		}
//		while (currentNode.always != null) {
//			results.add(currentNode.always);
//			currentNode = currentNode.always;
//		}
//		
//		return results;
//	}
//	
//	public List<AgendaItemBo> getWhenFalseList() {
//		List<AgendaItemBo> results = new ArrayList<AgendaItemBo>();
//		
//		AgendaItemBo currentNode = this;
//		if (currentNode.whenFalse != null) {
//			results.add(currentNode.whenFalse);
//			currentNode = currentNode.whenFalse;
//		}
//		while (currentNode.always != null) {
//			results.add(currentNode.always);
//			currentNode = currentNode.always;
//		}
//		
//		return results;
//	}
	
	// Would make life in KNS easier to map to related AgendaItemBos, and RuleBos

	
	/**
	* Converts a mutable bo to it's immutable counterpart
	* @param bo the mutable business object
	* @return the immutable object
	*/
	static AgendaItemDefinition to(AgendaItemBo bo) {
		if (bo == null) { return null }
		AgendaItemDefinition.Builder builder = AgendaItemDefinition.Builder.create(bo.getId(), bo.getAgendaId())
		builder.setRuleId( bo.getRuleId() )
		builder.setSubAgendaId( bo.getSubAgendaId() )
		builder.setWhenTrueId( bo.getWhenTrueId() )
		builder.setWhenFalseId( bo.getWhenFalseId() )
		builder.setAlwaysId( bo.getAlwaysId() )
		builder.setVersionNumber( bo.getVersionNumber() );
		return builder.build()	
		// NOTE: should we create tree nodes (whenTrue, whenFalse, always) ??
		//   If so, we would end up creating the entire sub-tree.
		//   If we do want to create the entire tree, having AgendaItemDefinition implement AgendaItemDefinitionContract would be easier
		//   so we could recursively build it out.
		
//		return org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition.Builder.create(bo).build()
	}

   /**
	* Converts a immutable object to it's mutable bo counterpart
	* @param im immutable object
	* @return the mutable bo
	*/
   static AgendaItemBo from(AgendaItemDefinition im) {
	   if (im == null) { return null }

	   AgendaItemBo bo = new AgendaItemBo()
	   bo.id = im.id
	   bo.agendaId = im.agendaId
	   bo.ruleId = im.ruleId
	   bo.subAgendaId = im.subAgendaId
	   bo.whenTrueId = im.whenTrueId
	   bo.whenFalseId = im.whenFalseId
	   bo.alwaysId = im.alwaysId
	   bo.versionNumber = im.versionNumber
	   
	   return bo
   }

} 