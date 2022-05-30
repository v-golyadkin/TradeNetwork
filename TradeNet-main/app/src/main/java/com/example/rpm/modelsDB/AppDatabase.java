package com.example.rpm.modelsDB;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TradeNetwork.class, Shop.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object DATABASE_NAME = "db3.sql";

    public abstract TradeNetworkDao tradeNetworkDao();
    public abstract ShopDao shopDao();

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            Log.d(LOG_TAG, "Creating new database instance");
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, (String) AppDatabase.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }
}
