package com.ms.newspapercontrol.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ms.newspapercontrol.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item WHERE newsboy_id = :id")
    List<Item> findByID(Integer id);

    @Insert
    void insertItem(Item item);
}
