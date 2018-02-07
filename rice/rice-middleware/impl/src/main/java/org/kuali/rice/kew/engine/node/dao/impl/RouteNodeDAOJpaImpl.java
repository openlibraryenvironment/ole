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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.KEWPropertyConstants;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.dao.RouteNodeDAO;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;

public class RouteNodeDAOJpaImpl implements RouteNodeDAO {

	@PersistenceContext(unitName="kew-unit")
	EntityManager entityManager;
	
    /**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(RouteNode node) {
    	if (node.getRouteNodeId() == null){
    		entityManager.persist(node);
    	} else {
    		OrmUtils.merge(entityManager, node);
    	}
    }

    public void save(RouteNodeInstance nodeInstance) {
    	if (nodeInstance.getRouteNodeInstanceId() == null){
    		entityManager.persist(nodeInstance);
    	} else {
    		OrmUtils.merge(entityManager, nodeInstance);
    	}
    }

    public void save(NodeState nodeState) {
    	if (nodeState.getNodeStateId() == null){
    		entityManager.persist(nodeState);
    	} else {
    		OrmUtils.merge(entityManager, nodeState);
    	}
    }

    public void save(Branch branch) {   	
    	if (branch.getBranchId() == null){
    		entityManager.persist(branch);
    	} else {
    		OrmUtils.merge(entityManager, branch);
    	}
    }

    public RouteNode findRouteNodeById(String nodeId) {
    	Query query = entityManager.createNamedQuery("RouteNode.FindByRouteNodeId");
    	query.setParameter(KEWPropertyConstants.ROUTE_NODE_ID, nodeId);
    	return (RouteNode) query.getSingleResult();
    }

    public RouteNodeInstance findRouteNodeInstanceById(String nodeInstanceId) {
    	Query query = entityManager.createNamedQuery("RouteNodeInstance.FindByRouteNodeInstanceId");
    	query.setParameter(KEWPropertyConstants.ROUTE_NODE_INSTANCE_ID, nodeInstanceId);

   	 	return (RouteNodeInstance) query.getSingleResult(); 		
    }

    @SuppressWarnings("unchecked")
    public List<RouteNodeInstance> getActiveNodeInstances(String documentId) {
    	Query query = entityManager.createNamedQuery("RouteNodeInstance.FindActiveNodeInstances");
    	query.setParameter(KEWPropertyConstants.DOCUMENT_ID, documentId);
    	return (List<RouteNodeInstance>)query.getResultList();
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

    @SuppressWarnings("unchecked")
    public List<RouteNodeInstance> getTerminalNodeInstances(String documentId) {
    	Query query = entityManager.createNamedQuery("RouteNodeInstance.FindTerminalNodeInstances");
    	query.setParameter(KEWPropertyConstants.DOCUMENT_ID, documentId);
		
		//FIXME: Can we do this better using just the JPQL query?  
		List<RouteNodeInstance> terminalNodes = new ArrayList<RouteNodeInstance>();
		List<RouteNodeInstance> routeNodeInstances = (List<RouteNodeInstance>) query.getResultList();
		for (RouteNodeInstance routeNodeInstance : routeNodeInstances) {
		    if (routeNodeInstance.getNextNodeInstances().isEmpty()) {
		    	terminalNodes.add(routeNodeInstance);
		    }
		}
		return terminalNodes;
    }

    @Override
    public List<String> getTerminalRouteNodeNames(final String documentId) {
        final DataSource dataSource = KEWServiceLocator.getDataSource();
        JdbcTemplate template = new JdbcTemplate(dataSource);
        List<String> names = template.execute(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement statement = connection.prepareStatement("SELECT rn.nm" +
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
                }, new PreparedStatementCallback<List<String>>() {
                    public List<String> doInPreparedStatement(
                            PreparedStatement statement) throws SQLException, DataAccessException {
                        List<String> routeNodeNames = new ArrayList<String>();
                        statement.setString(1, documentId);
                        statement.setBoolean(2, Boolean.FALSE);
                        statement.setBoolean(3, Boolean.TRUE);
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

    public List getInitialNodeInstances(String documentId) {
    	//FIXME: Not sure this query is returning what it needs to     	                                              
    	Query query = entityManager.createNamedQuery("RouteNodeInstance.FindInitialNodeInstances");
    	query.setParameter(KEWPropertyConstants.DOCUMENT_ID, documentId);
		return (List)query.getResultList();
    }

    public NodeState findNodeState(Long nodeInstanceId, String key) {
    	Query query = entityManager.createNamedQuery("NodeState.FindNodeState");
    	query.setParameter(KEWPropertyConstants.NODE_INSTANCE_ID, nodeInstanceId);
    	query.setParameter(KEWPropertyConstants.KEY, key);
		return (NodeState) query.getSingleResult();
    }

    public RouteNode findRouteNodeByName(String documentTypeId, String name) {
    	Query query = entityManager.createNamedQuery("RouteNode.FindRouteNodeByName");
    	query.setParameter(KEWPropertyConstants.DOCUMENT_TYPE_ID, documentTypeId);
    	query.setParameter(KEWPropertyConstants.ROUTE_NODE_NAME, name);
		return (RouteNode)query.getSingleResult();    	
    }

    public List<RouteNode> findFinalApprovalRouteNodes(String documentTypeId) {
    	Query query = entityManager.createNamedQuery("RouteNode.FindApprovalRouteNodes");
    	query.setParameter(KEWPropertyConstants.DOCUMENT_TYPE_ID, documentTypeId);
    	query.setParameter(KEWPropertyConstants.FINAL_APPROVAL, Boolean.TRUE);
    	return new ArrayList<RouteNode>(query.getResultList());
    }

    public List findProcessNodeInstances(RouteNodeInstance process) {
    	Query query = entityManager.createNamedQuery("RouteNodeInstance.FindProcessNodeInstances");
    	query.setParameter(KEWPropertyConstants.PROCESS_ID, process.getRouteNodeInstanceId());
    	return (List) query.getResultList();
    }

    public List findRouteNodeInstances(String documentId) {
    	Query query = entityManager.createNamedQuery("RouteNodeInstance.FindRouteNodeInstances");
    	query.setParameter(KEWPropertyConstants.DOCUMENT_ID, documentId);
    	return (List) query.getResultList();
    }

    public void deleteLinksToPreNodeInstances(RouteNodeInstance routeNodeInstance) {
		List<RouteNodeInstance> preNodeInstances = routeNodeInstance.getPreviousNodeInstances();
		for (Iterator<RouteNodeInstance> preNodeInstanceIter = preNodeInstances.iterator(); preNodeInstanceIter.hasNext();) {
		    RouteNodeInstance preNodeInstance = (RouteNodeInstance) preNodeInstanceIter.next();
		    List<RouteNodeInstance> nextInstances = preNodeInstance.getNextNodeInstances();
		    nextInstances.remove(routeNodeInstance);
		    entityManager.merge(preNodeInstance);
		}
    }

    public void deleteRouteNodeInstancesHereAfter(RouteNodeInstance routeNodeInstance) {
    	RouteNodeInstance rnInstance = findRouteNodeInstanceById(routeNodeInstance.getRouteNodeInstanceId());
    	entityManager.remove(rnInstance);
    }

    public void deleteNodeStateById(Long nodeStateId) {
    	Query query = entityManager.createNamedQuery("RouteNode.FindNodeStateById");
    	query.setParameter(KEWPropertyConstants.ROUTE_NODE_STATE_ID, nodeStateId);
    	NodeState nodeState = (NodeState) query.getSingleResult();
    	entityManager.remove(nodeState);
    }

    public void deleteNodeStates(List statesToBeDeleted) {
		for (Iterator stateToBeDeletedIter = statesToBeDeleted.iterator(); stateToBeDeletedIter.hasNext();) {
		    Long stateId = (Long) stateToBeDeletedIter.next();
		    deleteNodeStateById(stateId);
		}
    }

}
