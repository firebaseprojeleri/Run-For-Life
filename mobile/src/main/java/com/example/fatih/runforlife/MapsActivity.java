package com.example.fatih.runforlife;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,DirectionFinderListener  {

    Activities data=null;
    Button btnHarita;
    boolean isStart=false;
    String childId;

    FirebaseUser user;
    private FirebaseAuth mAuth;
    FirebaseDatabase mFireDB;
    DatabaseReference mDBRef;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private GoogleMap mMap;
    private  GPSTracker gpsTracker;
    private Location mLocation;
    String totalTime,totalDistance;
    double latitude,longitude;
    double dataLat,dataLong,dataFinishLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnHarita=(Button)findViewById(R.id.butonHarita);

        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();


        mFireDB=FirebaseDatabase.getInstance();
        mDBRef=mFireDB.getReference("Activities");

        mDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                data=dataSnapshot.getValue(Activities.class);

                if(data.getUserID().equals(user.getUid()) && data.finishLong==0)
                {
                    dataFinishLat=data.finishLat;
                    childId=dataSnapshot.getKey();
                    dataLat=data.getStartLat();
                    dataLong=data.startLong;
                    btnHarita.setText("Finish");
                    isStart=true;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }});

            getLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       // sendrequest();
    }


    public void onClickStartBtn(View v)
    {
        if(isStart==false) {
            try {
                Activities startActivite = new Activities(user.getUid(), latitude, 0, longitude, 0, "", "");
                mDBRef.push().setValue(startActivite);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }else
            {
                sendrequest();
                getLocation();


            }
    }
    public  void getLocation()
    {
        gpsTracker=new GPSTracker(getApplicationContext());
        mLocation=gpsTracker.getLocation();

        try {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }catch (Exception e)
        {
            System.out.println("Hata"+e.getMessage());
        }
    }


    public void sendrequest()
    {

        getLocation();
        String startAdress=""+dataLat+","+dataLong;
        String finishAdress=""+latitude+","+longitude;

        try
        {
            new DirectionFinder(this,startAdress,finishAdress).execute();
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Başlangıç Noktası"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));

    }
    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }
    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            totalDistance=route.distance.text;
            totalTime=route.duration.text;
            mMap.clear();
            Activities finishActivite=new Activities(user.getUid(),dataLat,latitude,dataLong,longitude,totalDistance,totalTime);
            mDBRef.child(childId).setValue(finishActivite);
            btnHarita.setText("Start");
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
