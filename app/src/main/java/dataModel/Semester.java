package dataModel;

public class Semester {
    private String id;
    private String semester_id;
    private String semester_name;

    public Semester(String id, String semester_id, String semester_name){
        this.id = id;
        this.semester_id = semester_id;
        this.semester_name = semester_name;
    }

    public String getSemester_name() {
        return semester_name;
    }

    public void setSemester_name(String semester_name) {
        this.semester_name = semester_name;
    }

    public String getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(String semester_id) {
        this.semester_id = semester_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
