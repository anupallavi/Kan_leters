package com.example.hp.kan_leters;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button btnSubmit;
    Spinner spinner;
    EditText Pass,new_pass,pre_pass;
    Button open,set,set_new_pass;
    AlertDialog.Builder builder;
    ToggleButton toggleButton;
    Locale myLocale;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String FILE_DIR = "diaryContent";
    static String saved_pass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Pass = (EditText) findViewById(R.id.password);
        open = (Button) findViewById(R.id.open);
        set = (Button) findViewById(R.id.setpassword);

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
            File f = new File(Environment.getExternalStorageDirectory(), FILE_DIR);
            if (!f.exists()) {
                f.mkdirs();
            }
        }





        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPassword().equals(Pass.getText().toString())) {
                    startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                } else {

                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Wrong password");
                    builder.setMessage("please enter correct password..");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //close alert dialog
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }



    public void setpassword(View view){
        setContentView(R.layout.setpassword);
        pre_pass = (EditText)findViewById(R.id.previous_pass);
        new_pass = (EditText)findViewById(R.id.new_pass);
        set_new_pass=(Button)findViewById(R.id.set_new_password);
        set_new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  writeToUserNameAndPassword(pre_pass.getText().toString());
                if (getPassword().equals(pre_pass.getText().toString()))
                {
                    writeToUserNameAndPassword(new_pass.getText().toString());
                    Toast.makeText(getBaseContext(),"new password saved",Toast.LENGTH_LONG).show();
                    //setContentView(R.layout.activity_main);
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    // they have the right userName and password
                }
                else
                {
                    // these preference Strings for their userName/password have both not been created
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("previous password is wrong");
                    builder.setMessage("please enter correct password..");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //close alert dialog
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterlay = ArrayAdapter.createFromResource(this,
                R.array.language_layout, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterlay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapterlay);


        btnSubmit = (Button) findViewById(R.id.btnSet);


        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                String language = String.valueOf(spinner.getSelectedItem());
                if(language.equals("Kannada")){
                    setLocale("kn");}
                else{
                    setLocale("");
                }
            }


        });



    }
    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


            // do your work here
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);

    }
    public String getPassword()
    {
        SharedPreferences sp = getSharedPreferences("userNameAndPassword", 0);
        String str = sp.getString("password","");
        return str;
    }

    public void writeToUserNameAndPassword(String password)
    {
        SharedPreferences.Editor pref =
                getSharedPreferences("userNameAndPassword",0).edit();

        pref.putString("password", password);
        pref.commit();
    }

    public void password(){

       /* if(!(pre_pass.getText().toString().equals(saved_pass) ))
        {
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("previous password is worng");
            builder.setMessage("please enter correct password..");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //close alert dialog
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {*/
        //  saved_pass = new_pass.getText().toString();
        Toast.makeText(getBaseContext(),"new password saved",Toast.LENGTH_LONG).show();
        //setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, CalendarActivity.class));
        // }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


}
