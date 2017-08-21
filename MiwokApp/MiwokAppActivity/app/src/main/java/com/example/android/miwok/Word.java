package com.example.android.miwok;
import android.content.Context;

/**
 * Created by adityav on 8/18/17.
 */

public class Word {

    private String mDefaultTranslation ;
    private String mMiwokTranslation;
    private final Integer NO_IMAGE_ID = -1;
    private Integer mImageResourceId = NO_IMAGE_ID;
    private Integer mAudioId = 0;


    public Word(String defaultTranslation, String miwokTranslation, Integer audioID) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioId  = audioID;
    }

    public Word(String defaultTranslation, String miwokTranslation, Integer imageResourceId, Integer audioID)
    {
        mDefaultTranslation =   defaultTranslation;
        mMiwokTranslation   =   miwokTranslation;
        mImageResourceId    =   imageResourceId;
        mAudioId            =   audioID;
    }

//    Getter methods
    public String getDefaultTranslation(){
        return(mDefaultTranslation);
    }

    public String getMiwokTranslation(){
        return(mMiwokTranslation);
    }

    public Integer getImageResourceID(){
        return(mImageResourceId);
    }

    public Integer getAudioID(){
        return(mAudioId);
    }

    public boolean hasImage(){
        return(mImageResourceId != NO_IMAGE_ID );
    }
}
