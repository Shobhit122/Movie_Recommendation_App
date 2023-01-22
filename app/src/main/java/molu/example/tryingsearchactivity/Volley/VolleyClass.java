package molu.example.tryingsearchactivity.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class VolleyClass {
    private Context context;

    public VolleyClass(Context context) {
        this.context = context;
    }
    RequestQueue queue;
    final String[] posterPath = new String[1];
    String tmdb = "https://api.themoviedb.org/3/search/movie?api_key=c79e8ce9f231bb69a182bfb1194cf92d&language=en-US&query=";
    String poster;

    // implementation of ResponseListener
    public String getPosterPath(String movieName){
        queue = Volley.newRequestQueue(context);
        String year = movieName.substring(movieName.indexOf("(")+1, movieName.indexOf(")"));
        movieName = movieName.replaceAll("\\(.*\\)", "");
        String urlSend = null;
        try {
            URL url  = new URL(tmdb + movieName+ "&year=" + year);
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
        Log.d("hmm", " path:" + poster);
        return poster;

    }
}
