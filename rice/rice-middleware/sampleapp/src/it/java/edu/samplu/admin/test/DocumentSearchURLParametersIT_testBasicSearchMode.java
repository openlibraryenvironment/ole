package edu.samplu.admin.test;

import org.junit.Test;
import org.openqa.selenium.WebElement;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testBasicSearchMode extends DocumentSearchURLParametersITBase{
    @Test
    public void testBasicSearchMode() throws InterruptedException{
        driver.get(getDocSearchURL(""));
        WebElement toggle = findModeToggleButton();
        assertSearchDetailMode(toggle, false);
        toggle.click();
        assertSearchDetailMode(findModeToggleButton(), true);
    }
}
