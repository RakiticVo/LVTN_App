package com.example.lvtn_app.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.LoginAdapter;
import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.DashBoardFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //Khai báo
    TabLayout tabLayout;
    ViewPager viewPager;
    public ArrayList<User> user_list = new ArrayList<>();
    float v=0;

    static LoginActivity instance;

    public static LoginActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Set up
        tabLayout = findViewById(R.id.acc_tabLayout);
        viewPager = findViewById(R.id.acc_viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabTextColors(Color.BLACK, Color.BLACK);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(loginAdapter);

        instance = this;

        ApiService service = ApiUtils.connectRetrofit();
        service.getUser().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//                Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                ArrayList<User> temp = response.body();
                if (temp != null){
                    user_list.addAll(temp);
                }
                for (User user : user_list){
//                    Toast.makeText(LoginActivity.this, "" + user.isStatus(), Toast.LENGTH_SHORT).show();
//                    if (user.isStatus()){
//                        Toast.makeText(LoginActivity.this, "" + user.getId_user()+ "\n"
//                                +user.getUserName()+ "\n"
//                                +user.getUserEmail()+ "\n"
//                                +"status: Online"+ "\n", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(LoginActivity.this, "" + user.getId_user()+ "\n"
//                                +user.getUserName()+ "\n"
//                                +user.getUserEmail()+ "\n"
//                                +"status: Offline"+ "\n", Toast.LENGTH_SHORT).show();
//                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}