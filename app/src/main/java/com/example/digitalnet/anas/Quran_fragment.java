package com.example.digitalnet.anas;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.SemesterAdapter;
import connection.MyHttpURLConnection;
import types.Semester;

import static android.content.ContentValues.TAG;

/**
 * Created by luay on 20/05/17.
 */

public class Quran_fragment extends Fragment {

    View Qview;
    Button addButton;
    String final_msg;
    String final_err_msg;
    Boolean final_err_flag;
    String final_chapter_msg;
    String last_page_id;
    String chapter_id;
    String student_cid;
    String teacher_id;
    private Spinner spinner1, spinner2;
    public String addPageParameters;
    private String currentPoint = "";
    private String sendParameters = "";
    String hostName ="";
    String addPageURL = "api/set_student_listened_subjects";

    public Quran_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("quran execute");
        Qview = inflater.inflate(R.layout.quran_fragments, container, false);


        Resources res = getResources();
        hostName = res.getString(R.string.hostName);

        addButton = (Button) Qview.findViewById(R.id.addPage);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText notes = (EditText) Qview.findViewById(R.id.notes);
                String note = notes.getText().toString();

/*                TextView chapter_number = (TextView) Qview.findViewById(R.id.Juza_num);
                chapter_id = chapter_number.getText().toString();

                TextView page_number = (TextView) Qview.findViewById(R.id.page_num);
                last_page_id = page_number.getText().toString();*/
                //"{\"student_cid\":\""+ student_cid +"\",\"student_id\":\""+ student_id ;
                currentPoint = "";
                currentPoint += ",\"subject_type\":\""+selected_subject+"\",\"subject_id\":"+selected_subject_id;
                if(!addPageParameters.contains("note"))
                    currentPoint += ",\"note\":\""+note+"\"}";
                else
                    currentPoint += "}";
                sendParameters = addPageParameters + currentPoint;
                System.out.println("here : " + addPageParameters);

                new add_page_task().execute();
            }
        });
        addListenerOnSpinnerItemSelection();

        return Qview;
    }

    public void addItemsOnSpinner2(List<String> list) {
        spinner2 = (Spinner) Qview.findViewById(R.id.spinner2);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) Qview.findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    @Override
    public void onDetach() {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.onDetach();
    }

    private class add_page_task extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

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
            System.out.println("louai url parametere" + sendParameters );
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                return  httpURLConnectionExample.sendPost(hostName + addPageURL, sendParameters);
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
                currentPoint = "";
                JSONObject obj = new JSONObject(result);
                Boolean status = obj.getBoolean("status");
                if(!status) {
                    final_err_msg = obj.getString("message");
                    final_err_flag = true;
                    //final_chapter_msg = obj.getString("chapter_message");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();

            if(final_err_flag != null && final_err_flag == true){

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
                Toast.makeText(getContext(), "The page has been added successfully" , Toast.LENGTH_SHORT).show();
                addButton.setVisibility(View.INVISIBLE);
                if(final_chapter_msg != null && !final_chapter_msg.equals("")){
                    Toast.makeText(getContext(), final_chapter_msg , Toast.LENGTH_SHORT).show();
                }

                /*Intent intent = new Intent(getActivity(), StudentActivity.class);
                intent.putExtra("teacher_id", teacher_id);
                intent.putExtra("semester_id", semester_id);
                startActivity(intent);*/
                getActivity().finish();
            }

            // Do things like hide the progress bar or change a TextView
        }
    }

    private String selected_subject = "";
    private String selected_subject_id = "";
    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            if(parent.getItemAtPosition(pos).toString().equals("تسميع سورة"))
                selected_subject = "surah";
            else
                selected_subject = "page";
            Toast.makeText(parent.getContext(),
                    "الرجاء الانتظار لتحميل " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();

            TextView chapter_number = (TextView) Qview.findViewById(R.id.Juza_num);
            chapter_number.setText("-");

            new SubjectTask().execute();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }

    public class CustomSp2OnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            selected_subject_id = list_pair.get(parent.getItemAtPosition(pos).toString());
            TextView chapter_number = (TextView) Qview.findViewById(R.id.Juza_num);
            chapter_number.setText(list_page_juza.get(parent.getItemAtPosition(pos).toString()));
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }
    Map<String,String> list_pair = new HashMap<String,String>();
    Map<String,String> list_page_juza = new HashMap<String,String>();
    List<String> list = new ArrayList<String>();
    private class SubjectTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

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
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                String _URL = hostName+"api/get_subjects?subject_type="+selected_subject+"&student_cid="+student_cid;
                System.out.println("::::::::::::::::::");
                System.out.println(_URL);
                return  httpURLConnectionExample.sendGet(_URL);
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
            System.out.println(result);
            try {
                JSONObject jObj = new JSONObject(result);
                Boolean status = jObj.getBoolean("status");
                String Message = jObj.getString("message");
                if(status){
                    JSONArray jsonArray = (JSONArray) jObj.get("data");
                    //DatabaseController.getInstance().EmptyTable(getApplicationContext(),"semester");
                    list.clear();
                    for(int i =0; jsonArray.length()>i ; i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        list_pair.put(obj.getString("subject_name"),obj.getString("s_id"));
                        list.add(obj.getString("subject_name"));
                        if(selected_subject=="page")
                            list_page_juza.put(obj.getString("subject_name"),obj.getString("juza_id"));
                    }
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), Message , Toast.LENGTH_LONG).show();
                }
                System.out.println("louai you can acceess " );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("louai all semesters ");
            progressDialog.dismiss();

            addItemsOnSpinner2(list);
            spinner2.setOnItemSelectedListener(new CustomSp2OnItemSelectedListener());
            // Do things like hide the progress bar or change a TextView
        }
    }
}