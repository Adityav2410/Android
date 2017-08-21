package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adityav on 8/18/17.
 */
public class WordAdapter extends ArrayAdapter<Word> {

    private Integer mTextBackgroundColor;

    public WordAdapter(Activity context, ArrayList<Word> wordList, Integer backgroundColor){
        super(context,0,wordList);
        mTextBackgroundColor = backgroundColor;
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView    =   convertView;
        if( listItemView == null )
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.text_view_2,parent, false);

        Word currentWord                    =   getItem(position);
        TextView defaultTranslationView     =   (TextView) listItemView.findViewById(R.id.defaultTranslation);
        TextView miwokTranslationView       =   (TextView) listItemView.findViewById(R.id.miwokTranslation);
        ImageView imageResourceID           =   (ImageView) listItemView.findViewById(R.id.imageResourceID);
        RelativeLayout list_item_text         =   (RelativeLayout) listItemView.findViewById(R.id.list_item_text);


        list_item_text.setBackgroundColor(ContextCompat.getColor(getContext(),mTextBackgroundColor));

        defaultTranslationView.setText(currentWord.getDefaultTranslation());
        miwokTranslationView.setText(currentWord.getMiwokTranslation());
        if(currentWord.hasImage() ) {
            imageResourceID.setImageResource(currentWord.getImageResourceID());
            imageResourceID.setVisibility(View.VISIBLE);
        }
        else
            imageResourceID.setVisibility(View.GONE);

//        final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), currentWord.getAudioID());
//        ImageView btnPlay = (ImageView) listItemView.findViewById(R.id.btnPlay);
//        btnPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mediaPlayer.start();
//            }
//        });

        return(listItemView);
    }
}
