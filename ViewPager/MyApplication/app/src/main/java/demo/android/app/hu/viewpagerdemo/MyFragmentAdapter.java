package demo.android.app.hu.viewpagerdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Peter on 2014.11.06..
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FragmentOne();
            case 1:
                return new FragmentTwo();
            default:
                return new FragmentOne();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "FragmentOne";
            case 1:
                return "FragmentTwo";
            default:
                return "FragmentOne";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
