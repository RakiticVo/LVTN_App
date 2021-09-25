package com.example.lvtn_app.Controller.Retrofit;

public class ApiUtils {
    //baseUrl is the website address where store database
    public static final String baseUrl = "http://192.168.1.4/paffer/";

    public static ApiService connectRetrofit(){
        return RetrofitClient.getClient(baseUrl).create(ApiService.class);
    }
}
