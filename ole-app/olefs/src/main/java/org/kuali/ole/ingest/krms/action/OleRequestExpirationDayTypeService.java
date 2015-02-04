package org.kuali.ole.ingest.krms.action;

import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;

import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/23/12
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleRequestExpirationDayTypeService extends ActionTypeServiceBase {
    private static final Logger LOG = Logger.getLogger(OleRequestExpirationDayTypeService.class);
    /**
     *      This method invokes appropriate Bib action based on action definition's name
     * @param actionDefinition
     * @return Action
     */
    @Override
    public Action loadAction(ActionDefinition actionDefinition) {
        String parameter= actionDefinition.getAttributes().get(OLEConstants.REQ_EXPIRATION_DAY_LIMIT);
        return new OleRequestExpirationDayAction(parameter);
    }

    public class OleRequestExpirationDayAction implements Action{
        private int numberOfDays;

        public OleRequestExpirationDayAction(String numberOfDays) {
            this.numberOfDays = new Integer(numberOfDays);
        }

        @Override
        public void execute(ExecutionEnvironment environment) {
            LOG.info("in the execute method"+numberOfDays);
           environment.getEngineResults().setAttribute(OLEConstants.REQ_EXPIRATION_DATE,addDate(new java.sql.Date(System.currentTimeMillis()),this.numberOfDays));
        }

        @Override
        public void executeSimulation(ExecutionEnvironment environment) {
            execute(environment);
        }

        private java.sql.Date addDate(java.sql.Date in, int daysToAdd) {
            if (in == null) {
                return null;
            }
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(in);
            cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
            return new java.sql.Date(cal.getTime().getTime());
        }
    }
}