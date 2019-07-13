package dataModel;

import android.content.Context;

import java.util.List;

/**
 * Created by DigitalNet on 9/22/2016.
 */
public class DatabaseController {

    private static DatabaseController instance;

    private DatabaseController(){}

    public static DatabaseController getInstance(){
        if (instance == null)
            instance = new DatabaseController();

        return instance;
    }

    public long registerSemester(Context context, AnasContract.Semester semester){
        DatabaseFactory dbFactory = new DatabaseFactory(context);
        dbFactory.openWritable();
        long id = dbFactory.addSemester(semester);
        dbFactory.close();

        return id;
    }

    public List<AnasContract.Semester> retriveAllSemesters (Context context){
        DatabaseFactory dbFactory = new DatabaseFactory(context);
        dbFactory.openReadable();
        List<AnasContract.Semester> semesters = dbFactory.getAllSemesters();

        return semesters;
    }

    public long registerTeacher(Context context, AnasContract.Teacher teacher){
        DatabaseFactory dbFactory = new DatabaseFactory(context);
        dbFactory.openWritable();
        long id = dbFactory.addTeacher(teacher);
        dbFactory.close();

        return id;
    }

    public List<AnasContract.Teacher> retriveTeachers (Context context, String semester_id){
        DatabaseFactory dbFactory = new DatabaseFactory(context);
        dbFactory.openReadable();
        List<AnasContract.Teacher> teachers = dbFactory.getTeacher(semester_id);

        return teachers ;
    }

    public long registerStudent(Context context, AnasContract.Student student){
        DatabaseFactory dbFactory = new DatabaseFactory(context);
        dbFactory.openWritable();
        long id = dbFactory.addStudent(student);
        dbFactory.close();

        return id;
    }

    public List<AnasContract.Student> retriveStudents (Context context, String teacher_id ,String semester_id){
        DatabaseFactory dbFactory = new DatabaseFactory(context);
        dbFactory.openReadable();
        List<AnasContract.Student> students = dbFactory.getStudents(teacher_id,semester_id);

        return students ;
    }

    public void EmptyTable(Context context, String table){
        DatabaseFactory dbFactory = new DatabaseFactory(context);
        dbFactory.openWritable();
        dbFactory.EmptyTable(table);
    }

}
