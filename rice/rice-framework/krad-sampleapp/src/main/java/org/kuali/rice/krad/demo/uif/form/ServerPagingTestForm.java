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

import org.kuali.rice.krad.web.form.UifFormBase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * form for the server paging component library page
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServerPagingTestForm extends UifFormBase {

    private static final long serialVersionUID = -8790636700086973158L;

    // Fields needed on component library forms
    private String themeName;
    private String exampleShown;
    private String currentExampleIndex;

    List<ServerPagingTestObject> collection1;
    private List<UITestObject> collection2 = new ArrayList<UITestObject>();
    List<ServerPagingTestObject> collection3;

    public ServerPagingTestForm() {
        int collection1Size = 1000;
        this.collection1 = new ArrayList<ServerPagingTestObject>(collection1Size);
        Random random = new Random(2);

        for (int i = 1; i < collection1Size; i++) {
            ServerPagingTestObject listItem = new ServerPagingTestObject();

            listItem.setIntegerField(i);

            // random rating between 8 and 10
            BigDecimal rating = new BigDecimal(BigInteger.valueOf(random.nextInt(200) + 800), 2);
            listItem.setDoubleField(rating);

            String standardDesc = ", I give XKCD " + i + " a rating of " + listItem.getDoubleField();

            if (rating.doubleValue() < 8.3) {
                listItem.setStringField("Funny" + standardDesc);
            } else if (rating.doubleValue() < 8.6) {
                listItem.setStringField("Good one" + standardDesc);
            } else if (rating.doubleValue() < 8.9) {
                listItem.setStringField("Really liked it" + standardDesc);
            } else if (rating.doubleValue() < 9.2) {
                listItem.setStringField("Clever" + standardDesc);
            } else if (rating.doubleValue() < 9.5) {
                listItem.setStringField("Brilliant" + standardDesc);
            } else if (rating.doubleValue() < 9.8) {
                listItem.setStringField("Stupendous" + standardDesc);
            } else {
                listItem.setStringField("Celestial" + standardDesc);
            }

            collection1.add(listItem);
        }

        collection2.add(new UITestObject("A", "100", "200", "300"));
        collection2.add(new UITestObject("A", "101", "200", "300"));
        collection2.add(new UITestObject("A", "102", "200", "300"));
        collection2.add(new UITestObject("A", "103", "200", "300"));
        collection2.add(new UITestObject("A", "104", "200", "300"));

        collection2.add(new UITestObject("B", "100", "200", "300"));
        collection2.add(new UITestObject("B", "101", "200", "300"));
        collection2.add(new UITestObject("B", "102", "200", "300"));

        collection2.add(new UITestObject("C", "100", "200", "300"));
        collection2.add(new UITestObject("C", "101", "200", "300"));
        collection2.add(new UITestObject("C", "102", "200", "300"));
        collection2.add(new UITestObject("C", "103", "200", "300"));

        collection2.add(new UITestObject("D", "100", "200", "300"));
        collection2.add(new UITestObject("D", "101", "200", "300"));
        collection2.add(new UITestObject("D", "102", "200", "300"));

        collection2.add(new UITestObject("E", "100", "200", "300"));
        collection2.add(new UITestObject("E", "101", "200", "300"));
        collection2.add(new UITestObject("E", "102", "200", "300"));

        collection2.add(new UITestObject("F", "100", "200", "300"));
        collection2.add(new UITestObject("F", "101", "200", "300"));
        collection2.add(new UITestObject("F", "102", "200", "300"));

        collection2.add(new UITestObject("G", "100", "200", "300"));
        collection2.add(new UITestObject("G", "101", "200", "300"));
        collection2.add(new UITestObject("G", "102", "200", "300"));

        collection2.add(new UITestObject("H", "100", "200", "300"));
        collection2.add(new UITestObject("H", "101", "200", "300"));
        collection2.add(new UITestObject("H", "102", "200", "300"));
        collection2.add(new UITestObject("H", "103", "200", "300"));
        collection2.add(new UITestObject("H", "104", "200", "300"));

        collection2.add(new UITestObject("I", "100", "200", "300"));
        collection2.add(new UITestObject("I", "101", "200", "300"));
        collection2.add(new UITestObject("I", "102", "200", "300"));
        collection2.add(new UITestObject("I", "103", "200", "300"));
        collection2.add(new UITestObject("I", "104", "200", "300"));

        this.collection3 = new ArrayList<ServerPagingTestObject>(collection1);
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getExampleShown() {
        return exampleShown;
    }

    public void setExampleShown(String exampleShown) {
        this.exampleShown = exampleShown;
    }

    public String getCurrentExampleIndex() {
        return currentExampleIndex;
    }

    public void setCurrentExampleIndex(String currentExampleIndex) {
        this.currentExampleIndex = currentExampleIndex;
    }

    public List<ServerPagingTestObject> getCollection1() {
        return collection1;
    }

    public void setCollection1(List<ServerPagingTestObject> collection1) {
        this.collection1 = collection1;
    }

    public List<UITestObject> getCollection2() {
        return collection2;
    }

    public void setCollection2(List<UITestObject> collection2) {
        this.collection2 = collection2;
    }

    public List<ServerPagingTestObject> getCollection3() {
        return collection3;
    }

    public void setCollection3(List<ServerPagingTestObject> collection3) {
        this.collection3 = collection3;
    }
}
