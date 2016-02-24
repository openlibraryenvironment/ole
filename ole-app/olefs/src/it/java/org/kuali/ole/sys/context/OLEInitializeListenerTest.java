package org.kuali.ole.sys.context;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.oclc.NettyClient;
import org.kuali.ole.oclc.OCLCNettyServerHandler;
import org.kuali.ole.service.NettyHandler;
import org.kuali.ole.service.NettyServer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

/**
 * Created by angelind on 2/23/16.
 */
public class OLEInitializeListenerTest{

    @Test
    public void testInitializeNettyHandlers() {
        OLEInitializeListener oleInitializeListener = new OLEInitializeListener();
        String host = "localhost";
        int port = 10001;

        Map<Integer, NettyHandler> nettyHandlerMap = new HashMap<>();
        nettyHandlerMap.put(Integer.valueOf(port), new OCLCNettyServerHandler("http://localhost:8080/olefs"));

        oleInitializeListener.startNettyServers(nettyHandlerMap);

        String request = "This is the request for TCP Server";
        String response = new NettyClient().sendRequestToNettyServer(request, host, port);
        assertTrue(StringUtils.isNotBlank(response));
        System.out.println(response);
    }
}
