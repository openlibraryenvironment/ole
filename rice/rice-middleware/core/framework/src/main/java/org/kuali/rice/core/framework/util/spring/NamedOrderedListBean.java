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
package org.kuali.rice.core.framework.util.spring;

import java.util.LinkedList;
import java.util.List;

/**
 * Used to group items in spring for later retrieval by list name. Order is important, because it is used to
 * facilitate overrides
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NamedOrderedListBean {
    private String name;
    private List<String> list;

    public NamedOrderedListBean() {
        list = new LinkedList();
    }

    public void setListItem(String listItem) {
        list.add(listItem);
    }

    public void setListItems(List<String> listItems) {
        list.addAll(listItems);
    }

    public List<String> getList() {
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
