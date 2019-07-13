package com.example.digitalnet.anas;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import connection.MyHttpURLConnection;

/**
 * Created by luay on 20/05/17.
 */

public class points_fragments extends Fragment {

    View Pview;
    String addPointURL = "api/set_student_points";
    public String addPointParameters;
    public String currentPoint;
    private String sendParameters = "";
    String rmPointURL = "masjed_Bader/index.php/teacher_api/deletePoints/format/json";
    public String rmPointParameters;

    String hostName ="";
    int points = 0;
    int tempPoints = 0;
    String final_msg;
    String final_err_msg;
    Boolean final_err_flag;
    String final_chapter_msg;
    private Spinner spinner3;
    public points_fragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Pview = inflater.inflate(R.layout.points_fragments, container, false);
        Resources res = getResources();
        hostName = res.getString(R.string.hostName);
        Button addButton = (Button) Pview.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText notes = (EditText) Pview.findViewById(R.id.points_notes);
                String note = notes.getText().toString();

                /*EditText points = (EditText) Pview.findViewById(R.id.points);
                String point = points.getText().toString();*/

                /*if(points_fragments.isNumeric(point))
                    tempPoints = Integer.parseInt(point);
                else
                    tempPoints = 0;*/
                currentPoint = "";
                if(addPointParameters != null && selected_point_id.length() > 0)
                    currentPoint = ",\"note\":\""+note+"\",\"point_id\":\""+selected_point_id+"\" }";
                sendParameters = addPointParameters + currentPoint;
                System.out.println("here : " + addPointParameters);

                if(note.length() > 2 && selected_point_id.length() > 0){
                    new points_fragments.add_points().execute();
                    //points.setText("");
                    notes.setText("");
                }
                else
                    Toast.makeText(getContext(), "رجاءً، أدخل نوع / سبب النقاط" , Toast.LENGTH_SHORT).show();
            }
        });

/*        Button rmButton = (Button) Pview.findViewById(R.id.remove);
        rmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText notes = (EditText) Pview.findViewById(R.id.points_notes);
                String note = notes.getText().toString();

                EditText points = (EditText) Pview.findViewById(R.id.points);
                String point = points.getText().toString();

                if(points_fragments.isNumeric(point))
                    tempPoints = Integer.parseInt(point);
                else
                    tempPoints = 0;

                if(!addPointParameters.contains("point_count"))
                    rmPointParameters = addPointParameters + "\",\"note\":\""+note+"\",\"point_count\":\""+tempPoints+"\" }";
                System.out.println("here : " + rmPointParameters);

                new points_fragments.remove_points().execute();
            }
        });*/
        new GetPointsTask().execute();
        return Pview;
    }

    public void addItemsOnSpinner2(List<String> list) {
        spinner3 = (Spinner) Pview.findViewById(R.id.spinner3);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(dataAdapter);
    }

    private String selected_point_id = "";
    Map<String,String> list_pair = new HashMap<String,String>();
    List<String> list = new ArrayList<String>();
    public class CustomSp2OnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            selected_point_id = list_pair.get(parent.getItemAtPosition(pos).toString());
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
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

    private class add_points extends AsyncTask<String, Integer, String> {

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
            System.out.println("louai url " + addPointURL);
            System.out.println("louai url parametere" + sendParameters );
            MyHttpURLConnection httpURLConnectionExample = new MyHttpURLConnection();
            try {
                return  httpURLConnectionExample.sendPost(hostName + addPointURL, sendParameters);
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
                currentPoint = "";
                //JSONArray jsonArray = new JSONArray(result);
                JSONObject obj = new JSONObject(result);
                Boolean status = obj.getBoolean("status");
                if(!status) {
                    final_err_msg = obj.getString("message");
                    final_err_flag = true;
                    //final_chapter_msg = obj.getString("chapter_message");
                }
                System.out.println("louai ttttt " + result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();

            if(final_err_flag != null && final_err_flag){

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
                Toast.makeText(getContext(), "تم إضافة النقاط بنجاح" , Toast.LENGTH_SHORT).show();

                if(final_chapter_msg != null && !final_chapter_msg.equals("")){
                    Toast.makeText(getContext(), final_chapter_msg , Toast.LENGTH_SHORT).show();
                }

                /*TextView points_label = (TextView) Pview.findViewById(R.id.Points_static_label5);
                String text = " نقطة ";
                points = points + tempPoints;
                points_label.setText(points + text);*/
            }

            // Do things like hide the progress bar or change a TextView
        }
    }

    private class GetPointsTask extends AsyncTask<String, Integer, String> {

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
                String _URL = hostName+"api/get_points";
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
                        list_pair.put(obj.getString("point_name")+" "+obj.getString("point_value"),obj.getString("id"));
                        list.add(obj.getString("point_name")+" "+obj.getString("point_value"));
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
            spinner3.setOnItemSelectedListener(new CustomSp2OnItemSelectedListener());
            // Do things like hide the progress bar or change a TextView
        }
    }

    /*private class remove_points extends AsyncTask<String, Integer, String> {

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
                return  httpURLConnectionExample.sendPost(hostName+rmPointURL, rmPointParameters);
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
                Toast.makeText(getContext(), "تم حذف النقاط!" , Toast.LENGTH_SHORT).show();
                if(final_chapter_msg != null && !final_chapter_msg.equals("")){
                    Toast.makeText(getContext(), final_chapter_msg , Toast.LENGTH_SHORT).show();
                }

                TextView points_label = (TextView) Pview.findViewById(R.id.Points_static_label5);
                String text = " نقطة ";
                points = points - tempPoints;
                points_label.setText(points + text);
            }

            // Do things like hide the progress bar or change a TextView
        }
    }*/

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
