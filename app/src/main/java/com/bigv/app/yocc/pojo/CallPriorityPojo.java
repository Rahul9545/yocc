package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 22/11/17.
 */

public class CallPriorityPojo implements Serializable{

    private String callPriority;
    private String callType;

    public String getCallPriority() {
        return callPriority;
    }

    public void setCallPriority(String callPriority) {
        this.callPriority = callPriority;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
