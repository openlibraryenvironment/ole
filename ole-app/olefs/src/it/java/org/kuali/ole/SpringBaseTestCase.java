package org.kuali.ole;

import org.junit.Before;
import org.kuali.ole.deliver.defaultload.LoadDefaultCirculationPoliciesBean;
import org.kuali.ole.deliver.defaultload.LoadDefaultEResourceBean;
import org.kuali.ole.deliver.defaultload.LoadDefaultLicensesBean;
import org.kuali.ole.deliver.defaultload.LoadDefaultPatronsBean;
import org.kuali.ole.describe.defaultload.LoadDefaultLocationsBean;
import org.kuali.ole.ingest.LoadDefaultIngestProfileBean;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

import javax.xml.namespace.QName;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 8/22/13
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */

@BaselineMode(Mode.ROLLBACK)
public abstract class SpringBaseTestCase extends BaselineTestCase {

    private static final String MODULE_NAME="kole";
    private SpringResourceLoader oleTestResourceLoader;

    public SpringBaseTestCase() {
        super(MODULE_NAME);
    }

    @Override
     protected List<Lifecycle> getSuiteLifecycles() {
        List<Lifecycle> suiteLifecycles = super.getSuiteLifecycles();
        suiteLifecycles.add(new Lifecycle() {
            @Override
            public void start() throws Exception {
                SpringContext.finishInitializationAfterRiceStartup();
            }

            @Override
            public void stop() throws Exception {

            }

            @Override
            public boolean isStarted() {
                return false;
            }
        });
        return suiteLifecycles;
    }

    @Override
    protected void loadSuiteTestData() throws Exception {
        /* Ingest Profile bean*/
        LoadDefaultIngestProfileBean loadDefaultIngestProfileBean
                = GlobalResourceLoader.getService("loadDefaultIngestProfileBean");
        try {
            List<String> strings = loadDefaultIngestProfileBean.loadDefaultIngestProfile(false);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

         /*Circulation Policies*/
        LoadDefaultCirculationPoliciesBean loadDefaultCirculationPoliciesBean =
                GlobalResourceLoader.getService("loadDefaultCirculationPoliciesBean");
        try {
            loadDefaultCirculationPoliciesBean.loadDefaultCircPolicies(false);
        } catch (Exception e) {
            LOG.error(e, e);
        }

         /*Location*/
        LoadDefaultLocationsBean loadDefaultLocationsBean = GlobalResourceLoader.getService("loadDefaultLocationsBean");
        try {
            loadDefaultLocationsBean.loadDefaultLocations(false);
        } catch (Exception e) {
            //e.printStackTrace();
            LOG.error("loadDefaultLocations Exception:" + e);
        }

         /*Default Patron*/
        LoadDefaultPatronsBean loadDefaultPatronsBean = GlobalResourceLoader.getService("loadDefaultPatronsBean");
        try {
            loadDefaultPatronsBean.loadDefaultPatrons(false);
        } catch (Exception e) {
            LOG.error(e, e);
        }

        /*Default License*/
        LoadDefaultLicensesBean loadDefaultLicensesBean =
                GlobalResourceLoader.getService("loadDefaultLicensesBean");
        try {
            loadDefaultLicensesBean.loadDefaultLicenses(false);
        } catch (Exception e) {
            LOG.error(e, e);
        }

         /*Eresoruce*/
        LoadDefaultEResourceBean loadDefaultEResourceBean =
                GlobalResourceLoader.getService("loadDefaultEResourcesBean");
        try {
            loadDefaultEResourceBean.loadDefaultEResource(false);
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    @Override
    protected Lifecycle getLoadApplicationLifecycle() {
        if (oleTestResourceLoader == null) {
            oleTestResourceLoader = new SpringResourceLoader(new QName("OLETestHarnessApplicationResourceLoader"), "classpath:org/kuali/ole/TestHarnessSpringBeans.xml", null);
            oleTestResourceLoader.setParentSpringResourceLoader(getTestHarnessSpringResourceLoader());
            getTestHarnessSpringResourceLoader().addResourceLoader(oleTestResourceLoader);
        }
        return oleTestResourceLoader;
    }

    protected List<String> getPerTestTablesNotToClear() {
        List<String> tablesNotToClear = new ArrayList<>();
        tablesNotToClear.addAll(super.getPerTestTablesNotToClear());
        tablesNotToClear.add("KRIM_.*");
        tablesNotToClear.add("KRNS_.*");
        tablesNotToClear.add("KREW_.*");
        tablesNotToClear.add("KREN_.*");
        tablesNotToClear.add("KRCR_.*");
        tablesNotToClear.add("KRMS_.*");


        return tablesNotToClear;
    }

    protected List<String> getPerTestTablesToClear() {
        List<String> tablesToClear = new ArrayList<>();

        tablesToClear.addAll(super.getPerTestTablesToClear());
        tablesToClear.add("OLE_.*");

        return tablesToClear;
    }

    @Override
    protected String getModuleName() {
        setClearTables(false);
        return MODULE_NAME;
    }

}
