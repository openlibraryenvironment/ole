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
package org.kuali.rice.krad.datadictionary.validation.constraint;

import java.io.Serializable;
import java.util.List;

/**
 * Direct copy of one that was in Kuali Student. Look up constraints are currently not implemented.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
public class CommonLookup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // unique ID of this lookup
    private String name; // name of this search
    private String desc;
    private String searchTypeId;
    private String resultReturnKey;
    private String searchParamIdKey;
    private List<CommonLookupParam> params;

    public String getSearchTypeId() {
        return searchTypeId;
    }

    public void setSearchTypeId(String searchTypeId) {
        this.searchTypeId = searchTypeId;
    }

    public String getResultReturnKey() {
        return resultReturnKey;
    }

    public void setResultReturnKey(String resultReturnKey) {
        this.resultReturnKey = resultReturnKey;
    }

    public List<CommonLookupParam> getParams() {
        return params;
    }

    public void setParams(List<CommonLookupParam> params) {
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSearchParamIdKey() {
        return searchParamIdKey;
    }

    public void setSearchParamIdKey(String searchParamIdKey) {
        this.searchParamIdKey = searchParamIdKey;
    }

}
