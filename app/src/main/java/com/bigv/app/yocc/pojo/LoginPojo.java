package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by mithun on 13/9/17.
 */

public class LoginPojo implements Serializable {

    String key;
    String status;
    String companyName;
    String logoUrl;
    String yoccNumber;
    String userId;
    String userType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getYoccNumber() {
        return yoccNumber;
    }

    public void setYoccNumber(String yoccNumber) {
        this.yoccNumber = yoccNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
