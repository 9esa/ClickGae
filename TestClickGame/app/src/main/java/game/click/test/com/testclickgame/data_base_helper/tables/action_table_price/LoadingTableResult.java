package game.click.test.com.testclickgame.data_base_helper.tables.action_table_price;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import game.click.test.com.testclickgame.R;
import game.click.test.com.testclickgame.data_base_helper.DataBaseHelper;
import game.click.test.com.testclickgame.fragments.ICallBackFromServer;
import game.click.test.com.testclickgame.load_infromation.AsyncLoadingInformation;
import game.click.test.com.testclickgame.load_infromation.ICallbackForLoadingMainInformation;
import game.click.test.com.testclickgame.logical.LogicalGame;

/**
 * Created by kirichenko on 19.08.2015.
 */
public class LoadingTableResult implements ICallbackForLoadingMainInformation {

    private LogicalGame oLogicalGame;

    private Context mContext;

    private ICallBackFromServer iCallBackFromServer;

    public LoadingTableResult(LogicalGame oLogicalGame, ICallBackFromServer iCallBackFromServer) {
        this.oLogicalGame = oLogicalGame;
        this.iCallBackFromServer = iCallBackFromServer;
    }

    public void startLoadSource(Context mContext){

        this.mContext = mContext;

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation(mContext.getString(R.string.link_for_request_price_click), this, "");
        loadingInformation.setDaemon(true);
        loadingInformation.start();

    }

    @Override
    public void getLoadedInformation(JSONArray oJsonArray, Object identificationNumber) {

        if(oLogicalGame != null){

            if(oJsonArray != null){
                TableResults.removeAllInfornationAboutTables(DataBaseHelper.getInstance(mContext));

                String sql = "INSERT INTO "+ TableResults.TABLE_NAME +" VALUES (NULL,?,?,?);";
                SQLiteStatement statement =  DataBaseHelper.getInstance(mContext).getWritableDatabase().compileStatement(sql);
                DataBaseHelper.getInstance(mContext).getWritableDatabase().beginTransaction();

                for(int iCount = 0; iCount < oJsonArray.length(); iCount ++){
                    try {

                        JSONObject jsonObject = (JSONObject) oJsonArray.get(iCount);

                        int iLvl = 0, iCountToUp = 0, iClickPrice = 0;

                        String sValue = jsonObject.get(TableResults.LVL_NAME).toString();

                        try{
                            iLvl =  Integer.valueOf(sValue);
                        }catch (Exception ex){};


                        sValue = jsonObject.get(TableResults.COUNT_TOP).toString();

                        try{
                            iCountToUp =  Integer.valueOf(sValue);
                        }catch (Exception ex){};

                        sValue = jsonObject.get(TableResults.CLICK_PRICE).toString();

                        try{
                            iClickPrice =  Integer.valueOf(sValue);
                        }catch (Exception ex){};

                        statement.clearBindings();

                        statement.bindLong(1, iLvl);
                        statement.bindLong(2, iCountToUp);
                        statement.bindLong(3, iClickPrice);

                        statement.execute();


//                        ContentValues values = new ContentValues();
//
//                        values.put(TableResults.LVL_NAME, iLvl);
//                        values.put(TableResults.COUNT_TOP, iCountToUp);
//                        values.put(TableResults.CLICK_PRICE, iClickPrice);
//
//                        DataBaseHelper.getInstance(mContext).getWritableDatabase().insertWithOnConflict(TableResults.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                DataBaseHelper.getInstance(mContext).getWritableDatabase().setTransactionSuccessful();
                DataBaseHelper.getInstance(mContext).getWritableDatabase().endTransaction();

            }
        }
        if(iCallBackFromServer != null){
            iCallBackFromServer.loadComplete();
        }

    }
}
