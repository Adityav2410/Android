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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mCompletionLister = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaResource();
        }
    };
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaResource();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                default:
                    break;
            }
        }
    };
    ArrayList<Word> color_array;


    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaResource();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        color_array = new ArrayList<Word>();
        color_array.add(new Word("red","weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        color_array.add(new Word("green","chokokki",R.drawable.color_green,R.raw.color_green));
        color_array.add(new Word("brown","ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        color_array.add(new Word("gray","ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        color_array.add(new Word("black","kululli",R.drawable.color_black,R.raw.color_black));
        color_array.add(new Word("white","kelelli",R.drawable.color_white,R.raw.color_white));
        color_array.add(new Word("dusty yellow","ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        color_array.add(new Word("mustard yellow","chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));

        WordAdapter wordAdapter = new WordAdapter(this, color_array,R.color.category_colors);
        ListView list_view = (ListView) findViewById(R.id.list_view);
        list_view.setAdapter(wordAdapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playMediaResource(position);
            }
        });
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private void playMediaResource(int position){
        releaseMediaResource();
        if(mAudioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) == AudioManager.AUDIOFOCUS_REQUEST_FAILED)
            return;
        Word clickedItem = color_array.get(position);
        Integer clickedAudioID = clickedItem.getAudioID();
        mMediaPlayer = MediaPlayer.create(ColorsActivity.this, clickedAudioID);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(mCompletionLister);
    }

    private void releaseMediaResource(){
        if(mMediaPlayer != null)
            mMediaPlayer.release();
        mMediaPlayer = null;
        mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
    }
}
