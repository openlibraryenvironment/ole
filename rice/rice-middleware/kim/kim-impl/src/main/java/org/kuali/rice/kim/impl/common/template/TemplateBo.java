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
package org.kuali.rice.kim.impl.common.template;

import javax.persistence.Column;
import org.hibernate.annotations.Type;
import org.kuali.rice.kim.api.common.template.TemplateContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

public abstract class TemplateBo extends PersistableBusinessObjectBase implements TemplateContract {

    @Column(name="NMSPC_CD")
    private String namespaceCode;

    @Column(name="NM")
    private String name;

    @Column(name="DESC_TXT", length=400)
    private String description;

    @Column(name="KIM_TYP_ID")
    private String kimTypeId;

    @Column(name="ACTV_IND")
    @Type(type="yes_no")
    private boolean active;

    @Override
    public String getNamespaceCode() {
        return namespaceCode;
    }

    public void setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getKimTypeId() {
        return kimTypeId;
    }

    public void setKimTypeId(String kimTypeId) {
        this.kimTypeId = kimTypeId;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}