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
package edu.sampleu.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TestSuggestClass {

    public static List<String> getLanguages(String term) {
        List<String> matchingLanguages = new ArrayList<String>();

        String[] languageArray =
                {"ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion", "Erlang",
                        "Fortran", "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby",
                        "Scala", "Scheme"};

        for (int i = 0; i < languageArray.length; i++) {
            String language = languageArray[i];
            if (language.toLowerCase().startsWith(term.toLowerCase())) {
                matchingLanguages.add(language);
            }
        }

        return matchingLanguages;
    }

    public static List<String> getAllLanguages() {
        String[] languageArray =
                {"ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion", "Erlang",
                        "Fortran", "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby",
                        "Scala", "Scheme"};

        return Arrays.asList(languageArray);
    }

    public static List<TestLabelValue> getRichOptions() {
        List<TestLabelValue> options = new ArrayList<TestLabelValue>();

        options.add(new TestLabelValue("r1", "<b>Rich Option 1</b><br/><i>this is a desc for option 1</i>"));
        options.add(new TestLabelValue("r1", "<b>Rich Option 2</b><br/><i>this is a desc for option 2</i>"));
        options.add(new TestLabelValue("r1", "<b>Rich Option 3</b><br/><i>this is a desc for option 3</i>"));

        return options;
    }

    public static List<TestSuggestObject> getComplexOptions() {
        List<TestSuggestObject> options = new ArrayList<TestSuggestObject>();

        options.add(new TestSuggestObject("1", "jhbon", "Bohan, Jack"));
        options.add(new TestSuggestObject("2", "jmcross", "Cross, Jeff"));
        options.add(new TestSuggestObject("3", "jomot", "Mot, Joe"));

        return options;
    }

    public static List<TestLocationObject> getLocationOptions() {
        List<TestLocationObject> options = new ArrayList<TestLocationObject>();

        options.add(new TestLocationObject(null, "Google", "http://www.google.com"));
        options.add(new TestLocationObject(null, "Kuali", "http://www.kuali.org"));
        options.add(new TestLocationObject(null, "Jira", "https://jira.kuali.org"));

        return options;
    }

    public static List<TestViewObject> getViewOptions() {
        List<TestViewObject> options = new ArrayList<TestViewObject>();

        options.add(new TestViewObject("UifCompView"));
        options.add(new TestViewObject("RichMessagesView"));
        options.add(new TestViewObject("ConfigurationTestView-Collections"));
        options.add(new TestViewObject("ConfigurationTestView"));

        return options;
    }

    public static List<TestObject> getObjectOptions(String term) {
        List<TestObject> options = new ArrayList<TestObject>();

        options.add(new TestObject("Apple", "AppleType", "aaa"));
        options.add(new TestObject("Orange", "OrangeType", "ooo"));
        options.add(new TestObject("Strawberry", "StrawberryType", "sss"));

        return options;
    }

    public static class TestObject {
        private String name;
        private String val;
        private String data;

        public TestObject(String name, String val, String data) {
            this.name = name;
            this.val = val;
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class TestViewObject extends TestLabelValue{

        private String id;

        public TestViewObject(String id){
            super(id, id);
            this.id = id;
        }

        public TestViewObject(String value, String label, String id) {
            super(value, label);
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class TestLabelValue {
        private String label;
        private String value;

        public TestLabelValue() {

        }

        public TestLabelValue(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class TestSuggestObject extends TestLabelValue {
        private String description;

        public TestSuggestObject(String value, String label, String description) {
            super(value, label);
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private static class TestLocationObject extends TestLabelValue {
        private String href;

        public TestLocationObject(String value, String label, String href) {
            super(value, label);
            this.href = href;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

    }
}
