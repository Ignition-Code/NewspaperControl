package com.ms.newspapercontrol.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ms.newspapercontrol.entities.Delivery;

import java.util.List;

@Dao
public interface DeliveryDao {
    @Insert
    void insertDelivery(Delivery delivery);

    @Query("SELECT delivery_id, delivery_item_return_date, delivery_item_quantity_delivered, delivery_item_amount_refunded, delivery_item_return_status, newsboy_id, r.reception_id, i.item_name, i.item_collectable, r.reception_newsboy_price FROM delivery AS d INNER JOIN reception AS r ON r.reception_id = d.reception_id INNER JOIN item AS i ON i.item_id = r.item_id WHERE d.newsboy_id = :newsboyID AND d.delivery_item_return_status = 0 ORDER BY i.item_collectable")
    List<Delivery> findActiveDelivery(Long newsboyID);

    @Update
    void updateDelivery(Delivery delivery);
}
