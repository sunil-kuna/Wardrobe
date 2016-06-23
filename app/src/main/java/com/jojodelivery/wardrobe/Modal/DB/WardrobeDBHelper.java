package com.jojodelivery.wardrobe.Modal.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 16/4/15.
 * Class Manages DataBase & Table Creation
 * Extends SQLiteOpenHelper
 */
public class WardrobeDBHelper extends SQLiteOpenHelper {

    // DataBase Name & Version
    private static final String DataBase_Name = "WardrobeAndroDB.db";
    public static final int DataBase_Version = 4 ;

    // Default Constructor
    public WardrobeDBHelper(Context context){
        super(context, DataBase_Name, null, DataBase_Version);
    }

    // Method Called During DataBase Creation
    @Override
    public void onCreate(SQLiteDatabase database){
        ClothesTable.onCreate(database);
        FavouritesTable.onCreate(database);
    }

    // Method Called During Update Of Database
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        ClothesTable.onUpgrade(database, oldVersion, newVersion);
        FavouritesTable.onUpgrade(database, oldVersion, newVersion);
    }

}
