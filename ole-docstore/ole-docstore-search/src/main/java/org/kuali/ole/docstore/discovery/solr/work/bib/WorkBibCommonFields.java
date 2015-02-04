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
package org.kuali.ole.docstore.discovery.solr.work.bib;

public interface WorkBibCommonFields {

    public static final String UNIQUE_ID = "uniqueId";
    public static final String DOC_TYPE = "DocType";
    public static final String DOC_FORMAT = "DocFormat";
    public static final String DOC_CATEGORY = "DocCategory";

    public static final String DOC_TYPE_ITEM_VALUE = "item";
    public static final String DOC_TYPE_HOLDING_VALUE = "holding";
    public static final String DOC_TYPE_INSTANCE_VALUE = "instance";

    public static final String DOC_CATEGORY_VALUE = "work";

    public static final String DOC_FORMAT_INSTANCE_VALUE = "oleml";

    public static final String ID = "id";

    public static final String LOCALID_SEARCH = "LocalId_search";
    public static final String LOCALID_DISPLAY = "LocalId_display";

    public static final String ALL_TEXT = "all_text";

    public static final String AUTHOR_SORT = "Author_sort";
    public static final String AUTHOR_DISPLAY = "Author_display";
    public static final String AUTHOR_SEARCH = "Author_search";
    public static final String AUTHOR_FACET = "Author_facet";

    public static final String DESCRIPTION_DISPLAY = "Description_display";
    public static final String DESCRIPTION_SEARCH = "Description_search";

    public static final String FORMAT_DISPLAY = "Format_display";
    public static final String FORMAT_SEARCH = "Format_search";
    public static final String FORMAT_FACET = "Format_facet";

    public static final String LANGUAGE_DISPLAY = "Language_display";
    public static final String LANGUAGE_SEARCH = "Language_search";
    public static final String LANGUAGE_FACET = "Language_facet";

    public static final String PUBLICATIONDATE_SORT = "PublicationDate_sort";
    public static final String PUBLICATIONDATE_DISPLAY = "PublicationDate_display";
    public static final String PUBLICATIONDATE_SEARCH = "PublicationDate_search";
    public static final String PUBLICATIONDATE_FACET = "PublicationDate_facet";


    public static final String PUBLISHER_DISPLAY = "Publisher_display";
    public static final String PUBLISHER_SEARCH = "Publisher_search";

    public static final String SUBJECT_DISPLAY = "Subject_display";
    public static final String SUBJECT_SEARCH = "Subject_search";
    public static final String SUBJECT_FACET = "Subject_facet";

    public static final String TITLE_SORT = "Title_sort";
    public static final String TITLE_DISPLAY = "Title_display";
    public static final String TITLE_SEARCH = "Title_search";

    public static final String TYPE_SEARCH = "Type_search";
    public static final String TYPE_DISPLAY = "Type_display";

    public static final String EDITION_SEARCH = "Edition_search";
    public static final String EDITION_DISPLAY = "Edition_display";

    public static final String GENRE_FACET = "Genre_facet";

    public static final String ISBN_NOT_NORMALIZED = "not Normalized";

    public static final String COVERAGE_SEARCH = "Coverage_search";
    public static final String RELATION_SEARCH = "Relation_search";
    public static final String COVERAGE_DISPLAY = "Coverage_display";
    public static final String RELATION_DISPLAY = "Relation_display";
    public static final String SYSTEM_CONTROL_NUMBER = "SystemControlNumber";
    public static final String ISBN_SEARCH = "ISBN_search";
    public static final String STAFF_ONLY_FLAG = "staffOnlyFlag";
    public static final String CREATED_BY = "createdBy";
    public static final String UPDATED_BY = "updatedBy";
    public static final String DATE_ENTERED = "dateEntered";
    public static final String DATE_UPDATED = "dateUpdated";
    public static final String STATUS_SEARCH = "Status_search";
    public static final String BIB_ID= "bibIdentifier";

    public static final String CLMS_RET_FLAG="claimsReturnedFlag";
    public static final String CLMS_RET_FLAG_CRE_DATE="claimsReturnedFlagCreateDate";
    public static final String CLMS_RET_NOTE="claimsReturnedNote";
    public static final String CURRENT_BORROWER="currentBorrower";
    public static final String PROXY_BORROWER="proxyBorrower";
    public static final String DUE_DATE_TIME="dueDateTime";

    }
