package com.ms.newspapercontrol.controller;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ms.newspapercontrol.dao.NewsboyDao;
import com.ms.newspapercontrol.entities.Newsboy;

@Database(entities = {Newsboy.class}, version = 1)
public abstract class DatabaseController extends RoomDatabase {
    public abstract NewsboyDao newsboyDao();
}
