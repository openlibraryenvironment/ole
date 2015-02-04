package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleGenerateNoticeTypeService extends ActionTypeServiceBase {
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.GENERATE_NOTICE);
        return  new OleGenerateNotice(parameter);
    }
    public class OleGenerateNotice implements Action {
        private String notice;


        public OleGenerateNotice(String notice) {
            this.notice = notice;
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            environment.getEngineResults().setAttribute(OLEConstants.NOTICE,notice);
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }
    }
}
