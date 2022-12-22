package com.ms.newspapercontrol;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.anggastudio.printama.Printama;
import com.ms.newspapercontrol.adapter.DeliveryAdapter;
import com.ms.newspapercontrol.controller.DatabaseController;
import com.ms.newspapercontrol.dao.DeliveryDao;
import com.ms.newspapercontrol.dao.ReceptionDao;
import com.ms.newspapercontrol.entities.Delivery;
import com.ms.newspapercontrol.entities.Reception;
import com.ms.newspapercontrol.util.Printer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeliveryActivity extends AppCompatActivity {

    private Calendar calendar;
    private DatabaseController databaseController;
    private DeliveryAdapter deliveryAdapter;
    private List<Reception> receptionList;
    private Long newsboyID;
    private String newsboyName;
    private TextView tvDateDelivery;

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
        setContentView(R.layout.activity_delivery);
        tvDateDelivery = findViewById(R.id.tvDateDelivery);
        RecyclerView rvItemDelivery = findViewById(R.id.rvItemDelivery);
        Button btSaveDelivery = findViewById(R.id.btSaveDelivery);
        rvItemDelivery.setHasFixedSize(true);
        rvItemDelivery.setLayoutManager(new LinearLayoutManager(this));
        receptionList = new ArrayList<>();
        deliveryAdapter = new DeliveryAdapter(receptionList);
        rvItemDelivery.setAdapter(deliveryAdapter);
        Bundle bundle = getIntent().getExtras();
        newsboyID = bundle.getLong("newsboy_id", 0);
        newsboyName = bundle.getString("newsboy_name", "");
        calendar = Calendar.getInstance();
        tvDateDelivery.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        tvDateDelivery.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> tvDateDelivery.setText(getFormattedDate(year1, (monthOfYear + 1), dayOfMonth)), year, month, day);
            datePickerDialog.show();
        });
        btSaveDelivery.setOnClickListener(v -> save());
        databaseController = Room.databaseBuilder(getApplicationContext(), DatabaseController.class, "newsboy-application").build();
        getReceptionList();
    }

    private String getFormattedDate(int year, int month, int day) {
        return day + "/" + month + "/" + year;
    }

    private void save() {
        final List<Printer> toPrint = new ArrayList<>();
        EXECUTOR.execute(() -> {
            DeliveryDao deliveryDao = databaseController.deliveryDao();
            for (Delivery delivery : deliveryAdapter.getDeliveryList()) {
                if (delivery.getDeliveryItemQuantityDelivered() != null) {
                    toPrint.add(new Printer(
                            getItemName(delivery.getReceptionID()),
                            delivery.getDeliveryItemQuantityDelivered().toString()
                    ));
                    delivery.setDeliveryItemReturnStatus(0);
                    delivery.setNewsboyID(newsboyID);
                    deliveryDao.insertDelivery(delivery);
                }
            }
            HANDLER.post(() -> {
                runOnUiThread(() -> Toast.makeText(this, "Save delivery", Toast.LENGTH_SHORT).show());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Imprimir boleta de entrega")
                        .setPositiveButton("Imprimir", (dialog, id) -> {
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
                                printResume(toPrint);
                            } else {
                                Printama.showPrinterList(this, R.color.teal_700, printerName -> {
                                });
                            }
                            dialog.dismiss();
                            finish();
                        })
                        .setNegativeButton("Cancelar", (DialogInterface.OnClickListener) (dialog, id) -> {
                            dialog.dismiss();
                            finish();
                        });
                builder.create().show();
            });
        });
    }

    /**
     * List of saved reception
     */
    private void getReceptionList() {
        EXECUTOR.execute(() -> {
            ReceptionDao receptionDao = databaseController.receptionDao();
            receptionList = receptionDao.findReceptionByDate(tvDateDelivery.getText().toString());
            HANDLER.post(() -> {
                deliveryAdapter.setReceptionList(receptionList);
                deliveryAdapter.notifyItemRangeInserted(0, receptionList.size() - 1);
            });
        });
    }

    private String getItemName(Long receptionID) {
        for (Reception reception : receptionList) {
            if (Objects.equals(reception.getReceptionID(), receptionID)) {
                return reception.getReceptionProductName();
            }
        }

        return null;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void printResume(List<Printer> toPrint) {
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
                //printama.printTextln("");
            }

            printama.setNormalText();
            printama.feedPaper();
            printama.close();
        }, this::showToast);
    }

    private Integer getMaxLength(List<Printer> toPrint){
        int length = 0;
        for (Printer print: toPrint) {
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