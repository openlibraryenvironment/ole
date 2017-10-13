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
package org.kuali.rice.core.framework.persistence.jpa.metadata;

import javax.persistence.CascadeType;

import org.apache.commons.lang.StringUtils;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class OneToOneDescriptor extends ObjectDescriptor implements java.io.Serializable {

	private static final long serialVersionUID = 5005807906661228700L;

	private String mappedBy = "";
	
	public String getMappedBy() {
		return this.mappedBy;
	}
	
	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("OneToOneDescriptor = [ ");
		sb.append("targetEntity:").append(targetEntity.getName()).append(", ");
		sb.append("cascade = { ");
		for (CascadeType ct : cascade) {
			sb.append(ct).append(" ");
		}
		sb.append("}, ");
		sb.append("fetch:").append(fetch).append(", ");
		sb.append("optional:").append(optional);
		if (!StringUtils.isBlank(mappedBy)) {
			sb.append(", mappedBy:").append(mappedBy);
		}
		if (!joinColumnDescriptors.isEmpty()) {
			sb.append(", join columns = { ");
			for (JoinColumnDescriptor joinColumnDescriptor : joinColumnDescriptors) {				
				sb.append(" jc = { ");
				sb.append("name:").append(joinColumnDescriptor.getName()).append(", ");
				sb.append("insertable:").append(joinColumnDescriptor.isInsertable()).append(", ");
				sb.append("nullable:").append(joinColumnDescriptor.isNullable()).append(", ");
				sb.append("unique:").append(joinColumnDescriptor.isUnique()).append(", ");
				sb.append("updateable:").append(joinColumnDescriptor.isUpdateable());
				sb.append(" }");
			}
			sb.append(" } ");
		}
		sb.append(" ]");
		return sb.toString();
	}
	
}
