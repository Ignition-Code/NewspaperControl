package com.ms.newspapercontrol;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ms.newspapercontrol.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemViewActivity extends AppCompatActivity {

//    private Button btSaveReturn;
//    private RecyclerView rvItem;

    //    private DatabaseController databaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
//        rvItem = findViewById(R.id.rvItem);
//        btSaveReturn = findViewById(R.id.btSaveReturn);
        //    private ExecutorService executor = Executors.newSingleThreadExecutor();
        //    private Handler handler = new Handler(Looper.getMainLooper());
        List<Item> itemList = new ArrayList<>();
//        ItemAdapter itemAdapter = new ItemAdapter(itemList);
//        rvItem.setHasFixedSize(true);
//        rvItem.setLayoutManager(new LinearLayoutManager(this));
//        rvItem.setAdapter(itemAdapter);
//        databaseController = Room.databaseBuilder(getApplicationContext(), DatabaseController.class, "newsboy-application").build();
//        getItemList();
    }

    /**
     * List of saved item
     */
   /* private void getItemList() {
        executor.execute(() -> {
            Long newsboyID = getIntent().getLongExtra("newsboy_id", 0);
            ItemDao itemDao = databaseController.itemDao();
            itemList = itemDao.listByID(newsboyID, 0);
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
                EditText etPriceSaveItem = view.findViewById(R.id.etPriceSaveItem);
                EditText etQuantitySaveItem = view.findViewById(R.id.etQuantitySaveItem);
                CheckBox cbCollectableSaveItem = view.findViewById(R.id.cbCollectableSaveItem);
                Item itemToSave = new Item();
                String itemName = etNameSaveItem.getText().toString();
                itemToSave.setItemName(itemName);
                int itemPrice = Integer.parseInt(etPriceSaveItem.getText().toString().replace(".", "").replace(",", ""));
                itemToSave.setItemPrice(itemPrice);
                int itemNewsboyPrice, itemDealerPrice;
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
                }
                itemToSave.setItemNewsboyPrice(itemNewsboyPrice);
                itemToSave.setItemDealerPrice(itemDealerPrice);
                itemToSave.setItemDeliveryDate(new Util().getFormattedDate("dd-MM-yyyy", new Date()));
                itemToSave.setItemQuantityDelivered(Integer.parseInt(etQuantitySaveItem.getText().toString()));
                Long newsboyID = getIntent().getLongExtra("newsboy_id", 0);
                itemToSave.setItemNewsboyID(newsboyID);
                itemToSave.setItemReturnStatus(0);
                itemToSave.setItemCollectable(cbCollectableSaveItem.isChecked() ? 1 : 0);
                executor.execute(() -> {
                    ItemDao itemDao = databaseController.itemDao();
                    itemDao.insertItem(itemToSave);
                    List<Item> tmpList = itemDao.listByID(newsboyID, 0);
                    itemList.clear();
                    itemList.addAll(tmpList);
                    handler.post(() -> {
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
    }*/
}