
package cn.lacknb.dto;

import java.beans.PropertyVetoException;

/**
 * <h2></h2>
 * @author niebaohua
 * date 2021/6/7
 */
public class CommonConfig {

    private String url;
    private String nacos;
    private String mysql;
    private String masterUsername;
    private String masterPassword;
    private String slaverUsername;
    private String slaverPassword;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNacos() {
        return nacos;
    }

    public void setNacos(String nacos) {
        this.nacos = nacos;
    }

    public String getMysql() {
        return mysql;
    }

    public void setMysql(String mysql) {
        this.mysql = mysql;
    }

    public String getMasterUsername() {
        return masterUsername;
    }

    public void setMasterUsername(String masterUsername) {
        this.masterUsername = masterUsername;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public String getSlaverUsername() {
        return slaverUsername;
    }

    public void setSlaverUsername(String slaverUsername) {
        this.slaverUsername = slaverUsername;
    }

    public String getSlaverPassword() {
        return slaverPassword;
    }

    public void setSlaverPassword(String slaverPassword) {
        this.slaverPassword = slaverPassword;
    }

    @Override
    public String toString() {
        return "CommonConfig{" +
                "url='" + url + '\'' +
                ", nacos='" + nacos + '\'' +
                ", mysql='" + mysql + '\'' +
                ", masterUsername='" + masterUsername + '\'' +
                ", masterPassword='" + masterPassword + '\'' +
                ", slaverUsername='" + slaverUsername + '\'' +
                ", slaverPassword='" + slaverPassword + '\'' +
                '}';
    }
}
