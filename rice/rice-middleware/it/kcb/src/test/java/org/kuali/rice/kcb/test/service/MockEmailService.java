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
package org.kuali.rice.kcb.test.service;

import java.util.List;
import java.util.Map;

import org.kuali.rice.kcb.service.EmailService;

/**
 * Interface for MockEmailServiceImpl to allow unit test code to grab
 * the mailboxes through Spring proxying 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MockEmailService extends EmailService {
    public Map<String, List<Map<String, String>>> getMailBoxes();
}
