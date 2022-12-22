package com.ms.newspapercontrol.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ms.newspapercontrol.entities.Reception;

import java.util.List;

@Dao
public interface ReceptionDao {
    @Insert
    void insertReception(Reception reception);

    @Query("SELECT reception_id, reception_newsboy_price, reception_dealer_price, reception_price, reception_date, item_quantity_received, i.item_id, i.item_name FROM reception AS r INNER JOIN item AS i ON r.item_id = i.item_id WHERE reception_date = :date")
    List<Reception> findReceptionByDate(String date);
}
