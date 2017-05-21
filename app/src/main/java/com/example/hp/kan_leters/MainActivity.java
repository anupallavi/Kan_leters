package com.example.hp.kan_leters;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText Pass,new_pass,pre_pass;
    Button open,set,set_new_pass;
    AlertDialog.Builder builder;
    static String saved_pass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pass = (EditText) findViewById(R.id.password);
        open = (Button) findViewById(R.id.open);
        set = (Button) findViewById(R.id.setpassword);


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
                    startActivity(new Intent(MainActivity.this, CalendarActivity.class));
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

}
