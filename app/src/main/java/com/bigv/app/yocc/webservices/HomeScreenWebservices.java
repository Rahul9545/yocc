package com.bigv.app.yocc.webservices;

import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.pojo.HourWiseCallPojo;
import com.bigv.app.yocc.pojo.LiveCallPojo;
import com.bigv.app.yocc.pojo.MonthWiseCallPojo;
import com.bigv.app.yocc.pojo.WeekWiseCallDurationPojo;
import com.bigv.app.yocc.pojo.WeekWiseCallPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by MiTHUN on 7/11/17.
 */

public interface HomeScreenWebservices {

    @GET("dashboard")
    Call<HomeScreenPojo> getHomeScreenData(@Header("key") String key,
                                           @Header("userid") String userId,
                                           @Header("usertype") String userType,
                                           @Header("username") String username,
                                           @Header("password") String password);

    @GET("MonthWiseDashboard")
    Call<List<MonthWiseCallPojo>> getMonthWiseCall(@Header("key") String key,
                                                   @Header("userid") String userId,
                                                   @Header("usertype") String userType);

    @GET("WeekWise/call")
    Call<List<WeekWiseCallPojo>> getWeekWiseCall(@Header("key") String key,
                                                 @Header("userid") String userId,
                                                 @Header("usertype") String userType);

    @GET("WeekWise/callduration")
    Call<List<WeekWiseCallDurationPojo>> getWeekWiseCallDuration(@Header("key") String key,
                                                                 @Header("userid") String userId,
                                                                 @Header("usertype") String userType);

    @GET("LiveCall")
    Call<List<LiveCallPojo>> getLiveCallList(@Header("key") String key,
                                             @Header("userid") String userId,
                                             @Header("usertype") String userType);


    @GET("HoursWiseCall")
    Call<List<HourWiseCallPojo>> getHourWiseCall(@Header("key") String key);
}
