package com.ms.newspapercontrol.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Entity(tableName = "item", foreignKeys = {
        @ForeignKey(
                entity = Newsboy.class,
                parentColumns = "newsboy_id",
                childColumns = "newsboy_id",
                onDelete = ForeignKey.CASCADE
        )
})
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    public Long itemID;
    @ColumnInfo(name = "item_name")
    public String itemName;
    //defines retail price
    @ColumnInfo(name = "item_price")
    public Integer itemPrice;
    //defines the price charged by the distributor
    @ColumnInfo(name = "item_newsboy_price")
    public Integer itemNewsboyPrice;
    //defines the price paid by the distributor
    @ColumnInfo(name = "item_dealer_price")
    public Integer itemDealerPrice;
    //defines delivery date
    @ColumnInfo(name = "item_delivery_date")
    public String itemDeliveryDate;
    //defines return date
    @ColumnInfo(name = "item_return_date")
    public String itemReturnDate;
    //defines quantity delivered
    @ColumnInfo(name = "item_quantity_delivered")
    public Integer itemQuantityDelivered;
    //defines amount refunded
    @ColumnInfo(name = "item_amount_refunded")
    public Integer itemAmountRefunded;
    //def foreign key
    @ColumnInfo(name = "newsboy_id", index = true)
    public Long itemNewsboyID;
    //item return status
    @ColumnInfo(name = "item_return_status")
    public Integer itemReturnStatus;
    //item collectable option
    @ColumnInfo(name = "item_collectable")
    public Integer itemCollectable;
}
