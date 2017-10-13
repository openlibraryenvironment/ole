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
package edu.sampleu.demo.kitchensink;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Form for Test UI Page
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifComponentsTestForm extends UifFormBase {
    private static final long serialVersionUID = -7525378097732916418L;

    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;
    private String field6;
    private String field7;
    private String field8;
    private String field9;
    private String field10;
    private String field11;
    private String field12;
    private String field13;
    private String field14;
    private String field15;
    private String field16;
    private String field17;
    private String field18;
    private String field19;
    private String field20;
    private String field21;
    private String field22;
    private String field23;
    private String field24;
    private String field25;
    private String field26;
    private String field27;
    private String field28;
    private String field29;
    private String field30;
    private String field31;
    private String field32;
    private String field33;
    private String field34;
    private String field35;
    private String field36;
    private String field37;
    private String field38;
    private String field39;
    private String field40;
    private String field41;
    private String field42;
    private String field43;
    private String field44;
    private String field45;
    private String field46;
    private String field47 = "";
    private String field48;
    private String field49;
    private String field50;
    private String field51;
    private String field52;
    private String field53;
    private String field54;
    private String field55;
    private String field56;
    private String field57;
    private String field58;
    private String field59;
    private String field60;
    private String field61;
    private String field62;
    private String field63;
    private String field64;
    private String field65;
    private String field66;
    private String field67;
    private String field68;
    private String field69;
    private String field70;
    private String field71;
    private String field72;
    private String field73;
    private String field74;
    private String field75;
    private String field76;
    private String field77;
    private String field78;
    private String field79;
    private String field80;
    private String field81;
    private String field82;
    private String field83;
    private String field84;
    private String field85;
    private String field86;
    private String field87;
    private String field88;
    private String field89;
    private String field90;
    private String field91;
    private String field92;
    private String field100;
    private String field101;
    private String field102;
    private String field103;
    private String field104;
    private String field105;
    private String field106;
    private String field107;
    private String field108;
    private String field109;
    private String field110;
    private String field111;
    private String field112;
    private String field113;
    private List<String> field114;
    private List<String> field115;
    private List<String> field116;
    private String field117;
    private String field118;
    private String field119;
    private List<String> field120;
    private String field121;
    private String field122;
    private String field123;
    private String field124;
    private String field125;
    private String field126;
    private String field127;
    private String field128;
    private String field129;
    private String field130;
    private Integer field131;
    private String field132;
    private String field133;
    private String field134 = "triggered by focus or and mouse over";
    private String field140;
    private String field141;
    private String field142;

    private String richMessageField = "[color=green][b]Message[/b][/color]";
    private String richMessageField2 =
            "Form Message with input specified by id-[id=Demo-SampleMessageInput3] and inlineComponent index number-[0]";
    private String testValueField = "Sample Data";

    private String testPersonId;
    private Person testPerson;

    private String gField1;
    private String gField2;
    private String gField3;

    private String mField1 = "SecretInfo555";
    private String mField2 = "SecretInfo111";
    private String mField3 = "SecretInfo222";
    private String fakeTotal = "123(server value)";

    private List<String> stringList1;
    private List<String> stringList2;
    private List<String> stringList3 = new ArrayList<String>(Arrays.asList("String1", "String2", "String3", "String4"));
    private List<String> stringList4 = new ArrayList<String>(Arrays.asList("String1", "String2", "String3", "String4"));
    private List<Integer> intList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
    private List<Double> doubleList = new ArrayList<Double>(Arrays.asList(1.1, 2.2, 3.3, 4.4));
    private List<Date> dateList;

    private MultipartFile fileUpload;

    private UITestObject uiTestObject;

    private List<UITestObject> list1 = new ArrayList<UITestObject>();
    private List<UITestObject> list2 = new ArrayList<UITestObject>();
    private List<UITestObject> list3 = new ArrayList<UITestObject>();
    private List<UITestObject> list4 = new ArrayList<UITestObject>();
    private List<UITestObject> list5 = new ArrayList<UITestObject>();
    private List<UITestObject> list6 = new ArrayList<UITestObject>();
    private List<UITestObject> emptyList = new ArrayList<UITestObject>();
    private List<UITestObject> groupedList1 = new ArrayList<UITestObject>();
    private List<UITestObject> groupedList2 = new ArrayList<UITestObject>();
    private List<UITestObject> groupedList3 = new ArrayList<UITestObject>();
    private List<UITestObject> doubleGroupedList = new ArrayList<UITestObject>();
    private List<UITestObject> list1generated = new ArrayList<UITestObject>();
    private List<UITestObject> list2generated = new ArrayList<UITestObject>();
    private List<UITestObject> list3generated = new ArrayList<UITestObject>();
    private List<TimeInfo> listTimeInfo = new ArrayList<TimeInfo>();
    private List<DayEvent> dayEvents = new ArrayList<DayEvent>();

    private Tree<String, String> tree1 = new Tree<String, String>();
    private Tree<UITestObject, String> tree2 = new Tree<UITestObject, String>();

    private Boolean bField1;
    private boolean bField2;
    private boolean bField3;
    private boolean booleanField;

    private int intField;

    private Date date1;
    private Date date2;
    private Date date3;
    private Date date4;
    private Date date5;
    private Date date6;
    private Date date7;

    private Map<String, Object> remoteFieldValuesMap;
    private Map<String, Object> remoteFieldValuesMap2;

    private String sourceCodeField;

    public UifComponentsTestForm() {
        super();

        uiTestObject = new UITestObject("Foo", "FooBar", "FooBear", "FooRacket");

        sourceCodeField =
                "&lt;bean parent=\"Uif-PrimaryActionButton\" p:actionLabel=\"Save\" p:methodToCall=\"performSave\"&gt;\n"
                        + "&#32;&#32;&lt;property name=\"actionImage\"&gt;\n"
                        + "&#32;&#32;&#32;&#32;&lt;bean parent=\"Uif-Image\"\n"
                        + "&#32;&#32;&#32;&#32;&#32;&#32;p:source=\"@{#ConfigProperties['krad.externalizable.images.url']}searchicon.png\"\n"
                        + "&#32;&#32;&#32;&#32;&#32;&#32;p:actionImageLocation=\"RIGHT\"/&gt;\n"
                        + "&#32;&#32;&lt;/property&gt;\n"
                        + "&lt;/bean&gt;";

        list1.add(new UITestObject("5", "6", "7", "8", new UITestObject("1", "1", "1", "1")));
        UITestObject obj1 = new UITestObject("1", "2", "3", "4", new UITestObject("1", "1", "1", "1"));
        obj1.setStringList(null);
        list1.add(obj1);
        UITestObject obj2 = new UITestObject("9", "10", "11", "12", new UITestObject("1", "1", "1", "1"));
        obj2.setStringList(new ArrayList<String>());
        list1.add(obj2);
        list1.add(new UITestObject("13", "14", "15", "16", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("17", "18", "19", "20", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("52", "6", "7", "8", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("12", "2", "3", "4", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("92", "10", "11", "12", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("132", "14", "15", "16", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("2132", "143", "151", "126", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("1332", "144", "155", "156", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("2132", "143", "151", "126", new UITestObject("1", "1", "1", "1")));
        list1.add(new UITestObject("1332", "144", "155", "156", new UITestObject("1", "1", "1", "1")));

        list2.add(new UITestObject("A", "B", "C", "D"));
        list2.add(new UITestObject("1", "2", "3", "4"));
        list2.add(new UITestObject("W", "X", "Y", "Z"));
        list2.add(new UITestObject("a", "b", "c", "d"));
        list2.add(new UITestObject("a", "s", "d", "f"));

        list3.add(new UITestObject("A", "B", "C", "D"));
        list3.get(0).getSubList().add(new UITestObject("A", "B", "C", "D"));
        list3.get(0).getSubList().add(new UITestObject("1", "2", "3", "4"));
        list3.get(0).getSubList().add(new UITestObject("W", "X", "Y", "Z"));
        list3.add(new UITestObject("1", "2", "3", "4"));
        list3.get(1).getSubList().add(new UITestObject("A", "B", "C", "D"));
        list3.get(1).getSubList().add(new UITestObject("1", "2", "3", "4"));
        list3.add(new UITestObject("W", "X", "Y", "Z"));
        list3.get(2).getSubList().add(new UITestObject("W", "X", "Y", "Z"));

        list4.add(new UITestObject("A", "B", "C", "D"));
        list4.get(0).getSubList().add(new UITestObject("1", "B", "C", "D", new UITestObject("1", "1", "1", "1")));
        list4.get(0).getSubList().add(new UITestObject("2", "2", "3", "4", new UITestObject("1", "1", "1", "1")));
        list4.get(0).getSubList().add(new UITestObject("3", "X", "Y", "Z", new UITestObject("1", "1", "1", "1")));
        list4.add(new UITestObject("1", "2", "3", "4"));
        list4.get(1).getSubList().add(new UITestObject("4", "b", "C", "D", new UITestObject("$50.00", "1", "1", "1")));
        /*list4.get(1).getSubList().add(new UITestObject("5", "s", "D", "F", new UITestObject("1","1","1","1")));*/

        //triple nesting
        list5.add(new UITestObject("a", "a", "a", "a"));
        list5.get(0).getSubList().add(new UITestObject("A", "B", "C", "D"));
        list5.get(0).getSubList().get(0).getSubList().add(new UITestObject("a3", "3", "3", "3"));
        list5.get(0).getSubList().get(0).getSubList().add(new UITestObject("a3", "3", "3", "3"));
        list5.get(0).getSubList().add(new UITestObject("1", "2", "3", "4"));
        list5.get(0).getSubList().get(1).getSubList().add(new UITestObject("b3", "3", "3", "3"));
        list5.get(0).getSubList().get(1).getSubList().add(new UITestObject("b3", "3", "3", "3"));
        list5.get(0).getSubList().get(1).getSubList().add(new UITestObject("b3", "3", "3", "3"));
        list5.add(new UITestObject("b", "b", "b", "b"));
        list5.get(1).getSubList().add(new UITestObject("a", "b", "C", "D"));
        list5.get(1).getSubList().get(0).getSubList().add(new UITestObject("a23", "3", "3", "3"));
        list5.get(1).getSubList().get(0).getSubList().add(new UITestObject("a23", "3", "3", "3"));
        list5.get(1).getSubList().add(new UITestObject("a", "s", "D", "F"));
        list5.get(1).getSubList().get(1).getSubList().add(new UITestObject("b23", "3", "3", "3"));
        list5.get(1).getSubList().get(1).getSubList().add(new UITestObject("b23", "3", "3", "3"));

        groupedList1.add(new UITestObject("A", "100", "200", "300"));
        groupedList1.add(new UITestObject("A", "101", "200", "300"));
        groupedList1.add(new UITestObject("A", "102", "200", "300"));
        groupedList1.add(new UITestObject("A", "103", "200", "300"));
        groupedList1.add(new UITestObject("A", "104", "200", "300"));

        groupedList1.add(new UITestObject("B", "100", "200", "300"));
        groupedList1.add(new UITestObject("B", "101", "200", "300"));
        groupedList1.add(new UITestObject("B", "102", "200", "300"));

        groupedList1.add(new UITestObject("C", "100", "200", "300"));
        groupedList1.add(new UITestObject("C", "101", "200", "300"));
        groupedList1.add(new UITestObject("C", "102", "200", "300"));
        groupedList1.add(new UITestObject("C", "103", "200", "300"));

        groupedList1.add(new UITestObject("D", "100", "200", "300"));
        groupedList1.add(new UITestObject("D", "101", "200", "300"));
        groupedList1.add(new UITestObject("D", "102", "200", "300"));
        groupedList1.add(new UITestObject("D", "103", "200", "300"));
        groupedList1.add(new UITestObject("D", "100", "200", "300"));
        groupedList1.add(new UITestObject("D", "101", "200", "300"));
        groupedList1.add(new UITestObject("D", "102", "200", "300"));
        groupedList1.add(new UITestObject("D", "103", "200", "300"));
        groupedList1.add(new UITestObject("D", "100", "200", "300"));
        groupedList1.add(new UITestObject("D", "101", "200", "300"));
        groupedList1.add(new UITestObject("D", "102", "200", "300"));
        groupedList1.add(new UITestObject("D", "103", "200", "300"));
        groupedList1.add(new UITestObject("D", "100", "200", "300"));
        groupedList1.add(new UITestObject("D", "101", "200", "300"));
        groupedList1.add(new UITestObject("D", "102", "200", "300"));
        groupedList1.add(new UITestObject("D", "103", "200", "300"));
        groupedList1.add(new UITestObject("D", "100", "200", "300"));
        groupedList1.add(new UITestObject("D", "101", "200", "300"));
        groupedList1.add(new UITestObject("D", "102", "200", "300"));
        groupedList1.add(new UITestObject("D", "103", "200", "300"));
        groupedList1.add(new UITestObject("D", "100", "200", "300"));
        groupedList1.add(new UITestObject("D", "101", "200", "300"));
        groupedList1.add(new UITestObject("D", "102", "200", "300"));
        groupedList1.add(new UITestObject("D", "103", "200", "300"));

        groupedList2.addAll(groupedList1);
        groupedList3.addAll(groupedList1);

        doubleGroupedList.add(new UITestObject("Fall", "2001", "AAA123", "2"));
        doubleGroupedList.add(new UITestObject("Fall", "2001", "BBB123", "3"));
        doubleGroupedList.add(new UITestObject("Fall", "2001", "CCC123", "4"));
        doubleGroupedList.add(new UITestObject("Fall", "2001", "DDD123", "3"));

        doubleGroupedList.add(new UITestObject("Fall", "2002", "AAA123", "3"));
        doubleGroupedList.add(new UITestObject("Fall", "2002", "BBB123", "2"));
        doubleGroupedList.add(new UITestObject("Fall", "2002", "CCC123", "3"));

        doubleGroupedList.add(new UITestObject("Fall", "2003", "AAA123", "3"));
        doubleGroupedList.add(new UITestObject("Fall", "2003", "CCC123", "3"));

        doubleGroupedList.add(new UITestObject("Spring", "2001", "AAA123", "3"));
        doubleGroupedList.add(new UITestObject("Spring", "2001", "BBB123", "3"));
        doubleGroupedList.add(new UITestObject("Spring", "2001", "CCC123", "3"));

        doubleGroupedList.add(new UITestObject("Spring", "2002", "AAA123", "4"));
        doubleGroupedList.add(new UITestObject("Spring", "2002", "BBB123", "4"));
        doubleGroupedList.add(new UITestObject("Spring", "2002", "CCC123", "2"));

        doubleGroupedList.add(new UITestObject("Spring", "2003", "AAA123", "4"));
        doubleGroupedList.add(new UITestObject("Spring", "2003", "BBB123", "3"));
        doubleGroupedList.add(new UITestObject("Spring", "2003", "CCC123", "3"));
        doubleGroupedList.add(new UITestObject("Spring", "2003", "DDD123", "2"));

        for (int i = 0; i < 22; i++) {
            UITestObject newObj = new UITestObject(RandomStringUtils.randomAlphanumeric(6),
                    RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6),
                    RandomStringUtils.randomNumeric(1));
            if (i % 2 == 0) {
                newObj.setBfield(true);
            }
            list6.add(newObj);
        }

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

        remoteFieldValuesMap = new HashMap<String, Object>();
        remoteFieldValuesMap.put("remoteField1", "Apple");
        remoteFieldValuesMap.put("remoteField2", "Banana");
        remoteFieldValuesMap.put("remoteField3", true);
        remoteFieldValuesMap.put("remoteField4", "Fruit");

        remoteFieldValuesMap2 = new HashMap<String, Object>();
        remoteFieldValuesMap2.put("remoteField1", "Apple");
        remoteFieldValuesMap2.put("remoteField2", "Banana");
        remoteFieldValuesMap2.put("remoteField3", true);
        remoteFieldValuesMap2.put("remoteField4", "Fruit");

        field88 = "Fruits";
        field91 = "Read only value";
        field92 = "Value 92";

        field131 = new Integer(0);

        DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
        dateList = new ArrayList<Date>();
        try {
            dateList.add(dateFormat.parse("01/01/1990"));
            dateList.add(dateFormat.parse("10/31/2001"));
            dateList.add(dateFormat.parse("11/05/2005"));
            dateList.add(dateFormat.parse("02/13/2011"));
        } catch (Exception e) {

        }

        emptyList.clear();
    }

    @Override
    public void postBind(HttpServletRequest request) {
        super.postBind(request);
    }

    /**
     * @return the field1
     */
    public String getField1() {
        return this.field1;
    }

    /**
     * @param field1 the field1 to set
     */
    public void setField1(String field1) {
        this.field1 = field1;
    }

    /**
     * @return the field2
     */
    public String getField2() {
        return this.field2;
    }

    /**
     * @param field2 the field2 to set
     */
    public void setField2(String field2) {
        this.field2 = field2;
    }

    /**
     * @return the field3
     */
    public String getField3() {
        return this.field3;
    }

    /**
     * @param field3 the field3 to set
     */
    public void setField3(String field3) {
        this.field3 = field3;
    }

    /**
     * @return the field4
     */
    public String getField4() {
        return this.field4;
    }

    /**
     * @param field4 the field4 to set
     */
    public void setField4(String field4) {
        this.field4 = field4;
    }

    /**
     * @return the field5
     */
    public String getField5() {
        return this.field5;
    }

    /**
     * @param field5 the field5 to set
     */
    public void setField5(String field5) {
        this.field5 = field5;
    }

    /**
     * @return the field6
     */
    public String getField6() {
        return this.field6;
    }

    /**
     * @param field6 the field6 to set
     */
    public void setField6(String field6) {
        this.field6 = field6;
    }

    /**
     * @return the field7
     */
    public String getField7() {
        return this.field7;
    }

    /**
     * @param field7 the field7 to set
     */
    public void setField7(String field7) {
        this.field7 = field7;
    }

    /**
     * @return the field8
     */
    public String getField8() {
        return this.field8;
    }

    /**
     * @param field8 the field8 to set
     */
    public void setField8(String field8) {
        this.field8 = field8;
    }

    /**
     * @return the field9
     */
    public String getField9() {
        return this.field9;
    }

    /**
     * @param field9 the field9 to set
     */
    public void setField9(String field9) {
        this.field9 = field9;
    }

    /**
     * @return the field10
     */
    public String getField10() {
        return this.field10;
    }

    /**
     * @param field10 the field10 to set
     */
    public void setField10(String field10) {
        this.field10 = field10;
    }

    /**
     * @return the field11
     */
    public String getField11() {
        return this.field11;
    }

    /**
     * @param field11 the field11 to set
     */
    public void setField11(String field11) {
        this.field11 = field11;
    }

    /**
     * @return the field12
     */
    public String getField12() {
        return this.field12;
    }

    /**
     * @param field12 the field12 to set
     */
    public void setField12(String field12) {
        this.field12 = field12;
    }

    /**
     * @return the field13
     */
    public String getField13() {
        return this.field13;
    }

    /**
     * @param field13 the field13 to set
     */
    public void setField13(String field13) {
        this.field13 = field13;
    }

    /**
     * @return the field14
     */
    public String getField14() {
        return this.field14;
    }

    /**
     * @param field14 the field14 to set
     */
    public void setField14(String field14) {
        this.field14 = field14;
    }

    /**
     * @return the field15
     */
    public String getField15() {
        return this.field15;
    }

    /**
     * @param field15 the field15 to set
     */
    public void setField15(String field15) {
        this.field15 = field15;
    }

    /**
     * @return the field16
     */
    public String getField16() {
        return this.field16;
    }

    /**
     * @param field16 the field16 to set
     */
    public void setField16(String field16) {
        this.field16 = field16;
    }

    /**
     * @return the field17
     */
    public String getField17() {
        return this.field17;
    }

    /**
     * @param field17 the field17 to set
     */
    public void setField17(String field17) {
        this.field17 = field17;
    }

    /**
     * @return the field18
     */
    public String getField18() {
        return this.field18;
    }

    /**
     * @param field18 the field18 to set
     */
    public void setField18(String field18) {
        this.field18 = field18;
    }

    /**
     * @return the field19
     */
    public String getField19() {
        return this.field19;
    }

    /**
     * @param field19 the field19 to set
     */
    public void setField19(String field19) {
        this.field19 = field19;
    }

    /**
     * @return the field20
     */
    public String getField20() {
        return this.field20;
    }

    /**
     * @param field20 the field20 to set
     */
    public void setField20(String field20) {
        this.field20 = field20;
    }

    /**
     * @return the field21
     */
    public String getField21() {
        return this.field21;
    }

    /**
     * @param field21 the field21 to set
     */
    public void setField21(String field21) {
        this.field21 = field21;
    }

    /**
     * @return the field22
     */
    public String getField22() {
        return this.field22;
    }

    /**
     * @param field22 the field22 to set
     */
    public void setField22(String field22) {
        this.field22 = field22;
    }

    /**
     * @return the field23
     */
    public String getField23() {
        return this.field23;
    }

    /**
     * @param field23 the field23 to set
     */
    public void setField23(String field23) {
        this.field23 = field23;
    }

    /**
     * @return the field24
     */
    public String getField24() {
        return this.field24;
    }

    /**
     * @param field24 the field24 to set
     */
    public void setField24(String field24) {
        this.field24 = field24;
    }

    /**
     * @return the field25
     */
    public String getField25() {
        return this.field25;
    }

    /**
     * @param field25 the field25 to set
     */
    public void setField25(String field25) {
        this.field25 = field25;
    }

    /**
     * @return the field26
     */
    public String getField26() {
        return this.field26;
    }

    /**
     * @param field26 the field26 to set
     */
    public void setField26(String field26) {
        this.field26 = field26;
    }

    /**
     * @return the field27
     */
    public String getField27() {
        return this.field27;
    }

    /**
     * @param field27 the field27 to set
     */
    public void setField27(String field27) {
        this.field27 = field27;
    }

    /**
     * @return the field28
     */
    public String getField28() {
        return this.field28;
    }

    /**
     * @param field28 the field28 to set
     */
    public void setField28(String field28) {
        this.field28 = field28;
    }

    /**
     * @return the field29
     */
    public String getField29() {
        return this.field29;
    }

    /**
     * @param field29 the field29 to set
     */
    public void setField29(String field29) {
        this.field29 = field29;
    }

    /**
     * @return the field30
     */
    public String getField30() {
        return this.field30;
    }

    /**
     * @param field30 the field30 to set
     */
    public void setField30(String field30) {
        this.field30 = field30;
    }

    /**
     * @return the field31
     */
    public String getField31() {
        return this.field31;
    }

    /**
     * @param field31 the field31 to set
     */
    public void setField31(String field31) {
        this.field31 = field31;
    }

    /**
     * @return the field32
     */
    public String getField32() {
        return this.field32;
    }

    /**
     * @param field32 the field32 to set
     */
    public void setField32(String field32) {
        this.field32 = field32;
    }

    /**
     * @return the field33
     */
    public String getField33() {
        return this.field33;
    }

    /**
     * @param field33 the field33 to set
     */
    public void setField33(String field33) {
        this.field33 = field33;
    }

    /**
     * @return the field34
     */
    public String getField34() {
        return this.field34;
    }

    /**
     * @return the field35
     */
    public String getField35() {
        return this.field35;
    }

    /**
     * @param field35 the field35 to set
     */
    public void setField35(String field35) {
        this.field35 = field35;
    }

    /**
     * @return the field36
     */
    public String getField36() {
        return this.field36;
    }

    /**
     * @param field36 the field36 to set
     */
    public void setField36(String field36) {
        this.field36 = field36;
    }

    /**
     * @return the field37
     */
    public String getField37() {
        return this.field37;
    }

    /**
     * @param field37 the field37 to set
     */
    public void setField37(String field37) {
        this.field37 = field37;
    }

    /**
     * @return the field38
     */
    public String getField38() {
        return this.field38;
    }

    /**
     * @param field38 the field38 to set
     */
    public void setField38(String field38) {
        this.field38 = field38;
    }

    /**
     * @return the field39
     */
    public String getField39() {
        return this.field39;
    }

    /**
     * @param field39 the field39 to set
     */
    public void setField39(String field39) {
        this.field39 = field39;
    }

    /**
     * @return the field40
     */
    public String getField40() {
        return this.field40;
    }

    /**
     * @param field40 the field40 to set
     */
    public void setField40(String field40) {
        this.field40 = field40;
    }

    /**
     * @return the field41
     */
    public String getField41() {
        return this.field41;
    }

    /**
     * @param field41 the field41 to set
     */
    public void setField41(String field41) {
        this.field41 = field41;
    }

    /**
     * @return the field42
     */
    public String getField42() {
        return this.field42;
    }

    /**
     * @param field42 the field42 to set
     */
    public void setField42(String field42) {
        this.field42 = field42;
    }

    /**
     * @return the field43
     */
    public String getField43() {
        return this.field43;
    }

    /**
     * @param field43 the field43 to set
     */
    public void setField43(String field43) {
        this.field43 = field43;
    }

    /**
     * @return the field44
     */
    public String getField44() {
        return this.field44;
    }

    /**
     * @param field44 the field44 to set
     */
    public void setField44(String field44) {
        this.field44 = field44;
    }

    /**
     * @return the field45
     */
    public String getField45() {
        return this.field45;
    }

    /**
     * @param field45 the field45 to set
     */
    public void setField45(String field45) {
        this.field45 = field45;
    }

    /**
     * @return the field46
     */
    public String getField46() {
        return this.field46;
    }

    /**
     * @param field46 the field46 to set
     */
    public void setField46(String field46) {
        this.field46 = field46;
    }

    /**
     * @return the field47
     */
    public String getField47() {
        return this.field47;
    }

    /**
     * @param field47 the field47 to set
     */
    public void setField47(String field47) {
        this.field47 = field47;
    }

    /**
     * @return the field48
     */
    public String getField48() {
        return this.field48;
    }

    /**
     * @param field48 the field48 to set
     */
    public void setField48(String field48) {
        this.field48 = field48;
    }

    /**
     * @return the field49
     */
    public String getField49() {
        return this.field49;
    }

    /**
     * @param field49 the field49 to set
     */
    public void setField49(String field49) {
        this.field49 = field49;
    }

    /**
     * @return the field50
     */
    public String getField50() {
        return this.field50;
    }

    /**
     * @param field50 the field50 to set
     */
    public void setField50(String field50) {
        this.field50 = field50;
    }

    /**
     * @return the field51
     */
    public String getField51() {
        return this.field51;
    }

    /**
     * @param field51 the field51 to set
     */
    public void setField51(String field51) {
        this.field51 = field51;
    }

    /**
     * @return the field52
     */
    public String getField52() {
        return this.field52;
    }

    /**
     * @param field52 the field52 to set
     */
    public void setField52(String field52) {
        this.field52 = field52;
    }

    /**
     * @return the field53
     */
    public String getField53() {
        return this.field53;
    }

    /**
     * @param field53 the field53 to set
     */
    public void setField53(String field53) {
        this.field53 = field53;
    }

    /**
     * @return the field54
     */
    public String getField54() {
        return this.field54;
    }

    /**
     * @param field54 the field54 to set
     */
    public void setField54(String field54) {
        this.field54 = field54;
    }

    /**
     * @return the field55
     */
    public String getField55() {
        return this.field55;
    }

    /**
     * @param field55 the field55 to set
     */
    public void setField55(String field55) {
        this.field55 = field55;
    }

    /**
     * @return the field56
     */
    public String getField56() {
        return this.field56;
    }

    /**
     * @param field56 the field56 to set
     */
    public void setField56(String field56) {
        this.field56 = field56;
    }

    /**
     * @return the field57
     */
    public String getField57() {
        return this.field57;
    }

    /**
     * @param field57 the field57 to set
     */
    public void setField57(String field57) {
        this.field57 = field57;
    }

    /**
     * @return the field58
     */
    public String getField58() {
        return this.field58;
    }

    /**
     * @param field58 the field58 to set
     */
    public void setField58(String field58) {
        this.field58 = field58;
    }

    /**
     * @return the field59
     */
    public String getField59() {
        return this.field59;
    }

    /**
     * @param field59 the field59 to set
     */
    public void setField59(String field59) {
        this.field59 = field59;
    }

    /**
     * @return the field60
     */
    public String getField60() {
        return this.field60;
    }

    /**
     * @param field60 the field60 to set
     */
    public void setField60(String field60) {
        this.field60 = field60;
    }

    /**
     * @return the field61
     */
    public String getField61() {
        return this.field61;
    }

    /**
     * @param field61 the field61 to set
     */
    public void setField61(String field61) {
        this.field61 = field61;
    }

    /**
     * @return the field62
     */
    public String getField62() {
        return this.field62;
    }

    /**
     * @param field62 the field62 to set
     */
    public void setField62(String field62) {
        this.field62 = field62;
    }

    /**
     * @return the field63
     */
    public String getField63() {
        return this.field63;
    }

    /**
     * @param field63 the field63 to set
     */
    public void setField63(String field63) {
        this.field63 = field63;
    }

    /**
     * @return the field64
     */
    public String getField64() {
        return this.field64;
    }

    /**
     * @param field64 the field64 to set
     */
    public void setField64(String field64) {
        this.field64 = field64;
    }

    /**
     * @return the field65
     */
    public String getField65() {
        return this.field65;
    }

    /**
     * @param field65 the field65 to set
     */
    public void setField65(String field65) {
        this.field65 = field65;
    }

    /**
     * @param field34 the field34 to set
     */
    public void setField34(String field34) {
        this.field34 = field34;
    }

    public Boolean getbField1() {
        return bField1;
    }

    public void setbField1(Boolean bField1) {
        this.bField1 = bField1;
    }

    /**
     * @return the bField2
     */
    public boolean isbField2() {
        return this.bField2;
    }

    /**
     * @param bField2 the bField2 to set
     */
    public void setbField2(boolean bField2) {
        this.bField2 = bField2;
    }

    /**
     * @return the bField3
     */
    public boolean isbField3() {
        return this.bField3;
    }

    /**
     * @param bField3 the bField3 to set
     */
    public void setbField3(boolean bField3) {
        this.bField3 = bField3;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    /**
     * @return the list1
     */
    public List<UITestObject> getList1() {
        return this.list1;
    }

    /**
     * @param list1 the list1 to set
     */
    public void setList1(List<UITestObject> list1) {
        this.list1 = list1;
    }

    /**
     * @return the list2
     */
    public List<UITestObject> getList2() {
        return this.list2;
    }

    /**
     * @param list2 the list2 to set
     */
    public void setList2(List<UITestObject> list2) {
        this.list2 = list2;
    }

    /**
     * @return the list3
     */
    public List<UITestObject> getList3() {
        return this.list3;
    }

    /**
     * @param list3 the list3 to set
     */
    public void setList3(List<UITestObject> list3) {
        this.list3 = list3;
    }

    /**
     * @return the list4
     */
    public List<UITestObject> getList4() {
        return this.list4;
    }

    /**
     * @param list4 the list4 to set
     */
    public void setList4(List<UITestObject> list4) {
        this.list4 = list4;
    }

    /**
     * @return the gField1
     */
    public String getgField1() {
        return this.gField1;
    }

    /**
     * @param gField1 the gField1 to set
     */
    public void setgField1(String gField1) {
        this.gField1 = gField1;
    }

    /**
     * @return the gField2
     */
    public String getgField2() {
        return this.gField2;
    }

    /**
     * @param gField2 the gField2 to set
     */
    public void setgField2(String gField2) {
        this.gField2 = gField2;
    }

    /**
     * @return the gField3
     */
    public String getgField3() {
        return this.gField3;
    }

    /**
     * @param gField3 the gField3 to set
     */
    public void setgField3(String gField3) {
        this.gField3 = gField3;
    }

    /**
     * @return the field66
     */
    public String getField66() {
        return this.field66;
    }

    /**
     * @param field66 the field66 to set
     */
    public void setField66(String field66) {
        this.field66 = field66;
    }

    /**
     * @return the field67
     */
    public String getField67() {
        return this.field67;
    }

    /**
     * @param field67 the field67 to set
     */
    public void setField67(String field67) {
        this.field67 = field67;
    }

    /**
     * @return the field68
     */
    public String getField68() {
        return this.field68;
    }

    /**
     * @param field68 the field68 to set
     */
    public void setField68(String field68) {
        this.field68 = field68;
    }

    /**
     * @return the field69
     */
    public String getField69() {
        return this.field69;
    }

    /**
     * @param field69 the field69 to set
     */
    public void setField69(String field69) {
        this.field69 = field69;
    }

    /**
     * @return the field70
     */
    public String getField70() {
        return this.field70;
    }

    /**
     * @param field70 the field70 to set
     */
    public void setField70(String field70) {
        this.field70 = field70;
    }

    /**
     * @return the field71
     */
    public String getField71() {
        return this.field71;
    }

    /**
     * @param field71 the field71 to set
     */
    public void setField71(String field71) {
        this.field71 = field71;
    }

    /**
     * @return the field72
     */
    public String getField72() {
        return this.field72;
    }

    /**
     * @param field72 the field72 to set
     */
    public void setField72(String field72) {
        this.field72 = field72;
    }

    /**
     * @return the field73
     */
    public String getField73() {
        return this.field73;
    }

    /**
     * @param field73 the field73 to set
     */
    public void setField73(String field73) {
        this.field73 = field73;
    }

    /**
     * @return the field74
     */
    public String getField74() {
        return this.field74;
    }

    /**
     * @param field74 the field74 to set
     */
    public void setField74(String field74) {
        this.field74 = field74;
    }

    /**
     * @return the field75
     */
    public String getField75() {
        return this.field75;
    }

    /**
     * @param field75 the field75 to set
     */
    public void setField75(String field75) {
        this.field75 = field75;
    }

    /**
     * @return the field76
     */
    public String getField76() {
        return this.field76;
    }

    /**
     * @param field76 the field76 to set
     */
    public void setField76(String field76) {
        this.field76 = field76;
    }

    /**
     * @return the field77
     */
    public String getField77() {
        return this.field77;
    }

    /**
     * @param field77 the field77 to set
     */
    public void setField77(String field77) {
        this.field77 = field77;
    }

    /**
     * @return the field78
     */
    public String getField78() {
        return this.field78;
    }

    /**
     * @param field78 the field78 to set
     */
    public void setField78(String field78) {
        this.field78 = field78;
    }

    /**
     * @return the field79
     */
    public String getField79() {
        return this.field79;
    }

    /**
     * @param field79 the field79 to set
     */
    public void setField79(String field79) {
        this.field79 = field79;
    }

    /**
     * @return the field80
     */
    public String getField80() {
        return this.field80;
    }

    /**
     * @param field80 the field80 to set
     */
    public void setField80(String field80) {
        this.field80 = field80;
    }

    /**
     * @return the field81
     */
    public String getField81() {
        return this.field81;
    }

    /**
     * @param field81 the field81 to set
     */
    public void setField81(String field81) {
        this.field81 = field81;
    }

    public String getMField1() {
        return mField1;
    }

    public void setMField1(String mField1) {
        this.mField1 = mField1;
    }

    public String getMField2() {
        return mField2;
    }

    public void setMField2(String mField2) {
        this.mField2 = mField2;
    }

    public String getMField3() {
        return mField3;
    }

    public void setMField3(String mField3) {
        this.mField3 = mField3;
    }

    /**
     * @return the list5
     */
    public List<UITestObject> getList5() {
        return this.list5;
    }

    /**
     * @param list5 the list5 to set
     */
    public void setList5(List<UITestObject> list5) {
        this.list5 = list5;
    }

    /**
     * @return the list6
     */
    public List<UITestObject> getList6() {
        return this.list6;
    }

    /**
     * @param list6 the list6 to set
     */
    public void setList6(List<UITestObject> list6) {
        this.list6 = list6;
    }

    public List<UITestObject> getEmptyList() {
        return emptyList;
    }

    public void setEmptyList(List<UITestObject> emptyList) {
        this.emptyList = emptyList;
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

    public Tree<UITestObject, String> getTree2() {
        return tree2;
    }

    public void setTree2(Tree<UITestObject, String> tree2) {
        this.tree2 = tree2;
    }

    public String getField82() {
        return field82;
    }

    public void setField82(String field82) {
        this.field82 = field82;
    }

    public String getField83() {
        return field83;
    }

    public void setField83(String field83) {
        this.field83 = field83;
    }

    public String getField84() {
        return field84;
    }

    public void setField84(String field84) {
        this.field84 = field84;
    }

    public String getField85() {
        return field85;
    }

    public void setField85(String field85) {
        this.field85 = field85;
    }

    public String getField86() {
        return field86;
    }

    public void setField86(String field86) {
        this.field86 = field86;
    }

    public String getField87() {
        return field87;
    }

    public void setField87(String field87) {
        this.field87 = field87;
    }

    public String getField88() {
        return field88;
    }

    public void setField88(String field88) {
        this.field88 = field88;
    }

    public String getField89() {
        return field89;
    }

    public void setField89(String field89) {
        this.field89 = field89;
    }

    public Map<String, Object> getRemoteFieldValuesMap() {
        return remoteFieldValuesMap;
    }

    public void setRemoteFieldValuesMap(Map<String, Object> remoteFieldValuesMap) {
        this.remoteFieldValuesMap = remoteFieldValuesMap;
    }

    public Map<String, Object> getRemoteFieldValuesMap2() {
        return remoteFieldValuesMap2;
    }

    public void setRemoteFieldValuesMap2(Map<String, Object> remoteFieldValuesMap2) {
        this.remoteFieldValuesMap2 = remoteFieldValuesMap2;
    }

    public String getField90() {
        return field90;
    }

    public void setField90(String field90) {
        this.field90 = field90;
    }

    public String getField91() {
        return field91;
    }

    public void setField91(String field91) {
        this.field91 = field91;
    }

    public String getField92() {
        return field92;
    }

    public void setField92(String field92) {
        this.field92 = field92;
    }

    public String getField100() {
        return field100;
    }

    public void setField100(String field100) {
        this.field100 = field100;
    }

    public String getField101() {
        return field101;
    }

    public void setField101(String field101) {
        this.field101 = field101;
    }

    public String getField102() {
        return field102;
    }

    public void setField102(String field102) {
        this.field102 = field102;
    }

    public String getField103() {
        return field103;
    }

    public void setField103(String field103) {
        this.field103 = field103;
    }

    public String getField104() {
        return field104;
    }

    public void setField104(String field104) {
        this.field104 = field104;
    }

    public String getField105() {
        return field105;
    }

    public void setField105(String field105) {
        this.field105 = field105;
    }

    public String getField106() {
        return field106;
    }

    public void setField106(String field106) {
        this.field106 = field106;
    }

    public String getField107() {
        return field107;
    }

    public void setField107(String field107) {
        this.field107 = field107;
    }

    public String getField108() {
        return field108;
    }

    public void setField108(String field108) {
        this.field108 = field108;
    }

    public String getField110() {
        return field110;
    }

    public void setField110(String field110) {
        this.field110 = field110;
    }

    public String getField109() {
        return field109;
    }

    public void setField109(String field109) {
        this.field109 = field109;
    }

    public String getField111() {
        return field111;
    }

    public void setField111(String field111) {
        this.field111 = field111;
    }

    public String getField112() {
        return field112;
    }

    public void setField112(String field112) {
        this.field112 = field112;
    }

    public String getField113() {
        return field113;
    }

    public void setField113(String field113) {
        this.field113 = field113;
    }

    public List<String> getField114() {
        return field114;
    }

    public void setField114(List<String> field114) {
        this.field114 = field114;
    }

    public List<String> getField115() {
        return field115;
    }

    public void setField115(List<String> field115) {
        this.field115 = field115;
    }

    public List<String> getField116() {
        return field116;
    }

    public void setField116(List<String> field116) {
        this.field116 = field116;
    }

    public String getField117() {
        return field117;
    }

    public void setField117(String field117) {
        this.field117 = field117;
    }

    public String getField118() {
        return field118;
    }

    public void setField118(String field118) {
        this.field118 = field118;
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

    public String getField119() {
        return field119;
    }

    public void setField119(String field119) {
        this.field119 = field119;
    }

    public List<String> getField120() {
        return field120;
    }

    public void setField120(List<String> field120) {
        this.field120 = field120;
    }

    public String getField121() {
        return field121;
    }

    public void setField121(String field121) {
        this.field121 = field121;
    }

    public String getField122() {
        return field122;
    }

    public void setField122(String field122) {
        this.field122 = field122;
    }

    public String getField123() {
        return field123;
    }

    public void setField123(String field123) {
        this.field123 = field123;
    }

    public String getField124() {
        return field124;
    }

    public void setField124(String field124) {
        this.field124 = field124;
    }

    public String getField125() {
        return field125;
    }

    public void setField125(String field125) {
        this.field125 = field125;
    }

    public String getField126() {
        return field126;
    }

    public void setField126(String field126) {
        this.field126 = field126;
    }

    public String getField127() {
        return field127;
    }

    public void setField127(String field127) {
        this.field127 = field127;
    }

    public String getField128() {
        return field128;
    }

    public void setField128(String field128) {
        this.field128 = field128;
    }

    public String getField129() {
        return field129;
    }

    public void setField129(String field129) {
        this.field129 = field129;
    }

    public String getField130() {
        return field130;
    }

    public void setField130(String field130) {
        this.field130 = field130;
    }

    public UITestObject getUiTestObject() {
        return uiTestObject;
    }

    public void setUiTestObject(UITestObject uiTestObject) {
        this.uiTestObject = uiTestObject;
    }

    public Integer getField131() {
        return field131;
    }

    public void setField131(Integer field131) {
        this.field131 = field131;
    }

    public String getField132() {
        return field132;
    }

    public void setField132(String field132) {
        this.field132 = field132;
    }

    public String getField133() {
        return field133;
    }

    public void setField133(String field133) {
        this.field133 = field133;
    }

    public String getField134() {
        return field134;
    }

    public void setField134(String field134) {
        this.field134 = field134;
    }

    public String getField140() {
        return field140;
    }

    public void setField140(String field140) {
        this.field140 = field140;
    }

    public String getField141() {
        return field141;
    }

    public void setField141(String field141) {
        this.field141 = field141;
    }

    public String getField142() {
        return field142;
    }

    public void setField142(String field142) {
        this.field142 = field142;
    }

    public MultipartFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(MultipartFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public List<TimeInfo> getListTimeInfo() {
        return listTimeInfo;
    }

    public void setListTimeInfo(List<TimeInfo> listTimeInfo) {
        this.listTimeInfo = listTimeInfo;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    public Date getDate4() {
        return date4;
    }

    public void setDate4(Date date4) {
        this.date4 = date4;
    }

    public Date getDate5() {
        return date5;
    }

    public void setDate5(Date date5) {
        this.date5 = date5;
    }

    public Date getDate6() {
        return date6;
    }

    public void setDate6(Date date6) {
        this.date6 = date6;
    }

    public Date getDate7() {
        return date7;
    }

    public void setDate7(Date date7) {
        this.date7 = date7;
    }

    public List<DayEvent> getDayEvents() {
        return dayEvents;
    }

    public void setDayEvents(List<DayEvent> dayEvents) {
        this.dayEvents = dayEvents;
    }

    public List<UITestObject> getList1generated() {
        return list1generated;
    }

    public void setList1generated(List<UITestObject> list1generated) {
        this.list1generated = list1generated;
    }

    public List<UITestObject> getList2generated() {
        return list2generated;
    }

    public void setList2generated(List<UITestObject> list2generated) {
        this.list2generated = list2generated;
    }

    public List<UITestObject> getList3generated() {
        return list3generated;
    }

    public void setList3generated(List<UITestObject> list3generated) {
        this.list3generated = list3generated;
    }

    public String getRichMessageField() {
        return richMessageField;
    }

    public void setRichMessageField(String richMessageField) {
        this.richMessageField = richMessageField;
    }

    public String getTestValueField() {
        return testValueField;
    }

    public void setTestValueField(String testValueField) {
        this.testValueField = testValueField;
    }

    public String getRichMessageField2() {
        return richMessageField2;
    }

    public void setRichMessageField2(String richMessageField2) {
        this.richMessageField2 = richMessageField2;
    }

    public String getCurrentTimestamp() {
        DateTimeService dateTimeService = CoreApiServiceLocator.getDateTimeService();
        return dateTimeService.getCurrentTimestamp().toString();
    }

    public List<UITestObject> getGroupedList1() {
        return groupedList1;
    }

    public void setGroupedList1(List<UITestObject> groupedList1) {
        this.groupedList1 = groupedList1;
    }

    public List<UITestObject> getGroupedList2() {
        return groupedList2;
    }

    public void setGroupedList2(List<UITestObject> groupedList2) {
        this.groupedList2 = groupedList2;
    }

    public List<UITestObject> getGroupedList3() {
        return groupedList3;
    }

    public void setGroupedList3(List<UITestObject> groupedList3) {
        this.groupedList3 = groupedList3;
    }

    public List<UITestObject> getDoubleGroupedList() {
        return doubleGroupedList;
    }

    public void setDoubleGroupedList(List<UITestObject> doubleGroupedList) {
        this.doubleGroupedList = doubleGroupedList;
    }

    public String getFakeTotal() {
        return fakeTotal;
    }

    public void setFakeTotal(String fakeTotal) {
        this.fakeTotal = fakeTotal;
    }

    public List<String> getStringList1() {
        return stringList1;
    }

    public void setStringList1(List<String> stringList1) {
        this.stringList1 = stringList1;
    }

    public List<String> getStringList2() {
        return stringList2;
    }

    public void setStringList2(List<String> stringList2) {
        this.stringList2 = stringList2;
    }

    public List<String> getStringList3() {
        return stringList3;
    }

    public void setStringList3(List<String> stringList3) {
        this.stringList3 = stringList3;
    }

    public List<String> getStringList4() {
        return stringList4;
    }

    public void setStringList4(List<String> stringList4) {
        this.stringList4 = stringList4;
    }

    public List<Integer> getIntList() {
        return intList;
    }

    public void setIntList(List<Integer> intList) {
        this.intList = intList;
    }

    public List<Double> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }

    public List<Date> getDateList() {
        return dateList;
    }

    public void setDateList(List<Date> dateList) {
        this.dateList = dateList;
    }

    public String getSourceCodeField() {
        return sourceCodeField;
    }

    public void setSourceCodeField(String sourceCodeField) {
        this.sourceCodeField = sourceCodeField;
    }
}