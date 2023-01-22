package molu.example.tryingsearchactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import molu.example.tryingsearchactivity.databinding.ActivityMoviePageBinding;

public class MoviePage extends AppCompatActivity {
    private ActivityMoviePageBinding binding;
    String tmdb = "https://api.themoviedb.org/3/search/movie?api_key=c79e8ce9f231bb69a182bfb1194cf92d&language=en-US&query=";
    final String[] posterPath = new String[1];
    public int id;
    RequestQueue queue;
    private MovieDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_movie_page);
        Intent intent = getIntent();

        String movie = intent.getStringExtra("movie_name");
        dbHelper = new MovieDbHelper(getApplicationContext());



        queue = Volley.newRequestQueue(getApplicationContext());
        String year = movie.substring(movie.indexOf("(")+1, movie.indexOf(")"));
        movie = movie.replaceAll("\\(.*\\)", "");
        String genre = dbHelper.getGenre(movie.substring(1,movie.length()-1));
        binding.genre.setText(genre);
        String urlSend = null;
        try {
            URL url  = new URL(tmdb + movie+ "&year=" + year);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            urlSend = uri.toASCIIString();
        } catch (MalformedURLException | URISyntaxException e) {
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSend, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray wapas_aya = response.getJSONArray("results");
                    JSONObject obj= wapas_aya.getJSONObject(0);
                    posterPath[0] = obj.getString("poster_path");
                    Picasso.get()
                            .load("https://image.tmdb.org/t/p/original" + posterPath[0])
                            .resize(3250, 4600)
                            .centerCrop()
                            .into(binding.poster);
                    binding.Moviename.setText(obj.getString("original_title"));

                    binding.mRatings.setText(obj.getString("vote_average"));
                    binding.about.setText(obj.getString("overview"));
                    binding.releaseDate.setText(obj.getString("release_date"));
                    id = obj.getInt("id");

                } catch (JSONException e) {
                    Log.d("hmm", "nhichala");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
        //binding.time.setText(id);




    }
}