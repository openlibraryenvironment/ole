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
package org.kuali.rice.kew.docsearch.service.impl

import static org.junit.Assert.assertEquals

import java.util.List;

import org.junit.Before
import org.junit.Test
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils
import org.kuali.rice.kew.useroptions.UserOptions
import org.kuali.rice.kew.useroptions.UserOptionsServiceImpl
import org.kuali.rice.kew.useroptions.dao.UserOptionsDAO


/**
 * Unit tests DocumentSearchCriteria saving behavior of DocumentSearchServiceImpl
 */
class DocSearchSavingTest {
    // mock out UserOptionsDAO - just save in memory
    private def mockUserOptionsDAO = new UserOptionsDAO() {
        def options = new HashMap<String, String>()
        Collection<UserOptions> findByWorkflowUser(String principalId) {
            options.collect {
                def opt = new UserOptions()
                opt.optionId = it.key
                opt.optionVal = it.value
                opt
            }
        }
        List<UserOptions> findByUserQualified(String principalId, String likeString) {
            def prefix = likeString.replaceAll(/%$/, '')
            options.findResults { k, v ->
                if (k.startsWith(prefix)) {
                    def opt = new UserOptions()
                    opt.optionId = k
                    opt.optionVal = v
                    opt
                }
            }
        }
        void deleteByUserQualified(String principalId, String likeString) {
            def prefix = likeString.replaceAll(/%$/, '')
            options = options.findAll { k, v -> !k.startsWith(prefix) }
        }
        void save(UserOptions userOptions) {
            options.put(userOptions.optionId, userOptions.optionVal)
        }
        void save(Collection<UserOptions> userOptions) {
            userOptions.each { save(it) }
        }
        void deleteUserOptions(UserOptions userOptions) {
            options.remove(userOptions.optionId)
        }
        UserOptions findByOptionId(String optionId, String principalId) {
            if (options[optionId]) {
              def opt = new UserOptions()
              opt.optionId = optionId
              opt.optionVal = options[optionId]
              opt
            } else {
              null
            }
        }
        Collection<UserOptions> findByOptionValue(String optionId, String optionValue) { null }
        Long getNewOptionIdForActionList() { 0 }
        
        List<UserOptions> findEmailUserOptionsByType(String emailSetting) { null }
    }

    private def userOptionsService = new UserOptionsServiceImpl()
    private def docSearchService = new DocumentSearchServiceImpl() {
        public void saveSearch(String principalId, DocumentSearchCriteria criteria) {
            super.saveSearch(principalId, criteria)
        }
    }

    @Before
    void init() {
        userOptionsService.setUserOptionsDAO(mockUserOptionsDAO)
        docSearchService.setUserOptionsService(userOptionsService)
    }


    @Test
    void testConsumesExceptions() {
        // assuming a null criteria will cause an NPE
        docSearchService.saveSearch("princ", null)
    }

    @Test
    void testUnnamedDocSearch() {
        def princ = "not blank" // mocked...

        def allUserOptions_before = userOptionsService.findByWorkflowUser(princ)

        assertEquals(0, allUserOptions_before.size())

        def c1 = saveSearch(princ)

        def allUserOptions_after = userOptionsService.findByWorkflowUser(princ)

        // saves the "last doc search criteria"
        // and a pointer to the "last doc search criteria"
        assertEquals(allUserOptions_before.size() + 2, allUserOptions_after.size())

        assertEquals("DocSearch.LastSearch.Holding0", userOptionsService.findByOptionId("DocSearch.LastSearch.Order", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c1), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding0", princ).optionVal)

        // 2nd search

        def c2 = saveSearch(princ)

        allUserOptions_after = userOptionsService.findByWorkflowUser(princ)

        // 1 more user option
        assertEquals(allUserOptions_before.size() + 3, allUserOptions_after.size())
        assertEquals("DocSearch.LastSearch.Holding1,DocSearch.LastSearch.Holding0", userOptionsService.findByOptionId("DocSearch.LastSearch.Order", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c1), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding0", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c2), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding1", princ).optionVal)

        docSearchService.clearNamedSearches(princ)
        assertEquals(0, userOptionsService.findByWorkflowUser(princ).size())
    }

    @Test
    void testNamedDocSearch() {
        def princ = "not blank" // mocked...
        def allUserOptions_before = userOptionsService.findByWorkflowUser(princ)

        assertEquals(0, allUserOptions_before.size())

        def c1 = saveSearch(princ, "save1")

        def allUserOptions_after = userOptionsService.findByWorkflowUser(princ)
        assertEquals(allUserOptions_before.size() + 1, allUserOptions_after.size())
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c1), userOptionsService.findByOptionId("DocSearch.NamedSearch." + c1.getSaveName(), princ).optionVal)

        // 2nd search

        def c2 = saveSearch(princ, "save2")

        allUserOptions_after = userOptionsService.findByWorkflowUser(princ)
        // saves a second named search
        assertEquals(allUserOptions_before.size() + 2, allUserOptions_after.size())
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c2), userOptionsService.findByOptionId("DocSearch.NamedSearch." + c2.getSaveName(), princ).optionVal)

        docSearchService.clearNamedSearches(princ)
        assertEquals(0, userOptionsService.findByWorkflowUser(princ).size())
    }

    @Test
    void testSavedSearchOrdering() {
        def MAX_SEARCH_ITEMS = 5 // searches start wrapping after this...
        def princ = "not blank"

        def allUserOptions_before = userOptionsService.findByWorkflowUser(princ)

        def c1 = saveSearch(princ)
        def c2 = saveSearch(princ)
        def c3 = saveSearch(princ)
        def c4 = saveSearch(princ)
        def c5 = saveSearch(princ)

        def allUserOptions_after = userOptionsService.findByWorkflowUser(princ)
        assertEquals(allUserOptions_before.size() + 5 + 1, allUserOptions_after.size())
        assertEquals("DocSearch.LastSearch.Holding4,DocSearch.LastSearch.Holding3,DocSearch.LastSearch.Holding2,DocSearch.LastSearch.Holding1,DocSearch.LastSearch.Holding0", userOptionsService.findByOptionId("DocSearch.LastSearch.Order", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c5), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding4", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c4), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding3", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c3), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding2", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c2), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding1", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c1), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding0", princ).optionVal)

        // now add 1 more

        def c6 = saveSearch(princ)


        allUserOptions_after = userOptionsService.findByWorkflowUser(princ)

        // order should have wrapped around, now Holding0 is first, and contains c6 criteria
        // still 5 entries
        assertEquals(allUserOptions_before.size() + 5 + 1, allUserOptions_after.size())
        assertEquals("DocSearch.LastSearch.Holding0,DocSearch.LastSearch.Holding4,DocSearch.LastSearch.Holding3,DocSearch.LastSearch.Holding2,DocSearch.LastSearch.Holding1", userOptionsService.findByOptionId("DocSearch.LastSearch.Order", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c6), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding0", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c5), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding4", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c4), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding3", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c3), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding2", princ).optionVal)
        assertEquals(DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c2), userOptionsService.findByOptionId("DocSearch.LastSearch.Holding1", princ).optionVal)

        docSearchService.clearNamedSearches(princ)
        assertEquals(0, userOptionsService.findByWorkflowUser(princ).size())
    }

    protected DocumentSearchCriteria saveSearch(String princ, String name = null) {
        def c = DocumentSearchCriteriaTest.create(name)
        docSearchService.saveSearch(princ, c)
        return c
    }
}
