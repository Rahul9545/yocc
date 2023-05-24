package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 23/11/17.
 */

public class AgentMasterPojo implements Serializable {

    private int operatorId;
    private String operatorNo;
    private String operatorName;
    private boolean isActive;
    private String extension;

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "ClassPojo [extension = " + extension + ", isActive = " + isActive + ", operatorName = " + operatorName + ", operatorId = " + operatorId + ", operatorNo = " + operatorNo + "]";
    }
}
