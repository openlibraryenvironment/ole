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
package org.kuali.rice.ken.services.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.ken.service.impl.NotificationRecipientServiceKimImpl;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.ken.test.TestConstants;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.KimConstants.KimGroupMemberTypes;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import static org.junit.Assert.*;

/**
 * This is a description of what this class does - chb don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class NotificationRecipientServiceKimImplTest extends KENTestCase
{
    NotificationRecipientServiceKimImpl nrski = new NotificationRecipientServiceKimImpl();
    /**
     * Test method for {@link org.kuali.rice.ken.service.impl.NotificationRecipientServiceKimImpl#getGroupMembers(java.lang.String)}.
     */
    @Test
    public void testGetGroupMembersValid()
    {
        Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                Utilities.parseGroupNamespaceCode(TestConstants.VALID_KIM_GROUP_NAME_1), Utilities.parseGroupName(
                TestConstants.VALID_KIM_GROUP_NAME_1));
        assertTrue(nrski.getGroupMembers(group.getId()).length == TestConstants.KIM_GROUP_1_MEMBERS);
    }

    /**
     * Test method for {@link org.kuali.rice.ken.service.impl.NotificationRecipientServiceKimImpl#getUserDisplayName(java.lang.String)}.
     */
    @Test
    @Ignore
    public final void testGetUserDisplayName()
    {
        //hoping gary will take care of this when he does KEW user conversion
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.kuali.rice.ken.service.impl.NotificationRecipientServiceKimImpl#isGroupRecipientValid(java.lang.String)}.
     */
    @Test
    public final void testIsGroupRecipientValid()
    {
        Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                Utilities.parseGroupNamespaceCode(TestConstants.VALID_KIM_GROUP_NAME_1), Utilities.parseGroupName(
                TestConstants.VALID_KIM_GROUP_NAME_1));
        assertTrue(nrski.isGroupRecipientValid(group.getId()));
    }

    /**
     * Test method for {@link org.kuali.rice.ken.service.impl.NotificationRecipientServiceKimImpl#isRecipientValid(java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testIsRecipientValid()
    {
        assertTrue( nrski.isRecipientValid( TestConstants.VALID_KIM_PRINCIPAL_NAME, KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.getCode()));
        assertFalse( nrski.isRecipientValid( "BoogalooShrimp44", KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.getCode()));

        Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                Utilities.parseGroupNamespaceCode(TestConstants.VALID_KIM_GROUP_NAME_1), Utilities.parseGroupName(
                TestConstants.VALID_KIM_GROUP_NAME_1));
        assertTrue( nrski.isRecipientValid( group.getId(), KimGroupMemberTypes.GROUP_MEMBER_TYPE.getCode()));
        assertFalse( nrski.isRecipientValid( "FooSchnickens99", KimGroupMemberTypes.GROUP_MEMBER_TYPE.getCode()));
    }

    /**
     * Test method for {@link org.kuali.rice.ken.service.impl.NotificationRecipientServiceKimImpl#isUserRecipientValid(java.lang.String)}.
     */
    @Test
    public final void testIsUserRecipientValid()
    {
        assertTrue( nrski.isUserRecipientValid( TestConstants.VALID_KIM_PRINCIPAL_NAME ));
    }

}
