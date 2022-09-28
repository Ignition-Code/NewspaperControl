package com.ms.newspapercontrol.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ms.newspapercontrol.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item WHERE newsboy_id = :id AND item_return_status = :status ORDER BY item_collectable DESC")
    List<Item> listByID(Long id, Integer status);

    @Insert
    void insertItem(Item item);
}
