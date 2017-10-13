/**
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
package org.kuali.rice.krad.devtools.maintainablexml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This is a command line utility class which upgrades the maintenance document xml stored in krns_maint_doc_t.doc_cntnt
 * to be able to still open and use any maintenance documents that were enroute at the time of an upgrade to Rice 2.0.
 *
 * <p>Instructions:</p>
 * <ol>
 *   <li>Backup database.</li>
 *   <li>Add the conversion rules to the rules xml file -
 *       ..\rice\development-tools\src\main\resources\org\kuali\rice\devtools\krad\maintainablexml\MaintainableXMLUpgradeRules.xml
 *       See comments in the xml file to setup the rules.</li>
 *   <li>Run this class.  Note that any classes being converted must be on the classpath.</li>
 *   <li>Enter the rice config file location that has the database connection properties. Only enter the location relative
 *       to user.dir.  NOTE -- If it is uncommented in ConvertMaintainableXML.java, MaintainableXMLConversionConfig.xml
 *       in the user.dir will be used as the conversion config file.  runmode, fromRange, and toRange can be
 *       specified in this file to avoid prompting the user for this information.  An example file can be found in
 *        ..\rice\development-tools\src\main\resources\org\kuali\rice\devtools\krad\maintainablexml\MaintainableXMLConversionConfig.xml
 *   </li>
 *   <li>If it is not specified in the config file, the user will be prompted for a run mode. Mode 1 will do the xml
 *   upgrade and update the krns_maint_doc_t table with the new xml. CANNOT ROLL BACK AFTER THIS HAS BEEN DONE.
 *   Mode 2 will only print out the old xml and the new xml - this can be used to test the rules xml setup.</li>
 *   <li>If it is not specified in the config file, the user will be prompted for the document number to upgrade from.
 *   Enter "all" in the from range prompt to upgrade all documents.  Otherwise this should be an starting document ID
 *   such as 1000</li>
 *   <li>If it is not specified in the config file and the fromRange is not "all", the user will be prompted for the document
 *   number to upgrade to.  If "all" was entered for the fromRange, toRange will be ignored.  toRange should be a
 *   document ID such as 9999</li>
 * </ol>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ConvertMaintainableXML {

    private static final String MAINTAINABLE_CONVERSION_CONFIG_FILE = "maintainable.conversion.rule.file";

    public static void main(String[] args) {

        // NOTE : If you want the user to be prompted for the config settings file name, Leave this set to ""
        // Otherwise uncomment out the second line and it will look for the file MaintainableXMLConversionConfig.xml

        String settingsFile = "";
        //settingsFile =  "MaintainableXMLConversionConfig.xml";

        if (StringUtils.isEmpty(settingsFile)) {
            settingsFile = readInput("Enter config file location relative to Kuali main user directory "
                + "(ie. dev/sample-app-config.xml OR MaintainableXMLConversionConfig.xml) : ", null, false);
        }

        String filePath = System.getProperty("user.home") + "/" + settingsFile;

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("The settings file location is not valid : \n" + filePath);
            System.exit(1);
        }

        try {
            HashMap settingsMap = getSettings(filePath);
            System.out.println("Using the following settings : " + settingsMap + "\n");

            //  RUN MODE
            String runMode = (String)settingsMap.get("runMode");
            if (StringUtils.isEmpty(runMode) || !(StringUtils.equals(runMode, "1") || StringUtils.equals(runMode, "2"))) {
                runMode = readInput("Run mode :\n1. Update xml in DB\n2. Print old and new xml\nEnter 1 or 2\n",
                    new String[]{"1", "2"}, false);
            }

            //  FROM RANGE
            String fromRange = (String)settingsMap.get("fromRange");
            if (StringUtils.isEmpty(fromRange) || (!StringUtils.equals(fromRange, "all") && !(isNumber(fromRange)))) {
                fromRange = readInput("Please enter document start range value ('all', empty line for all, or the starting document number such as 1000) :\n",
                    new String[]{"all", ""}, true);
            }

            String toRange = null;
            boolean hasRangeParameters = false;

            if (!"".equals(fromRange) && !"all".equals(fromRange)) {
                //  TO RANGE, if needed
                toRange = (String)settingsMap.get("toRange");
                if (StringUtils.isEmpty(toRange) || !(isNumber(toRange))) {
                    toRange = readInput("Please enter end range value :\n", null, true);
                }
                System.out.println("Upgrading document numbers from " + fromRange + " to " + toRange + "\n");
                hasRangeParameters = true;
            }

            FileConverter fileConverter = new FileConverter();

            // RUN THE CONVERSION!
            fileConverter.runFileConversion(settingsMap, runMode, fromRange, toRange, hasRangeParameters);

        } catch (Exception e) {
            System.out.println("Error executing conversion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Displays message in command line and read user input. Checks that entered values are within valid input.
     *
     * @param message - the message string to print out
     * @param validOptions - the allowed user input
     * @return the string input from the user
     */
    private static String readInput(String message, String[] validOptions, boolean numeric) {
        System.out.print(message);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String inputString = null;

        try {
            inputString = br.readLine();
            if (numeric && (validOptions == null || !Arrays.asList(validOptions).contains(inputString))) {
                Integer.parseInt(inputString);
                return inputString;
            }
        } catch (IOException ioe) {
            System.out.println("IO error trying to read input!");
            System.exit(1);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid Option! Must be numeric.");
            readInput(message, validOptions, numeric);
        }
        if (validOptions != null && !Arrays.asList(validOptions).contains(inputString)) {
            System.out.println("Invalid Option!");
            readInput(message, validOptions, numeric);
        }
        return inputString;
    }

    /**
     * Parses settings file and put the properties in a map.
     *
     * @param filePath - the location of the settings file
     * @return a HashMap populated with the settings
     * @throws Exception
     */
    public static HashMap getSettings(String filePath) throws Exception {
        File file = new File(filePath);
        HashMap params = new HashMap();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeLst = doc.getElementsByTagName("param");

        for (int s = 0; s < nodeLst.getLength(); s++) {
            Node fstNode = nodeLst.item(s);
            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                Element fstElmnt = (Element) fstNode;
                String paramName = fstElmnt.getAttribute("name");
                NodeList textFNList = fstElmnt.getChildNodes();
                if (textFNList.item(0) != null) {
                    String paramValue = ((Node) textFNList.item(0)).getNodeValue().trim();
                    params.put(paramName, paramValue);
                } else {
                    params.put(paramName, null);
                }
            }
        }
        return params;
    }

}
