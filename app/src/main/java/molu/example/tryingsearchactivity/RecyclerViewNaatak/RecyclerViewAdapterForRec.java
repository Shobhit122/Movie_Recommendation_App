package molu.example.tryingsearchactivity.RecyclerViewNaatak;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;

import molu.example.tryingsearchactivity.MoviePage;
import molu.example.tryingsearchactivity.R;
import molu.example.tryingsearchactivity.Volley.VolleyClass;

public class RecyclerViewAdapterForRec extends RecyclerView.Adapter<RecyclerViewAdapterForRec.ViewHolderForRec> {

    private ArrayList<String> movieArray;
    private Context context;
    private RequestQueue queue;
    String tmdb = "https://api.themoviedb.org/3/search/movie?api_key=c79e8ce9f231bb69a182bfb1194cf92d&language=en-US&query=";
    final String[] posterPath = new String[1];

    int id;

    public RecyclerViewAdapterForRec(ArrayList<String> movieArray, Context context) {
        this.movieArray = movieArray;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterForRec.ViewHolderForRec onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_rec_row,parent, false);
        return new ViewHolderForRec(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterForRec.ViewHolderForRec holder, int position) {
        String movie = movieArray.get(position);
        holder.title.setText(movie);

        queue = Volley.newRequestQueue(context);
        String year = movie.substring(movie.indexOf("(")+1, movie.indexOf(")"));
        movie = movie.replaceAll("\\(.*\\)", "");

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
                            .resize(160, 190)
                            .centerCrop()
                            .into(holder.poster);

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoviePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("movie_name", holder.title.getText());
                holder.itemView.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return movieArray.size();
    }

    public class ViewHolderForRec extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView poster;
        public ViewHolderForRec(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_rec);
            poster = itemView.findViewById(R.id.poster_rec);
        }
    }
}