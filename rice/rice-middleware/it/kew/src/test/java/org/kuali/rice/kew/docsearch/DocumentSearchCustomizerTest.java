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
import org.junit.Test;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeDataType;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.framework.document.attribute.SearchableAttribute;
import org.kuali.rice.kew.framework.document.search.DocumentSearchCustomizerBase;
import org.kuali.rice.kew.framework.document.search.DocumentSearchResultValue;
import org.kuali.rice.kew.framework.document.search.DocumentSearchResultValues;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * An integration test for the DocumentSearchCustomizer class.  Includes tests on various aspects of the customzations
 * that class provides, attempting to use the high-level document search apis to exercise it.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchCustomizerTest extends KEWTestCase {

    @Override
	protected void loadTestData() throws Exception {
    	loadXmlFile("DocumentSearchCustomizerTest.xml");
    }

    @Test
    public void testCustomizeCriteria() throws Exception {

        String ewestfal = getPrincipalIdForName("ewestfal");
        DocumentSearchCriteria.Builder builder = DocumentSearchCriteria.Builder.create();

        // first check a full doc search, should return no results
        DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(ewestfal, builder.build());
        assertTrue(results.getSearchResults().isEmpty());
        assertFalse(results.getCriteria().getDocumentStatuses().contains(DocumentStatus.FINAL));

        // now check a document search against the "DocumentSearchCustomizerTest" document type, it is configured with the
        // CustomizeCriteria customizer
        builder.setDocumentTypeName("DocumentSearchCustomizerTest");

        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(ewestfal, builder.build());
        assertTrue(results.getSearchResults().isEmpty());
        DocumentSearchCriteria resultCriteria = results.getCriteria();
        assertTrue("Document Statuses should have contained FINAL, instead contains: " + resultCriteria
                .getDocumentStatuses(), resultCriteria.getDocumentStatuses().contains(DocumentStatus.FINAL));

        // now route an instance of the CustomizeCriteria document type to FINAL
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"),
                "DocumentSearchCustomizerTest");
        document.route("");
        assertTrue(document.isFinal());

        // now run another search, we should get back one result this time
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(ewestfal, builder.build());
        assertEquals(1, results.getSearchResults().size());
        assertEquals(document.getDocumentId(), results.getSearchResults().get(0).getDocument().getDocumentId());
    }

    @Test
    public void testCustomizeClearCriteria() throws Exception {

        // grab a couple of document types, the first is the TestDocumentType which has no DocumentSearchCustomizer
        // configured on it, the second in our Document Type for this integration test which is configured with the
        // Customizer

        DocumentType testDocumentType = KEWServiceLocator.getDocumentTypeService().findByName("TestDocumentType");
        assertNotNull(testDocumentType);
        DocumentType customizedDocumentType = KEWServiceLocator.getDocumentTypeService().findByName("DocumentSearchCustomizerTest");
        assertNotNull(customizedDocumentType);

        // first set document id and application document id on a criteria and clear it using the TestDocumentType, it
        // should clear out both

        DocumentSearchCriteria.Builder builder = DocumentSearchCriteria.Builder.create();
        builder.setDocumentId("12345");
        builder.setApplicationDocumentId("54321");
        DocumentSearchCriteria clearedCriteria = KEWServiceLocator.getDocumentSearchService().clearCriteria(testDocumentType, builder.build());
        assertNull(clearedCriteria.getDocumentId());
        assertNull(clearedCriteria.getApplicationDocumentId());

        // now clear the same criteria with the customized document type, it should clear out the document id but
        // preserve the application document id

        clearedCriteria = KEWServiceLocator.getDocumentSearchService().clearCriteria(customizedDocumentType, builder.build());
        assertNull(clearedCriteria.getDocumentId());
        assertEquals("54321", clearedCriteria.getApplicationDocumentId());
    }

    @Test
    public void testCustomizeResults() throws Exception {

        // route an instance of the CustomizeCriteria document type to FINAL

        String ewestfal = getPrincipalIdForName("ewestfal");
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"),
                "DocumentSearchCustomizerTest");
        document.route("");
        assertTrue(document.isFinal());

        // check that the document attributes get indexed properly

        List<String> attributeValues = KewApiServiceLocator.getWorkflowDocumentService().getSearchableAttributeStringValuesByKey(document.getDocumentId(), "myAttribute");
        assertEquals(1, attributeValues.size());
        assertEquals("myValue", attributeValues.get(0));
        attributeValues = KewApiServiceLocator.getWorkflowDocumentService().getSearchableAttributeStringValuesByKey(document.getDocumentId(), "myMultiValuedAttribute");
        assertEquals(2, attributeValues.size());
        assertTrue(attributeValues.contains("value1"));
        assertTrue(attributeValues.contains("value2"));

        DocumentSearchCriteria.Builder builder = DocumentSearchCriteria.Builder.create();
        DocumentSearchResults results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(ewestfal, builder.build());
        assertEquals(1, results.getSearchResults().size());
        DocumentSearchResult result = results.getSearchResults().get(0);

        // TODO - the below assertions really should work, but they currently don't.  Currently, unless you pass the
        // specific document type as one of the criteria the document attributes are not returned with the search
        // There should at least be an option on the search api to enable the returning of document attributes from
        // the search API - see https://jira.kuali.org/browse/KULRICE-6764
        /*assertEquals(1, result.getDocumentAttributes().size());
        DocumentAttribute attribute = result.getDocumentAttributes().get(0);
        assertEquals("myAttribute", attribute.getName());
        assertEquals("myValue", attribute.getValue());
        assertEquals(DocumentAttributeDataType.STRING, attribute.getDataType());*/

        // now do a document search targeting the specific customizer document type, the result should be customized
        // and the "myAttribute" attribute should have a customized value of "myCustomizedValue", also the
        // "myMultiValuedAttribute" should now only have a single value of "value0"
        
        builder.setDocumentTypeName("DocumentSearchCustomizerTest");
        results = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(ewestfal, builder.build());
        assertEquals(1, results.getSearchResults().size());
        result = results.getSearchResults().get(0);
        assertEquals(3, result.getDocumentAttributes().size());
        for (DocumentAttribute attribute : result.getDocumentAttributes()) {
            if (attribute.getName().equals("myAttribute")) {
                assertEquals("myAttribute", attribute.getName());
                assertEquals("myCustomizedValue", attribute.getValue());
                assertEquals(DocumentAttributeDataType.STRING, attribute.getDataType());
            } else if (attribute.getName().equals("myMultiValuedAttribute")) {
                assertEquals("myMultiValuedAttribute", attribute.getName());
                assertEquals("value0", attribute.getValue());
                assertEquals(DocumentAttributeDataType.STRING, attribute.getDataType());
            } else if (attribute.getName().equals("criteriaUserId")) {
                assertEquals("criteriaUserId", attribute.getName());
                assertEquals(ewestfal, attribute.getValue());
            }else {
                fail("Encountered an attribute name which i didn't understand: " + attribute.getName());
            }
        }
    }

    /**
     * An implementation of a DocumentSearchCustomizer which does some simple customizations to allow us to test that
     * the customizer is functioning properly.
     */
    public static final class Customizer extends DocumentSearchCustomizerBase {

        @Override
        public DocumentSearchCriteria customizeCriteria(DocumentSearchCriteria documentSearchCriteria) {
            DocumentSearchCriteria.Builder builder = DocumentSearchCriteria.Builder.create(documentSearchCriteria);
            builder.setDocumentStatuses(Collections.singletonList(DocumentStatus.FINAL));
            return builder.build();
        }
        @Override
        public boolean isCustomizeCriteriaEnabled(String documentTypeName) {
            return true;
        }

        @Override
        public DocumentSearchCriteria customizeClearCriteria(DocumentSearchCriteria documentSearchCriteria) {
            // preserver applicationDocumentId on clear, but clear out everything else
            DocumentSearchCriteria.Builder builder = DocumentSearchCriteria.Builder.create();
            builder.setApplicationDocumentId(documentSearchCriteria.getApplicationDocumentId());
            return builder.build();
        }
        @Override
        public boolean isCustomizeClearCriteriaEnabled(String documentTypeName) {
            return true;
        }

        @Override
        public DocumentSearchResultValues customizeResults(DocumentSearchCriteria documentSearchCriteria,
                List<DocumentSearchResult> defaultResults) {
            if (defaultResults.size() == 1) {
                assertEquals(1, defaultResults.size());
                DocumentSearchResultValues.Builder valuesBuilder = DocumentSearchResultValues.Builder.create();

                DocumentSearchResultValue.Builder resultValueBuilder = DocumentSearchResultValue.Builder.create(defaultResults.get(0).getDocument().getDocumentId());
                resultValueBuilder.getDocumentAttributes().add(DocumentAttributeFactory.loadContractIntoBuilder(DocumentAttributeFactory.createStringAttribute("myAttribute", "myCustomizedValue")));
                resultValueBuilder.getDocumentAttributes().add(DocumentAttributeFactory.loadContractIntoBuilder(DocumentAttributeFactory.createStringAttribute("myMultiValuedAttribute", "value0")));

                // Return if principal id was foudn in criteria
                if(StringUtils.isNotBlank(documentSearchCriteria.getDocSearchUserId())) {
                     resultValueBuilder.getDocumentAttributes().add(DocumentAttributeFactory.loadContractIntoBuilder(DocumentAttributeFactory.createStringAttribute("criteriaUserId", documentSearchCriteria.getDocSearchUserId())));
                }

                valuesBuilder.getResultValues().add(resultValueBuilder);
                return valuesBuilder.build();
            } else {
                return null;
            }
        }
        @Override
        public boolean isCustomizeResultsEnabled(String documentTypeName) {
            return true;
        }
        
    }

    public static final class CustomSearchAttribute implements SearchableAttribute {
        @Override
        public String generateSearchContent(ExtensionDefinition extensionDefinition,
                String documentTypeName,
                WorkflowAttributeDefinition attributeDefinition) {
            return null;
        }
        @Override
        public List<DocumentAttribute> extractDocumentAttributes(ExtensionDefinition extensionDefinition,
                DocumentWithContent documentWithContent) {
            List<DocumentAttribute> attributes = new ArrayList<DocumentAttribute>();
            attributes.add(DocumentAttributeFactory.createStringAttribute("myAttribute", "myValue"));
            attributes.add(DocumentAttributeFactory.createStringAttribute("myMultiValuedAttribute", "value1"));
            attributes.add(DocumentAttributeFactory.createStringAttribute("myMultiValuedAttribute", "value2"));
            attributes.add(DocumentAttributeFactory.createStringAttribute("criteriaUserId", "blank"));
            return attributes;
        }
        @Override
        public List<RemotableAttributeField> getSearchFields(ExtensionDefinition extensionDefinition,
                String documentTypeName) {
            List<RemotableAttributeField> searchFields = new ArrayList<RemotableAttributeField>();
            RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create("myAttribute");
            searchFields.add(builder.build());
            builder = RemotableAttributeField.Builder.create("myMultiValuedAttribute");
            searchFields.add(builder.build());
            builder = RemotableAttributeField.Builder.create("criteriaUserId");
            searchFields.add(builder.build());
            return searchFields;
        }
        @Override
        public List<RemotableAttributeError> validateDocumentAttributeCriteria(ExtensionDefinition extensionDefinition,
                DocumentSearchCriteria documentSearchCriteria) {
            return null;
        }
    }

}
