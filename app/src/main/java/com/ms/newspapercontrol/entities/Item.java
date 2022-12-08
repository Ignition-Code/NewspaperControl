package com.ms.newspapercontrol.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Entity(tableName = "item")
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    public Long itemID;
    @ColumnInfo(name = "item_name")
    public String itemName;
    //item collectable option
    @ColumnInfo(name = "item_collectable")
    public Integer itemCollectable;
    //item status
    @ColumnInfo(name = "item_status")
    public Integer itemStatus;
}
