package com.example.rpm.modelsDB;

import android.content.Context;

import java.util.List;

public class DataHandler {

    private AppDatabase db;
    private TradeNetworkDao tradeNetworkDao;
    private ShopDao shopDao;


    public void createOrConnectToDB(Context context){
        db = AppDatabase.getInstance(context);

        tradeNetworkDao = db.tradeNetworkDao();
        shopDao = db.shopDao();

    }
    public AppDatabase getDB(){
        return db;
    }


    //TradeNetwork
    public void addTradeNetwork(String name){
        TradeNetwork tradenetwork = new TradeNetwork();
        tradenetwork.name = name;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tradeNetworkDao.insertAll(tradenetwork);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void addTradeNetwork(int id, String name){
        TradeNetwork tradenetwork = new TradeNetwork();
        tradenetwork.id = id;
        tradenetwork.name = name;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tradeNetworkDao.insertAll(tradenetwork);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void deleteTradeNetwork(TradeNetwork tradenetwork){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tradeNetworkDao.delete(tradenetwork);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void updateTradeNetwork(int id, String newName){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TradeNetwork tradenetwork = tradeNetworkDao.getOneById(id);
                tradenetwork.name = newName;
                tradeNetworkDao.update(tradenetwork);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public  void updateTradeNetwork(TradeNetwork tradenetwork){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tradeNetworkDao.update(tradenetwork);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }



    //Shop


    public void addShop(Shop shop){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shopDao.insertAll(shop);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void updateShop(Shop shop){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shopDao.update(shop);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void updateShop(int id, String newName){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Shop dir = shopDao.getOneById(id);
                dir.name = newName;
                shopDao.update(dir);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void deleteShop(int id){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Shop dir = shopDao.getOneById(id);
                shopDao.delete(dir);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
