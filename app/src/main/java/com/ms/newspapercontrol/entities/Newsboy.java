package com.ms.newspapercontrol.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Entity(tableName = "newsboy")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Newsboy {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "newsboy_id")
    public Long newsboyID;
    @ColumnInfo(name = "newsboy_name")
    public String newsboyName;
}
