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
package org.kuali.ole.docstore.discovery.service;

import org.kuali.ole.docstore.discovery.model.SearchParams;

public interface DiscoveryService {
    public static final String SEARCH_TYPE_NEW = "newSearch";
    public static final String SEARCH_TYPE_QUICK = "quickSearch";
    public static final String SEARCH_TYPE_ADVANCED = "advancedSearch";
    public static final String SEARCH_TYPE_FACET = "facetSearch";
    public static final String SEARCH_TYPE_FACET_DELETE = "facetDelete";
    public static final String SEARCH_TYPE_LINK = "linksearch";
    public static final String BIBLIOGRAPHIC = "bibliographic";
    public static final String INSTANCE = "instance";
    public static final String HOLDINGS = "holding";
    public static final String ITEM = "item";
    public static final String INSTANCE_LINK_FIELDS = "bibIdentifier,holdingsIdentifier,itemIdentifier";
    public static final String BIB_LINK_FIELDS = "instanceIdentifier";
    public static final String HOLDINGS_LINK_FIELDS = "bibIdentifier,instanceIdentifier,itemIdentifier";
    public static final String ITEM_LINK_FIELDS = "bibIdentifier,instanceIdentifier,holdingsIdentifier";
    public static final String HOLDINGS_FIELDS = "LocalId_display,Uri_display,HoldingsNote_display,ReceiptStatus_display,CallNumber_display,CallNumberPrefix_display,CallNumberType_display,ClassificationPart_display,DocType,DocFormat,id" + "," + HOLDINGS_LINK_FIELDS;
    public static final String ITEM_FIELDS = "LocalId_display,ItemBarcode_display,ItemTypeFullValue_display,VendorLineItemIdentifier_display,ShelvingSchemeValue_display,ShelvingOrderValue_display,PurchaseOrderLineItemIdentifier_display,CopyNumber_display,VolumeNumber_display,DocType,DocFormat,id,Barcode_display" + "," + ITEM_LINK_FIELDS;
    public static final String INSTANCE_FIELDS = "LocalId_display,Source_display,DocType,DocFormat,id,Barcode_display" + "," + INSTANCE_LINK_FIELDS;
    public static final String BIB_FIELDS = "LocalId_display,Title_display,Author_display,Publisher_display,Description_display,Subject_display,Location_display,Format_display,DocType,DocFormat,id,Barcode_display" + "," + BIB_LINK_FIELDS;
    public static final String AUTHOR_FACET = "Author_facet";
    public static final String SUBJECT_FACET = "Subject_facet";
    public static final String FORMAT_FACET = "Format_facet";
    public static final String LANGUAGE_FACET = "Language_facet";
    public static final String PUBLICATION_DATE_FACET = "PublicationDate_facet";
    public static final String GENRE_FACET = "Genre_facet";
    public static final String SEARCH_TYPE_MORE_FACET = "moreFacets";
    public static final String BATCH_UPLOAD_LINK_SEARCH = "batchuploadsearch";

    /**
     * Performs a search based on the given search parameters and returns the result in solr xml format.
     * This is used by the GUI screens.
     *
     * @param searchParams - search parameters
     * @return search result in xml format
     */
    public String search(SearchParams searchParams);

    public String buildQuery(SearchParams searchParams);
}
