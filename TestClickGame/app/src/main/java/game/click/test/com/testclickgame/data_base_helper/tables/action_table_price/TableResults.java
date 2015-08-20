package game.click.test.com.testclickgame.data_base_helper.tables.action_table_price;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import game.click.test.com.testclickgame.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 20.08.2015.
 */
public class TableResults {

    final public static String TABLE_NAME  = "table_result";

    final public static String ID = "_id";
    final public static String LVL_NAME = "Level";
    final public static String COUNT_TOP = "Count_to_up";
    final public static String CLICK_PRICE = "Click_price";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LVL_NAME + " INTEGER,"
            + COUNT_TOP + " INTEGER,"
            + CLICK_PRICE + " INTEGER );";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public static Cursor getPriceForClicks(DataBaseHelper dbHelper){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }

}
