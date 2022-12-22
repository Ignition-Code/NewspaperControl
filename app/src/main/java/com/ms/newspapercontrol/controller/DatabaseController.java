package com.ms.newspapercontrol.controller;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ms.newspapercontrol.dao.DeliveryDao;
import com.ms.newspapercontrol.dao.ItemDao;
import com.ms.newspapercontrol.dao.NewsboyDao;
import com.ms.newspapercontrol.dao.ReceptionDao;
import com.ms.newspapercontrol.entities.Delivery;
import com.ms.newspapercontrol.entities.Item;
import com.ms.newspapercontrol.entities.Newsboy;
import com.ms.newspapercontrol.entities.Reception;

@Database(entities = {Newsboy.class, Item.class, Reception.class, Delivery.class}, version = 6, exportSchema = false)
public abstract class DatabaseController extends RoomDatabase {
    public abstract NewsboyDao newsboyDao();
    public abstract ItemDao itemDao();
    public abstract ReceptionDao receptionDao();
    public abstract DeliveryDao deliveryDao();
}
