package com.example.digitalnet.anas;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapters.StudentAdapter;
import connection.MyHttpURLConnection;
import types.Student;

public class StudentActivity extends AppCompatActivity {

    List<Student> studentsArrayList;
    List<Student> _studentsArrayList;
    List<String> last_sent;
    StudentAdapter studentAdapter;
    ListView studentListView;
    String attendURL = "api/set_students_points";
    String listener_id;
    String course_id = "";
    String hostName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Resources res = getResources();
        hostName = res.getString(R.string.hostName);
        attendURL = hostName + attendURL;

        SearchView searchView = (SearchView) findViewById(R.id.action_search);
        final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHint("بحث ...");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        course_id = getIntent().getStringExtra("course_id");
        studentsArrayList = new ArrayList<Student>();
        _studentsArrayList = new ArrayList<Student>();
        last_sent = new ArrayList<String>();

        new StudentTask().execute();
        /**
         * The Buttons
         * */

        final Button selectRemoveButton = (Button)findViewById(R.id.list_remove_selected_rows);
        final Button selectAllButton = (Button)findViewById(R.id.list_select_all);
        final Button selectNoneButton = (Button)findViewById(R.id.list_select_none);
        final Button selectReverseButton = (Button)findViewById(R.id.list_select_reverse);

        Switch _Switch = (Switch)findViewById(R.id.switch1);
        _Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectRemoveButton.setVisibility(View.VISIBLE);
                    selectRemoveButton.bringToFront();
                    selectAllButton.setVisibility(View.VISIBLE);
                    selectNoneButton.setVisibility(View.VISIBLE);
                    selectReverseButton.setVisibility(View.VISIBLE);
                    studentsArrayList.clear();
                    studentsArrayList.addAll(_studentsArrayList);
                    studentAdapter.notifyDataSetChanged();

                    studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                            // Get user selected item.
                            Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                            // Translate the selected item to DTO object.
                            Student itemDto = (Student)itemObject;

                            // Get the checkbox.
                            CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                            // Reverse the checkbox and clicked item check state.
                            if(itemDto.isChecked())
                            {
                                itemCheckbox.setChecked(false);
                                itemDto.setChecked(false);
                                studentsArrayList.get(itemIndex).setChecked(false);
                                _studentsArrayList.get(itemIndex).setChecked(false);
                            }else
                            {
                                itemCheckbox.setChecked(true);
                                itemDto.setChecked(true);
                                studentsArrayList.get(itemIndex).setChecked(true);
                                _studentsArrayList.get(itemIndex).setChecked(true);
                            }

                            //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    selectRemoveButton.setVisibility(View.INVISIBLE);
                    selectRemoveButton.bringToFront();
                    selectAllButton.setVisibility(View.INVISIBLE);
                    selectNoneButton.setVisibility(View.INVISIBLE);
                    selectReverseButton.setVisibility(View.INVISIBLE);
                    studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Student selectedStudent = studentsArrayList.get(position);
                            String student_id = selectedStudent.getStudent_id();

                            Intent studentIntent = new Intent(StudentActivity.this, student_main.class);
                            studentIntent.putExtra("student_cid", selectedStudent.getStudent_cid());
                            studentIntent.putExtra("listener_id", listener_id);
                            studentIntent.putExtra("student_id", student_id);
                            studentIntent.putExtra("student_name", selectedStudent.getStudent_name());
                            startActivity(studentIntent);
                        }
                    });
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchEditText.getText().toString().toLowerCase(Locale.getDefault());
                studentAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


        /**
         *
         * Check box functionality
         *
         * */


        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = studentsArrayList.size();
                for(int i=0;i<size;i++)
                {
                    Student dto = studentsArrayList.get(i);
                    dto.setChecked(true);
                    _studentsArrayList.get(i).setChecked(true);
                }

                studentAdapter.notifyDataSetChanged();
            }
        });

        // Click this button to disselect all listview items with checkbox unchecked.
        selectNoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = studentsArrayList.size();
                for(int i=0;i<size;i++)
                {
                    Student dto = studentsArrayList.get(i);
                    dto.setChecked(false);
                    _studentsArrayList.get(i).setChecked(false);
                }

                studentAdapter.notifyDataSetChanged();
            }
        });

        // Click this button to reverse select listview items.
        selectReverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = studentsArrayList.size();
                for(int i=0;i<size;i++)
                {
                    Student dto = studentsArrayList.get(i);

                    if(dto.isChecked())
                    {
                        dto.setChecked(false);
                        _studentsArrayList.get(i).setChecked(false);
                    }else {
                        dto.setChecked(true);
                        _studentsArrayList.get(i).setChecked(true);
                    }
                }

                studentAdapter.notifyDataSetChanged();
            }
        });

        // Click this button to remove selected items from listview.

        selectRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(StudentActivity.this).create();
                alertDialog.setMessage("هل أنت متأكد من أنك أخذت الغياب بشكل صحيح؟");

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int size = studentsArrayList.size();
                        for(int i=0;i<size;i++)
                        {
                            Student dto = studentsArrayList.get(i);

                            if(!dto.isChecked())
                            {
                                studentsArrayList.remove(i);
                                i--;
                                size = studentsArrayList.size();
                            }
                            else if(last_sent.size()==0){
                                last_sent.add(dto.getStudent_cid());
                            }
                        }
                        new absence_list_sync().execute();
                        studentAdapter.notifyDataSetChanged();
                    }
                });

                alertDialog.show();
            }
        });
    }

    private class absence_list_sync extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog pd = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(StudentActivity.this,"","الرجاء الانتظار ليتم إرسال الحضور إلى الخادم",true);
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                String stdAttendParameters = "{\"std\":[";
                for (int i=0; i< last_sent.size();i++){
                    boolean found = false;
                    for (int j=0; j< studentsArrayList.size();j++){
                        if(last_sent.get(i).equals(studentsArrayList.get(j).getStudent_cid())) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        stdAttendParameters += "{\"id\":\"" + last_sent.get(i) + "\",\"point_id\":\"-1\"},";
                        System.out.println("UN-Register Attendence for user " + stdAttendParameters);
                    }
                }
                last_sent.clear();
                for (int i=0; i< studentsArrayList.size();i++){
                    last_sent.add(studentsArrayList.get(i).getStudent_cid());
                    stdAttendParameters += "{\"id\":\"" + studentsArrayList.get(i).getStudent_cid() + "\",\"point_id\":\"6\"},";
                    System.out.println("Register Attendence for user " + stdAttendParameters);
                }
                if(studentsArrayList.size()>0)
                    stdAttendParameters = stdAttendParameters.substring(0, stdAttendParameters.length() - 1);
                stdAttendParameters +="]}";

                System.out.println(stdAttendParameters);
                return httpURLConnectionExample.sendPost(attendURL, stdAttendParameters);
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
            System.out.println("Register Attendence for user server response :::> " + result);

            try {
                JSONObject obj = new JSONObject(result);
                String total = obj.getString("total");
                String removed = obj.getString("removed");
                String registerd = obj.getString("registered");

                Toast.makeText(getApplicationContext(),"تم تسجيل الغياب ل"+registerd+" من أصل "+total+" وإلغاء الغياب ل "+removed,Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"هنالك خطأ ما في الخادم الرجاء التواصل مع قسم الدعم الفني",Toast.LENGTH_LONG).show();
            }

            pd.hide();
            pd.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }

    private class StudentTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        //ProgressDialog progressDialog = new ProgressDialog(SemesterActivity.this);
        ProgressDialog progressDialog = new ProgressDialog(StudentActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog.show();
            progressDialog.show();
            System.out.println("louai on pre execute");

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                String url = hostName + "api/get_course_students?course_id="+course_id;
                return  httpURLConnectionExample.sendGet(url);
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

                        String cid = obj.getString("id");
                        String id = obj.getString("student_id");
                        String name = obj.getString("student_fname")+" "+obj.getString("student_lname");

                        System.out.println("louai Std id " + course_id);
                        _studentsArrayList.add(new Student(id,cid,name));
                        studentsArrayList.add(new Student(id,cid,name));
                    }
                }
                else{
                    Toast.makeText(StudentActivity.this.getApplicationContext(), Message , Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("louai all Students ");
            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView

            studentAdapter = new StudentAdapter(StudentActivity.this,studentsArrayList);

            //System.out.println("louai semester Array list size " + semesterArrayList.size() );
            studentListView = (ListView)findViewById(R.id.studentList);
            studentListView.setAdapter(studentAdapter);
            studentAdapter.notifyDataSetChanged();

            String MY_PREFS_NAME = "log_in";
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String restoredText = prefs.getString("userName", null);
            //final String listener_id = prefs.getString("listener_id", null);
            if(restoredText != null){
                listener_id = prefs.getString("listener_id", null);
            }

            studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Student selectedStudent = studentsArrayList.get(position);

                    Intent studentIntent = new Intent(StudentActivity.this, student_main.class);
                    studentIntent.putExtra("student_cid", selectedStudent.getStudent_cid());
                    studentIntent.putExtra("listener_id", listener_id);
                    studentIntent.putExtra("student_id", selectedStudent.getStudent_id());
                    studentIntent.putExtra("student_name", selectedStudent.getStudent_name());
                    startActivity(studentIntent);
                }
            });

        }
    }
}
