package com.ms.newspapercontrol;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.anggastudio.printama.Printama;
import com.ms.newspapercontrol.adapter.ReturnAdapter;
import com.ms.newspapercontrol.controller.DatabaseController;
import com.ms.newspapercontrol.dao.DeliveryDao;
import com.ms.newspapercontrol.entities.Delivery;
import com.ms.newspapercontrol.util.Printer;
import com.ms.newspapercontrol.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
        getDeliveryList();
        btSaveReturn.setOnClickListener(v -> {
            save();
        });
    }

    /**
     * List of saved delivery
     */
    private void getDeliveryList() {
        EXECUTOR.execute(() -> {
            DeliveryDao deliveryDao = databaseController.deliveryDao();
            deliveryList = deliveryDao.findActiveDelivery(newsboyID);
            HANDLER.post(() -> {
                returnAdapter.setDeliveryList(deliveryList);
                returnAdapter.notifyItemRangeInserted(0, deliveryList.size());
            });
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void save(){
        EXECUTOR.execute(() -> {
            DeliveryDao deliveryDao = databaseController.deliveryDao();
            List<Printer> toPrint = new ArrayList<>();
            List<Delivery> tmpDelivery = returnAdapter.getDeliveryList();
            Integer total = 0;
            for (Delivery delivery: tmpDelivery) {
                if (Objects.equals(delivery.getItemCollectable(), 0)) {
                    //not collectable
                    Integer delivered = delivery.getDeliveryItemQuantityDelivered();
                    Integer refunded = delivery.getDeliveryItemAmountRefunded();
                    Integer price = delivery.getReceptionNewsboyPrice();
                    Integer subTotal = (delivered - (Objects.equals(refunded, null) ? 0 : refunded)) * price;
                    total += subTotal;
                    delivery.setDeliveryItemReturnStatus(1);
                    delivery.setDeliveryItemReturnDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                    toPrint.add(new Printer(
                            delivery.getItemName(),
                            new Util().getFormattedNumber(subTotal)));
                    deliveryDao.updateDelivery(delivery);
                }
            }

            final Integer finalTotal = total;
            HANDLER.post(() -> {
                BluetoothDevice connectedPrinter = Printama.with(this).getConnectedPrinter();
                if (connectedPrinter != null) {
                    printResume(toPrint, finalTotal);
                    finish();
                } else {
                    Printama.showPrinterList(this, R.color.teal_700, printerName -> {
                        finish();
                    });
                }
                runOnUiThread(() -> Toast.makeText(this, "Save return", Toast.LENGTH_SHORT).show());
            });
        });
    }

    private void printResume(List<Printer> toPrint, Integer total) {
        Integer maxLength = getMaxLength(toPrint) + 4;
        Printama.with(this).connect(printama -> {
            printama.printDoubleDashedLine();
            printama.setBold();
            printama.printTextlnWide(Printama.CENTER, newsboyName.toUpperCase());
            printama.printDoubleDashedLine();
            printama.printTextln(" ");

            for (Printer printer : toPrint) {
                printama.printTextNormal(getFormattedText(printer.getItemName().toUpperCase(), maxLength));
                printama.printTextlnWideBold(printer.getItemNumber());
                printama.printLine();
                printama.addNewLine();
            }

            printama.printTextln(" ");
            printama.printDoubleDashedLine();
            printama.setBold();
            printama.printTextlnWide(Printama.CENTER, new Util().getFormattedNumber(total));
            printama.printDoubleDashedLine();

            printama.setNormalText();
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }

    private Integer getMaxLength(List<Printer> toPrint) {
        int length = 0;
        for (Printer print : toPrint) {
            int tmp = print.getItemName().length();
            if (tmp > length) {
                length = tmp;
            }
        }

        return length;
    }

    private String getFormattedText(final String text, final Integer length) {
        String newText = text;
        for (int i = 0; i <= (length - text.length()); i++) {
            newText += " ";
        }

        return newText;
    }
}