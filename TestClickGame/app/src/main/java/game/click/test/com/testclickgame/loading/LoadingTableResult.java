package game.click.test.com.testclickgame.loading;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import game.click.test.com.testclickgame.R;
import game.click.test.com.testclickgame.loadInfromation.AsyncLoadingInformation;
import game.click.test.com.testclickgame.loadInfromation.ICallbackForLoadingMainInformation;
import game.click.test.com.testclickgame.logical.ItemListOfLvl;
import game.click.test.com.testclickgame.logical.LogicalGame;

/**
 * Created by kirichenko on 19.08.2015.
 */
public class LoadingTableResult implements ICallbackForLoadingMainInformation {

    public static String LVL_NAME = "Level";
    public static String COUNT_TOP = "Count_to_up";
    public static String CLICK_PRICE = "Click_price";

    private LogicalGame oLogicalGame;

    public LoadingTableResult(LogicalGame oLogicalGame) {
        this.oLogicalGame = oLogicalGame;
    }

    public void startLoadSource(Context mContext){
        
        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation(mContext.getString(R.string.link_for_request_price_click), this, "");
        loadingInformation.setDaemon(true);
        loadingInformation.start();

    }


    @Override
    public void getLoadedInformation(JSONArray oJsonArray, Object identificationNumber) {
        HashMap<Integer, ItemListOfLvl> mapOfListClick;

        if(oLogicalGame != null){

                mapOfListClick = oLogicalGame.getMapOfListClick();

                if(mapOfListClick == null){
                    mapOfListClick = new HashMap<>();
                }else{
                    mapOfListClick.clear();
                }

            if(oJsonArray != null){

                for(int iCount = 0; iCount < oJsonArray.length(); iCount ++){
                    try {

                        JSONObject jsonObject = (JSONObject) oJsonArray.get(iCount);

                        int iLvl = 0, iCountToUp = 0, iClickPrice = 0;

                        String sValue = jsonObject.get(LVL_NAME).toString();

                        try{
                            iLvl =  Integer.valueOf(sValue);
                        }catch (Exception ex){};


                        sValue = jsonObject.get(COUNT_TOP).toString();

                        try{
                            iCountToUp =  Integer.valueOf(sValue);
                        }catch (Exception ex){};

                        sValue = jsonObject.get(CLICK_PRICE).toString();

                        try{
                            iClickPrice =  Integer.valueOf(sValue);
                        }catch (Exception ex){};

                        mapOfListClick.put(iLvl, new ItemListOfLvl(iLvl, iCountToUp, iClickPrice));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                oLogicalGame.setMapOfListClick(mapOfListClick);

            }

        }

    }
}
