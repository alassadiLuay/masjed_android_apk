package dataModel;

import android.provider.BaseColumns;

/**
 * Created by DigitalNet on 9/20/2016.
 */
public class AnasContract {
    private AnasContract(){}

    public  static class Semester implements BaseColumns{
        public static final String TABLE_NAME = "semester";

        private String id;
        private String semester_id;
        private String semester_name;

        public static final String COLUMN_NAME_SEMESTER_ID = "semester_id";
        public static final String COLUMN_NAME_SEMESTER_NAME = "semester_neme";

        public String getSemester_id() {
            return semester_id;
        }

        public void setSemester_id(String semester_id) {
            this.semester_id = semester_id;
        }

        public String getSemester_name() {
            return semester_name;
        }

        public void setSemester_name(String semester_name) {
            this.semester_name = semester_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Semester(String semester_id, String semester_name) {
            this.semester_id = semester_id;
            this.semester_name = semester_name;
        }

        public Semester (String id, String semester_id, String semester_name){
            this.id = id;
            this.semester_id = semester_id;
            this.semester_name = semester_name;
        }

        @Override
        public String toString(){
            return "Semester{ "+
                    "id=' "+id + '\'' +
                    " ,semester_id=' "+semester_id + '\'' +
                    " ,semester_name=' " + semester_name + '\''+
                    '}';
        }
    }

    public static class Teacher implements BaseColumns{
        public static final String TABLE_NAME = "teacher";

        private String id;
        private String teacher_id;
        private String semester_id;
        private String teacher_name;
        private int student_number;

        public static final String COLUMN_NAME_TEACHER_ID = "teacher_id";
        public static final String COLUMN_NAME_SEMESTER_ID = "semester_id";
        public static final String COLUMN_NAME_TEACHER_NAME = "teacher_name";
       // public static final String COLUMN_NAME_STUDENT_NUMBER = "student_number";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getStudent_number() {
            return student_number;
        }

        public void setStudent_number(int Student_number) {
            this.student_number = Student_number;
        }

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public String getSemester_id() {
            return semester_id;
        }

        public void setSemester_id(String semester_id) {
            this.semester_id = semester_id;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public Teacher(String id, String teacher_id, String semester_id, String teacher_name) {
            this.id = id;
            this.teacher_id = teacher_id;
            this.semester_id = semester_id;
            this.teacher_name = teacher_name;
            //this.student_number = student_number;
        }

        public Teacher(String teacher_id, String semester_id, String teacher_name) {
            this.teacher_id = teacher_id;
            this.semester_id = semester_id;
            this.teacher_name = teacher_name;
            //this.student_number = student_number;
        }
    }

    public static class Student implements BaseColumns {
        public static final String TABLE_NAME = "student";

        private String id;
        private String student_id;
        private String teacher_id;
        private String semester_id;
        private String student_name;
        private boolean checked = false;

        public static final String COLUMN_NAME_STUDENT_ID = "student_id";
        public static final String COLUMN_NAME_TEACHER_ID = "teacher_id";
        public static final String COLUMN_NAME_SEMESTER_ID = "semester_id";
        public static final String COLUMN_NAME_STUDENT_NAME = "student_name";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public String getSemester_id() {
            return semester_id;
        }

        public void setSemester_id(String semester_id) {
            this.semester_id = semester_id;
        }

        public String getStudent_name() {
            return student_name;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public Student(String id, String student_id, String teacher_id, String semester_id, String student_name) {
            this.id = id;
            this.student_id = student_id;
            this.teacher_id = teacher_id;
            this.semester_id = semester_id;
            this.student_name = student_name;
        }

        public Student(String student_id, String teacher_id, String semester_id, String student_name) {
            this.student_id = student_id;
            this.teacher_id = teacher_id;
            this.semester_id = semester_id;
            this.student_name = student_name;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
