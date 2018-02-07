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
package org.kuali.rice.kew.engine.node.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.dao.RouteNodeDAO;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;


public class RouteNodeDAOOjbImpl extends PersistenceBrokerDaoSupport implements RouteNodeDAO {

    private static final String ROUTE_NODE_ID = "routeNodeId";
    private static final String ROUTE_NODE_INSTANCE_ID = "routeNodeInstanceId";
    private static final String NODE_INSTANCE_ID = "nodeInstanceId";
    private static final String DOCUMENT_ID = "documentId";
    private static final String ROUTE_NODE_NAME = "routeNodeName";
    private static final String DOCUMENT_TYPE_ID = "documentTypeId";
    private static final String PROCESS_ID = "processId";
    private static final String ACTIVE = "active";
    private static final String COMPLETE = "complete";
    private static final String FINAL_APPROVAL = "finalApprovalInd";
    private static final String KEY = "key";
    private static final String Route_Node_State_ID = "nodeStateId";

    public void save(RouteNode node) {
	getPersistenceBrokerTemplate().store(node);
    }

    public void save(RouteNodeInstance nodeInstance) {
    	// this is because the branch table relates to the node instance table - both through their keys - and
    	// ojb can't automatically do this bi-directional relationship
    	getPersistenceBrokerTemplate().store(nodeInstance.getBranch());
    	getPersistenceBrokerTemplate().store(nodeInstance);
    }

    public void save(NodeState nodeState) {
	getPersistenceBrokerTemplate().store(nodeState);
    }

    public void save(Branch branch) {
	getPersistenceBrokerTemplate().store(branch);
    }

    public RouteNode findRouteNodeById(String nodeId) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(ROUTE_NODE_ID, nodeId);
	return (RouteNode) getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(RouteNode.class, criteria));
    }

    public RouteNodeInstance findRouteNodeInstanceById(String nodeInstanceId) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(ROUTE_NODE_INSTANCE_ID, nodeInstanceId);
	return (RouteNodeInstance) getPersistenceBrokerTemplate().getObjectByQuery(
		new QueryByCriteria(RouteNodeInstance.class, criteria));
    }

    @SuppressWarnings(value = "unchecked")
    public List<RouteNodeInstance> getActiveNodeInstances(String documentId) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(DOCUMENT_ID, documentId);
	criteria.addEqualTo(ACTIVE, Boolean.TRUE);
	return (List<RouteNodeInstance>) getPersistenceBrokerTemplate().getCollectionByQuery(
		new QueryByCriteria(RouteNodeInstance.class, criteria));
    }

    private static final String CURRENT_ROUTE_NODE_NAMES_SQL = "SELECT rn.nm" +
            " FROM krew_rte_node_t rn," +
            "      krew_rte_node_instn_t rni" +
            " LEFT JOIN krew_rte_node_instn_lnk_t rnl" +
            "   ON rnl.from_rte_node_instn_id = rni.rte_node_instn_id" +
            " WHERE rn.rte_node_id = rni.rte_node_id AND" +
            "       rni.doc_hdr_id = ? AND" +
            "       rnl.from_rte_node_instn_id IS NULL";

    @Override
    public List<String> getCurrentRouteNodeNames(final String documentId) {
        final DataSource dataSource = KEWServiceLocator.getDataSource();
        JdbcTemplate template = new JdbcTemplate(dataSource);
        List<String> names = template.execute(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        return connection.prepareStatement(CURRENT_ROUTE_NODE_NAMES_SQL);
                    }
                }, new PreparedStatementCallback<List<String>>() {
                    public List<String> doInPreparedStatement(
                            PreparedStatement statement) throws SQLException, DataAccessException {
                        List<String> routeNodeNames = new ArrayList<String>();
                        statement.setString(1, documentId);
                        ResultSet rs = statement.executeQuery();
                        try {
                            while (rs.next()) {
                                String name = rs.getString("nm");
                                routeNodeNames.add(name);
                            }
                        } finally {
                            if (rs != null) {
                                rs.close();
                            }
                        }
                        return routeNodeNames;
                    }
                }
        );
        return names;
    }

    @Override
	public List<String> getActiveRouteNodeNames(final String documentId) {
    	final DataSource dataSource = KEWServiceLocator.getDataSource();
    	JdbcTemplate template = new JdbcTemplate(dataSource);
    	List<String> names = template.execute(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement statement = connection.prepareStatement(
								"SELECT rn.nm FROM krew_rte_node_t rn, krew_rte_node_instn_t rni WHERE rn.rte_node_id = rni.rte_node_id AND rni.doc_hdr_id = ? AND rni.actv_ind = ?");
						return statement;
					}
				},
				new PreparedStatementCallback<List<String>>() {
					public List<String> doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {
						List<String> routeNodeNames = new ArrayList<String>();
						statement.setString(1, documentId);
						statement.setBoolean(2, Boolean.TRUE);
						ResultSet rs = statement.executeQuery();
						try {
							while(rs.next()) {
								String name = rs.getString("nm");
								routeNodeNames.add(name);
							}
						} finally {
							if(rs != null) {
								rs.close();
							}
						}
						return routeNodeNames;
					}
				});
    	return names;
	}

    @Override
	public List<String> getTerminalRouteNodeNames(final String documentId) {
		final DataSource dataSource = KEWServiceLocator.getDataSource();
    	JdbcTemplate template = new JdbcTemplate(dataSource);
    	List<String> names = template.execute(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement statement = connection.prepareStatement(
								"SELECT rn.nm" +
								"  FROM krew_rte_node_t rn," +
								"       krew_rte_node_instn_t rni" +
								"  LEFT JOIN krew_rte_node_instn_lnk_t rnl" +
								"    ON rnl.from_rte_node_instn_id = rni.rte_node_instn_id" +
								"  WHERE rn.rte_node_id = rni.rte_node_id AND" +
								"        rni.doc_hdr_id = ? AND" +
								"        rni.actv_ind = ? AND" +
								"        rni.cmplt_ind = ? AND" +
								"        rnl.from_rte_node_instn_id IS NULL");
						return statement;
					}
				},
				new PreparedStatementCallback<List<String>>() {
					public List<String> doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {
						List<String> routeNodeNames = new ArrayList<String>();
						statement.setString(1, documentId);
						statement.setBoolean(2, Boolean.FALSE);
						statement.setBoolean(3, Boolean.TRUE);
						ResultSet rs = statement.executeQuery();
						try {
							while(rs.next()) {
								String name = rs.getString("nm");
								routeNodeNames.add(name);
							}
						} finally {
							if(rs != null) {
								rs.close();
							}
						}
						return routeNodeNames;
					}
				});
    	return names;
	}

    @SuppressWarnings("unchecked")
    public List<RouteNodeInstance> getTerminalNodeInstances(String documentId) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(DOCUMENT_ID, documentId);
	criteria.addEqualTo(ACTIVE, Boolean.FALSE);
	criteria.addEqualTo(COMPLETE, Boolean.TRUE);
//	criteria.addIsNull("nextNodeInstances.routeNodeInstanceId");
//	QueryByCriteria query = new QueryByCriteria(RouteNodeInstance.class, criteria);
//	// we need to outer join here because we are looking for nodes with no nextNodeInstances
//	query.setPathOuterJoin("nextNodeInstances");
//	return (List) getPersistenceBrokerTemplate().getCollectionByQuery(query);
	
	//forced to do this programmatically, for some reason the above code stopped working 
	List<RouteNodeInstance> terminalNodes = new ArrayList<RouteNodeInstance>();
	List<RouteNodeInstance> routeNodeInstances = (List<RouteNodeInstance>) getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RouteNodeInstance.class, criteria));
	for (RouteNodeInstance routeNodeInstance : routeNodeInstances) {
	    if (routeNodeInstance.getNextNodeInstances().isEmpty()) {
		terminalNodes.add(routeNodeInstance);
	    }
	}
	return terminalNodes;
    }

    public List getInitialNodeInstances(String documentId) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("initialDocumentRouteHeaderValues." + DOCUMENT_ID, documentId);
        return (List) getPersistenceBrokerTemplate().getCollectionByQuery(
                new QueryByCriteria(RouteNodeInstance.class, criteria));
    }

    public NodeState findNodeState(Long nodeInstanceId, String key) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(NODE_INSTANCE_ID, nodeInstanceId);
	criteria.addEqualTo(KEY, key);
	return (NodeState) getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(NodeState.class, criteria));
    }

    public RouteNode findRouteNodeByName(String documentTypeId, String name) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(ROUTE_NODE_NAME, name);
	criteria.addEqualTo(DOCUMENT_TYPE_ID, documentTypeId);
	return (RouteNode) getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(RouteNode.class, criteria));
    }

    public List<RouteNode> findFinalApprovalRouteNodes(String documentTypeId) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(DOCUMENT_TYPE_ID, documentTypeId);
	criteria.addEqualTo(FINAL_APPROVAL, Boolean.TRUE);
	return new ArrayList<RouteNode>(getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RouteNode.class, criteria)));
    }

    public List findProcessNodeInstances(RouteNodeInstance process) {
	Criteria crit = new Criteria();
	crit.addEqualTo(PROCESS_ID, process.getRouteNodeInstanceId());
	return (List) getPersistenceBrokerTemplate()
		.getCollectionByQuery(new QueryByCriteria(RouteNodeInstance.class, crit));
    }

    public List findRouteNodeInstances(String documentId) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(DOCUMENT_ID, documentId);
	return (List) getPersistenceBrokerTemplate().getCollectionByQuery(
		new QueryByCriteria(RouteNodeInstance.class, criteria));
    }

    public void deleteLinksToPreNodeInstances(RouteNodeInstance routeNodeInstance) {
	List<RouteNodeInstance> preNodeInstances = routeNodeInstance.getPreviousNodeInstances();
	for (Iterator<RouteNodeInstance> preNodeInstanceIter = preNodeInstances.iterator(); preNodeInstanceIter.hasNext();) {
	    RouteNodeInstance preNodeInstance = (RouteNodeInstance) preNodeInstanceIter.next();
	    List<RouteNodeInstance> nextInstances = preNodeInstance.getNextNodeInstances();
	    nextInstances.remove(routeNodeInstance);
	    save(preNodeInstance);
	}
    }

    public void deleteRouteNodeInstancesHereAfter(RouteNodeInstance routeNodeInstance) {
	this.getPersistenceBrokerTemplate().delete(routeNodeInstance);
    }

    public void deleteNodeStateById(Long nodeStateId) {
	Criteria criteria = new Criteria();
	criteria.addEqualTo(Route_Node_State_ID, nodeStateId);
	NodeState nodeState = (NodeState) getPersistenceBrokerTemplate().getObjectByQuery(
		new QueryByCriteria(NodeState.class, criteria));
	getPersistenceBrokerTemplate().delete(nodeState);
    }

    public void deleteNodeStates(List statesToBeDeleted) {
	for (Iterator stateToBeDeletedIter = statesToBeDeleted.iterator(); stateToBeDeletedIter.hasNext();) {
	    Long stateId = (Long) stateToBeDeletedIter.next();
	    deleteNodeStateById(stateId);
	}
    }

}
