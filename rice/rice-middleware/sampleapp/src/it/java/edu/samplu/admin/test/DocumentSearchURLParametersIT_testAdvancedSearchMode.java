package edu.samplu.admin.test;

import org.junit.Test;
import org.kuali.rice.krad.util.KRADConstants;
import org.openqa.selenium.WebElement;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testAdvancedSearchMode extends DocumentSearchURLParametersITBase {
    @Test
    public void testAdvancedSearchMode() {
        driver.get(getDocSearchURL((KRADConstants.ADVANCED_SEARCH_FIELD + "=YES")));
        WebElement toggle = findModeToggleButton();
        assertSearchDetailMode(toggle, true);
        toggle.click();
        assertSearchDetailMode(findModeToggleButton(), false);
    }
}
