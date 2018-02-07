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
package org.kuali.rice.kew.framework.postprocessor;

import org.kuali.rice.kew.framework.postprocessor.IDocumentEvent;

/**
 * <p><Title> </p>
 * <p><Description> </p>
 * <p><p><p>Copyright: Copyright (c) 2002</p>
 * <p><p>Company: UIS - Indiana University</p>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentRouteLevelChange implements IDocumentEvent {
	
	// TODO for now we will include the new node-based routing fields onto this object to avoid an interface
	// change to the PostProcessor interface.
	
  private static final long serialVersionUID = 785552701611174468L;

  private String documentId;
  private String appDocId;
  private Integer oldRouteLevel;
  private Integer newRouteLevel;
  private String oldNodeName;
  private String newNodeName;
  private String oldNodeInstanceId;
  private String newNodeInstanceId;
  
  //  this constructor is for backwards compatibility
  public DocumentRouteLevelChange(String documentId, String appDocId, Integer oldRouteLevel, Integer newRouteLevel) {
	  this(documentId, appDocId, oldRouteLevel, newRouteLevel, null, null, null, null);
  }
  
  public DocumentRouteLevelChange(String documentId, String appDocId, Integer oldRouteLevel,
    Integer newRouteLevel, String oldNodeName, String newNodeName, String oldNodeInstanceId, String newNodeInstanceId) {
    this.documentId = documentId;
    this.oldRouteLevel = oldRouteLevel;
    this.newRouteLevel = newRouteLevel;
    this.oldNodeName = oldNodeName;
    this.newNodeName = newNodeName;
    this.oldNodeInstanceId = oldNodeInstanceId;
    this.newNodeInstanceId = newNodeInstanceId;
    this.appDocId = appDocId;
  }

  public String getDocumentEventCode() {
    return ROUTE_LEVEL_CHANGE;
  }

  public String getDocumentId() {
    return documentId;
  }

  public Integer getOldRouteLevel() {
    return oldRouteLevel;
  }

  public Integer getNewRouteLevel() {
    return newRouteLevel;
  }

  public String getNewNodeInstanceId() {
	return newNodeInstanceId;
  }

  public String getNewNodeName() {
	return newNodeName;
  }

  public String getOldNodeInstanceId() {
	return oldNodeInstanceId;
  }

  public String getOldNodeName() {
	return oldNodeName;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("DocumentId ").append(documentId);
    buffer.append(" changing from routeLevel ").append(oldRouteLevel);
    buffer.append(" to routeLevel ").append(newRouteLevel);

    return buffer.toString();
  }

  /**
   * @return
   */
  public String getAppDocId() {
    return appDocId;
  }
}
