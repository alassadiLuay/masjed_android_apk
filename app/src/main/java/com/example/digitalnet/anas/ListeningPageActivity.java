package com.example.digitalnet.anas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.SemesterAdapter;
import connection.MyHttpURLConnection;
import dataModel.AnasContract;
import dataModel.DatabaseController;

public class ListeningPageActivity extends AppCompatActivity {

    String checkChapterURL = "masjed_Bader/index.php/teacher_api/check_chapter/format/json";
    String checkChapterParameters;

    String addPageURL = "masjed_Bader/index.php/teacher_api/add_listened_page/format/json";
    String addPageParameters;

    String semester_id;
    String teacher_id;
    String listener_id;
    String student_id;

    String chapter_id;
    String last_page_id;
    String message;
    String flag;

    String final_msg;
    String final_err_msg;
    String final_err_flag;
    String final_chapter_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_page);

        Resources res = getResources();
        String hostName = res.getString(R.string.hostName);
        checkChapterURL = hostName + checkChapterURL;
        addPageURL = hostName + addPageURL;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button add = (Button) findViewById(R.id.add_page);


        semester_id = getIntent().getStringExtra("semester_id");
        teacher_id = getIntent().getStringExtra("teacher_id");
        listener_id = getIntent().getStringExtra("listener_id");
        student_id = getIntent().getStringExtra("student_id");

        checkChapterParameters = "{\"semester_id\":\""+ semester_id +"\",\n" +"\"student_id\":\""+ student_id +"\"}";

        new check_chapter_task().execute();



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addNote = (EditText) findViewById(R.id.addNote);

                String note = addNote.getText().toString();

                //loginParameters = "{\"userName\":\""+user_name+"\",\"password\":\""+pass_word+"\"}";
                addPageParameters = "{\"semester_id\":\""+ semester_id +"\",\"student_id\":\""+ student_id +"\",\"teacher_id\" :\""+ teacher_id +"\",\"listener_id\":\""+listener_id+"\",\"chapter_id\":\""+chapter_id+"\",\"page_number\":\""+last_page_id+"\",\"note\":\""+note+"\" }";

                new add_page_task().execute();

            }
        });

    }

    private class add_page_task extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog progressDialog = new ProgressDialog(ListeningPageActivity.this);

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
            System.out.println("louai url " + addPageURL);
            System.out.println("louai url parametere" + addPageParameters );
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                return  httpURLConnectionExample.sendPost(addPageURL, addPageParameters);
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
            System.out.println("louai on post execute semester");
            System.out.println("louai result after addig page" + result);
            try {
                //JSONArray jsonArray = new JSONArray(result);

                JSONObject obj = new JSONObject(result);
                final_msg = obj.getString("message");
                final_err_msg = obj.getString("error_message");
                final_err_flag = obj.getString("error_flag");
                final_chapter_msg = obj.getString("chapter_message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();

            if(final_err_flag != null && final_err_flag.equals("true")){

                AlertDialog alertDialog = new AlertDialog.Builder(ListeningPageActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(final_err_msg);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            else {
                Toast.makeText(getApplicationContext(), "The page has been added successfully" , Toast.LENGTH_SHORT).show();

                if(final_chapter_msg != null && !final_chapter_msg.equals("")){
                    Toast.makeText(getApplicationContext(), final_chapter_msg , Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(ListeningPageActivity.this, StudentActivity.class);
                intent.putExtra("teacher_id", teacher_id);
                intent.putExtra("semester_id", semester_id);
                startActivity(intent);
                finish();
            }

            // Do things like hide the progress bar or change a TextView
        }
    }

    private class check_chapter_task extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog progressDialog = new ProgressDialog(ListeningPageActivity.this);

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
            System.out.println("louai url " + checkChapterURL);
            System.out.println("louai url parametere" + checkChapterParameters );
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                return  httpURLConnectionExample.sendPost(checkChapterURL, checkChapterParameters);
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
            System.out.println("louai on post execute semester");
            System.out.println("louai result semester" + result);
            try {
                //JSONArray jsonArray = new JSONArray(result);

                JSONObject obj = new JSONObject(result);
                chapter_id = obj.getString("chapter_id");
                last_page_id = obj.getString("last_page_id");
                message = obj.getString("message");
                flag = obj.getString("flag");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("louai all semesters ");
            progressDialog.dismiss();

            if(flag.equals("false")){

                AlertDialog alertDialog = new AlertDialog.Builder(ListeningPageActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent studentIntent = new Intent(ListeningPageActivity.this, StudentActivity.class);
                                studentIntent.putExtra("semester_id",semester_id);
                                studentIntent.putExtra("teacher_id",teacher_id);
                                startActivity(studentIntent);
                                finish();
                            }
                        });
                alertDialog.show();

            }else{

                EditText chapter_number = (EditText) findViewById(R.id.chapter_number);
                chapter_number.append(chapter_id);

                EditText page_number = (EditText) findViewById(R.id.page_number);
                page_number.append(last_page_id);

            }
            // Do things like hide the progress bar or change a TextView
        }
    }
}


