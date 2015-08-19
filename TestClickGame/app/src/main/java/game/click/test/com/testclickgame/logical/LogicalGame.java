package game.click.test.com.testclickgame.logical;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import game.click.test.com.testclickgame.R;
import game.click.test.com.testclickgame.fragments.ICallbackPrintInfo;

/**
 * Created by kirichenko on 18.08.2015.
 */
public class LogicalGame {

    private int iLvl = 1, iScore = 0;

    private HashMap <Integer, ItemListOfLvl> mapOfListClick;

    private Activity activity;

    public LogicalGame(Activity activity) {

        this.activity =  activity;

    }

    public HashMap<Integer, ItemListOfLvl> getMapOfListClick() {
        return mapOfListClick;
    }

    public void setMapOfListClick(HashMap<Integer, ItemListOfLvl> mapOfListClick) {
        this.mapOfListClick = mapOfListClick;
    }

    private boolean controlLvlUp(ItemListOfLvl itemListOfLvl){

        if(mapOfListClick != null){
            if(itemListOfLvl != null){
                if(itemListOfLvl.getiCountToUp() <= getiScore()){
                    return true;
                }else{
                    return false;
                }
            }

        }

        return false;
    }

    public int getiLvl() {
        return iLvl;
    }

    public void setiLvl(int iLvl) {
        this.iLvl = iLvl;
    }

    public int getiScore() {
        return iScore;
    }

    public void setiScore(int iScore) {
        this.iScore = iScore;
    }

    public Activity getActivity() {
        return activity;
    }

    /**
     * Reduce click
     * @return Cost for this click. If cost is -1, it's game over.
     */
    //TODO Переход по уровням тоже необходимо доделать.
    public int actionClick(ICallbackPrintInfo iCallbackPrintInfo) {

        ItemListOfLvl itemListOfLvl = mapOfListClick.get(getiLvl());

        int iCurrentPriceClick = itemListOfLvl.getiClickPrice();

        setiScore(getiScore() + itemListOfLvl.getiClickPrice());

        if(controlLvlUp(itemListOfLvl)){

            if(mapOfListClick.get(getiLvl() + 1) != null){
                setiLvl(getiLvl() + 1);
            }else{
                iCurrentPriceClick = -1;
            }

        }

        if(iCallbackPrintInfo != null && itemListOfLvl != null) {
            iCallbackPrintInfo.printInformation(getiLvl(), getiScore(), itemListOfLvl.getiClickPrice());
        }

        return iCurrentPriceClick;
    }

    public void loadCurrentInformation(ICallbackPrintInfo iCallbackPrintInfo){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        int iPermanentValue = sharedPref.getInt(getActivity().getString(R.string.key_for_save_lvl), 1);

        setiLvl(iPermanentValue);

        iPermanentValue = sharedPref.getInt(getActivity().getString(R.string.key_for_save_score), 0);

        setiScore(iPermanentValue);

        if(mapOfListClick != null){
            ItemListOfLvl itemListOfLvl = mapOfListClick.get(getiLvl());

            if(iCallbackPrintInfo != null && itemListOfLvl != null){
                iCallbackPrintInfo.printInformation(getiLvl(), getiScore(), itemListOfLvl.getiClickPrice());
            }
        }
    }

    public void saveCurrentInformation(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(getActivity().getString(R.string.key_for_save_lvl), getiLvl());
        editor.putInt(getActivity().getString(R.string.key_for_save_score), getiScore());

        editor.commit();
    }

    public void restartGame(ICallbackPrintInfo iCallbackPrintInfo){
        setiLvl(1);
        setiScore(0);

        ItemListOfLvl itemListOfLvl = mapOfListClick.get(getiLvl());

        if(iCallbackPrintInfo != null && itemListOfLvl != null){
            iCallbackPrintInfo.printInformation(getiLvl(), getiScore(), itemListOfLvl.getiClickPrice());
        }
    }
}
