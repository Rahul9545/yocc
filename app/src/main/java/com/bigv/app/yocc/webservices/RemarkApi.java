package com.bigv.app.yocc.webservices;

import com.bigv.app.yocc.pojo.RemarkResponsePojo;
import com.bigv.app.yocc.pojo.RemarkUpdatePojo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RemarkApi {

    @GET("Remarks/List")
    Call<ArrayList<RemarkResponsePojo>> remarkNotificationList(@Header("key") String key,
                                                          @Header("userid") String userId);

    @GET("Remarks/ListDetails")
    Call<ArrayList<RemarkResponsePojo>> remarkCallerList(@Header("key") String key,
                                                         @Header("userid") String userId,
                                                         @Header("caller_id") String callerNumber);

    @POST("Remarks/InsertRemarks")
    Call<RemarkUpdatePojo> callerRemarkInsert(@Header("key") String key,
                                              @Header("userid") String userId,
                                              @Header("caller_id") String callerNumber,
                                              @Header("remarks") String callerRemark,
                                              @Header("FollowUpDate") String followUpDate);


}
