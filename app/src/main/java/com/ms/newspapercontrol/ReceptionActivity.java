package com.ms.newspapercontrol;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.ms.newspapercontrol.controller.DatabaseController;
import com.ms.newspapercontrol.dao.ReceptionDao;
import com.ms.newspapercontrol.entities.Reception;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceptionActivity extends AppCompatActivity {

    private Bundle bundle;
    private Calendar calendar;
    private DatabaseController databaseController;
    private EditText etPriceReception, etNumberReception;
    private TextView tvDateReception, tvItemNameReception;

    private final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final Handler HANDLER = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);
        Button btnSaveReception = findViewById(R.id.btnSaveReception);
        etPriceReception = findViewById(R.id.etPriceReception);
        etNumberReception = findViewById(R.id.etNumberReception);
        tvDateReception = findViewById(R.id.tvDateReception);
        tvItemNameReception = findViewById(R.id.tvItemNameReception);
        TextView tvItemNameReception = findViewById(R.id.tvItemNameReception);
        calendar = Calendar.getInstance();
        bundle = getIntent().getExtras();
        tvItemNameReception.setText(bundle.getString("item_name"));
        tvDateReception.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        tvDateReception.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> tvDateReception.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            datePickerDialog.show();
        });
        btnSaveReception.setOnClickListener(v -> save());
        databaseController = Room.databaseBuilder(getApplicationContext(), DatabaseController.class, "newsboy-application").build();
    }

    private void save () {
        final String itemName = tvItemNameReception.getText().toString();
        final Integer dealerPrice = Integer.parseInt(etPriceReception.getText().toString());
        final Integer receivedAmount = Integer.parseInt(etNumberReception.getText().toString());
        int price = 0;
        int newsboyPrice = 0;
        //Calculate
        if (itemName.equalsIgnoreCase("extra")) {
            if (Objects.equals(dealerPrice, 1750)) {
                newsboyPrice = 2000;
                price = 3000;
            } else if (Objects.equals(dealerPrice, 2400)) {
                newsboyPrice = 3000;
                price = 4000;
            }
        } else if (itemName.equalsIgnoreCase("popular")) {
            if (Objects.equals(dealerPrice, 1750)) {
                newsboyPrice = 2200;
                price = 3000;
            } else if (Objects.equals(dealerPrice, 1950)) {
                newsboyPrice = 2500;
                price = 3500;
            }
        } else {
            price = dealerPrice * 100 / 70;
            double tmpPrice = price * 0.8;
            newsboyPrice = (int) tmpPrice;
        }

        Reception reception = new Reception();
        reception.setReceptionDate(tvDateReception.getText().toString());
        reception.setReceptionPrice(price);
        reception.setReceptionDealerPrice(dealerPrice);
        reception.setReceptionNewsboyPrice(newsboyPrice);
        reception.setItemID(bundle.getLong("item_id"));
        reception.setItemQuantityReceived(receivedAmount);
        EXECUTOR.execute(() -> {
            ReceptionDao receptionDao = databaseController.receptionDao();
            receptionDao.insertReception(reception);
            HANDLER.post(() -> runOnUiThread(this::finish));
        });
        runOnUiThread(() -> Toast.makeText(this, "Save reception", Toast.LENGTH_SHORT).show());
    }
}