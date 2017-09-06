package com.example.android.pets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by adityav on 9/4/17.
 */

public class PetAdapter extends CursorAdapter {

    public PetAdapter(Context context, Cursor cursor){
        super(context, cursor,0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.pet_item_view,parent,false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_petName = (TextView) view.findViewById(R.id.tv_pet_name);
        TextView tv_petBreed = (TextView) view.findViewById(R.id.tv_pet_breed);

        String petName = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME));
        String petBreed = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED));

        tv_petName.setText(petName);
        tv_petBreed.setText(petBreed);
    }
}
