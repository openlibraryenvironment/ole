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
package org.kuali.ole.repository;

import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.logger.DocStoreLogger;
import org.kuali.ole.pojo.OleException;
import org.springframework.beans.factory.annotation.Required;

import javax.jcr.RepositoryException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/13/11
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Singleton instance of this class is created by Spring.
 */
public class DocumentStoreManager {
    DocStoreLogger docStoreLogger = new DocStoreLogger(this.getClass().getName());
    /**
     * Singleton instance of  CheckinManager initialized by Spring DI.
     */
    private CheckinManager checkinManager;
    /**
     * Singleton instance of  CheckoutManager initialized by Spring DI.
     */
    private CheckoutManager checkoutManager;
    /**
     * Singleton instance of  DeleteManager initialized by Spring DI.
     */
    private DeleteManager deleteManager;
    /**
     * Singleton instance of  RequestHandler initialized by Spring DI.
     */
    private RequestHandler requestHandler;

    @Required
    public void setCheckinManager(CheckinManager checkinManager) {
        this.checkinManager = checkinManager;
    }

    @Required
    public void setCheckoutManager(CheckoutManager checkoutManager) {
        this.checkoutManager = checkoutManager;
    }

    @Required
    public void setDeleteManager(DeleteManager deleteManager) {
        this.deleteManager = deleteManager;
    }

    @Required
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public Response processDeleteRequest(String requestString) throws Exception {
        Response response = null;
        Request request = requestHandler.toObject(requestString);
        response = deleteManager.deleteDocs(request);
        return response;

    }

    public String checkOut(String uuid, String userId, String action) {
        try {
            return checkoutManager.checkOut(uuid, userId, action);
        } catch (OleException e) {
            docStoreLogger.log(e.getMessage());
            return "Error in checking out the file. Please refer to the logs for more details!";
        } catch (RepositoryException e) {
            docStoreLogger.log(e.getMessage());
            return "Error in checking out the file. Please refer to the logs for more details!";
        } catch (FileNotFoundException e) {
            docStoreLogger.log(e.getMessage());
            return "Error in checking out the file. Please refer to the logs for more details!";
        }
    }


    public String updateRecord(RequestDocument updateContent) throws OleException {
        return checkinManager.updateContent(updateContent);
    }

    public File checkOutMultiPart(Request request) throws Exception {
        return checkoutManager.checkOutMultiPart(request);
    }

    public void addReference(String uuidFile1, String uuidFile2, String userId, String action) {
    }

    /*  public CheckinManager getCheckinManager() {
        if (null == checkinManager) {
            checkinManager = new CheckinManager();
        }
        return checkinManager;
    }*/

    public String checkOutBinary(String uuid, String userId, String action, String docFormat) throws IOException {
        try {
            return checkoutManager.checkOutBinary(uuid, userId, action, docFormat);
        } catch (OleException e) {
            docStoreLogger.log(e.getMessage());
            return "Error in checking out the file. Please refer to the logs for more details!";
        } catch (RepositoryException e) {
            docStoreLogger.log(e.getMessage());
            return "Error in checking out the file. Please refer to the logs for more details!";
        } catch (FileNotFoundException e) {
            docStoreLogger.log(e.getMessage());
            return "Error in checking out the file. Please refer to the logs for more details!";
        }
    }
}
