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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaResource();
        }
    };
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
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
    ArrayList<Word> phrase_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        phrase_array = new ArrayList<Word>();
        phrase_array.add(new Word("Where are you going?","minto wuksus",R.raw.phrase_where_are_you_going));
        phrase_array.add(new Word("What is your name?","tinnә oyaase'nә",R.raw.phrase_what_is_your_name));
        phrase_array.add(new Word("My name is...","oyaaset...",R.raw.phrase_my_name_is));
        phrase_array.add(new Word("How are you feeling?","michәksәs?",R.raw.phrase_how_are_you_feeling));
        phrase_array.add(new Word("I’m feeling good.","kuchi achit",R.raw.phrase_im_feeling_good));
        phrase_array.add(new Word("Are you coming?","әәnәs'aa?",R.raw.phrase_are_you_coming));
        phrase_array.add(new Word("Yes, I’m coming.","hәә’ әәnәm",R.raw.phrase_yes_im_coming));
        phrase_array.add(new Word("I’m coming.","әәnәm",R.raw.phrase_im_coming));
        phrase_array.add(new Word("Let’s go.","yoowutis",R.raw.phrase_lets_go));
        phrase_array.add(new Word("Come here.","әnni'nem",R.raw.phrase_come_here));
        WordAdapter wordAdapter = new WordAdapter(this, phrase_array,R.color.category_phrases);
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

    public void playMediaResource(int position){
        releaseMediaResource();
        if(mAudioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) == AudioManager.AUDIOFOCUS_REQUEST_FAILED)
            return;
        Word clickedItem = phrase_array.get(position);
        Integer clickedAudioID = clickedItem.getAudioID();
        mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, clickedAudioID);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
    }

    private void releaseMediaResource(){
        if( mMediaPlayer != null )
            mMediaPlayer.release();
        mMediaPlayer = null;
        mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaResource();
    }

    @Override
    public String toString() {
        return "PhrasesActivity{" +
                "mMediaPlayer=" + mMediaPlayer +
                '}';
    }
}
