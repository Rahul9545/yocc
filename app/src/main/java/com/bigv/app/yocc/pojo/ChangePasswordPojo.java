package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by mithun on 13/9/17.
 */

public class ChangePasswordPojo implements Serializable{

    String userID;
    String oldPassword;
    String newPassword;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
