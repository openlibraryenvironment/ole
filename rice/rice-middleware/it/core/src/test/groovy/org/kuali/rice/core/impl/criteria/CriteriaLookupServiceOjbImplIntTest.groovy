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
package org.kuali.rice.core.impl.criteria

import org.kuali.rice.core.api.criteria.OrderByField
import org.kuali.rice.core.api.criteria.OrderDirection
import org.kuali.rice.core.api.criteria.PredicateFactory as pf

import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.api.criteria.CountFlag
import org.kuali.rice.core.api.criteria.QueryByCriteria
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import org.kuali.rice.coreservice.impl.parameter.ParameterBo
import org.kuali.rice.core.test.CORETestCase
import org.kuali.rice.test.data.PerSuiteUnitTestData
import org.kuali.rice.test.data.UnitTestData
import org.kuali.rice.test.data.UnitTestSql
import static org.junit.Assert.*
import static org.kuali.rice.core.api.criteria.PredicateFactory.*
import org.kuali.rice.core.api.criteria.CriteriaLookupService

@PerSuiteUnitTestData(value = [@UnitTestData(sqlStatements = [
  @UnitTestSql("INSERT INTO KRCR_NMSPC_T(NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_ID) VALUES('FOO-NS', '53680C68F595AD9BE0404F8189D80A6B', 1, 'FOO System', 'Y', 'FOO-KUALI')"),
  @UnitTestSql("INSERT INTO KRCR_PARM_TYP_T(PARM_TYP_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES('FOO-T', '53680C68F593AD9BE0404F8189D80A6B', 1, 'Foo Type', 'Y')"),
  @UnitTestSql("INSERT INTO KRCR_CMPNT_T(NMSPC_CD, CMPNT_CD, OBJ_ID, VER_NBR, NM, ACTV_IND) VALUES('FOO-NS', 'All-FOO', '53680C68F596AD9BE0404F8189D80A6B', 1, 'All-FOO', 'Y')"),

  @UnitTestSql("INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES('FOO-NS', 'ALL-FOO', 'TURN_FOO_ON', '5A689075D35E7AEBE0404F8189D80326', 1, 'FOO-T', 'Y', 'turn the foo on.', 'A', 'FOO-KUALI')"),
  @UnitTestSql("INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES('FOO-NS', 'ALL-FOO', 'TURN_ANOTHER_FOO_ON', '5A689075D35E7AEBE0404F8189D80325', 1, 'FOO-T', 'N', 'turn another the foo on.', 'A', 'FOO-KUALI')"),

  //using ver_num for the numerics - sort of hacky
  @UnitTestSql("INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES('FOO-NS', 'ALL-FOO', 'FOO_NUMERIC', '5A689075D35E7AEBE0404F8189D80328', 99, 'FOO-T', '99', 'low num.', 'A', 'FOO-KUALI')"),
  @UnitTestSql("INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES('FOO-NS', 'ALL-FOO', 'ANOTHER_FOO_NUMERIC', '5A689075D35E7AEBE0404F8189D80329', 100, 'FOO-T', '100', 'high num.', 'A', 'FOO-KUALI')"),
  @UnitTestSql("INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES('FOO-NS', 'ALL-FOO', 'FOO_NUMERIC2', '5C689075D35E7AEBE0404F8189D80328', -99, 'FOO-T', '-99', 'high num.', 'A', 'FOO-KUALI')"),
  @UnitTestSql("INSERT INTO KRCR_PARM_T(NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES('FOO-NS', 'ALL-FOO', 'ANOTHER_FOO_NUMERIC2', '5B689075D35E7AEBE0404F8189D80329', -100, 'FOO-T', '-100', null, 'A', 'FOO-KUALI')"),
])])
class CriteriaLookupServiceOjbImplIntTest extends CORETestCase {

    CriteriaLookupService lookup;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp()
        lookup = GlobalResourceLoader.getService("criteriaLookupService")
    }

    @Test
    void test_no_predicate() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        def results = lookup.lookup(ParameterBo.class, builder.build());
        //we at least have more than one result in the system....
        assertTrue "results size are ${results.getResults().size()}", results.getResults().size() > 1
    }

    @Test
    void test_order_by_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = equal("namespaceCode", "FOO-NS")
        builder.orderByFields = Collections.singletonList(OrderByField.Builder.create("name", OrderDirection.ASCENDING).build())
        def results = lookup.lookup(ParameterBo.class, builder.build())

        assertTrue "name of third parameter is ${results.getResults().get(2).getName()}", "FOO_NUMERIC".equals(results.getResults().get(2).getName())
    }

    @Test
    void test_order_by_lookup_desc() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = equal("namespaceCode", "FOO-NS")
        builder.orderByFields = Collections.singletonList(OrderByField.Builder.create("name", OrderDirection.DESCENDING).build())
        def results = lookup.lookup(ParameterBo.class, builder.build())

        assertTrue "name of third parameter is ${results.getResults().get(0).getName()}", "TURN_FOO_ON".equals(results.getResults().get(0).getName())
    }

    @Test
    void test_basic_equal_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = equal("name", "TURN_ANOTHER_FOO_ON")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 1, results.getResults().size()
        assertEquals "TURN_ANOTHER_FOO_ON", results.getResults()[0].name
    }

    @Test
    void test_basic_not_equal_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = and(equal("namespaceCode", "FOO-NS"), notEqual("name", "TURN_ANOTHER_FOO_ON"))

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 5, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_FOO_ON"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC2"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC2"} != null
    }

    @Test
    void test_basic_like_multi_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = like("name", "TURN*FOO_ON")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 2, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_ANOTHER_FOO_ON"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_FOO_ON"} != null
    }

    @Test
    void test_basic_not_like_multi_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = and(equal("namespaceCode", "FOO-NS"), notLike("name", "TURN*FOO_ON"))

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 4, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC2"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC2"} != null
    }

    @Test
    void test_basic_like_single_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = like("name", "TURN?FOO_ON")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 1, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_FOO_ON"} != null
    }

    @Test
    void test_basic_not_like_single_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = and(equal("namespaceCode", "FOO-NS"), notLike("name", "TURN?FOO_ON"))

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 5, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_ANOTHER_FOO_ON"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC2"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC2"} != null
    }

    @Test
    void test_basic_in_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = pf.in("name", "TURN_ANOTHER_FOO_ON", "TURN_FOO_ON")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 2, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_ANOTHER_FOO_ON"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_FOO_ON"} != null
    }

    @Test(expected=IllegalArgumentException.class)
    void test_basic_in_empty_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = pf.in("name", [] as Object[])
    }

    @Test(expected=IllegalArgumentException.class)
    void test_basic_in_null_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = pf.in("name", null)
    }

    @Test
    void test_basic_greater_than_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = greaterThan("versionNumber", 99)

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 1, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC"} != null
    }

    @Test
    void test_basic_greater_than_equal_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = greaterThanOrEqual("versionNumber", 99)

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 2, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC"} != null
    }

    @Test
    void test_basic_less_than_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = lessThan("versionNumber", -99)

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 1, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC2"} != null
    }

    @Test
    void test_basic_less_than_equal_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        //doing "and" just so the test is less fragile
        builder.predicates = lessThanOrEqual("versionNumber", -99)

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 2, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC2"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC2"} != null
    }

    @Test
    void test_basic_and_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = and(equal("value", "Y"), like("name", "TURN*FOO_ON"))

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 1, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_FOO_ON"} != null
    }

    @Test
    void test_basic_or_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = or(equal("name", "TURN_FOO_ON"), equal("name", "TURN_ANOTHER_FOO_ON"))

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 2, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_FOO_ON"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_ANOTHER_FOO_ON"} != null
    }

    @Test
    void test_basic_null_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = and(isNull("description"), equal("namespaceCode", "FOO-NS"))

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 1, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC2"} != null
    }

    @Test
    void test_basic_not_null_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates = and(isNotNull("description"), equal("namespaceCode", "FOO-NS"))

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 5, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_FOO_ON"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_ANOTHER_FOO_ON"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "ANOTHER_FOO_NUMERIC"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "FOO_NUMERIC2"} != null
    }

    @Test
    void test_nested_or_lookup() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.predicates =
            and(equal("namespaceCode", "FOO-NS"),
                or(equal("name", "TURN_FOO_ON"), equal("name", "TURN_ANOTHER_FOO_ON")))

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 2, results.getResults().size()
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_FOO_ON"} != null
        assertTrue results.toString(), results.getResults().find {it.name  == "TURN_ANOTHER_FOO_ON"} != null
    }

    @Test
    void test_count_only() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.countFlag = CountFlag.ONLY
        builder.predicates = equal("name", "TURN_ANOTHER_FOO_ON")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 0, results.getResults().size()
        assertEquals 1, results.totalRowCount
    }

    @Test
    void test_count_none() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.countFlag = CountFlag.NONE
        builder.predicates = equal("name", "TURN_ANOTHER_FOO_ON")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 1, results.getResults().size()
        assertEquals null, results.totalRowCount
    }

    @Test
    void test_count_include() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.countFlag = CountFlag.INCLUDE
        builder.predicates = equal("name", "TURN_ANOTHER_FOO_ON")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 1, results.getResults().size()
        assertEquals 1, results.totalRowCount
    }

    @Test
    void test_max_results() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.maxResults = 5
        builder.predicates = equal("namespaceCode", "FOO-NS")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 5, results.getResults().size()
        assertTrue results.moreResultsAvailable
    }

    @Test
    void test_start_index() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.startAtIndex = 1
        builder.predicates = equal("namespaceCode", "FOO-NS")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 5, results.getResults().size()
    }

    @Test
    void test_start_index_max_results() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.startAtIndex = 3
        builder.maxResults = 5
        builder.predicates = equal("namespaceCode", "FOO-NS")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 3, results.getResults().size()
        assertFalse results.moreResultsAvailable
    }

    @Test
    void test_count_max_results() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.countFlag = CountFlag.INCLUDE
        builder.maxResults = 5
        builder.predicates = equal("namespaceCode", "FOO-NS")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 5, results.getResults().size()
        assertEquals 6, results.getTotalRowCount()
        assertTrue results.isMoreResultsAvailable()
    }

    @Test
    void test_start_index_count_max_results() {
        def builder = QueryByCriteria.Builder.<ParameterBo>create()
        builder.countFlag = CountFlag.INCLUDE
        builder.startAtIndex = 3
        builder.maxResults = 2
        builder.predicates = equal("namespaceCode", "FOO-NS")

        def results = lookup.lookup(ParameterBo.class, builder.build());
        assertEquals 2, results.getResults().size()
        assertEquals 6, results.getTotalRowCount()
        assertTrue results.isMoreResultsAvailable()
    }
}