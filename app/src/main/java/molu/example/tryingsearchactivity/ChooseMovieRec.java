package molu.example.tryingsearchactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.ArrayList;

import molu.example.tryingsearchactivity.RecyclerViewNaatak.RecyclerViewAdapterChoose;
import molu.example.tryingsearchactivity.databinding.ChooseMovieRecBinding;

public class ChooseMovieRec extends AppCompatActivity {
    private ChooseMovieRecBinding binding;
    private RecyclerViewAdapterChoose recyclerViewAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_movie_rec);
        binding = DataBindingUtil.setContentView(this, R.layout.choose_movie_rec);
        Intent intent = getIntent();


        ArrayList<String> movieList = intent.getStringArrayListExtra("movie_names");
        for (String movie:
             movieList) {
            Log.d("hmm", movie);
        }
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ChooseMovieRec.this));
        recyclerViewAdapter =new RecyclerViewAdapterChoose(movieList,getApplicationContext());
        binding.recyclerView.setAdapter(recyclerViewAdapter);


    }
}
