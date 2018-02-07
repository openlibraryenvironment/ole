package edu.samplu.admin.test;

import org.junit.Test;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testAdvancedSearchFieldsAndExecuteSearch extends DocumentSearchURLParametersITBase {
    @Test
    public void testAdvancedSearchFieldsAndExecuteSearch() throws InterruptedException{
        // criteria.initiator=delyea&criteria.docTypeFullName=" + documentTypeName +
        Map<String, String> expected = new HashMap<String, String>(BASIC_FIELDS);
        expected.putAll(ADVANCED_FIELDS);

        Map<String, String> values = new HashMap<String, String>(expected);
        values.put("methodToCall", "search");
        driver.get(getDocSearchURL(values));

        assertInputValues(expected);

        // verify that it attempted the search
        assertTrue(driver.getPageSource().contains("No values match this search"));

        driver.findElement(By.id("toggleAdvancedSearch")).click();

        assertInputValues(BASIC_FIELDS);
    }
}
