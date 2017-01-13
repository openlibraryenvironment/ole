/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

/**
 * This class... OleCopies Business Object
 */
public interface OleCopies extends PersistableBusinessObject {

    public Integer getItemCopiesId();

    public void setItemCopiesId(Integer itemCopiesId);

    public Integer getItemIdentifier();

    public void setItemIdentifier(Integer itemIdentifier);

    public KualiInteger getParts();

    public void setParts(KualiInteger parts);

    public String getPartEnumeration();

    public void setPartEnumeration(String partEnumeration);

    public String getLocationCopies();

    public void setLocationCopies(String locationCopies);

    public KualiInteger getStartingCopyNumber();

    public void setStartingCopyNumber(KualiInteger startingCopyNumber);

    public void setItemCopies(KualiDecimal itemCopies);

    public KualiDecimal getItemCopies();

    public String getVolumeNumber();

    public void setVolumeNumber(String volumeNumber);

    public String getCaption();

    public void setCaption(String caption);

    public void setSingleCopyNumber(KualiInteger singleCopyNumber);

    public KualiInteger getSingleCopyNumber();


}
