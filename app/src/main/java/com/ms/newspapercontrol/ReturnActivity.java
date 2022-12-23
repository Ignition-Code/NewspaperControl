package com.ms.newspapercontrol;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ms.newspapercontrol.adapter.ReturnAdapter;
import com.ms.newspapercontrol.controller.DatabaseController;
import com.ms.newspapercontrol.dao.DeliveryDao;
import com.ms.newspapercontrol.entities.Delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReturnActivity extends AppCompatActivity {

    private DatabaseController databaseController;
    private ReturnAdapter returnAdapter;
    private List<Delivery> deliveryList;
    private Long newsboyID;
    private String newsboyName;

    private final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        Bundle bundle = getIntent().getExtras();
        Button btSaveReturn = findViewById(R.id.btSaveReturn);
        RecyclerView rvItemReturn = findViewById(R.id.rvItemReturn);
        newsboyID = bundle.getLong("newsboy_id", 0);
        newsboyName = bundle.getString("newsboy_name", "");
        deliveryList = new ArrayList<>();
        returnAdapter = new ReturnAdapter(deliveryList);
        rvItemReturn.setHasFixedSize(true);
        rvItemReturn.setLayoutManager(new LinearLayoutManager(this));
        rvItemReturn.setAdapter(returnAdapter);
        databaseController = Room.databaseBuilder(getApplicationContext(), DatabaseController.class, "newsboy-application").build();
        getReceptionList();
        btSaveReturn.setOnClickListener(v -> {
            save();
        });
    }

    /**
     * List of saved delivery
     */
    private void getReceptionList() {
        EXECUTOR.execute(() -> {
            DeliveryDao deliveryDao = databaseController.deliveryDao();
            deliveryList = deliveryDao.findActiveDelivery(newsboyID);
            HANDLER.post(() -> {
                returnAdapter.setDeliveryList(deliveryList);
                returnAdapter.notifyItemRangeInserted(0, deliveryList.size() - 1);
            });
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void save(){
        EXECUTOR.execute(() -> {
            List<Delivery> tmpDelivery = returnAdapter.getDeliveryList();
            System.out.println("''''''''''''''''''''''''''''''''''''''''''''''''''");
            for (Delivery delivery: tmpDelivery) {
                System.out.println(delivery.toString());
            }
//            ReceptionDao receptionDao = databaseController.receptionDao();
//            receptionDao.insertReception(reception);
            HANDLER.post(() -> {});
        });
        //runOnUiThread(() -> Toast.makeText(this, "Save reception", Toast.LENGTH_SHORT).show());
    }
}