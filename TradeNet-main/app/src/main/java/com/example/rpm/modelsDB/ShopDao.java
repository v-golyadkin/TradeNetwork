package com.example.rpm.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShopDao {
    @Query("SELECT * FROM Shop")
    List<Shop> getAll();

    @Query("SELECT * FROM Shop WHERE tradeNetworkId IS :tradeNetworkId")
    List<Shop> getTradeNetworkShop(int tradeNetworkId);

    @Query("SELECT * FROM Shop WHERE id = :id")
    Shop getOneById(int id);

    @Insert
    void insertAll(Shop... shops);

    @Update
    void update(Shop shop);

    @Delete
    void delete(Shop shop);
}
