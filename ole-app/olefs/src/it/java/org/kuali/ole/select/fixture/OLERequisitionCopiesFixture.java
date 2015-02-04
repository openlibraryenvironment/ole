/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.select.fixture;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.select.businessobject.OleRequisitionCopies;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;

import java.math.BigDecimal;

/**
 * This fixture provides necessary copy values to Requisition and PO documents.
 */

public enum OLERequisitionCopiesFixture {
    MULTI_COPIES_MULTI_PARTS(null,//itemCopiesId
        null, //itemIdentifier
        new KualiInteger(2), //parts
        new KualiDecimal(3), //itemCopies
        "e", //partEnumeration
        "API", //locationCopies
        new KualiInteger(1), //startingCopyNumber
        "c", //caption
        "1,2" //volumeNumber
    ),
    BASIC_COPY_1(null,//itemCopiesId
            null, //itemIdentifier
            new KualiInteger(1), //parts
            new KualiDecimal(3), //itemCopies
            "e", //partEnumeration
            "PALCI", //locationCopies
            new KualiInteger(1), //startingCopyNumber
            "c", //caption
            "1" //volumeNumber
    ),
    BASIC_COPY_2(null,//itemCopiesId
            null, //itemIdentifier
            new KualiInteger(1), //parts
            new KualiDecimal(1), //itemCopies
            "e", //partEnumeration
            "ARCH/PA", //locationCopies
            new KualiInteger(1), //startingCopyNumber
            "c", //caption
            "1" //volumeNumber
    ),
    BASIC_COPY_3(null,//itemCopiesId
            null, //itemIdentifier
            new KualiInteger(1), //parts
            new KualiDecimal(1), //itemCopies
            "e", //partEnumeration
            "ARCH/PSA", //locationCopies
            new KualiInteger(1), //startingCopyNumber
            "c", //caption
            "1" //volumeNumber
    ),;

    protected Integer itemCopiesId;
    protected Integer itemIdentifier;
    protected KualiInteger parts;
    protected KualiDecimal itemCopies;
    protected String partEnumeration;
    protected String locationCopies;
    protected KualiInteger startingCopyNumber;
    protected String caption;
    protected String volumeNumber;

    private OLERequisitionCopiesFixture(Integer itemCopiesId, Integer itemIdentifier, KualiInteger parts, KualiDecimal itemCopies, String partEnumeration, String locationCopies, KualiInteger startingCopyNumber, String caption, String volumeNumber) {
        this.itemCopiesId = itemCopiesId;
        this.itemIdentifier = itemIdentifier;
        this.parts = parts;
        this.itemCopies =itemCopies;
        this.partEnumeration = partEnumeration;
        this.locationCopies = locationCopies;
        this.startingCopyNumber = startingCopyNumber;
        this.caption = caption;
        this.volumeNumber = volumeNumber;
    }

    public OleRequisitionCopies createOleRequisitionCopies() {
        OleRequisitionCopies oleRequisitionCopies = new OleRequisitionCopies();
        oleRequisitionCopies.setItemCopiesId(itemCopiesId);
        oleRequisitionCopies.setItemIdentifier(itemIdentifier);
        oleRequisitionCopies.setParts(parts);
        oleRequisitionCopies.setItemCopies(itemCopies);
        oleRequisitionCopies.setPartEnumeration(partEnumeration);
        oleRequisitionCopies.setLocationCopies(locationCopies);
        oleRequisitionCopies.setStartingCopyNumber(startingCopyNumber);
        oleRequisitionCopies.setCaption(caption);
        oleRequisitionCopies.setVolumeNumber(volumeNumber);
        return oleRequisitionCopies;
    }
}
