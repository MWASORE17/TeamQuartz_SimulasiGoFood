package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import quartz.gofoodsimulation.models.UserModel;

/**
 * Created by Wisz  on 4/1/17.
 * Modified by: sxio
 * Modified Remark:
 * 03-Apr-17
 * Tambah method getSingleEntry()
 * 04-Apr-17 Wisz
 * Ubah cara insert data menjadi object
 */

public class UserData {
    static final String TABLE_NAME = "MSTUSER";
    static final String COLUMN_USER_ID = "idUser";
    static final String COLUMN_USER_EMAIL = "email";
    static final String COLUMN_USER_NAMA = "nama";
    static final String COLUMN_USER_NO_TELP = "noTelp";
    static final String COLUMN_USER_PASS = "pass";
    static final String COLUMN_USER_GOPAY = "gopay";
    static final String COLUMN_USER_POINT = "point";

    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_EMAIL + " TEXT, " +
            COLUMN_USER_NAMA + " TEXT, " +
            COLUMN_USER_NO_TELP + " TEXT, " +
            COLUMN_USER_PASS + " TEXT, " +
            COLUMN_USER_GOPAY + " INTEGER, " +
            COLUMN_USER_POINT + " INTEGER);";
    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    private String[] allColumns = {COLUMN_USER_ID, COLUMN_USER_EMAIL,
            COLUMN_USER_NAMA, COLUMN_USER_NO_TELP, COLUMN_USER_PASS,
            COLUMN_USER_GOPAY, COLUMN_USER_POINT};

    public UserData(Context context) {
        dbHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * TODO: Create a User
     *
     * @param userModel UserModel that contains the requirements for creating a User
     * @return UserModel
     */
    public UserModel createUser(UserModel userModel) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, userModel.getEmail());
        values.put(COLUMN_USER_NAMA, userModel.getNama());
        values.put(COLUMN_USER_NO_TELP, userModel.getNoTelp());
        values.put(COLUMN_USER_PASS, userModel.getPass());
        values.put(COLUMN_USER_GOPAY, userModel.getGoPay());
        values.put(COLUMN_USER_POINT, userModel.getPoint());
        long insertId = database.insert(TABLE_NAME, null, values);

        Cursor cursor = database.query(TABLE_NAME, allColumns, COLUMN_USER_ID
                + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        UserModel newUserModel = cursorToUserModel(cursor);
        cursor.close();
        return newUserModel;
    }

    /**
     * TODO: Update the point after shopping
     *
     * @param userModel UserModel that contains the requirements for updating the point
     * @return UserModel
     */
    public UserModel updatePoint(UserModel userModel) {
        this.open();
        UserModel currentUser = getSingleEntry(userModel.getEmail());
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_POINT, userModel.getPoint() + currentUser.getPoint());
        database.update(TABLE_NAME, values, COLUMN_USER_ID + "=" + userModel.getId(), null);
        return getSingleEntry(userModel.getEmail());
    }

    /**
     * TODO: Update the point after shopping
     *
     * @param userModel UserModel that contains the requirements for updating the GO-PAY
     * @return UserModel
     */
    public UserModel updateGoPay(UserModel userModel) {
        this.open();
        UserModel currentUser = getSingleEntry(userModel.getEmail());
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_GOPAY, userModel.getGoPay() + currentUser.getGoPay());
        database.update(TABLE_NAME, values, COLUMN_USER_ID + "=" + userModel.getId(), null);
        return getSingleEntry(userModel.getEmail());
    }

    public void deleteUser(UserModel usermodel) {
        this.open();
        long id = usermodel.getId();
        System.out.println("User telah dihapus dengan id " + id);
        database.delete(TABLE_NAME, COLUMN_USER_ID + " = " + id, null);
    }

    public List<UserModel> getAllUser() {
        List<UserModel> userModels = new ArrayList<UserModel>();
        Cursor cursor = database.query(TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            UserModel userModel = cursorToUserModel(cursor);
            userModels.add(userModel);
            cursor.moveToNext();
        }
        cursor.close();
        return userModels;
    }

    /**
     * TODO: Get a single user
     *
     * @param _email email of the user
     * @return UserModel
     */
    public UserModel getSingleEntry(String _email) {
        this.open();
        UserModel getUser = new UserModel();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_USER_EMAIL + "=?", new String[]{_email}, null, null, null);
        if (cursor.getCount() < 1) // _email Not Exist
        {
            cursor.close();
            getUser.setEmail("NULL");
            return getUser;
        }
        cursor.moveToFirst();
        getUser = cursorToUserModel(cursor);
        cursor.close();
        return getUser;
    }

    private UserModel cursorToUserModel(Cursor cursor) {
        UserModel cUserModel = new UserModel();
        cUserModel.setId(cursor.getInt(0));
        cUserModel.setEmail(cursor.getString(1));
        cUserModel.setNama(cursor.getString(2));
        cUserModel.setNoTelp(cursor.getString(3));
        cUserModel.setPass(cursor.getString(4));
        cUserModel.setGoPay(cursor.getLong(5));
        cUserModel.setPoint(cursor.getLong(6));
        return cUserModel;
    }
}
