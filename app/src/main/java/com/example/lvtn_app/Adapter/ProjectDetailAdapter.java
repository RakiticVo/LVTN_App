package com.example.lvtn_app.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lvtn_app.View.Fragment.DashBoardFragment;
import com.example.lvtn_app.View.Fragment.LoginTabFragment;
import com.example.lvtn_app.View.Fragment.MemberFragment;
import com.example.lvtn_app.View.Fragment.SignUpTabFragment;
import com.example.lvtn_app.View.Fragment.StatisticFragment;

public class ProjectDetailAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTab;
    String[] titles = new String[]
            {
                    "Dashboard",
                    "Statistic",
                    "Member"
            };
    public ProjectDetailAdapter(@NonNull FragmentManager fm, Context context, int totalTab) {
        super(fm);
        this.context = context;
        this.totalTab = totalTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                return dashBoardFragment;
            case 1:
                StatisticFragment statisticFragment = new StatisticFragment();
                return  statisticFragment;
            case 2:
                MemberFragment memberFragment = new MemberFragment();
                return  memberFragment;
            default:
                return new DashBoardFragment();
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
