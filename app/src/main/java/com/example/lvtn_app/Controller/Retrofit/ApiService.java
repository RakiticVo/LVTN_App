package com.example.lvtn_app.Controller.Retrofit;

import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    //Baseurl là: http://192.168.1.3/paffer/

    //Todo: Get User
    @GET("getUser.php") // phần còn lại của để ghép thành một baseUrl
    // để tạo ra một domain hoàn chỉnh
    Call<ArrayList<User>> getUser();

    //Todo: Update User Status
    @FormUrlEncoded
    @POST("updateUserStatus.php")
    Call<String> isUpdateUserInformationSuccess(@Field("id_user") int id_user,
                                                @Field("status") boolean status);

    //Todo: Create New User
    @FormUrlEncoded
    @POST("insertNewUser.php")
    Call<String> isCreateNewUserSuccess(@Field("userName") String userName,
                                        @Field("userEmail") String userEmail,
                                        @Field("userPass") String userPass);

    //Todo: Insert New User Information
    @FormUrlEncoded
    @POST("insertNewUserInformation.php")
    Call<String> isInsertNewUserInformationSuccess(@Field("id_user") int id_user);

    //Todo: Get User Information
    @GET("getUserInformation.php")
    Call<ArrayList<User>> getUserInformation();

    //Todo: Update User Info
    @FormUrlEncoded
    @POST("updateUserInformation.php")
    Call<String> isUpdateUserInformationSuccess(@Field("id_user") int id_user,
                                                @Field("gender_PI") String gender_PI,
                                                @Field("phone_PI") String phone_PI,
                                                @Field("dob_PI") String dob_PI,
                                                @Field("address_PI") String address_PI,
                                                @Field("avatar_PI") String avatar_PI);

    //Todo: Update User
    @FormUrlEncoded
    @POST("updateUser.php")
    Call<String> isUpdateUserSuccess(@Field("id_user") int id_user,
                                     @Field("userName") String userName,
                                     @Field("userName") String userEmail);

    //Todo: Create New Project
    @FormUrlEncoded
    @POST("insertNewProject.php")
    Call<String> isCreateNewProjectSuccess(@Field("projectName") String projectName,
                                           @Field("projectDescription") String projectDescription,
                                           @Field("projectFinishDate") String projectFinishDate,
                                           @Field("projectType") String projectType,
                                           @Field("projectDateCreate") String projectDateCreate,
                                           @Field("projectLeader") String projectLeader,
                                           @Field("projectBackground") int projectBackground);

    //Todo: Get Project
    @GET("getProject.php")
    Call<ArrayList<Project>> getProject();
}
