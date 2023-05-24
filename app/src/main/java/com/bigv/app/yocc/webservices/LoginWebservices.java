package com.bigv.app.yocc.webservices;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import com.bigv.app.yocc.pojo.LoginPojo;
import com.bigv.app.yocc.pojo.PasswordCheckPojo;

/**
 * Created by mithun on 13/9/17.
 * Updated by Rahul Rokade 30/01/2023
 */

public interface LoginWebservices {

    @GET("login")
    Call<LoginPojo> loginUser(@Header("userId") String username,
                              @Header("password") String password);

    @GET("passwordcheck")
    Call<PasswordCheckPojo> checkPassApi(@Header("password") String password,
                                       @Header("key") String key,
                                       @Header("Username") String username);
}
