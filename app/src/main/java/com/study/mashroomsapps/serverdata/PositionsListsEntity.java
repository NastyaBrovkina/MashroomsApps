package com.study.mashroomsapps.serverdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.study.mashroomsapps.entities.Mashrooms;

import java.util.List;

/**
 * Created by nvv on 30.01.2018.
 */
public class PositionsListsEntity {

    @SerializedName("positions")
    @Expose
    List<Mashrooms> positionsEntities;

    public List<Mashrooms> getPositionsEntities() {
        return positionsEntities;
    }

    public void setPositionsEntities(List<Mashrooms> positionsEntities) {
        this.positionsEntities = positionsEntities;
    }
}
