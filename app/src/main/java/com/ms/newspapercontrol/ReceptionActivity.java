package com.ms.newspapercontrol;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReceptionActivity extends AppCompatActivity {

    private Calendar calendar;
    private TextView tvDateReception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);
        EditText etPriceReception = findViewById(R.id.etPriceReception);
        EditText etNumberReception = findViewById(R.id.etNumberReception);
        tvDateReception = findViewById(R.id.tvDateReception);
        TextView tvItemNameReception = findViewById(R.id.tvItemNameReception);
        calendar = Calendar.getInstance();
        Bundle bundle = getIntent().getExtras();
        tvItemNameReception.setText(bundle.getString("item_name"));
        tvDateReception.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        tvDateReception.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> tvDateReception.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            datePickerDialog.show();
        });
    }
}