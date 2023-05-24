package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 9/11/17.
 */

public class CallDetailsSummeryPojo implements Serializable {

    String totalCall;
    String answered;
    String unanswered;
    String unoptedCall;

    public String getTotalCall() {
        return totalCall;
    }

    public void setTotalCall(String totalCall) {
        this.totalCall = totalCall;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getUnanswered() {
        return unanswered;
    }

    public void setUnanswered(String unanswered) {
        this.unanswered = unanswered;
    }

    public String getUnoptedCall() {
        return unoptedCall;
    }

    public void setUnoptedCall(String unoptedCall) {
        this.unoptedCall = unoptedCall;
    }
}