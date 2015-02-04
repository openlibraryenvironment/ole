package org.kuali.ole.describe.service;

import org.kuali.ole.describe.form.CallNumberBrowseForm;
import org.kuali.ole.describe.form.OLESearchForm;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/14/13
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BrowseService {


    public List callNumberBrowse(CallNumberBrowseForm callNumberBrowseForm);

    public List callNumberBrowsePrev(CallNumberBrowseForm callNumberBrowseForm);

    public List callNumberBrowseNext(CallNumberBrowseForm callNumberBrowseForm);

    public boolean getPreviosFlag();

    public boolean getNextFlag();

    public String getPageShowEntries();

    public List browse(OLESearchForm oleSearchForm);

    public List browseOnChange(OLESearchForm oleSearchForm);

    public List browsePrev(OLESearchForm oleSearchForm);

    public List browseNext(OLESearchForm oleSearchForm);

    public String validateLocation(String locationString);
}
