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
package org.kuali.ole.select.lookup;

import org.kuali.rice.krad.bo.BusinessObjectBase;

import java.util.LinkedHashMap;

public class DocData extends BusinessObjectBase implements DocStoreData {

    private String author;
    private String isbn;
    private String localIdentifier;
    private String titleId;
    private String uniqueId;
    private String bibIdentifier;
    private String itemTitle;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLocalIdentifier() {
        return localIdentifier;
    }

    public void setLocalIdentifier(String localIdentifier) {
        this.localIdentifier = localIdentifier;
    }

    private String publisher;

    public String getAuthor() {
        return author;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public DocData() {
    }

    public DocData(String author, String title, String isbn, String publisher, String placeOfPulication, String publicationDate, String format, String price, String titleId, String uniqueId, String bibIdentifier) {
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.placeOfPublication = placeOfPulication;
        this.publicationDate = publicationDate;
        this.format = format;
        this.price = price;
        this.titleId = titleId;
        this.uniqueId = uniqueId;
        this.bibIdentifier = bibIdentifier;

    }

    public DocData(String author, String title, String isbn, String localIdentifier, String publisher, String placeOfPulication, String publicationDate, String format, String price, String titleId, String uniqueId, String bibIdentifier) {
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.localIdentifier = localIdentifier;
        this.publisher = publisher;
        this.placeOfPublication = placeOfPulication;
        this.publicationDate = publicationDate;
        this.format = format;
        this.price = price;
        this.titleId = titleId;
        this.uniqueId = uniqueId;
        this.bibIdentifier = bibIdentifier;

    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        return null;
    }

    public void refresh() {
        // TODO Auto-generated method stub

    }

    private String placeOfPublication;
    private String publicationDate;
    private String format;
    private String price;

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = placeOfPublication;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getItemTitle() {

        itemTitle = getTitle();
        if(itemTitle.contains("<")){
           itemTitle = itemTitle.replace("<","&#60;");
        }
        if(itemTitle.contains(">")){
            itemTitle = itemTitle.replace(">","&#62;");
        }
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
