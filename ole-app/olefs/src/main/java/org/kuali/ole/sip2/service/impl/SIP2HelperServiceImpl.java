package org.kuali.ole.sip2.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.service.NettyServer;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.service.SIP2HelperService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public class SIP2HelperServiceImpl implements SIP2HelperService {

    Logger LOG = Logger.getLogger(SIP2HelperServiceImpl.class);

    private BusinessObjectService businessObjectService;

    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    @Override
    public NettyServer startOLESip2Server(StringBuffer responseString, NettyServer olesip2Server) {
        LOG.info("******** Request for starting OLE Socket server ********");
        String portNo = ConfigContext.getCurrentContextConfig().getProperty("sip2.port");
        String serverUrl = ConfigContext.getCurrentContextConfig().getProperty("sip2.url");
        //String circulationService=parameterMap.get("circulationService")[0];
        if (StringUtils.isNotBlank(portNo) && StringUtils.isNotBlank(serverUrl)) {

            //olesip2Server = new NettyServer(Integer.parseInt(portNo), serverUrl);

            try {
                Thread thread = new Thread(olesip2Server);
                thread.start();
                try {
                    Thread.sleep(5 * 1000);
                    if (olesip2Server.getMessage() != null) {
                        responseString.append(olesip2Server.getMessage());
                    } else {
                        responseString.append(OLESIP2Constants.STARTED_SUCCESSFULLY);
                        LOG.info("******** " + OLESIP2Constants.STARTED_SUCCESSFULLY + " ********");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    responseString.append(OLESIP2Constants.PROBLEM_WITH_SERVER + "\n");
                    responseString.append("Exception: \n" + e);
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseString.append(OLESIP2Constants.PROBLEM_WITH_SERVER + "\n");
                responseString.append("Exception: \n" + e);
            }

            //}

        } else {
            responseString.append(OLESIP2Constants.FAIL_TO_LOAD + "\n");
            responseString.append(OLESIP2Constants.PARAMETER_MISSING);
            LOG.info("******** " + OLESIP2Constants.FAIL_TO_LOAD + " ********");
            LOG.info("******** " + OLESIP2Constants.PARAMETER_MISSING + " ********");
        }
        return olesip2Server;

    }


    @Override
    public void stopOLESip2Server(StringBuffer responseString, NettyServer olesip2Server) {
        LOG.info("******** Request for stop OLE Socket server ********");
        if (olesip2Server != null) {
            olesip2Server.stop();
            responseString.append(OLESIP2Constants.SERVER_STOPPED);
            LOG.info("******** " + OLESIP2Constants.SERVER_STOPPED + " ********");
        }

    }

    @Override
    public void startOLESip2Server() {
        boolean onLoadStartup = ConfigContext.getCurrentContextConfig().getBooleanProperty("sip2.startOnLoad");
        if (onLoadStartup) {
            StringBuffer responseString = new StringBuffer();
            OLESIP2Constants.sip2Server = startOLESip2Server(responseString, OLESIP2Constants.sip2Server);
            LOG.info(responseString.toString());
        }

    }

    @Override
    public void stopOLESip2Server() {
        StringBuffer responseString = new StringBuffer();
        stopOLESip2Server(responseString, OLESIP2Constants.sip2Server);
        LOG.info(responseString.toString());

    }

}
