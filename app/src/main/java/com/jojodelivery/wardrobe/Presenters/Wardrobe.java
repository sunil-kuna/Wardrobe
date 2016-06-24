package com.jojodelivery.wardrobe.Presenters;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jojodelivery.wardrobe.DataDefitions.Cloth;
import com.jojodelivery.wardrobe.DataDefitions.Constants;
import com.jojodelivery.wardrobe.Modal.WardrobeLoader;
import com.jojodelivery.wardrobe.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class Wardrobe extends AppCompatActivity implements View.OnClickListener,LoaderManager.LoaderCallbacks<Bundle> {

    private static final int LOADER_ID_GET_SHIRTS_TROUSERS = 0;
    private static final String TAG = Wardrobe.class.getSimpleName() ;
    ViewPager shirtsViewPager,trousersViewPager;
    WardrobeAdapter shirtsAdapter,trousersAdapter;
    ArrayList<Cloth> shirtsList,trousersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wardrobe);
        shirtsViewPager = (ViewPager)findViewById(R.id.shirt);
        trousersViewPager = (ViewPager)findViewById(R.id.trousers);


        findViewById(R.id.shuffle).setOnClickListener(this);
        findViewById(R.id.favourite).setOnClickListener(this);
        findViewById(R.id.add_shirt).setOnClickListener(this);
        findViewById(R.id.add_trousers).setOnClickListener(this);



        if(savedInstanceState == null) {
            shirtsList = new ArrayList<>();
            trousersList = new ArrayList<>();
            getLoaderManager().initLoader(LOADER_ID_GET_SHIRTS_TROUSERS, null, Wardrobe.this).forceLoad();
        }
        else {
            Log.i(TAG, savedInstanceState.toString());
            /*shirtsAdapter.restoreState(savedInstanceState.getParcelable(Constants.SHIRTS_ADAPTER), Parcelable.class.getClassLoader());
            trousersAdapter.restoreState(savedInstanceState.getParcelable(Constants.TROUSERS_ADAPTER),Parcelable.class.getClassLoader());
            */
            shirtsList = savedInstanceState.getParcelableArrayList(Constants.SHIRTS_LIST);
            trousersList = savedInstanceState.getParcelableArrayList(Constants.TROUSERS_LIST);
            /*shirtsViewPager.onRestoreInstanceState(savedInstanceState.getParcelable(Constants.SHIRTS_VIEWPAGER));
            trousersViewPager.onRestoreInstanceState(savedInstanceState.getParcelable(Constants.TROUSERS_VIEWPAGER));*/
        }

        shirtsAdapter = new WardrobeAdapter(getSupportFragmentManager(),shirtsList);
        trousersAdapter = new WardrobeAdapter(getSupportFragmentManager(),trousersList);

        shirtsViewPager.setAdapter(shirtsAdapter);
        trousersViewPager.setAdapter(trousersAdapter);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"onCreateLoader");
        return new WardrobeLoader(this, Constants.GET_SHIRTS_TROUSERS,args);
    }

    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle data) {

        Log.i(TAG,"onLoadFinished"+data.toString());
        if (data.getString(Constants.RESULT).equals(Constants.RESULT_OK)) {
            shirtsList.clear();
            shirtsList.addAll(data.<Cloth>getParcelableArrayList(Constants.SHIRTS));
            trousersList.clear();
            trousersList.addAll(data.<Cloth>getParcelableArrayList(Constants.TROUSERS));

            shirtsAdapter.notifyDataSetChanged();
            trousersAdapter.notifyDataSetChanged();

        }
        else {
            Toast.makeText(this,"Unable to retrieve Data",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Bundle> loader) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.shuffle:
                break;
            case R.id.favourite:
                break;
            case R.id.add_shirt:
                Intent intent = new Intent(this,ImageSelector.class);
                intent.putExtra(Constants.CLOTH_TYPE,Constants.CLOTH_SHIRT);
                startActivityForResult(intent,0);
                break;
            case R.id.add_trousers:
                intent = new Intent(this,ImageSelector.class);
                intent.putExtra(Constants.CLOTH_TYPE,Constants.CLOTH_TROUSERS);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLoaderManager().initLoader(LOADER_ID_GET_SHIRTS_TROUSERS, null, Wardrobe.this).forceLoad();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*outState.putParcelable(Constants.SHIRTS_ADAPTER,shirtsAdapter.saveState());
        outState.putParcelable(Constants.TROUSERS_ADAPTER,trousersAdapter.saveState());
        */
        outState.putParcelableArrayList(Constants.SHIRTS_LIST, shirtsList);
        outState.putParcelableArrayList(Constants.TROUSERS_LIST,trousersList);
        super.onSaveInstanceState(outState);
    }
}
