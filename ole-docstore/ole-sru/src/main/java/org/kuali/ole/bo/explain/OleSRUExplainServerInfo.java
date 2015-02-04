package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainServerInfo {

    private String host;
    private String port;
    private String database;

    private String protocol;
    private String version;
    private String transport;
    private String method;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "Explain ServerInfo{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", protocol='" + protocol + '\'' +
                ", version='" + version + '\'' +
                ", transport='" + transport + '\'' +
                ", method='" + method + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
