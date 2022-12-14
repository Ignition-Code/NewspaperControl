package com.ms.newspapercontrol;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.anggastudio.printama.Printama;
import com.ms.newspapercontrol.adapter.NewsboyAdapter;
import com.ms.newspapercontrol.controller.DatabaseController;
import com.ms.newspapercontrol.dao.NewsboyDao;
import com.ms.newspapercontrol.entities.Newsboy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.S)
public class MainActivity extends AppCompatActivity implements NewsboyAdapter.NewsboyListener {

    private List<Newsboy> newsboyList;
    private DatabaseController databaseController;
    private NewsboyAdapter newsboyAdapter;

    private final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final Handler HANDLER = new Handler(Looper.getMainLooper());

    private enum ACTIVITY_MODE {
        DELIVERY,
        RETURN
    }

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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvNewsboy = findViewById(R.id.rvNewsboy);
        rvNewsboy.setLayoutManager(new GridLayoutManager(this, 2));
        newsboyList = new ArrayList<>();
        newsboyAdapter = new NewsboyAdapter(newsboyList, this);
        rvNewsboy.setAdapter(newsboyAdapter);
        databaseController = Room.databaseBuilder(getApplicationContext(), DatabaseController.class, "newsboy-application").build();
        getNewsboyList();
    }

    /**
     * List of saved newsboy
     */
    private void getNewsboyList() {
        EXECUTOR.execute(() -> {
            final int oldSize = newsboyList.size();
            NewsboyDao newsboyDao = databaseController.newsboyDao();
            newsboyList = newsboyDao.getAll();
            HANDLER.post(() -> {
                newsboyAdapter.notifyItemRangeRemoved(0, oldSize);
                newsboyAdapter.setNewsboyList(newsboyList);
                newsboyAdapter.notifyItemRangeInserted(0, newsboyList.size());
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_newsboy) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.save_newsboy, null);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            view.findViewById(R.id.btSaveNewsboy).setOnClickListener(v -> {
                EditText etNewsboyName = view.findViewById(R.id.etSaveNewsboyName);
                Newsboy newsboy = new Newsboy();
                newsboy.setNewsboyName(etNewsboyName.getText().toString());
                EXECUTOR.execute(() -> {
                    NewsboyDao newsboyDao = databaseController.newsboyDao();
                    newsboyDao.insertNewsboy(newsboy);
                    HANDLER.post(() -> {
//                        newsboyAdapter.setNewsboyList(tmpList);
//                        newsboyAdapter.notifyItemChanged(tmpList.size());
                        runOnUiThread(() -> {
                            getNewsboyList();
                        });
                        alertDialog.dismiss();
                    });
                });
            });
            return true;
        } else if (item.getItemId() == R.id.menu_add_item) {
            startActivity(new Intent(this, ItemActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menu_bluetooth) {
            int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
            if (permission1 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_STORAGE,
                        1
                );
            } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_LOCATION,
                        1
                );
            }

            checkBluetooth();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkBluetooth() {
        BluetoothDevice connectedPrinter = Printama.with(this).getConnectedPrinter();
        if (connectedPrinter != null) {
            runOnUiThread(() -> Toast.makeText(this, "Bluetooth already connected!", Toast.LENGTH_SHORT).show());
        } else {
            Printama.showPrinterList(this, R.color.teal_700, printerName -> {
            });
        }
    }

    @Override
    public void onNewsboyClick(int position) {
        final long newsboyID = this.newsboyList.get(position).getNewsboyID();
        final String newsboyName = this.newsboyList.get(position).getNewsboyName();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setPositiveButton("Devoluci??n", (dialog, id) -> {
                    showActivity(ACTIVITY_MODE.RETURN, newsboyID, newsboyName);
                    dialog.dismiss();
                })
                .setNegativeButton("Entrega", (DialogInterface.OnClickListener) (dialog, id) -> {
                    showActivity(ACTIVITY_MODE.DELIVERY, newsboyID, newsboyName);
                    dialog.dismiss();
                });
        builder.create().show();

    }

    private void showActivity(ACTIVITY_MODE mode, long newsboyID, String newsboyName) {
        Intent intent;
        if (mode == ACTIVITY_MODE.DELIVERY) {
            intent = new Intent(this, DeliveryActivity.class);
            intent.putExtra("newsboy_id", newsboyID);
            intent.putExtra("newsboy_name", newsboyName);
            startActivity(intent);
        } else if (mode == ACTIVITY_MODE.RETURN) {
            intent = new Intent(this, ReturnActivity.class);
            intent.putExtra("newsboy_id", newsboyID);
            intent.putExtra("newsboy_name", newsboyName);
            startActivity(intent);
        }
    }
}