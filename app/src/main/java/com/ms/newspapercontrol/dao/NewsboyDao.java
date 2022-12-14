package com.ms.newspapercontrol.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ms.newspapercontrol.entities.Newsboy;

import java.util.List;

@Dao
public interface NewsboyDao {
    @Query("SELECT * FROM newsboy")
    List<Newsboy> getAll();

    @Insert
    void insertNewsboy(Newsboy newsboy);
}
