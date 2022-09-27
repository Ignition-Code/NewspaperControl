package com.ms.newspapercontrol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
//        getActionBar().setDisplayShowHomeEnabled(true);
        rvItem = findViewById(R.id.rvItem);
        btSaveReturn = findViewById(R.id.btSaveReturn);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        rvItem.setAdapter(itemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == androidx.appcompat.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.add_item_menu) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.save_item, null);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            view.findViewById(R.id.btSaveItem).setOnClickListener(v -> {
                /*EditText etNewsboyName = view.findViewById(R.id.etSaveNewsboyName);
                Newsboy newsboy = new Newsboy();
                newsboy.setNewsboyName(etNewsboyName.getText().toString());
                executor.execute(() -> {
                    NewsboyDao newsboyDao = databaseController.newsboyDao();
                    newsboyDao.insertNewsboy(newsboy);
                    List<Newsboy> tmpList = newsboyDao.getAll();
                    handler.post(() -> {
                        alertDialog.dismiss();
                        runOnUiThread(() -> {
                            newsboyAdapter.setNewsboyList(tmpList);
                            newsboyAdapter.notifyItemChanged(tmpList.size() - 1);
                        });
                    });
                });*/
                runOnUiThread(() -> Toast.makeText(this, "Save item", Toast.LENGTH_SHORT).show());
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}