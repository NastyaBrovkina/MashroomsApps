package com.study.mashroomsapps.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "MASHROOMS".
 */
@Entity

public class Mashrooms {

    @Id
    @SerializedName("id")
    @Expose
    private Long Id;

    @NotNull
    @Index
    @SerializedName("lat")
    @Expose
    private String Latidute;

    @NotNull
    @Index
    @SerializedName("long")
    @Expose
    private String Longtidute;

    @NotNull
    @Index
    @SerializedName("desc")
    @Expose
    private String Description;

    @Generated
    public Mashrooms() {
    }

    public Mashrooms(Long Id) {
        this.Id = Id;
    }

    @Generated
    public Mashrooms(Long Id, String Latidute, String Longtidute, String Description) {
        this.Id = Id;
        this.Latidute = Latidute;
        this.Longtidute = Longtidute;
        this.Description = Description;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    @NotNull
    public String getLatidute() {
        return Latidute;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLatidute(@NotNull String Latidute) {
        this.Latidute = Latidute;
    }

    @NotNull
    public String getLongtidute() {
        return Longtidute;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLongtidute(@NotNull String Longtidute) {
        this.Longtidute = Longtidute;
    }

    @NotNull
    public String getDescription() {
        return Description;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDescription(@NotNull String Description) {
        this.Description = Description;
    }

}
