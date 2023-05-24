package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 9/11/17.
 */

public class ReportCallDetailsPojo implements Serializable {

    private String srNo;
    private String date;
    private String callerDetails;
    private String startTime;
    private String endTime;
    private String callDuration;
    private String status;
    private String menuDetails;
    private String operatorDetails;

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCallerDetails() {
        return callerDetails;
    }

    public void setCallerDetails(String callerDetails) {
        this.callerDetails = callerDetails;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(String menuDetails) {
        this.menuDetails = menuDetails;
    }

    public String getOperatorDetails() {
        return operatorDetails;
    }

    public void setOperatorDetails(String operatorDetails) {
        this.operatorDetails = operatorDetails;
    }
}