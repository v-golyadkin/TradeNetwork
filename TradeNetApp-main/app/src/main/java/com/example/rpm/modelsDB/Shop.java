package com.example.rpm.modelsDB;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

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
    @Ignore public static Shop findShopById(ArrayList<Shop> searchingList, int shopId){
        for(Shop shop :searchingList){
            if(shop.id == shopId){
                return shop;
            }
        }
        return null;
    }
}
