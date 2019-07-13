package types;

/**
 * Created by DigitalNet on 9/7/2016.
 */
public class Listener {

    private String username;
    private String password;
    private String listenerId;

    public Listener(String username, String password, String listenerId) {
        this.username = username;
        this.password = password;
        this.listenerId = listenerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getListenerId() {
        return listenerId;
    }

    public void setListenerId(String listenerId) {
        this.listenerId = listenerId;
    }
}
