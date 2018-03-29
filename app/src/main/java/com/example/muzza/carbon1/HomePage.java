package com.example.muzza.carbon1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    TextView textViewMovemnet;
    EditText editTextDistance, editTextSpeed, editTextTimeInterval;
    Button buttonHistory;

    LocationManager locationManager;
    GoogleApiClient mLocationClient;
    LocationListener mListener;

    String vehicle = "";
    String vehicleCat = "";

    double lat, lon;

    double carbonDioxideVal;

    double distance, speed;

    double carbonFootprint;

    double x1, x2, y1, y2, t1, t2;
    double x_Diff, y_Diff, timeInterval;


    double total_TimeIntervals = 0;
    double total_DistanceMoved = 0;
    double avg_Speed = 0;


    CountDownTimer timer;
    double finishTime;

    String userMail;

    DatabaseReference dbref;

    DatabaseReference dbref_Profile;

    double bicycle[] = {10, 15};
    double motorcycle[] = {15, 100};
    double car[] = {30, 120};
    double bus[] = {25, 80};
    double truck[] = {25, 40};
    double metro[] = {15, 40};



    //////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewMovemnet = (TextView) findViewById(R.id.textViewMovement);
        editTextDistance = (EditText) findViewById(R.id.editTextDistance);
        editTextSpeed = (EditText) findViewById(R.id.editTextSpeed);
        editTextTimeInterval = (EditText) findViewById(R.id.editTextTimeInterval);

        buttonHistory = (Button) findViewById(R.id.buttonHistory);

        dbref = FirebaseDatabase.getInstance().getReference().child("UserTrackingData");

        dbref_Profile = FirebaseDatabase.getInstance().getReference().child("UserProfileData");


        //GPS Enabled ?
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context);

        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            // Build the alert dialog
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "Cannot use app without GPS", Toast.LENGTH_SHORT).show();
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }


        /// =================================== ///

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ///// Retrieve user email from mainActivity
        Bundle emailData = getIntent().getExtras();

        if (emailData == null) {
            return;
        }

        userMail = emailData.getString("userEMail");

        TextView txtUID = (TextView) findViewById(R.id.textViewUId);
        txtUID.setText(userMail);

        /// End retrieve ///



    }

    //Network available ?
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(HomePage.this, UserProfileActivity.class);

            i.putExtra("userEMail", userMail);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();

                if(String.valueOf(lat).isEmpty()){
                    lat = 0;
                }

                if(String.valueOf(lon).isEmpty()){
                    lon = 0;
                }

           //    Toast.makeText(HomePage.this, "Location: " + location.getLatitude() + " - " + location.getLongitude(), Toast.LENGTH_SHORT).show();




            }


        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(4000);
        request.setFastestInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, request, mListener);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        return;


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        return;


    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, mListener);

    }

    @Override
    protected void onStart() {
        super.onStart();


        firebaseRead(); // call to fetch fm db

    }

    //Firebase Read//
    public void firebaseRead(){

        dbref_Profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    String emailAdd = ds.child("email").getValue(String.class);
                    int userAge = Integer.parseInt(ds.child("age").getValue(String.class));
                    String fname = ds.child("firstname").getValue(String.class);
                    String lname = ds.child("lastname").getValue(String.class);
                    String vehicleType = ds.child("vehicle").getValue(String.class);
                    String vehCat = ds.child("vehicleCateg").getValue(String.class);



                  /*  Toast.makeText(HistoryActivity.this, emailAdd + " - " + String.valueOf(avgSpeed) + " - " + String.valueOf(totalDist) + " - " +
                                    String.valueOf(totalTime) + " - " + currDate, Toast.LENGTH_SHORT).show(); */

                    if(emailAdd.equals(userMail)){
                        //String listStr = emailAdd + "|" + currDate + "|" + totalDist + "|" + avgSpeed + "|" + totalTime;
                       // userDataArr.add(listStr);

                        vehicle = vehicleType;
                        vehicleCat = vehCat;


                        // Toast.makeText(HomePage.this, vehicle + " - " + vehicleCat, Toast.LENGTH_SHORT).show();

                    }

                }//



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;

            }
        });

    } // End firebaseRead

    @Override
    protected void onResume() {
        super.onResume();

        /// Location
        mLocationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationClient.connect();
        /////////



            // Start timer
            timer =   new CountDownTimer(6000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if(((millisUntilFinished/1000) == 4)){
                        x1 = lat;
                        y1 = lon;

                        t1 = millisUntilFinished/1000;
                    }

                    if(((millisUntilFinished/1000) == 1)){
                        x2 = lat;
                        y2 = lon;

                        t2 = millisUntilFinished/1000;

                        //timeInterval = t2 - t1;



                        BigDecimal bd1 = new BigDecimal(x1);
                        bd1 = bd1.round(new MathContext(7));
                        x1 = bd1.doubleValue();

                        BigDecimal bd2 = new BigDecimal(x2);
                        bd2 = bd2.round(new MathContext(7));
                        x2 = bd2.doubleValue();

                        BigDecimal bd3 = new BigDecimal(y1);
                        bd3 = bd3.round(new MathContext(7));
                        y1 = bd3.doubleValue();

                        BigDecimal bd4 = new BigDecimal(y2);
                        bd4 = bd4.round(new MathContext(7));
                        y2 = bd4.doubleValue();


                        x_Diff = Math.abs(x2) - Math.abs(x1);
                        x_Diff = Math.abs(x_Diff);

                        y_Diff = Math.abs(y2) - Math.abs(y1);
                        y_Diff = Math.abs(y_Diff);

                        // Toast.makeText(getApplicationContext(), "x_Diff: " + x_Diff  + ";  y-diff: " + y_Diff, Toast.LENGTH_SHORT).show();

                        // User Vehicle info

                        // CAR
                        if(vehicle.equals("Car") && vehicleCat.equals("Small")){
                            carbonDioxideVal = 0.14545;
                        }

                        if(vehicle.equals("Car") && vehicleCat.equals("Medium")){
                            carbonDioxideVal = 0.1738;
                        }

                        if(vehicle.equals("Car") && vehicleCat.equals("Average")){
                            carbonDioxideVal = 0.17887;
                        }

                        if(vehicle.equals("Car") && vehicleCat.equals("Large")){
                            carbonDioxideVal = 0.21834;
                        }
                        // END CAR /////////////

                        // Van //
                        if(vehicle.equals("Van") && vehicleCat.equals("Small")){
                            carbonDioxideVal = 0.14545;
                        }

                        if(vehicle.equals("Van") && vehicleCat.equals("Medium")){
                            carbonDioxideVal = 0.1738;
                        }

                        if(vehicle.equals("Van") && vehicleCat.equals("Average")){
                            carbonDioxideVal = 0.17887;
                        }

                        if(vehicle.equals("Van") && vehicleCat.equals("Large")){
                            carbonDioxideVal = 0.21834;
                        }
                        // END Van ////////////////////

                        // Motorbike //
                        if(vehicle.equals("Motorbike") && vehicleCat.equals("Small")){
                            carbonDioxideVal = 0.08474;
                        }

                        if(vehicle.equals("Motorbike") && vehicleCat.equals("Medium")){
                            carbonDioxideVal = 0.10323;
                        }

                        if(vehicle.equals("Motorbike") && vehicleCat.equals("Average")){
                            carbonDioxideVal = 0.11662;
                        }

                        if(vehicle.equals("Motorbike") && vehicleCat.equals("Large")){
                            carbonDioxideVal = 0.13542;
                        }
                        // END Motorbike //////////////////

                        // End User Vehicle info



                        distance = Math.sqrt(Math.pow((x2 - x1),2) + Math.pow((y2 - y1), 2));


                        carbonFootprint = distance * carbonDioxideVal;

                        speed = distance / 3;

                        // Public transport
                        if((speed >= 4.5) && (speed <= 8)){
                            vehicle = "Bus";
                            vehicleCat = "Long vehicle";
                        }

                        if((speed >= 8.5) && (speed <= 12.5)){
                            vehicle = "Metro";
                            vehicleCat = "Long vehicle";
                        }

                        // End Public transport

                        timeInterval = t2 - t1;
                        timeInterval = Math.abs(timeInterval);

                        editTextDistance.setText(String.valueOf(distance));
                        editTextSpeed.setText(String.valueOf(speed));
                        editTextTimeInterval.setText(String.valueOf(timeInterval));




                        if((speed == 0.0 || speed < 0.00005)){

                            //// Current time ////
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date today = Calendar.getInstance().getTime();
                            String reportDate = df.format(today);

                            ///

                            textViewMovemnet.setText("still");

                            avg_Speed = total_DistanceMoved / total_TimeIntervals;

                            UserTrackData userData = new UserTrackData(userMail, reportDate, String.valueOf(total_DistanceMoved), String.valueOf(total_TimeIntervals), String.valueOf(avg_Speed), String.valueOf(carbonFootprint), vehicle, vehicleCat);

                            if(String.valueOf(total_DistanceMoved).equals("0.0") || String.valueOf(total_TimeIntervals).equals("0.0") || String.valueOf(avg_Speed).equals("NaN")){
                                return;
                            }
                                dbref.push().setValue(userData);



                          /*  if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !userMail.isEmpty() || !reportDate.isEmpty()
                                    || !String.valueOf(total_DistanceMoved).isEmpty() || !String.valueOf(total_TimeIntervals).isEmpty() ||
                                    !String.valueOf(avg_Speed).isEmpty() || !String.valueOf(carbonFootprint).isEmpty() || !vehicle.isEmpty() ||
                                    !vehicleCat.isEmpty()){


                                dbref.push().setValue(userData);

                            }
                            else {
                                return;
                            } */


                            //  Toast.makeText(HomePage.this, "final: " + total_DistanceMoved + " ; " + total_TimeIntervals + " ; " + avg_Speed, Toast.LENGTH_SHORT).show();

                            avg_Speed = total_DistanceMoved = total_TimeIntervals = 0;



                        }
                        else {


                            textViewMovemnet.setText("moving");

                            total_DistanceMoved += distance;
                            total_TimeIntervals += timeInterval;
                        }

                    }
                    finishTime = (millisUntilFinished/1000);
                }

                public void onFinish() {
                    //Toast.makeText(getApplicationContext(), "Receive done" + finishTime, Toast.LENGTH_SHORT).show();
                    this.start();
                }
            }.start();

            Toast.makeText(getApplicationContext(), "Tracking started", Toast.LENGTH_SHORT).show();



    }





    public void viewHistory(View view){
        Intent i = new Intent(HomePage.this, HistoryActivity.class);
        i.putExtra("userEMailAddr", userMail);
        startActivity(i);
    }


}
