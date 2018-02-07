/**
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
package edu.samplu.mainmenu.test;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.util.KRADConstants;
import org.openqa.selenium.By;

import java.io.File;

/**
 * Tests the Notes and Attachments in the People Flow screen.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PeopleFlowCreateNewNotesAndAttachmentsSmokeTest extends PeopleFlowCreateNewSTJUnitBase {

    /**
     * Provider of the temporary folder.
     */
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private static final String NOTES_AND_ATTACHMENTS_PREFIX = UifPropertyPaths.NEW_COLLECTION_LINES
            + "['"
            + KRADConstants.DOCUMENT_PROPERTY_NAME
            + "."
            + KRADConstants.NOTES_PROPERTY_NAME
            + "']";

    @Override
    public String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    public String getTestUrl() {
        return BOOKMARK_URL;
    }

    /**
     * Tests adding both the required note and an optional attachment.
     *
     * @throws Exception when an error is encountered in the test
     */
    @Test
    public void testPeopleFlowCreateNewNotesAndAttachments_DefaultAttachment() throws Exception {
        navigateToPeopleFlowNotesAndAttachments();

        waitAndTypeByName(NOTES_AND_ATTACHMENTS_PREFIX + "." + KRADConstants.NOTE_TEXT_PROPERTY_NAME,
                "Attachment_Note");
        waitAndAddAttachment("attachment.txt", "Testing123");
        waitAndClick(By.cssSelector("button[title='Add a Note']"));

        Thread.sleep(2000);

        assertTextPresent("Attachment_Note");
        assertTextPresent("attachment.txt");
        assertTextNotPresent("Note Text is a required field.");
    }

    /**
     * Tests adding just the required note.
     *
     * @throws Exception when an error is encountered in the test
     */
    @Test
    public void testPeopleFlowCreateNewNotesAndAttachments_DefaultNoAttachment() throws Exception {
        navigateToPeopleFlowNotesAndAttachments();

        waitAndTypeByName(NOTES_AND_ATTACHMENTS_PREFIX + "." + KRADConstants.NOTE_TEXT_PROPERTY_NAME,
                "Attachment_Note");
        waitAndClick(By.cssSelector("button[title='Add a Note']"));

        Thread.sleep(2000);

        assertTextPresent("Attachment_Note");
        assertTextNotPresent("Note Text is a required field.");
    }

    /**
     * Tests adding just the optional attachment, which should result in an error.
     *
     * @throws Exception when an error is encountered in the test
     */
    @Test
    public void testPeopleFlowCreateNewNotesAndAttachments_NoNoteText() throws Exception {
        navigateToPeopleFlowNotesAndAttachments();

        waitAndAddAttachment("attachment.txt", "Testing123");
        waitAndClick(By.cssSelector("button[title='Add a Note']"));

        Thread.sleep(2000);

        assertTextPresent("Note Text is a required field.");
        assertTextNotPresent("attachment.txt");
    }

    private void navigateToPeopleFlowNotesAndAttachments() throws Exception {
        selectFrameIframePortlet();

        waitAndClickByLinkText("Create New");
        waitForElementPresent(
                "div[data-header_for='PeopleFlow-MaintenanceView'] div[data-label='Document Number'] > span");

        waitAndClickByLinkText("Notes and Attachments (0)");
    }

    private void waitAndAddAttachment(String fileName, String content) throws Exception {
        File file = temporaryFolder.newFile(fileName);
        FileUtils.writeStringToFile(file, content);
        String path = file.getAbsolutePath().toString();
        driver.findElement(By.name("attachmentFile")).sendKeys(path);
    }
}
