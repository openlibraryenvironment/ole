package edu.samplu.admin.test;

import org.junit.Test;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testBasicSearchFields extends DocumentSearchURLParametersITBase {
    @Test
    public void testBasicSearchFields() throws InterruptedException{
        // criteria.initiator=delyea&criteria.docTypeFullName=" + documentTypeName +
        driver.get(getDocSearchURL(BASIC_FIELDS));

        assertInputValues(BASIC_FIELDS);

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
    }

}
