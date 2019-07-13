package com.example.digitalnet.anas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connection.MyHttpURLConnection;
import types.Student;

/**
 * Created by luay on 20/05/17.
 */


public class student_main extends AppCompatActivity {

    private Quran_fragment QF;
    private points_fragments PF;
    private TabLayout allTabs;
    private String student_name;
    private int points;
    private int current_position = 0;
    private float lastX = 0;

    String checkChapterURL = "api/get_last_subjects?student_cid=";
    String checkChapterParameters;

    String addPageURL = "api/set_student_listened_subjects?";
    String addPageParameters;
    String addPageParametersPoint;

    String pointsURL = "masjed_Bader/index.php/teacher_api/studentPoints/format/json";
    String pointsParameters;

    String student_cid;
    String teacher_id;
    String listener_id;
    String student_id;

    String chapter_id;
    String last_page_id;
    String message;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        intialize_chapter();
        intialize_values();
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();



/*        Button add = (Button) QF.getView().findViewById(R.id.addPage);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText notes = (EditText) QF.getView().findViewById(R.id.notes);

                String note = notes.getText().toString();

                //loginParameters = "{\"userName\":\""+user_name+"\",\"password\":\""+pass_word+"\"}";
                addPageParameters = "{\"semester_id\":\""+ semester_id +"\",\"student_id\":\""+ student_id +"\",\"teacher_id\" :\""+ teacher_id +"\",\"listener_id\":\""+listener_id+"\",\"chapter_id\":\""+chapter_id+"\",\"page_number\":\""+last_page_id+"\",\"note\":\""+note+"\" }";

                new add_page_task().execute();

            }
        });*/
    }

    private void intialize_values(){
        student_name = getIntent().getStringExtra("student_name");
        student_cid = getIntent().getStringExtra("student_cid");
        teacher_id = getIntent().getStringExtra("teacher_id");
        listener_id = getIntent().getStringExtra("listener_id");
        student_id = getIntent().getStringExtra("student_id");

        checkChapterParameters = "{\"student_cid\":"+ student_cid +"}";

        new check_chapter_task().execute();
        //new point_details_task().execute();

        TextView userName = (TextView) findViewById(R.id.student_name);
        userName.setText(student_name);

        addPageParameters = "{\"student_cid\":\""+ student_cid +"\",\"student_id\":"+ student_id ;
        addPageParametersPoint = "{\"student_id\":"+ student_cid ;
        //addPageParameters = "student_cid="+ student_cid +"&student_id="+ student_id;
    }

    private void intialize_chapter(){
        Resources res = getResources();
        String hostName = res.getString(R.string.hostName);
        checkChapterURL = hostName + checkChapterURL;
        addPageURL = hostName + addPageURL;
        pointsURL = hostName + pointsURL;
    }

    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }

    private void setupTabLayout() {
        /*
        * Quran Fragment
        * */
        QF = new Quran_fragment();
        QF.student_cid = student_cid;
        QF.teacher_id = teacher_id;

        /*
        * Points Fragment
        * */
        PF = new points_fragments();

        allTabs.addTab(allTabs.newTab().setText("القرآن الكريم"),true);
        allTabs.addTab(allTabs.newTab().setText("النقاط"));
    }

    private void bindWidgetsWithAnEvent()
    {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                current_position = 0;
                replaceFragment(QF);
                break;
            case 1 :
                current_position = 1;
                replaceFragment(PF);
                break;

        }
    }

    public void replaceFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        TabLayout.Tab currentTab = allTabs.getTabAt(current_position);
        if (currentTab != null) {
            System.out.println("current " + current_position );
            View customView = currentTab.getCustomView();
            if (customView != null) {
                System.out.println("custom " + current_position );
                customView.setSelected(true);
            }
            //currentTab.select();
             if(current_position == 0) {
                 new check_chapter_task().execute();
                 currentTab.select();
             }
             else {
                 new point_details_task().execute();
                 currentTab.select();
             }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen to swap
            case MotionEvent.ACTION_DOWN: {
                lastX = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float currentX = touchevent.getX();

                // if left to right swipe on screen
                if (lastX < currentX) {
                    switchTabs(false);
                }

                // if right to left swipe on screen
                if (lastX > currentX) {
                    switchTabs(true);
                }

                break;
            }
        }
        return false;
    }

    public void switchTabs(boolean direction) {
        if (direction) // true = move left
        {
            if (current_position == 0)
                setCurrentTabFragment(1);
            else
                setCurrentTabFragment(current_position - 1);
        } else
        // move right
        {
            if (current_position == 1)
                setCurrentTabFragment(0);
            else
                setCurrentTabFragment(1);
        }

    }

    private class check_chapter_task extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog progressDialog = new ProgressDialog(student_main.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                return  httpURLConnectionExample.sendGet(checkChapterURL+student_cid);
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

                if(result.length() < 1) {
                    Button addButton = (Button) QF.getView().findViewById(R.id.addPage);
                    addButton.setVisibility(View.INVISIBLE);

                    EditText notes = (EditText) QF.getView().findViewById(R.id.notes);
                    notes.setVisibility(View.INVISIBLE);
                }
                JSONObject jObj = new JSONObject(result);
                Boolean status = jObj.getBoolean("status");
                String Message = jObj.getString("message");
                if(status){
                    if(jObj.get("data") != null){
                        try{
                            JSONObject obj = (JSONObject) jObj.get("data");
                            if(obj.has("juza_id")) {
                                chapter_id = obj.getString("juza_id");
                                last_page_id += "الصفحة رقم ";
                            }
                            last_page_id = obj.getString("last_subject");
                            message = obj.getString("message");
                            flag = obj.getString("flag");
                        }
                        catch (Exception e){
                            System.out.println("NULL Pointer Exception for empty data object");
                            System.out.println(e);
                        }
                    }
                }
                else{
                    flag = "";
                    message = Message;
                    Toast.makeText(student_main.this, Message , Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("louai all semesters ");
            progressDialog.dismiss();

            if(flag != null && flag.equals("false")){

/*                AlertDialog alertDialog = new AlertDialog.Builder(student_main.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent studentIntent = new Intent(student_main.this, StudentActivity.class);
                                studentIntent.putExtra("semester_id",semester_id);
                                studentIntent.putExtra("teacher_id",teacher_id);
                                studentIntent.putExtra("hide",true);
                                startActivity(studentIntent);
                                finish();
                            }
                        });
                alertDialog.show();*/
                if(QF.getView() != null) {
                    Button addButton = (Button) QF.getView().findViewById(R.id.addPage);
                    addButton.setVisibility(View.INVISIBLE);

                    EditText notes = (EditText) QF.getView().findViewById(R.id.notes);
                    notes.setVisibility(View.INVISIBLE);
                }
            }else{

                TextView chapter_number = (TextView) QF.getView().findViewById(R.id.Juza_num);
                chapter_number.setText(chapter_id);

                TextView page_number = (TextView) QF.getView().findViewById(R.id.page_num);
                page_number.setText(last_page_id);

                Button addButton = (Button) QF.getView().findViewById(R.id.addPage);
                addButton.setVisibility(View.VISIBLE);

                EditText notes = (EditText) QF.getView().findViewById(R.id.notes);
                notes.setVisibility(View.VISIBLE);

                QF.addPageParameters = addPageParameters;
                QF.student_cid = student_cid;
                QF.teacher_id = teacher_id;
            }
            // Do things like hide the progress bar or change a TextView
        }
    }

    private class point_details_task extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog progressDialog = new ProgressDialog(student_main.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                pointsParameters = "{\"student_cid\":\""+ student_cid +"\",\n" +"\"student_id\":\""+ student_id +"\"}";
                System.out.println("louai para koko " + pointsParameters);
                return  httpURLConnectionExample.sendPost(pointsURL, pointsParameters);
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
            System.out.println("louai on post execute koko");
            System.out.println("louai result koko" + result);
            try {
                //JSONArray jsonArray = new JSONArray(result);
                JSONObject obj = new JSONObject(result);
                points = obj.getInt("point_count");
                flag = obj.getString("error_flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();

            if(flag != null && flag.equals("false")){

/*                AlertDialog alertDialog = new AlertDialog.Builder(student_main.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent studentIntent = new Intent(student_main.this, StudentActivity.class);
                                studentIntent.putExtra("semester_id",semester_id);
                                studentIntent.putExtra("teacher_id",teacher_id);
                                startActivity(studentIntent);
                                finish();
                            }
                        });
                alertDialog.show();*/

            }else{

                /*TextView points_label = (TextView) PF.getView().findViewById(R.id.Points_static_label5);
                String text = " نقطة ";
                points_label.setText(points + text );
                System.out.println(points);*/
                PF.points = points;
                PF.addPointParameters = addPageParametersPoint;
            }
            // Do things like hide the progress bar or change a TextView
        }
    }

}