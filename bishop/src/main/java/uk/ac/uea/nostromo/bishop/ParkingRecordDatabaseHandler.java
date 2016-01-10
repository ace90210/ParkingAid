package uk.ac.uea.nostromo.bishop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ParkingRecordDatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "parkingRecords";

    // Contacts table name
    private static final String TABLE_PARKING_RECORDS = "records";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ZONE= "zone";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_FEE = "fee";

    public ParkingRecordDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder CREATE_CONTACTS_TABLE = new StringBuilder();

        CREATE_CONTACTS_TABLE.append("CREATE TABLE " + TABLE_PARKING_RECORDS + "(" + KEY_ID + " INTEGER PRIMARY KEY,");

        CREATE_CONTACTS_TABLE.append(KEY_NAME + " TEXT,");
        CREATE_CONTACTS_TABLE.append(KEY_ZONE + " TEXT,");
        CREATE_CONTACTS_TABLE.append(KEY_START_TIME + " TEXT,");
        CREATE_CONTACTS_TABLE.append(KEY_END_TIME + " TEXT,");
        CREATE_CONTACTS_TABLE.append(KEY_FEE + " TEXT)");

        db.execSQL(CREATE_CONTACTS_TABLE.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING_RECORDS);

        onCreate(db);
    }

    public void addRecord(ParkingRecord record){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, record.getParkName());
        values.put(KEY_ZONE, record.getZone());
        values.put(KEY_START_TIME, record.getStartTime());
        values.put(KEY_END_TIME, record.getEndTime());
        values.put(KEY_FEE, record.getFee());

        // Inserting Row
        db.insert(TABLE_PARKING_RECORDS, null, values);
        db.close(); // Closing database connection
    }

    public ParkingRecord getParkingRecord(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PARKING_RECORDS,
                new String[] {
                        KEY_ID,
                        KEY_NAME,
                        KEY_ZONE,
                        KEY_START_TIME,
                        KEY_END_TIME,
                        KEY_FEE
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ParkingRecord pr = new ParkingRecord(
                Integer.parseInt(cursor.getString(0)), //Id
                cursor.getString(1), //park name
                cursor.getString(2), //zone
                cursor.getString(3), //start time
                cursor.getString(4), //end time
                cursor.getString(5) //fee
        );
        cursor.close();
        return pr;
    }

    public List<ParkingRecord> getAllRecords(){
        List<ParkingRecord> prList = new ArrayList<ParkingRecord>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PARKING_RECORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ParkingRecord pr = new ParkingRecord(
                        Integer.parseInt(cursor.getString(0)), //Id
                        cursor.getString(1), //park name
                        cursor.getString(2), //zone
                        cursor.getString(5), //fee
                        cursor.getString(3), //start time
                        cursor.getString(4) //end time
                );

                // Adding contact to list
                prList.add(pr);

            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return prList;
    }

    public int getParkingRecordCount(){
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + TABLE_PARKING_RECORDS;
        Cursor cursor = db.rawQuery(countQuery, null);

        cursor.close();

        return cursor.getCount();
    }

    public int updateParkingRecord(ParkingRecord record){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, record.getParkName());
        values.put(KEY_ZONE, record.getZone());
        values.put(KEY_START_TIME, record.getStartTime());
        values.put(KEY_END_TIME, record.getEndTime());
        values.put(KEY_FEE, record.getFee());

        // updating row
        return db.update(TABLE_PARKING_RECORDS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(record.get_id()) });
    }

    public void deleteParkingRecord(ParkingRecord record){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARKING_RECORDS, KEY_ID + " = ?",
                new String[] { String.valueOf(record.get_id()) });
        db.close();
    }
}
