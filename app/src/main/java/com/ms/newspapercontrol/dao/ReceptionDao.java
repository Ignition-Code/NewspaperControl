package com.ms.newspapercontrol.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.ms.newspapercontrol.entities.Reception;

@Dao
public interface ReceptionDao {
    @Insert
    void insertReception(Reception reception);
}
