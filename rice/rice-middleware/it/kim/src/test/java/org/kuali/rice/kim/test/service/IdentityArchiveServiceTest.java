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
package org.kuali.rice.kim.test.service;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentStatusCategory;
import org.kuali.rice.kim.impl.identity.IdentityArchiveService;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.impl.identity.EntityDefaultInfoCacheBo;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.service.impl.IdentityArchiveServiceImpl;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.test.BaselineTestCase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Unit test for the IdentityArchiveService
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class IdentityArchiveServiceTest extends KIMTestCase {
    public static final String KIM_IDENTITY_ARCHIVE_SERVICE = "kimIdentityArchiveService";

	private IdentityArchiveService identityArchiveService;

    public static IdentityArchiveService getIdentityArchiveService() {
    	return GlobalResourceLoader.getService(KIM_IDENTITY_ARCHIVE_SERVICE);
    }

	public void setUp() throws Exception {
		super.setUp();
		final Map<String, Object> emptyMap = Collections.emptyMap();
		KRADServiceLocator.getBusinessObjectService().deleteMatching(EntityDefaultInfoCacheBo.class, emptyMap);
		if (null == identityArchiveService) {
			identityArchiveService = getIdentityArchiveService();
		}
	}

	/**
	 * This tests
	 * <ol><li>trying to retrieve a non-existant {@link EntityDefault}
	 * <li>saving a {@link EntityDefault} and retrieving it.
	 * </ol>
	 * This test is specific to {@link IdentityArchiveServiceImpl}
	 */
	@Test
	public void testArchiveFlushesWhenQueueIsFull() throws Exception {
		final int maxWriteQueueSize =
			Integer.valueOf(ConfigContext.getCurrentContextConfig().getProperty("kim.identityArchiveServiceImpl.maxWriteQueueSize"));


        //flush the archive service before trying this to make sure no records are sitting waiting for flush
        identityArchiveService.flushToArchive();
        // give it a second or 2 to flush
        log.info("Sleeping, waiting for the flush!");
        for (int j=2; j >= 0; j--) {
            Thread.sleep(1000);
            log.info(String.valueOf(j));
        }
        log.info("Done sleeping!");

		List<EntityDefault> added = new ArrayList<EntityDefault>();

		// exceed the max write queue size to initiate a flush
		for (int i=1; i<=(maxWriteQueueSize); i++) {
			MinimalKEDIBuilder builder = new MinimalKEDIBuilder("bogusUser" + i);
			builder.setEntityId("bogusUser" + i);
			EntityDefault bogusUserInfo = builder.build();

			EntityDefault retrieved = identityArchiveService.getEntityDefaultFromArchiveByPrincipalId(
                    builder.getPrincipalId());
			assertNull(retrieved);
			retrieved = identityArchiveService.getEntityDefaultFromArchiveByPrincipalName(builder.getPrincipalName());
			assertNull(retrieved);
			retrieved = identityArchiveService.getEntityDefaultFromArchive(builder.getEntityId());
			assertNull(retrieved);

			identityArchiveService.saveEntityDefaultToArchive(bogusUserInfo);
			added.add(bogusUserInfo);
		}

		// give it a second or 10 to flush
		log.info("Sleeping, hoping for a flush to occur!");
        for (int j=10; j >= 0; j--) {
            Thread.sleep(1000);
            log.info(String.valueOf(j));
        }
		log.info("Done sleeping!");

		// these should have been flushed by now, test retrieval

		for (EntityDefault kedi : added) {
			// retrieve it every way we can
			EntityDefault retrieved = identityArchiveService.getEntityDefaultFromArchiveByPrincipalId(
                    kedi.getPrincipals().get(0).getPrincipalId());
            assertNotNull("no value retrieved for principalId: " + kedi.getPrincipals().get(0).getPrincipalId(), retrieved);
			assertTrue(kedi.getPrincipals().get(0).getPrincipalId().equals(retrieved.getPrincipals().get(0).getPrincipalId()));
			retrieved = identityArchiveService.getEntityDefaultFromArchiveByPrincipalName(
                    kedi.getPrincipals().get(0).getPrincipalName());
			assertTrue(kedi.getPrincipals().get(0).getPrincipalId().equals(retrieved.getPrincipals().get(0).getPrincipalId()));
			retrieved = identityArchiveService.getEntityDefaultFromArchive(kedi.getEntityId());
			assertTrue(kedi.getPrincipals().get(0).getPrincipalId().equals(retrieved.getPrincipals().get(0).getPrincipalId()));
		}
	}

	private static class MinimalKEDIBuilder {
		private String entityId;
		private String principalId;
		private String principalName;
		private Boolean active;

		public MinimalKEDIBuilder(String name) {
			entityId = UUID.randomUUID().toString();
			principalId = principalName = name;
		}

		public EntityDefault build() {
			if (entityId == null) entityId = UUID.randomUUID().toString();
			if (principalId == null) principalId = UUID.randomUUID().toString();
			if (principalName == null) principalName = principalId;
			if (active == null) active = true;

			Principal.Builder principal = Principal.Builder.create(principalName);
			principal.setActive(active);
			principal.setEntityId(entityId);
			principal.setPrincipalId(principalId);

			EntityDefault.Builder kedi = EntityDefault.Builder.create();
			kedi.setPrincipals(Collections.singletonList(principal));
			kedi.setEntityId(entityId);

			return kedi.build();
		}

		/**
		 * @return the entityId
		 */
		public String getEntityId() {
			return this.entityId;
		}

		/**
		 * @param entityId the entityId to set
		 */
		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		/**
		 * @return the principalId
		 */
		public String getPrincipalId() {
			return this.principalId;
		}

		/**
		 * @param principalId the principalId to set
		 */
		public void setPrincipalId(String principalId) {
			this.principalId = principalId;
		}

		/**
		 * @return the principalName
		 */
		public String getPrincipalName() {
			return this.principalName;
		}

		/**
		 * @param principalName the principalName to set
		 */
		public void setPrincipalName(String principalName) {
			this.principalName = principalName;
		}

		/**
		 * @return the active
		 */
		public Boolean getActive() {
			return this.active;
		}

		/**
		 * @param active the active to set
		 */
		public void setActive(Boolean active) {
			this.active = active;
		}


	}

}
