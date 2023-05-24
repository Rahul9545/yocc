package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 3/11/17.
 */

public class HomeScreenPojo implements Serializable {

    private int callTransferBalance;
    private int totalCall;
    private int totalLiveCall;
    private int smsBalanace;
    private int totalAnsweredCallCount;
    private int totalNotAnsweredCallCount;
    private int totalUnoptedCallCount;
    private int totalMissedCallCount;
    private int overAllAnswerdCall;
    private int overAllNotAnsweredCall;
    private int overAllUnoptedCall;
    private int overAllTotalCall;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCallTransferBalance() {
        return callTransferBalance;
    }

    public void setCallTransferBalance(int callTransferBalance) {
        this.callTransferBalance = callTransferBalance;
    }

    public int getTotalCall() {
        return totalCall;
    }

    public void setTotalCall(int totalCall) {
        this.totalCall = totalCall;
    }

    public int getTotalAnsweredCallCount() {
        return totalAnsweredCallCount;
    }

    public void setTotalAnsweredCallCount(int totalAnsweredCallCount) {
        this.totalAnsweredCallCount = totalAnsweredCallCount;
    }

    public int getTotalNotAnsweredCallCount() {
        return totalNotAnsweredCallCount;
    }

    public void setTotalNotAnsweredCallCount(int totalNotAnsweredCallCount) {
        this.totalNotAnsweredCallCount = totalNotAnsweredCallCount;
    }

    public int getTotalUnoptedCallCount() {
        return totalUnoptedCallCount;
    }

    public void setTotalUnoptedCallCount(int totalUnoptedCallCount) {
        this.totalUnoptedCallCount = totalUnoptedCallCount;
    }

    public int getTotalMissedCallCount() {
        return totalMissedCallCount;
    }

    public void setTotalMissedCallCount(int totalMissedCallCount) {
        this.totalMissedCallCount = totalMissedCallCount;
    }

    public int getTotalLiveCall() {
        return totalLiveCall;
    }

    public void setTotalLiveCall(int totalLiveCall) {
        this.totalLiveCall = totalLiveCall;
    }

    public int getSmsBalanace() {
        return smsBalanace;
    }

    public void setSmsBalanace(int smsBalanace) {
        this.smsBalanace = smsBalanace;
    }

    public int getOverAllAnswerdCall() {
        return overAllAnswerdCall;
    }

    public void setOverAllAnswerdCall(int overAllAnswerdCall) {
        this.overAllAnswerdCall = overAllAnswerdCall;
    }

    public int getOverAllNotAnsweredCall() {
        return overAllNotAnsweredCall;
    }

    public void setOverAllNotAnsweredCall(int overAllNotAnsweredCall) {
        this.overAllNotAnsweredCall = overAllNotAnsweredCall;
    }

    public int getOverAllUnoptedCall() {
        return overAllUnoptedCall;
    }

    public void setOverAllUnoptedCall(int overAllUnoptedCall) {
        this.overAllUnoptedCall = overAllUnoptedCall;
    }

    public int getOverAllTotalCall() {
        return overAllTotalCall;
    }

    public void setOverAllTotalCall(int overAllTotalCall) {
        this.overAllTotalCall = overAllTotalCall;
    }
}