package com.example.rpm.modelsDB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
        entity = TradeNetwork.class,
        parentColumns = "id",
        childColumns = "tradeNetworkId", onDelete = CASCADE))
public class Shop {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "adress")
    public String adress;



    public int tradeNetworkId;

    @Ignore public boolean newObj;
    @Ignore public boolean changed;
}
