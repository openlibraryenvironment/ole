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
package org.kuali.rice.krad.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Ad Hoc Route Person Business Object
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@IdClass(AdHocRoutePersonId.class)
@Entity
@Table(name = "KRNS_ADHOC_RTE_ACTN_RECIP_T")
public class AdHocRoutePerson extends AdHocRouteRecipient {
    private static final long serialVersionUID = 1L;

    @Transient
    private transient Person person;

    public AdHocRoutePerson() {
        setType(PERSON_TYPE);

        try {
            person = (Person) KimApiServiceLocator.getPersonService().getPersonImplementationClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setType(Integer type) {
        if (!PERSON_TYPE.equals(type)) {
            throw new IllegalArgumentException("cannot change type to " + type);
        }
        super.setType(type);
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (StringUtils.isNotBlank(id)) {
            person = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(id);
            setPerson(person);
        }
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        if (StringUtils.isNotBlank(name) && getId() != null &&
                ((person != null) && !StringUtils.equals(person.getName(), name))) {
            person = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(getId());
            setPerson(person);
        }
    }

    public Person getPerson() {
        if ((person == null) || !StringUtils.equals(person.getPrincipalName(), getId())) {
            person = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(getId());

            if (person == null) {
                try {
                    person = (Person) KimApiServiceLocator.getPersonService().getPersonImplementationClass()
                            .newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        if (person != null) {
            this.id = person.getPrincipalName();
            this.name = person.getName();
        }
    }
}

