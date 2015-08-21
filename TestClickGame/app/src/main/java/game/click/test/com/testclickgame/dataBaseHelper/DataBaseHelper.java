package game.click.test.com.testclickgame.dataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import game.click.test.com.testclickgame.dataBaseHelper.tables.actionTablePrice.TableResults;

public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "game.click.test.com.testclickgame.db";
    public static final int DATABASE_VERSION = 1;

    private static DataBaseHelper mInstance;

    public static DataBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TableResults.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(TableResults.SQL_DELETE_ENTRIES);

        onCreate(db);
    }

}

