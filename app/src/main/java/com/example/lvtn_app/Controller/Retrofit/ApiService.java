package com.example.lvtn_app.Controller.Retrofit;

import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    //Todo: Upload User Image
    @Multipart
    @POST("uploadImage.php")
    Call<String> isUploadUserImageSuccess(@Part MultipartBody.Part image);

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
                                     @Field("userEmail") String userEmail);

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

    //Todo: Create New User for Project
    @FormUrlEncoded
    @POST("insertNewUserForProject.php")
    Call<String> isCreateNewUserForProjectSuccess(@Field("id_project") int id_project,
                                                  @Field("id_user") int id_user,
                                                  @Field("position") String position);

    //Todo: Get Project List
    @GET("getProject.php")
    Call<ArrayList<Project>> getProject();

    //Todo: Get Project List by User
    @FormUrlEncoded
    @POST("getProjectListByUser.php")
    Call<ArrayList<Project>> getProjectListByUser(@Field("id_user") int id_user);

    //Todo: Get User List by Project
    @FormUrlEncoded
    @POST("getUserListByProject.php")
    Call<ArrayList<User>> getUserListByProject(@Field("id_project") int id_project);

    //Todo: Update User Pass
    @FormUrlEncoded
    @POST("updateUserPass.php")
    Call<String> isUpdateUserPassSuccess(@Field("id_user") int id_user,
                                         @Field("userPass") String userPass);

    //Todo: Create New Group Chat
    @FormUrlEncoded
    @POST("insertNewGroupChat.php")
    Call<String> isCreateNewGroupChatSuccess(@Field("groupName") String groupName,
                                             @Field("groupImage") String groupImage,
                                             @Field("groupCreator") String groupCreator,
                                             @Field("groupLastMess") String groupLastMess,
                                             @Field("groupLastSender") String groupLastSender);

    //Todo: Get Group Chat List
    @GET("getGroupChat.php")
    Call<ArrayList<GroupChat>> getGroupChat();

    //Todo: Create New User for Group Chat
    @FormUrlEncoded
    @POST("insertNewUserForGroupChat.php")
    Call<String> isCreateNewUserForGroupChatSuccess(@Field("id_user") int id_user,
                                                    @Field("id_Group") int id_Group,
                                                    @Field("position") String position);

    //Todo: Get Group Chat List by User
    @FormUrlEncoded
    @POST("getGroupChatListByUser.php")
    Call<ArrayList<GroupChat>> getGroupChatListByUser(@Field("id_user") int id_user);

    //Todo: Create New Issue
    @FormUrlEncoded
    @POST("insertNewIssue.php")
    Call<String> isCreateNewIssueSuccess(@Field("issueName") String issueName,
                                         @Field("issueProjectType") String issueProjectType,
                                         @Field("issueDecription") String issueDecription,
                                         @Field("issueType") String issueType,
                                         @Field("issueStartDate") String issueStartDate,
                                         @Field("issuePriority") String issuePriority,
                                         @Field("issueAssignee") String issueAssignee,
                                         @Field("issueEstimateTime") String issueEstimateTime,
                                         @Field("issueCreator") String issueCreator,
                                         @Field("issueProjectID") int issueProjectID,
                                         @Field("issueFinishDate") String issueFinishDate);

    //Todo: Create New Task
    @FormUrlEncoded
    @POST("insertNewTask.php")
    Call<String> isCreateNewTaskSuccess(@Field("issueName") String issueName,
                                        @Field("issueDecription") String issueDecription,
                                        @Field("issueStartDate") String issueStartDate,
                                        @Field("issueCreator") String issueCreator);

    //Todo: Get Issue List by Project
    @FormUrlEncoded
    @POST("getIssueListbyProject.php")
    Call<ArrayList<Issue>> getIssueListbyProject(@Field("issueProjectID") int issueProjectID);

    //Todo: Get Task List by User
    @FormUrlEncoded
    @POST("getTaskListbyUser.php")
    Call<ArrayList<Issue>> getTaskListbyUser(@Field("issueCreator") String issueCreator);

    //Todo: Create New Message
    @FormUrlEncoded
    @POST("insertNewMessage.php")
    Call<String> isCreateNewMessageSuccess(@Field("id_Group") int id_Group,
                                           @Field("sender") String sender,
                                           @Field("img_sender") String img_sender,
                                           @Field("message") String message,
                                           @Field("send_time") String send_time,
                                           @Field("send_date") String send_date);

    //Todo: Get User List by Group Chat
    @FormUrlEncoded
    @POST("getUserListByGroupChat.php")
    Call<ArrayList<User>> getUserListByGroupChat(@Field("id_Group") int id_Group);

    //Todo: Update Last Message
    @FormUrlEncoded
    @POST("updateLastMess.php")
    Call<String> isUpdateLastMessSuccess(@Field("id_Group") int id_Group,
                                         @Field("groupLastMess") String groupLastMess,
                                         @Field("groupLastSender") String groupLastSender);

}
