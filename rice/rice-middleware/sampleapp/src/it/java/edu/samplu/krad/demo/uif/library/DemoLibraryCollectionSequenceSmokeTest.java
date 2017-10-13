/*
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.samplu.krad.demo.uif.library;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DemoLibraryCollectionSequenceSmokeTest extends DemoLibraryBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-CollectionSequence-View
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-CollectionSequence-View";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        navigateToLibraryDemo("Collection Features", "Sequence Column");
    }

    protected void changeSequenceView() throws Exception {
        selectOptionByName("exampleShown","Demo-CollectionSequence-Example2");
        waitForPageToLoad();
        assert(isOptionSelected("exampleShown", "Demo-CollectionSequence-Example2"));
    }

    protected void testCollectionSequenceExamples() throws Exception {
        changeSequenceView();
    }

    private boolean isOptionSelected(String dropDownName, String optionValue) {
        WebElement select = driver.findElement(By.name(dropDownName));
        List<WebElement> options = select.findElements(By.tagName("option"));
        for (WebElement option: options) {
            if (option.getAttribute("selected")!=null) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testCollectionSequenceBookmark() throws Exception {
        testCollectionSequenceExamples();
        passed();
    }

    @Test
    public void testCollectionSequenceNav() throws Exception {
        testCollectionSequenceExamples();
        passed();
    }
}
