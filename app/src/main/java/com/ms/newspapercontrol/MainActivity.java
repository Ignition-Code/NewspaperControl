package com.ms.newspapercontrol;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.newspapercontrol.adapter.NewsboyAdapter;
import com.ms.newspapercontrol.entities.Newsboy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvNewsboy;
    private List<Newsboy> newsboyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvNewsboy = findViewById(R.id.rvNewsboy);
        rvNewsboy.setLayoutManager(new GridLayoutManager(this, 2));
        newsboyList = new ArrayList<>();
        newsboyList.add(new Newsboy(1L, "Nombre y apellido"));
        newsboyList.add(new Newsboy(2L, "Nombre y apellido"));
        newsboyList.add(new Newsboy(3L, "Nombre y apellido"));
        newsboyList.add(new Newsboy(4L, "Nombre y apellido"));
        newsboyList.add(new Newsboy(5L, "Nombre y apellido"));
        newsboyList.add(new Newsboy(6L, "Nombre y apellido"));

        rvNewsboy.setAdapter(new NewsboyAdapter(newsboyList));
    }
}