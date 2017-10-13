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
package org.kuali.rice.krad.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.kuali.rice.kns.util.WebUtils;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.*;

/**
 * Unit tests for the KNS {@link WebUtils}
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class WebUtilsTest extends KRADTestCase {

	/**
	 * Tests WebUtils.getButtonImageUrl()
	 *
	 */
	@Test
	public void testButtonImageUrl() {
		final String test1 = "test1";
		final String test2 = "test2";
		
		final String test2Image = WebUtils.getButtonImageUrl(test2);
		final String test2DefaultImage = WebUtils.getDefaultButtonImageUrl(test2);
		assertEquals("test2 image did not equal default for test2", test2Image, test2DefaultImage);
		
		final String test1Image = WebUtils.getButtonImageUrl(test1);
		final String test1DefaultImage = WebUtils.getDefaultButtonImageUrl(test1);
		assertNotSame("the test1 image should not be the default", test1Image, test1DefaultImage);
		assertEquals("/test/images/test1.png", test1Image);
	}
	
	/**
	 * Tests the filterHtmlAndReplaceRiceMarkup method
	 */
	@Test
	public void testFilterHtmlAndReplaceRiceMarkup() {
		String questionText = "";
		
		// verify script markup does not make it through
		questionText = "<script> function () </script>";
		String filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertTrue("Script tags not filtered out", !StringUtils.equals(questionText, filteredText));
		
		// verify supported tags get translated
		questionText = "[p][/p]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Paragraph tag not translated to markup", "<p></p>", filteredText);
		
		questionText = "[b][/b]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Bold tag not translated to markup", "<b></b>", filteredText);
		
		questionText = "[br][/br]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Break tag not translated to markup", "<br></br>", filteredText);
		
		questionText = "[tr][/tr]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Table row tag not translated to markup", "<tr></tr>", filteredText);
		
		questionText = "[td][/td]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Table cell tag not translated to markup", "<td></td>", filteredText);
		
		questionText = "[font #000000][/font]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Font with hex color tag not translated to markup", "<font color=\"#000000\"></font>", filteredText);
		
		questionText = "[font red][/font]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Font with color name tag not translated to markup", "<font color=\"red\"></font>", filteredText);
		
		questionText = "[table][/table]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Table tag not translated to markup", "<table></table>", filteredText);
	
		questionText = "[table questionTable][/table]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Table with class tag not translated to markup", "<table class=\"questionTable\"></table>", filteredText);
		
		questionText = "[td leftTd][/td]";
		filteredText = WebUtils.filterHtmlAndReplaceRiceMarkup(questionText);
		assertEquals("Table cell with class tag not translated to markup", "<td class=\"leftTd\"></td>", filteredText);
	}

    @Test
    public void testToAbsoluteURL() {
        assertEquals("base/relative", WebUtils.toAbsoluteURL("base", "/relative"));
        assertEquals("http://base/relative", WebUtils.toAbsoluteURL("http://base", "/relative"));
        assertEquals("http://absolute", WebUtils.toAbsoluteURL("http://base", "http://absolute"));
        assertEquals("https://absolute", WebUtils.toAbsoluteURL("http://base", "https://absolute"));
        assertEquals("https://absolute", WebUtils.toAbsoluteURL(null, "https://absolute"));
        assertEquals("", WebUtils.toAbsoluteURL("", ""));
        assertEquals("base", WebUtils.toAbsoluteURL("base", ""));
        assertEquals("base", WebUtils.toAbsoluteURL("base", null));
    }
}
