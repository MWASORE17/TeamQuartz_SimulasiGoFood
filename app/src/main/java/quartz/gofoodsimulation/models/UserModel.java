package quartz.gofoodsimulation.models;

/**
 * Created by meikelwis on 4/1/17.
 */

public class UserModel {
    private long id;
    private String email;
    private String nama;
    private String noTelp;
    private String pass;
    private long goPay;
    private long point;

    public UserModel() {

    }

    public UserModel(String email, String nama, String noTelp, String pass) {
        this.email = email;
        this.nama = nama;
        this.noTelp = noTelp;
        this.pass = pass;
        this.goPay = 0;
        this.point = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public long getGoPay() {
        return goPay;
    }

    public void setGoPay(long goPay) {
        this.goPay = goPay;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

}
