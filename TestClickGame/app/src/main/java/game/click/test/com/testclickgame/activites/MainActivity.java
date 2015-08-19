package game.click.test.com.testclickgame.activites;

import android.app.Activity;
import android.os.Bundle;

import game.click.test.com.testclickgame.R;
import game.click.test.com.testclickgame.fragments.GameFragment;

/**
 * Created by kirichenko on 18.08.2015.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity_layout);

        if (savedInstanceState == null){
            GameFragment oGameFragment = new GameFragment();
            android.app.FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.mainContainer, oGameFragment)
                    .commit();
        }


    }
}
