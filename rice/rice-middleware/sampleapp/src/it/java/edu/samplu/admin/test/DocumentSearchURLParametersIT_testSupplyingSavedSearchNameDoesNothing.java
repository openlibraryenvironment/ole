package edu.samplu.admin.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testSupplyingSavedSearchNameDoesNothing extends DocumentSearchURLParametersITBase {
    /**
     * Supplying a saveName does not result in the saved search getting loaded.
     * @throws InterruptedException
     */
    @Test
    public void testSupplyingSavedSearchNameDoesNothing() throws InterruptedException {
        // get the search saved
        driver.get(getDocSearchURL(BASIC_FIELDS));

        driver.get(getDocSearchURL("saveName=testBasicSearchFields_saved_search"));

        Map<String, String> emptyForm = new HashMap<String, String>();
        for (String key: CORE_FIELDS.keySet()) {
            emptyForm.put(key, "");
        }

        assertInputValues(emptyForm);
    }
}
