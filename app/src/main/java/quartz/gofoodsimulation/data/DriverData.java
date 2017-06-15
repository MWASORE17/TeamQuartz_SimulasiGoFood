package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import quartz.gofoodsimulation.models.DriverModel;

/**
 * Created by sxio on 06-Jun-17.
 */

public class DriverData {
    static final String TABLE_NAME = "MSTDRIVER";
    static final String COLUMN_DRIVER_ID = "idDriver";
    static final String COLUMN_DRIVER_NAME = "nameDriver";
    static final String COLUMN_DRIVER_PHONE = "phoneNumber";
    static final String COLUMN_DRIVER_LATITUDE = "latitude";
    static final String COLUMN_DRIVER_LONGITUDE = "longitude";

    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_DRIVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DRIVER_NAME + " TEXT, " +
            COLUMN_DRIVER_PHONE + " TEXT, " +
            COLUMN_DRIVER_LATITUDE + " REAL, " +
            COLUMN_DRIVER_LONGITUDE + " REAL);";
    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    private String[] allColumns = {
            COLUMN_DRIVER_ID,
            COLUMN_DRIVER_NAME,
            COLUMN_DRIVER_PHONE,
            COLUMN_DRIVER_LATITUDE,
            COLUMN_DRIVER_LONGITUDE};

    public DriverData(Context context) {
        dbHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * TODO: Insert driver to database
     *
     * @param driverModel driver model
     * @return newly created DriverModel
     */
    public DriverModel createDriver(DriverModel driverModel) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRIVER_NAME, driverModel.getName());
        values.put(COLUMN_DRIVER_PHONE, driverModel.getPhone());
        values.put(COLUMN_DRIVER_LATITUDE, driverModel.getLatitude());
        values.put(COLUMN_DRIVER_LONGITUDE, driverModel.getLongitude());
        long id = database.insert(TABLE_NAME, null, values);

        Cursor cursor = database.query(TABLE_NAME, allColumns, COLUMN_DRIVER_ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();
        DriverModel driver = cursorToDriverModel(cursor);
        cursor.close();
        return driver;
    }

    /**
     * TODO: Create a Driver and insert driver to database
     *
     * @param name      driver name
     * @param phone     driver phone number
     * @param latitude  driver latitude coordinate
     * @param longitude driver longitude coordinate
     * @return newly created DriverModel
     */
    public DriverModel createDriver(String name, String phone, double latitude, double longitude) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRIVER_NAME, name);
        values.put(COLUMN_DRIVER_PHONE, phone);
        values.put(COLUMN_DRIVER_LATITUDE, latitude);
        values.put(COLUMN_DRIVER_LONGITUDE, longitude);
        long id = database.insert(TABLE_NAME, null, values);

        Cursor cursor = database.query(TABLE_NAME, allColumns, COLUMN_DRIVER_ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();
        DriverModel driver = cursorToDriverModel(cursor);
        cursor.close();
        return driver;
    }

    /**
     * TODO:  get all registered driver from database
     *
     * @return ArrayList of DriverModel that contain all listed driver
     */
    public ArrayList<DriverModel> getAllDriver() {
        this.open();
        ArrayList<DriverModel> driverModels = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, allColumns, null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            driverModels.add(cursorToDriverModel(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return driverModels;
    }

    public DriverModel cursorToDriverModel(Cursor cursor) {
        DriverModel driverModel = new DriverModel();
        driverModel.setId(cursor.getInt(0));
        driverModel.setName(cursor.getString(1));
        driverModel.setPhone(cursor.getString(2));
        driverModel.setLatitude(cursor.getDouble(3));
        driverModel.setLongitude(cursor.getDouble(4));
        return driverModel;
    }
}
