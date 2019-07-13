package types;

/**
 * Created by DigitalNet on 9/8/2016.
 */
public class Semester {

    public Semester(int id, String semester_id, String name) {
        this.id = id;
        this.name = name;
        this.semester_id = semester_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(String semester_id) {
        this.semester_id = semester_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String semester_id;
    private String name;
    private int id;
}
