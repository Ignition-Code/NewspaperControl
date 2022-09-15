package com.ms.newspapercontrol;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ms.newspapercontrol.adapter.NewsboyAdapter;
import com.ms.newspapercontrol.controller.DatabaseController;
import com.ms.newspapercontrol.dao.NewsboyDao;
import com.ms.newspapercontrol.entities.Newsboy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvNewsboy;
    private List<Newsboy> newsboyList;
    private DatabaseController databaseController;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    private NewsboyAdapter newsboyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvNewsboy = findViewById(R.id.rvNewsboy);
        rvNewsboy.setLayoutManager(new GridLayoutManager(this, 2));
        newsboyList = new ArrayList<>();
        newsboyAdapter = new NewsboyAdapter(newsboyList);
        rvNewsboy.setAdapter(newsboyAdapter);
        databaseController = Room.databaseBuilder(getApplicationContext(), DatabaseController.class, "newsboy-application").build();
        getNewsboyList();
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
}