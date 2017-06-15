package quartz.gofoodsimulation.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Wisz on 4/1/17.
 * Modified by: sxio
 * Modified Remark:
 * 03-Apr-17:
 * Move table USER to UserData.java
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "gfs.db";
    public static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public DataBaseHelper(Context context, String database_name, CursorFactory factory, int version) {
        super(context, database_name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MasterSequenceData.DATABASE_CREATE);
        db.execSQL(FoodCategoryData.DATABASE_CREATE);
        db.execSQL(FoodGroupData.DATABASE_CREATE);
        db.execSQL(FoodData.DATABASE_CREATE);
        db.execSQL(UserData.DATABASE_CREATE);
        db.execSQL(SellerData.DATABASE_CREATE);
        db.execSQL(TransactionHeaderData.DATABASE_CREATE);
        db.execSQL(TransactionDetailData.DATABASE_CREATE);
        db.execSQL(DriverData.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getName(), "Dengan mengupgrade database dari versi " + oldVersion + " ke " +
                newVersion + ", akan menghilangkan semua data yang telah disimpan");
        database.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
        onCreate(database);
    }

    public void deleteTable() {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        db.execSQL(MasterSequenceData.DATABASE_DELETE);
        db.execSQL(FoodCategoryData.DATABASE_DELETE);
        db.execSQL(FoodGroupData.DATABASE_DELETE);
        db.execSQL(FoodData.DATABASE_DELETE);
        db.execSQL(UserData.DATABASE_DELETE);
        db.execSQL(SellerData.DATABASE_DELETE);
        db.execSQL(TransactionHeaderData.DATABASE_DELETE);
        db.execSQL(TransactionDetailData.DATABASE_DELETE);
        db.execSQL(DriverData.DATABASE_DELETE);

        this.onCreate(db);
    }
}
