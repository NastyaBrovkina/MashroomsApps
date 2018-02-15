package com.study.mashroomsapps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.study.mashroomsapps.Dialogs.MyAlertDialog;
import com.study.mashroomsapps.entities.Mashrooms;
import com.study.mashroomsapps.entities.MashroomsDao;
import com.study.mashroomsapps.serverdata.MashroomClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng myOldposition;
    List<MarkerOptions> markerCollection;
    List<MarkerOptions> loadedMarkerCollection;
    List<Polyline> polylines;

    private Switch sw;

    MashroomClient mashroomClient;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;
    private long FASTEST_INTERVAL = 2000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;

        sw = (Switch) findViewById(R.id.switchBtn);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

      /*  ((ImageButton) findViewById(R.id.btnReload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    updateMarkerList();
                }else{
                    MyAlertDialog myAlertDialog = new MyAlertDialog(context);
                    myAlertDialog.setMessage(context.getString(R.string.dialog_info_map_internet_deprecated_text));
                    myAlertDialog.setPositiveButton(context.getString(R.string.dialog_info_map_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    myAlertDialog.show();
                }
            }
        });*/
        ((ImageButton) findViewById(R.id.btnMyloc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);;
                boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
               if(enabled==true) {
                   updateMap(myOldposition);
               }else{
                   MyAlertDialog myAlertDialog = new MyAlertDialog(context);
                   myAlertDialog.setMessage("Включите GPS");
                   myAlertDialog.setPositiveButton(context.getString(R.string.dialog_info_map_positive), new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   });
                   myAlertDialog.show();
               }


                }

        });
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(!markerCollection.isEmpty()){
            MyAlertDialog myAlertDialog = new MyAlertDialog(context);
            myAlertDialog.setMessage(context.getString(R.string.dialog_info_map_destroy_activity_text));
            myAlertDialog.setNegativeButton(context.getString(R.string.dialog_info_map_negative), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            myAlertDialog.setPositiveButton(context.getString(R.string.dialog_info_map_positive), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MashroomsDao mashroomsDao = getDaoSession().getMashroomsDao();
                    for(MarkerOptions item: markerCollection) {
                        saveMarkerInDB(mashroomsDao, item);
                    }
                    dialog.dismiss();
                    finish();
                }
            });
            myAlertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(markerCollection == null){
            markerCollection = new ArrayList<>();
        }
        if(loadedMarkerCollection == null){
            loadedMarkerCollection = new ArrayList<>();
            List<Mashrooms> mashroomses = getDaoSession().getMashroomsDao().loadAll();
            if(!mashroomses.isEmpty()){
                for (Mashrooms item : mashroomses){
                    addMarkerToList(item);
                }
            }
        }
        if(polylines == null){
            polylines = new ArrayList<>();
        }
        if(mashroomClient == null){
            mashroomClient = new MashroomClient();
        }
        if(isNetworkAvailable()){
            updateMarkerList();
        }else{
            MyAlertDialog myAlertDialog = new MyAlertDialog(context);
            myAlertDialog.setMessage(context.getString(R.string.dialog_info_map_internet_deprecated_text));
            myAlertDialog.setPositiveButton(context.getString(R.string.dialog_info_map_positive), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            myAlertDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(false);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                buildChooseDialog(latLng);
            }
        });
        startLocationUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, context.getString(R.string.error_maps_gps), Toast.LENGTH_SHORT).show();
    }

    private void updateMarkerList(){
        mashroomClient.getDataFromServer(new Callback<List<Mashrooms>>() {
            @Override
            public void onResponse(Call<List<Mashrooms>> call, Response<List<Mashrooms>> response) {
                if(response.isSuccessful()){
                    for(Mashrooms item: response.body()){
                        addMarkerToList(item);
                    }
                    updateMap(myOldposition);
                }
            }

            @Override
            public void onFailure(Call<List<Mashrooms>> call, Throwable t) {
                Toast.makeText(context,"connection faild", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addMarkerToList(Mashrooms item){
        MarkerOptions markerOptions =
                new MarkerOptions().
                        position(new LatLng(Double.valueOf(item.getLatidute()),Double.valueOf(item.getLongtidute()))).
                        title(item.getDescription()).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.bol_grib));
        loadedMarkerCollection.add(markerOptions);
    }

    private void saveMarkerInDB(MashroomsDao mashroomsDao, MarkerOptions item){
        Mashrooms mashrooms = new Mashrooms();
        mashrooms.setLatidute(String.valueOf(item.getPosition().latitude));
        mashrooms.setLongtidute(String.valueOf(item.getPosition().longitude));
        mashrooms.setDescription(item.getTitle());
        mashroomsDao.insert(mashrooms);
    }

    private void startLocationUpdates() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


                mMap.setMyLocationEnabled(true);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        updateMarkerUserPlace(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void updateMarkerUserPlace(Location lastLocation){
        updateMap(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()));
    }

    private void updateMap(LatLng myposition){
        mMap.clear();
        if(myOldposition == null){
            myOldposition = myposition;
        }
        for(MarkerOptions item: markerCollection){
            mMap.addMarker(item);
        }
        for(MarkerOptions item: loadedMarkerCollection){
            mMap.addMarker(item);
        }
        mMap.addMarker(new MarkerOptions().position(myposition).title("Me").icon(BitmapDescriptorFactory.fromResource(R.drawable.position)));
        if(sw.isChecked()){
        polylines.add(new Polyline(myOldposition,myposition));
        for(Polyline item : polylines){
            drowUserPolyline(item.getFrom(),item.getTo());
        }}
        myOldposition = myposition;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));
    }



    private void drowUserPolyline(LatLng from, LatLng to){

        mMap.addPolyline(new PolylineOptions()
                .add(from, to)
                .width(5)
                .color(Color.GREEN));




    }

    private void buildChooseDialog(final LatLng latLng){
        MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        myAlertDialog.setMessage(context.getString(R.string.dialog_info_map_add_marker_text));
        myAlertDialog.setNegativeButton(context.getString(R.string.dialog_info_map_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        myAlertDialog.setPositiveButton(context.getString(R.string.dialog_info_map_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                buildEditDialog(latLng);
            }
        });
        myAlertDialog.show();
    }

    private void buildEditDialog(final LatLng latLng){

        final View view = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.edit_text,null);

        MyAlertDialog myAlertDialog = new MyAlertDialog(context);
        myAlertDialog.setMessage(context.getString(R.string.dialog_info_map_add_marker_text));
        myAlertDialog.setView(view);
        myAlertDialog.setNegativeButton(context.getString(R.string.dialog_info_map_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        myAlertDialog.setPositiveButton(context.getString(R.string.dialog_info_map_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = ((EditText)view.findViewById(R.id.etMarkerDescr)).getText().toString();
                dialog.dismiss();
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(message).icon(BitmapDescriptorFactory.fromResource(R.drawable.bol_grib));
                markerCollection.add(markerOptions);
                mMap.addMarker(markerOptions);
            }
        });
        myAlertDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private class Polyline{

        private LatLng from;
        private LatLng to;

        public Polyline(LatLng from, LatLng to) {
            this.from = from;
            this.to = to;
        }

        public LatLng getFrom() {
            return from;
        }

        public LatLng getTo() {
            return to;
        }
    }

}
