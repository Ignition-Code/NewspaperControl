package com.ms.newspapercontrol.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity(tableName = "newsboy")
@Getter
@NoArgsConstructor
public class Newsboy {
    @PrimaryKey(autoGenerate = true)
    public Long newsboyID;
    @ColumnInfo(name = "newsboy_name")
    public String newsboyName;
}
