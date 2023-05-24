package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 14/11/17.
 */

public class WeekWiseCallDurationPojo implements Serializable{

    private String callDuration;
    private String day;
    private String dateName;

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
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
        return "ClassPojo [callDuration = " + callDuration + ", day = " + day + ", dateName = " + dateName + "]";
    }
}
