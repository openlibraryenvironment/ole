package org.kuali.ole.deliver.form;

import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Created by gopalp on 3/18/16.
 */
public class LoanHistoryUpdateForm  extends UifFormBase {
    private boolean running;
    private String message;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

