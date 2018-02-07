package edu.samplu.admin.test;

import org.junit.Test;

public class DocumentSearchURLParametersIT_testCriteriaDisabled extends DocumentSearchURLParametersITBase {
    @Test
    public void testCriteriaDisabled() throws InterruptedException{
        driver.get(getDocSearchURL("searchCriteriaEnabled=NO"));
        assertInputPresence(CORE_FIELDS, false);
        driver.get(getDocSearchURL("searchCriteriaEnabled=true"));
        assertInputPresence(CORE_FIELDS, true);
    }

}
