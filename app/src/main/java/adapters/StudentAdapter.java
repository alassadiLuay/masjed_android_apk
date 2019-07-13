package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.digitalnet.anas.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import listview.checkbox.ListViewItemViewHolder;
import types.Student;

/**
 * Created by DigitalNet on 9/28/2016.
 */
public class StudentAdapter extends ArrayAdapter {

    private final Context context;
    LayoutInflater inflater;
    private List<Student> Studentlist = null;
    private ArrayList<Student> arraylist;

    public StudentAdapter(Context context, List<Student> studentList){
        super(context, R.layout.student_item, studentList);
        System.out.println("louai inside studentAdapter constructor " + context);
        this.context = context;
        this.Studentlist = studentList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Student>();
        this.arraylist.addAll(Studentlist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItemViewHolder viewHolder = null;

        final Student student = (Student) getItem(position);
        System.out.println("louai student Adapter get view");
        if(convertView==null) {
            convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.student_item,null);
            System.out.println("louai convertView " + convertView);
        }

        CheckBox listItemCheckbox = (CheckBox) convertView.findViewById(R.id.list_view_item_checkbox);
        TextView textView = (TextView) convertView.findViewById(R.id.student_name);
        textView.setText(student.getStudent_name());

        viewHolder = new ListViewItemViewHolder(convertView);

        viewHolder.setItemCheckbox(listItemCheckbox);

        viewHolder.setItemTextView(textView);

        viewHolder.getItemCheckbox().setChecked(student.isChecked());
        viewHolder.getItemTextView().setText(student.getStudent_name());

        convertView.setTag(viewHolder);

/*
        TextView pageNumberView = (TextView) convertView.findViewById(R.id.pages_students_num);
        pageNumberView.setText(student.getPage_number());*/
        System.out.println("louai student position "+ student.getStudent_name());

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Studentlist.clear();
        if (charText.length() == 0) {
            Studentlist.addAll(arraylist);
        }
        else
        {
            for (Student st : arraylist)
            {
                if (st.getStudent_name().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    Studentlist.add(st);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(Studentlist!=null)
        {
            ret = Studentlist.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int itemIndex) {
        Object ret = null;
        if(Studentlist!=null) {
            ret = Studentlist.get(itemIndex);
        }
        return ret;
    }

    public long getItemId(int itemIndex) {
        return itemIndex;
    }
}
