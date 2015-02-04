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
package org.kuali.ole.web;

import org.kuali.ole.RepositoryManager;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 8/10/11
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentStoreInitServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentStoreInitServlet.class);

    public void init(ServletConfig config) throws ServletException {
        LOG.info("DocumentStore initializing");
        try {
            RepositoryManager.getRepositoryManager().init();
        } catch (OleException e) {
            LOG.error("DocumentStore failed to initialize.");
        }
        LOG.info("DocumentStore initialized");
        super.init(config);
    }
}
