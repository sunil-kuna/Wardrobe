package com.jojodelivery.wardrobe.Modal.DB;

/**
 * Created by asus on 24-06-2016.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavouritesTable {

    // DataBase Table Login
    // Table Names
    public static final String Yatis_Table_Favourites = "Favourites_Data";

    public static final String Favourites_Column_Type1 = "Type1";
    public static final String Favourites_Column_Type2 = "Type2";

    public static final String[] Favourites_Projection = new String[]{
            FavouritesTable.Favourites_Column_Type1,
            FavouritesTable.Favourites_Column_Type2 };


    /**
     * To-Be Called from SQLiteOpenHelper Class' onCreate Method
     * Creates FavouritesTable Data Table In DataBase
     * @param database The DataBase In Which The Table Is To-Be Created
     */
    public static void onCreate(SQLiteDatabase database){
        String query_Create_Favourites = "CREATE TABLE IF NOT EXISTS " +
                Yatis_Table_Favourites + " (" + Favourites_Column_Type1+" text, " +
                Favourites_Column_Type2 +" text )";
        database.execSQL(query_Create_Favourites);
        Log.d("FavouritesTable: ", "WardrobeDB: FavouritesTable Data Table Created");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
    }
}
