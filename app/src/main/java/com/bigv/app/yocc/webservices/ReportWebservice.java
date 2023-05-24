package com.bigv.app.yocc.webservices;

import com.bigv.app.yocc.pojo.ReportCallListAndSummeryPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by MiTHUN on 9/11/17.
 */

public interface ReportWebservice {

    @GET("CallDetail/List")
    Call<ReportCallListAndSummeryPojo> getReportDailyCallDetailsList(@Header("key") String key,
                                                                     @Header("fdate") String fdate);

    @GET("CallDetail/List")
    Call<ReportCallListAndSummeryPojo> getReportWeeklyCallDetailsList(@Header("key") String key);

}
