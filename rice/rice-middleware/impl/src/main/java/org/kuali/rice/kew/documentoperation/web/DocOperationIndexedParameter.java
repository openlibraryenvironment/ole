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
package org.kuali.rice.kew.documentoperation.web;

import java.io.Serializable;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocOperationIndexedParameter implements Serializable {

	private static final long serialVersionUID = -7519728468541922828L;
	private Integer index;
    private String value;
    
    public DocOperationIndexedParameter(){
    }
    
    public DocOperationIndexedParameter(Integer index, String value){
        this.index = index;
        this.value = value;
    }
   
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
