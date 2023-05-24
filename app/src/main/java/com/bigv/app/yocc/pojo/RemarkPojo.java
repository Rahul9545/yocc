package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 12/12/17.
 */

public class RemarkPojo implements Serializable {

    private int trNo;
    private String date;
    private String remark;
    private String followUp;

    public int getTrNo() {
        return trNo;
    }

    public void setTrNo(int trNo) {
        this.trNo = trNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFollowUp() {
        return followUp;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }
}
