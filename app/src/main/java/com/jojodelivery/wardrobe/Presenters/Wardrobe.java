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
import android.widget.ImageView;
import android.widget.Toast;

import com.jojodelivery.wardrobe.DataDefitions.Cloth;
import com.jojodelivery.wardrobe.DataDefitions.Constants;
import com.jojodelivery.wardrobe.DataDefitions.Favourite;
import com.jojodelivery.wardrobe.Modal.WardrobeLoader;
import com.jojodelivery.wardrobe.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Wardrobe extends AppCompatActivity implements View.OnClickListener,LoaderManager.LoaderCallbacks<Bundle>,ViewPager.OnPageChangeListener {

    private static final int LOADER_ID_GET_SHIRTS_TROUSERS = 0;
    private static final int LOADER_ID_GET_FAVOURITES = 1;
    private static final int LOADER_ID_SET_FAVOURITE = 2;

    private static final String TAG = Wardrobe.class.getSimpleName() ;
    ViewPager shirtsViewPager,trousersViewPager;
    WardrobeAdapter shirtsAdapter,trousersAdapter;
    ArrayList<Cloth> shirtsList,trousersList;
    HashSet<String> favouritesSet;
    ImageView favouriteImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wardrobe);
        shirtsViewPager = (ViewPager)findViewById(R.id.shirt);
        trousersViewPager = (ViewPager)findViewById(R.id.trousers);
        favouriteImageView = (ImageView)findViewById(R.id.favourite);
        favouriteImageView.setOnClickListener(this);

        findViewById(R.id.shuffle).setOnClickListener(this);
        findViewById(R.id.add_shirt).setOnClickListener(this);
        findViewById(R.id.add_trousers).setOnClickListener(this);



        if(savedInstanceState == null) {
            shirtsList = new ArrayList<>();
            trousersList = new ArrayList<>();
            favouritesSet = new HashSet<>();
            getLoaderManager().restartLoader(LOADER_ID_GET_SHIRTS_TROUSERS, null, Wardrobe.this).forceLoad();
            getLoaderManager().restartLoader(LOADER_ID_GET_FAVOURITES, null, Wardrobe.this).forceLoad();
        }
        else {
            Log.i(TAG, savedInstanceState.toString());

            shirtsList = savedInstanceState.getParcelableArrayList(Constants.SHIRTS_LIST);
            trousersList = savedInstanceState.getParcelableArrayList(Constants.TROUSERS_LIST);
            favouritesSet = new HashSet<>(savedInstanceState.getStringArrayList(Constants.FAVOURITES_LIST));
        }

        shirtsAdapter = new WardrobeAdapter(getSupportFragmentManager(),shirtsList);
        trousersAdapter = new WardrobeAdapter(getSupportFragmentManager(),trousersList);

        shirtsViewPager.setAdapter(shirtsAdapter);
        trousersViewPager.setAdapter(trousersAdapter);

        shirtsViewPager.setOnPageChangeListener(this);
        trousersViewPager.setOnPageChangeListener(this);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"onCreateLoader");
        if(id == LOADER_ID_GET_SHIRTS_TROUSERS)
            return new WardrobeLoader(this, Constants.GET_SHIRTS_TROUSERS,args);
        if(id == LOADER_ID_GET_FAVOURITES)
            return new WardrobeLoader(this, Constants.GET_FAVOURITES,args);
        if(id == LOADER_ID_SET_FAVOURITE)
            return new WardrobeLoader(this, Constants.SET_FAVOURITE,args);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle data) {

        Log.i(TAG,"onLoadFinished"+data.toString());
        if(loader.getId() == LOADER_ID_GET_SHIRTS_TROUSERS) {

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
        }else if(loader.getId() == LOADER_ID_GET_FAVOURITES ||
                loader.getId() == LOADER_ID_SET_FAVOURITE){

            if (data.getString(Constants.RESULT).equals(Constants.RESULT_OK)) {
                favouritesSet.clear();
                favouritesSet.addAll(data.getStringArrayList(Constants.FAVOURITES));
            }
            else {
                Toast.makeText(this,"Unable to retrieve Data",Toast.LENGTH_SHORT).show();
            }
            if(shirtsList.size()>0 && trousersList.size()>0)
                setFavorite();
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
                long seed = System.nanoTime();
                Collections.shuffle(shirtsList, new Random(seed));
                seed = System.nanoTime();
                Collections.shuffle(trousersList, new Random(seed));
                shirtsAdapter.notifyDataSetChanged();
                trousersAdapter.notifyDataSetChanged();
                break;
            case R.id.favourite:
                Log.i(TAG,"Favourite Clicked");
                if(shirtsList.size()>0 && trousersList.size()>0) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.IS_FAVOURITE, isFavourite());
                    bundle.putParcelable(Constants.FAVOURITE_ITEM, getFavouriteItem());
                    getLoaderManager().restartLoader(LOADER_ID_SET_FAVOURITE, bundle, Wardrobe.this).forceLoad();
                }else {
                    if(trousersList.size()==0)
                        Toast.makeText(this," Add Trousers",Toast.LENGTH_LONG).show();
                    if(shirtsList.size()==0)
                        Toast.makeText(this," Add Shirts",Toast.LENGTH_LONG).show();
                }
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

    private boolean isFavourite() {
        if(shirtsList.size()>0 && trousersList.size()>0) {
            String combo = shirtsList.get(shirtsViewPager.getCurrentItem()).getId() + "_" +
                    trousersList.get(trousersViewPager.getCurrentItem()).getId();
            return favouritesSet.contains(combo);
        }
        return  false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLoaderManager().restartLoader(LOADER_ID_GET_SHIRTS_TROUSERS, null, Wardrobe.this).forceLoad();
        getLoaderManager().restartLoader(LOADER_ID_GET_FAVOURITES, null, Wardrobe.this).forceLoad();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*outState.putParcelable(Constants.SHIRTS_ADAPTER,shirtsAdapter.saveState());
        outState.putParcelable(Constants.TROUSERS_ADAPTER,trousersAdapter.saveState());
        */
        outState.putParcelableArrayList(Constants.SHIRTS_LIST, shirtsList);
        outState.putParcelableArrayList(Constants.TROUSERS_LIST, trousersList);
        outState.putStringArrayList(Constants.FAVOURITES_LIST, new ArrayList(favouritesSet));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setFavorite();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    void setFavorite()
    {
        String combo = shirtsList.get(shirtsViewPager.getCurrentItem()).getId()+"_"+
                trousersList.get(trousersViewPager.getCurrentItem()).getId();
        if(favouritesSet.contains(combo))
            favouriteImageView.setImageResource(R.mipmap.favouriteon);
        else
            favouriteImageView.setImageResource(R.mipmap.favourite);
    }

    public Favourite getFavouriteItem() {
        Favourite favouriteItem = new Favourite();
        favouriteItem.setShirtId(shirtsList.get(shirtsViewPager.getCurrentItem()).getId());
        favouriteItem.setTrousersId(trousersList.get(trousersViewPager.getCurrentItem()).getId());
        return favouriteItem;
    }
}
