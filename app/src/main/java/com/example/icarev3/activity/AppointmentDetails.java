package com.example.icarev3.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icarev3.R;
//import com.example.icarev3.alarm.AlertReceiver;
import com.example.icarev3.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AppointmentDetails extends AppCompatActivity {
    CheckBox reminder;
    EditText reminderDate;
    EditText reminderTime;
    TextView title;
    TextView doctor;
    TextView location;
    TextView date;
    TextView time;
    TextView dateTime;
    DBHelper dbHelper;
    Button saveReminder;
    Button cancelReminder;
    DatePickerDialog reminderDatePicker;
    private SimpleDateFormat dateFormatter;
    Integer profileId;
    HashMap<Integer,Calendar> reminderMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_detail);
        Intent intent = getIntent();
        profileId = intent.getIntExtra("profile_id", 0);
        final Integer appointment_id = intent.getIntExtra("appointment_id", 0);
        reminderMap=new HashMap<>();
        Toast.makeText(this, "" + appointment_id, Toast.LENGTH_SHORT).show();
        dbHelper = new DBHelper(getApplicationContext());
        HashMap<String, String> map = new HashMap<String, String>();
        map = dbHelper.getOneAppointmentDetails(appointment_id);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        reminder = (CheckBox) findViewById(R.id.cbAppointmentDetailReminder);
        reminderDate = (EditText) findViewById(R.id.etAppointmentReminderDate);
        reminderDate.setInputType(InputType.TYPE_NULL);
        reminderDate.requestFocus();
        reminderTime = (EditText) findViewById(R.id.etAppointmentReminderTime);
        reminderTime.setInputType(InputType.TYPE_NULL);
        reminderTime.requestFocus();

        title = (TextView) findViewById(R.id.tvAppointmentDetailTitle);
        doctor = (TextView) findViewById(R.id.tvAppointmentDetailDoctor);
        location = (TextView) findViewById(R.id.tvAppointmentDetailLocation);
        date = (TextView) findViewById(R.id.tvAppointDetailDate);
        time = (TextView) findViewById(R.id.tvAppointDetailTime);
        dateTime = (TextView) findViewById(R.id.tvAppointmentDetailDateTime);

        saveReminder=(Button)findViewById(R.id.btnReminderSave);
        cancelReminder=(Button)findViewById(R.id.btnReminderCancel);

        title.setText(map.get("title"));
        doctor.setText(map.get("doctor_name"));
        location.setText(map.get("care_center"));
        dateTime.setText(map.get("date") + "  " + map.get("time"));

        setReminder();
        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    date.setVisibility(View.VISIBLE);
                    time.setVisibility(View.VISIBLE);
                    reminderDate.setVisibility(View.VISIBLE);
                    reminderTime.setVisibility(View.VISIBLE);

                } else {
                    date.setVisibility(View.GONE);
                    time.setVisibility(View.GONE);
                    reminderDate.setVisibility(View.GONE);
                    reminderTime.setVisibility(View.GONE);

                }
            }
        });
        reminderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderDatePicker.show();


            }
        });

        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                reminderMap.put(appointment_id,mcurrentTime);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AppointmentDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String AmPm = "";
                        int hour;

                        if (selectedHour > 0 && selectedHour < 12) {
                            AmPm = "AM";
                            hour = selectedHour;

                        } else {
                            AmPm = "PM";
                            hour = selectedHour - 12;

                        }

                        reminderTime.setText(hour + ":" + selectedMinute + "  " + AmPm);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        saveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reminderDateString = reminderDate.getText().toString();
                String reminderTimeString = reminderTime.getText().toString();
                DBHelper db = new DBHelper(getApplicationContext());

                if(db.updateAppointment(reminderDateString,reminderTimeString,appointment_id))
                {
                    Toast.makeText(getApplicationContext(),"reminder date and time updated",Toast.LENGTH_LONG).show();
                }


                Calendar c=reminderMap.get(appointment_id);
//                startAlarm(c);

                Intent backToAppointment = new Intent(AppointmentDetails.this, Appointment.class);
                backToAppointment.putExtra("profile_id", profileId);
                startActivity(backToAppointment);
                finish();
            }
        });
    }
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void startAlarm(Calendar c) {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1);
//        }
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//    }

    public void setReminder() {

        Calendar newCalendar = Calendar.getInstance();

        reminderDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                reminderDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


}
