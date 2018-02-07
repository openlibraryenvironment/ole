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
package org.kuali.rice.krad.dao.impl;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.krad.bo.Attachment;
import org.kuali.rice.krad.dao.AttachmentDao;

/**
 * This class is the OJB implementation of the NoteDao interface.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AttachmentDaoOjb extends PlatformAwareDaoBaseOjb implements AttachmentDao {
    private static Logger LOG = Logger.getLogger(AttachmentDaoOjb.class);

    /**
     * Default constructor.
     */
    public AttachmentDaoOjb() {
        super();
    }
    
    public Attachment getAttachmentByNoteId(Long noteId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("noteIdentifier", noteId);
        return (Attachment) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(Attachment.class, crit));          
    }

}
