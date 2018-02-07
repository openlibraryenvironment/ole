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
package org.kuali.rice.location.impl.state

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

import org.hibernate.annotations.Type
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.location.api.state.State
import org.kuali.rice.location.framework.state.StateEbo
import org.kuali.rice.location.impl.country.CountryBo

@IdClass(StateId.class)
@Entity
@Table(name = "KRLC_ST_T")
class StateBo extends PersistableBusinessObjectBase implements StateEbo {

    @Id
    @Column(name = "POSTAL_STATE_CD")
    def String code;

    @Id
    @Column(name = "POSTAL_CNTRY_CD")
    def String countryCode;

    @Column(name = "POSTAL_STATE_NM")
    def String name;

    @Type(type = "yes_no")
    @Column(name = "ACTV_IND")
    def boolean active;

    @ManyToOne(targetEntity = CountryBo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "POSTAL_CNTRY_CD", insertable = false, updatable = false)
    def CountryBo country;

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return An immutable State if the passed in mutable is not null.  If the mutable reference was null, then null
     * is returned.
     */
    static State to(StateBo bo) {
        if (bo == null) {
            return null
        }

        return State.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return a new mutable CountryBo if the passed in immutable is not null.  If the immutable reference was null,
     * then null is returned.
     */
    static StateBo from(State im) {
        if (im == null) {
            return null
        }

        StateBo bo = new StateBo()
        bo.code = im.code
        bo.countryCode = im.countryCode
        bo.name = im.name
        bo.active = im.active
        bo.versionNumber = im.versionNumber

        return bo
    }
}
