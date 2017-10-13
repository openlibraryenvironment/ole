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
package org.kuali.rice.krad.test;

import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;

import java.util.List;
import java.util.Map;

/**
 * Object for testing bean configurations
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TestDictionaryBean extends UifDictionaryBeanBase {

    private String property1;
    private String property2;
    private boolean property3;
    private int property4;

    private List<String> list1;
    private Map<String, String> map1;

    private TestDictionaryBean reference1;
    private TestDictionaryBean reference2;

    private List<TestDictionaryBean> listReference1;
    private List<TestDictionaryBean> listReference2;

    private Map<String, TestDictionaryBean> mapReference1;

    public TestDictionaryBean() {

    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public boolean isProperty3() {
        return property3;
    }

    public void setProperty3(boolean property3) {
        this.property3 = property3;
    }

    public int getProperty4() {
        return property4;
    }

    public void setProperty4(int property4) {
        this.property4 = property4;
    }

    public List<String> getList1() {
        return list1;
    }

    public void setList1(List<String> list1) {
        this.list1 = list1;
    }

    public Map<String, String> getMap1() {
        return map1;
    }

    public void setMap1(Map<String, String> map1) {
        this.map1 = map1;
    }

    public TestDictionaryBean getReference1() {
        return reference1;
    }

    public void setReference1(TestDictionaryBean reference1) {
        this.reference1 = reference1;
    }

    public TestDictionaryBean getReference2() {
        return reference2;
    }

    public void setReference2(TestDictionaryBean reference2) {
        this.reference2 = reference2;
    }

    public List<TestDictionaryBean> getListReference1() {
        return listReference1;
    }

    public void setListReference1(List<TestDictionaryBean> listReference1) {
        this.listReference1 = listReference1;
    }

    public List<TestDictionaryBean> getListReference2() {
        return listReference2;
    }

    public void setListReference2(List<TestDictionaryBean> listReference2) {
        this.listReference2 = listReference2;
    }

    public Map<String, TestDictionaryBean> getMapReference1() {
        return mapReference1;
    }

    public void setMapReference1(Map<String, TestDictionaryBean> mapReference1) {
        this.mapReference1 = mapReference1;
    }
}
