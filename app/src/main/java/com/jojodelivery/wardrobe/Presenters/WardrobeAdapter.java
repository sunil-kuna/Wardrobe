package com.jojodelivery.wardrobe.Presenters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.jojodelivery.wardrobe.DataDefitions.Cloth;

import java.util.ArrayList;

/**
 * Created by asus on 24-06-2016.
 */
class WardrobeAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = WardrobeAdapter.class.getSimpleName() ;
    ArrayList<Cloth> data;
    public WardrobeAdapter(FragmentManager fragmentManager,ArrayList<Cloth> data) {
        super(fragmentManager);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "position = "+position+" data = "+data.toString());
        return ImageFragment.init(data.get(position).getId());
    }

    // This is called when notifyDataSetChanged() is called
    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }
}

