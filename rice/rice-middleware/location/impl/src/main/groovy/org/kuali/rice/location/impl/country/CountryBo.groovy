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

package org.kuali.rice.location.impl.country

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

import org.hibernate.annotations.Type
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.location.api.country.Country
import org.kuali.rice.location.framework.country.CountryEbo

@Entity
@Table(name="KRLC_CNTRY_T")
class CountryBo extends PersistableBusinessObjectBase implements CountryEbo {

  @Id
  @Column(name = "POSTAL_CNTRY_CD")
  def String code;

  @Column(name = "ALT_POSTAL_CNTRY_CD")
  def String alternateCode;

  @Column(name = "POSTAL_CNTRY_NM")
  def String name;

  @Type(type = "yes_no")
  @Column(name = "PSTL_CNTRY_RSTRC_IND")
  def boolean restricted;

  @Type(type = "yes_no")
  @Column(name = "ACTV_IND")
  def boolean active;

  /**
   * Converts a mutable CountryBo to an immutable Country representation.
   * @param bo
   * @return an immutable Country
   */
  static Country to(CountryBo bo) {
    if (bo == null) { return null }
    return Country.Builder.create(bo).build()
  }

  /**
   * Creates a CountryBo business object from an immutable representation of a Country.
   * @param an immutable Country
   * @return a CountryBo
   */
  static CountryBo from(Country immutable) {
    if (immutable == null) {return null}

    CountryBo bo = new CountryBo()
    bo.code = immutable.code
    bo.alternateCode = immutable.alternateCode
    bo.name = immutable.name
    bo.restricted = immutable.restricted
    bo.active = immutable.active
    bo.versionNumber = immutable.versionNumber

    return bo;
  }
}
