package com.example.lvtn_app.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lvtn_app.View.Fragment.MyDashBoardFragment;
import com.example.lvtn_app.View.Fragment.PersonalStactisticFragment;
import com.example.lvtn_app.View.Fragment.ScheduleFragment;

public class MyTasksAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTab;
    String[] titles = new String[]
            {
                    "My Dashboard",
                    "Schedule",
                    "Statistic"
            };
    public MyTasksAdapter(@NonNull FragmentManager fm, Context context, int totalTab) {
        super(fm);
        this.context = context;
        this.totalTab = totalTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MyDashBoardFragment myDashBoardFragment = new MyDashBoardFragment();
                return myDashBoardFragment;
            case 1:
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                return scheduleFragment;
            case 2:
                PersonalStactisticFragment personalStactisticFragment = new PersonalStactisticFragment();
                return personalStactisticFragment;
            default:
                return new MyDashBoardFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return totalTab;
    }

}