package stu.lxh.fx_headshadow.entity;

import javafx.geometry.Point2D;

/**
 * Created by LXH on 2019/2/12.
 */
public class Point {
    String pointName;
    Point2D point2D;

    public Point(String pointName, Point2D point2D) {
        this.pointName = pointName;
        this.point2D = point2D;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public Point2D getPoint2D() {
        return point2D;
    }

    public void setPoint2D(Point2D point2D) {
        this.point2D = point2D;
    }

    @Override
    public String toString() {
        return "Point{" +
                "pointName='" + pointName + '\'' +
                ", point2D=" + point2D +
                '}';
    }
}
