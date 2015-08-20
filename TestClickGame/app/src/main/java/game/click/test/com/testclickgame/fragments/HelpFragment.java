package game.click.test.com.testclickgame.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import game.click.test.com.testclickgame.R;
import game.click.test.com.testclickgame.data_base_helper.DataBaseHelper;
import game.click.test.com.testclickgame.data_base_helper.tables.action_table_price.TableResults;

/**
 * Created by kirichenko on 20.08.2015.
 */
public class HelpFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView lvItemPrice;

    private FrameLayout flForProgressBar;

    private TextView tvHeaderLvl;
    private TextView tvHeaderScoreToUp;
    private TextView tvHeaderClickPrice;

    private AdapterPriceItem adapterPriceItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oView = inflater.inflate(R.layout.help_fragment, null);

        lvItemPrice = (ListView) oView.findViewById(R.id.lvPriceItems);

        tvHeaderLvl = (TextView) oView.findViewById(R.id.tvItemLvl);
        tvHeaderScoreToUp = (TextView) oView.findViewById(R.id.tvItemScoreForUp);
        tvHeaderClickPrice = (TextView) oView.findViewById(R.id.tvItemPriceClick);

        flForProgressBar = (FrameLayout) oView.findViewById(R.id.flProgressHelp);

        flForProgressBar.setVisibility(View.VISIBLE);
        flForProgressBar.bringToFront();

        setHeaderName();

        adapterPriceItem = new AdapterPriceItem(getActivity(), null);

        lvItemPrice.setAdapter(adapterPriceItem);

        getActivity().getLoaderManager().restartLoader(0, new Bundle(), this);

        return oView;
    }

    private void setHeaderName(){
        tvHeaderLvl.setText(R.string.level);
        tvHeaderScoreToUp.setText(R.string.count_click_for_up);
        tvHeaderClickPrice.setText(R.string.price_for_click);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoaderForItemPrice(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapterPriceItem.swapCursor(cursor);
        flForProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapterPriceItem.swapCursor(null);
    }

    static class CursorLoaderForItemPrice extends CursorLoader {

        private Context mContext;

        public CursorLoaderForItemPrice(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        public Cursor loadInBackground() {
            return TableResults.getPriceForClicks(DataBaseHelper.getInstance(mContext));
        }
    }

}
