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
package org.kuali.rice.kew.web;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree of boolean flags which represent the state of show/hide for the GUI.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ShowHideTree implements java.io.Serializable {

	private static final long serialVersionUID = 6048341469002946402L;

	private Boolean show = Boolean.TRUE;
    private List children = new ArrayList();
    
    public ShowHideTree() {
    }
    
    public List getChildren() {
        return children;
    }
    
    public void setChildren(List children) {
        this.children = children;
    }
        
    public ShowHideTree getChild(Integer value) {
        return getChild(value.intValue());
    }
    
    public ShowHideTree getChild(int value) {
        for (int index = children.size(); index <= value; index++) {
            children.add(new ShowHideTree());
        }
        return (ShowHideTree)children.get(value);
    }
    
    public ShowHideTree append() {
        return getChild(children.size());
    }
    
    public ShowHideTree remove(Integer index) {
        return remove(index.intValue());
    }
    
    public ShowHideTree remove(int index) {
        return (ShowHideTree)children.remove(index);
    }
        
    public Boolean getShow() {
        return show;
    }
    
    public void setShow(Boolean show) {
        this.show = show;
    }
        
}
