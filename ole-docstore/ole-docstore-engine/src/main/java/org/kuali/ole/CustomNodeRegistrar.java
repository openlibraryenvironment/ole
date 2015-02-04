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
package org.kuali.ole;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;
import org.kuali.ole.pojo.OleException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 5/1/11
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomNodeRegistrar {
    private String CUSTOM_NODES_FILE_NAME = "customFileNode.cnd";

    public NodeType[] registerCustomNodeTypes(Session session) throws OleException {

        NodeType[] nodeTypes = new NodeType[0];
        try {
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File temporaryFile = new File(tempDir, CUSTOM_NODES_FILE_NAME);
            InputStream templateStream = getClass().getResourceAsStream(CUSTOM_NODES_FILE_NAME);
            IOUtils.copy(templateStream, new FileOutputStream(temporaryFile));
            String absolutePath = temporaryFile.getAbsolutePath();
            Session newSession = null;
            if (session == null) {
                newSession = getSession();
                session = newSession;
            }
            nodeTypes = CndImporter.registerNodeTypes(new FileReader(new File(absolutePath)), session);
            if (newSession != null) {
                RepositoryManager.getRepositoryManager().logout(newSession);
            }
        } catch (ParseException e) {
            throw new OleException(e.getMessage());
        } catch (RepositoryException e) {
            throw new OleException(e.getMessage());
        } catch (IOException e) {
            throw new OleException(e.getMessage());
        }
        return nodeTypes;
    }

    public Session getSession() throws OleException {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("CustomNodeRegistrar", "getSession");
        return session;
    }
}
