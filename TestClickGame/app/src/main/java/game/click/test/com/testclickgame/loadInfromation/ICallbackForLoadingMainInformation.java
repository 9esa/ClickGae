package game.click.test.com.testclickgame.loadInfromation;


import org.json.JSONArray;

public interface ICallbackForLoadingMainInformation {

    // Callback with returned information
    void getLoadedInformation(JSONArray oJsonArray, Object identificationNumber);

}
