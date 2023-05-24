package com.bigv.app.yocc.webservices;

import com.bigv.app.yocc.pojo.CallDetailsPojo;
import com.bigv.app.yocc.pojo.CallDetailsSummeryPojo;
import com.bigv.app.yocc.pojo.CallPriorityPojo;
import com.bigv.app.yocc.pojo.RemarkPojo;
import com.bigv.app.yocc.pojo.ResultPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by MiTHUN on 9/11/17.
 */

public interface CallDetailWebservice {

    @GET("CallDetail/List")
    Call<List<CallDetailsPojo>> getCallDetailsList(@Header("key") String key,
                                                   @Header("fdate") String fdate,
                                                   @Header("tdate") String tdate,
                                                   @Header("userid") String userId,
                                                   @Header("usertype") String userType);

    @GET("CallDetail/Summary")
    Call<CallDetailsSummeryPojo> getCallDetailsSummery(@Header("key") String key,
                                                       @Header("fdate") String fdate,
                                                       @Header("tdate") String tdate,
                                                       @Header("userid") String userId,
                                                       @Header("usertype") String userType);

    @GET("CallDetail/CallPriority")
    Call<List<CallPriorityPojo>> getCallPriorityList();

    @POST("CallDetail/Edit")
    Call<ResultPojo> saveCallDetails(@Header("key") String key,
                                     @Body CallDetailsPojo callDetailsPojo);

    @GET("CallDetail/RemarkList")
    Call<List<RemarkPojo>> getCallRemarkList(@Header("key") String key,
                                             @Header("callerNumber") String callerNumber);
}
