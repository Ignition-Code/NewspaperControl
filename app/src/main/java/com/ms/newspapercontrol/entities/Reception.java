package com.ms.newspapercontrol.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Entity(tableName = "reception", foreignKeys = {
        @ForeignKey(
                entity = Item.class,
                parentColumns = "item_id",
                childColumns = "item_id",
                onDelete = ForeignKey.CASCADE
        )
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Reception {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "reception_id")
        public Long receptionID;
        //defines the price charged by the distributor
        @ColumnInfo(name = "reception_newsboy_price")
        public Integer receptionNewsboyPrice;
        //defines the price paid by the distributor
        @ColumnInfo(name = "reception_dealer_price")
        public Integer receptionDealerPrice;
        //defines retail price
        @ColumnInfo(name = "reception_price")
        public Integer receptionPrice;
        //defines delivery date
        @ColumnInfo(name = "reception_date")
        public String receptionDate;
        //defines quantity delivered
        @ColumnInfo(name = "item_quantity_received")
        public Integer itemQuantityReceived;
        //def foreign key
        @ColumnInfo(name = "item_id", index = true)
        public Long itemID;
        //Product name to show
        @ColumnInfo(name = "item_name")
        public String receptionProductName;
}
