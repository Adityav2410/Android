package com.example.android.android_me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

public class MainActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener{

    private final String LOG_TAG = this.getClass().getSimpleName();
    int headIndex = 0;
    int bodyIndex = 0;
    int legIndex = 0;

    private boolean mTwoPane = false;

    @Override
    public void onImageSelected(int position) {
        Toast.makeText(this, "Image selected at position " + position,Toast.LENGTH_SHORT).show();

        int bodyPartIndex = position/12;
        int itemIndex = position%12;

        if( mTwoPane){

            BodyPartFragment newFragment = new BodyPartFragment();
            switch (bodyPartIndex) {
                case 0:
                    newFragment.setmImageIDs(AndroidImageAssets.getHeads());
                    newFragment.setListIndex(itemIndex);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.head_container, newFragment)
                            .commit();
                    break;
                case 1:
                    newFragment.setmImageIDs(AndroidImageAssets.getBodies());
                    newFragment.setListIndex(itemIndex);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.body_container, newFragment)
                            .commit();

                    bodyIndex = itemIndex;
                    break;
                case 2:
                    newFragment.setmImageIDs(AndroidImageAssets.getLegs());
                    newFragment.setListIndex(itemIndex);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.leg_container, newFragment)
                            .commit();
                    break;
            }


        } else {

            switch (bodyPartIndex) {
                case 0:
                    headIndex = itemIndex;
                    break;
                case 1:
                    bodyIndex = itemIndex;
                    break;
                case 2:
                    legIndex = itemIndex;
                    break;
            }
            Bundle b = new Bundle();
            b.putInt("HeadIndex", headIndex);
            b.putInt("BodyIndex", bodyIndex);
            b.putInt("LegIndex", legIndex);
            final Intent intent = new Intent(this, AndroidMeActivity.class);
            intent.putExtras(b);

            Button btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( findViewById(R.id.android_me_linear_layout) != null ) {
            mTwoPane = true;

            Button btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setVisibility(View.GONE);

            GridView gridView = (GridView) findViewById(R.id.images_grid_view);
            gridView.setNumColumns(2);

            if(savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Create head fragment
                BodyPartFragment headFragment = new BodyPartFragment();
                headFragment.setmImageIDs(AndroidImageAssets.getHeads());
                fragmentManager.beginTransaction()
                                .add(R.id.head_container, headFragment)
                                .commit();

                // Create body fragment
                BodyPartFragment bodyFragment = new BodyPartFragment();
                headFragment.setmImageIDs(AndroidImageAssets.getBodies());
                fragmentManager.beginTransaction()
                        .add(R.id.body_container, bodyFragment)
                        .commit();

                // Create leg fragment
                BodyPartFragment legFragment = new BodyPartFragment();
                headFragment.setmImageIDs(AndroidImageAssets.getLegs());
                fragmentManager.beginTransaction()
                        .add(R.id.leg_container, legFragment)
                        .commit();

            }
        } else {
            mTwoPane = false;
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



    }

}
