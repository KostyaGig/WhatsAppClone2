package com.kostya_zinoviev.whatsap0p;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapterViewPager extends FragmentPagerAdapter {


    public FragmentAdapterViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentChats();
            case 1:
                return new FragmentGroupsChats();
            case 2:
                return new FragmentContacts();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    //Для TabLayout название итемов
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Чаты";
            case 1:
                return "Группы";
            case 2:
                return "Контакты";
            default: return null;
        }
    }
}
