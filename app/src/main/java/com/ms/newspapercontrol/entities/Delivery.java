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
@Entity(tableName = "delivery", foreignKeys = {
        @ForeignKey(
                entity = Newsboy.class,
                parentColumns = "newsboy_id",
                childColumns = "newsboy_id",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Reception.class,
                parentColumns = "reception_id",
                childColumns = "reception_id",
                onDelete = ForeignKey.CASCADE
        )
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Delivery {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "delivery_id")
        public Long deliveryID;
        //defines return date
        @ColumnInfo(name = "delivery_item_return_date")
        public String deliveryItemReturnDate;
        //defines quantity delivered
        @ColumnInfo(name = "delivery_item_quantity_delivered")
        public Integer deliveryItemQuantityDelivered;
        //defines amount refunded
        @ColumnInfo(name = "delivery_item_amount_refunded")
        public Integer deliveryItemAmountRefunded;
        //item return status
        @ColumnInfo(name = "delivery_item_return_status")
        public Integer deliveryItemReturnStatus;
        //def foreign key
        @ColumnInfo(name = "newsboy_id", index = true)
        public Long newsboyID;
        @ColumnInfo(name = "reception_id", index = true)
        public Long receptionID;
}
