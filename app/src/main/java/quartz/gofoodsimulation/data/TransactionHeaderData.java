package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import quartz.gofoodsimulation.models.HistoryHeaderModel;
import quartz.gofoodsimulation.models.TransactionHeaderModel;
import quartz.gofoodsimulation.utility.DateHelper;

/**
 * Created by sxio on 25-May-17.
 */

public class TransactionHeaderData {
    static final String TABLE_NAME = "TRHTRANSAKSI";
    static final String COLUMN_ID_TRANSAKSI = "idTransaksi";
    static final String COLUMN_USER_ID = "idUser";
    static final String COLUMN_TGL_TRANSAKSI = "tglTransaksi";
    static final String COLUMN_ID_SELLER = "idSeller";
    static final String COLUMN_GRAND_TOTAL = "grandTotal";
    static final String COLUMN_STATUS_BAYAR = "statusBayar";
    static final String COLUMN_KODE_DRIVER = "kodeDriver";
    static final String COLUMN_BIAYA_ANTAR = "biayaAntar";

    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID_TRANSAKSI + " TEXT PRIMARY KEY, " +
            COLUMN_USER_ID + " INTEGER, " +
            COLUMN_TGL_TRANSAKSI + " TEXT, " +
            COLUMN_ID_SELLER + " INTEGER, " +
            COLUMN_GRAND_TOTAL + " INTEGER, " +
            COLUMN_STATUS_BAYAR + " TEXT, " +
            COLUMN_KODE_DRIVER + " INTEGER, " +
            COLUMN_BIAYA_ANTAR + " INTEGER);";

    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context context;
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    private String[] allColumns = {COLUMN_ID_TRANSAKSI, COLUMN_USER_ID,
            COLUMN_TGL_TRANSAKSI, COLUMN_ID_SELLER,
            COLUMN_GRAND_TOTAL, COLUMN_STATUS_BAYAR,
            COLUMN_KODE_DRIVER, COLUMN_BIAYA_ANTAR};

    public TransactionHeaderData(Context context) {
        this.context = context;
        dbHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * TODO: Insert any created transaction to DB
     *
     * @param trhModel TransactionHeaderModel contains the requirements of data
     * @return TransactionHeaderModel
     */

    public TransactionHeaderModel insertTransaction(TransactionHeaderModel trhModel) {
        this.open();
        String sTglTrans = DateHelper.convertToString(new Date());

        MasterSequenceData mstseq = new MasterSequenceData(context);
        String id_trans = "GFS" + String.format("%04d", mstseq.get_seq(1)) + "-" + sTglTrans;

        trhModel.setIdTransaksi(id_trans);
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_TRANSAKSI, trhModel.getIdTransaksi());
        values.put(COLUMN_USER_ID, trhModel.getIdUser());
        values.put(COLUMN_TGL_TRANSAKSI, sTglTrans);
        values.put(COLUMN_ID_SELLER, trhModel.getIdSeller());
        values.put(COLUMN_GRAND_TOTAL, trhModel.getGrandTotal());
        values.put(COLUMN_STATUS_BAYAR, trhModel.getStatusBayar());
        values.put(COLUMN_KODE_DRIVER, trhModel.getIdDriver());
        values.put(COLUMN_BIAYA_ANTAR, trhModel.getDeliveryFee());

        long row_id = database.insert(TABLE_NAME, null, values);
        if (row_id != -1) mstseq.update_seq(1);

        String selection = COLUMN_ID_TRANSAKSI + " = ?";
        String[] selectionArgs = {trhModel.getIdTransaksi()};
        Cursor cursor = database.query(TABLE_NAME, allColumns, selection,
                selectionArgs, null, null, null);
        cursor.moveToFirst();
        TransactionHeaderModel newTrans = cursorToTransactionModel(cursor);
        cursor.close();
        return newTrans;
    }

    /**
     * TODO: Get the transaction by the specified user id
     *
     * @param idUser specified the unique user
     * @return ArrayList of HistoryHeaderModel
     */
    public ArrayList<HistoryHeaderModel> getTransactionByUserId(long idUser) {
        this.open();
        String query = "SELECT T.idTransaksi, T.tglTransaksi, T.grandTotal, " +
                "T.biayaAntar, T.statusBayar, S.address, S.sellerName " +
                "FROM TRHTRANSAKSI T JOIN MSTSELLER S " +
                "ON T.idSeller = S.idSeller WHERE T.idUser = ? " +
                "ORDER BY T.idTransaksi DESC";
        String[] selectionArgs = {Long.toString(idUser)};
        Cursor cursor = database.rawQuery(query, selectionArgs);
        ArrayList<HistoryHeaderModel> hhm = new ArrayList<>();
        while (cursor.moveToNext()) {
            hhm.add(cursorToHistoryHeaderModel(cursor));
        }
        return hhm;
    }

    /**
     * TODO: Get the transaction by specified transaction id
     *
     * @param idTransaction specified the unique transaction
     * @return HistoryHeaderModel
     */
    public HistoryHeaderModel getTransactionById(String idTransaction) {
        this.open();
        String query = "SELECT T.idTransaksi, T.tglTransaksi, T.grandTotal, " +
                "T.biayaAntar, T.statusBayar, S.address, S.sellerName " +
                "FROM TRHTRANSAKSI T JOIN MSTSELLER S " +
                "ON T.idSeller = S.idSeller WHERE T.idTransaksi = ?";
        String[] selectionArgs = {idTransaction};
        Cursor cursor = database.rawQuery(query, selectionArgs);
        cursor.moveToFirst();
        HistoryHeaderModel hhm = cursorToHistoryHeaderModel(cursor);
        cursor.close();
        return hhm;
    }

    /**
     * TODO: OverLoad the getTransactionById
     *
     * @param trhModel TransactionHeaderModel
     * @return TransactionHeaderModel
     */
    private TransactionHeaderModel getTransactionById(TransactionHeaderModel trhModel) {
        this.open();
        String whereClause = COLUMN_ID_TRANSAKSI + "='" + trhModel.getIdTransaksi() + "'";
        Cursor cursor = database.query(TABLE_NAME, null, whereClause, null, null, null, null);
        cursor.moveToFirst();
        TransactionHeaderModel trh = cursorToTransactionModel(cursor);
        cursor.close();
        return trh;
    }

    /**
     * TODO: Update the transaction
     *
     * @param trhModel TransactionHeaderModel that contains the requirements for updating the transaction
     * @@return TransactionHeaderModel
     */
    public TransactionHeaderModel updateTransaction(TransactionHeaderModel trhModel) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GRAND_TOTAL, trhModel.getGrandTotal());
        values.put(COLUMN_STATUS_BAYAR, trhModel.getStatusBayar());
        values.put(COLUMN_KODE_DRIVER, trhModel.getIdDriver());

        String whereClause = COLUMN_ID_TRANSAKSI + "='" + trhModel.getIdTransaksi() + "'";
        database.update(TABLE_NAME, values, whereClause, null);

        return getTransactionById(trhModel);
    }

    private TransactionHeaderModel cursorToTransactionModel(Cursor cursor) {
        TransactionHeaderModel trh = new TransactionHeaderModel();
        trh.setIdTransaksi(cursor.getString(0));
        trh.setIdUser(cursor.getInt(1));
        trh.setTglTransaksi(DateHelper.convertToDate(cursor.getString(2)));
        trh.setIdSeller(cursor.getInt(3));
        trh.setGrandTotal(cursor.getInt(4));
        trh.setStatusBayar(cursor.getString(5));
        trh.setIdDriver(cursor.getInt(6));
        trh.setDeliveryFee(cursor.getInt(7));
        return trh;
    }

    private HistoryHeaderModel cursorToHistoryHeaderModel(Cursor cursor) {
        HistoryHeaderModel hhm = new HistoryHeaderModel();
        hhm.setIdTransaksi(cursor.getString(0));
        hhm.setTglOrder(cursor.getString(1));
        hhm.setGrandTotal(cursor.getInt(2));
        hhm.setBiayaAntar(cursor.getInt(3));
        hhm.setStatusBayar(cursor.getString(4));
        hhm.setSellerAddress(cursor.getString(5));
        hhm.setSellerName(cursor.getString(6));
        return hhm;
    }
}

