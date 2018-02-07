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
package org.kuali.rice.kim.bo.ui

import org.junit.Test
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo
import org.kuali.rice.kim.impl.identity.entity.EntityBo
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo
import org.kuali.rice.kim.test.BoPersistenceTest
import org.kuali.rice.kim.test.Factory
import org.joda.time.DateTime

/**
 * Tests persisting PersonDocumentAddress object in order to verify ORM mappings
 */
class PersonDocumentAddressPersistenceTest extends BoPersistenceTest {
    @Test
    void test_save_persondocumentaddress() {
        EntityBo entity = Factory.make(EntityBo)
        boService.save(entity)
        EntityTypeContactInfoBo ent_type = Factory.make(EntityTypeContactInfoBo, entity: entity)
        boService.save(ent_type)
        EntityAddressBo addr = Factory.make(EntityAddressBo, entity: entity)
        boService.save(addr)

        PersonDocumentAddress pda = new PersonDocumentAddress([
            addressType: addr.addressType,
            addressTypeCode: addr.addressTypeCode,
            entityTypeCode: ent_type.entityTypeCode,
            documentNumber: System.currentTimeMillis(),
            city: addr.cityUnmasked,
            stateProvinceCode: addr.stateProvinceCodeUnmasked,
            postalCode: addr.postalCodeUnmasked,
            countryCode: addr.countryCodeUnmasked,
            attentionLine: addr.attentionLineUnmasked,
            line1: addr.line1Unmasked,
            line2: addr.line2Unmasked,
            line3: addr.line3Unmasked,
            addressFormat: addr.addressFormat,
            modifiedDate: toDbTimestamp(addr.modifiedDate),
            validatedDate: toDbTimestamp(addr.validatedDate),
            validated: addr.validated,
            noteMessage: addr.noteMessage
        ])

        boService.save(pda)

        assertRow(kimdoc_fields(pda) + [
            ENTITY_ADDR_ID: pda.entityAddressId,
            ATTN_LINE: pda.attentionLine,
            ADDR_LINE_1: pda.line1,
            ADDR_LINE_2: pda.line2,
            ADDR_LINE_3: pda.line3,
            STATE_PVC_CD: pda.stateProvinceCode,
            POSTAL_CD: pda.postalCode,
            POSTAL_CNTRY_CD: pda.countryCode,
            ADDR_TYP_CD: pda.addressTypeCode,
            ADDR_FMT: pda.addressFormat,
            MOD_DT: toDbTimestamp(pda.modifiedDate.time),
            VALID_DT: toDbTimestamp(pda.validatedDate.time),
            VALID_IND: pda.validated ? "Y" : "N",
            NOTE_MSG: pda.noteMessage,
            CITY: pda.city,
            DISPLAY_SORT_CD: null
        ],
        "KRIM_PND_ADDR_MT", "ENTITY_ADDR_ID")
    }
}