package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.digitalnet.anas.R;

import java.util.List;

import dataModel.AnasContract;
import types.Semester;

/**
 * Created by DigitalNet on 9/8/2016.
 */
public class SemesterAdapter extends ArrayAdapter {

    private final List<Semester> semesterList;
    private final Context context;

    public SemesterAdapter(Context context, List<Semester> semesterList){
        super(context, R.layout.semesters_item, semesterList);
        System.out.println("louai inside SemesterAdapter constructor " + context);
        this.semesterList = semesterList;
        this.context = context;
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Semester semester = (Semester) getItem(position);
        System.out.println("louai semester Adapter get view");
        if(convertView==null) {
            //convertView = LayoutInflater.from(context).inflate(R.layout.semesters_item,null);
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.semesters_item, parent, false);
            convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.semesters_item,null);
            System.out.println("louai convertView " + convertView);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.semster_name);
        textView.setText(semester.getName());
        System.out.println("louai semesterList position "+ semester.getName());

        return convertView;
    }
}
