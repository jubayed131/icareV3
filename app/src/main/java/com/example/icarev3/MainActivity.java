package com.example.icarev3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.icarev3.activity.AddNewProfile;
import com.example.icarev3.activity.HomeScreen;
import com.example.icarev3.database.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView profileList;
    DBHelper mydb;
    ArrayList<HashMap<String, String>> profileListArray;
    List<String> profile_list=new ArrayList<>();
    Integer profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        profileList = (ListView) findViewById(R.id.list);
        profileListArray = new ArrayList<HashMap<String, String>>();

        mydb = new DBHelper(this);
        profileListArray = mydb.getAllProfile();
        if(profileListArray.size()==0){
            Intent createProfileIntent = new Intent(this, AddNewProfile.class);
            startActivity(createProfileIntent);
            finish();
        }
        profile_list = new ArrayList<>();
        for (int i = 0; i < profileListArray.size(); i++) {

            profile_list.
                    add(profileListArray.get(i).get("name"));
        }

        //  Toast.makeText(this,profileListArray.get(0).get("id").toString(),Toast.LENGTH_LONG).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.profile_list_item, R.id.tvProfileListItem, profile_list);
        profileList.setAdapter(arrayAdapter);

        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                profileId = Integer.parseInt(profileListArray.get(position).get("id"));


                intent.putExtra("profile_id", profileId);
                intent.putExtra("profile_name",profileListArray.get(position).get("name").toString() );
                startActivity(intent);

            }
        });
        profileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                profileId = Integer.parseInt(profileListArray.get(position).get("id"));

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("profile name");
                alertDialogBuilder
                        .setMessage("Do you want to delete all data of this user ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteProfile(profileId);
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




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent createProfileIntent = new Intent(getApplicationContext(), AddNewProfile.class);
                startActivity(createProfileIntent);
                finish();
            }
        });








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
