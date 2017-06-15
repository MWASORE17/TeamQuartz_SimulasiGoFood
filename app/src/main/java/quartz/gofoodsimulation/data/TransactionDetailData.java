package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import quartz.gofoodsimulation.models.FoodModel;
import quartz.gofoodsimulation.models.GroceryModel;
import quartz.gofoodsimulation.models.TransactionDetailModel;

/**
 * Created by sxio on 25-May-17.
 */

public class TransactionDetailData {
    static final String TABLE_NAME = "TRDTRANSAKSI";
    static final String COLUMN_ID = "idTransaksi";
    static final String COLUMN_ID_FOOD = "idFood";
    static final String COLUMN_QUANTITY = "quantity";
    static final String COLUMN_PCS_HARGA = "pcsHarga";

    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " TEXT, " +
            COLUMN_ID_FOOD + " TEXT, " +
            COLUMN_QUANTITY + " INTEGER, " +
            COLUMN_PCS_HARGA + " INTEGER, " +
            "PRIMARY KEY (" + COLUMN_ID + ", " + COLUMN_ID_FOOD + "));";
    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    private String[] allColumns = {COLUMN_ID, COLUMN_ID_FOOD,
            COLUMN_QUANTITY, COLUMN_PCS_HARGA};

    public TransactionDetailData(Context context) {
        dbHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * TODO: Insert transaction to DB
     *
     * @param trdModel ArrayList of TransactionDetailModel that contains the requirements to create a transaction
     */
    public void insertTransaction(ArrayList<TransactionDetailModel> trdModel) {
        this.open();
        for (TransactionDetailModel trd : trdModel) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, trd.getIdTransaksi());
            values.put(COLUMN_ID_FOOD, trd.getIdFood());
            values.put(COLUMN_QUANTITY, trd.getQuantity());
            values.put(COLUMN_PCS_HARGA, trd.getPcsHarga());

            database.insert(TABLE_NAME, null, values);
        }
    }

    /**
     * TODO: Get Transaction Detail based on specified transaction id
     *
     * @param idTransaction the id of a transaction
     * @return groceries
     */
    public ArrayList<GroceryModel> getTransactionDetailById(String idTransaction) {
        this.open();
        String query = "SELECT T.quantity, T.pcsHarga, F.* " +
                "FROM TRDTRANSAKSI T JOIN FOOD F " +
                "ON T.idFood = F.idFood " +
                "WHERE T.idTransaksi = ? " +
                "ORDER BY T.pcsHarga";
        String[] selectionArgs = {idTransaction};
        Cursor cursor = database.rawQuery(query, selectionArgs);
        ArrayList<GroceryModel> groceries = new ArrayList<>();
        while (cursor.moveToNext()) {
            groceries.add(cursorToGroceryModel(cursor));
        }
        return groceries;
    }

    public void deleteTransactionById(String idTransaction) {
        this.open();
        database.delete(TABLE_NAME, COLUMN_ID + "='" + idTransaction + "'", null);
    }

    private GroceryModel cursorToGroceryModel(Cursor cursor) {
        FoodModel food = new FoodModel();
        food.setIdFood(cursor.getString(2));
        food.setCodeCategory(cursor.getString(3));
        food.setNameFood(cursor.getString(4));
        food.setPriceFood(cursor.getInt(1));
        food.setPhotoFood(cursor.getString(6));
        GroceryModel grocery = new GroceryModel(food, cursor.getInt(0));
        return grocery;
    }

    private TransactionDetailModel cursorToTransactionModel(Cursor cursor) {
        TransactionDetailModel trd = new TransactionDetailModel();
        trd.setIdTransaksi(cursor.getString(0));
        trd.setIdFood(cursor.getString(1));
        trd.setQuantity(cursor.getInt(2));
        trd.setPcsHarga(cursor.getInt(3));
        return trd;
    }
}