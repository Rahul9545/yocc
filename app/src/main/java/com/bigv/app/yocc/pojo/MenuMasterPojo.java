package com.bigv.app.yocc.pojo;

import java.io.Serializable;

/**
 * Created by MiTHUN on 25/11/17.
 */

public class MenuMasterPojo implements Serializable {

    private String menuId;
    private String menuName;
    private String menuDescription;
    private String patId;
    private String typeId;
    private String dialTimeout;
    private String extension;


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

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDialTimeout() {
        return dialTimeout;
    }

    public void setDialTimeout(String dialTimeout) {
        this.dialTimeout = dialTimeout;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
