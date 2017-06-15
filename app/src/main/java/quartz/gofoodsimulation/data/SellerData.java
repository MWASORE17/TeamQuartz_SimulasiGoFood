package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import quartz.gofoodsimulation.models.SellerModel;
import quartz.gofoodsimulation.utility.DateHelper;
import quartz.gofoodsimulation.utility.GPSTracker;

/**
 * Created by kened on 4/8/2017.
 * Modified by: kened
 * Modified Remark:
 * 08-Apr-17
 * create variables and database create string
 * 09-Apr-17
 * create getMatchSellersn and cursorToSellerModel
 */

public class SellerData {
    static final String TABLE_NAME = "MSTSELLER";
    static final String COLUMN_SELLER_ID = "idSeller";
    static final String COLUMN_SELLER_NAME = "sellerName";
    static final String COLUMN_SELLER_PHONE = "phone";
    static final String COLUMN_SELLER_ADDRESS = "address";
    static final String COLUMN_SELLER_COVER_PHOTO = "coverPhoto";
    static final String COLUMN_SELLER_CATEGORY = "category";
    static final String COLUMN_SELLER_STATUS = "status";
    static final String COLUMN_SELLER_JUMLAH_ORDER = "jumlahOrder";
    static final String COLUMN_SELLER_OPEN_TIME = "operatianalTime";
    static final String COLUMN_SELLER_JOIN_DATE = "joinDate";
    static final String COLUMN_SELLER_LAT = "latitude";
    static final String COLUMN_SELLER_LONG = "longitude";


    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_SELLER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SELLER_NAME + " TEXT, " +
            COLUMN_SELLER_PHONE + " TEXT, " +
            COLUMN_SELLER_ADDRESS + " TEXT, " +
            COLUMN_SELLER_COVER_PHOTO + " TEXT, " +
            COLUMN_SELLER_CATEGORY + " TEXT, " +
            COLUMN_SELLER_STATUS + " INTEGER, " +
            COLUMN_SELLER_JUMLAH_ORDER + " INTEGER, " +
            COLUMN_SELLER_OPEN_TIME + " TEXT, " +
            COLUMN_SELLER_JOIN_DATE + " TEXT, " +
            COLUMN_SELLER_LAT + " REAL, " +
            COLUMN_SELLER_LONG + " REAL);";
    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    DataBaseHelper dataBaseHelper;
    private SQLiteDatabase database;
    private Context context;

    private String[] allColumns = {
            COLUMN_SELLER_ID,
            COLUMN_SELLER_NAME,
            COLUMN_SELLER_PHONE,
            COLUMN_SELLER_ADDRESS,
            COLUMN_SELLER_COVER_PHOTO,
            COLUMN_SELLER_CATEGORY,
            COLUMN_SELLER_STATUS,
            COLUMN_SELLER_JUMLAH_ORDER,
            COLUMN_SELLER_OPEN_TIME,
            COLUMN_SELLER_JOIN_DATE,
            COLUMN_SELLER_LAT,
            COLUMN_SELLER_LONG};

    public SellerData(Context context) {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    private void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
    }

    /**
     * TODO: Create a Seller and insert it to DB
     *
     * @param sellers contains all of the requirements to create a seller like seller`s name, etc.
     */
    public void createSeller(ArrayList<SellerModel> sellers) {
        this.open();
        for (SellerModel seller : sellers) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_SELLER_NAME, seller.getName());
            cv.put(COLUMN_SELLER_PHONE, seller.getPhone());
            cv.put(COLUMN_SELLER_ADDRESS, seller.getAddress());
            cv.put(COLUMN_SELLER_COVER_PHOTO, seller.getCoverPhoto());
            cv.put(COLUMN_SELLER_CATEGORY, seller.getCategory());
            cv.put(COLUMN_SELLER_STATUS, seller.getStatus());
            cv.put(COLUMN_SELLER_JUMLAH_ORDER, seller.getJumlahOrder());
            cv.put(COLUMN_SELLER_OPEN_TIME, seller.getTime());
            cv.put(COLUMN_SELLER_JOIN_DATE, DateHelper.convertToString(seller.getJoinDate()));
            cv.put(COLUMN_SELLER_LAT, seller.getLatitude());
            cv.put(COLUMN_SELLER_LONG, seller.getLongitude());

            database.insert(TABLE_NAME, null, cv);
        }
    }

    /**
     * TODO: Get the total order of a Seller based on the specified id
     *
     * @param idSeller to specified a seller
     * @return total order
     */
    private int getTotalOrder(long idSeller) {
        this.open();
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_SELLER_JUMLAH_ORDER}, COLUMN_SELLER_ID + "=?",
                new String[]{String.valueOf(idSeller)}, null, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    /**
     * TODO: Update the total order of a Seller based on the specified id
     *
     * @param idSeller to specified a seller
     */
    public void updateTotalOrder(long idSeller) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SELLER_JUMLAH_ORDER, this.getTotalOrder(idSeller) + 1);
        database.update(TABLE_NAME, values, COLUMN_SELLER_ID + "=" + idSeller, null);
    }

    /**
     * TODO: Untuk mencari Seller
     *
     * @param searchValue   value search yang diketik
     * @param listType      untuk membedakan tombol-tombol, 0: Near Me , 1: 24 Hours , 2: Best Seller, 3: New Sellers , 4: Typing search
     * @param categoryValue untuk mengidentifikasi value category seller
     * @return ArrayList of SellerModel
     */
    public ArrayList<SellerModel> getMatchSellers(String searchValue, int listType, String categoryValue) {
        this.open();
        ArrayList<SellerModel> sellerModels = new ArrayList<>();
        Cursor cursor;
        String query;
        switch (listType) {
            //best seller
            case 2:
                query = "SELECT MSTSELLER.*, MSTFOODGROUP.nameGroup FROM MSTSELLER " +
                        "JOIN MSTFOODGROUP ON " +
                        "MSTSELLER.category = MSTFOODGROUP.codeGroup " +
                        "WHERE MSTSELLER.jumlahOrder >= ? " +
                        "AND MSTSELLER.sellerName LIKE ? " +
                        "ORDER BY MSTSELLER.jumlahOrder DESC";
                cursor = database.rawQuery(query, new String[]{String.valueOf(100), "%" + searchValue + "%"});
                break;

            case 4:
                query = "SELECT MSTSELLER.*, MSTFOODGROUP.nameGroup FROM MSTSELLER " +
                        "JOIN MSTFOODGROUP ON " +
                        "MSTSELLER.category = MSTFOODGROUP.codeGroup " +
                        "WHERE MSTSELLER.category = ? AND " +
                        "MSTSELLER.sellerName LIKE ?";
                cursor = database.rawQuery(query, new String[]{categoryValue, "%" + searchValue + "%"});
                break;

            //normal search
            default:
                query = "SELECT MSTSELLER.*, MSTFOODGROUP.nameGroup FROM MSTSELLER " +
                        "JOIN MSTFOODGROUP ON " +
                        "MSTSELLER.category = MSTFOODGROUP.codeGroup " +
                        "WHERE MSTSELLER.sellerName = ?";
                cursor = database.rawQuery(query, new String[]{searchValue});
                break;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sellerModels.add(cursorToSellerModel(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        sellerModels = getRangeSorted(sellerModels, listType);
        return sellerModels;
    }

    /**
     * TODO: Get All listed Sellers
     *
     * @param listType      untuk membedakan tombol-tombol, 0: Near Me , 1: 24 Hours , 2: Best Seller, 3: New Sellers , 4: Typing search
     * @param categoryValue untuk mengidentifikasi value category seller
     * @return ArrayList of SellerModel
     */
    public ArrayList<SellerModel> getAllSellers(int listType, String categoryValue) {
        this.open();
        ArrayList<SellerModel> SellerModels = new ArrayList<>();
        Cursor cursor;
        String query;
        switch (listType) {
            //best seller
            case 2:
                query = "SELECT MSTSELLER.*, MSTFOODGROUP.nameGroup FROM MSTSELLER " +
                        "JOIN MSTFOODGROUP ON " +
                        "MSTSELLER.category = MSTFOODGROUP.codeGroup " +
                        "WHERE MSTSELLER.jumlahOrder >= ? " +
                        "ORDER BY MSTSELLER.jumlahOrder DESC";
                String[] selectionArgs = {String.valueOf(100)};
                cursor = database.rawQuery(query, selectionArgs);
                break;

            case 4:
                query = "SELECT MSTSELLER.*, MSTFOODGROUP.nameGroup FROM MSTSELLER " +
                        "JOIN MSTFOODGROUP ON " +
                        "MSTSELLER.category = MSTFOODGROUP.codeGroup " +
                        "WHERE MSTSELLER.category = ?";
                cursor = database.rawQuery(query, new String[]{categoryValue});
                break;

            //normal search
            default:
                query = "SELECT MSTSELLER.*, MSTFOODGROUP.nameGroup FROM MSTSELLER " +
                        "JOIN MSTFOODGROUP ON " +
                        "MSTSELLER.category = MSTFOODGROUP.codeGroup";
                cursor = database.rawQuery(query, null);
                break;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SellerModels.add(cursorToSellerModel(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        SellerModels = getRangeSorted(SellerModels, -1);
        return SellerModels;
    }

    /**
     * TODO: Sort given ArrayList of SellerModel by its Range then by its filter function
     *
     * @param sellers  The ArrayList of SellerModel to sort
     * @param listType Filter list type
     * @return Sorted ArrayList of SellerModel
     */
    private ArrayList<SellerModel> getRangeSorted(ArrayList<SellerModel> sellers, final int listType) {
        Collections.sort(sellers, new Comparator<SellerModel>() {
            @Override
            public int compare(SellerModel o1, SellerModel o2) {
                int result = Float.compare(o1.getRange(), o2.getRange());
                switch (listType) {
                    case 2:
                        if (result == 0) {
                            result = Integer.compare(o2.getJumlahOrder(), o1.getJumlahOrder());
                        }
                        return result;
                    default:
                        return result;
                }
            }
        });
        return sellers;
    }

    private SellerModel cursorToSellerModel(Cursor cursor) {
        GPSTracker gps = new GPSTracker(context);
        Location myLocation = gps.getLocation();

        SellerModel sellerModel = new SellerModel();
        sellerModel.setId(cursor.getLong(0));
        sellerModel.setName(cursor.getString(1));
        sellerModel.setPhone(cursor.getString(2));
        sellerModel.setAddress(cursor.getString(3));
        sellerModel.setCoverPhoto(cursor.getString(4));
        sellerModel.setCategory(cursor.getString(12));
        sellerModel.setJumlahOrder(cursor.getInt(7));
        sellerModel.setTime(cursor.getString(8));
        sellerModel.setJoinDate(DateHelper.convertToDate(cursor.getString(9)));
        sellerModel.setLatitude(cursor.getDouble(10));
        sellerModel.setLongitude(cursor.getDouble(11));
        sellerModel.setRange(myLocation);
        gps.stopUsingGPS();
        return sellerModel;
    }
}
