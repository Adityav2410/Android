package com.example.android.quakereport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.android.quakereport.R.id.time;

/**
 * Created by adityav on 8/21/17.
 */

public class EqInfo {
    private Double  mMagnitude;
    private String  mCityPrimary;
    private String  mCitySecondary;
    private String  mTime;
    private String  mDate;
    private String  mEqUrl;

    public EqInfo(Double magnitude, String citySecondary, String cityPrimary, String date, String time, String eq_url){
        mMagnitude      =   magnitude;
        mCityPrimary    =   cityPrimary;
        mCitySecondary  =   citySecondary;
        mTime           =   time;
        mDate           =   date;
        mEqUrl          =   eq_url;
    }

    public Double getMagnitude(){
        return(mMagnitude);
    }

    public String getCityPrimary(){
        return(mCityPrimary);
    }

    public String getmCitySecondary(){
        return(mCitySecondary);
    }

    public String getTime(){
        return(mTime);
    }

    public String getDate(){
        return(mDate);
    }

    public String getUrl(){
        return(mEqUrl);
    }
}
