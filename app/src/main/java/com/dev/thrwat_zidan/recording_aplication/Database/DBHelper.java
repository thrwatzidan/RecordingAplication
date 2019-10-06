package com.dev.thrwat_zidan.recording_aplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.dev.thrwat_zidan.recording_aplication.InterFace.OnDataBaseChangeListener;
import com.dev.thrwat_zidan.recording_aplication.Models.RecodingItem;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "saved_recoding.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "saved_recoding_table";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_TIME_ADDED = "time_added";

    private static final String COMA_SKP = ",";

    private static OnDataBaseChangeListener onDataBaseChangeListener;

    private static final String SQLITE_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+"id INTEGER PRIMARY KEY" +
            " AUTOINCREMENT" + COMA_SKP +
            COLUMN_NAME + " TEXT" + COMA_SKP +
            COLUMN_PATH + " TEXT" + COMA_SKP +
            COLUMN_LENGTH + " INTEGER" + COMA_SKP +
            COLUMN_TIME_ADDED + " INTEGER " + ")";


    public DBHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,  null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLITE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean addRecoding(RecodingItem recodingItem) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_NAME, recodingItem.getName());
            contentValues.put(COLUMN_PATH, recodingItem.getPath());
            contentValues.put(COLUMN_LENGTH, recodingItem.getPath());
            contentValues.put(COLUMN_TIME_ADDED, recodingItem.getTime_added());

            db.insert(TABLE_NAME, null, contentValues);

            if (onDataBaseChangeListener != null) {
                onDataBaseChangeListener.onNewDatabaseEntryAdded(recodingItem);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<RecodingItem> getAllAudioFiles(){

        ArrayList<RecodingItem> arrayList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        String sql="SELECT * FROM "+TABLE_NAME;


            Cursor cursor = database.rawQuery(sql, null);
            if (cursor != null) {

                while (cursor.moveToNext()) {

                    String name = cursor.getString(1);
                    String path = cursor.getString(2);
                    int length = (int) cursor.getLong(3);
                    long timeAdded = cursor.getLong(4);

                    RecodingItem recodingItem = new RecodingItem(name, path, length, timeAdded);
                    arrayList.add(recodingItem);
                }

                cursor.close();
                return arrayList;
            } else {
                return null;
            }

    }

    public static void setOnDataBaseChangeListener(OnDataBaseChangeListener listener) {
        onDataBaseChangeListener = listener;
    }
}
