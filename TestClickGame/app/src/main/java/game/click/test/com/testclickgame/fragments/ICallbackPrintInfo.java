package game.click.test.com.testclickgame.fragments;

/**
 * Created by kirichenko on 19.08.2015.
 */
public interface ICallbackPrintInfo {

    void printInformation(int iCurrentLvl, int iCurrentScore, int iPriceForClick);

    /**
     * Return information about count lvl
     * @param iSize Count of lvl
     */
    void callBackAboutSizeLvl(int iSize);
}
