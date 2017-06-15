package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import quartz.gofoodsimulation.models.FoodGroupModel;

/**
 * Created by meikelwis on 14/05/17.
 * Modified Remark:
 * sxio - 14-May-17: add open to create
 * meikelwis 15/08/17: implementasi base columns dan function cursor to model dihilangkan pada model
 * menghilangkan proses ambil dan simpan data tidak menggunakan object lagi
 */

public class FoodGroupData {
    public static final String TABLE_NAME = "MSTFOODGROUP";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE_GROUP = "codeGroup";
    public static final String COLUMN_NAME_GROUP = "nameGroup";

    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CODE_GROUP + " TEXT, " +
            COLUMN_NAME_GROUP + " TEXT);";
    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    public FoodGroupData(Context context) {
        dbHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    public void createFoodGroup(String codeGroup, String nameGroup) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE_GROUP, codeGroup);
        values.put(COLUMN_NAME_GROUP, nameGroup);
        long insertId = database.insert(TABLE_NAME, null, values);
    }

    /**
     * TODO: retrieve all food group in database
     *
     * @return ArrayList of FoodGroup Model
     */
    public ArrayList<FoodGroupModel> getAllFoodGroup() {
        database = dbHelper.getReadableDatabase();

        String sortOrder = COLUMN_ID + " ASC";
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, sortOrder);
        ArrayList<FoodGroupModel> foodGroupModels = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            foodGroupModels.add(cursorToFoodGroupModel(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return foodGroupModels;
    }

    private FoodGroupModel cursorToFoodGroupModel(Cursor cursor) {
        FoodGroupModel fgm = new FoodGroupModel();
        fgm.setId(cursor.getInt(0));
        fgm.setCodeGroup(cursor.getString(1));
        fgm.setNameGroup(cursor.getString(2));
        return fgm;
    }
}
