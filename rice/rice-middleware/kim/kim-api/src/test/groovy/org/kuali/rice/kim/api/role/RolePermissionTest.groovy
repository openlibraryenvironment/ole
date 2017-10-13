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
package org.kuali.rice.kim.api.role

import javax.xml.bind.JAXBContext
import org.junit.Assert
import org.junit.Test;

class RolePermissionTest {

	private static final String ID = "50"
	private static final String ROLE_ID = "12013"
	private static final String PERMISSION_ID = "93121771551017"
	private static final String OBJECT_ID = UUID.randomUUID()
	private static final Long VERSION_NUMBER = new Long(1) 
	private static final boolean ACTIVE = "true"
	
	private static final String XML = """
		<rolePermission xmlns="http://rice.kuali.org/kim/v2_0">
			<id>${ID}</id>
			<roleId>${ROLE_ID}</roleId>
			<permissionId>${PERMISSION_ID}</permissionId>
			<active>${ACTIVE}</active>
			<versionNumber>${VERSION_NUMBER}</versionNumber>
        	<objectId>${OBJECT_ID}</objectId>
		</rolePermission>
		"""
	
    @Test
    void happy_path() {
        RolePermission.Builder.create(ID, ROLE_ID, PERMISSION_ID)
    }

	@Test
	void test_copy() {
		def o1b = RolePermission.Builder.create(ID, ROLE_ID, PERMISSION_ID)		
		def o1 = o1b.build()
		def o2 = RolePermission.Builder.create(o1).build()

		Assert.assertEquals(o1, o2)
	}
	
	@Test
	public void test_Xml_Marshal_Unmarshal() {
	  def jc = JAXBContext.newInstance(RolePermission.class)
	  def marshaller = jc.createMarshaller()
	  def sw = new StringWriter()

	  def param = this.create()
	  marshaller.marshal(param,sw)

	  def unmarshaller = jc.createUnmarshaller()
	  def actual = unmarshaller.unmarshal(new StringReader(sw.toString()))
	  def expected = unmarshaller.unmarshal(new StringReader(XML))

	  Assert.assertEquals(expected,actual)
	}
	
	private create() {
		return RolePermission.Builder.create(new RolePermissionContract() {
			String id = RolePermissionTest.ID
			String roleId = RolePermissionTest.ROLE_ID
			String permissionId = RolePermissionTest.PERMISSION_ID
			boolean active = RolePermissionTest.ACTIVE
			Long versionNumber = RolePermissionTest.VERSION_NUMBER
			String objectId = RolePermissionTest.OBJECT_ID
		}).build()
	}
}
