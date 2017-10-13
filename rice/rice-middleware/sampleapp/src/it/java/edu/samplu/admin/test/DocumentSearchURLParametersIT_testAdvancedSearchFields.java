package edu.samplu.admin.test;

import org.junit.Test;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testAdvancedSearchFields extends DocumentSearchURLParametersITBase {
    @Test
    public void testAdvancedSearchFields() throws InterruptedException{
        // criteria.initiator=delyea&criteria.docTypeFullName=" + documentTypeName +
        Map<String, String> values = new HashMap<String, String>(BASIC_FIELDS);
        values.putAll(ADVANCED_FIELDS);
        driver.get(getDocSearchURL(values));

        assertInputValues(values);

        driver.findElement(By.id("toggleAdvancedSearch")).click();

        assertInputValues(BASIC_FIELDS);
    }
}
