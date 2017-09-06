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
package com.example.android.pets;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetProvider;

import java.util.List;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView pet_list_view;
    View emptyView;
    private final static int PetLoader = 0;
    PetAdapter mPetCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        pet_list_view = (ListView) findViewById(R.id.pet_list_view);
        emptyView = findViewById(R.id.empty_view);
        pet_list_view.setEmptyView(emptyView);

        mPetCursorAdapter = new PetAdapter(this, null);
        pet_list_view.setAdapter(mPetCursorAdapter);
        getLoaderManager().initLoader(PetLoader,null,this).forceLoad();

        pet_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                intent.setData( ContentUris.withAppendedId(PetEntry.CONTENT_URI,id ) );
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    private void insertPet(){
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "DUMMY_PET");
        values.put(PetEntry.COLUMN_PET_BREED, "DUMMY_BREED");
        values.put(PetEntry.COLUMN_PET_WEIGHT, 5);
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);

        Uri insertUri   =   getContentResolver().insert(PetEntry.CONTENT_URI,values);
        long newRowID    =   ContentUris.parseId(insertUri);
        Toast.makeText(this, "Pet saved", Toast.LENGTH_SHORT).show();
        Log.i("Catalog Activity","New row ID: " + newRowID);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                int nPetDeleted = getContentResolver().delete(PetEntry.CONTENT_URI,null,null);
                if( nPetDeleted == 0)
                    Toast.makeText(this,"Pet table could not be deleted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"Pet table deleted",Toast.LENGTH_SHORT).show();
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        // Create and/or open a database to read from it


        String []projection = {PetEntry._ID, PetEntry.COLUMN_PET_NAME,PetEntry.COLUMN_PET_BREED, PetEntry.COLUMN_PET_GENDER,PetEntry.COLUMN_PET_WEIGHT};
        String Selection =  PetEntry.COLUMN_PET_GENDER + " =? "; // +
//                            PetEntry.COLUMN_PET_WEIGHT + " >? ";
        String []SelectionArgs = { ""+PetEntry.GENDER_MALE} ; //, "0" };
        String sortOrder = PetEntry.COLUMN_PET_WEIGHT + " DESC";
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
//        Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);

        Cursor cursor = getContentResolver().query(Uri.parse("content://com.example.android.pets/pets/"), projection, null, null, null);

        PetAdapter petAdapter = new PetAdapter(this, cursor);
        pet_list_view.setAdapter(petAdapter);

//        String s = "";
//        while( cursor.moveToNext()){
//            s += cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME)) + "-";
//            s += cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED)) + "-";
//            s += cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER)) + "-";
//            s += cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT));
//            s += "\n";
//        }
//        try {
//            // Display the number of rows in the Cursor (which reflects the number of rows in the
//            // pets table in the database).
//            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
//            displayView.setText("Number of rows in pets database table: " + cursor.getCount() + "\n\n\n\n\n" + s );
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String []projection = { PetEntry._ID,
                                PetEntry.COLUMN_PET_NAME,
                                PetEntry.COLUMN_PET_BREED};
        String Selection =  PetEntry.COLUMN_PET_GENDER + " =? ";
        String []SelectionArgs = { ""+PetEntry.GENDER_MALE} ;
        String sortOrder = PetEntry.COLUMN_PET_WEIGHT + " DESC";


        return new CursorLoader(this,PetEntry.CONTENT_URI,
                                projection,
                                null,
                                null,
                                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPetCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPetCursorAdapter.swapCursor(null);
    }
}
