package com.study.mashroomsapps.serverdata;

/**
 * Created by nvv on 03.02.2018.
 */

public class MashroomData {

    private Long id;

    int latidute;

    int longidute;


    String description;

    public MashroomData() {
    }

    public MashroomData(int latidute, int longidute, String description) {
        this.latidute = latidute;
        this.longidute = longidute;
        this.description = description;
    }

    public int getLatidute() {
        return latidute;
    }

    public int getLongidute() {
        return longidute;
    }

    public String getDescription() {
        return description;
    }
}
