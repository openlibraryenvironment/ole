package edu.samplu.admin.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testBasicSearchFieldsAndExecuteSearchWithHiddenCriteria extends DocumentSearchURLParametersITBase {
    @Test
    public void testBasicSearchFieldsAndExecuteSearchWithHiddenCriteria() throws InterruptedException {
        // criteria.initiator=delyea&criteria.docTypeFullName=" + documentTypeName +
        Map<String, String> fields = new HashMap<String, String>();
        fields.putAll(BASIC_FIELDS);
        fields.put("methodToCall", "search");
        fields.put("searchCriteriaEnabled", "NO");
        driver.get(getDocSearchURL(fields));

        assertInputPresence(BASIC_FIELDS, false);

        // verify that it attempted the search
        assertTrue(driver.getPageSource().contains("No values match this search"));

        // NOTE: toggling modes re-enables the search criteria
    }
}
