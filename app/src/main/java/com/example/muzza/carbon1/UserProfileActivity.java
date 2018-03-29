package com.example.muzza.carbon1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    Spinner spinnerVehicle, spinnerVehicleCat;
    EditText editFname, editLname, editAge;

    String userMailadd;

    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        dbref = FirebaseDatabase.getInstance().getReference().child("UserProfileData");

        spinnerVehicle = (Spinner) findViewById(R.id.spinnerVehicle);
        spinnerVehicleCat = (Spinner) findViewById(R.id.spinnerVehicleCat);

        editFname = (EditText) findViewById(R.id.editFname);
        editLname = (EditText) findViewById(R.id.editLname);
        editAge = (EditText) findViewById(R.id.editAge);

        //Vehicle
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVehicle.setAdapter(adapter);

        //Vehicle Category
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.vehicleCategory_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVehicleCat.setAdapter(adapter2);


        ///// Retrieve user email from mainActivity
        Bundle emailData = getIntent().getExtras();

        if (emailData == null) {
            return;
        }

        userMailadd = emailData.getString("userEMail");

        final EditText editMail = (EditText) findViewById(R.id.editEmail);
        editMail.setText(userMailadd);

        /// End retrieve ///


    }

    //SAVE button
    public void saveFirebase(View view){
        String fname = editFname.getText().toString();

        String Lname = editLname.getText().toString();

        String age = String.valueOf(editAge.getText());

        String emailADDr = userMailadd;

        String vehicle = spinnerVehicle.getSelectedItem().toString();
        String vehicleCat = spinnerVehicleCat.getSelectedItem().toString();

        UserProfileFirebase uProfile = new UserProfileFirebase(fname, Lname, age, emailADDr, vehicle, vehicleCat);

        dbref.push().setValue(uProfile);

        Toast.makeText(UserProfileActivity.this, "Insert to firebase DONE !!!" , Toast.LENGTH_SHORT).show();



    }
}
