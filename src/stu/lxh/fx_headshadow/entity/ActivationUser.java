package stu.lxh.fx_headshadow.entity;

/**
 * Created by LXH on 2019/3/9.
 */
public class ActivationUser {
    private String serial;
    private String license;

    public ActivationUser() {
    }

    public ActivationUser(String serial, String license) {
        this.serial = serial;
        this.license = license;
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

    @Override
    public String toString() {
        return "ActivationUser{" +
                "serial='" + serial + '\'' +
                ", license='" + license + '\'' +
                '}';
    }
}
