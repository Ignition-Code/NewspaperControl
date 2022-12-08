package com.ms.newspapercontrol;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity implements NewsboyAdapter.NewsboyListener {

    private RecyclerView rvNewsboy;
    private List<Newsboy> newsboyList;
    private DatabaseController databaseController;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    private NewsboyAdapter newsboyAdapter;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
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
        setContentView(R.layout.activity_main);
        rvNewsboy = findViewById(R.id.rvNewsboy);
        rvNewsboy.setLayoutManager(new GridLayoutManager(this, 2));
        newsboyList = new ArrayList<>();
        newsboyAdapter = new NewsboyAdapter(newsboyList, this);
        rvNewsboy.setAdapter(newsboyAdapter);
        databaseController = Room.databaseBuilder(getApplicationContext(), DatabaseController.class, "newsboy-application").build();
        getNewsboyList();

        Button print = findViewById(R.id.btnPrint);
        print.setOnClickListener((v) -> {
            int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
            if (permission1 != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
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

            BluetoothDevice connectedPrinter = Printama.with(this).getConnectedPrinter();
            if (connectedPrinter != null) {
                printTextStyles();
            } else {
                Printama.showPrinterList(this, R.color.teal_700, printerName -> {});
            }
        });
    }

    private void printTextStyles() {
        Printama.with(this).connect(printama -> {
            printama.setTall();
            printama.printText("small___________");
            printama.printTextln("TEXTtext");

            printama.setNormalText();
            printama.printText("normal__________");
            printama.printTextln("TEXTtext");

            printama.printTextNormal("bold____________");
            printama.printTextlnBold("TEXTtext");

            printama.setNormalText();
            printama.printTextNormal("tall____________");
            printama.printTextlnTall("TEXTtext");

            printama.printTextNormal("tall bold_______");
            printama.printTextlnTallBold("TEXTtext");

            printama.printTextNormal("wide____________");
            printama.printTextlnWide("TEXTtext");

            printama.printTextNormal("wide bold_______");
            printama.printTextlnWideBold("TEXTtext");

            printama.printTextNormal("wide tall_______");
            printama.printTextlnWideTall("TEXTtext");

            printama.printTextNormal("wide tall bold__");
            printama.printTextlnWideTallBold("TEXTtext");

            printama.printTextNormal("underline_______");
            printama.setUnderline();
            printama.printTextln("TEXTtext");

            printama.printTextNormal("delete line_____");
            printama.setDeleteLine();
            printama.printTextln("TEXTtext");

            printama.setNormalText();
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * List of saved newsboy
     */
    private void getNewsboyList() {
        executor.execute(() -> {
            NewsboyDao newsboyDao = databaseController.newsboyDao();
            newsboyList = newsboyDao.getAll();
            newsboyAdapter.setNewsboyList(newsboyList);
            newsboyAdapter.notifyItemRangeInserted(0, newsboyList.size() - 1);
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
                });
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewsboyClick(int position) {
        Intent intent = new Intent(this, ItemViewActivity.class);
        intent.putExtra("newsboy_id", newsboyList.get(position).getNewsboyID());
        startActivity(intent);
    }
}