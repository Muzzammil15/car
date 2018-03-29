package com.example.muzza.carbon1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {


    EditText editUserHistory;

    DatabaseReference dbref;

    String userMailAddress;

    double totalDistance, averageSpeed, averageTime;

    ArrayList userDataArr = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        editUserHistory = (EditText) findViewById(R.id.editUserHistory);

       // editUserHistory.setEnabled(false);


        dbref = FirebaseDatabase.getInstance().getReference().child("UserTrackingData");


        ///// Retrieve user email from mainActivity
        Bundle emailData = getIntent().getExtras();

        if (emailData == null) {
            return;
        }

        userMailAddress = emailData.getString("userEMailAddr");

        /// End retrieve ///
    }


    //Firebase Read//
    public void firebaseRead(){

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String emailAdd = ds.child("email").getValue(String.class);
                    double avgSpeed = Double.parseDouble(ds.child("avgSpeed").getValue(String.class));
                    double totalDist = Double.parseDouble(ds.child("totalDistance").getValue(String.class));
                    double totalTime = Double.parseDouble(ds.child("totalTime").getValue(String.class));
                    String currDate = ds.child("currentDate").getValue(String.class);

                  /*  Toast.makeText(HistoryActivity.this, emailAdd + " - " + String.valueOf(avgSpeed) + " - " + String.valueOf(totalDist) + " - " +
                                    String.valueOf(totalTime) + " - " + currDate, Toast.LENGTH_SHORT).show(); */

                    if(emailAdd.equals(userMailAddress)){
                       String listStr = emailAdd + "|" + currDate + "|" + totalDist + "|" + avgSpeed + "|" + totalTime;
                        userDataArr.add(listStr);


                      // Toast.makeText(HistoryActivity.this, emailAdd + " - " + emailAdd, Toast.LENGTH_SHORT).show();

                    }

                }//

                editUserHistory.setText(userDataArr.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    } // End firebaseRead

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRead();
    }
}
