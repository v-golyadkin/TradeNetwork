package com.example.rpm.modelsDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class TradeNetwork {
    @PrimaryKey(autoGenerate = true) public int id;

    @ColumnInfo(name = "name") public String name;

    @Ignore public boolean newObj;
    @Ignore public boolean changed;

    @Ignore public static TradeNetwork findTradeNetworkById(ArrayList<TradeNetwork> searchingList, int tradeNetworkId){
        for(TradeNetwork tradenetwork :searchingList){
            if(tradenetwork.id == tradeNetworkId){
                return tradenetwork;
            }
        }
        return null;
    }
}
