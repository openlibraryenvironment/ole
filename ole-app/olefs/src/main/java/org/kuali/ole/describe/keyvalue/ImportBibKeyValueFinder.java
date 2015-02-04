package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.ImportBibUserPreferences;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 30/11/12
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportBibKeyValueFinder
        extends UifKeyValuesFinderBase {
    @Override
    public List<KeyValue> getKeyValues(ViewModel viewModel) {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<ImportBibUserPreferences> importBibUserPreferences = KRADServiceLocator.getBusinessObjectService()
                .findAll(
                        ImportBibUserPreferences.class);
        for (ImportBibUserPreferences userPref : importBibUserPreferences) {
            options.add(new ConcreteKeyValue(String.valueOf(userPref.getPrefId()), userPref.getPrefName()));
        }
        return options;
    }
}
