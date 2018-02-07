package edu.samplu.admin.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testAdvancedSearchFieldsAndExecuteSearchWithHiddenCriteria extends DocumentSearchURLParametersITBase {
    @Test
    public void testAdvancedSearchFieldsAndExecuteSearchWithHiddenCriteria() throws InterruptedException {
        // criteria.initiator=delyea&criteria.docTypeFullName=" + documentTypeName +
        Map<String, String> expected = new HashMap<String, String>(BASIC_FIELDS);
        expected.putAll(ADVANCED_FIELDS);

        Map<String, String> values = new HashMap<String, String>(expected);
        values.put("methodToCall", "search");
        values.put("searchCriteriaEnabled", "NO");
        driver.get(getDocSearchURL(values));

        assertInputPresence(expected, false);

        // verify that it attempted the search
        assertTrue(driver.getPageSource().contains("No values match this search"));

        // NOTE: toggling modes re-enables the search criteria
    }
}
