package com.example.digitalnet.anas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import adapters.SemesterAdapter;
import connection.MyHttpURLConnection;
import dataModel.AnasContract;
import dataModel.DatabaseController;
import types.Semester;

public class SemesterActivity extends AppCompatActivity {

    String semesterUrl = "api/get_semesters";
    String semesterParameters = "{}";
    //String teacherParameters = "{\"samester_id\":\"6\"}";
    String studentUrl = "get_course_students";
    String studentParameter = "{}";
    //String studentParameter = "{\"course_id\":\"6\"}";

    List<Semester> semesterArrayList = new ArrayList<Semester>();
    SemesterAdapter semesterAdapter;
    GridView semesterListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.action_settings:
                String MY_PREFS_NAME = "log_in";
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("userName", null);
                editor.putString("password", null);
                editor.putString("listener_id", null);
                editor.commit();
                Intent intent = new Intent(SemesterActivity.this,SplashActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Listener name");

        Resources res = getResources();
        String hostName = res.getString(R.string.hostName);
        semesterUrl = hostName + semesterUrl;
        studentUrl = hostName + studentUrl;

        String MY_PREFS_NAME = "log_in";
        SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String user_name =preferences.getString("userName" , null);
        getSupportActionBar().setTitle("اختر إحدى الفترات"); //user_name
        //toolbar.setTitleTextColor(0xFFFFFFFF);
        makeActionOverflowMenuShown();

        new SemesterTask().execute();
        /*
        new StudentTask().execute();*/

    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            System.out.println("louai exception " + e.getLocalizedMessage());
            //Log.d(TAG, e.getLocalizedMessage());
        }
    }


    private class SemesterTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog progressDialog = new ProgressDialog(SemesterActivity.this);

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
            System.out.println("louai url " + semesterUrl);
            System.out.println("louai url parametere" + semesterParameters );
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                return  httpURLConnectionExample.sendGet(semesterUrl);
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
            System.out.println(result);
            try {
                JSONObject jObj = new JSONObject(result);
                Boolean status = jObj.getBoolean("status");
                String Message = jObj.getString("message");
                if(status){

                    JSONArray jsonArray = (JSONArray) jObj.get("data");
                    //DatabaseController.getInstance().EmptyTable(getApplicationContext(),"semester");
                    for(int i =0; jsonArray.length()>i ; i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String semester_id = obj.getString("id");
                        String semester_name = obj.getString("semester_name");
                        final Semester semester = new Semester(i,semester_id,semester_name);
                        System.out.println("louai semester id " + i);
                        System.out.println("louai database semester_id " + semester.getSemester_id()+ " smester_name " + semester.getName() );
                        System.out.println("louai semesterArrayList inside async task ");
                        semesterArrayList.add(new Semester(i,semester_id,semester_name));
                    }
                }
                else{
                    Toast.makeText(SemesterActivity.this.getApplicationContext(), Message , Toast.LENGTH_LONG).show();
                }
                System.out.println("louai you can acceess " );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("louai all semesters ");
            progressDialog.dismiss();

            //semesterArrayList = new ArrayList<Semester>();
            //semesterArrayList = DatabaseController.getInstance().retriveAllSemesters(SemesterActivity.this.getApplicationContext());
            semesterAdapter = new SemesterAdapter(SemesterActivity.this,semesterArrayList);

            //System.out.println("louai semester Array list size " + semesterArrayList.size() );
            semesterListView = (GridView)findViewById(R.id.semesterList);
            semesterListView.setAdapter(semesterAdapter);
            semesterAdapter.notifyDataSetChanged();

            semesterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Semester selectedSemester = semesterArrayList.get(position);
                    System.out.println("::::::::::::::::::::::::::::");
                    System.out.println(selectedSemester.getId());
                    System.out.println(selectedSemester.getSemester_id());
                    System.out.println(selectedSemester.getName());
                    String selectedSemesterId = selectedSemester.getSemester_id();

                    Intent semesterIntent = new Intent(SemesterActivity.this, TeacherActivity.class);
                    semesterIntent.putExtra("selectedSemesterId", selectedSemesterId);
                    semesterIntent.putExtra("selectedSemesterName", selectedSemester.getName());
                    startActivity(semesterIntent);
                }
            });

            // Do things like hide the progress bar or change a TextView
        }
    }

}
