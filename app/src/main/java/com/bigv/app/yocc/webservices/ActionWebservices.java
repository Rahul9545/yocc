package com.bigv.app.yocc.webservices;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import com.bigv.app.yocc.pojo.CallBlockPojo;
import com.bigv.app.yocc.pojo.CallDetailsPojo;
import com.bigv.app.yocc.pojo.CallTranscriptionPojo;
import com.bigv.app.yocc.pojo.CallifyPojo;
import com.bigv.app.yocc.pojo.ResultPojo;

/**
 * Created by MiTHUN on 7/11/17.
 */

public interface ActionWebservices {

    @POST("Action/BlockCaller")
    Call<ResultPojo> callBlock(@Header("key") String key,
                               @Body CallBlockPojo callBlockPojo);

    @GET("Action/DownloadandPlay")
    Call<ResultPojo> getFileDownloadData(@Header("key") String string,
                                         @Header("cdTrNo") String cdTrNo);

    @POST("Action/Callify")
    Call<ResultPojo> callIVR(@Header("key") String key,
                             @Body CallifyPojo callifyPojo);

    @GET("CallDetail/CallTranscription")
    Call<List<CallTranscriptionPojo>> getCallTranscriptionList(@Header("key") String key,
                                                               @Header("cdTrNo") String cdTrNo);
}
