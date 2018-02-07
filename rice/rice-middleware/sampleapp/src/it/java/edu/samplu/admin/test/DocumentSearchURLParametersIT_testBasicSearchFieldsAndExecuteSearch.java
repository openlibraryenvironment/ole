package edu.samplu.admin.test;

import org.junit.Test;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testBasicSearchFieldsAndExecuteSearch extends DocumentSearchURLParametersITBase {
    @Test
    public void testBasicSearchFieldsAndExecuteSearch() throws InterruptedException {
        // criteria.initiator=delyea&criteria.docTypeFullName=" + documentTypeName +
        Map<String, String> fields = new HashMap<String, String>();
        fields.putAll(BASIC_FIELDS);
        fields.put("methodToCall", "search");
        driver.get(getDocSearchURL(fields));

        assertInputValues(BASIC_FIELDS);

        // verify that it attempted the search
        assertTrue(driver.getPageSource().contains("No values match this search"));

        driver.findElement(By.id("toggleAdvancedSearch")).click();

        Map<String, String> expected = new HashMap<String, String>(BASIC_FIELDS);
        for (Map.Entry<String, String> entry: ADVANCED_FIELDS.entrySet()) {
            if (!"isAdvancedSearch".equals(entry.getKey())) {
                expected.put(entry.getKey(), "");
            } else {
                expected.put(entry.getKey(), entry.getValue());
            }
        }
        assertInputValues(expected);

        // I guess switching modes doesn't re-execute the search
        // assertTrue(driver.getPageSource().contains("No values match this search"));
    }
}
