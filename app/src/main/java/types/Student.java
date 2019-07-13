package types;

public class Student {
    private String student_id;
    private String student_cid;
    private String student_name;
    private boolean checked = false;

    public Student(String student_id, String student_cid, String student_name) {
        this.student_id = student_id;
        this.student_cid = student_cid;
        this.student_name = student_name;
    }


    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_cid() {
        return student_cid;
    }

    public void setStudent_cid(String student_cid) {
        this.student_cid = student_cid;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
