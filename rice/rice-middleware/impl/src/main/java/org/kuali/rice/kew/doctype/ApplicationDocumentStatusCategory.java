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

import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Model bean representing a grouping of valid application document statuses for a document type
 * An instance of this class represents a subset of the valid status for a given document type.
 * </p>
 * <p>The purpose of the Application Document Status Category is to provide a grouping mechanism
 * for Application Document Status that can be used to not only label a set of statuses as having some common
 * characteristic, but to allow users to search on all the grouped statuses by selecting the category name.
 * </p>
 * <p>The (optional) valid application statuses element within a document type definition may (again, optionally) contain
 * category elements which define these groupings of valid statuses.
 * </p>
 * @author Peter Giles
 *
 */
@Entity
@Table(name="KREW_DOC_TYP_APP_STAT_CAT_T")
public class ApplicationDocumentStatusCategory extends PersistableBusinessObjectBase {
	private static final long serialVersionUID = -2212481684546954746L;

	@EmbeddedId
	private ApplicationDocumentStatusCategoryId applicationDocumentStatusCategoryId;

	@MapsId("documentTypeId")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DOC_TYP_ID")
	private DocumentType documentType;

	@Transient
	private String documentTypeId;
	@Transient
	private String categoryName;

    /**
     * Gets the composite identifier, a {@link org.kuali.rice.kew.doctype.ApplicationDocumentStatusCategoryId}
     * @return the application document status category id
     */
    public ApplicationDocumentStatusCategoryId getApplicationDocumentStatusCategoryId() {
    	if (this.applicationDocumentStatusCategoryId == null) {
    		this.applicationDocumentStatusCategoryId = new ApplicationDocumentStatusCategoryId();
    	}
		return this.applicationDocumentStatusCategoryId;
	}

    /**
     * Sets the composition identifier
     * @param categoryId the composite identifier to set
     */
	public void setApplicationDocumentStatusCategoryId(ApplicationDocumentStatusCategoryId categoryId) {
		this.applicationDocumentStatusCategoryId = categoryId;
	}

    /**
     * Get the document type id for this category
     * @return the document type id
     */
	public String getDocumentTypeId() {
		if (this.getApplicationDocumentStatusCategoryId().getDocumentTypeId() != null) {
            return this.getApplicationDocumentStatusCategoryId().getDocumentTypeId();
        } else {
            return this.documentTypeId;
        }
	}

    /**
     * Set the document type id for this category
     * @param documentTypeId the document type id to set
     */
	public void setDocumentTypeId(String documentTypeId) {
		this.documentTypeId = documentTypeId;
		this.getApplicationDocumentStatusCategoryId().setDocumentTypeId(documentTypeId);
	}

    /**
     * The name for this category
     * @return the category name
     */
	public String getCategoryName() {
        if (this.getApplicationDocumentStatusCategoryId().getCategoryName() != null) {
            return this.getApplicationDocumentStatusCategoryId().getCategoryName();
        } else {
            return this.categoryName;
        }
	}

    /**
     * Set the name for this category
     * @param statusName the category name to set
     */
	public void setCategoryName(String statusName) {
		this.categoryName = statusName;
		this.getApplicationDocumentStatusCategoryId().setCategoryName(statusName);
	}

    /**
     * Get the document type that this category is associated with
     * @return the document type for this category
     */
	public DocumentType getDocumentType() {
		return this.documentType;
	}

    /**
     * Set the document type that this category is associated with
     * @param documentType the document type to set
     */
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

}
