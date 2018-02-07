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
package org.kuali.rice.kew.doctype;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Composite identifier object for an {@link org.kuali.rice.kew.doctype.ApplicationDocumentStatusCategory}.  The
 * elements that uniquely identify a category are the document type id and the category name.
 */
@Embeddable
public class ApplicationDocumentStatusCategoryId implements Serializable {

    @Column(name="DOC_TYP_ID")
    private String documentTypeId;
    @Column(name="NM")
    private String categoryName;

    /**
     * Get the document type id
     * @return the document type id
     */
    public String getDocumentTypeId() { return documentTypeId; }

    /**
     * Set the document type id
     * @param documentTypeId the document type id to set
     */
    public void setDocumentTypeId(String documentTypeId) { this.documentTypeId = documentTypeId; }

    /**
     * Get the category name
     * @return the category name
     */
    public String getCategoryName() { return categoryName; }

    /**
     * Set the category name
     * @param categoryName the category name to set
     */
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

	/**
	 * calculates a hashcode based on the documentTypeId and categoryName
	 *
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.documentTypeId == null) ? 0 : this.documentTypeId
						.hashCode());
		result = prime * result
				+ ((this.categoryName == null) ? 0 : this.categoryName.hashCode());
		return result;
	}

	/**
	 * determines equality based on the class, documentTypeId and categoryName
	 *
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ApplicationDocumentStatusCategoryId other = (ApplicationDocumentStatusCategoryId) obj;
		if (this.documentTypeId == null) {
			if (other.documentTypeId != null)
				return false;
		} else if (!this.documentTypeId.equals(other.documentTypeId))
			return false;
		if (this.categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!this.categoryName.equals(other.categoryName))
			return false;
		return true;
	}

}

