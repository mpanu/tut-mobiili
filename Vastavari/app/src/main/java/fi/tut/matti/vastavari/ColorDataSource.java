package fi.tut.matti.vastavari;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matti on 12/8/17.
 */

public class ColorDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public ColorDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void store(int r, int g, int b){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.C_RED, r);
        values.put(MySQLiteHelper.C_GREEN, g);
        values.put(MySQLiteHelper.C_BLUE, b);
        database.insert(MySQLiteHelper.T_COLOR, null, values);
    }

    public List<int[]> findAll(){
        List<int[]> colors = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.T_COLOR,
                new String[]{MySQLiteHelper.C_RED, MySQLiteHelper.C_GREEN, MySQLiteHelper.C_BLUE, MySQLiteHelper.COLUMN_ID },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int[] intArr = new int[4];
            intArr[0] = cursor.getInt(0);
            intArr[1] = cursor.getInt(1);
            intArr[2] = cursor.getInt(2);
            intArr[3] = cursor.getInt(3);
            colors.add(intArr);
            cursor.moveToNext();
        }
        cursor.close();
        return colors;
    }

    public void delete(int id){
        database.delete(MySQLiteHelper.T_COLOR, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

}
