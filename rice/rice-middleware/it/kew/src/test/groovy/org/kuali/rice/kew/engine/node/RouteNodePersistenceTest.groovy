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
package org.kuali.rice.kew.engine.node

import org.kuali.rice.test.persistence.PersistenceTestHelper
import org.junit.Test
import org.kuali.rice.kew.api.doctype.RouteNodeContract
import org.kuali.rice.kew.api.doctype.RouteNodeConfigurationParameterContract
import org.junit.Before
import org.kuali.rice.kew.test.KEWTestCase
import org.kuali.rice.kew.service.KEWServiceLocator
import org.kuali.rice.kew.engine.node.dao.RouteNodeDAO
import org.kuali.rice.kew.api.KewApiConstants

/**
 * Tests persisting RouteNode
 */
class RouteNodePersistenceTest extends KEWTestCase {
    @Delegate PersistenceTestHelper helper

    RouteNodeDAO dao

    @Before
    void init() {
        helper = new PersistenceTestHelper("kewDataSource")
        dao = KEWServiceLocator.getService("enRouteNodeDAO")
    }

    @Test
    void test_save_routenode() {
        RouteNode node =  new RouteNode([
            routeNodeId: "id",
            documentTypeId: "1", // OJB mapping is VARCHAR?
            routeNodeName: "name",
            routeMethodName: "routeMethodName",
            routeMethodCode: KewApiConstants.ROUTE_LEVEL_ROUTE_MODULE,
            finalApprovalInd: false,
            mandatoryRouteInd: false,
            exceptionWorkgroupId: "exceptionWorkgroupId",
            activationType: ActivationTypeEnum.PARALLEL.getCode(),
            nextDocStatus: "nextDocStatus"
        ])

        dao.save(node)

        assertRow([
            RTE_NODE_ID: node.routeNodeId,
            CONTENT_FRAGMENT: null,
            RTE_MTHD_CD: 'RM',
            VER_NBR: new BigDecimal(1),
            GRP_ID: 'exceptionWorkgroupId',
            ACTVN_TYP: 'P',
            RTE_MTHD_NM: 'routeMethodName',
            FNL_APRVR_IND: new BigDecimal(0),
            TYP: 'org.kuali.rice.kew.engine.node.RequestsNode',
            BRCH_PROTO_ID: null,
            NM: 'name',
            MNDTRY_RTE_IND: new BigDecimal(0),
            DOC_TYP_ID: "1",
            NEXT_DOC_STAT: 'nextDocStatus'
        ],
        "KREW_RTE_NODE_T", "RTE_NODE_ID")

        /*
        <column name="RTE_NODE_ID" primaryKey="true" size="40" type="VARCHAR"/>
        <column name="DOC_TYP_ID" size="19" type="DECIMAL"/>
        <column name="NM" required="true" size="255" type="VARCHAR"/>
        <column name="TYP" required="true" size="255" type="VARCHAR"/>
        <column name="RTE_MTHD_NM" size="255" type="VARCHAR"/>
        <column name="RTE_MTHD_CD" size="2" type="VARCHAR"/>
        <column name="FNL_APRVR_IND" size="1" type="DECIMAL"/>
        <column name="MNDTRY_RTE_IND" size="1" type="DECIMAL"/>
        <column name="ACTVN_TYP" size="1" type="VARCHAR"/>
        <column name="BRCH_PROTO_ID" size="40" type="VARCHAR"/>
        <column default="0" name="VER_NBR" size="8" type="DECIMAL"/>
        <column name="CONTENT_FRAGMENT" size="4000" type="VARCHAR"/>
        <column name="GRP_ID" size="40" type="VARCHAR"/>
        <column name="NEXT_DOC_STAT" size="64" type="VARCHAR"/>
         */
    }
}
