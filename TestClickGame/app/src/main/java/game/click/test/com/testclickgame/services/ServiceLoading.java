package game.click.test.com.testclickgame.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import game.click.test.com.testclickgame.data_base_helper.tables.action_table_price.LoadingTableResult;
import game.click.test.com.testclickgame.fragments.ICallBackFromServer;
import game.click.test.com.testclickgame.logical.LogicalGame;

public class ServiceLoading extends Service {

    public static String LOG_TAG = "ServiceLoading";

    private ServiceBinder serviceBinder = new ServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void loadScoreForGame(LogicalGame oLogicalGame, ICallBackFromServer iCallBackFromServer){
        new LoadingTableResult(oLogicalGame, iCallBackFromServer).startLoadSource(getBaseContext());
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(LOG_TAG, LOG_TAG + " onRebind");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, LOG_TAG + " onBind");
        return serviceBinder;
    }

    public class ServiceBinder extends Binder {
        public ServiceLoading getService() {
            return ServiceLoading.this;
        }
    }
}
