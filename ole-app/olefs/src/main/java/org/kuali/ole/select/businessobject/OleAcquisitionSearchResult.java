/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.businessobject;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.bo.BusinessObjectBase;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * This class is the Business Object class for the Acquisition Search
 */
public class OleAcquisitionSearchResult extends BusinessObjectBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleAcquisitionSearchResult.class);

    private String organizationDocumentNumber;
    private String financialDocumentTypeCode;
    private String documentDescription;
    private String applicationDocumentStatus;
    private String initiator;
    private Date dateCreated;
    private String purapDocumentIdentifier;
    private String itemTitleId;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String localIdentifier;
    private PersonService personService;

    /**
     * Gets the organizationDocumentNumber attribute.
     *
     * @return Returns the organizationDocumentNumber.
     */
    public String getOrganizationDocumentNumber() {
        return organizationDocumentNumber;
    }

    /**
     * Sets the organizationDocumentNumber attribute value.
     *
     * @param organizationDocumentNumber The organizationDocumentNumber to set.
     */
    public void setOrganizationDocumentNumber(String organizationDocumentNumber) {
        this.organizationDocumentNumber = organizationDocumentNumber;
    }

    /**
     * Gets the financialDocumentTypeCode attribute.
     *
     * @return Returns the financialDocumentTypeCode.
     */
    public String getFinancialDocumentTypeCode() {
        return financialDocumentTypeCode;
    }

    public String getLocalIdentifier() {
        return localIdentifier;
    }

    public void setLocalIdentifier(String localIdentifier) {
        this.localIdentifier = localIdentifier;
    }

    /**
     * Sets the financialDocumentTypeCode attribute value.
     *
     * @param financialDocumentTypeCode The financialDocumentTypeCode to set .
     */
    public void setFinancialDocumentTypeCode(String financialDocumentTypeCode) {
        this.financialDocumentTypeCode = financialDocumentTypeCode;
    }

    /**
     * Gets the documentDescription attribute.
     *
     * @return Returns the documentDescription.
     */
    public String getDocumentDescription() {
        return documentDescription;
    }

    /**
     * Sets the documentDescription attribute value.
     *
     * @param documentDescription The documentDescription to set.
     */
    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    /**
     * Gets the applicationDocumentStatus attribute.
     *
     * @return Returns the applicationDocumentStatus.
     */
    public String getApplicationDocumentStatus() {
        return applicationDocumentStatus;
    }

    /**
     * Sets the applicationDocumentStatus attribute value.
     *
     * @param applicationDocumentStatus The applicationDocumentStatus to set.
     */
    public void setApplicationDocumentStatus(String applicationDocumentStatus) {
        this.applicationDocumentStatus = applicationDocumentStatus;
    }

    /**
     * Gets the initiator attribute.
     *
     * @return Returns the initiator.
     */
    public String getInitiator() {
        return initiator;
    }

    /**
     * Sets the initiator attribute value.
     *
     * @param initiator The initiator to set.
     */
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    /**
     * Gets the dateCreated attribute.
     *
     * @return Returns the dateCreated.
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the dateCreated attribute value.
     *
     * @param dateCreated The dateCreated to set.
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Gets the purapDocumentIdentifier attribute.
     *
     * @return Returns the purapDocumentIdentifier.
     */
    public String getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    /**
     * Sets the purapDocumentIdentifier attribute value.
     *
     * @param purapDocumentIdentifier The purapDocumentIdentifier to set.
     */
    public void setPurapDocumentIdentifier(String purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

    /**
     * Gets the itemTitleId attribute.
     *
     * @return Returns the itemTitleId.
     */
    public String getItemTitleId() {
        return itemTitleId;
    }

    /**
     * Sets the itemTitleId attribute value.
     *
     * @param itemTitleId The itemTitleId to set.
     */
    public void setItemTitleId(String itemTitleId) {
        this.itemTitleId = itemTitleId;
    }

    /**
     * Gets the title attribute.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title attribute value.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author attribute.
     *
     * @return Returns the author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author attribute value.
     *
     * @param author The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the publisher attribute.
     *
     * @return Returns the publisher.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher attribute value.
     *
     * @param publisher The publisher to set.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


    /**
     * Gets the isbn attribute.
     *
     * @return Returns the isbn.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the isbn attribute value.
     *
     * @param isbn The isbn to set.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    /**
     * This method sets the result fields from the DocumentSearchResult and the
     *
     * @param isBibSearch
     * @param searchResults
     * @param docDatas
     */
    /* public void setResultDetails(boolean isBibSearch, DocumentSearchResult searchResult, List<DocData> docDatas) {
        if (LOG.isDebugEnabled()) {
            LOG.info("Inside setResultDetails method of OleAcquisitionSearchResult");
        }
        Document document = searchResult.getDocument();
        List<DocumentAttribute> documentAttributes = searchResult.getDocumentAttributes();
        for (DocumentAttribute docAttribute : documentAttributes) {
            String name = docAttribute.getName();
            if (OleSelectConstant.AcquisitionsSearch.RESULT_FIELDS.contains(name)) {
                if (name.equals(OleSelectConstant.AcquisitionsSearch.PO_ID)) {
                    name = OleSelectConstant.AcquisitionsSearch.ACQ_PO_NUMBER;
                }
                Method getMethod;
                try {
                    getMethod = getSetMethod(OleAcquisitionSearchResult.class, name, new Class[]{String.class});
                    getMethod.invoke(this, docAttribute.getValue().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            this.setApplicationDocumentStatus(document.getApplicationDocumentStatus());
            this.setOrganizationDocumentNumber(document.getDocumentId());
            this.setFinancialDocumentTypeCode(document.getDocumentTypeName());
            this.setInitiator((SpringContext.getBean(PersonService.class)).getPerson(document.getInitiatorPrincipalId()).getPrincipalName());
            this.setDateCreated(document.getDateCreated().toDate());
        }
        if (isBibSearch && docDatas.size() > 0) {
            for (DocData docData : docDatas) {
                if (itemTitleId != null && this.itemTitleId.equals(docData.getUniqueId())) {
                    this.setTitle(docData.getTitle());
                    this.setAuthor(docData.getAuthor());
                    this.setPublisher(docData.getPublisher());
                    this.setIsbn(docData.getIsbn());
                    this.setLocalIdentifier(docData.getLocalIdentifier());
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.info("Leaving setResultDetails method of OleAcquisitionSearchResult");
        }
    }
*/
    public void setResultDetails(boolean isBibSearch, DocumentSearchResult searchResult, List<Bib> bibs) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside setResultDetails method of OleAcquisitionSearchResult");
        }
        Document document = searchResult.getDocument();
        List<DocumentAttribute> documentAttributes = searchResult.getDocumentAttributes();
        for (DocumentAttribute docAttribute : documentAttributes) {
            String name = docAttribute.getName();
            if (OleSelectConstant.AcquisitionsSearch.RESULT_FIELDS.contains(name)) {
                if (name.equals(OleSelectConstant.AcquisitionsSearch.PO_ID)) {
                    name = OleSelectConstant.AcquisitionsSearch.ACQ_PO_NUMBER;
                }
                Method getMethod;
                try {
                    getMethod = getSetMethod(OleAcquisitionSearchResult.class, name, new Class[]{String.class});
                    getMethod.invoke(this, docAttribute.getValue().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            this.setApplicationDocumentStatus(document.getApplicationDocumentStatus());
            this.setOrganizationDocumentNumber(document.getDocumentId());
            this.setFinancialDocumentTypeCode(document.getDocumentTypeName());
            this.setInitiator((SpringContext.getBean(PersonService.class)).getPerson(document.getInitiatorPrincipalId()).getPrincipalName());
            this.setDateCreated(document.getDateCreated().toDate());
        }
        if (isBibSearch && bibs.size() > 0) {
            for (Bib bib : bibs) {
                //bib=(Bib)bib.deserializeContent(bib);
                if (itemTitleId != null && this.itemTitleId.equals(bib.getId())) {
                    String localId = DocumentUniqueIDPrefix.getDocumentId(bib.getId());
                    if(StringUtils.isNotBlank(localId)){
                        this.setTitle(bib.getTitle());
                        this.setAuthor(bib.getAuthor());
                        this.setPublisher(bib.getPublisher());
                        this.setIsbn(bib.getIsbn());
                        this.setLocalIdentifier(localId);
                        break;
                    }

                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Leaving setResultDetails method of OleAcquisitionSearchResult");
        }
    }

    /**
     * This method generates the setter method for the given attribute
     *
     * @param c
     * @param attr
     * @param objectAttributes
     * @return
     * @throws Exception
     */
    private Method getSetMethod(Class targetClass, String attr, Class[] objectAttributes) throws Exception {
        Method method = targetClass.getMethod("set" + StringUtils.capitalize(attr), objectAttributes);
        return method;
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

}
