/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.service;

import java.util.List;
import java.util.Map;

/**
 * This interface extends LookupDao for integrating Docstore search
 */
public interface OleDocStoreSearchService {
    public List getResult(Class cl, String attr, List<Object> val, Map vas);

    public List getResult(Class cl, Map<String, List<Object>> val);

    public String getBibUUID(String itemTitleId);
}
