package com.example.digitalnet.anas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import connection.MyHttpURLConnection;
import types.Listener;

public class LoginActivity extends AppCompatActivity {
    String loginUrl ="api/login";
    String loginParameters;
    String user_name;
    String pass_word;

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Resources res = getResources();
        String hostName = res.getString(R.string.hostName);
        loginUrl = hostName + loginUrl;

        Button hostIP = (Button) findViewById(R.id.ip_button);
        final Button saveIp = (Button) findViewById(R.id.save_ip_button);
        final EditText newIp = (EditText) findViewById(R.id.ipEditText);
        final String current_ip = getApplicationContext().getResources().getString (R.string.hostName);
        newIp.setText(current_ip.replace("http://","").replace(":7000/",""));

        //http://192.168.137.1:7000/
        hostIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveIp.getVisibility() == View.VISIBLE) {
                    // Its visible
                    saveIp.setVisibility(v.INVISIBLE);
                    newIp.setVisibility(v.INVISIBLE);
                } else {
                    // Either gone or invisible
                    saveIp.setVisibility(v.VISIBLE);
                    newIp.setVisibility(v.VISIBLE);
                }
            }
        });

        saveIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("Env", MODE_PRIVATE).edit();
                editor.putString("host", newIp.getText().toString());
                editor.apply();
            }
        });

        Button login = (Button) findViewById(R.id.login);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        //user_name = username.getText().toString();
        //pass_word = password.getText().toString();
        //System.out.println("louai user name "+ username.getText().toString() );
        //System.out.println("louai password "+ password.getText().toString() );
        //loginParameters = "{\"userName\":\""+user_name+"\",\"password\":\""+pass_word+"\"}";

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("louai user name "+ username.getText().toString() );
                System.out.println("louaia password "+ password.getText().toString() );
                user_name = username.getText().toString();
                pass_word = password.getText().toString() ;
                loginParameters = "{\"username\":\""+user_name+"\",\"password\":\""+pass_word+"\"}";
                if (user_name != null && pass_word != null){
                    //send username & password to the server
                    System.out.println("louai parmaeters " + loginParameters);
                    new LoginTask().execute();
                }
            }
        });

    }

    private class LoginTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            System.out.println("louai on pre execute");

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            System.out.println("louai do in pack ground");
            System.out.println("louai url " + loginUrl);
            System.out.println("louai url parametere" + loginParameters );
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                return  httpURLConnectionExample.sendPost(loginUrl, loginParameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("louai on post execute");
            System.out.println("louai result" + result);

            try {
                String MY_PREFS_NAME_ = "log_in";
/*                SharedPreferences.Editor editor_ = getSharedPreferences(MY_PREFS_NAME_, MODE_PRIVATE).edit();
                editor_.putString("userName", user_name);
                editor_.putString("password", pass_word);
                editor_.putString("listener_id", "1");
                editor_.commit();*/

                //Listener listener_ = new Listener(user_name,pass_word,"1");

                //Intent mainIntent_ = new Intent(LoginActivity.this , SemesterActivity.class);
                //startActivity(mainIntent_);
                //finish();
                System.out.println("koooooooooooooooooooooo" +result);
                JSONObject obj = new JSONObject(result);
                Boolean id = obj.getBoolean("status");
                if(id) {
                    String MY_PREFS_NAME = "log_in";
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("userName", user_name);
                    editor.putString("password", pass_word);
                    editor.putString("listener_id", "1");
                    editor.commit();

                    Listener listener = new Listener(user_name,pass_word,"1");

                    Intent mainIntent = new Intent(LoginActivity.this , SemesterActivity.class);
                    startActivity(mainIntent);
                    finish();

                    System.out.println("louai you can acceess " + id);
                }else {
                    TextView error = (TextView) findViewById(R.id.error_message);
                    error.setVisibility(View.VISIBLE);
                    System.out.println("louai you can not acceess " + id);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

            // Do things like hide the progress bar or change a TextView
        }
    }


}
