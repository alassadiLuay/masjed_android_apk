package types;

/**
 * Created by DigitalNet on 10/1/2016.
 */
public class GridType {

    private String name;
    private int img;
    private int id;

    public GridType(String name, int img, int id) {
        this.name = name;
        this.img = img;
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GridType(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
