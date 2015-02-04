/*
 * Copyright 2011 The Kuali Foundation.
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

package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.BusinessObjectBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DocInfoBean extends BusinessObjectBase {

    //private String author;
    private String author_display;

    private String dateOfPublication;

    private String date_d;

    private List<String> descriptionList;

    //private String description;
    private String description_display;

    private String dimentions;

    private String docType;

    private String extent;

    private String generalNote;

    private List<String> isbnList;

    //private String isbn;
    private String isbn_display;

    private String mainEntryPersonalNameComposite;

    private String mainEntryPersonalNameComposite_suggest;

    private String modifyingAgency;

    private String modifyingAgency_suggest;

    //private String nameOfPublisher;
    private String publisher_display;

    //private String placeOfPublication;
    private String publicationPlace_search;

    private String placeOfPublication_suggest;

    private String price_f;

    private String remainderOfTitle;

    private String statementOfResponsibility;

    private List<String> subjectList;

    //private String subject;
    private String subject_display;

    //private String title;
    private String title_display;

    private String titleStatementComposite;

    private String title_suggest;

    private String topicalTermorgeographicnameElement;

    private String yearOfPublication;

    private List<String> all_controlFieldsList;

    private String all_controlFields;

    private List<String> all_subfieldsList;

    private String all_subfields;

    private List<String> all_textList;

    private String all_text;

    //private String id;
    private String titleId;

    private String uniqueId;

    private String noOfRecords;

    private String bibIdentifier;

    private String publisher_search;

    private String localIdentifier_search;

    private String publicationDate_search;


    public DocInfoBean() {
        all_textList = new ArrayList<String>();
        all_subfieldsList = new ArrayList<String>();
        all_controlFieldsList = new ArrayList<String>();
        subjectList = new ArrayList<String>();
        isbnList = new ArrayList<String>();
        descriptionList = new ArrayList<String>();

    }
    
/*    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }*/

    public String getAuthor_display() {
        return author_display;
    }

    public void setAuthor_display(String author_display) {
        this.author_display = author_display;
    }

    public String getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(String dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public String getDate_d() {
        return date_d;
    }

    public void setDate_d(String date_d) {
        this.date_d = date_d;
    }

    public String getDimentions() {
        return dimentions;
    }

    public void setDimentions(String dimentions) {
        this.dimentions = dimentions;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public String getMainEntryPersonalNameComposite() {
        return mainEntryPersonalNameComposite;
    }

    public void setMainEntryPersonalNameComposite(String mainEntryPersonalNameComposite) {
        this.mainEntryPersonalNameComposite = mainEntryPersonalNameComposite;
    }

    public String getMainEntryPersonalNameComposite_suggest() {
        return mainEntryPersonalNameComposite_suggest;
    }

    public void setMainEntryPersonalNameComposite_suggest(String mainEntryPersonalNameComposite_suggest) {
        this.mainEntryPersonalNameComposite_suggest = mainEntryPersonalNameComposite_suggest;
    }

    public String getModifyingAgency() {
        return modifyingAgency;
    }

    public void setModifyingAgency(String modifyingAgency) {
        this.modifyingAgency = modifyingAgency;
    }

    public String getModifyingAgency_suggest() {
        return modifyingAgency_suggest;
    }

    public void setModifyingAgency_suggest(String modifyingAgency_suggest) {
        this.modifyingAgency_suggest = modifyingAgency_suggest;
    }

/*    public String getNameOfPublisher() {
        return nameOfPublisher;
    }

    public void setNameOfPublisher(String nameOfPublisher) {
        this.nameOfPublisher = nameOfPublisher;
    }

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = placeOfPublication;
    }*/

    public String getPlaceOfPublication_suggest() {
        return placeOfPublication_suggest;
    }

    public String getPublisher_display() {
        return publisher_display;
    }

    public void setPublisher_display(String publisher_display) {
        this.publisher_display = publisher_display;
    }

    public String getPublicationPlace_search() {
        return publicationPlace_search;
    }

    public void setPublicationPlace_search(String publicationPlace_search) {
        this.publicationPlace_search = publicationPlace_search;
    }

    public void setPlaceOfPublication_suggest(String placeOfPublication_suggest) {
        this.placeOfPublication_suggest = placeOfPublication_suggest;
    }

    public String getPrice_f() {
        return price_f;
    }

    public void setPrice_f(String price_f) {
        this.price_f = price_f;
    }

    public String getRemainderOfTitle() {
        return remainderOfTitle;
    }

    public void setRemainderOfTitle(String remainderOfTitle) {
        this.remainderOfTitle = remainderOfTitle;
    }

    public String getStatementOfResponsibility() {
        return statementOfResponsibility;
    }

    public void setStatementOfResponsibility(String statementOfResponsibility) {
        this.statementOfResponsibility = statementOfResponsibility;
    }

/*    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }*/

    public String getTitle_display() {
        return title_display;
    }

    public void setTitle_display(String title_display) {
        this.title_display = title_display;
    }

    public String getTitleStatementComposite() {
        return titleStatementComposite;
    }

    public void setTitleStatementComposite(String titleStatementComposite) {
        this.titleStatementComposite = titleStatementComposite;
    }

    public String getTitle_suggest() {
        return title_suggest;
    }

    public void setTitle_suggest(String title_suggest) {
        this.title_suggest = title_suggest;
    }

    public String getTopicalTermorgeographicnameElement() {
        return topicalTermorgeographicnameElement;
    }

    public void setTopicalTermorgeographicnameElement(String topicalTermorgeographicnameElement) {
        this.topicalTermorgeographicnameElement = topicalTermorgeographicnameElement;
    }

    public String getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    /*   public String getId() {
            return id;
        }*/
    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public List<String> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<String> descriptionList) {
        this.descriptionList = descriptionList;
    }

/*    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.descriptionList.add(description);
    }*/

    public String getDescription_display() {
        return description_display;
    }

    public void setDescription_display(String description_display) {
        this.description_display = description_display;
        this.descriptionList.add(description_display);
    }

    public List<String> getIsbnList() {
        return isbnList;
    }

    public void setIsbnList(List<String> isbnList) {
        this.isbnList = isbnList;
    }

    /*    public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
            this.isbnList.add(isbn);
        }
    */
    public String getIsbn_display() {
        return isbn_display;
    }

    public void setIsbn_display(String isbn_display) {
        this.isbn_display = isbn_display;
        this.isbnList.add(isbn_display);
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

/*public String getSubject() {
    return subject;
}

public void setSubject(String subject) {
	this.subject = subject;
	this.subjectList.add(subject);
}*/

    public String getSubject_display() {
        return subject_display;
    }

    public void setSubject_display(String subject_display) {
        this.subject_display = subject_display;
        this.subjectList.add(subject_display);
    }

    public List<String> getAll_controlFieldsList() {
        return all_controlFieldsList;
    }

    public void setAll_controlFieldsList(List<String> all_controlFieldsList) {
        this.all_controlFieldsList = all_controlFieldsList;
    }

    public String getAll_controlFields() {
        return all_controlFields;
    }

    public void setAll_controlFields(String all_controlFields) {
        this.all_controlFields = all_controlFields;
        this.all_controlFieldsList.add(all_controlFields);
    }

    public List<String> getAll_subfieldsList() {
        return all_subfieldsList;
    }

    public void setAll_subfieldsList(List<String> all_subfieldsList) {
        this.all_subfieldsList = all_subfieldsList;
    }

    public String getAll_subfields() {
        return all_subfields;
    }

    public void setAll_subfields(String all_subfields) {
        this.all_subfields = all_subfields;
        this.all_subfieldsList.add(all_subfields);
    }

    public List<String> getAll_textList() {
        return all_textList;
    }

    public void setAll_textList(List<String> all_textList) {
        this.all_textList = all_textList;
    }

    public String getAll_text() {
        return all_text;
    }

    public void setAll_text(String all_text) {
        this.all_text = all_text;
        this.all_textList.add(all_text);
    }

/*public void setId(String id) {
    this.id = id;
 }*/

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        return null;
    }

    public void refresh() {
        // TODO Auto-generated method stub

    }

    public String getNoOfRecords() {
        return noOfRecords;
    }

    public void setNoOfRecords(String noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String getPublicationDate_search() {
        return publicationDate_search;
    }

    public void setPublicationDate_search(String publicationDate_search) {
        this.publicationDate_search = publicationDate_search;
    }

    public String getPublisher_search() {
        return publisher_search;
    }

    public void setPublisher_search(String publisher_search) {
        this.publisher_search = publisher_search;
    }

    public String getLocalIdentifier_search() {
        return localIdentifier_search;
    }

    public void setLocalIdentifier_search(String localIdentifier_search) {
        this.localIdentifier_search = localIdentifier_search;
    }
}
