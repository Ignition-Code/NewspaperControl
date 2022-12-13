package com.ms.newspapercontrol.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.ms.newspapercontrol.entities.Delivery;

@Dao
public interface DeliveryDao {
    @Insert
    void insertDelivery(Delivery delivery);
}
