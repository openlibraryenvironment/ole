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
package org.kuali.rice.ken.util;

import org.apache.log4j.Logger;
import org.kuali.rice.ken.bo.NotificationContentTypeBo;
import org.kuali.rice.ken.service.NotificationContentTypeService;

/**
 * Utility base class for Entity and LSResource Resolvers that should resolve
 * arguments to XSDs defined for NotificationContentTypes
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ContentTypeResourceResolver {
    protected static final String CONTENT_TYPE_PREFIX = "resource:notification/ContentType";

    protected final Logger LOG = Logger.getLogger(getClass());
    
    private NotificationContentTypeService notificationContentTypeService;

    /**
     * Constructs a ContentTypeResourceResolver.java.
     * @param notificationContentTypeService
     */
    public ContentTypeResourceResolver(NotificationContentTypeService notificationContentTypeService) {
	this.notificationContentTypeService = notificationContentTypeService;
    }

    /**
     * This method resolves the NotificationContentType by id.
     * @param id
     * @return
     */
    public NotificationContentTypeBo resolveContentType(String id) {
	if (!id.startsWith(CONTENT_TYPE_PREFIX)) return null;
        String contentType = id.substring(CONTENT_TYPE_PREFIX.length());
        NotificationContentTypeBo notificationContentType = notificationContentTypeService.getNotificationContentType(contentType);
        if (contentType == null) {
            LOG.warn("Content type '" + contentType + "' not found in notification database");
            return null;
        }

        LOG.info("Resolved '" + id + "' to notification content type: " + notificationContentType);
        return notificationContentType;
    }
}
