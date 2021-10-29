package com.example.lvtn_app.Controller.Retrofit;

import com.example.lvtn_app.View.NotificationMessage.MyResponse;
import com.example.lvtn_app.View.NotificationMessage.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServiceFirebase {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA3NOxF1A:APA91bF_r7tUKlooxhrbMfobZF60O5nVfR6w6QifYcHqHYWI-FdIMmeNf_R2_jc_6wAz3qhey6uI3HD1JdIXQ4Kg7L1Kh7MZ3JhyEdupBPaWkmfCt6rJdE1o2uB0GhgvoIq9z6oVoexE"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
