package dataModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DigitalNet on 9/22/2016.
 */
public class DatabaseFactory  {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public DatabaseFactory(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public void openWritable(){
        db = databaseHelper.getWritableDatabase();
    }

    public void openReadable(){
        db = databaseHelper.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public long addSemester (AnasContract.Semester semester){

        ContentValues contentValues= new ContentValues();
        contentValues.put(AnasContract.Semester.COLUMN_NAME_SEMESTER_ID, semester.getSemester_id());
        contentValues.put(AnasContract.Semester.COLUMN_NAME_SEMESTER_NAME, semester.getSemester_name());

        long id = db.insertWithOnConflict(AnasContract.Semester.TABLE_NAME , null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        semester.setId(String.valueOf(id));
        return id;
    }

    public List<AnasContract.Semester> getAllSemesters(){
        List<AnasContract.Semester> semesters = new ArrayList<AnasContract.Semester>();
        String[] projection = {
                AnasContract.Semester._ID,
                AnasContract.Semester.COLUMN_NAME_SEMESTER_ID,
                AnasContract.Semester.COLUMN_NAME_SEMESTER_NAME
        };

        Cursor cursor = db.query(AnasContract.Semester.TABLE_NAME, projection, null, null, null, null, AnasContract.Semester._ID);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AnasContract.Semester semester = new AnasContract.Semester(cursor.getString(cursor.getColumnIndex(AnasContract.Semester._ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Semester.COLUMN_NAME_SEMESTER_ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Semester.COLUMN_NAME_SEMESTER_NAME)));
            semesters.add(semester);
            cursor.moveToNext();
        }

        cursor.close();
        return semesters;
    }

    public long addTeacher (AnasContract.Teacher teacher){

        ContentValues contentValues= new ContentValues();
        contentValues.put(AnasContract.Teacher.COLUMN_NAME_TEACHER_ID, teacher.getTeacher_id());
        contentValues.put(AnasContract.Teacher.COLUMN_NAME_SEMESTER_ID, teacher.getSemester_id());
        contentValues.put(AnasContract.Teacher.COLUMN_NAME_TEACHER_NAME, teacher.getTeacher_name());
        //contentValues.put(AnasContract.Teacher.COLUMN_NAME_STUDENT_NUMBER, teacher.getStudent_number());

        long id = db.insertWithOnConflict(AnasContract.Teacher.TABLE_NAME , null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        teacher.setId(String.valueOf(id));
        return id;
    }


    public List<AnasContract.Teacher> getTeacher(String semester_id){
        List<AnasContract.Teacher> teachers = new ArrayList<AnasContract.Teacher>();
        String[] projection = {
                AnasContract.Teacher._ID,
                AnasContract.Teacher.COLUMN_NAME_TEACHER_ID,
                AnasContract.Teacher.COLUMN_NAME_SEMESTER_ID,
                AnasContract.Teacher.COLUMN_NAME_TEACHER_NAME,
                //AnasContract.Teacher.COLUMN_NAME_STUDENT_NUMBER
        };
        //where close where semester id = semester id
        //String whereArgs = semester_id;
        String[] whereArgs = new String[]{semester_id};
        Cursor cursor = db.query(AnasContract.Teacher.TABLE_NAME, projection, AnasContract.Teacher.COLUMN_NAME_SEMESTER_ID  + "= '" + semester_id + "'" , null, null, null, AnasContract.Teacher._ID);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AnasContract.Teacher teacher = new AnasContract.Teacher(cursor.getString(cursor.getColumnIndex(AnasContract.Teacher._ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Teacher.COLUMN_NAME_TEACHER_ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Teacher.COLUMN_NAME_SEMESTER_ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Teacher.COLUMN_NAME_TEACHER_NAME)));
                    /*cursor.getInt(cursor.getColumnIndex(AnasContract.Teacher.COLUMN_NAME_STUDENT_NUMBER))*/
            teachers.add(teacher);
            cursor.moveToNext();
        }

        cursor.close();
        return teachers;
    }

    public long addStudent (AnasContract.Student student){

        ContentValues contentValues= new ContentValues();
        contentValues.put(AnasContract.Student.COLUMN_NAME_STUDENT_ID, student.getStudent_id());
        contentValues.put(AnasContract.Student.COLUMN_NAME_TEACHER_ID, student.getTeacher_id());
        contentValues.put(AnasContract.Student.COLUMN_NAME_SEMESTER_ID, student.getSemester_id());
        contentValues.put(AnasContract.Student.COLUMN_NAME_STUDENT_NAME, student.getStudent_name());

        long id = db.insertWithOnConflict(AnasContract.Student.TABLE_NAME , null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        student.setId(String.valueOf(id));
        return id;
    }

    public List<AnasContract.Student> getStudents(String teacher_id , String semester_id){
        List<AnasContract.Student> students = new ArrayList<AnasContract.Student>();
        String[] projection = {
                AnasContract.Student._ID,
                AnasContract.Student.COLUMN_NAME_STUDENT_ID,
                AnasContract.Student.COLUMN_NAME_TEACHER_ID,
                AnasContract.Student.COLUMN_NAME_SEMESTER_ID,
                AnasContract.Student.COLUMN_NAME_STUDENT_NAME
        };
        //where close where semester id = semester id
        //String whereArgs = semester_id;
        String[] whereClause = new String[]{AnasContract.Student.COLUMN_NAME_TEACHER_ID ,AnasContract.Student.COLUMN_NAME_SEMESTER_ID};
        String[] whereArgs = new String[]{teacher_id , semester_id};
        Cursor cursor = db.query(AnasContract.Student.TABLE_NAME, projection, "teacher_id=? and semester_id=?" , whereArgs, null, null, AnasContract.Student._ID);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AnasContract.Student student = new AnasContract.Student(cursor.getString(cursor.getColumnIndex(AnasContract.Student._ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Student.COLUMN_NAME_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Student.COLUMN_NAME_TEACHER_ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Student.COLUMN_NAME_SEMESTER_ID)),
                    cursor.getString(cursor.getColumnIndex(AnasContract.Student.COLUMN_NAME_STUDENT_NAME)));
            students.add(student);
            cursor.moveToNext();
        }

        cursor.close();
        return students;
    }

    public void EmptyTable(String tableName){
        databaseHelper.Empty(db,tableName);
    }
}
