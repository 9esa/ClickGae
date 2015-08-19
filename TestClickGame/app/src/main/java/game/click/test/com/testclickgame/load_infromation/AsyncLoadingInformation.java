package game.click.test.com.testclickgame.load_infromation;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

public class AsyncLoadingInformation extends Thread implements Runnable {

    private static String LOG_TAG = "ASYNC_LOADING_INFO";
    private String sRequest;
    private ICallbackForLoadingMainInformation iCallBackWithInformation;

    private Object iIdentificationNumber;

    public AsyncLoadingInformation(String sRequest, ICallbackForLoadingMainInformation callBackWithInformation, Object iIdentificationNumber) {
        this.iCallBackWithInformation = callBackWithInformation;
        this.sRequest = sRequest;
        this.iIdentificationNumber = iIdentificationNumber;
    }

    @Override
    public void run() {

        JSONArray oJsonArray = null;

        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(sRequest);

            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpGet);

            String sResponseJson = EntityUtils.toString(httpResponse.getEntity());
            oJsonArray = new JSONArray(sResponseJson);

        } catch (Exception e) {
            Log.d(LOG_TAG, "Error in reading response");
        }

        if (iCallBackWithInformation != null) {
            iCallBackWithInformation.getLoadedInformation(oJsonArray, iIdentificationNumber);
        }

    }
}
