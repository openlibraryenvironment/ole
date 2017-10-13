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

import java.io.Serializable;



/**
 * Base event interface for events emitted from the workflow engine
 * and hooked by the {@link PostProcessor}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface IDocumentEvent extends Serializable {
  public static final String ROUTE_LEVEL_CHANGE = "rt_lvl_change";
  public static final String ROUTE_STATUS_CHANGE = "rt_status_change";
  public static final String DELETE_CHANGE = "delete_document";
  public static final String ACTION_TAKEN = "action_taken";
  public static final String BEFORE_PROCESS = "before_process";
  public static final String AFTER_PROCESS = "after_process";
  public static final String LOCK_DOCUMENTS = "lock_documents";

  /**
   * @return the code of this document event 
   */
  public String getDocumentEventCode();

  /**
   * @return the document id for which this event was generated
   */
  public String getDocumentId();

  /**
   * @return the application document id registered for this document when it was created
   */
  public String getAppDocId();
}
