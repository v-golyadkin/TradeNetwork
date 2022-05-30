package com.example.rpm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rpm.modelsDB.Shop;

import java.util.List;

public class ShopListAdapter extends ArrayAdapter<Shop> {

    private LayoutInflater inflater;
    private Context thisContext;
    private int layout;
    private List<Shop> shop;

    public ShopListAdapter(@NonNull Context context, int resource, @NonNull List<Shop> objects) {
        super(context, resource, objects);
        this.shop = objects;
        this.layout = resource;
        this.thisContext = context;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView nameView = view.findViewById(R.id.std_name_text_view);

        TextView adressView = view.findViewById(R.id.std_adress_text_view);



        setViewListeners(nameView, adressView, position);

        Shop shop = this.shop.get(position);

        nameView.setText(shop.name);
        adressView.setText(shop.adress);


        return view;
    }
    private void setViewListeners(TextView nameView, TextView adressView, int position){
        //Long Click
        nameView.setOnLongClickListener(v->{
            ((ShopListActivity)thisContext).sortByColumn(0);
            return true;
        });
        adressView.setOnLongClickListener(v->{
            ((ShopListActivity)thisContext).sortByColumn(1);
            return true;
        });


        //Short Click
        nameView.setOnClickListener(v->{
            ((ShopListActivity)thisContext).simpleClick(position);
        });
        adressView.setOnClickListener(v->{
            ((ShopListActivity)thisContext).simpleClick(position);
        });

    }


    public interface ISortByColumn{
        void sortByColumn(int columnId);
        void simpleClick(int position);
    }
}
