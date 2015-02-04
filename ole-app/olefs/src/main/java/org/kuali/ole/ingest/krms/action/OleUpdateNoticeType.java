package org.kuali.ole.ingest.krms.action;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 12/24/12
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleUpdateNoticeType implements Action {
    private String noticeType;

    public OleUpdateNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    @Override
    public void execute(ExecutionEnvironment environment) {
        environment.getEngineResults().setAttribute(OLEConstants.NOTICE_TYPE,noticeType);
    }

    @Override
    public void executeSimulation(ExecutionEnvironment environment) {
        execute(environment);
    }
}
