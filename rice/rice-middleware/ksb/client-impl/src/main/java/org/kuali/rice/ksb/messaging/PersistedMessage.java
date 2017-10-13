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
package org.kuali.rice.ksb.messaging;

import org.kuali.rice.ksb.api.messaging.AsynchronousCall;

import java.io.Serializable;
import java.sql.Timestamp;

public interface PersistedMessage extends Serializable {
    String getApplicationId();

    String getIpNumber();

    Timestamp getQueueDate();

    Integer getQueuePriority();

    String getQueueStatus();

    Integer getRetryCount();

    Long getRouteQueueId();

    String getServiceName();

    AsynchronousCall getMethodCall();

    String getMethodName();

    Timestamp getExpirationDate();

    PersistedMessagePayload getPayload();

    String getValue1();

    String getValue2();
}
