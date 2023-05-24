package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 15/11/17.
 */

public class MonthWiseCallPojo implements Serializable {

    private int notAnswered;
    private int inboundCount;
    private int unopted;
    private int answered;
    private String monthName;

    public int getNotAnswered() {
        return notAnswered;
    }

    public void setNotAnswered(int notAnswered) {
        this.notAnswered = notAnswered;
    }

    public int getInboundCount() {
        return inboundCount;
    }

    public void setInboundCount(int inboundCount) {
        this.inboundCount = inboundCount;
    }

    public int getUnopted() {
        return unopted;
    }

    public void setUnopted(int unopted) {
        this.unopted = unopted;
    }

    public int getAnswered() {
        return answered;
    }

    public void setAnswered(int answered) {
        this.answered = answered;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    @Override
    public String toString() {
        return "ClassPojo [notAnswered = " + notAnswered + ", monthName = " + monthName + ", inboundCount = " + inboundCount + ", unopted = " + unopted + ", answered = " + answered + "]";
    }
}