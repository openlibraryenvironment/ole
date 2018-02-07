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
package org.kuali.rice.kcb.api.message;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;


/**
 * Message value object for published API  
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private String deliveryType;
    private String title;
    private String channel;
    private String producer;
    private String content;
    private String contentType;
    private String recipient;
    private String url;
    private String originId;

    public String getDeliveryType() {
        return this.deliveryType;
    }
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContentType() {
        return this.contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getRecipient() {
        return this.recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public String getChannel() {
        return this.channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getProducer() {
        return this.producer;
    }
    public void setProducer(String producer) {
        this.producer = producer;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getOriginId() {
        return this.originId;
    }
    public void setOriginId(String originId) {
        this.originId = originId;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
