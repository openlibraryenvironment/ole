package org.kuali.ole.sip2.service;

import org.kuali.ole.service.NettyServer;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public interface SIP2HelperService {

    public NettyServer startOLESip2Server(StringBuffer responseString, NettyServer olesip2Server);

    public void stopOLESip2Server(StringBuffer responseString, NettyServer olesip2Server);

    public void startOLESip2Server();

    public void stopOLESip2Server();

}
