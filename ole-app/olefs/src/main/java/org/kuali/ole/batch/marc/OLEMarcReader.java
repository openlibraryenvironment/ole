package org.kuali.ole.batch.marc;

import org.marc4j.MarcReader;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OLEMarcReader extends MarcReader {
    public boolean hasErrors();

    public List<OLEMarcErrorHandler> getErrors();

    public OLEMarcErrorHandler getError();

    public void clearErrors();

}
