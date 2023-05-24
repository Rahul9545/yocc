package com.bigv.app.yocc.pojo;

import com.google.gson.annotations.SerializedName;

/****
 * Created by Rahul 1/2/2023
 * */

public class PasswordCheckPojo {
    @SerializedName("passwordstatus")
    boolean passwordstatus;

    public PasswordCheckPojo(boolean passwordstatus) {
        this.passwordstatus = passwordstatus;
    }

    public boolean isPasswordstatus() {
        return passwordstatus;
    }

    public void setPasswordstatus(boolean passwordstatus) {
        this.passwordstatus = passwordstatus;
    }

    @Override
    public String toString() {
        return "PasswordCheckPojo{" +
                "passwordstatus=" + passwordstatus +
                '}';
    }
}
