package com.bigv.app.yocc.webservices;

import com.bigv.app.yocc.pojo.AddressBookPojo;
import com.bigv.app.yocc.pojo.GroupAddressBookPojo;
import com.bigv.app.yocc.pojo.ResultPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by MiTHUN on 7/11/17.
 */

public interface AddressBookWebservices {

    @GET("AddressBook/List")
    Call<List<AddressBookPojo>> getAddressBookList(@Header("key") String key);

    @POST("AddressBook/Save")
    Call<ResultPojo> saveAddressBook(@Header("key") String key,
                                     @Body AddressBookPojo addressBookPojo);

    @POST("AddressBook/Delete")
    Call<ResultPojo> deleteAddressBook(@Header("key") String string,
                                       @Body AddressBookPojo addressBookPojo);

    @GET("AddressBook/Group/List")
    Call<List<GroupAddressBookPojo>> getGroupAddressBookList(@Header("key") String key);

    @POST("AddressBook/Group/Create")
    Call<ResultPojo> saveGroupAddressBook(@Header("key") String key,
                                          @Body GroupAddressBookPojo groupAddressBookPojo);

    @POST("AddressBook/Group/Delete")
    Call<ResultPojo> deleteGroupAddressBook(@Header("key") String string,
                                            @Body GroupAddressBookPojo groupAddressBookPojo);

    @GET("AddressBook/Download/Excel")
    Call<ResultPojo> downloadXlsGroupAddressBookList(@Header("key") String key,
                                                     @Header("departmentId") String departmentId);

    @GET("AddressBook/Download/PDF")
    Call<ResultPojo> downloadPdfGroupAddressBookList(@Header("key") String key,
                                                     @Header("departmentId") String departmentId);
}
