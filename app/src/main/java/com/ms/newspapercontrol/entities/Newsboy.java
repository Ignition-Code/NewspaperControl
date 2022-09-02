package com.ms.newspapercontrol.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "newsboy")
public class Newsboy {
    @PrimaryKey(autoGenerate = true)
    public Long newsboyID;
    @ColumnInfo(name = "newsboy_name")
    public String newsboyName;
}
