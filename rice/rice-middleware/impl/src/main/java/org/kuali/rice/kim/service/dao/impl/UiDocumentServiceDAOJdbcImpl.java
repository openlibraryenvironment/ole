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
package org.kuali.rice.kim.service.dao.impl;

import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.kuali.rice.kim.service.dao.UiDocumentServiceDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring JdbcTemplate implementation of UiDocumentServiceDAO
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class UiDocumentServiceDAOJdbcImpl implements UiDocumentServiceDAO {

    public static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UiDocumentServiceDAOJdbcImpl.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
    }

    @Override
    public Map<String, Group> findGroupsForRole(final String roleId) {
        final Map<String, Group> roleGroupMembers = new HashMap<String, Group>();

        JdbcTemplate template = new JdbcTemplate(dataSource);
        Map<String, Group> results  = template.execute(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                String sql = " SELECT GRP_ID, GRP_NM, NMSPC_CD, KIM_TYP_ID, ACTV_IND" +
                    " FROM KRIM_GRP_T G, KRIM_ROLE_MBR_T RM WHERE" +
                    " G.GRP_ID = RM.MBR_ID AND GRP_ID IN" +
                    " (SELECT MBR_ID FROM KRIM_ROLE_MBR_T WHERE MBR_TYP_CD = '" +
                    MemberType.GROUP.getCode() + "' AND ROLE_ID = '" + roleId + "')";

                    LOG.debug("Query to find Entity Names for role " + roleId + ":" + sql);

                PreparedStatement statement = connection.prepareStatement(sql);
                return statement;
            }
        }, new PreparedStatementCallback<Map<String,Group>>() {
            public Map<String,Group> doInPreparedStatement(
                PreparedStatement statement) throws SQLException, DataAccessException {
                    ResultSet rs = statement.executeQuery();
                    try {
                        while (rs.next()) {

                            String groupId = rs.getString(1);
                            String groupName = rs.getString(2);
                            String groupNameSpace = rs.getString(3);
                            String kimTypeId = rs.getString(4);
                            String activeInd = rs.getString(5);
                            Group.Builder builder = Group.Builder.create(groupNameSpace, groupName, kimTypeId);
                            builder.setId(groupId);
                            builder.build();
                            if (activeInd.equalsIgnoreCase(KewApiConstants.ACTIVE_CD)) {
                                builder.setActive(true);
                            }
                            roleGroupMembers.put(groupId, builder.build());
                        }
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                    }
                    return roleGroupMembers;
                }
            }
        );
        return roleGroupMembers;
    }

    @Override
    public Map<String, EntityName> findEntityNamesForRole(final String roleId) {
        final Map<String, EntityName> entityNamesForPrincipals = new HashMap<String, EntityName>();

        JdbcTemplate template = new JdbcTemplate(dataSource);
        Map<String, EntityName> results  = template.execute(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                String sql = "SELECT EN.ENTITY_NM_ID, EN.ENTITY_ID, EN.FIRST_NM, EN.LAST_NM, EP.SUPPRESS_NM_IND FROM" +
                    " KRIM_PRNCPL_T KP, KRIM_ENTITY_NM_T EN LEFT JOIN KRIM_ENTITY_PRIV_PREF_T EP" +
                    " ON EN.ENTITY_ID = EP.ENTITY_ID" +
                    " WHERE EN.ACTV_IND='" + KewApiConstants.ACTIVE_CD +
                    "' AND EN.DFLT_IND='"+ KewApiConstants.ACTIVE_CD +
                    "' AND EN.NM_TYP_CD='"+ KimConstants.NameTypes.PREFERRED +
                    "' AND EN.ENTITY_ID = KP.ENTITY_ID " +
                    " AND KP.PRNCPL_ID IN (SELECT MBR_ID FROM KRIM_ROLE_MBR_T WHERE ROLE_ID = '" + roleId + "')";
                    LOG.debug("Query to find Entity Names for role " + roleId + ":" + sql);

                PreparedStatement statement = connection.prepareStatement(sql);
                return statement;
            }
        }, new PreparedStatementCallback<Map<String,EntityName>>() {
            public Map<String,EntityName> doInPreparedStatement(
                PreparedStatement statement) throws SQLException, DataAccessException {
                    ResultSet rs = statement.executeQuery();
                    try {
                        while (rs.next()) {
                            String id = rs.getString(1);
                            String entityId = rs.getString(2);
                            String firstName = rs.getString(3);
                            String lastName = rs.getString(4);
                            String suppressName = rs.getString(5);
                            boolean suppressNameBoolean = false;
                            if (KRADConstants.YES_INDICATOR_VALUE.equalsIgnoreCase(suppressName))  {
                                suppressNameBoolean = true;
                            }
                            EntityName.Builder builder = EntityName.Builder.create(id, entityId, firstName, lastName, suppressNameBoolean);
                            builder.setActive(true);
                            builder.setDefaultValue(true);
                            EntityName entityName = builder.build();
                            entityNamesForPrincipals.put(entityName.getEntityId(), entityName);
                        }
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                    }
                        return entityNamesForPrincipals;
                    }
                }
        );
        return entityNamesForPrincipals;
    }
}