/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener;
    private MediaPlayer.OnCompletionListener mCompletionLister = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaResource();
        }
    };
    protected ArrayList<Word> number_array ;

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaResource();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

//      Create object with defaultTranslation and miwokTranslation of numbers
        Log.v("Numbers Activity","Declaring English words array");
        number_array = new ArrayList<Word>();
        number_array.add(new Word("one","lutti",R.drawable.number_one,R.raw.number_one));
        number_array.add(new Word("two","otiiko",R.drawable.number_two,R.raw.number_two));
        number_array.add(new Word("three","tolookosu",R.drawable.number_three,R.raw.number_three));
        number_array.add(new Word("four","oyyisa",R.drawable.number_four,R.raw.number_four));
        number_array.add(new Word("five","massokka",R.drawable.number_five,R.raw.number_five));
        number_array.add(new Word("six","temmokka",R.drawable.number_six,R.raw.number_six));
        number_array.add(new Word("seven","kenekaku",R.drawable.number_seven,R.raw.number_seven));
        number_array.add(new Word("eight","kawinta",R.drawable.number_eight,R.raw.number_eight));
        number_array.add(new Word("nine","wo’e",R.drawable.number_nine,R.raw.number_nine));
        number_array.add(new Word("ten","na’aacha",R.drawable.number_ten,R.raw.number_ten));

        WordAdapter number_adapter = new WordAdapter(this,number_array,R.color.category_numbers);
        ListView list_view = (ListView) findViewById(R.id.list_view);
        list_view.setAdapter(number_adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playMediaResource(position);
            }
        });
        mAudioManager = getAudioManager(NumbersActivity.this);

    }

    private AudioManager getAudioManager(Context c){
        mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener(){
            public void onAudioFocusChange(int focusChange){
                switch(focusChange){
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        mMediaPlayer.pause();   mMediaPlayer.seekTo(0);   break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        releaseMediaResource();   break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        mMediaPlayer.pause();   mMediaPlayer.seekTo(0);   break;
                    case AudioManager.AUDIOFOCUS_GAIN:
                        break;
                    default:
                        break;
                }
            }
        };
        return (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
    }

    public void playMediaResource(int position){
        releaseMediaResource();
        if( mAudioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) == AudioManager.AUDIOFOCUS_REQUEST_FAILED )
            return;
        Word clickedItem = number_array.get(position);
        Integer clickedAudioID = clickedItem.getAudioID();
        mMediaPlayer = MediaPlayer.create(NumbersActivity.this, clickedAudioID);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(mCompletionLister);
    }

    private void releaseMediaResource(){
        if(mMediaPlayer != null)
            mMediaPlayer.release();
        mMediaPlayer = null;
        mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
    }

    @Override
    public String toString() {
        return "NumbersActivity{" +
                "mMediaPlayer=" + mMediaPlayer +
                '}';
    }
}

