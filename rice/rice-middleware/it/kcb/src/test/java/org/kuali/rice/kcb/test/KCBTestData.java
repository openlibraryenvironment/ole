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
package org.kuali.rice.kcb.test;

import org.kuali.rice.kcb.bo.Message;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.bo.MessageDeliveryStatus;

/**
 * Contains test data objects 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KCBTestData {
    public static final Long FAKE_ID = 0xBEEFl;
    public static final Long INVALID_ID = -1l;

    public static Message getMessage1() {
        Message m= new Message();
        m.setId(Long.valueOf(1));
        m.setChannel("channel1");
        m.setContent("test content 1");
        m.setContentType("test content type 1");
        m.setDeliveryType("test delivery type 1");
        m.setRecipient("test recipient 1");
        m.setTitle("test title 1");
        
        return m;
    }
    
    public static MessageDelivery getMessageDelivery1() {
        MessageDelivery md = new MessageDelivery();
        md.setDelivererTypeName("mock");
        
        return md;
    }
}
