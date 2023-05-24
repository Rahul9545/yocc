package com.bigv.app.yocc.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class RemarkResponsePojo implements Serializable {

    @SerializedName("Caller")
    String Caller;

    @SerializedName("TRNO")
    int TRNO;

    @SerializedName("CD_TRNO")
    int CDTRNO;

    @SerializedName("ADUM_USER_ID")
    String ADUMUSERID;

    @SerializedName("Remark")
    String Remark;

    @SerializedName("Name")
    String Name;

    @SerializedName("client_id")
    int clientId;

    @SerializedName("Current_Date")
    String Current_Date;

    @SerializedName("Follow_Date")
    String FollowDate;

    @SerializedName("Caller_ID")
    String CallerID;

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCurrent_Date() {
        return Current_Date;
    }

    public void setCurrent_Date(String current_Date) {
        Current_Date = current_Date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCaller() {
        return Caller;
    }

    public void setCaller(String caller) {
        Caller = caller;
    }

    public int getTRNO() {
        return TRNO;
    }

    public void setTRNO(int TRNO) {
        this.TRNO = TRNO;
    }

    public int getCDTRNO() {
        return CDTRNO;
    }

    public void setCDTRNO(int CDTRNO) {
        this.CDTRNO = CDTRNO;
    }

    public String getADUMUSERID() {
        return ADUMUSERID;
    }

    public void setADUMUSERID(String ADUMUSERID) {
        this.ADUMUSERID = ADUMUSERID;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFollowDate() {
        return FollowDate;
    }

    public void setFollowDate(String followDate) {
        FollowDate = followDate;
    }

    public String getCallerID() {
        return CallerID;
    }

    public void setCallerID(String callerID) {
        CallerID = callerID;
    }
}
