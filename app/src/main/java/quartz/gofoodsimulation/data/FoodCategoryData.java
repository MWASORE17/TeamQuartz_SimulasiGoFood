package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import quartz.gofoodsimulation.models.FoodCategoryModel;

/**
 * Created by meikelwis on 14/05/17.
 * Modified Remark:
 * sxio - 14-May-17: add open to create
 * meikelwis 15/05/17: implementasi base columns dan function cursor to model dihilangkan pada model
 * menghilangkan proses ambil dan simpan data tidak menggunakan object lagi
 * sxio - 15-May-17: melepas implementasi base columns krn tidak cocok, +function cursor to model
 */

public class FoodCategoryData {
    public static final String TABLE_NAME = "FOODCATEGORY";
    public static final String COLUMN_CODE_CATEGORY = "codeCategory";
    public static final String COLUMN_SELLER_ID = "idSeller";
    public static final String COLUMN_NAME_CATEGORY = "nameCategory";
    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_CODE_CATEGORY + " TEXT PRIMARY KEY, " +
            COLUMN_SELLER_ID + " INTEGER, " +
            COLUMN_NAME_CATEGORY + " TEXT);";

    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    public FoodCategoryData(Context context) {
        dbHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    public static FoodCategoryModel cursorToFoodCategoryModel(Cursor cursor) {
        FoodCategoryModel cFCM = new FoodCategoryModel();
        cFCM.setCodeCategory(cursor.getString(0));
        cFCM.setIdSeller(cursor.getInt(1));
        cFCM.setNameCategory(cursor.getString(2));
        return cFCM;
    }

    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * TODO: Creating Food Category for each Seller and insert it to DB
     *
     * @param codeCategory for making the food category code like "martabak" code is for "Martabak" category
     * @param idSeller     for init the food category to the right seller
     * @param nameCategory for the name of the category
     */
    public void createFoodCategory(String codeCategory, int idSeller, String nameCategory) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE_CATEGORY, codeCategory);
        values.put(COLUMN_SELLER_ID, idSeller);
        values.put(COLUMN_NAME_CATEGORY, nameCategory);
        long insertId = database.insert(TABLE_NAME, null, values);
    }

    public void updateFoodCategory(String codeCategory, int idSeller, String nameCategory, String args) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE_CATEGORY, codeCategory);
        values.put(COLUMN_SELLER_ID, idSeller);
        values.put(COLUMN_NAME_CATEGORY, nameCategory);
        String selection = COLUMN_CODE_CATEGORY + " LIKE ?";
        String[] selectionArgs = {args};
        int count = database.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public void deleteFoodCategory(String args) {
        String selection = COLUMN_CODE_CATEGORY + " LIKE ?";
        String[] selectionArgs = {args};
        database.delete(TABLE_NAME, selection, selectionArgs);
    }


    /**
     * TODO: Get the Seller Food Category
     *
     * @param idSeller to get the right food category from the right seller
     * @return ArrayList of FoodCategoryModel that contained the list of the Seller Food Category
     */
    public ArrayList<FoodCategoryModel> getSellerFoodCategory(String idSeller) {
        this.open();
        String selection = COLUMN_SELLER_ID + "=?";
        String[] selectionArgs = {idSeller};

        Cursor cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        ArrayList<FoodCategoryModel> fcd = new ArrayList<>();
        while (cursor.moveToNext()) {
            fcd.add(this.cursorToFoodCategoryModel(cursor));
        }
        cursor.close();
        return fcd;
    }

    /**
     * TODO: Get all of the Food Category in DB
     *
     * @return ArrayList of FoodCategoryModel that contain the list of all food category
     */
    public ArrayList<FoodCategoryModel> getAllFoodCategory() {
        this.open();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<FoodCategoryModel> fcd = new ArrayList<>();
        while (cursor.moveToNext()) {
            fcd.add(this.cursorToFoodCategoryModel(cursor));
        }
        cursor.close();
        return fcd;
    }
}
