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
package org.kuali.ole.docstore.discovery.solr.work.bib.marc;

import org.kuali.ole.docstore.discovery.solr.work.bib.WorkBibCommonFields;

/**
 * Class to WorkBibMarcFields.
 *
 * @author Rajesh Chowdary K
 */
public interface WorkBibMarcFields
        extends WorkBibCommonFields {

    public static final String BARCODE_DISPLAY = "Barcode_display";
    public static final String BARCODE_SEARCH = "Barcode_search";

    public static final String EDITION_DISPLAY = "Edition_display";

    public static final String FORMGENRE_DISPLAY = "FormGenre_display";
    public static final String FORMGENRE_SEARCH = "FormGenre_search";

    public static final String GENRE_FACET = "Genre_facet";

    public static final String ISBN_DISPLAY = "ISBN_display";
    public static final String ISBN_SEARCH = "ISBN_search";

    public static final String ISSN_DISPLAY = "ISSN_display";
    public static final String ISSN_SEARCH = "ISSN_search";

    public static final String COMMON_IDENTIFIER_SEARCH = "common_identifier_search";

    public static final String LEADER = "leader";

    public static final String LOCATION_DISPLAY = "Location_display";
    public static final String LOCATION_SEARCH = "Location_search";

    public static final String PUBLICATION_PLACE_DISPLAY = "PublicationPlace_display";
    public static final String PUBLICATION_PLACE_SEARCH = "PublicationPlace_search";
}
