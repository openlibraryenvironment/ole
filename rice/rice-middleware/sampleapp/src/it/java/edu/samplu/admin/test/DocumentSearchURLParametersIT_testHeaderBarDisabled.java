package edu.samplu.admin.test;

import org.junit.Test;
import org.openqa.selenium.By;

import static com.thoughtworks.selenium.SeleneseTestBase.assertFalse;
import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchURLParametersIT_testHeaderBarDisabled extends DocumentSearchURLParametersITBase {
    @Test
    public void testHeaderBarDisabled() throws InterruptedException{
        driver.get(getDocSearchURL("headerBarEnabled=false"));
        assertTrue(driver.findElements(By.id("headerarea-small")).isEmpty());
        assertInputPresence(CORE_FIELDS, true);
        driver.get(getDocSearchURL("headerBarEnabled=true"));
        assertFalse(driver.findElements(By.id("headerarea-small")).isEmpty());
        assertInputPresence(CORE_FIELDS, true);
    }
}
