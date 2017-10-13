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
package org.kuali.rice.kim.impl.common;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kim.api.common.attribute.KimAttribute;
import org.kuali.rice.kim.api.common.attribute.KimAttributeContract;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeAttributeContract;
import org.kuali.rice.kim.api.type.KimTypeContract;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo;
import org.kuali.rice.kim.impl.group.GroupAttributeBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class KimAttributeDataBoTest {

    private static KimTypeContract mockKimTypeContract;
    private static KimAttributeContract mockKimAttributeContract;

    private KimTypeInfoService mockInfoService;

    private static KimType create() {
        return KimType.Builder.create(mockKimTypeContract).build();
    }

    private static KimAttribute createKimAttribute() {
        return KimAttribute.Builder.create(mockKimAttributeContract).build();
    }

    @Before
    public void setup() throws Exception {

        mockKimAttributeContract = mock(KimAttributeContract.class);
        {
            Long versionNumber = 1L;
            String attributeName = "one";
            String namespaceCode = "namespaceCode";
            String id = "id_one";
            when( mockKimAttributeContract.getVersionNumber()).thenReturn(versionNumber);
            when( mockKimAttributeContract.getAttributeName()).thenReturn(attributeName);
            when( mockKimAttributeContract.getNamespaceCode()).thenReturn(namespaceCode);
            when( mockKimAttributeContract.getId() ).thenReturn(id);
        }

        KimTypeAttributeContract one = mock(KimTypeAttributeContract.class);
        {
            String id = "one";
            Long versionNumber = 1L;
            KimAttribute attribute = createKimAttribute();
            when(one.getKimAttribute()).thenReturn(attribute);
            when(one.getId()).thenReturn(id);
            when(one.getVersionNumber()).thenReturn(versionNumber);
        }


        mockKimTypeContract = mock(KimTypeContract.class);
        {
            String id = new String("27");
            String serviceName = "barService";
            String namespaceCode ="NAMESPACE CODE";
            String name = "mock Kim Type Contract";
            Long versionNumber = 1L;
            String objectId = "1";
            List attributeDefinitions = new ArrayList<KimTypeAttributeContract>();
            attributeDefinitions.add(one);
            when(mockKimTypeContract.getId()).thenReturn(id);
            when(mockKimTypeContract.getServiceName()).thenReturn(serviceName);
            when(mockKimTypeContract.getNamespaceCode()).thenReturn(namespaceCode);
            when(mockKimTypeContract.getName()).thenReturn(name);
            when(mockKimTypeContract.getVersionNumber()).thenReturn(versionNumber);
            when(mockKimTypeContract.getObjectId()).thenReturn(objectId);
            when(mockKimTypeContract.getAttributeDefinitions()).thenReturn(attributeDefinitions);
        }

        KimType kimType = create();
        mockInfoService = mock(KimTypeInfoService.class);
        {
            when( mockInfoService.getKimType(anyString()) ).thenReturn(kimType);
        }

    }

    @Test
    public void testKimAttributeDataBo() {
        KimType kimType = create();
        Map<String,String> attributes = new HashMap<String,String>(); //{organizationCode=COMP, chartOfAccountsCode=BL}
        attributes.put("one","one");

        String kimTypeId = "27";

        assertEquals(kimType.getId(), "27");

        KimAttributeDataBo.setKimTypeInfoService(mockInfoService);

        GroupAttributeBo groupQualifier = KimAttributeDataBo.createFrom(GroupAttributeBo.class, attributes, kimTypeId).get(0);

        assertNotNull(groupQualifier.getKimType());
        assertEquals(groupQualifier.getKimType().getId(), KimTypeBo.from(kimType).getId());

    }

}