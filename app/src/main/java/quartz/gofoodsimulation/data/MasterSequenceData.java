package quartz.gofoodsimulation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sxio on 14-May-17.
 */

public class MasterSequenceData {
    static final String TABLE_NAME = "MSTSEQUENCE";
    static final String COLUMN_SEQ_ID = "id";
    static final String COLUMN_SEQ_NAME = "seq_name";
    static final String COLUMN_SEQ_VALUE = "seq_number";
    static final String COLUMN_SEQ_DESCRIPTION = "seq_description";

    static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_SEQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SEQ_NAME + " TEXT, " +
            COLUMN_SEQ_VALUE + " INTEGER, " +
            COLUMN_SEQ_DESCRIPTION + " TEXT);";
    static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;

    private String[] allColumns = {COLUMN_SEQ_ID, COLUMN_SEQ_NAME, COLUMN_SEQ_VALUE, COLUMN_SEQ_DESCRIPTION};

    public MasterSequenceData(Context context) {
        dbHelper = new DataBaseHelper(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Get sequence value by its id
     *
     * @param id The id of sequence used to search the sequence
     * @return An integer of the sequence value
     */
    public int get_seq(int id) {
        this.open();
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_SEQ_VALUE}, COLUMN_SEQ_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    /**
     * Add Sequence list to SQLite Database
     *
     * @param seq_name The sequence Name
     * @param seq_desc The sequence Description
     */
    public long addSequence(String seq_name, String seq_desc) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SEQ_NAME, seq_name);
        values.put(COLUMN_SEQ_VALUE, 1);
        values.put(COLUMN_SEQ_DESCRIPTION, seq_desc);
        return database.insert(TABLE_NAME, null, values);
    }

    /**
     * Update the related Sequence's value by 1
     *
     * @param id The Sequence id used to increase it value
     */
    public void update_seq(int id) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SEQ_VALUE, this.get_seq(id) + 1);
        database.update(TABLE_NAME, values, COLUMN_SEQ_ID + "=" + id, null);
    }
}
