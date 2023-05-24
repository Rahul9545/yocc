package com.bigv.app.yocc.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RemarkUpdatePojo implements Serializable {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
