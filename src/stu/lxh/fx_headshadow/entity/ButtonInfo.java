package stu.lxh.fx_headshadow.entity;


/**
 * Created by LXH on 2019/2/26.
 */
public class ButtonInfo {
    private String buttonId;
    private String imagePath;
    private double width;
    private double height;

    public ButtonInfo() {
    }

    public ButtonInfo(String buttonId, String imagePath, double width, double height) {
        this.buttonId = buttonId;
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

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
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
                "buttonId='" + buttonId + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
