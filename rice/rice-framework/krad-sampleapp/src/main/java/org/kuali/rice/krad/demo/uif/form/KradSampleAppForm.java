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
package org.kuali.rice.krad.demo.uif.form;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic form for the KRAD sample application
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KradSampleAppForm extends UifFormBase {
    private static final long serialVersionUID = -7525378097732916418L;
    private String themeName;
    private String exampleShown;
    private String currentExampleIndex;

    //Fields separated by demonstration type below:

    //InputField
    private String inputField1;
    private String inputField2;
    private String inputField3;
    private String inputField4;
    private String inputField5;
    private String inputField6;
    private String inputField7;
    private String inputField8;
    private String inputField9;
    private String inputField10;
    private String inputField11;
    private String inputField12;
    private String inputField13;
    private String inputField14;
    private String inputField15;
    private String inputField16;
    private String inputField17;
    private String inputField18;
    private String inputField19;
    private String inputField20;
    private String inputField21;
    private String inputField22;
    private String inputField23;
    private String inputField24;
    private String inputField25;
    private String inputField26;
    private String inputField27;
    private String inputField28;
    private String inputField29;
    private String inputField30;

    private boolean booleanField1 = false;
    private boolean booleanField2 = false;
    private boolean booleanField3 = false;

    private List<String> checkboxesField1;
    private List<String> checkboxesField2;
    private List<String> checkboxesField3;
    private List<String> checkboxesField4;
    private List<String> multiSelectField1;

    private String testPersonId;
    private Person testPerson;

    private String testGroupId;

    //DataField
    private String dataField1 = "1001";
    private String dataField2 = "";
    private String dataField3 = "My Book Title";

    //MessageField
    private String messageField1;
    private String richMessageField = "[color=green][b]Message[/b][/color]";

    // Collections
    private List<UITestObject> collection1 = new ArrayList<UITestObject>();
    private List<UITestObject> collection1_2 = new ArrayList<UITestObject>();
    private List<UITestObject> collection1_3 = new ArrayList<UITestObject>();
    private List<UITestObject> collection1_4 = new ArrayList<UITestObject>();
    private List<UITestObject> collection1_5 = new ArrayList<UITestObject>();
    private List<UITestObject> collection1_6 = new ArrayList<UITestObject>();
    private List<UITestObject> collection1_7 = new ArrayList<UITestObject>();
    private List<UITestObject> collection1_8 = new ArrayList<UITestObject>();
    private List<UITestObject> collection1_9 = new ArrayList<UITestObject>();
    private List<UITestObject> collection2 = new ArrayList<UITestObject>();
    private List<UITestObject> collection3 = new ArrayList<UITestObject>();
    private List<UITestObject> collection4 = new ArrayList<UITestObject>();
    private List<UITestObject> collection5 = new ArrayList<UITestObject>();

    private List<UIInactivatableTestObject> inactivatableCollection = new ArrayList<UIInactivatableTestObject>();

    private List<UITestObject> groupedCollection1 = new ArrayList<UITestObject>();
    private List<UITestObject> groupedCollection2 = new ArrayList<UITestObject>();
    private List<UITestObject> groupedCollection3 = new ArrayList<UITestObject>();
    private List<UITestObject> doubleGroupedCollection = new ArrayList<UITestObject>();

    private List<UITestObject> emptyCollection = new ArrayList<UITestObject>();

    private Tree<String, String> tree1 = new Tree<String, String>();
    private Tree<UITestObject, String> tree2 = new Tree<UITestObject, String>();

    private List<UITestObject> subList = new ArrayList<UITestObject>();

    private String fakeTotal = "123(server value)";

    //preset fields
    private List<String> optionListSelection = Arrays.asList("2", "4");

    private String codeSample = ""
            + "        if ((testPerson == null) || !StringUtils.equals(testPerson.getPrincipalId(), getTestPersonId())) {\n"
            + "            testPerson = KimApiServiceLocator.getPersonService().getPerson(getTestPersonId());\n"
            + "\n"
            + "            if (testPerson == null) {\n"
            + "                try {\n"
            + "                    testPerson = KimApiServiceLocator.getPersonService().getPersonImplementationClass().newInstance();\n"
            + "                } catch (Exception e) {\n"
            + "                    throw new RuntimeException(e);\n"
            + "                }\n"
            + "            }\n"
            + "        }";

    public KradSampleAppForm() {
        super();

        messageField1 = "fruits";

        getCollection1().add(new UITestObject("13", "14", "15", "16", "17", "18"));
        getCollection1().add(new UITestObject("19", "20", "21", "22", "23", "24"));
        getCollection1().add(new UITestObject("7", "8", "9", "10", "11", "12"));
        getCollection1().add(new UITestObject("1", "2", "3", "4", "5", "6"));
        getCollection1().add(new UITestObject("13", "14", "15", "16", "17", "18"));
        getCollection1().add(new UITestObject("19", "20", "21", "22", "23", "24"));
        getCollection1().add(new UITestObject("213", "143", "151", "126", "137", "164"));
        getCollection1().add(new UITestObject("133", "144", "155", "166", "177", "188"));
        getCollection1().add(new UITestObject("25", "14", "15", "15", "17", "17"));
        getCollection1().add(new UITestObject("1", "5", "5", "4", "6", "6"));
        getCollection1().add(new UITestObject("5", "5", "5", "5", "5", "5"));
        getCollection1().add(new UITestObject("5", "7", "3", "1", "9", "11"));

        collection1_2.addAll(collection1);
        collection1_3.addAll(collection1);
        collection1_4.addAll(collection1);
        collection1_5.addAll(collection1);
        collection1_6.addAll(collection1);
        collection1_7.addAll(collection1);
        collection1_8.addAll(collection1);
        collection1_9.addAll(collection1);

        getCollection2().add(new UITestObject("A", "B", "C", "D"));
        getCollection2().add(new UITestObject("1", "2", "3", "4"));
        getCollection2().add(new UITestObject("W", "X", "Y", "Z"));
        collection2.add(new UITestObject("a", "b", "c", "d"));
        collection2.add(new UITestObject("a", "s", "d", "f"));

        collection3.add(new UITestObject("A", "B", "C", "D"));
        collection3.get(0).getSubList().add(new UITestObject("A", "B", "C", "D"));
        collection3.get(0).getSubList().add(new UITestObject("1", "2", "3", "4"));
        collection3.get(0).getSubList().add(new UITestObject("W", "X", "Y", "Z"));
        collection3.add(new UITestObject("1", "2", "3", "4"));
        collection3.get(1).getSubList().add(new UITestObject("A", "B", "C", "D"));
        collection3.get(1).getSubList().add(new UITestObject("1", "2", "3", "4"));
        collection3.add(new UITestObject("W", "X", "Y", "Z"));
        collection3.get(2).getSubList().add(new UITestObject("W", "X", "Y", "Z"));

        collection4.add(new UITestObject("A", "B", "C", "D"));
        collection4.get(0).getSubList().add(new UITestObject("A", "B", "C", "D"));
        collection4.get(0).getSubList().add(new UITestObject("1", "2", "3", "4"));
        collection4.get(0).getSubList().add(new UITestObject("W", "X", "Y", "Z"));
        collection4.add(new UITestObject("1", "2", "3", "4"));
        collection4.get(1).getSubList().add(new UITestObject("a", "b", "C", "D"));
        collection4.get(1).getSubList().add(new UITestObject("a", "s", "D", "F"));

        //triple nesting
        collection5.add(new UITestObject("a", "a", "a", "a"));
        collection5.get(0).getSubList().add(new UITestObject("A", "B", "C", "D"));
        collection5.get(0).getSubList().get(0).getSubList().add(new UITestObject("a3", "3", "3", "3"));
        collection5.get(0).getSubList().get(0).getSubList().add(new UITestObject("a3", "3", "3", "3"));
        collection5.get(0).getSubList().add(new UITestObject("1", "2", "3", "4"));
        collection5.get(0).getSubList().get(1).getSubList().add(new UITestObject("b3", "3", "3", "3"));
        collection5.get(0).getSubList().get(1).getSubList().add(new UITestObject("b3", "3", "3", "3"));
        collection5.get(0).getSubList().get(1).getSubList().add(new UITestObject("b3", "3", "3", "3"));
        collection5.add(new UITestObject("b", "b", "b", "b"));
        collection5.get(1).getSubList().add(new UITestObject("a", "b", "C", "D"));
        collection5.get(1).getSubList().get(0).getSubList().add(new UITestObject("a23", "3", "3", "3"));
        collection5.get(1).getSubList().get(0).getSubList().add(new UITestObject("a23", "3", "3", "3"));
        collection5.get(1).getSubList().add(new UITestObject("a", "s", "D", "F"));
        collection5.get(1).getSubList().get(1).getSubList().add(new UITestObject("b23", "3", "3", "3"));
        collection5.get(1).getSubList().get(1).getSubList().add(new UITestObject("b23", "3", "3", "3"));

        inactivatableCollection.add(new UIInactivatableTestObject("A", "100", "200", "300", true));
        inactivatableCollection.add(new UIInactivatableTestObject("B", "100", "200", "300", true));
        inactivatableCollection.add(new UIInactivatableTestObject("b3", "3", "3", "3", false));
        inactivatableCollection.add(new UIInactivatableTestObject("a", "b", "C", "D", true));
        inactivatableCollection.add(new UIInactivatableTestObject("W", "X", "Y", "Z", false));
        inactivatableCollection.add(new UIInactivatableTestObject("a", "s", "d", "f", true));
        inactivatableCollection.add(new UIInactivatableTestObject("Fall", "2002", "AAA123", "3", false));

        groupedCollection1.add(new UITestObject("A", "100", "200", "300"));
        groupedCollection1.add(new UITestObject("A", "101", "200", "300"));
        groupedCollection1.add(new UITestObject("A", "102", "200", "300"));
        groupedCollection1.add(new UITestObject("A", "103", "200", "300"));
        groupedCollection1.add(new UITestObject("A", "104", "200", "300"));

        groupedCollection1.add(new UITestObject("B", "100", "200", "300"));
        groupedCollection1.add(new UITestObject("B", "101", "200", "300"));
        groupedCollection1.add(new UITestObject("B", "102", "200", "300"));

        groupedCollection1.add(new UITestObject("C", "100", "200", "300"));
        groupedCollection1.add(new UITestObject("C", "101", "200", "300"));
        groupedCollection1.add(new UITestObject("C", "102", "200", "300"));
        groupedCollection1.add(new UITestObject("C", "103", "200", "300"));

        

        groupedCollection2.addAll(groupedCollection1);
        
        groupedCollection2.add(new UITestObject("D", "100", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "101", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "102", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "103", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "100", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "101", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "102", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "103", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "100", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "101", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "102", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "103", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "100", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "101", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "102", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "103", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "100", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "101", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "102", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "103", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "100", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "101", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "102", "200", "300"));
        groupedCollection2.add(new UITestObject("D", "103", "200", "300"));

        groupedCollection3.addAll(groupedCollection2);

        doubleGroupedCollection.add(new UITestObject("Fall", "2001", "AAA123", "2"));
        doubleGroupedCollection.add(new UITestObject("Fall", "2001", "BBB123", "3"));
        doubleGroupedCollection.add(new UITestObject("Fall", "2001", "CCC123", "4"));
        doubleGroupedCollection.add(new UITestObject("Fall", "2001", "DDD123", "3"));

        doubleGroupedCollection.add(new UITestObject("Fall", "2002", "AAA123", "3"));
        doubleGroupedCollection.add(new UITestObject("Fall", "2002", "BBB123", "2"));
        doubleGroupedCollection.add(new UITestObject("Fall", "2002", "CCC123", "3"));

        doubleGroupedCollection.add(new UITestObject("Fall", "2003", "AAA123", "3"));
        doubleGroupedCollection.add(new UITestObject("Fall", "2003", "CCC123", "3"));

        doubleGroupedCollection.add(new UITestObject("Spring", "2001", "AAA123", "3"));
        doubleGroupedCollection.add(new UITestObject("Spring", "2001", "BBB123", "3"));
        doubleGroupedCollection.add(new UITestObject("Spring", "2001", "CCC123", "3"));

        doubleGroupedCollection.add(new UITestObject("Spring", "2002", "AAA123", "4"));
        doubleGroupedCollection.add(new UITestObject("Spring", "2002", "BBB123", "4"));
        doubleGroupedCollection.add(new UITestObject("Spring", "2002", "CCC123", "2"));

        doubleGroupedCollection.add(new UITestObject("Spring", "2003", "AAA123", "4"));
        doubleGroupedCollection.add(new UITestObject("Spring", "2003", "BBB123", "3"));
        doubleGroupedCollection.add(new UITestObject("Spring", "2003", "CCC123", "3"));
        doubleGroupedCollection.add(new UITestObject("Spring", "2003", "DDD123", "2"));

        { // scope for name hiding purposes
            Node<String, String> item1 = new Node<String, String>("Item 1", "Item 1");
            item1.addChild(new Node<String, String>("SubItem A", "SubItem A"));
            item1.addChild(new Node<String, String>("SubItem B", "SubItem B"));

            Node<String, String> item2 = new Node<String, String>("Item 2", "Item 2");
            item2.addChild(new Node<String, String>("SubItem A", "SubItem A"));
            Node<String, String> sub2B = new Node<String, String>("SubItem B", "SubItem B");
            sub2B.addChild(new Node<String, String>("Item B-1", "Item B-1"));
            sub2B.addChild(new Node<String, String>("Item B-2", "Item B-2"));
            sub2B.addChild(new Node<String, String>("Item B-3", "Item B-3"));
            item2.addChild(sub2B);
            item2.addChild(new Node<String, String>("SubItem C", "SubItem C"));

            Node<String, String> item3 = new Node<String, String>("Item 3", "Item 3");
            item3.addChild(new Node<String, String>("SubItem A", "SubItem A"));
            item3.addChild(new Node<String, String>("SubItem B", "SubItem B"));
            item3.addChild(new Node<String, String>("SubItem C", "SubItem C"));
            item3.addChild(new Node<String, String>("SubItem D", "SubItem D"));

            Node<String, String> root = new Node<String, String>("Root", "Root");
            root.addChild(item1);
            root.addChild(item2);
            root.addChild(item3);

            tree1.setRootElement(root);
        }

        { // scope for name hiding purposes
            Node<UITestObject, String> item1 = new Node<UITestObject, String>(new UITestObject("1-A", "1-B", "1-C",
                    "1-D"), "Item 1");
            item1.addChild(new Node<UITestObject, String>(new UITestObject("1SA-A", "1SA-B", "1SA-C", "1SA-D"),
                    "SubItem A"));
            item1.addChild(new Node<UITestObject, String>(new UITestObject("1SB-A", "1SB-B", "1SB-C", "1SB-D"),
                    "SubItem B"));

            Node<UITestObject, String> item2 = new Node<UITestObject, String>(new UITestObject("2-A", "2-B", "2-C",
                    "2-D"), "Item 2");
            item2.addChild(new Node<UITestObject, String>(new UITestObject("SA-a", "SA-b", "SA-c", "SA-d"),
                    "SubItem A"));
            Node<UITestObject, String> sub2B = new Node<UITestObject, String>(new UITestObject("SB-a", "SB-b", "SB-c",
                    "SB-d"), "SubItem B");
            sub2B.addChild(new Node<UITestObject, String>(new UITestObject("AA", "BB", "CC", "DD"), "Item B-1"));
            sub2B.addChild(new Node<UITestObject, String>(new UITestObject("Aa", "Bb", "Cc", "Dd"), "Item B-2"));
            sub2B.addChild(new Node<UITestObject, String>(new UITestObject("aA", "bB", "cC", "dD"), "Item B-3"));
            item2.addChild(sub2B);
            item2.addChild(new Node<UITestObject, String>(new UITestObject("SC-a", "SC-b", "SC-c", "SC-d"),
                    "SubItem C"));

            Node<UITestObject, String> item3 = new Node<UITestObject, String>(new UITestObject("3-A", "3-B", "3-C",
                    "3-D"), "Item 3");
            item3.addChild(new Node<UITestObject, String>(new UITestObject("A", "B", "C", "D"), "SubItem A"));
            item3.addChild(new Node<UITestObject, String>(new UITestObject("1", "2", "3", "4"), "SubItem B"));
            item3.addChild(new Node<UITestObject, String>(new UITestObject("w", "x", "y", "z"), "SubItem C"));
            item3.addChild(new Node<UITestObject, String>(new UITestObject("!", "@", "#", "$"), "SubItem D"));

            Node<UITestObject, String> root = new Node<UITestObject, String>(new UITestObject("foo", "bar", "baz",
                    "roo"), "Root");
            root.addChild(item1);
            root.addChild(item2);
            root.addChild(item3);

            tree2.setRootElement(root);
        }
    }

    /**
     * Theme by name (id) currently used for the component library view
     *
     * @return
     */
    public String getThemeName() {
        return themeName;
    }

    /**
     * @param themeName
     */
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    /**
     * Backing property for the large example dropdown since on is required.  Stores dropdown selection
     *
     * @return
     */
    public String getExampleShown() {
        return exampleShown;
    }

    /**
     * Large example selection
     *
     * @param exampleShown
     */
    public void setExampleShown(String exampleShown) {
        this.exampleShown = exampleShown;
    }

    /**
     * Index of the current example, used to reselect between submit actions
     *
     * @return
     */
    public String getCurrentExampleIndex() {
        return currentExampleIndex;
    }

    /**
     * Index of the current example
     *
     * @param currentExampleIndex
     */
    public void setCurrentExampleIndex(String currentExampleIndex) {
        this.currentExampleIndex = currentExampleIndex;
    }

    /**
     * Below are basic getters and setters for this data object - no javadoc needed *
     */

    public String getDataField1() {
        return dataField1;
    }

    public void setDataField1(String dataField1) {
        this.dataField1 = dataField1;
    }

    public String getDataField2() {
        return dataField2;
    }

    public void setDataField2(String dataField2) {
        this.dataField2 = dataField2;
    }

    public String getDataField3() {
        return dataField3;
    }

    public void setDataField3(String dataField3) {
        this.dataField3 = dataField3;
    }

    public String getInputField1() {
        return inputField1;
    }

    public void setInputField1(String inputField1) {
        this.inputField1 = inputField1;
    }

    public String getInputField2() {
        return inputField2;
    }

    public void setInputField2(String inputField2) {
        this.inputField2 = inputField2;
    }

    public String getInputField3() {

        return inputField3;
    }

    public void setInputField3(String inputField3) {
        this.inputField3 = inputField3;
    }

    public String getInputField4() {
        return inputField4;
    }

    public void setInputField4(String inputField4) {
        this.inputField4 = inputField4;
    }

    public String getInputField5() {
        return inputField5;
    }

    public void setInputField5(String inputField5) {
        this.inputField5 = inputField5;
    }

    public String getInputField6() {
        return inputField6;
    }

    public void setInputField6(String inputField6) {
        this.inputField6 = inputField6;
    }

    public String getInputField7() {
        return inputField7;
    }

    public void setInputField7(String inputField7) {
        this.inputField7 = inputField7;
    }

    public String getInputField8() {
        return inputField8;
    }

    public void setInputField8(String inputField8) {
        this.inputField8 = inputField8;
    }

    public String getInputField9() {
        return inputField9;
    }

    public void setInputField9(String inputField9) {
        this.inputField9 = inputField9;
    }

    public String getInputField10() {
        return inputField10;
    }

    public void setInputField10(String inputField10) {
        this.inputField10 = inputField10;
    }

    public String getInputField11() {
        return inputField11;
    }

    public void setInputField11(String inputField11) {
        this.inputField11 = inputField11;
    }

    public String getInputField12() {
        return inputField12;
    }

    public void setInputField12(String inputField12) {
        this.inputField12 = inputField12;
    }

    public String getInputField13() {
        return inputField13;
    }

    public void setInputField13(String inputField13) {
        this.inputField13 = inputField13;
    }

    public String getInputField14() {
        return inputField14;
    }

    public void setInputField14(String inputField14) {
        this.inputField14 = inputField14;
    }

    public String getInputField15() {
        return inputField15;
    }

    public void setInputField15(String inputField15) {
        this.inputField15 = inputField15;
    }

    public String getInputField16() {
        return inputField16;
    }

    public void setInputField16(String inputField16) {
        this.inputField16 = inputField16;
    }

    public String getInputField17() {
        return inputField17;
    }

    public void setInputField17(String inputField17) {
        this.inputField17 = inputField17;
    }

    public String getInputField18() {
        return inputField18;
    }

    public void setInputField18(String inputField18) {
        this.inputField18 = inputField18;
    }

    public String getInputField19() {
        return inputField19;
    }

    public void setInputField19(String inputField19) {
        this.inputField19 = inputField19;
    }

    public String getInputField20() {
        return inputField20;
    }

    public void setInputField20(String inputField20) {
        this.inputField20 = inputField20;
    }

    public String getInputField21() {
        return inputField21;
    }

    public void setInputField21(String inputField21) {
        this.inputField21 = inputField21;
    }

    public String getInputField22() {
        return inputField22;
    }

    public void setInputField22(String inputField22) {
        this.inputField22 = inputField22;
    }

    public String getInputField23() {
        return inputField23;
    }

    public void setInputField23(String inputField23) {
        this.inputField23 = inputField23;
    }

    public String getInputField24() {
        return inputField24;
    }

    public void setInputField24(String inputField24) {
        this.inputField24 = inputField24;
    }

    public String getInputField25() {
        return inputField25;
    }

    public void setInputField25(String inputField25) {
        this.inputField25 = inputField25;
    }

    public String getInputField26() {
        return inputField26;
    }

    public void setInputField26(String inputField26) {
        this.inputField26 = inputField26;
    }

    public String getInputField27() {
        return inputField27;
    }

    public void setInputField27(String inputField27) {
        this.inputField27 = inputField27;
    }

    public String getInputField28() {
        return inputField28;
    }

    public void setInputField28(String inputField28) {
        this.inputField28 = inputField28;
    }

    public String getInputField29() {
        return inputField29;
    }

    public void setInputField29(String inputField29) {
        this.inputField29 = inputField29;
    }

    public String getInputField30() {
        return inputField30;
    }

    public void setInputField30(String inputField30) {
        this.inputField30 = inputField30;
    }

    public String getMessageField1() {
        return messageField1;
    }

    public void setMessageField1(String messageField1) {
        this.messageField1 = messageField1;
    }

    public String getTestPersonId() {
        return testPersonId;
    }

    public void setTestPersonId(String testPersonId) {
        this.testPersonId = testPersonId;
    }

    public Person getTestPerson() {
        if ((testPerson == null) || !StringUtils.equals(testPerson.getPrincipalId(), getTestPersonId())) {
            testPerson = KimApiServiceLocator.getPersonService().getPerson(getTestPersonId());

            if (testPerson == null) {
                try {
                    testPerson = KimApiServiceLocator.getPersonService().getPersonImplementationClass().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return testPerson;
    }

    public void setTestPerson(Person testPerson) {
        this.testPerson = testPerson;
    }

    public String getTestGroupId() {
        return testGroupId;
    }

    public void setTestGroupId(String testGroupId) {
        this.testGroupId = testGroupId;
    }

    public List<UITestObject> getCollection1() {
        return collection1;
    }

    public void setCollection1(List<UITestObject> collection1) {
        this.collection1 = collection1;
    }

    public List<UITestObject> getCollection1_2() {
        return collection1_2;
    }

    public void setCollection1_2(List<UITestObject> collection1_2) {
        this.collection1_2 = collection1_2;
    }

    public List<UITestObject> getCollection1_3() {
        return collection1_3;
    }

    public void setCollection1_3(List<UITestObject> collection1_3) {
        this.collection1_3 = collection1_3;
    }

    public List<UITestObject> getCollection1_4() {
        return collection1_4;
    }

    public void setCollection1_4(List<UITestObject> collection1_4) {
        this.collection1_4 = collection1_4;
    }

    public List<UITestObject> getCollection1_5() {
        return collection1_5;
    }

    public void setCollection1_5(List<UITestObject> collection1_5) {
        this.collection1_5 = collection1_5;
    }

    public List<UITestObject> getCollection1_6() {
        return collection1_6;
    }

    public void setCollection1_6(List<UITestObject> collection1_6) {
        this.collection1_6 = collection1_6;
    }

    public List<UITestObject> getCollection1_7() {
        return collection1_7;
    }

    public void setCollection1_7(List<UITestObject> collection1_7) {
        this.collection1_7 = collection1_7;
    }

    public List<UITestObject> getCollection1_8() {
        return collection1_8;
    }

    public void setCollection1_8(List<UITestObject> collection1_8) {
        this.collection1_8 = collection1_8;
    }

    public List<UITestObject> getCollection1_9() {
        return collection1_9;
    }

    public void setCollection1_9(List<UITestObject> collection1_9) {
        this.collection1_9 = collection1_9;
    }

    public List<UITestObject> getCollection2() {
        return collection2;
    }

    public void setCollection2(List<UITestObject> collection2) {
        this.collection2 = collection2;
    }

    public List<UITestObject> getCollection3() {
        return collection3;
    }

    public void setCollection3(List<UITestObject> collection3) {
        this.collection3 = collection3;
    }

    public List<UITestObject> getCollection4() {
        return collection4;
    }

    public void setCollection4(List<UITestObject> collection4) {
        this.collection4 = collection4;
    }

    public List<UITestObject> getCollection5() {
        return collection5;
    }

    public void setCollection5(List<UITestObject> collection5) {
        this.collection5 = collection5;
    }

    public List<UIInactivatableTestObject> getInactivatableCollection() {
        return inactivatableCollection;
    }

    public void setInactivatableCollection(List<UIInactivatableTestObject> inactivatableCollection) {
        this.inactivatableCollection = inactivatableCollection;
    }

    public List<UITestObject> getGroupedCollection1() {
        return groupedCollection1;
    }

    public void setGroupedCollection1(List<UITestObject> groupedCollection1) {
        this.groupedCollection1 = groupedCollection1;
    }

    public List<UITestObject> getGroupedCollection2() {
        return groupedCollection2;
    }

    public void setGroupedCollection2(List<UITestObject> groupedCollection2) {
        this.groupedCollection2 = groupedCollection2;
    }

    public List<UITestObject> getGroupedCollection3() {
        return groupedCollection3;
    }

    public void setGroupedCollection3(List<UITestObject> groupedCollection3) {
        this.groupedCollection3 = groupedCollection3;
    }

    public List<UITestObject> getDoubleGroupedCollection() {
        return doubleGroupedCollection;
    }

    public void setDoubleGroupedCollection(List<UITestObject> doubleGroupedCollection) {
        this.doubleGroupedCollection = doubleGroupedCollection;
    }

    public List<UITestObject> getEmptyCollection() {
        return emptyCollection;
    }

    public void setEmptyCollection(List<UITestObject> emptyCollection) {
        this.emptyCollection = emptyCollection;
    }

    public String getFakeTotal() {
        return fakeTotal;
    }

    public void setFakeTotal(String fakeTotal) {
        this.fakeTotal = fakeTotal;
    }

    public String getRichMessageField() {
        return richMessageField;
    }

    public void setRichMessageField(String richMessageField) {
        this.richMessageField = richMessageField;
    }

    public List<String> getCheckboxesField1() {
        return checkboxesField1;
    }

    public void setCheckboxesField1(List<String> checkboxesField1) {
        this.checkboxesField1 = checkboxesField1;
    }

    public List<String> getCheckboxesField2() {
        return checkboxesField2;
    }

    public void setCheckboxesField2(List<String> checkboxesField2) {
        this.checkboxesField2 = checkboxesField2;
    }

    public List<String> getCheckboxesField3() {
        return checkboxesField3;
    }

    public void setCheckboxesField3(List<String> checkboxesField3) {
        this.checkboxesField3 = checkboxesField3;
    }

    public List<String> getCheckboxesField4() {
        return checkboxesField4;
    }

    public void setCheckboxesField4(List<String> checkboxesField4) {
        this.checkboxesField4 = checkboxesField4;
    }

    public List<String> getMultiSelectField1() {
        return multiSelectField1;
    }

    public void setMultiSelectField1(List<String> multiSelectField1) {
        this.multiSelectField1 = multiSelectField1;
    }

    public boolean isBooleanField1() {
        return booleanField1;
    }

    public void setBooleanField1(boolean booleanField1) {
        this.booleanField1 = booleanField1;
    }

    public boolean isBooleanField2() {
        return booleanField2;
    }

    public void setBooleanField2(boolean booleanField2) {
        this.booleanField2 = booleanField2;
    }

    public boolean isBooleanField3() {
        return booleanField3;
    }

    public void setBooleanField3(boolean booleanField3) {
        this.booleanField3 = booleanField3;
    }

    /**
     * @return the tree1
     */
    public Tree<String, String> getTree1() {
        return this.tree1;
    }

    /**
     * @param tree1 the tree1 to set
     */
    public void setTree1(Tree<String, String> tree1) {
        this.tree1 = tree1;
    }

    /**
     * @return the tree2
     */
    public Tree<UITestObject, String> getTree2() {
        return tree2;
    }

    /**
     * @param tree1 the tree2 to set
     */
    public void setTree2(Tree<UITestObject, String> tree2) {
        this.tree2 = tree2;
    }

    /**
     * @param subList the subList to set
     */
    public void setSubList(List<UITestObject> subList) {
        this.subList = subList;
    }

    /**
     * @return the subList
     */
    public List<UITestObject> getSubList() {
        return subList;
    }

    public List<String> getOptionListSelection() {
        return optionListSelection;
    }

    public void setOptionListSelection(List<String> optionListSelection) {
        this.optionListSelection = optionListSelection;
    }

    public String getCodeSample() {
        return codeSample;
    }

    public void setCodeSample(String codeSample) {
        this.codeSample = codeSample;
    }
}