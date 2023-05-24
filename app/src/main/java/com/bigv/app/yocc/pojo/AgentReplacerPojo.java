package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 25/11/17.
 */

public class AgentReplacerPojo implements Serializable {

    private int conId;
    private int operatorId;
    private String operatorName;
    private String operatorNo;
    private String menuId;
    private String menuName;
    private String sequence;
    private String languageKey;
    private boolean isActive;

    public int getConId() {
        return conId;
    }

    public void setConId(int conId) {
        this.conId = conId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getLanguageKey() {
        return languageKey;
    }

    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
