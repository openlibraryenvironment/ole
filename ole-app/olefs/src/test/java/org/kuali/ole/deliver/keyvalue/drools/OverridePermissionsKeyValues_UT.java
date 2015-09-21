package org.kuali.ole.deliver.keyvalue.drools;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.permission.PermissionQueryResults;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.kuali.rice.core.api.criteria.PredicateFactory.and;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;

/**
 * Created by sheiksalahudeenm on 7/6/15.
 */
public class OverridePermissionsKeyValues_UT {

    @Mock
    PermissionService mockPermissionService;
    @Mock
    PermissionQueryResults mockPermissionQueryResults;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetKeyValues() throws Exception {
        PermissionBo mockPermissionBo1 = new PermissionBo();
        mockPermissionBo1.setId("1");
        mockPermissionBo1.setName("perm1");
        mockPermissionBo1.setNamespaceCode("OLE-DLVR");
        Permission.Builder builder = Permission.Builder.create(mockPermissionBo1);
        Permission mockPermission1 = builder.build();

        PermissionBo mockPermissionBo2 = new PermissionBo();
        mockPermissionBo2.setId("2");
        mockPermissionBo2.setName("perm2");
        mockPermissionBo2.setNamespaceCode("OLE-DLVR");
        Permission.Builder builder1 = Permission.Builder.create(mockPermissionBo2);
        Permission mockPermission2 = builder1.build();

        PermissionBo mockPermissionBo3 = new PermissionBo();
        mockPermissionBo3.setId("3");
        mockPermissionBo3.setName("perm3");
        mockPermissionBo3.setNamespaceCode("OLE-DLVR");
        Permission.Builder builder2 = Permission.Builder.create(mockPermissionBo3);
        Permission mockPermission3 = builder2.build();

        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(mockPermission1);
        permissionList.add(mockPermission2);
        permissionList.add(mockPermission3);

        OverridePermissionsKeyValues overridePermissionsKeyValues = new OverridePermissionsKeyValues();

        Predicate p = and(
                equal("namespaceCode", "OLE-DLVR"));

        Mockito.when(mockPermissionQueryResults.getResults()).thenReturn(permissionList);

        Mockito.when(mockPermissionService.findPermissions(QueryByCriteria.Builder.fromPredicates(p))).thenReturn(mockPermissionQueryResults);

        overridePermissionsKeyValues.setPermissionService(mockPermissionService);

        List<KeyValue> keyValues = overridePermissionsKeyValues.getKeyValues();
        assertNotNull(keyValues);
        assertTrue(keyValues.size() > 0);
    }
}