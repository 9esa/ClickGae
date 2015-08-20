package game.click.test.com.testclickgame.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import game.click.test.com.testclickgame.R;
import game.click.test.com.testclickgame.data_base_helper.tables.action_table_price.TableResults;

/**
 * Created by kirichenko on 20.08.2015.
 */
public class AdapterPriceItem extends CursorAdapter {

    public AdapterPriceItem(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View oCurrentView = LayoutInflater.from(context).inflate(R.layout.item_price_click, parent, false);
        MenuHolder menuHolder = new MenuHolder();

        menuHolder.tvLvl = (TextView) oCurrentView.findViewById(R.id.tvItemLvl);
        menuHolder.tvScoreToUp = (TextView) oCurrentView.findViewById(R.id.tvItemScoreForUp);
        menuHolder.tvClickPrice = (TextView) oCurrentView.findViewById(R.id.tvItemPriceClick);

        oCurrentView.setTag(menuHolder);

        return oCurrentView;
    }

    @Override
    public void bindView(View oCurrentView, Context context, Cursor cursor) {
        int iLvl, iScoreForUp, iPriceForClick;

        MenuHolder menuHolder = (MenuHolder) oCurrentView.getTag();

        if (menuHolder != null) {

            iLvl = cursor.getInt(cursor.getColumnIndex(TableResults.LVL_NAME));
            iScoreForUp = cursor.getInt(cursor.getColumnIndex(TableResults.COUNT_TOP));
            iPriceForClick = cursor.getInt(cursor.getColumnIndex(TableResults.CLICK_PRICE));

            if(iLvl == 0){
                menuHolder.tvLvl.setText("-");
            }else{
                menuHolder.tvLvl.setText(iLvl+ "");
            }

            if(iScoreForUp == 0){
                menuHolder.tvScoreToUp.setText("-");
            }else{
                menuHolder.tvScoreToUp.setText(iScoreForUp + "");
            }

            if(iPriceForClick == 0){
                menuHolder.tvClickPrice.setText("-");
            }else{
                menuHolder.tvClickPrice.setText(iPriceForClick + "");
            }

        }
    }


    public static class MenuHolder {
        TextView tvLvl;
        TextView tvScoreToUp;
        TextView tvClickPrice;
    }
}
