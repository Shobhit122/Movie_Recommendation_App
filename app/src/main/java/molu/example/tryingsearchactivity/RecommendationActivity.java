package molu.example.tryingsearchactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import molu.example.tryingsearchactivity.RecyclerViewNaatak.RecyclerViewAdapterForRec;
import molu.example.tryingsearchactivity.databinding.ActivityTestBinding;

public class RecommendationActivity extends AppCompatActivity {
    private RecyclerViewAdapterForRec recyclerViewAdapterForRec;
    private ActivityTestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RecommendationActivity.this, R.layout.activity_test);

        Intent intent = getIntent();
        String movieRec = intent.getStringExtra("movie_name");

        RequestQueue queue = Volley.newRequestQueue(this);

        HashMap data = new HashMap();

        if(movieRec.charAt(0)=='\"'){
            movieRec = movieRec.substring(1, movieRec.length() - 1);
        }
        data.put("movie_title", movieRec);
        String urlRec = "http://192.168.1.4:80/recms";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlRec,
                new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        
                        try {
                            binding.loadingPanel.setVisibility(View.GONE);
                            String str = response.getString("rec_movie");
                            str = str.replaceAll(", ", ";");
                            str = str.substring(1, str.length()-1);
                            //split the string into an array
                            String[] strArray = str.split(",");
                            ArrayList<String> movieArray = new ArrayList<>();
                            for (String string:
                                 strArray) {
                                string = string.replaceAll(";", ", ");
                                movieArray.add(string);
                            }
                            binding.recyclerViewRec.setHasFixedSize(true);
                            binding.recyclerViewRec.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            recyclerViewAdapterForRec = new RecyclerViewAdapterForRec(movieArray, getApplicationContext());
                            binding.recyclerViewRec.setAdapter(recyclerViewAdapterForRec);


                        } catch (JSONException e) {
                            Log.d("hmm", e.toString());
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("hmm", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        queue.add(jsonObjectRequest);

    }
}