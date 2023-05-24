package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 15/11/17.
 */

public class HourWiseCallPojo implements Serializable {

    private int totalCall;
    private String hours;

    public int getTotalCall() {
        return totalCall;
    }

    public void setTotalCall(int totalCall) {
        this.totalCall = totalCall;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}