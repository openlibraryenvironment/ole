/*
 * Copyright 2005-2014 The Kuali Foundation
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
/**
 * tests various utility functions in krad.utility.js. Test names need a 'test' suffix to be detected
 *
 * Run with jsTestDriver in working directory that holds this file:
 * java -jar [path/to/]JsTestDriver-1.3.4.b.jar --tests all --basePath [path/to/]web/src
 *
 * To run from another working directory, the --config option will need to be supplied to specify the config file location
 *
 * Good tutorial at http://www.ibm.com/developerworks/java/library/os-jstesting/index.html?ca=drs-
 */
KradUtilityTest = TestCase("KradUtilityTest");

/**
 * sets up a document fragment for use in tests
 */
KradUtilityTest.prototype.setUp = function () {
    /*:DOC +=<div id="doc"><p id="para"/>
     <input id="hidden1" type="hidden" value="jQuery('#para').attr('data-result1', String(1+1));" name="script">
     <input id="hidden2" type="hidden" value="jQuery('#para').attr('data-result2', String(2+3));" data-role="dataScript"></div> */
};

/**
 * tests evalHiddenScript()
 */
KradUtilityTest.prototype.testEvalHiddenScript = function () {
    evalHiddenScript(jQuery("#hidden1"));
    this.assertHiddenScriptAttributeChanges(jQuery("#hidden1"));
    assertEquals("hidden script did not run", "2", jQuery("#para").attr("data-result1"));
};

/**
 * a convenience method for checking for expected attribute changes in hidden input element containing a script
 *
 * @param jqueryObj - the hidden input element
 */
KradUtilityTest.prototype.assertHiddenScriptAttributeChanges = function (jqueryObj) {
    assertEquals("attribute 'name' should not be present", undefined, jqueryObj.attr("name"));
    assertEquals("attribute 'script' has different value", "first_run", jqueryObj.attr("script"));
}