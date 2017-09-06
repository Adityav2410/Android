package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.android.pets.data.PetContract.PetEntry.CONTENT_URI;
import static com.example.android.pets.data.PetContract.PetEntry.isValidGender;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    /** Database Helper object **/
    private PetDbHelper mDbHelper;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    private static final int PETS = 100;
    private static final int PET_ID = 101;

    static{
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID);
    }



    /** Tag for the log messages */

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());

        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    /**
     * Check the type of URI . whether to search from whole table or some particular row
     */

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        final int match = sUriMatcher.match(uri);
        Log.d(LOG_TAG,"Match value = " + match);
        switch(match){
            case PETS:
                cursor = db.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null , sortOrder);
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String [] {String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    public Uri insertPet(Uri uri, ContentValues contentValues){
        String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
        if( name.equals("") )
            return(null);  //throw new IllegalArgumentException("Pet requires a name");

        String breed = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_BREED);
            if(breed.equals(""))
                return(null); //throw new IllegalArgumentException("Pet breed should be provided");

        Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
        if( gender == null || !isValidGender(gender) )
            return(null);//throw new IllegalArgumentException("Pet requires valid gender");

        Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if( weight == null || weight < 0 )
            return null; //throw new IllegalArgumentException("Pet requires valid weight");

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(PetContract.PetEntry.TABLE_NAME,null, contentValues);

        if( id == -1){
            Log.e(LOG_TAG,"Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not correct for the uri " + uri);
        }
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */

    public int updatePet(Uri uri, ContentValues contentValues, String selection, String [] selectionArgs){

        if( contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_NAME)){
            String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
            if( name.equals("") )
                    return(0); //throw new IllegalArgumentException("Pet name should be provided");
        }
        if( contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_BREED)){
            String breed = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_BREED);
            if(breed.equals(""))
                return(0);  //throw new IllegalArgumentException("Pet breed should be provided");
        }
        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !isValidGender(gender))
                return(0);  //throw new IllegalArgumentException("Pet gender should be valid");
        }
        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            if (weight == null || weight < 0)
                return(0);  //throw new IllegalArgumentException("Pet weight should be valid");
        }

        if(contentValues.size() == 0 )
            return 0;

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int numRowUpdated = database.update(PetContract.PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if( numRowUpdated != 0 )
            getContext().getContentResolver().notifyChange(uri, null);
        return numRowUpdated;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case PETS:
                return updatePet(uri,contentValues, selection, selectionArgs);
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri,contentValues, selection, selectionArgs);
            default:
                break;
        }
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    private int deletePet(Uri uri, String selection, String [] selectionArgs){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int nRowDeleted = database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
        return(nRowDeleted);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        switch(match){
            case PETS:
                rowsDeleted =  deletePet(uri, selection, selectionArgs);
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted =  deletePet(uri, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI provided");
        }
        if( rowsDeleted != 0 )
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch(match){
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Invalid URI " + uri + " with match " + match );
        }
    }
}