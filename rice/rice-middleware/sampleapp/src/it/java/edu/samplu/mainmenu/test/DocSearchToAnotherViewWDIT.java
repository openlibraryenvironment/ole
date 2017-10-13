package edu.samplu.mainmenu.test;

import junit.framework.Assert;

import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver.Window;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

/**
 * test that after doc search, navigating to people flow maintenance view does not cause Javascript errors
 * and therefore interfere with JS functionality like validation
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocSearchToAnotherViewWDIT extends WebDriverLegacyITBase {

    @Override
    public void fail(String message) {
        org.junit.Assert.fail(message);
    }

    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }

	@Test
    /**
     * test that after doc search, navigating to people flow maintenance view does not cause Javascript errors
     * and therefore interfere with JS functionality like validation
     */
	public void testDocSearchToAnotherView() throws Exception {
		waitAndClick("img[alt=\"doc search\"]");
		waitForPageToLoad();
		selectFrame("iframeportlet");
		waitAndClick("td.infoline > input[name=\"methodToCall.search\"]");
		waitForPageToLoad();
	//	selectFrame("relative=top");
		driver.switchTo().defaultContent();
		waitAndClickByLinkText("Main Menu");
        waitForPageToLoad();
		//setSpeed("2000");
		waitAndClickByLinkText("People Flow");
		waitForPageToLoad();
		selectFrame("iframeportlet");
		waitAndClickByLinkText("Create New");
		waitForPageToLoad();
		fireEvent("document.documentHeader.documentDescription", "focus");
		waitAndTypeByName("document.documentHeader.documentDescription", "sample description");
		fireEvent("document.documentHeader.explanation", "focus");
		waitAndTypeByName("document.documentHeader.explanation", "sample explanation");		
//		((JavascriptExecutor)driver).executeScript("document.getElementById(\"uif-cancel\").focus();");
		waitAndClickByLinkText("Cancel");
		Thread.sleep(5000);
		final String text = "Form has unsaved data. Do you want to leave anyway?";
		Alert a=driver.switchTo().alert();
		Assert.assertTrue(a.getText().equals(text));
		a.dismiss();
        passed();
	}
}
