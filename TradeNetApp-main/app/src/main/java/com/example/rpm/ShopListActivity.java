package com.example.rpm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rpm.controllers.ApiService;
import com.example.rpm.modelsDB.DataHandler;
import com.example.rpm.modelsDB.Shop;
import com.example.rpm.modelsDB.TradeNetwork;
import com.example.rpm.modelsJSON.CountForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShopListActivity extends AppCompatActivity implements ShopListAdapter.ISortByColumn {

    private Menu shopMenu;
    private ListView listView;

    private DataHandler dataHandler = new DataHandler();

    private int shopId;
    private String shopName;
    private String shopAdress;
    private ArrayList<Shop> allShop;
    private int currentShopId;
    private ArrayList<Shop> shopList = new ArrayList<>();
    private ApiService apiService = new ApiService();
    private CountForm countform = new CountForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        initializeValues();
        if(shopId != -1){
            getDatabaseInfo();
        }
        else{
            Toast.makeText(getApplicationContext(), "Не удалось получить данные", Toast.LENGTH_SHORT)
                    .show();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countform = apiService.selectCount(shopName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        sleep(1000);
        setTitle(countform.name + " время работы " +countform.attime + " - " + countform.fortime);
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initializeValues(){
        listView = findViewById(R.id.shop_list_view);
        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        shopId = args.getInt("shopId");
        shopName = args.getString("title");
        shopAdress = args.getString("adress");;
        setTitle(shopName);
        dataHandler.createOrConnectToDB(getApplicationContext());
    }
    private void getDatabaseInfo(){
        GetShop getShop = new GetShop();
        getShop.execute();
    }
    private void setListView(){
        ShopListAdapter empAdapter = new ShopListAdapter(this, R.layout.shop_element_listview, shopList);
        listView.setAdapter(empAdapter);
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        shopMenu = menu;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_new_shop:{
                createOrChangeShop(true, null);
                return true;
            }
            case R.id.change_shop_name:{
                AlertDialog inputDialog = new AlertDialog.Builder(ShopListActivity.this).create();
                View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.input_shop, null);
                inputDialog.setView(vv);
                inputDialog.setCancelable(true);
                ((EditText) vv.findViewById(R.id.input_shop_name)).setText(shopName);
                ((EditText) vv.findViewById(R.id.input_shop_adress)).setText(shopAdress);
                ((Button) vv.findViewById(R.id.add_shop_accept)).setText("Изменить");
                ((Button) vv.findViewById(R.id.add_shop_accept)).setOnClickListener(v->{

                    String newName = ((EditText) vv.findViewById(R.id.input_shop_name)).getText().toString();

                    newName = newName.trim();
                    String newAdress = ((EditText) vv.findViewById(R.id.input_shop_adress)).getText().toString();
                    newAdress = newAdress.trim();


                    if(newName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Недопустимое название", Toast.LENGTH_SHORT)
                                .show();
                        inputDialog.cancel();
                        return;
                    }
                    if(newAdress.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Недопустимый адрес", Toast.LENGTH_SHORT)
                                .show();
                        inputDialog.cancel();
                        return;
                    }



                    setTitle(((EditText) vv.findViewById(R.id.input_shop_name)).getText().toString());
                    setTitle(((EditText) vv.findViewById(R.id.input_shop_adress)).getText().toString());

                    String changeName = ((EditText) vv.findViewById(R.id.input_shop_name)).getText().toString();
                    String changeAdress = ((EditText) vv.findViewById(R.id.input_shop_adress)).getText().toString();
                    dataHandler.updateShop(shopId, changeName, changeAdress);

                   ;
                    inputDialog.cancel();
                });
                ((Button) vv.findViewById(R.id.add_shop_decline)).setOnClickListener(v->{
                    inputDialog.cancel();
                });
                inputDialog.show();
                return true;
            }
            case R.id.delete_shop:{
                dataHandler.deleteShop(shopId);
                Intent intent = new Intent(ShopListActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void createOrChangeShop(boolean createNew, Shop shop){
        AlertDialog inputDialog = new AlertDialog.Builder(ShopListActivity.this).create();
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.input_shop, null);
        inputDialog.setView(vv);
        inputDialog.setCancelable(true);

        EditText nameTextView = (EditText) vv.findViewById(R.id.input_shop_name);
        EditText adressTextView = (EditText) vv.findViewById(R.id.input_shop_adress);
        Button acceptButton = (Button) vv.findViewById(R.id.add_shop_accept);




        acceptButton.setOnClickListener(v->{
            if(createNew){
                Shop newShop = new Shop();
                newShop.name = ((EditText) vv.findViewById(R.id.input_shop_name)).getText().toString();
                newShop.adress = ((EditText) vv.findViewById(R.id.input_shop_adress)).getText().toString();

                newShop.id = shopId;
                dataHandler.addShop(newShop);
                String atTime = countform.attime;
                String forTime = countform.fortime;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {


                            apiService.insertCount(atTime, forTime, shopName);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                sleep(1000);

            }
            else{
                shop.name = nameTextView.getText().toString();
                shop.adress = adressTextView.getText().toString();
                dataHandler.updateShop(shop);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GetShop getShop = new GetShop();
            getShop.execute();
            inputDialog.cancel();
        });



    }

    @Override
    public void sortByColumn(int columnId) {
        if(columnId == 0){
            Comparator<Shop> comparator = (o1, o2) -> o1.name.compareTo(o2.name);
            Collections.sort(shopList, comparator);
            setListView();
            Toast.makeText(getApplicationContext(), "Вы нажали на столбец с названием", Toast.LENGTH_SHORT)
                    .show();
        }
        if(columnId == 1){
            Comparator<Shop> comparator = (o1, o2) -> o1.adress.compareTo(o2.adress);
            Collections.sort(shopList, comparator);
            setListView();
            Toast.makeText(getApplicationContext(), "Вы нажали на столбец с адресом", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void simpleClick(int position) {
        createOrChangeShop(false, shopList.get(position));
    }

    class GetShop extends AsyncTask<Void, Void, ArrayList<Shop>> {
        @Override
        protected ArrayList<Shop> doInBackground(Void... unused) {
            return (ArrayList<Shop>) dataHandler.getDB()
                    .shopDao()
                    .getTradeNetworkShop(shopId);
        }
        @Override
        protected void onPostExecute(ArrayList<Shop> shopArrayList) {
            shopList = shopArrayList;
            if(shopList.size() < 1){
                Toast.makeText(getApplicationContext(), "Пока нет магазинов", Toast.LENGTH_SHORT)
                        .show();
            }else{
                Toast.makeText(getApplicationContext(), "Магазины загружены (" + String.valueOf(shopList.size()) + ")", Toast.LENGTH_SHORT)
                        .show();
            }
            setListView();

        }
    }
}