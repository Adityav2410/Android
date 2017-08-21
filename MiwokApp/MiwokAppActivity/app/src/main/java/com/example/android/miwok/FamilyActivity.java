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

import static com.example.android.miwok.R.color.category_family;

public class FamilyActivity extends AppCompatActivity {

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
    private ArrayList<Word> family_array;

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaResource();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        family_array= new ArrayList<Word>();
        family_array.add(new Word("father","әpә",R.drawable.family_father,R.raw.family_father));
        family_array.add(new Word("mother","әṭa",R.drawable.family_mother,R.raw.family_mother));
        family_array.add(new Word("son","angsi",R.drawable.family_son,R.raw.family_son));
        family_array.add(new Word("daughter","tune",R.drawable.family_daughter,R.raw.family_daughter));
        family_array.add(new Word("older brother","taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
        family_array.add(new Word("younger brother","chalitti",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        family_array.add(new Word("older sister","teṭe",R.drawable.family_older_sister,R.raw.family_older_sister));
        family_array.add(new Word("younger sister","kolliti",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        family_array.add(new Word("grandmother","ama",R.drawable.family_grandmother,R.raw.family_grandmother));
        family_array.add(new Word("grandfather","paapa",R.drawable.family_grandfather,R.raw.family_grandfather));

        WordAdapter wordAdapter = new WordAdapter(this, family_array,R.color.category_family);
        ListView list_view = (ListView) findViewById(R.id.list_view);
//        list_view.setBackground(R.color.category_numbers);
        list_view.setAdapter(wordAdapter);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                playMediaResource(position);
            }
        });
    }

    void playMediaResource(int position){
        releaseMediaResource();
        if( mAudioManager.requestAudioFocus(mAudioFocusChangeListener,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) == AudioManager.AUDIOFOCUS_REQUEST_FAILED)
            return;
        Word clickedItem = family_array.get(position);
        Integer clickedAudioId = clickedItem.getAudioID();
        mMediaPlayer = MediaPlayer.create(FamilyActivity.this, clickedAudioId);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
    }

    private void releaseMediaResource()
    {
        if( mMediaPlayer != null)
            mMediaPlayer.release();
        mMediaPlayer = null;
        mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
    }
}
