package org.kuali.ole.deliver.keyvalue.drools;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.permission.PermissionQueryResults;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.kuali.rice.core.api.criteria.PredicateFactory.and;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;

/**
 * Created by sheiksalahudeenm on 7/6/15.
 */
public class OverridePermissionsKeyValues extends KeyValuesBase {

    private PermissionService permissionService;

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<>();
        keyValues.add(new ConcreteKeyValue("", ""));
        List<ConcreteKeyValue> concreteKeyValues = new ArrayList<ConcreteKeyValue>();
        if (CollectionUtils.isNotEmpty(getPermissions())) {
            for (Iterator<Permission> iterator = getPermissions().iterator(); iterator.hasNext(); ) {
                Permission permission = iterator.next();
                concreteKeyValues.add(new ConcreteKeyValue(permission.getName(), permission.getName()));
            }

        }
        Collections.sort(concreteKeyValues);
        keyValues.addAll(concreteKeyValues);
        return keyValues;
    }

    private PermissionService getPermissionService() {
        if (null == permissionService) {
            permissionService = KimApiServiceLocator.getPermissionService();
        }
        return permissionService;
    }

    public List<Permission> getPermissions() {
        Predicate p = and(
                equal("namespaceCode", "OLE-DLVR"));
        PermissionQueryResults permissionQueryResults = getPermissionService().findPermissions(QueryByCriteria.Builder.fromPredicates(p));
        List<Permission> permissionList = permissionQueryResults.getResults();
        return permissionList;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }


}
