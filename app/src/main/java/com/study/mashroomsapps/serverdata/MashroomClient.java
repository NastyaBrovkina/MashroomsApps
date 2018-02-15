package com.study.mashroomsapps.serverdata;

import com.study.mashroomsapps.entities.Mashrooms;
import com.study.mashroomsapps.serverdata.IMashrooms.IMashroomClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nvv on 03.02.2018.
 */

public class MashroomClient extends RetrofitClient {

    IMashroomClient client = null;

    public MashroomClient() {
        client =  retrofit.create(IMashroomClient.class);
    }

    public void getDataFromServer(Callback<List<Mashrooms>> listCallback){
        client.getDataFromServer().enqueue(listCallback);
    }

    public void sendDataToServer(List<Mashrooms> Mashroomss){
        client.sendDataToServer(Mashroomss);
    }
}
