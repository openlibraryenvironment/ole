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
package org.kuali.rice.krad.dao.proxy;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.krad.bo.Attachment;
import org.kuali.rice.krad.dao.AttachmentDao;
public class AttachmentDaoProxy implements AttachmentDao {

    private AttachmentDao attachmentDaoJpa;
    private AttachmentDao attachmentDaoOjb;
	
    private AttachmentDao getDao(Class clazz) {
    	return (OrmUtils.isJpaAnnotated(clazz) && (OrmUtils.isJpaEnabled() || OrmUtils.isJpaEnabled("rice.krad"))) ? attachmentDaoJpa : attachmentDaoOjb;
    }
    
	
    public Attachment getAttachmentByNoteId(Long noteId){
    	return getDao(Attachment.class).getAttachmentByNoteId(noteId);
    }


	public void setAttachmentDaoJpa(AttachmentDao attachmentDaoJpa) {
		this.attachmentDaoJpa = attachmentDaoJpa;
	}

	public void setAttachmentDaoOjb(AttachmentDao attachmentDaoOjb) {
		this.attachmentDaoOjb = attachmentDaoOjb;
	}

}
