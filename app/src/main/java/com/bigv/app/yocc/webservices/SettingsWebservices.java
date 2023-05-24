package com.bigv.app.yocc.webservices;

import com.bigv.app.yocc.pojo.AgentMasterPojo;
import com.bigv.app.yocc.pojo.AgentReplacerPojo;
import com.bigv.app.yocc.pojo.ChangePasswordPojo;
import com.bigv.app.yocc.pojo.LanguagePojo;
import com.bigv.app.yocc.pojo.MenuMasterPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.pojo.RoutingPatternPojo;
import com.bigv.app.yocc.pojo.RoutingTypePojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by MiTHUN on 7/11/17.
 */

public interface SettingsWebservices {

    @POST("ChangePassword")
    Call<ResultPojo> changePassword(@Header("key") String key,
                                    @Body ChangePasswordPojo changePasswordPojo);

    @GET("AgentMaster/List")
    Call<List<AgentMasterPojo>> getAgentMasterList(@Header("key") String key);

    @POST("AgentMaster/save")
    Call<ResultPojo> saveAgentMaster(@Header("key") String key,
                                     @Body AgentMasterPojo agentMasterPojo);

    @POST("AgentMaster/Delete")
    Call<ResultPojo> deleteAgent(@Header("key") String string,
                                 @Body AgentMasterPojo agentMasterPojo);

    @GET("AgentRelacer/List")
    Call<List<AgentReplacerPojo>> getAgentReplacerList(@Header("key") String key);

    @POST("AgentRelacer/save")
    Call<ResultPojo> saveAgentReplacer(@Header("key") String key,
                                       @Body AgentReplacerPojo agentReplacerPojo);

    @POST("AgentRelacer/Delete")
    Call<ResultPojo> deleteAgentReplacer(@Header("key") String string,
                                         @Body AgentReplacerPojo agentReplacerPojo);

    @GET("Menu/List")
    Call<List<MenuMasterPojo>> getMenuMasterList(@Header("key") String key);

    @POST("Menu/Edit")
    Call<ResultPojo> saveMenuMaster(@Header("key") String key,
                                    @Body MenuMasterPojo menuMasterPojo);

    @GET("Menu/RouttingPattern")
    Call<List<RoutingPatternPojo>> getRoutingPatternList(@Header("key") String key);


    @GET("Menu/RouttingType")
    Call<List<RoutingTypePojo>> getRoutingTypeList(@Header("key") String key);

    @GET("Menu/Language")
    Call<List<LanguagePojo>> getLanguageList(@Header("key") String key);

    @GET("AgentMaster/Download/Excel")
    Call<ResultPojo> downloadXlsAgentMaster(@Header("key") String key);

    @GET("AgentMaster/Download/PDF")
    Call<ResultPojo> downloadPdfAgentMaster(@Header("key") String key);

    @GET("Menu/Download/Excel")
    Call<ResultPojo> downloadXlsMenuMaster(@Header("key") String key);

    @GET("Menu/Download/PDF")
    Call<ResultPojo> downloadPdfMenuMaster(@Header("key") String key);

    @GET("AgentRelacer/Download/Excel")
    Call<ResultPojo> downloadXlsAgentReplacer(@Header("key") String key);

    @GET("AgentRelacer/Download/PDF")
    Call<ResultPojo> downloadPdfAgentReplacer(@Header("key") String key);

}
