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
package org.kuali.rice.kcb.service.impl;

import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.springframework.beans.factory.annotation.Required;

/**
 * Base class for service implementations handling business objects 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BusinessObjectServiceImpl {
    protected GenericDao dao;

    /**
     * Sets our BusinessObjectDao
     * @param bos the BusinessObjectDao
     */
    @Required
    public void setGenericDao(GenericDao dao) {
        this.dao = dao;
    }
}
