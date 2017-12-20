package fi.tut.matti.vastavari;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String T_COLOR = "color";
    public static final String COLUMN_ID = "id";
    public static final String C_RED = "red";
    public static final String C_GREEN = "green";
    public static final String C_BLUE = "blue";

    private static final String DATABASE_NAME = "commments.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + T_COLOR + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + C_RED + " integer not null, "
            + C_GREEN + " integer not null, "
            + C_BLUE + " integer not null "
            + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + T_COLOR);
        onCreate(db);
    }


}