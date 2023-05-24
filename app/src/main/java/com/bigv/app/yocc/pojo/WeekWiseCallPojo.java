package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 14/11/17.
 */

public class WeekWiseCallPojo implements Serializable{

    private String totalCall;
    private String day;
    private String dateName;

    public String getTotalCall() {
        return totalCall;
    }

    public void setTotalCall(String totalCall) {
        this.totalCall = totalCall;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    @Override
    public String toString() {
        return "ClassPojo [totalCall = " + totalCall + ", day = " + day + ", dateName = " + dateName + "]";
    }
}
