package com.ms.newspapercontrol;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.newspapercontrol.adapter.ItemAdapter;
import com.ms.newspapercontrol.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemViewActivity extends AppCompatActivity {

    private Button btSaveReturn;
    private RecyclerView rvItem;

    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        rvItem = findViewById(R.id.rvItem);
        btSaveReturn = findViewById(R.id.btSaveReturn);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        rvItem.setAdapter(itemAdapter);
    }
}