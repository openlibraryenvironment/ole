package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.web.form.DocumentFormBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/24/12
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatronBillPaymentForm extends DocumentFormBase {


    /**
     * Copyright 2005-2012 The Kuali Foundation
     * <p/>
     * Licensed under the Educational Community License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     * <p/>
     * http://www.opensource.org/licenses/ecl2.php
     * <p/>
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */


    private static final long serialVersionUID = -5805825500852498048L;

    protected String dataObjectClassName;
    protected String maintenanceAction;

    private String patronId;
    private PatronBillPayment patronBillPayment;

    public PatronBillPaymentForm() {
        super();
        setViewTypeName(UifConstants.ViewType.MAINTENANCE);
    }

    @Override
    public MaintenanceDocument getDocument() {
        return (MaintenanceDocument) super.getDocument();
    }

    // This is to provide a setter with matching type to
    // public MaintenanceDocument getDocument() so that no
    // issues occur with spring 3.1-M2 bean wrappers
    public void setDocument(MaintenanceDocument document) {
        super.setDocument(document);
    }

    public String getDataObjectClassName() {
        return this.dataObjectClassName;
    }

    public void setDataObjectClassName(String dataObjectClassName) {
        this.dataObjectClassName = dataObjectClassName;
    }

    public String getMaintenanceAction() {
        return this.maintenanceAction;
    }

    public void setMaintenanceAction(String maintenanceAction) {
        this.maintenanceAction = maintenanceAction;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public PatronBillPayment getPatronBillPayment() {
        return patronBillPayment;
    }

    public void setPatronBillPayment(PatronBillPayment patronBillPayment) {
        this.patronBillPayment = patronBillPayment;
    }
}
