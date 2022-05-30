package com.example.rpm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.rpm.controllers.ApiService;
import com.example.rpm.modelsDB.DataHandler;
import com.example.rpm.modelsDB.Shop;
import com.example.rpm.modelsDB.TradeNetwork;
import com.example.rpm.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.P)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding binding;

    private NavigationView navigationView;
    private ListView directionsList;
    private Menu mainMenu;

    private DataHandler dataHandler = new DataHandler();
    private int currentTradeNetworkId;

    private ArrayList<TradeNetwork> allTradeNetworks;
    private ArrayList<Shop> allShops;
    private ArrayList<Shop> currentTradingNetworkShops;

    private ApiService apiService = new ApiService();

    //Initializing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeValues();

        getDatabaseInfo();

        createNavigationMenu();

    }

    private void initializeValues(){
        currentTradeNetworkId = -1;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        directionsList = (ListView) findViewById(R.id.directionsListView);
        directionsList.setOnItemClickListener((parent, view, position, id)->{
            Intent intent = new Intent(MainActivity.this, ShopListActivity.class);
            int dirId = currentTradingNetworkShops.get(position).id;
            String title = currentTradingNetworkShops.get(position).name;
            String adress = currentTradingNetworkShops.get(position).adress;

            intent.putExtra("directionId", dirId);
            intent.putExtra("title", title);
            intent.putExtra("adress", adress);

            startActivity(intent);
        });

        dataHandler.createOrConnectToDB(getApplicationContext());
    }

    private void setListViewAdapter(){
        ArrayList<String> str = new ArrayList<>();
        for(Shop shop : currentTradingNetworkShops){
            int amount = 0;
            str.add(shop.name + ' ' + shop.adress + ' ' );
        }
        directionsList.setAdapter(
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        str)
        );
    }

    private void getDatabaseInfo(){
        GetTradeNetwork getFaculties = new GetTradeNetwork();
        getFaculties.execute();
        GetShop getShop = new GetShop();
        getShop.execute();
    }

    @Override public void onBackPressed() {
        DrawerLayout dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
            return;
        }
        if(currentTradeNetworkId != -1){
            currentTradeNetworkId = -1;
            directionsList.setAdapter(
                    new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1)
            );
            changeMenuOptions(false);
            setTitle("Network App");
        }
    }

    //Menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mainMenu = menu;
        return true;
    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_trade_network:{
                addOrChangeTradeNetwork(true);
                return true;
            }
            case R.id.change_trade_network_name:{
                addOrChangeTradeNetwork(false);
                return true;
            }
            case R.id.add_shop:{
                AlertDialog inputDialog = new AlertDialog.Builder(MainActivity.this).create();
                View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.input_shop, null);
                inputDialog.setView(vv);
                inputDialog.setCancelable(true);

                ((Button) vv.findViewById(R.id.add_shop_accept)).setOnClickListener(v->{

                    String newName = ((EditText) vv.findViewById(R.id.input_shop_name)).getText().toString();
                    newName = newName.trim();
                    String newAdress = ((EditText) vv.findViewById(R.id.input_shop_adress)).getText().toString();
                    newAdress = newAdress.trim();


                    if(newName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Недопустимое название магазина", Toast.LENGTH_SHORT)
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


                    Shop shop = new Shop();
                    shop.name = ((EditText) vv.findViewById(R.id.input_shop_name)).getText().toString();
                    shop.adress = ((EditText) vv.findViewById(R.id.input_shop_adress)).getText().toString();
                    shop.tradeNetworkId = currentTradeNetworkId;

                    dataHandler.addShop(shop);
                    sleep(500);
                    GetShop getShop = new GetShop();
                    getShop.execute();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                apiService.insertFaculty(shop.name);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    inputDialog.cancel();
                });
                ((Button) vv.findViewById(R.id.add_shop_decline)).setOnClickListener(v->{
                    inputDialog.cancel();
                });
                inputDialog.show();
                return true;
            }
            case R.id.delete_trade_network:{
                changeMenuOptions(false);
                setTitle("TradeNetwork App");
                dataHandler.deleteTradeNetwork(TradeNetwork.findTradeNetworkById(allTradeNetworks, currentTradeNetworkId));
                currentTradeNetworkId = -1;
                sleep(500);
                getDatabaseInfo();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeMenuOptions(boolean visible){
        MenuItem addDepItem = mainMenu.findItem(R.id.add_shop);
        MenuItem change = mainMenu.findItem(R.id.change_trade_network_name);
        MenuItem deleteTradeNetwork = mainMenu.findItem(R.id.delete_trade_network);

        addDepItem.setVisible(visible);
        change.setVisible(visible);
        deleteTradeNetwork.setVisible(visible);
    }

    private void addOrChangeTradeNetwork(boolean createNew){
        AlertDialog inputDialog = new AlertDialog.Builder(MainActivity.this).create();
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.input_new_trading_network_layout, null);
        inputDialog.setView(vv);
        inputDialog.setCancelable(true);
        EditText editTradeNetworkName = (EditText) vv.findViewById(R.id.editTradeNetworkName);
        Button accept = (Button) vv.findViewById(R.id.addTradeNetworkAccept);

        if(!createNew){
            accept.setText("Изменить");
            editTradeNetworkName.setText(TradeNetwork.findTradeNetworkById(allTradeNetworks, currentTradeNetworkId).name);
        }
        else accept.setText("Добавить");
        accept.setOnClickListener(v->{

            String newName = editTradeNetworkName.getText().toString();
            newName = newName.trim();
            if(newName.isEmpty()){
                Toast.makeText(getApplicationContext(), "Недопустимое название торговой сети", Toast.LENGTH_SHORT)
                        .show();
                inputDialog.cancel();
                return;
            }

            if(createNew){
                dataHandler.addTradeNetwork(editTradeNetworkName.getText().toString());

            }
            else{
                setTitle(editTradeNetworkName.getText().toString());
                TradeNetwork tradenetwork = TradeNetwork.findTradeNetworkById(allTradeNetworks, currentTradeNetworkId);
                tradenetwork.name = editTradeNetworkName.getText().toString();
                dataHandler.updateTradeNetwork(tradenetwork);
            }
            sleep(500);
            GetTradeNetwork getTradeNetwork = new GetTradeNetwork();
            getTradeNetwork.execute();
            inputDialog.cancel();
        });
        ((Button) vv.findViewById(R.id.addTradeNetworkDecline)).setOnClickListener(v->{
            inputDialog.cancel();
        });
        inputDialog.show();
    }

    //Navigation Drawer

    private void createNavigationMenu() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateNavigationMenuValues(){
        navigationView.getMenu().clear();

        if(allTradeNetworks.size() > 0){
            for(TradeNetwork tradenetwork : allTradeNetworks){
                navigationView.getMenu().add(Menu.NONE, tradenetwork.id, Menu.NONE, tradenetwork.name);
            }
        }
        else navigationView.getMenu().add(Menu.NONE, 0, Menu.NONE, "Еще нет торговых сетей");


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        changeMenuOptions(true);
        currentTradeNetworkId = item.getItemId();
        setTitle(TradeNetwork.findTradeNetworkById(allTradeNetworks, currentTradeNetworkId).name);
        getDatabaseInfo();
        if(allShops.size() > 0){
            searchForTradeNetworkShop();
            setListViewAdapter();
        }

        DrawerLayout dl = (DrawerLayout)findViewById(R.id.drawer_layout);

        if(dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void searchForTradeNetworkShop(){
        currentTradingNetworkShops = new ArrayList<>();
        for(Shop shop : allShops){
            if(shop.tradeNetworkId == currentTradeNetworkId) currentTradingNetworkShops.add(shop);
        }
    }

    class GetTradeNetwork extends AsyncTask<Void, Void, ArrayList<TradeNetwork>> {
        @Override
        protected ArrayList<TradeNetwork> doInBackground(Void... unused) {
            return (ArrayList<TradeNetwork>) dataHandler.getDB().tradeNetworkDao().getAll();
        }
        @Override
        protected void onPostExecute(ArrayList<TradeNetwork> tradeNetworks) {
            allTradeNetworks = tradeNetworks;
            Toast.makeText(getApplicationContext(), "Торговые сети загружены(" + String.valueOf(allTradeNetworks.size()) + ")", Toast.LENGTH_SHORT)
                    .show();
            updateNavigationMenuValues();
        }
    }

    class GetShop extends AsyncTask<Void, Void, ArrayList<Shop>> {
        @Override
        protected ArrayList<Shop> doInBackground(Void... unused) {
            return (ArrayList<Shop>) dataHandler.getDB().shopDao().getAll();
        }
        @Override
        protected void onPostExecute(ArrayList<Shop> shopArrayList) {
            allShops = shopArrayList;
            if(currentTradeNetworkId != -1){
                searchForTradeNetworkShop();
                setListViewAdapter();
                //((ArrayAdapter) departmentsList.getAdapter()).notifyDataSetChanged();
            }
            Toast.makeText(getApplicationContext(), "Магазины(" + String.valueOf(allShops.size()) + ")", Toast.LENGTH_SHORT)
                    .show();
        }
    }


}