package org.kuali.ole.bo.diagnostics;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/16/12
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUDiagnostic {

    private String uri;
    private String details;
    private String message;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "OleSRUDiagnostic{" +
                "uri='" + uri + '\'' +
                ", details='" + details + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
