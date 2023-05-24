package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 13/12/17.
 */

public class CallifyPojo implements Serializable {

    private String callerNumber;
    private String operatorNumber;

    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorNumber(String operatorNumber) {
        this.operatorNumber = operatorNumber;
    }
}
