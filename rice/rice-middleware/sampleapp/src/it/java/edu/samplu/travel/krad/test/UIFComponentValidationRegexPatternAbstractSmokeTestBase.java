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
package edu.samplu.travel.krad.test;

import edu.samplu.common.Failable;
import edu.samplu.common.ITUtil;
import edu.samplu.common.WebDriverLegacyITBase;

/**
 * Tests the Component section in Rice.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class UIFComponentValidationRegexPatternAbstractSmokeTestBase extends WebDriverLegacyITBase  {

    /**
     * "/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&readOnlyFields=field91"
     */
    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=UifCompView&methodToCall=start&readOnlyFields=field91";


    protected void bookmark() {
        open(ITUtil.getBaseUrlString() + BOOKMARK_URL);
    }

    /**
     * Nav tests start at {@link edu.samplu.common.ITUtil#PORTAL}.  Bookmark Tests should override and return BOOKMARK_URL
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getTestUrl() {
        return ITUtil.PORTAL;
    }


    protected void navigation() throws InterruptedException {
        waitAndClickKRAD();
        waitAndClickByXpath(KITCHEN_SINK_XPATH);
        switchToWindow(KUALI_UIF_COMPONENTS_WINDOW_XPATH);
    }

    protected void testValidCharacterConstraintNav(Failable failable) throws Exception {
        navigation();
        testValidCharacterConstraint();
        passed();
    }

    protected void testValidCharacterConstraintBookmark(Failable failable) throws Exception {
        testValidCharacterConstraint();
        passed();
    }

    protected void testValidCharacterConstraint() throws Exception {
//        waitAndClickByXpath("//a[contains(text(),'Validation - Regex')][2]");
        waitAndClickById("UifCompView-Navigation4");

        //---------------------------------------------Fixed Point------------------------------//
        waitAndTypeByName("field50", "123.12");
        fireEvent("field50", "blur");
        try {
            validateErrorImage(true);
            fail("Framework error validateErrorImage(true) should have thrown an AssertionExcpetion");
        } catch (AssertionError ae) {
            jGrowl("validateErrorImage(true) when not expected okay.");
            // expected
        }
        validateErrorImage(false);

        clearTextByName("field50");
        waitAndTypeByName("field50", "123.123");
        fireEvent("field50", "blur");
        try {
            validateErrorImage(false);
            fail("Framework error validateErrorImage(false) should have thrown an AssertionExcpetion");
        } catch (AssertionError ae) {
            jGrowl("validateErrorImage(false) when expected okay.");
        }
        validateErrorImage(true);

        clearTextByName("field50");
        waitAndTypeByXpath("//input[@name='field50']", "1234.4");
        fireEvent("field50", "blur");
        validateErrorImage(true);
        clearTextByName("field50");
        waitAndTypeByXpath("//input[@name='field50']", "1234.434");
        fireEvent("field50", "blur");
        validateErrorImage(true);
        clearTextByName("field50");
        waitAndTypeByXpath("//input[@name='field50']", "123.67");
        fireEvent("field50", "blur");
        validateErrorImage(false);
        clearTextByName("field50");

        //---------------------------------------------Floating Point------------------------------//
        waitAndTypeByXpath("//input[@name='field51']", "127.");
        fireEvent("field51", "blur");
        validateErrorImage(true);
        clearTextByName("field51");
        waitAndTypeByXpath("//input[@name='field51']", "1234()98");
        fireEvent("field51", "blur");
        validateErrorImage(true);
        clearTextByName("field51");
        waitAndTypeByXpath("//input[@name='field51']", "-123.67");
        fireEvent("field51", "blur");
        validateErrorImage(false);
        clearTextByName("field51");

        //---------------------------------------------Integer Pattern constraint------------------------------//
        waitAndTypeByXpath("//input[@name='field77']", "127.");
        fireEvent("field77", "blur");
        validateErrorImage(true);
        clearTextByName("field77");
        waitAndTypeByXpath("//input[@name='field77']", "1234.4123");
        fireEvent("field77", "blur");
        validateErrorImage(true);
        clearTextByName("field77");
        waitAndTypeByXpath("//input[@name='field77']", "123E123");
        fireEvent("field77", "blur");
        validateErrorImage(true);
        clearTextByName("field77");
        waitAndTypeByXpath("//input[@name='field77']", "-123");
        fireEvent("field77", "blur");
        validateErrorImage(false);
        clearTextByName("field77");

        //---------------------------------------------Phone Text------------------------------//
        waitAndTypeByXpath("//input[@name='field52']", "1271231234");
        fireEvent("field52", "blur");
        validateErrorImage(true);
        clearTextByName("field52");
        waitAndTypeByXpath("//input[@name='field52']", "123-123-123");
        fireEvent("field52", "blur");
        validateErrorImage(true);
        clearTextByName("field52");
        waitAndTypeByXpath("//input[@name='field52']", "12-12-123445");
        fireEvent("field52", "blur");
        validateErrorImage(true);
        clearTextByName("field52");
        waitAndTypeByXpath("//input[@name='field52']", "1234-12-1234");
        fireEvent("field52", "blur");
        validateErrorImage(true);
        clearTextByName("field52");
        waitAndTypeByXpath("//input[@name='field52']", "123.123.1234");
        fireEvent("field52", "blur");
        validateErrorImage(true);
        clearTextByName("field52");
        waitAndTypeByXpath("//input[@name='field52']", "123-123-12345");
        fireEvent("field52", "blur");
        validateErrorImage(true);
        clearTextByName("field52");
        waitAndTypeByXpath("//input[@name='field52']", "123-123-1234");
        fireEvent("field52", "blur");
        validateErrorImage(false);
        clearTextByName("field52");

        //---------------------------------------------JavaClass Text------------------------------//
        waitAndTypeByXpath("//input[@name='field53']", "127");
        fireEvent("field53", "blur");
        validateErrorImage(true);
        clearTextByName("field53");
        waitAndTypeByXpath("//input[@name='field53']", "TestJava!@#Class");
        fireEvent("field53", "blur");
        validateErrorImage(true);
        clearTextByName("field53");
        waitAndTypeByXpath("//input[@name='field53']", "Test JavaClass");
        fireEvent("field53", "blur");
        validateErrorImage(true);
        clearTextByName("field53");
        waitAndTypeByXpath("//input[@name='field53']", "Test JavaClass");
        fireEvent("field53", "blur");
        validateErrorImage(true);
        clearTextByName("field53");
        waitAndTypeByXpath("//input[@name='field53']", "TestJavaClass");
        fireEvent("field53", "blur");
        validateErrorImage(false);
        clearTextByName("field53");

        //---------------------------------------------Email Text------------------------------//
        waitAndTypeByXpath("//input[@name='field54']", "123@123.123");
        fireEvent("field54", "blur");
        validateErrorImage(true);
        clearTextByName("field54");
        waitAndTypeByXpath("//input[@name='field54']", "email.com@emailServer");
        fireEvent("field54", "blur");
        validateErrorImage(true);
        clearTextByName("field54");
        waitAndTypeByXpath("//input[@name='field54']", "emailemailServer@.com");
        fireEvent("field54", "blur");
        validateErrorImage(true);
        clearTextByName("field54");
        waitAndTypeByXpath("//input[@name='field54']", "email@emailServercom");
        fireEvent("field54", "blur");
        validateErrorImage(true);
        clearTextByName("field54");
        waitAndTypeByXpath("//input[@name='field54']", "email@emailServer.com");
        fireEvent("field54", "blur");
        validateErrorImage(false);
        clearTextByName("field54");

        //---------------------------------------------URL pattern Text------------------------------//
        waitAndTypeByXpath("//input[@name='field84']", "www.google.com");
        fireEvent("field84", "blur");
        validateErrorImage(true);
        clearTextByName("field84");
        waitAndTypeByXpath("//input[@name='field84']", "https:www.google.com");
        fireEvent("field84", "blur");
        validateErrorImage(true);
        clearTextByName("field84");
        waitAndTypeByXpath("//input[@name='field84']", "ftp://www.google.comsdfa123!#@");
        fireEvent("field84", "blur");
        validateErrorImage(true);
        clearTextByName("field84");
        waitAndTypeByXpath("//input[@name='field84']", "ftp:/www.google.coms");
        fireEvent("field84", "blur");
        validateErrorImage(true);
        clearTextByName("field84");
        waitAndTypeByXpath("//input[@name='field84']", "ftp://www.google.com");
        fireEvent("field84", "blur");
        validateErrorImage(false);
        clearTextByName("field84");
        waitAndTypeByXpath("//input[@name='field84']", "https://www.google.com");
        fireEvent("field84", "blur");
        validateErrorImage(false);
        clearTextByName("field84");
        waitAndTypeByXpath("//input[@name='field84']", "http://www.google.com");
        fireEvent("field84", "blur");
        validateErrorImage(false);
        clearTextByName("field84");

        //---------------------------------------------Date pattern Text------------------------------//
        //-------------invalid formats
        waitAndTypeByXpath("//input[@name='field55']", "12/12/2112 12:12:87 am");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "12-12-2112 12:12 am");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "12-12-2112 12:12");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "12/12/2112 12:12");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "12-12-2112 12:12:78");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "12 Sept");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "Sept 12 12:12");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "221299 12:12:13");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "111222 12:12");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "9/9/2012 12:12 am");
        fireEvent("field55", "blur");
        validateErrorImage(true);
        clearTextByName("field55");

        //-------------valid formats
        waitAndTypeByXpath("//input[@name='field55']", "09/09/2012 12:12 pm");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "090923");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "Sept 12");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "2034");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "12/12/2012 23:12:59");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "12-12-12 23:12:59");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "121212 23:12:32");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "Sept 12 23:45:50");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");
        waitAndTypeByXpath("//input[@name='field55']", "2011 12:23:32");
        fireEvent("field55", "blur");
        validateErrorImage(false);
        clearTextByName("field55");

        //---------------------------------------------BasicDate pattern Text------------------------------//
        waitAndTypeByXpath("//input[@name='field75']", "12122012");
        fireEvent("field75", "blur");
        validateErrorImage(true);
        clearTextByName("field75");
        waitAndTypeByXpath("//input[@name='field75']", "13-12-34");
        fireEvent("field75", "blur");
        validateErrorImage(true);
        clearTextByName("field75");
        waitAndTypeByXpath("//input[@name='field75']", "12:12:2034");
        fireEvent("field75", "blur");
        validateErrorImage(true);
        clearTextByName("field75");
        waitAndTypeByXpath("//input[@name='field75']", "12-12-2034");
        fireEvent("field75", "blur");
        validateErrorImage(false);
        clearTextByName("field75");

        //---------------------------------------------Time12H Pattern Text------------------------------//
        waitAndTypeByXpath("//input[@name='field82']", "13:00:12");
        fireEvent("field82", "blur");
        validateErrorImage(true);
        clearTextByName("field82");
        waitAndTypeByXpath("//input[@name='field82']", "09:00:");
        fireEvent("field82", "blur");
        validateErrorImage(true);
        clearTextByName("field82");
        waitAndTypeByXpath("//input[@name='field82']", "3-00:12");
        fireEvent("field82", "blur");
        validateErrorImage(true);
        clearTextByName("field82");
        waitAndTypeByXpath("//input[@name='field82']", "3:00:34");
        fireEvent("field82", "blur");
        validateErrorImage(false);
        clearTextByName("field82");
        waitAndTypeByXpath("//input[@name='field82']", "3:00");
        fireEvent("field82", "blur");
        validateErrorImage(false);
        clearTextByName("field82");

        //---------------------------------------------Time24H Pattern Text------------------------------//
        waitAndTypeByXpath("//input[@name='field83']", "24:00:12");
        fireEvent("field83", "blur");
        validateErrorImage(true);
        clearTextByName("field83");
        waitAndTypeByXpath("//input[@name='field83']", "14:00:");
        fireEvent("field83", "blur");
        validateErrorImage(true);
        clearTextByName("field83");
        waitAndTypeByXpath("//input[@name='field83']", "13:00:76");
        fireEvent("field83", "blur");
        validateErrorImage(true);
        clearTextByName("field83");
        waitAndTypeByXpath("//input[@name='field83']", "13:00:23");
        fireEvent("field83", "blur");
        validateErrorImage(false);
        clearTextByName("field83");
        waitAndTypeByXpath("//input[@name='field83']", "23:00:12");
        fireEvent("field83", "blur");
        validateErrorImage(false);
        clearTextByName("field83");

        //---------------------------------------------Timestamp pattern Text------------------------------//
        waitAndTypeByXpath("//input[@name='field56']", "1000-12-12 12:12:12.103");
        fireEvent("field56", "blur");
        validateErrorImage(true);
        clearTextByName("field56");
        waitAndTypeByXpath("//input[@name='field56']", "2000/12/12 12-12-12.87");
        fireEvent("field56", "blur");
        validateErrorImage(true);
        clearTextByName("field56");
        waitAndTypeByXpath("//input[@name='field56']", "2000/12/12 12-12-12.87");
        fireEvent("field56", "blur");
        validateErrorImage(true);
        clearTextByName("field56");
        waitAndTypeByXpath("//input[@name='field56']", "2011-08-12 12:12:12");
        fireEvent("field56", "blur");
        validateErrorImage(true);
        clearTextByName("field56");

        //--------this should not be allowed
        /*
        clearTimeStampText();
        waitAndType("//input[@name='field56']", "2999-12-12 12:12:12.103");
        focus("//input[@name='field57']");
        Thread.sleep(100);
        assertTrue(isTextPresent("Must be a date/time in the format of yyyy-mm-dd hh:mm:ss.ms, between the years of 1900 and 2099, inclusive. \"ms\" represents milliseconds, and must be included."));
        */
        waitAndTypeByXpath("//input[@name='field56']", "2099-12-12 12:12:12.103");
        fireEvent("field56", "blur");
        validateErrorImage(false);
        clearTextByName("field56");

        //---------------------------------------------Year Pattern Text------------------------------//
        waitAndTypeByXpath("//input[@name='field57']", "1599");
        fireEvent("field57", "blur");
        validateErrorImage(true);
        clearTextByName("field57");
        waitAndTypeByXpath("//input[@name='field57']", "2200");
        fireEvent("field57", "blur");
        validateErrorImage(true);
        clearTextByName("field57");
        waitAndTypeByXpath("//input[@name='field57']", "20000");
        fireEvent("field57", "blur");
        validateErrorImage(true);
        clearTextByName("field57");
        waitAndTypeByXpath("//input[@name='field57']", "-202");
        fireEvent("field57", "blur");
        validateErrorImage(true);
        clearTextByName("field57");
        waitAndTypeByXpath("//input[@name='field57']", "2000");
        fireEvent("field57", "blur");
        validateErrorImage(false);
        clearTextByName("field57");

        //---------------------------------------------Month Pattern Text------------------------------//
        waitAndTypeByXpath("//input[@name='field58']", "0");
        fireEvent("field58", "blur");
        validateErrorImage(true);
        clearTextByName("field58");
        waitAndTypeByXpath("//input[@name='field58']", "-12");
        fireEvent("field58", "blur");
        validateErrorImage(true);
        clearTextByName("field58");
        waitAndTypeByXpath("//input[@name='field58']", "100");
        fireEvent("field58", "blur");
        validateErrorImage(true);
        clearTextByName("field58");
        waitAndTypeByXpath("//input[@name='field58']", "12");
        fireEvent("field58", "blur");
        validateErrorImage(false);
        clearTextByName("field58");

        //---------------------------------------------ZipCode Pattern Text------------------------------//
        waitAndTypeByXpath("//input[@name='field61']", "123");
        fireEvent("field61", "blur");
        validateErrorImage(true);
        clearTextByName("field61");
        waitAndTypeByXpath("//input[@name='field61']", "2341 12");
        fireEvent("field61", "blur");
        validateErrorImage(true);
        clearTextByName("field61");
        waitAndTypeByXpath("//input[@name='field61']", "0-1231");
        fireEvent("field61", "blur");
        validateErrorImage(true);
        clearTextByName("field61");
        waitAndTypeByXpath("//input[@name='field61']", "12345");
        fireEvent("field61", "blur");
        validateErrorImage(false);
        clearTextByName("field61");

        //---------------------------------------------Alpha Numeric w/o options Text------------------------------//
        waitAndTypeByXpath("//input[@name='field62']", "123 23 @#");
        fireEvent("field62", "blur");
        validateErrorImage(true);
        clearTextByName("field62");
        waitAndTypeByXpath("//input[@name='field62']", "-asd123");
        fireEvent("field62", "blur");
        validateErrorImage(true);
        clearTextByName("field62");
        waitAndTypeByXpath("//input[@name='field62']", "asd/123");
        fireEvent("field62", "blur");
        validateErrorImage(true);
        clearTextByName("field62");
        waitAndTypeByXpath("//input[@name='field62']", "asd123");
        fireEvent("field62", "blur");
        validateErrorImage(false);
        clearTextByName("field62");

        //---------------------------------------------Alpha Numeric with options Text------------------------------//
        waitAndTypeByXpath("//input[@name='field63']", "123^we");
        fireEvent("field63", "blur");
        validateErrorImage(true);
        clearTextByName("field63");
        waitAndTypeByXpath("//input[@name='field63']", "-123_asd");
        fireEvent("field63", "blur");
        validateErrorImage(true);
        clearTextByName("field63");
        waitAndTypeByXpath("//input[@name='field63']", "123 23 @#");
        fireEvent("field63", "blur");
        clearTextByName("field63");
        waitAndTypeByXpath("//input[@name='field63']", "as_de 456/123");
        fireEvent("field63", "blur");
        validateErrorImage(false);
        clearTextByName("field63");

        //---------------------------------------------Alpha with Whitespace and commas Text------------------------------//
        waitAndTypeByXpath("//input[@name='field64']", "123^we");
        fireEvent("field64", "blur");
        validateErrorImage(true);
        clearTextByName("field64");
        waitAndTypeByXpath("//input[@name='field64']", "asd_pqr");
        fireEvent("field64", "blur");
        validateErrorImage(true);
        clearTextByName("field64");
        waitAndTypeByXpath("//input[@name='field64']", "asd/def");
        fireEvent("field64", "blur");
        validateErrorImage(true);
        clearTextByName("field64");
        waitAndTypeByXpath("//input[@name='field64']", "asd ,pqr");
        fireEvent("field64", "blur");
        validateErrorImage(false);
        clearTextByName("field64");

        //---------------------------------------------AlphaPatterrn with disallowed charset Text------------------------------//
        waitAndTypeByXpath("//input[@name='field76']", "123");
        fireEvent("field76", "blur");
        validateErrorImage(true);
        clearTextByName("field76");
        waitAndTypeByXpath("//input[@name='field76']", "`abcd`");
        fireEvent("field76", "blur");
        validateErrorImage(true);
        clearTextByName("field76");
        waitAndTypeByXpath("//input[@name='field76']", "|abcd|");
        fireEvent("field76", "blur");
        validateErrorImage(true);
        clearTextByName("field76");
        waitAndTypeByXpath("//input[@name='field76']", "~abcd~");
        fireEvent("field76", "blur");
        validateErrorImage(true);
        clearTextByName("field76");
        waitAndTypeByXpath("//input[@name='field76']", " ab_c d_ef ");
        fireEvent("field76", "blur");
        validateErrorImage(false);
        clearTextByName("field76");

        //---------------------------------------------Anything with No Whitespace Text------------------------------//
        waitAndTypeByXpath("//input[@name='field65']", "123 ^we");
        fireEvent("field65", "blur");
        validateErrorImage(true);
        clearTextByName("field65");
        waitAndTypeByXpath("//input[@name='field65']", "123^we!@#^&*~:");
        fireEvent("field65", "blur");
        validateErrorImage(false);
        clearTextByName("field65");

        //---------------------------------------------CharacterSet Text------------------------------//
        waitAndTypeByXpath("//input[@name='field66']", "123 ^we");
        fireEvent("field66", "blur");
        validateErrorImage(true);
        clearTextByName("field66");
        waitAndTypeByXpath("//input[@name='field66']", "123_^we");
        fireEvent("field66", "blur");
        validateErrorImage(true);
        clearTextByName("field66");
        waitAndTypeByXpath("//input[@name='field66']", "abc ABC");
        fireEvent("field66", "blur");
        validateErrorImage(true);
        clearTextByName("field66");
        waitAndTypeByXpath("//input[@name='field66']", "aAbBcC");
        fireEvent("field66", "blur");
        validateErrorImage(false);
        clearTextByName("field66");

        //---------------------------------------------Numeric Character Text------------------------------//
        waitAndTypeByXpath("//input[@name='field67']", "123 ^we");
        fireEvent("field67", "blur");
        validateErrorImage(true);
        clearTextByName("field67");
        waitAndTypeByXpath("//input[@name='field67']", "123/10");
        fireEvent("field67", "blur");
        validateErrorImage(true);
        clearTextByName("field67");
        waitAndTypeByXpath("//input[@name='field67']", "(123.00)");
        fireEvent("field67", "blur");
        validateErrorImage(true);
        clearTextByName("field67");
        waitAndTypeByXpath("//input[@name='field67']", "(12-3)");
        fireEvent("field67", "blur");
        validateErrorImage(false);
        clearTextByName("field67");

        //---------------------------------------------Valid Chars Custom Text------------------------------//
        waitAndTypeByXpath("//input[@name='field68']", "123.123");
        fireEvent("field68", "blur");
        validateErrorImage(true);
        clearTextByName("field68");
        waitAndTypeByXpath("//input[@name='field68']", "a.b");
        fireEvent("field68", "blur");
        validateErrorImage(true);
        clearTextByName("field68");
        waitAndTypeByXpath("//input[@name='field68']", "123 qwe");
        fireEvent("field68", "blur");
        validateErrorImage(true);
        clearTextByName("field68");
        waitAndTypeByXpath("//input[@name='field68']", "5.a");
        fireEvent("field68", "blur");
        validateErrorImage(true);
        clearTextByName("field68");
        waitAndTypeByXpath("//input[@name='field68']", "a.0,b.4");
        fireEvent("field68", "blur");
        validateErrorImage(true);
        clearTextByName("field68");
        waitAndTypeByXpath("//input[@name='field68']", "a.0");
        fireEvent("field68", "blur");
        validateErrorImage(false);
        clearTextByName("field68");
        passed();
    }
}