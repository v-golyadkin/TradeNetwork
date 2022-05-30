package com.example.rpm.modelsJSON;

import com.google.gson.annotations.SerializedName;

public class CountForm {

    @SerializedName("name")
    public String name;

    @SerializedName("attime")
    public String attime;

    @SerializedName("fortime")
    public String fortime;


    @Override
    public String toString() {
        return(name + ' ' + attime + ' ' + fortime);
    }
}