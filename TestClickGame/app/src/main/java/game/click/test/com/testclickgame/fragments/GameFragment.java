package game.click.test.com.testclickgame.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import game.click.test.com.testclickgame.R;
import game.click.test.com.testclickgame.logical.LogicalGame;
import game.click.test.com.testclickgame.services.ServiceLoading;

/**
 * Created by kirichenko on 18.08.2015.
 */
public class GameFragment extends Fragment implements ICallbackPrintInfo, ICallBackFromServer{

    private TextView tvValueScore, tvValueLvl;
    private Button btnHelp, btnClickMe;

    private FrameLayout flSpaceForScore;
    private FrameLayout flForProgressBar;

    private Animation oAnimationScore;

    private LogicalGame oLogicalGame;

    private boolean bTryReloadData = false;

    private ServiceConnection sConnectionChecking;
    private Intent intentCheckingData;
    private Boolean bConnectToService = false;
    private ServiceLoading oServiceLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.game_fragment, null);

        getAllElementsFromFragment(oView);

        if(getoLogicalGame() == null){
            oLogicalGame = new LogicalGame(getActivity());
        }

        flForProgressBar.setVisibility(View.VISIBLE);
        flForProgressBar.bringToFront();

        oLogicalGame.startLoadInformation(this);

        return oView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(getoLogicalGame() != null){
            getoLogicalGame().saveCurrentInformation();
        }

        if (!bConnectToService){
            return;
        }else{

            getActivity().unbindService(sConnectionChecking);
            bConnectToService = false;

        }

    }

    private void getAllElementsFromFragment(View oView) {

        tvValueScore = (TextView) oView.findViewById(R.id.tvValueScore);
        tvValueLvl = (TextView) oView.findViewById(R.id.tvValueLvl);

        btnHelp = (Button) oView.findViewById(R.id.btnHelp);
        btnClickMe = (Button) oView.findViewById(R.id.btnClickMe);

        flSpaceForScore = (FrameLayout) oView.findViewById(R.id.flSpaceForScore);
        flForProgressBar = (FrameLayout) oView.findViewById(R.id.flProgressBar);

        addAllListeners();

    }

    private void addAllListeners() {

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HelpFragment oHelpFragment = new HelpFragment();
                android.app.FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, oHelpFragment)
                        .addToBackStack("fragmentStack")
                        .commit();

            }
        });

        btnClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegateActionClick();
            }
        });

    }

    private void connectionToService(){

        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

        sConnectionChecking = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
                bConnectToService = true;

                if(getoLogicalGame()!= null){
                    delegateStartLoadFromServer();
                }

            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToService = false;
            }
        };

        getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);

    }

    /**
     * Function creates animation with adding value for score
     */
    private void createNewAddValue(int iCostForClick) {

        Activity oActivity = getActivity();

        if (oActivity == null) {
            return;
        }

        if (iCostForClick == -1){
            finishDialog();
            return;
        }

        setoAnimationScore(AnimationUtils.loadAnimation(oActivity, R.anim.score_animation));

        TextView tvAddScoreValue = new TextView(oActivity);

        tvAddScoreValue.setText("+" + iCostForClick);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);


        params.setMargins(returnNewLeftMargin(tvAddScoreValue), flSpaceForScore.getHeight() / 2, 0, 0);

        tvAddScoreValue.startAnimation(getoAnimationScore());

        flSpaceForScore.addView(tvAddScoreValue, params);

        removeViewAfterDisplaying(tvAddScoreValue);
    }

    private void finishDialog(){

        if(getActivity() != null){
            AlertDialog.Builder finishDialog = new AlertDialog.Builder(getActivity());

            finishDialog.setTitle(R.string.you_winner);
            finishDialog.setMessage(R.string.want_play_again);

            finishDialog.setPositiveButton(R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.game_was_started,
                            Toast.LENGTH_LONG).show();

                    delegateRestart();

                }
            });

            finishDialog.setNegativeButton(R.string.no, new OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.game_was_not_started,
                            Toast.LENGTH_LONG).show();
                }
            });

            finishDialog.setCancelable(false);

            finishDialog.show();
        }

    }


    public boolean isbTryReloadData() {
        return bTryReloadData;
    }

    public void setbTryReloadData(boolean bTryReloadData) {
        this.bTryReloadData = bTryReloadData;
    }

    private void delegateActionClick(){
        createNewAddValue(getoLogicalGame().actionClick(this));
    }

    private void delegateRestart(){
        if(getoLogicalGame() != null){
            getoLogicalGame().restartGame(this);
        }
    }

    private void delegateStartLoadFromServer(){
        if(oServiceLoading != null){
            oServiceLoading.loadScoreForGame(getoLogicalGame(), this);
        }
    }

    private void delegateLoadCurrentState(){
        getoLogicalGame().loadCurrentInformation(this);
    }

    private void removeViewAfterDisplaying(final TextView tvForRemove) {
        getoAnimationScore().setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation paramAnimation) {
            }

            public void onAnimationRepeat(Animation paramAnimation) {
            }

            public void onAnimationEnd(Animation paramAnimation) {

                flSpaceForScore.post(new Runnable() {
                    public void run() {

                        Activity activity = getActivity();

                        if(activity != null){
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    flSpaceForScore.removeView(tvForRemove);
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    /**
     * @param tvAddScoreValue TextView for display
     * @return Left margin for this TextView
     */
    private int returnNewLeftMargin(TextView tvAddScoreValue) {

        int iRandValue = new Random().nextInt(flSpaceForScore.getWidth() - tvAddScoreValue.getWidth());

        if (iRandValue < (tvAddScoreValue.getWidth() / 2)) {
            iRandValue = tvAddScoreValue.getWidth() / 2;
        }

        return iRandValue;
    }

    public LogicalGame getoLogicalGame() {
        return oLogicalGame;
    }

    public Animation getoAnimationScore() {
        return oAnimationScore;
    }

    public void setoAnimationScore(Animation oAnimationScore) {
        this.oAnimationScore = oAnimationScore;
    }

    @Override
    public void printInformation(int iCurrentLvl, int iCurrentScore, int iPriceForClick) {

        tvValueScore.setText(iCurrentScore + "");
        tvValueLvl.setText(iCurrentLvl + "");
        btnClickMe.setText("+" + iPriceForClick);

    }

    @Override
    public void callBackAboutSizeLvl(int iSize) {

        if(iSize == 0){
            /**
             * Fix for not looping
             */
            if(!isbTryReloadData()){
                connectionToService();
                setbTryReloadData(true);
            }
        }else{
            if(getoLogicalGame() != null){
                delegateLoadCurrentState();
                flForProgressBar.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void loadComplete() {
        if(getoLogicalGame() != null){
            getoLogicalGame().initLoadFromDataBase();
        }
    }
}
