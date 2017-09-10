package com.example.android.android_me.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adityav on 9/6/17.
 */

public class BodyPartFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final String IMAGE_ID_LIST = "image_ids";
    public static final String List_INDEX = "list_index";

    private List<Integer> mImageIDs;
    private int mListIndex;

    public BodyPartFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_body_part,container, false);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);
        if(mImageIDs != null) {
            imageView.setImageResource( mImageIDs.get(mListIndex) );
        }else {
            Log.v(LOG_TAG,"This fragment has a null list of image id's");
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListIndex = (mListIndex+1)%(mImageIDs.size());
                imageView.setImageResource( mImageIDs.get(mListIndex));
            }
        });

        return rootView;
    }

    public void setmImageIDs(List<Integer> imageIDs){
        mImageIDs = imageIDs;
    }

    public void setListIndex(int index){
        mListIndex = index;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putIntegerArrayList(IMAGE_ID_LIST, (ArrayList<Integer>) mImageIDs);
        currentState.putInt(List_INDEX, mListIndex);
    }

}
