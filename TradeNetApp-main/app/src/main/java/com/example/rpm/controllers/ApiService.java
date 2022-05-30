package com.example.rpm.controllers;

import android.util.Log;

import com.example.rpm.modelsJSON.CountForm;
import com.google.gson.Gson;

import org.json.JSONArray;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiService {
    public CountForm countform;
    //private ArrayList<Person> allPerson = new ArrayList<>();
    public CountForm selectCount(String shop) throws Exception {
        final OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("name", shop)
                .build();

        Request request = new Request.Builder()
                .url("http://194.87.248.41/Restaurant/shop.php")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            System.out.println(response.code());
            Gson g = new Gson();
            JSONArray json = new JSONArray(response.body().string());
            //Log.d("MyLog",""+);
            for (int i=0; i < json.length(); i++) {
                countform = g.fromJson(json.getJSONObject(i).toString(), CountForm.class);

                //allPerson.add(person);
                Log.d("MyLogi", countform.toString());
            }

        }
        return countform;


    }

    public void insertCount(String attime, String fortime, String name) throws Exception {
        final OkHttpClient client = new OkHttpClient();
        Log.d("MyLog",""+attime);
        RequestBody formBody = new FormBody.Builder()
                .add("attime", attime)
                .add("fortime", fortime)
                .add("name", name)
                .build();

        Request request = new Request.Builder()
                .url("http://194.87.248.41/Restaurant/shop.php")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            System.out.println(response.code());
        }
    }

    public void insertFaculty(String title) throws Exception {
        final OkHttpClient client = new OkHttpClient();
        Log.d("MyLog",""+title);
        RequestBody formBody = new FormBody.Builder()
                .add("title", title)
                .build();

        Request request = new Request.Builder()
                .url("http://194.87.248.41/Restaurant/shop.php")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            System.out.println(response.code());
        }
    }
}
