package org.kuali.ole.sip2.sip2Server.processor;

/**
 * Created by pvsubrah on 9/28/15.
 */
public abstract class NettyProcessor {
    public abstract boolean isInterested(String code);
    public abstract String process();
}
