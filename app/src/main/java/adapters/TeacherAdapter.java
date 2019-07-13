package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.digitalnet.anas.R;

import java.util.List;

import dataModel.AnasContract;
import types.Course;

/**
 * Created by DigitalNet on 9/25/2016.
 */
public class TeacherAdapter extends ArrayAdapter {

    private final Context context;

    public TeacherAdapter(Context context, List<Course> teacherList){
        super(context, R.layout.teacher_item, teacherList);
        System.out.println("louai inside SemesterAdapter constructor " + context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Course course = (Course) getItem(position);
        System.out.println("louai semester Adapter get view");
        if(convertView==null) {
            convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.teacher_item,null);
            System.out.println("louai convertView " + convertView);
        }

        TextView course_name = (TextView) convertView.findViewById(R.id.teacher_name);
        course_name.setText(course.getCourse_name());
        return convertView;
    }
}
