package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import quartz.gofoodsimulation.models.FoodCategoryModel;
import quartz.gofoodsimulation.models.FoodModel;

/**
 * Created by meikelwis on 14/05/17.
 * Modified Remark:
 * sxio - 14-May-17: add open to create
 * meikelwis 15/08/17: implementasi base columns dan function cursor to model dihilangkan pada model
 * menghilangkan proses ambil dan simpan data tidak menggunakan object lagi
 * sxio - 16-May-17: melepas implementasi base columns krn tidak cocok, +function cursor to model
 */

public class FoodData {
    private static final String TABLE_NAME = "FOOD";
    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String COLUMN_FOOD_ID = "idFood";
    private static final String COLUMN_CODE_CATEGORY = "codeCategory";
    private static final String COLUMN_NAME_FOOD = "nameFood";
    private static final String COLUMN_PRICE_FOOD = "priceFood";
    private static final String COLUMN_PHOTO_FOOD = "photoFood";
    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_FOOD_ID + " TEXT PRIMARY KEY, " +
            COLUMN_CODE_CATEGORY + " TEXT, " +
            COLUMN_NAME_FOOD + " TEXT, " +
            COLUMN_PRICE_FOOD + " INTEGER, " +
            COLUMN_PHOTO_FOOD + " TEXT);";
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    public FoodData(Context context) {
        dbHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void deleteAllRecord() {
        this.open();
        database.delete(TABLE_NAME, "1=1", null);
    }

    /**
     * TODO: Create item Food and insert it to DB
     *
     * @param idFood       to differentiate one item food to another
     * @param codeCategory to give the food the right category
     * @param nameFood     for initiate the name of the food and insert to DB
     * @param priceFood    to give the food a price
     * @param photoFood    for the photo of the food
     */
    public void createFood(String idFood, String codeCategory,
                           String nameFood, int priceFood, String photoFood) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_ID, idFood);
        values.put(COLUMN_CODE_CATEGORY, codeCategory);
        values.put(COLUMN_NAME_FOOD, nameFood);
        values.put(COLUMN_PRICE_FOOD, priceFood);
        values.put(COLUMN_PHOTO_FOOD, photoFood);
        long insertId = database.insert(TABLE_NAME, null, values);
    }

    public void updateFood(String idFood, int idSeller, String codeCategory,
                           String nameFood, int priceFood,
                           String photoFood, String args) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_ID, idFood);
        values.put(COLUMN_CODE_CATEGORY, codeCategory);
        values.put(COLUMN_NAME_FOOD, nameFood);
        values.put(COLUMN_PRICE_FOOD, priceFood);
        values.put(COLUMN_PHOTO_FOOD, photoFood);
        String selection = COLUMN_FOOD_ID + " LIKE ?";
        String[] selectionArgs = {args};
        int count = database.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public void deleteFood(String args) {
        this.open();
        String selection = COLUMN_FOOD_ID + " LIKE ?";
        String[] selectionArgs = {args};
        database.delete(TABLE_NAME, selection, selectionArgs);
    }

    public ArrayList<FoodCategoryModel> getFoodCategoryMenu(String idSeller) {
        this.open();
        String[] selectionArgs = {idSeller};
        String query = "SELECT FOODCATEGORY.* " +
                "FROM FOODCATEGORY JOIN FOOD ON " +
                "FOODCATEGORY.codeCategory = FOOD.codeCategory " +
                "WHERE FOOD.idSeller = ? " +
                "GROUP BY FOOD.codeCategory " +
                "ORDER BY FOODCATEGORY.id";

        Cursor cursor = database.rawQuery(query, selectionArgs);

        ArrayList<FoodCategoryModel> fcm = new ArrayList<>();
        while (cursor.moveToNext()) {
            fcm.add(FoodCategoryData.cursorToFoodCategoryModel(cursor));
        }
        cursor.close();
        return fcm;
    }

    /**
     * TODO: to get the list of food of a Seller with the specified id and codeCategory
     *
     * @param args contain the arguments for query condition
     * @return ArrayList of FoodModel contains the list of the food
     */
    public ArrayList<FoodModel> getFoodByIdSeller(String[] args) {
        this.open();
        String query = "SELECT FOOD.* FROM FOOD JOIN FOODCATEGORY ON " +
                "FOOD.codeCategory = FOODCATEGORY.codeCategory " +
                "WHERE FOODCATEGORY.idSeller = ? AND FOOD.codeCategory = ?" +
                "ORDER BY FOODCATEGORY.codeCategory";

        String[] selectionArgs = args;

        Cursor cursor = database.rawQuery(query, selectionArgs);
        ArrayList<FoodModel> FM = new ArrayList<>();
        while (cursor.moveToNext()) {
            FM.add(this.cursorToFoodModel(cursor));
        }
        cursor.close();
        return FM;
    }

    private FoodModel cursorToFoodModel(Cursor cursor) {
        FoodModel cFM = new FoodModel();
        cFM.setIdFood(cursor.getString(0));
        cFM.setCodeCategory(cursor.getString(1));
        cFM.setNameFood(cursor.getString(2));
        cFM.setPriceFood(cursor.getInt(3));
        cFM.setPhotoFood(cursor.getString(4));
        return cFM;
    }
}
