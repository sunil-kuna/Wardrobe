package com.jojodelivery.wardrobe.Modal;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jojodelivery.wardrobe.DataDefitions.Cloth;
import com.jojodelivery.wardrobe.DataDefitions.Constants;
import com.jojodelivery.wardrobe.Modal.DB.ClothesTable;
import com.jojodelivery.wardrobe.Modal.DB.WardrobeDBHelper;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by asus on 24-06-2016.
 */
public class WardrobeContentProvider extends android.content.ContentProvider {

    /**
     * Default Constructor
     */
    public WardrobeContentProvider(){
        // Auto-Generated Method Stub
    }

    // DataBase
    private WardrobeDBHelper database;

    // For Uri Matcher
    // Id Needed While Matching Uri's
    private static final int STORE_IMAGE = 1;

    // Authority Value And Base_Data_Type
    public static final String AUTHORITY = "com.jojodelivery.wardrobe.database";
    private static final String BASE_PATH = "Base_Path";
    private static final String BASE_PATH_STORE_IMAGE = "Store_Image";

    // Content_Uri For Tables
    // Create Different Table Specific Content_Uris To Support Multiple Tables
    public static final Uri CONTENT_URI_BASE_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    public static final Uri CONTENT_URI_STORE_IMAGE = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_STORE_IMAGE);
//    private static final String CONTENT_TYPE_GARAGEDATA = "/garage_data";

    // Add Table Specific Content_Uris To UriMatcher
    private static final UriMatcher CUS_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        CUS_URI_MATCHER.addURI(AUTHORITY, BASE_PATH_STORE_IMAGE, STORE_IMAGE);
    }

    @Override
    public synchronized boolean onCreate(){
        database = new WardrobeDBHelper(getContext());
        return false;
    }

    public synchronized Cursor query(Uri uri, String[] projection, String selection,
                                     String[] selection_args, String groupBy){

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        /*switch (CUS_URI_MATCHER.match(uri)){
            case GARAGE_DATA:
                // Set List of Tables To Work With
                queryBuilder.setTables(GarageDataTable.Garage_Data_Table);
                break;

            default:
                throw new IllegalArgumentException("YatisDataCollectionProvider: Unknown Uri " + uri);
        }
*/
        // Get Writable Instance Of Database
        SQLiteDatabase db = database.getReadableDatabase();
        // Get Cursor To Read From DataBase
        Cursor cursor = queryBuilder.query(db, projection, selection, selection_args, groupBy, null, null);

        // Check If All Potential Listeners Are Getting Notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }



    @Override
    public synchronized String getType(Uri uri){
        return null;
    }

    @Override
    public synchronized Uri insert(Uri uri, ContentValues values){
        SQLiteDatabase sqDB = database.getWritableDatabase();
        long id = 0;
        Log.i("ContentProvider", values.toString());
        /*switch (CUS_URI_MATCHER.match(uri)){
            case GARAGE_DATA:
                values.put(GarageDataTable.GarageData_Column_GarageMarkUploaded,0);
                id = sqDB.insert(GarageDataTable.Garage_Data_Table, null, values);
                break;

            default:
                throw new IllegalArgumentException("YatisDataCollectionProvider: Unkown Uri " + uri);
        }*/

        getContext().getContentResolver().notifyChange(uri, null);
        return null;//Uri.parse(BASE_PATH_GARAGEDATA + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionargs) {
        SQLiteDatabase sqDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
       /* switch (CUS_URI_MATCHER.match(uri)){
            case GARAGE_DATA:
                rowsUpdated = sqDB.delete(GarageDataTable.Garage_Data_Table,  selection, selectionargs);
                break;

            default:
                throw new IllegalArgumentException("YatisDataCollectionProvider: Unknown Uri " + uri);
        }*/

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection, String[] selectionargs){
        SQLiteDatabase sqDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        /*switch (CUS_URI_MATCHER.match(uri)){
            case GARAGE_DATA:
                rowsUpdated = sqDB.update(GarageDataTable.Garage_Data_Table, values, selection, selectionargs);
                break;
            default:
                throw new IllegalArgumentException("YatisDataCollectionProvider: Unknown Uri " + uri);
        }*/

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
    public synchronized int bulkInsert(Uri uri, ContentValues[] values){
        SQLiteDatabase sqDB = database.getWritableDatabase();
        int id=0;
        /*switch (CUS_URI_MATCHER.match(uri)){
            case GARAGE_PHOTOS_DATA:
                for(int i =0;i<values.length;i++)
                {
                    values[i].put(GaragePhotosDataTable.GaragePhotoData_Column_GaragePhotoUploaded,0);
                    sqDB.insert(GaragePhotosDataTable.Garage_Photos_Data_Table,null, values[i]);
                }
                break;

            default:
                throw new IllegalArgumentException("YatisDataCollectionProvider: Unkown Uri " + uri);
        }*/

        getContext().getContentResolver().notifyChange(uri, null);
        return values.length;
    }

    @Nullable
    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        switch (method)
        {
            case Constants.INSERT_IMAGE:
                return insertImage(extras);
        }
        return super.call(method, arg, extras);
    }

    private Bundle insertImage(Bundle extras) {

        Bundle bundle = new Bundle();
        try {
            SQLiteDatabase sqDB = database.getWritableDatabase();
            Cloth cloth = extras.getParcelable(Constants.CLOTH);
            ContentValues values = new ContentValues();
            values.put(ClothesTable.Clothes_Column_Type,cloth.getType());
            long rowId = sqDB.insert(ClothesTable.Yatis_Table_Clothes, null, values);
            String root = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
            File file = new File(root + "/" + rowId + ".jpg");
            if (file.exists ()) file.delete ();
            FileOutputStream out = new FileOutputStream(file);
            cloth.getImage().compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            bundle.putString(Constants.RESULT,Constants.RESULT_OK);
        }
        catch (Exception e) {
            bundle.putString(Constants.RESULT,Constants.RESULT_FAIL);
            e.printStackTrace();
        }
        return  bundle;
    }


}
