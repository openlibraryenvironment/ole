package org.kuali.ole.select.checkList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: JuliyaMonica.S
 * Date: 9/5/12
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class OleLicenseRequest_IT extends KFSTestCaseBase {

    @Test
    @Transactional
    public void testOwners() {
        List<String> memberIds = new ArrayList<String>();
        Map<String,String> roleSearchCriteria = new HashMap<String, String>();
        roleSearchCriteria.put("namespaceCode","OLE");
        List<RoleBo> roles = (List<RoleBo>)getLookupService().findCollectionBySearchHelper(
                RoleBo.class, roleSearchCriteria, true);
            for(RoleBo roleBo : roles){
                List<RoleMemberBo> roleMembers = roleBo.getMembers();
                if(roleMembers != null && !roleMembers.isEmpty()){
                    for(RoleMemberBo memberImpl : roleMembers) {
                        memberIds.add(memberImpl.getMemberId());
                    }
                }
            }
        assertNotNull(memberIds);
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
}
