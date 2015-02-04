package org.kuali.ole.service;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/21/13
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OverlayOutputService {

    public void setOutPutValue(String field, String value, Object object);

    public void persist(Object object)throws Exception;

/*        public boolean performUseValueForDocstore();

        public boolean performLookupValueForTransaction();

        public boolean performLookupValueForDocstore();*/


}
