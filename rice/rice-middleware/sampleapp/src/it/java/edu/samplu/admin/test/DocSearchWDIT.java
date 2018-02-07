/**
 * Copyright 2005-2011 The Kuali Foundation
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

package edu.samplu.admin.test;

import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocSearchWDIT extends WebDriverLegacyITBase {

    String docId;
    String parentName;

    @Override
    public void fail(String message) {
        Assert.fail(message);
    }

    @Override
    public String getTestUrl(){
        return ITUtil.PORTAL;
    }
    
    public void createAndSaveDoc() throws Exception{
        waitForTitleToEqualKualiPortalIndex();
        waitAndClickByLinkText("Administration");
        waitForTitleToEqualKualiPortalIndex();
        waitAndClickByLinkText("Document Type");
        waitForTitleToEqualKualiPortalIndex();
        selectFrame("iframeportlet");
        waitAndClickByXpath("//img[contains(@alt,'create new')]");
        waitForPageToLoad();
        Thread.sleep(2000);
        assertElementPresentByXpath("//*[@name='methodToCall.route' and @alt='submit']","save button does not exist on the page");
        waitForElementPresentByXpath("//div[@id='headerarea']/div/table/tbody/tr[1]/td[1]");
        docId = driver.findElement(By.xpath("//div[@id='headerarea']/div/table/tbody/tr[1]/td[1]")).getText();
        waitAndTypeByXpath("//input[@id='document.documentHeader.documentDescription']", "Creating new Document Type");
        String parentDocType = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.kew.doctype.bo.DocumentType!!).(((name:document.newMaintainableObject.parentDocType.name,documentTypeId:document.newMaintainableObject.docTypeParentId,))).((`document.newMaintainableObject.parentDocType.name:name,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;" + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClickByXpath(parentDocType);
        waitForPageToLoad();
        //Thread.sleep(2000);
        waitAndClickByXpath("//input[@name='methodToCall.search' and @value='search']");
        waitForPageToLoad();
        parentName= driver.findElement(By.xpath("//table[@id='row']/tbody/tr[1]/td[3]")).getText();
        waitAndClickByLinkText("return value");
        String docTypeName = "TestDocType " + ITUtil.DTS;
        waitForElementPresentByXpath("//input[@id='document.newMaintainableObject.name']");
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.name']", docTypeName);
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.unresolvedDocHandlerUrl']", "${kr.url}/maintenance.do?methodToCall=docHandler");
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.actualNotificationFromAddress']", "NFA");
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.label']", "TestDocument Label");
        waitAndTypeByXpath("//input[@id='document.newMaintainableObject.unresolvedHelpDefinitionUrl']", "default.htm?turl=WordDocuments%2Fdocumenttype.htm");
        waitAndClickByXpath("//input[@name='methodToCall.save' and @alt='save']");
        // TODO wait for save confirmation
        //checkForIncidentReport();
        selectTopFrame();
    }
    @Test
    public void testBasicDocSearch() throws Exception {
        createAndSaveDoc();
        waitAndClickByXpath("//a/img[@alt='doc search']");
        waitForPageToLoad();
        selectFrame("iframeportlet");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='detailed search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='superuser search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='clear saved searches search']");
        waitAndTypeByName("documentTypeName", parentName);
        waitAndTypeByName("initiatorPrincipalName", "admin");
        waitAndTypeByName("documentId", docId);
        //waitAndTypeByName("rangeLowerBoundKeyPrefix_dateCreated", "10/01/2010");
        //waitAndTypeByName("dateCreated", "10/13/2010");
        waitAndClickByXpath("//input[@name='methodToCall.search' and @alt='search']");
        assertEquals(docId, waitFor(By.xpath("//table[@id='row']/tbody/tr[1]/td[1]/a")).getText());
        //Thread.sleep(2000);
        waitAndClickByXpath("//input[@name='methodToCall.clearValues' and @alt='clear']");
        assertEquals("", driver.findElement(By.xpath("//input[@name='documentTypeName']")).getAttribute("value"));
        assertEquals("", driver.findElement(By.xpath("//input[@name='initiatorPrincipalName']")).getAttribute("value"));
        assertEquals("", driver.findElement(By.xpath("//input[@name='documentId']")).getAttribute("value"));
        assertEquals("", driver.findElement(By.xpath("//input[@name='rangeLowerBoundKeyPrefix_dateCreated']")).getAttribute("value"));
        assertEquals("", driver.findElement(By.xpath("//input[@name='dateCreated']")).getAttribute("value"));
        waitAndClickByXpath("//a[@title='cancel']");
        waitForPageToLoad();
        passed();
    }
    
    @Ignore // TODO fix
    @Test
    public void testDetailedDocSearch() throws Exception{
        //createAndSaveDoc();
        waitAndClickByXpath("//a/img[@alt='doc search']");
        waitForPageToLoad();
        selectFrame("iframeportlet");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='detailed search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='superuser search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='clear saved searches search']");
        waitAndClickByXpath("//div[@class='lookupcreatenew']/input[@alt='detailed search']");
        waitForPageToLoad();
        assertElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='basic search']");
        //waitAndTypeByName("documentTypeName", parentName);
        waitAndTypeByName("initiatorPrincipalName", "admin");
        //waitAndTypeByName("documentId", docId);
        //waitAndTypeByName("rangeLowerBoundKeyPrefix_dateCreated", "10/01/2010");
        //waitAndTypeByName("dateCreated", "10/13/2010");
        assertElementPresentByName("approverPrincipalName", "Approver input field is not there in the detailed search");
        //waitAndTypeByName("approverPrincipalName", "director");
        assertElementPresentByName("viewerPrincipalName", "Viewer input field is not there in the detailed search");
        //waitAndTypeByName("viewerPrincipalName", "superviser");
        assertElementPresentByXpath("//select[@id='statusCode']", "Document Status select field is not there in the detailed search");
        selectByXpath("//select[@id='statusCode']", "- SAVED");
        waitAndClickByXpath("//input[@name='methodToCall.search' and @alt='search']");
        waitForPageToLoad();
        assertTrue(driver.findElement(By.id("row")).getText().contains("SAVED"));
        assertElementPresentByXpath("//table[@id='row']/tbody/tr[1]/td[contains(a,'admin')]");
        //Thread.sleep(2000);
        waitAndClickByXpath("//input[@name='methodToCall.clearValues' and @alt='clear']");
        //assertEquals("", driver.findElement(By.xpath("//input[@name='documentTypeName']")).waitAndGetAttribute("value"));
        assertEquals("", driver.findElement(By.xpath("//input[@name='initiatorPrincipalName']")).getAttribute("value"));
        //assertEquals("", driver.findElement(By.xpath("//input[@name='documentId']")).waitAndGetAttribute("value"));
        //assertEquals("", driver.findElement(By.xpath("//input[@name='rangeLowerBoundKeyPrefix_dateCreated']")).waitAndGetAttribute("value"));
        //assertEquals("", driver.findElement(By.xpath("//input[@name='dateCreated']")).waitAndGetAttribute("value"));
        waitAndClickByXpath("//a[@title='cancel']");
        waitForPageToLoad();
        passed();
    }
    
    @Test
    public void testSuperUserSearch() throws Exception{
        //createAndSaveDoc();
        waitAndClickByXpath("//a/img[@alt='doc search']");
        waitForPageToLoad();
        selectFrame("iframeportlet");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='detailed search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='superuser search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='clear saved searches search']");
        waitAndClickByXpath("//div[@class='lookupcreatenew']/input[@alt='superuser search']");
        waitForPageToLoad();
        assertElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='non-superuser search']");
        waitAndClickByXpath("//input[@name='methodToCall.search' and @alt='search']");
        waitForPageToLoad();
        waitAndClickByXpath("//table[@id='row']/tbody/tr[1]/td[1]/a");
        selectTopFrame();
        Thread.sleep(3000);
        switchToWindow("Kuali :: Superuser Document Service");
        waitForPageToLoad();
        //Thread.sleep(4000);

        waitAndClickByXpath("//input[@src='images/buttonsmall_complete.gif']");
        waitForPageToLoad();
        assertElementPresentByName("methodToCall.approve","approve button does not exist on the page");
        assertElementPresentByName("methodToCall.disapprove","disapprove button does not exist on the page");
        assertElementPresentByName("methodToCall.cancel","cancel button does not exist on the page");
        waitAndClickByName("methodToCall.approve","approve button does not exist on the page");
        waitForPageToLoad();
        waitAndClickByXpath("//a[@href='DocumentSearch.do']/img[@alt='cancel']");
        waitForPageToLoad();
        waitAndClickByXpath("//input[@name='methodToCall.search' and @alt='search']");
        waitForPageToLoad();
        assertEquals("FINAL", driver.findElement(By.xpath("//table[@id='row']/tbody/tr[1]/td[4]")).getText());
        passed();
    }
    
    @Test
    public void testClearSavedSearches() throws Exception{
        waitAndClickByXpath("//a/img[@alt='doc search']");
        waitForPageToLoad();
        selectFrame("iframeportlet");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='detailed search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='superuser search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='clear saved searches search']");
        waitAndClickByXpath("//div[@class='lookupcreatenew']/input[@alt='clear saved searches search']");
        waitForPageToLoad();
        WebElement select1 = driver.findElement(By.xpath("//select[@id='savedSearchToLoadAndExecute']"));
        List<WebElement> options = select1.findElements(By.tagName("option"));
        int count= options.size();
        assertEquals(5,count);
        passed();
    }
    
    @Test
    public void testAjaxPageReload() throws Exception{
        waitAndClickByXpath("//a/img[@alt='doc search']");
        waitForPageToLoad();
        selectFrame("iframeportlet");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='detailed search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='superuser search']");
        waitForElementPresentByXpath("//div[@class='lookupcreatenew']/input[@alt='clear saved searches search']");
        waitForElementPresentByXpath("//select[@id='savedSearchToLoadAndExecute']");
        waitAndTypeByName("documentTypeName", "KualiNotification");
        Thread.sleep(2000);
        fireEvent("documentTypeName", "blur");
        Thread.sleep(1000);
        assertElementPresentByName("documentAttribute.notificationContentType");
        assertElementPresentByName("documentAttribute.notificationChannel");
        assertElementPresentByName("documentAttribute.notificationProducer");
        assertElementPresentByName("documentAttribute.notificationPriority");
        assertElementPresentByName("documentAttribute.notificationRecipients");
        assertElementPresentByName("documentAttribute.notificationSenders");
        waitAndClickByXpath("//a[@title='cancel']");
        passed();
    }
}