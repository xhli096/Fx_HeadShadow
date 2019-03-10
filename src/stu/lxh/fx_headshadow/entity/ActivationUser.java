package stu.lxh.fx_headshadow.entity;

/**
 * Created by LXH on 2019/3/9.
 */
public class ActivationUser {
    private String serial;
    private String license;
    private int activation;

    public ActivationUser() {
    }

    public ActivationUser(String serial, String license) {
        this.serial = serial;
        this.license = license;
        this.activation = 0;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getActivation() {
        return activation;
    }

    public void setActivation(int activation) {
        this.activation = activation;
    }

    @Override
    public String toString() {
        return "ActivationUser{" +
                "serial='" + serial + '\'' +
                ", license='" + license + '\'' +
                ", activation=" + activation +
                '}';
    }
}
