package molu.example.tryingsearchactivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {

    ArrayList<String> searchResults;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        MovieDbHelper dbHelper = new MovieDbHelper(getApplicationContext());
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchResults = dbHelper.searchMovie(query);
        }

        Intent searchIntent = new Intent(this, ChooseMovieRec.class);
        searchIntent.putStringArrayListExtra("movie_names", searchResults);
        startActivity(searchIntent);
    }
}
