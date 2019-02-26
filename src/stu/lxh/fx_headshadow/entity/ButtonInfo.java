package stu.lxh.fx_headshadow.entity;

import javax.swing.text.html.ImageView;

/**
 * Created by LXH on 2019/2/26.
 */
public class ButtonInfo {
    private String id;
    private ImageView imageView;
    private double width;
    private double height;

    public ButtonInfo() {
    }

    public ButtonInfo(String id, ImageView imageView, double width, double height) {
        this.id = id;
        this.imageView = imageView;
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "ButtonInfo{" +
                "id='" + id + '\'' +
                ", imageView=" + imageView +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
