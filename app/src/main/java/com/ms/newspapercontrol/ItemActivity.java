package com.ms.newspapercontrol;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ms.newspapercontrol.adapter.ItemAdapter;
import com.ms.newspapercontrol.controller.DatabaseController;
import com.ms.newspapercontrol.dao.ItemDao;
import com.ms.newspapercontrol.entities.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemActivity extends AppCompatActivity {

    private DatabaseController databaseController;
    private final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final Handler HANDLER = new Handler(Looper.getMainLooper());
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        RecyclerView rvItem = findViewById(R.id.rvItemList);
        this.itemList = new ArrayList<>();
        this.itemAdapter = new ItemAdapter(this.itemList);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.setAdapter(itemAdapter);
        databaseController = Room.databaseBuilder(getApplicationContext(), DatabaseController.class, "newsboy-application").build();
        getItemList();
    }

    /**
     * List of saved item
     */
    private void getItemList() {
        EXECUTOR.execute(() -> {
            ItemDao itemDao = databaseController.itemDao();
            itemList = itemDao.findByStatus(1);
            itemAdapter.setItemList(itemList);
            itemAdapter.notifyItemRangeInserted(0, itemList.size() - 1);
        });
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
                EditText etNameSaveItem = view.findViewById(R.id.etNameSaveItem);
                CheckBox cbCollectableSaveItem = view.findViewById(R.id.cbCollectableSaveItem);
                Item itemToSave = new Item();
                String itemName = etNameSaveItem.getText().toString();
                itemToSave.setItemName(itemName);

                /*int itemNewsboyPrice, itemDealerPrice;
                if (itemName.equalsIgnoreCase("extra") || itemName.equalsIgnoreCase("popular")) {
                    if (itemName.equalsIgnoreCase("popular")) {
                        if (itemPrice == 3000) {
                            itemNewsboyPrice = 2200;
                            itemDealerPrice = 1750;
                        } else {
                            itemNewsboyPrice = 2500;
                            itemDealerPrice = 1950;
                        }
                    } else {
                        if (itemPrice == 3000) {
                            itemNewsboyPrice = 2000;
                            itemDealerPrice = 1750;
                        } else {
                            itemNewsboyPrice = 3000;
                            itemDealerPrice = 2400;
                        }
                    }
                } else {
                    itemNewsboyPrice = (int) (itemPrice - (itemPrice * 0.2));
                    itemDealerPrice = (int) (itemPrice - (itemPrice * 0.3));
                }*/
//                Long newsboyID = getIntent().getLongExtra("newsboy_id", 0);
                itemToSave.setItemCollectable(cbCollectableSaveItem.isChecked() ? 1 : 0);
                itemToSave.setItemStatus(1);
                EXECUTOR.execute(() -> {
                    ItemDao itemDao = databaseController.itemDao();
                    itemDao.insertItem(itemToSave);
                    List<Item> tmpList = itemDao.findByStatus(1);
                    itemList.clear();
                    itemList.addAll(tmpList);
                    HANDLER.post(() -> {
                        alertDialog.dismiss();
                        itemAdapter.setItemList(tmpList);
                        runOnUiThread(() -> {
                            itemAdapter.notifyItemRangeRemoved(0, itemList.size() - 1);
                            itemAdapter.notifyItemRangeInserted(0, tmpList.size());
                        });
                    });
                });
                runOnUiThread(() -> Toast.makeText(this, "Save item", Toast.LENGTH_SHORT).show());
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}