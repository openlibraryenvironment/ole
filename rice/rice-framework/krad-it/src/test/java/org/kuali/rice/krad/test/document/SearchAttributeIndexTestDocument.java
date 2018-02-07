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
package org.kuali.rice.krad.test.document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.krad.document.TransactionalDocumentBase;

/**
 * Mock document for testing how document search carries out indexing 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="TST_SEARCH_ATTR_INDX_TST_DOC_T")
public class SearchAttributeIndexTestDocument extends TransactionalDocumentBase {
	static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SearchAttributeIndexTestDocument.class);
	private static final long serialVersionUID = -2290510385815271758L;
	@Column(name="RTE_LVL_CNT")
	private int routeLevelCount = 0;
	@Column(name="CNSTNT_STR")
	private String constantString;
	@Column(name="RTD_STR")
	private String routedString;
	@Column(name="HLD_RTD_STR")
	private String heldRoutedString;
	@Column(name="RD_ACCS_CNT")
	private int readAccessCount = 0;
	
	/**
	 * Constructor for the document which sets the constant string and keeps a hole of the routedString
	 * @param constantString the constant String to set
	 * @param routedString the routed String to hold on to, but not set until routing has occurred
	 */
	public void initialize(String constantString, String routedString) {
		this.constantString = constantString;
		this.heldRoutedString = routedString;
	}
	
	/**
	 * @return the count of how many route levels have been passed
	 */
	public int getRouteLevelCount() {
		readAccessCount += 1;
		return routeLevelCount;
	}
	
	/**
	 * @return a constant String
	 */
	public String getConstantString() {
		return constantString;
	}
	
	/**
	 * @return a routed String
	 */
	public String getRoutedString() {
		return routedString;
	}
	
	/**
	 * @return the readAccessCount
	 */
	public int getReadAccessCount() {
		return this.readAccessCount;
	}

	/**
	 * Overridden to make the document state change as route levels occur
	 * 
	 * @see org.kuali.rice.krad.document.DocumentBase#doRouteLevelChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange)
	 */
	@Override
	public void doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) {
		super.doRouteLevelChange(levelChangeEvent);
		routeLevelCount += 1;
		if (routedString == null) {
			routedString = heldRoutedString;
		}
		LOG.info("Performing route level change on SearchAttributeIndexTestDocument; routeLevelCount is "+routeLevelCount);
	}
	
}
