package joyli.example.com.shopifyinterntrialchallenge;

/**
 * Created by Joyli on 2017-06-22.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLdatabaseActivity extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mylist.db";
    public static final String TABLE_NAME = "mylist_data";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3="REVENUE";

    public SQLdatabaseActivity(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //called when the database is created for the first time. This iswehre the creation of tables and the initial population of the tables should happen

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " NAME TEXT, REVENUE TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String revenue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, revenue);

        long output = db.insert(TABLE_NAME, null, contentValues);
        //if data is inserted incorrectly it will return a -1

        if (output == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase(); //create and/ore open a database that will be used for reading and writing
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
