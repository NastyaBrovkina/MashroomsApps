package com.study.mashroomsapps.serverdata.IMashrooms;


import com.study.mashroomsapps.entities.Mashrooms;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by nvv on 03.02.2018.
 */

public interface IMashroomClient {

    @GET("/userdata/")
    Call<List<Mashrooms>> getDataFromServer();

    @POST("/userdata/")
    void sendDataToServer(List<Mashrooms> Mashroomss);
}
