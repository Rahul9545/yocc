package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 9/11/17.
 */

public class CallDetailsPojo implements Serializable {

    private String cdTrNo;
    private String callerName;
    private String callerNumber;
    private String callDuration;
    private String operatorName;
    private String operatorNumber;
    private String status;
    private String startTime;
    private String endTime;
    private String date;
    private String menuName;
    private String menuDescription;
    private String callType;
    private String followUp;
    private String remark;
    private String callPriority;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String email;
    private boolean isCallBlock;

    public String getCdTrNo() {
        return cdTrNo;
    }

    public void setCdTrNo(String cdTrNo) {
        this.cdTrNo = cdTrNo;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorNumber(String operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getFollowUp() {
        return followUp;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCallPriority() {
        return callPriority;
    }

    public void setCallPriority(String callPriority) {
        this.callPriority = callPriority;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCallBlock() {
        return isCallBlock;
    }

    public void setCallBlock(boolean callBlock) {
        isCallBlock = callBlock;
    }

    @Override
    public String toString() {
        return "ClassPojo [startTime = " + startTime + ", callerNumber = " + callerNumber + ", callDuration = " + callDuration + ", operatorName = " + operatorName + ", status = " + status + ", menuDescription = " + menuDescription + ", callerName = " + callerName + ", operatorNumber = " + operatorNumber + ", endTime = " + endTime + ", date = " + date + ", menuName = " + menuName + "]";
    }
}