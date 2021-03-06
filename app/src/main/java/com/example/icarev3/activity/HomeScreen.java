package com.example.icarev3.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icarev3.MainActivity;
import com.example.icarev3.R;
import com.example.icarev3.adapter.GridAdapter;

public class HomeScreen extends AppCompatActivity {
    GridView mainGridView;
    String[] gridTitle = {"Basic Info", "Medical Info", "Growth Status",
            "Diet", "Vaccine", "Disease", "Appointment", "Medical history",
            "Doctor", "Medical center", "Notes", "Emergency"};

    Integer[] gridImage = {R.drawable.profile, R.drawable.medicalinfo,
            R.drawable.growthstatus, R.drawable.diet, R.drawable.vaccine,
            R.drawable.disease, R.drawable.appointment, R.drawable.medicalhistory,
            R.drawable.doctor, R.drawable.hospital, R.drawable.note,
            R.drawable.emmergency};

    Integer profileId;
    ImageButton imageButtonBack;
    TextView toolbarProfileName;
    // ArrayList<String> gridTitle;
    //  ArrayList<Integer> gridImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        imageButtonBack=(ImageButton)findViewById(R.id.backButtonHomescreen);
        toolbarProfileName=(TextView)findViewById(R.id.tvHomeProfileName);
        mainGridView = (GridView) findViewById(R.id.gridView1);
        Intent intent = getIntent();
        profileId = intent.getIntExtra("profile_id", 0);

        toolbarProfileName.setText(intent.getStringExtra("profile_name"));
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, MainActivity.class));
            }
        });
        mainGridView.setAdapter(new GridAdapter(getApplicationContext(), gridTitle, gridImage));
        mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        Intent basicInfoIntent = new Intent(getApplicationContext(), BasicInfo.class);
                        basicInfoIntent.putExtra("profile_id", profileId);
                        startActivity(basicInfoIntent);
                        break;

                    case 1:
                        Intent medicalInfoIntent = new Intent(getApplicationContext(), MedicalInfo.class);
                        medicalInfoIntent.putExtra("profile_id", profileId);
                        startActivity(medicalInfoIntent);
                        break;

                    case 2:
                        Intent growthStatusIntent = new Intent(getApplicationContext(), GrowthStatus.class);
                        growthStatusIntent.putExtra("profile_id", profileId);
                        startActivity(growthStatusIntent);
                        break;

                    case 3:

                        Intent dietIntent = new Intent(getApplicationContext(), Diet.class);
                        dietIntent.putExtra("profile_id", profileId);
                        startActivity(dietIntent);
                        break;
                    case 4:
                        Intent vaccineIntent = new Intent(getApplicationContext(), Vaccine.class);
                        vaccineIntent.putExtra("profile_id", profileId);
                        startActivity(vaccineIntent);
                        break;

                    case 5:
                        break;
                    case 6:
                        Intent appointmentIntent = new Intent(getApplicationContext(), Appointment.class);
                        appointmentIntent.putExtra("profile_id", profileId);
                        startActivity(appointmentIntent);
                        break;

                    case 7:
                        break;

                    case 8:
                        Intent doctorIntent = new Intent(getApplicationContext(), Doctor.class);
                        doctorIntent.putExtra("profile_id", profileId);
                        startActivity(doctorIntent);
                        break;
                    case 9:
                        Intent medicalCenterIntent = new Intent(getApplicationContext(), MedicalCenter.class);
                        medicalCenterIntent.putExtra("profile_id", profileId);
                        startActivity(medicalCenterIntent);
                        break;

                    case 10:
                        Intent noteIntent = new Intent(getApplicationContext(), Notes.class);
                        noteIntent.putExtra("profile_id", profileId);
                        startActivity(noteIntent);

                        break;

                    case 11:
                        break;


                }
            }
        });



    }

}
