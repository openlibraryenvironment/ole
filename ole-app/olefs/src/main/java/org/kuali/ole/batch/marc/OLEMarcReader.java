package org.kuali.ole.batch.marc;

import org.marc4j.MarcReader;

import java.util.List;

import org.marc4j.ErrorHandler.Error;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OLEMarcReader extends MarcReader {
    public boolean hasErrors();

    public List<Error> getErrors();

    public Error getError();

    public void clearErrors();

}
