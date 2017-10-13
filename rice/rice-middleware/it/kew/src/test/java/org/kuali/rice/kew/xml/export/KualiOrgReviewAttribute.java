/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.xml.export;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.kuali.rice.kew.rule.GenericWorkflowAttribute;

/**
 * This is a test WorkflowAttribute which holds two values, fin_coa_cd and org_cd
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiOrgReviewAttribute extends GenericWorkflowAttribute {

	// We weant to provide support for these values:
	/*
	 				<ruleExtensionValues>
						<ruleExtensionValue>
							<key>fin_coa_cd</key>
							<value>KU</value>
						</ruleExtensionValue>
						<ruleExtensionValue>
							<key>org_cd</key>
							<value>KOOL</value>
						</ruleExtensionValue>
					</ruleExtensionValues>
	 */
	
	private static final long serialVersionUID = 6717444752714424385L;
	
	private static final String FIN_COA_CD = "fin_coa_cd";
	private static final String ORG_CD = "org_cd";
	
	private String finCoaCode;
	private String orgCode;

	/**
	 * provide the attribute values as map entries
	 * 
	 * @see org.kuali.rice.kew.rule.GenericWorkflowAttribute#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		Map<String,String> properties = new LinkedHashMap<String, String>();
		properties.put(FIN_COA_CD, finCoaCode);
		properties.put(ORG_CD, orgCode);
		return properties;
	}
	
	public List validateRoutingData(Map paramMap) {
		return validateInputMap(paramMap);
	}

	public List validateRuleData(Map paramMap) {
		return validateInputMap(paramMap);
	}

    private List validateInputMap(Map paramMap) {
    	this.finCoaCode = (String) paramMap.get(FIN_COA_CD);
    	this.orgCode = (String) paramMap.get(ORG_CD);
    	return Collections.emptyList();
    }

	/**
	 * @return the finCoaCode
	 */
	public String getFinCoaCode() {
		return this.finCoaCode;
	}

	/**
	 * @param finCoaCode the finCoaCode to set
	 */
	public void setFinCoaCode(String finCoaCode) {
		this.finCoaCode = finCoaCode;
	}

	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return this.orgCode;
	}

	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
}
