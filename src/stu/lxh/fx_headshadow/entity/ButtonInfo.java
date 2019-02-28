package stu.lxh.fx_headshadow.entity;


import javafx.scene.image.Image;

/**
 * Created by LXH on 2019/2/26.
 */
public class ButtonInfo {
    private String id;
    private String imagePath;
    private double width;
    private double height;

    public ButtonInfo() {
    }

    public ButtonInfo(String id, String imagePath, double width, double height) {
        this.id = id;
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "ButtonInfo{" +
                "id='" + id + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
