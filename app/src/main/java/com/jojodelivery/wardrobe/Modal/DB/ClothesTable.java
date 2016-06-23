package com.jojodelivery.wardrobe.Modal.DB;

/**
 * Created by asus on 24-06-2016.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class ClothesTable {

    // DataBase Table Login
    // Table Names
    public static final String Yatis_Table_Clothes = "Clothes_Data";

    public static final String Clothes_Column_Id = "Id";
    public static final String Clothes_Column_Type = "Type";

    public static final String[] Clothes_Projection = new String[]{
            ClothesTable.Clothes_Column_Id,
            ClothesTable.Clothes_Column_Type };


    /**
     * To-Be Called from SQLiteOpenHelper Class' onCreate Method
     * Creates ClothesTable Data Table In DataBase
     * @param database The DataBase In Which The Table Is To-Be Created
     */
    public static void onCreate(SQLiteDatabase database){
        String query_Create_Clothes = "CREATE TABLE IF NOT EXISTS " +
                Yatis_Table_Clothes + " (" + Clothes_Column_Id +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                Clothes_Column_Type +" text )";
        database.execSQL(query_Create_Clothes);
        Log.d("ClothesTable: ", "WardrobeDB: ClothesTable Data Table Created");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
    }
}
