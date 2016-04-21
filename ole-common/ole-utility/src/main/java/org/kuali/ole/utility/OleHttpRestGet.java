package org.kuali.ole.utility;

import java.io.IOException;

public interface OleHttpRestGet {
    /**
     * Do a HTTP Get at some rest URL.
     *
     * @param restPath  the path starting with "rest/" of the URL where to do the GET
     * @return      response to the Get request, null if restPath not found (HTTP 404)
     * @exception IOException  on IO error
     */
    public String rest(final String restPath) throws IOException;
}
