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
package org.kuali.rice.kew.docsearch;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.impl.document.search.DocumentSearchCriteriaTranslatorImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.dao.DocumentRouteHeaderDAO;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests the StandardGenericXMLSearchableAttribute.
 *
 * KULWF-654: Tests the resolution to this issue by configuring a CustomActionListAttribute as well as a
 * searchable attribute.
 */
public class SearchableAttributeTest extends DocumentSearchTestBase {

    protected void loadTestData() throws Exception {
        loadXmlFile("SearchAttributeConfig.xml");
        loadXmlFile("SearchableTrimTest.xml");
    }

//    private SearchAttributeCriteriaComponent createSearchAttributeCriteriaComponent(String key,String value,Boolean isLowerBoundValue,DocumentType docType) {
//    	String formKey = (isLowerBoundValue == null) ? key : ((isLowerBoundValue != null && isLowerBoundValue.booleanValue()) ? SearchableAttributeOld.RANGE_LOWER_BOUND_PROPERTY_PREFIX : SearchableAttributeOld.RANGE_UPPER_BOUND_PROPERTY_PREFIX);
//    	String savedKey = key;
//    	SearchAttributeCriteriaComponent sacc = new SearchAttributeCriteriaComponent(formKey,value,savedKey);
//    	Field field = getFieldByFormKey(docType, formKey);
//    	if (field != null) {
//        	sacc.setSearchableAttributeValue(DocSearchUtils.getSearchableAttributeValueByDataTypeString(field.getFieldDataType()));
//        	sacc.setRangeSearch(field.isMemberOfRange());
//        	sacc.setAllowWildcards(field.isAllowingWildcards());
//        	sacc.setAutoWildcardBeginning(field.isAutoWildcardAtBeginning());
//        	sacc.setAutoWildcardEnd(field.isAutoWildcardAtEnding());
//        	sacc.setCaseSensitive(field.isCaseSensitive());
//        	sacc.setSearchInclusive(field.isInclusive());
//            sacc.setSearchable(field.isSearchable());
//            sacc.setCanHoldMultipleValues(Field.MULTI_VALUE_FIELD_TYPES.contains(field.getFieldType()));
//    	}
//    	return sacc;
//    }
//
//    private Field getFieldByFormKey(DocumentType docType, String formKey) {
//    	if (docType == null) {
//    		return null;
//    	}
//		for (SearchableAttributeOld searchableAttribute : docType.getSearchableAttributesOld()) {
//			for (Row row : searchableAttribute.getSearchingRows()) {
//				for (Field field : row.getFields()) {
//					if (field.getPropertyName().equals(formKey)) {
//						return field;
//					}
//				}
//			}
//		}
//		return null;
//    }

    /**
     * This tests the ability to get the searchableAttributeValues directly without going through the document.
     */
    @Test public void testSearchableAttributeSearch()throws Exception {
    	String documentTypeName = "SearchDocType";
        String userNetworkId = "rkirkend";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalId(userNetworkId), workflowDocument.getDocumentId());
        DocumentRouteHeaderValue doc = KEWServiceLocator.getRouteHeaderService().getRouteHeader(workflowDocument.getDocumentId());

        // HACK: we are cheating, but this functionality was removed from the service apis, so we hit the DAO directly
        DocumentRouteHeaderDAO dao = KEWServiceLocator.getBean("enDocumentRouteHeaderDAO");
        Collection<SearchableAttributeValue> allValues = dao.findSearchableAttributeValues(workflowDocument.getDocumentId());
        assertEquals("Wrong number of searchable attributes", 4, allValues.size());

        for (SearchableAttributeValue attributeValue: allValues) {
            if (attributeValue instanceof SearchableAttributeStringValue) {
                SearchableAttributeStringValue realValue = (SearchableAttributeStringValue) attributeValue;

                for(String value:getRouteHeaderService().getSearchableAttributeStringValuesByKey(doc.getDocumentId(), realValue.getSearchableAttributeKey())){
                	assertEquals("Assert that the values are the same", value, attributeValue.getSearchableAttributeValue());
                }

            } else if (attributeValue instanceof SearchableAttributeLongValue) {
                SearchableAttributeLongValue realValue = (SearchableAttributeLongValue) attributeValue;
                for(Long value:getRouteHeaderService().getSearchableAttributeLongValuesByKey(doc.getDocumentId(), realValue.getSearchableAttributeKey())){
                	assertEquals("Assert that the values are the same", value, attributeValue.getSearchableAttributeValue());
                }
            } else if (attributeValue instanceof SearchableAttributeFloatValue) {
                SearchableAttributeFloatValue realValue = (SearchableAttributeFloatValue) attributeValue;
                for(BigDecimal value:getRouteHeaderService().getSearchableAttributeFloatValuesByKey(doc.getDocumentId(), realValue.getSearchableAttributeKey())){
                	assertEquals("Assert that the values are the same", value, attributeValue.getSearchableAttributeValue());
                }

            } else if (attributeValue instanceof SearchableAttributeDateTimeValue) {
                SearchableAttributeDateTimeValue realValue = (SearchableAttributeDateTimeValue) attributeValue;
                assertEquals("The only DateTime attribute that should have been added has key '" + TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY + "'", TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, realValue.getSearchableAttributeKey());

                Calendar testDate = Calendar.getInstance();
                testDate.setTimeInMillis(realValue.getSearchableAttributeValue().getTime());
                testDate.set(Calendar.SECOND, 0);
                testDate.set(Calendar.MILLISECOND, 0);

                for(Timestamp value:getRouteHeaderService().getSearchableAttributeDateTimeValuesByKey(doc.getDocumentId(), realValue.getSearchableAttributeKey())){
                	Calendar attributeDate = Calendar.getInstance();
                    attributeDate.setTimeInMillis(value.getTime());
                    attributeDate.set(Calendar.SECOND, 0);
                    attributeDate.set(Calendar.MILLISECOND, 0);

                    assertEquals("The month value for the searchable attribute is wrong",testDate.get(Calendar.MONTH),attributeDate.get(Calendar.MONTH));
                    assertEquals("The date value for the searchable attribute is wrong",testDate.get(Calendar.DATE),attributeDate.get(Calendar.DATE));
                    assertEquals("The year value for the searchable attribute is wrong",testDate.get(Calendar.YEAR),attributeDate.get(Calendar.YEAR));
                }

            } else {
                fail("Searchable Attribute Value base class should be one of the four checked always");
            }
        }
    }

    protected RouteHeaderService getRouteHeaderService(){
    	RouteHeaderService rRet = KEWServiceLocator.getRouteHeaderService();
    	return rRet;
    }

    protected String getPrincipalId(String networkId){
    	return KimApiServiceLocator.getPersonService().getPersonByPrincipalName(networkId).getPrincipalId();
    }

    @Test public void testCustomSearchableAttributesWithDataType() throws Exception {
        String documentTypeName = "SearchDocType";
    	DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        String userNetworkId = "rkirkend";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        workflowDocument = WorkflowDocumentFactory.loadDocument(getPrincipalId(userNetworkId), workflowDocument.getDocumentId());
        DocumentRouteHeaderValue doc = KEWServiceLocator.getRouteHeaderService().getRouteHeader(workflowDocument.getDocumentId());

        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);
        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(userNetworkId);

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY,
                TestXMLSearchableAttributeString.SEARCH_STORAGE_VALUE);
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeString.SEARCH_STORAGE_KEY, "fred");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "fakeproperty", "doesntexist");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException e) {}

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY,
                TestXMLSearchableAttributeLong.SEARCH_STORAGE_VALUE.toString());
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeLong.SEARCH_STORAGE_KEY, "1111111");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "fakeymcfakefake", "99999999");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException e) {}

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY,
                TestXMLSearchableAttributeFloat.SEARCH_STORAGE_VALUE.toString());
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeFloat.SEARCH_STORAGE_KEY, "215.3548");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "fakeylostington", "9999.9999");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException e) {}

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY,
                DocumentSearchInternalUtils.getDisplayValueWithDateOnly(new Timestamp(
                        TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE_IN_MILLS)));
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should have one document.", 1, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, "07/06/1979");
        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals("Search results should be empty.", 0, results.getSearchResults().size());

        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        addSearchableAttribute(criteria, "lastingsfakerson", "07/06/2007");
        try {
            docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
            fail("Search results should be throwing a validation exception for use of non-existant searchable attribute");
        } catch (RuntimeException e) {}
    }

    /**
     * Tests searching documents with searchable attributes
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     */
    @Test public void testSearchAttributesAcrossDocumentTypeVersions() throws Exception {
        // first test searching for an initial version of the doc which does not have a searchable attribute
        loadXmlFile("testdoc0.xml");

        String documentTypeName = "SearchDoc";
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), documentTypeName);
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        doc.route("routing");

        DocumentSearchService docSearchService = (DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE);

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setDateCreatedFrom(new DateTime(2004, 1, 1, 0, 0));

        Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("arh14");
        DocumentSearchResults results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals(1, results.getSearchResults().size());

        // now upload the new version with a searchable attribute
        loadXmlFile("testdoc1.xml");
        docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);

        // route a new doc
        doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), documentTypeName);
        doc.route("routing");

        // with no attribute criteria, both docs should be found
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setDateCreatedFrom(new DateTime(2004, 1, 1, 0, 0));

        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals(2, results.getSearchResults().size());

        // search with specific SearchableAttributeOld value
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setDateCreatedFrom(new DateTime(2004, 1, 1, 0, 0));
        addSearchableAttribute(criteria, "MockSearchableAttributeKey", "MockSearchableAttributeValue");

        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());
        assertEquals(1, results.getSearchResults().size());

        // search with any SearchableAttributeOld value
        criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setDateCreatedFrom(new DateTime(2004, 1, 1, 0, 0));

        results = docSearchService.lookupDocuments(user.getPrincipalId(), criteria.build());

        assertEquals(2, results.getSearchResults().size());
    }

    /**
     * Tests the usage of wildcards on searchable attributes of varying data types.
     * Note that the bounds of ".."-related search expressions will not throw an exception if the lower bound is greater than the upper bound;
     * instead, such an expression will simply return zero results.
     * @throws Exception
     */
    @Test public void testWildcardsOnSearchableAttributes() throws Exception {
        String documentTypeName = "WildcardTestDocType";
    	DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        String principalName = "rkirkend";
        String principalId = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalName).getPrincipalId();
        String[][] searchableAttributeValuesAsStrings = { {"testString", "9984", "38.1357", "06/24/2009"},
        		{"anotherStr", "33", "80000.65432", "07/08/2010"}, {"MoreText", "432", "-0.765", "12/12/2012"} };

        // Route some documents containing the searchable attribute values given by the above array.
        for (int i = 0; i < searchableAttributeValuesAsStrings.length; i++) {
        	WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(principalId, documentTypeName);

        	// Add the string searchable attribute.
        	WorkflowAttributeDefinition.Builder wcStringXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeWildcardString");
        	wcStringXMLDef.addPropertyDefinition("xmlSearchableAttributeWildcardString", searchableAttributeValuesAsStrings[i][0]);
        	workflowDocument.addSearchableDefinition(wcStringXMLDef.build());
        	// Add the long searchable attribute.
        	WorkflowAttributeDefinition.Builder wcLongXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeWildcardLong");
        	wcLongXMLDef.addPropertyDefinition("xmlSearchableAttributeWildcardLong", searchableAttributeValuesAsStrings[i][1]);
        	workflowDocument.addSearchableDefinition(wcLongXMLDef.build());
        	// Add the float searchable attribute.
        	WorkflowAttributeDefinition.Builder wcFloatXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeWildcardFloat");
        	wcFloatXMLDef.addPropertyDefinition("xmlSearchableAttributeWildcardFloat", searchableAttributeValuesAsStrings[i][2]);
        	workflowDocument.addSearchableDefinition(wcFloatXMLDef.build());
        	// Add the datetime searchable attribute.
        	WorkflowAttributeDefinition.Builder wcDatetimeXMLDef = WorkflowAttributeDefinition.Builder.create("XMLSearchableAttributeWildcardDatetime");
        	wcDatetimeXMLDef.addPropertyDefinition("xmlSearchableAttributeWildcardDatetime", searchableAttributeValuesAsStrings[i][3]);
        	workflowDocument.addSearchableDefinition(wcDatetimeXMLDef.build());

        	workflowDocument.setTitle("Search Def Test Doc " + i);
        	workflowDocument.route("routing search def doc " + i);
        }

        // Ensure that wildcards work on searchable string attributes. Note that this search should be case-insensitive by default.
        // Also note that this should be the only case where the string-specific wildcards ("!", "?", and "*") should be working, unless
        // they are being used in a range expression.
        assertSearchableAttributeWildcardsWork(docType, principalId, "xmlSearchableAttributeWildcardString",
        		new String[]  {"TESTSTRING|moretext", "!MoreText"   , "!anotherStr!testString", "!anotherStr&&!MoreText"  , "!SomeString"      ,
        					"*str*"                 , "More????"    , "*e*n?"                 , "???String"               , "*te*&&!????String", "!test??????"       , "anotherStr..MoreText",
        					"testString..MoreText"  , ">=testString", "<=anotherStr|>MoreText", "<=testString&&!anotherStr", ">=abc"             , "<=anotherOne&&>=text",
        					">=More????"             , "<=*test*"},
        			new int[] {2                    , 2             , 1                       , 1                         , 3                  ,
        					2                       , 1             , 1                       , 0                         , 1                  , 2                   , 2 /*1*/               ,
        					-1                       , 1             , 2                       , 2                         , 3                  , 0                   ,
        					2                       , 2});

        // ensure multiple values work
        assertSearchableAttributeMultiplesWork(docType, principalId, "xmlSearchableAttributeWildcardString",
        		new String[][] { {"testString"}, {"anotherStr"}, {"MoreText"}, {"testString", "anotherStr"}, {"testString", "MoreText"}, {"anotherStr", "MoreText"}, {"testString", "anotherStr", "MoreText"}, {"monkey"}, {"monkey", "giraffe"}, {"monkey", "testString"} },
        			new int[]  {  1,              1,              1,            2,                            2,                          2,                          3,                                        0,          0,                     1                       });

        // Ensure that wildcards work on searchable long attributes, and ensure the string-specific wildcards are not being utilized.
        assertSearchableAttributeWildcardsWork(docType, principalId, "xmlSearchableAttributeWildcardLong",
        		new String[]  {"99??", "*2"       , "!33"         , "<9984", ">=433", "<34", ">=432", ">=34&&<9984", "<100000&&>=20", ">=9984&&<33", "431..<9985",
        					"9999..1", "<432|>432", ">=9000|<=100", "!", ">=-76"},
        			new int[] {-1     , -1          , 2             , 2      , 1     , 1     , 2      , 1           , 3               , 0           , 2 /*1*/    ,
        					-1        , 2          , 2             , -1 , 3});

        // ensure multiple values work
        assertSearchableAttributeMultiplesWork(docType, principalId, "xmlSearchableAttributeWildcardLong",
        		new String[][] { {"9984"}, {"33"}, {"432"}, {"9984", "33"}, {"9984", "432"}, {"33", "432"}, {"9984", "33", "432"}, {"7"}, {"7", "4488"}, {"7", "9984"} },
        			new int[]  {  1,              1,              1,            2,                            2,                          2,                          3,                                        0,          0,                     1                       });

        // Ensure that wildcards work on searchable float attributes, and ensure the string-specific wildcards are not being utilized.
        assertSearchableAttributeWildcardsWork(docType, principalId, "xmlSearchableAttributeWildcardFloat",
        		new String[]  {"38.1???", "!-0.765", "*80*"                , "<80000.65432"   , ">=0"                  , "<-0.763", ">=38.1357", "<38.1358", "<-0.5|>0.5", ">=-0.765&&<=-0.765", ">=38.1358&&<80000.65432",
        					"-50..<50"   , "100..10", "<38.1358|>=38.1357" , ">=123.4567|<0.11", "-1.1..<38.1357&&<3.3"},
        			new int[] {-1        , 2        , -1                     , 2                , 2                     , 1         , 2          , 2         , 3           , 1                   , 0                       ,
        					2           , -1        , 3                     , 2                , 1});

        // ensure multiple values work
        assertSearchableAttributeMultiplesWork(docType, principalId, "xmlSearchableAttributeWildcardFloat",
        		new String[][] { {"38.1357"}, {"80000.65432"}, {"-0.765"}, {"38.1357", "80000.65432"}, {"38.1357", "-0.765"}, {"80000.65432", "-0.765"}, {"38.1357", "80000.65432", "-0.765"}, {"3.1415928"}, {"3.1415928", "4488.0"}, {"3.1415928", "38.1357"} },
        			new int[]  {  1,              1,              1,            2,                            2,                          2,                          3,                                        0,          0,                     1                       });


        // Ensure that wildcards work on searchable datetime attributes, and ensure the string-specific wildcards are not being utilized.
        /* 06/24/2009, 07/08/2010, 12/12/2012 */
        assertSearchableAttributeWildcardsWork(docType, principalId, "xmlSearchableAttributeWildcardDatetime",
        		new String[]  {"??/??/20??"            , "12/12/20*"               , "!07/08/2010"           , ">=06/25/2009", "<07/08/2010", ">=12/12/2012", "<05/06/2011", ">=06/25/2009&&<07/09/2010",
        					">=01/01/2001&&<06/24/2009", "11/29/1990..<12/31/2009"  , "12/13/2100..<08/09/1997",
        					"<06/24/2009|>=12/12/2012" , "<06/25/2009|>=07/09/2010", ">02/31/2011"},
        			new int[] {-1                      , -1                         , 2 /* supports NOT operator*/, 2            , 1            , 1             , 2             , 1                          ,
        					0                          , 1                         , -1                       ,
        					1                          , 2                         , -1});
        
        // ensure multiple values work
        assertSearchableAttributeMultiplesWork(docType, principalId, "xmlSearchableAttributeWildcardDatetime",
        		new String[][] { {"06/24/2009"}, {"07/08/2010"}, {"12/12/2012"}, {"06/24/2009", "07/08/2010"}, {"06/24/2009", "12/12/2012"}, {"07/08/2010", "12/12/2012"}, {"06/24/2009", "07/08/2010", "12/12/2012"}, {"12/20/2012"}, {"12/20/2012", "11/09/2009"}, {"12/20/2012", "12/12/2012"} },
        			new int[]  {  1,              1,              1,            2,                            2,                          2,                          3,                                        0,          0,                     1                       });

    }

    /**
     * A convenience method for testing wildcards on searchable attributes.
     *
     * @param docType The document type containing the attributes.
     * @param principalId The ID of the user performing the search.
     * @param fieldDefKey The name of the field given by the field definition on the searchable attribute.
     * @param searchValues The wildcard-filled search strings to test.
     * @param resultSizes The number of expected documents to be returned by the search; use -1 to indicate that an error should have occurred.
     * @throws Exception
     */
    private void assertSearchableAttributeWildcardsWork(DocumentType docType, String principalId, String fieldDefKey, String[] searchValues,
    		int[] resultSizes) throws Exception {
    	DocumentSearchCriteria.Builder criteria = null;
        DocumentSearchResults results = null;
        DocumentSearchService docSearchService = KEWServiceLocator.getDocumentSearchService();
        for (int i = 0; i < resultSizes.length; i++) {
        	criteria = DocumentSearchCriteria.Builder.create();
        	criteria.setDocumentTypeName(docType.getName());
        	addSearchableAttribute(criteria, fieldDefKey, searchValues[i]);
        	try {
        		results = docSearchService.lookupDocuments(principalId, criteria.build());
        		if (resultSizes[i] < 0) {
        			fail(fieldDefKey + "'s search at loop index " + i + " should have thrown an exception");
        		}
        		if(resultSizes[i] != results.getSearchResults().size()){
        			assertEquals(fieldDefKey + "'s search results at loop index " + i + " returned the wrong number of documents.", resultSizes[i], results.getSearchResults().size());
        		}
        	} catch (Exception ex) {
        		if (resultSizes[i] >= 0) {
                    ex.printStackTrace();
        			fail(fieldDefKey + "'s search at loop index " + i + " should not have thrown an exception");
        		}
        	}
        	GlobalVariables.clear();
        }
    }
    
    /**
     * A convenience method for testing multiple value fields on searchable attributes.
     *
     * @param docType The document type containing the attributes.
     * @param principalId The ID of the user performing the search.
     * @param fieldDefKey The name of the field given by the field definition on the searchable attribute.
     * @param searchValues The wildcard-filled search strings to test.
     * @param resultSizes The number of expected documents to be returned by the search; use -1 to indicate that an error should have occurred.
     * @throws Exception
     */
    private void assertSearchableAttributeMultiplesWork(DocumentType docType, String principalId, String fieldDefKey, String[][] searchValues,
    		int[] resultSizes) throws Exception {
        DocumentSearchCriteria.Builder criteria = null;
        DocumentSearchResults results = null;
        DocumentSearchService docSearchService = KEWServiceLocator.getDocumentSearchService();
        for (int i = 0; i < resultSizes.length; i++) {
            criteria = DocumentSearchCriteria.Builder.create();
        	criteria.setDocumentTypeName(docType.getName());
        	addSearchableAttribute(criteria, fieldDefKey, searchValues[i]);
        	try {
                results = docSearchService.lookupDocuments(principalId, criteria.build());
                if (resultSizes[i] < 0) {
        			fail(fieldDefKey + "'s search at loop index " + i + " should have thrown an exception");
        		}
        		if(resultSizes[i] != results.getSearchResults().size()){
        			assertEquals(fieldDefKey + "'s search results at loop index " + i + " returned the wrong number of documents.", resultSizes[i], results.getSearchResults().size());
        		}
        	} catch (Exception ex) {
        		if (resultSizes[i] >= 0) {
        			fail(fieldDefKey + "'s search at loop index " + i + " should not have thrown an exception");
        		}
        	}
        	GlobalVariables.clear();
        }
    }
    
    
    
    /**
     * Per KULRICE-3681, tests that StandardGenericXMLSearchableAttribute throws no cast class exception when it shouldn't
     */
    @Test
    public void testValidateUserSearchInputsNoCast() {
    	StandardGenericXMLSearchableAttribute searchableAttribute = new StandardGenericXMLSearchableAttribute();
        ExtensionDefinition extensionDefinition = KewApiServiceLocator.getExtensionRepositoryService().getExtensionByName("SearchableAttributeVisible");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
    	Map<String, List<String>> simpleParamMap = new HashMap<String, List<String>>();
    	simpleParamMap.put("givenname", Collections.singletonList("test"));
        criteria.setDocumentAttributeValues(simpleParamMap);
    	List errors = new ArrayList();
    	Exception caughtException = null;
    	try {
    		errors = searchableAttribute.validateDocumentAttributeCriteria(extensionDefinition, criteria.build());
    	} catch (RuntimeException re) {
    		caughtException = re;
    	}
    	assertNull("Found exception "+caughtException, caughtException);
    	assertTrue("Found errors "+errors, (errors.size() == 0));
    	
    	Map<String, List<String>> listParamMap = new HashMap<String, List<String>>();
    	List<String> multipleValues = new ArrayList<String>();
    	multipleValues.add("testone");
    	multipleValues.add("testtwo");
    	listParamMap.put("givenname", multipleValues);
        criteria.setDocumentAttributeValues(listParamMap);
    	errors = new ArrayList();
    	caughtException = null;
    	try {
    		errors = searchableAttribute.validateDocumentAttributeCriteria(extensionDefinition, criteria.build());
    	} catch (RuntimeException re) {
    		caughtException = re;
    	}
    	assertNull("Found exception "+caughtException, caughtException);
    	assertTrue("Found errors "+errors, (errors.size() == 0));
    	
    }
    
    @Test
    public void testSearchableAttributeTrim() {
    	RuleAttribute trimAttribute = KEWServiceLocator.getRuleAttributeService().findByName("TrimSearchableAttribute");
    	assert(trimAttribute.getName().equals("TrimSearchableAttribute"));
    	assert(trimAttribute.getResourceDescriptor().equals("org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute"));
    	assert(trimAttribute.getLabel().equals("Unit111"));
    	assert(trimAttribute.getType().equals("SearchableXmlAttribute"));
    	assert(trimAttribute.getDescription().equals("Unit111"));
    	assert(trimAttribute.getApplicationId().equals("NSTrimSearchableTest"));
    	//System.out.println(trimAttribute.getName());
    }

    @Test
    public void testXmlGeneration() {
        loadXmlFile("testdoc1.xml");
        WorkflowAttributeDefinition searchableDefinition = WorkflowAttributeDefinition.Builder.create("SearchAttribute").build();
        DocumentContentUpdate.Builder documentContentUpdateBuilder = DocumentContentUpdate.Builder.create();
        documentContentUpdateBuilder.getSearchableDefinitions().add(searchableDefinition);
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId("ewestfal"), "SearchDoc", null, documentContentUpdateBuilder.build());
        workflowDocument.route("");
        assertTrue(workflowDocument.isFinal());
        assertEquals(StringUtils.deleteWhitespace("<" + KewApiConstants.SEARCHABLE_CONTENT_ELEMENT + ">" + MockSearchableAttribute.SEARCH_CONTENT + "</" + KewApiConstants.SEARCHABLE_CONTENT_ELEMENT + ">"),
                StringUtils.deleteWhitespace(workflowDocument.getDocumentContent().getSearchableContent()));
    }

    @Test
    public void testAttributeRangeFieldGeneration() {
        String documentTypeName = "SearchDocType";
        DocumentType docType = ((DocumentTypeService)KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE)).findByName(documentTypeName);
        String userNetworkId = "rkirkend";
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(getPrincipalId(userNetworkId), documentTypeName);
        workflowDocument.setTitle("Routing style");
        workflowDocument.route("routing this document.");

        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(documentTypeName);
        criteria.setDateApprovedFrom(new DateTime(2010, 1, 1, 0, 0));
        criteria.setDateApprovedTo(new DateTime(2011, 1, 1, 0, 0));
        String fieldValue = ">= " + DocumentSearchInternalUtils.getDisplayValueWithDateOnly(new Timestamp(TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_VALUE_IN_MILLS));
        addSearchableAttribute(criteria, TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY, fieldValue);

        Map<String, String[]> fields = new DocumentSearchCriteriaTranslatorImpl().translateCriteriaToFields(criteria.build());
        System.err.println(fields);
        String lowerBoundField = KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + KRADConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX + TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY;
        String upperBoundField = KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + TestXMLSearchableAttributeDateTime.SEARCH_STORAGE_KEY;

        assertNotNull(fields.get(lowerBoundField));
        assertNotNull(fields.get(upperBoundField));
        assertNotNull(fields.get(lowerBoundField)[0]);
        assertNull(fields.get(upperBoundField)[0]);

        assertEquals(DocumentSearchInternalUtils.getDisplayValueWithDateOnly(new Timestamp(new DateTime(2007, 3, 15, 0, 0).toDateTime().getMillis())), fields.get(lowerBoundField)[0]);
    }
    
}
