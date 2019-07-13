package com.example.digitalnet.anas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.SemesterAdapter;
import adapters.TeacherAdapter;
import connection.MyHttpURLConnection;
import dataModel.AnasContract;
import dataModel.DatabaseController;
import types.Course;
import types.Semester;

public class TeacherActivity extends AppCompatActivity {

    List<Course> courseArrayList ;
    TeacherAdapter teacherAdapter;
    String coursesUrl = "api/get_semester_course";
    String courseParameters = "{}";
    String hostName = "";
    String semester_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Resources res = getResources();
        hostName = res.getString(R.string.hostName);
        coursesUrl = hostName + coursesUrl;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        semester_id = getIntent().getStringExtra("selectedSemesterId");
        String semester_name = getIntent().getStringExtra("selectedSemesterName");
        getSupportActionBar().setTitle(semester_name); //user_name
        System.out.println("semester_id" + semester_id);
        courseArrayList = new ArrayList<Course>();
        new CourseTask().execute();
    }

    private class CourseTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        //ProgressDialog progressDialog = new ProgressDialog(SemesterActivity.this);
        ProgressDialog progressDialog = new ProgressDialog(TeacherActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog.show();
            System.out.println("louai on pre execute");
            progressDialog.show();
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            System.out.println("louai do in pack ground");
            System.out.println("louai url " + coursesUrl);
            System.out.println("louai url parametere" + courseParameters);
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                courseParameters = "?semester_id="+semester_id;
                return  httpURLConnectionExample.sendGet(coursesUrl+courseParameters);
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
            System.out.println("louai on post execute teacher");
            System.out.println("louai result teacher" + result);
            try {
                JSONObject jObj = new JSONObject(result);
                Boolean status = jObj.getBoolean("status");
                String Message = jObj.getString("message");
                if(status){
                    JSONArray jsonArray = (JSONArray) jObj.get("data");
                    System.out.println(jsonArray);
                    //DatabaseController.getInstance().EmptyTable(getApplicationContext(),"semester");
                    for(int i =0; jsonArray.length()>i ; i++){
                        JSONObject obj = jsonArray.getJSONObject(i);

                        Integer course_id = obj.getInt("id");
                        String course_name = obj.getString("course_name");

                        System.out.println("louai teacher id " + course_id);
                        System.out.println("louai database teacher_id " + course_id);
                        courseArrayList.add(new Course(course_id,course_name));
                    }
                }
                else{
                    Toast.makeText(TeacherActivity.this.getApplicationContext(), Message , Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("louai all teachers ");
            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
            teacherAdapter = new TeacherAdapter(TeacherActivity.this,courseArrayList);
            ListView teacherListView = (ListView)findViewById(R.id.teacherList);
            teacherListView.setAdapter(teacherAdapter);
            teacherAdapter.notifyDataSetChanged();

            teacherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Course selectedCourse = courseArrayList.get(position);
                    Integer selectedCourseId = selectedCourse.getCourse_id();

                    Intent teacherIntent = new Intent(TeacherActivity.this, StudentActivity.class);
                    teacherIntent.putExtra("course_id", selectedCourseId.toString());
                    startActivity(teacherIntent);
                }
            });
        }
    }
}
