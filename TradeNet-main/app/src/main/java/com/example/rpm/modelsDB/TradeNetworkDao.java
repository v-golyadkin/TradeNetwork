package com.example.rpm.modelsDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface TradeNetworkDao {
    @Query("SELECT * FROM TradeNetwork")
    List<TradeNetwork> getAll();

    @Query("SELECT * FROM TradeNetwork WHERE id = :id")
    List<TradeNetwork> getById(int id);

    @Query("SELECT * FROM TradeNetwork WHERE id = :id")
    TradeNetwork getOneById(int id);

    @Insert
    void insertAll(TradeNetwork... faculties);

    @Update
    void update(TradeNetwork tradenetwork);

    @Delete
    void delete(TradeNetwork tradenetwork);
}
