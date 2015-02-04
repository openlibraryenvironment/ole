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
package org.kuali.ole.select.batch.service;

import org.kuali.ole.select.businessobject.BibInfoBean;

import java.util.List;

/**
 * This service interface defines the methods that a RequisitionLoadTransactionsService implementation must provide.
 * <p/>
 * Provides methods to load batch files for the requisition(PreOrderRequest) batch job.
 */
public interface RequisitionLoadTransactionsService {
    /**
     * Validates and parses the file identified by the given files name. If successful, parsed entries are stored.
     *
     * @param fileName Name of file to be uploaded and processed.
     * @return True if the file load and store was successful, false otherwise.
     */
    public boolean loadRequisitionFile(String fileName);

    /**
     * Set the values for the Requisition Document and save.
     *
     * @param bibInfoBeanList ArrayList
     */
    public List saveRequisitionDocument(List<BibInfoBean> bibInfoBeanList) throws Exception;

}
