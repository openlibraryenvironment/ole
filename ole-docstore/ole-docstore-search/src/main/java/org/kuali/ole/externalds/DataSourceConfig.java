package org.kuali.ole.externalds;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 2/13/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSourceConfig {
    private Integer id;
    private String name;
    private String desc;
    private String domainName;
    private String portNum;
    private String loginId;
    private String authKey;
    private String password;
    private String databaseName;


    public void setAuthKey(String authKey) {

        this.authKey = authKey;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortNum() {
        return portNum;
    }

    public void setPortNum(String portNum) {
        this.portNum = portNum;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
