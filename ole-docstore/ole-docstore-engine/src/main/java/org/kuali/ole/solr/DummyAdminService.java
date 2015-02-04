/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.solr;

import org.kuali.ole.docstore.discovery.service.AdminService;

/**
 * Class to DummyAdminService.
 *
 * @author Rajesh Chowdary K
 * @created Apr 24, 2012
 */
public class DummyAdminService
        implements AdminService {

    /**
     *
     */
    public DummyAdminService() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kuali.ole.docstore.discovery.service.AdminService#optimize(java.lang.String)
     */
    @Override
    public void optimize(String indexName) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.kuali.ole.docstore.discovery.service.AdminService#optimize()
     */
    @Override
    public void optimize() throws Exception {

    }

    /**
     * @throws Exception
     */
    @Override
    public void optimize(Boolean waitFlush, Boolean waitSearcher) throws Exception {

    }

}
