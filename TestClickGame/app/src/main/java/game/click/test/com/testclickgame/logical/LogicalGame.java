package game.click.test.com.testclickgame.logical;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import java.util.HashMap;

import game.click.test.com.testclickgame.R;
import game.click.test.com.testclickgame.data_base_helper.DataBaseHelper;
import game.click.test.com.testclickgame.data_base_helper.tables.action_table_price.TableResults;
import game.click.test.com.testclickgame.fragments.ICallbackPrintInfo;

/**
 * Created by kirichenko on 18.08.2015.
 */
public class LogicalGame implements LoaderManager.LoaderCallbacks<Cursor> {

    private int iLvl = 1, iScore = 0;

    private HashMap<Integer, ItemListOfLvl> mapOfListClick;

    private Activity activity;

    private ICallbackPrintInfo iCallbackPrintInfo;

    public LogicalGame(Activity activity) {

        this.activity = activity;

    }

    public HashMap<Integer, ItemListOfLvl> getMapOfListClick() {
        return mapOfListClick;
    }

    public void setMapOfListClick(HashMap<Integer, ItemListOfLvl> mapOfListClick) {
        this.mapOfListClick = mapOfListClick;
    }

    private boolean controlLvlUp(ItemListOfLvl itemListOfLvl) {

        if (mapOfListClick != null) {
            if (itemListOfLvl != null) {
                if (itemListOfLvl.getiCountToUp() <= getiScore()) {
                    return true;
                } else {
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
     *
     * @return Cost for this click. If cost is -1, it's game over.
     */
    public int actionClick(ICallbackPrintInfo iCallbackPrintInfo) {

        ItemListOfLvl itemListOfLvl = mapOfListClick.get(getiLvl());

        if (itemListOfLvl != null) {
            int iCurrentPriceClick = itemListOfLvl.getiClickPrice();

            setiScore(getiScore() + itemListOfLvl.getiClickPrice());

            if (mapOfListClick.get(getiLvl() + 1) != null) {

                ItemListOfLvl futureItemListOfLvl = mapOfListClick.get(getiLvl() + 1);

                if(futureItemListOfLvl != null){
                    if (controlLvlUp(futureItemListOfLvl)) {
                        setiLvl(getiLvl() + 1);
                    }
                }

                if (iCallbackPrintInfo != null && itemListOfLvl != null) {
                    iCallbackPrintInfo.printInformation(getiLvl(), getiScore(), itemListOfLvl.getiClickPrice());
                }

            } else {
                if (controlLvlUp(itemListOfLvl)) {
                    iCurrentPriceClick = -1;
                    setiScore(getiScore() - itemListOfLvl.getiClickPrice());
                }
            }

            return iCurrentPriceClick;
        } else {
            return 0;
        }


    }

    public void startLoadInformation(ICallbackPrintInfo iCallbackPrintInfo) {

        this.iCallbackPrintInfo = iCallbackPrintInfo;
        initLoadFromDataBase();

    }

    public void loadCurrentInformation(ICallbackPrintInfo iCallbackPrintInfo) {

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        int iPermanentValue = sharedPref.getInt(getActivity().getString(R.string.key_for_save_lvl), 1);

        setiLvl(iPermanentValue);

        iPermanentValue = sharedPref.getInt(getActivity().getString(R.string.key_for_save_score), 0);

        setiScore(iPermanentValue);

        if (mapOfListClick != null) {
            ItemListOfLvl itemListOfLvl = mapOfListClick.get(getiLvl());

            if (iCallbackPrintInfo != null && itemListOfLvl != null) {
                iCallbackPrintInfo.printInformation(getiLvl(), getiScore(), itemListOfLvl.getiClickPrice());
            }
        }
    }

    public void saveCurrentInformation() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(getActivity().getString(R.string.key_for_save_lvl), getiLvl());
        editor.putInt(getActivity().getString(R.string.key_for_save_score), getiScore());

        editor.commit();
    }

    public void restartGame(ICallbackPrintInfo iCallbackPrintInfo) {
        setiLvl(1);
        setiScore(0);

        ItemListOfLvl itemListOfLvl = mapOfListClick.get(getiLvl());

        if (iCallbackPrintInfo != null && itemListOfLvl != null) {
            iCallbackPrintInfo.printInformation(getiLvl(), getiScore(), itemListOfLvl.getiClickPrice());
        }
    }

    public int getCurrentSizeOfLvl() {

        if (getMapOfListClick() != null) {
            return getMapOfListClick().size();
        } else {
            return 0;
        }

    }

    public void initLoadFromDataBase() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    delegateLoadFromDataBase();
                }
            });
        }
    }

    private void delegateLoadFromDataBase() {
        if (activity != null) {
            activity.getLoaderManager().restartLoader(0, new Bundle(), this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoaderForPrice(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursotOfData) {

        HashMap<Integer, ItemListOfLvl> mapOfListClick = getMapOfListClick();

        if (mapOfListClick == null) {
            mapOfListClick = new HashMap<Integer, ItemListOfLvl>(13);
        } else {
            mapOfListClick.clear();
        }

        int iColumnIndexLvl = cursotOfData.getColumnIndex(TableResults.LVL_NAME);
        int iCountForUp = cursotOfData.getColumnIndex(TableResults.COUNT_TOP);
        int iClickPrice = cursotOfData.getColumnIndex(TableResults.CLICK_PRICE);

        if (cursotOfData.moveToFirst()) {
            do {

                ItemListOfLvl itemListOfLvl = new ItemListOfLvl();

                int iLvl = cursotOfData.getInt(iColumnIndexLvl);

                itemListOfLvl.setiLvl(iLvl);
                itemListOfLvl.setiCountToUp(cursotOfData.getInt(iCountForUp));
                itemListOfLvl.setiClickPrice(cursotOfData.getInt(iClickPrice));

                mapOfListClick.put(iLvl, itemListOfLvl);

            } while (cursotOfData.moveToNext());
        }

        setMapOfListClick(mapOfListClick);

        if (iCallbackPrintInfo != null) {
            iCallbackPrintInfo.callBackAboutSizeLvl(getCurrentSizeOfLvl());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class CursorLoaderForPrice extends CursorLoader {

        private Context mContext;

        public CursorLoaderForPrice(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        public Cursor loadInBackground() {
            return TableResults.getPriceForClicks(DataBaseHelper.getInstance(mContext));
        }
    }
}
