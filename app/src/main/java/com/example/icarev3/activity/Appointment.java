package com.example.icarev3.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icarev3.R;
import com.example.icarev3.database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class Appointment extends AppCompatActivity {
    ImageButton addAppointment;
    Integer profileId;
    ListView appointmentListView;
    DBHelper mydb;
    ArrayList<HashMap<String, String>> appointmentlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment);
        addAppointment = (ImageButton) findViewById(R.id.ivAddAppointment);
        Intent intent = getIntent();
        profileId = intent.getIntExtra("profile_id", 0);
        appointmentListView = (ListView) findViewById(R.id.lvAppointment);
        appointmentlist = new ArrayList<HashMap<String, String>>();
        mydb = new DBHelper(this);
        appointmentlist = mydb.getAllAppointment(profileId);
        ListAdapter adapter = new SimpleAdapter(
                Appointment.this, appointmentlist,
                R.layout.appointment_list_item, new String[]{"title", "date", "time"},
                new int[]{R.id.tvAppointmentTitle, R.id.tvAppointmentDate, R.id.tvAppointmentTime});

        appointmentListView.setAdapter(adapter);


        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAppointmentIntent = new Intent(getApplicationContext(), AddAppointment.class);
                addAppointmentIntent.putExtra("profile_id", profileId);
                startActivity(addAppointmentIntent);

            }
        });

        appointmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent AppointmentDetail = new Intent(Appointment.this, AppointmentDetails.class);
                Integer appointmentId = Integer.parseInt(appointmentlist.get(position).get("appointment_id"));
                AppointmentDetail.putExtra("appointment_id", appointmentId);
                AppointmentDetail.putExtra("profile_id", profileId);
                startActivity(AppointmentDetail);

            }
        });

        appointmentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Integer AppointmentId = Integer.parseInt(appointmentlist.get(position).get("appointment_id"));
                String AppointmentTitle = appointmentlist.get(position).get("title");

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Appointment.this);
                alertDialogBuilder.setTitle(AppointmentTitle);
                alertDialogBuilder
                        .setMessage("Do you want to delete this Appointment ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteAppointment(AppointmentId);
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return false;
            }
        });
    }
}
