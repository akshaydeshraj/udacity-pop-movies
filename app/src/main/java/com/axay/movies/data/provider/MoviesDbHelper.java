package com.axay.movies.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

import static com.axay.movies.data.provider.MovieContract.MovieEntry;

/**
 * @author akshay
 * @since 7/2/16
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_MOVIES + "(" + MovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER + " TEXT , " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT , " +
                MovieEntry.COLUMN_BACKDROP + " TEXT , " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT , " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT , " +
                MovieEntry.COLUMN_POPULARITY + " TEXT , " +
                MovieEntry.COLUMN_VOTE_COUNT + " TEXT , " +
                MovieEntry.COLUMN_VIDEO + " TEXT , " +
                MovieEntry.COLUMN_ADULT + " TEXT );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.i("Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");

        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_MOVIES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieEntry.TABLE_MOVIES + "'");

        // re-create database
        onCreate(db);
    }
}
