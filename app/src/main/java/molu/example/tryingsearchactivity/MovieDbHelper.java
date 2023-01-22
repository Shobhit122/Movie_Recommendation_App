package molu.example.tryingsearchactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION =1 ;
    public static final String DATABASE_NAME = "Movie.db";
    private final Context context;

    public MovieDbHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MovieContract.Movie.SQL_CREATE_ENTRIES);

        InputStreamReader minput = null;
        try {
            minput = new InputStreamReader(context.getAssets().open("movie_csv.csv"));
        } catch (IOException e) {
            Log.d("hmm", String.valueOf(e));
        }
        String str1 = "INSERT INTO movie_table " + "(" + MovieContract.Movie.COLUMN_NAME_MOVIE + ", " + MovieContract.Movie.COLUMN_NAME_GENRE + ") values(";
        String str2 = ");";
        BufferedReader reader = new BufferedReader(minput);
        String nextline = null;
        sqLiteDatabase.beginTransaction();
        while (true){
            try {
                if ((nextline = reader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] tokens = nextline.split(";");
            StringBuilder sb = new StringBuilder(str1);
            sb.append("'" + tokens[1] + "',");
            sb.append("'" + tokens[2] + "'");
            sb.append(str2);
            sqLiteDatabase.execSQL(sb.toString());


        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertMovieName(String movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieContract.Movie.COLUMN_NAME_MOVIE, movie);

        db.insert(MovieContract.Movie.TABLE_NAME, null, values);
    }

    public ArrayList<String> searchMovie(String searchString){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM movie_table WHERE movie_name like ?", new String[]{ "%"+ searchString + " %"} );

        ArrayList<String> movieReturn = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                movieReturn.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return movieReturn;
    }

    public String getGenre(String movieName){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT movie_genre FROM movie_table WHERE movie_name like ?", new String[]{"%" +movieName + "%"} );

        String genre = new String();
        if (cursor.moveToFirst()) {
            do {
                genre = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        Log.d("hmm", genre);
        return genre;
    }


}
