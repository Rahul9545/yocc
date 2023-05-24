package com.bigv.app.yocc.pojo;

import java.util.List;

/**
 * Created by MiTHUN on 26/12/17.
 */

public class CallDetailsListAndSummeryPojo {


    private String unanswered;
    private String totalCall;
    private String unoptedCall;
    private String answered;
    private List<CallDetailsPojo> callDetails;


    public String getUnanswered() {
        return unanswered;
    }

    public void setUnanswered(String unanswered) {
        this.unanswered = unanswered;
    }

    public String getTotalCall() {
        return totalCall;
    }

    public void setTotalCall(String totalCall) {
        this.totalCall = totalCall;
    }

    public String getUnoptedCall() {
        return unoptedCall;
    }

    public void setUnoptedCall(String unoptedCall) {
        this.unoptedCall = unoptedCall;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public List<CallDetailsPojo> getCallDetails() {
        return callDetails;
    }

    public void setCallDetails(List<CallDetailsPojo> callDetails) {
        this.callDetails = callDetails;
    }

    @Override
    public String toString() {
        return "ClassPojo [unanswered = " + unanswered + ", callDetails = " + callDetails + ", totalCall = " + totalCall + ", unoptedCall = " + unoptedCall + ", answered = " + answered + "]";
    }
}