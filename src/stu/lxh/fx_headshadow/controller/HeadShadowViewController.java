package stu.lxh.fx_headshadow.controller;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import stu.lxh.fx_headshadow.entity.Point;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by LXH on 2019/1/29.
 */
public class HeadShadowViewController {
    @FXML
    private AnchorPane patientPane;
    @FXML
    private HBox content;
    @FXML
    private Canvas canvas;
    @FXML
    private ImageView patientXImage;
    @FXML
    private ImageView headShadowGuidanceImageView;

    @FXML
    private TextArea measurementResultTextArea;

    @FXML
    private Label OrPointLabel;
    @FXML
    private Label OrPointTextLabel;
    @FXML
    private Label PtmPointLabel;
    @FXML
    private Label PtmPointTextLabel;
    @FXML
    private Label ANSPointLabel;
    @FXML
    private Label ANSPointTextLabel;
    @FXML
    private Label PNSPointLabel;
    @FXML
    private Label PNSPointTextLabel;
    @FXML
    private Label APointLabel;
    @FXML
    private Label APointTextLabel;
    @FXML
    private Label AMaxPointLabel;
    @FXML
    private Label AMaxPointTextLabel;
    @FXML
    private Label SPrPointLabel;
    @FXML
    private Label SPrPointTextLabel;
    @FXML
    private Label UIPointLabel;
    @FXML
    private Label UIPointTextLabel;
    @FXML
    private Label UIAPointLabel;
    @FXML
    private Label UIAPointTextLabel;
    @FXML
    private Label UMoPointLabel;
    @FXML
    private Label UMoPointTextLabel;


    @FXML
    private Label LIPointLabel;
    @FXML
    private Label LIPointTextLabel;
    @FXML
    private Label LIAPointLabel;
    @FXML
    private Label LIAPointTextLabel;
    @FXML
    private Label CoPointLabel;
    @FXML
    private Label CoPointTextLabel;
    @FXML
    private Label ArPointLabel;
    @FXML
    private Label ArPointTextLabel;
    @FXML
    private Label GoPointLabel;
    @FXML
    private Label GoPointTextLabel;
    @FXML
    private Label LMoPointLabel;
    @FXML
    private Label LMoPointTextLabel;
    @FXML
    private Label MFPointLabel;
    @FXML
    private Label MFPointTextLabel;
    @FXML
    private Label DPointLabel;
    @FXML
    private Label DPointTextLabel;
    @FXML
    private Label PogPointLabel;
    @FXML
    private Label PogPointTextLabel;
    @FXML
    private Label GnPointLabel;
    @FXML
    private Label GnPointTextLabel;
    @FXML
    private Label MePointLabel;
    @FXML
    private Label MePointTextLabel;
    @FXML
    private Label BPointLabel;
    @FXML
    private Label BPointTextLabel;
    @FXML
    private Label IdPointLabel;
    @FXML
    private Label IdPointTextLabel;
    @FXML
    private Label SPointLabel;
    @FXML
    private Label SPointTextLabel;
    @FXML
    private Label NaPointLabel;
    @FXML
    private Label NaPointTextLabel;
    @FXML
    private Label PoPointLabel;
    @FXML
    private Label PoPointTextLabel;
    @FXML
    private Label BaPointTextLabel;
    @FXML
    private Label BaPointLabel;

    @FXML
    private CheckBox OrCheckBox;
    @FXML
    private CheckBox PtmCheckBox;
    @FXML
    private CheckBox ANSCheckBox;
    @FXML
    private CheckBox PNSCheckBox;
    @FXML
    private CheckBox ACheckBox;
    @FXML
    private CheckBox AMmaxCheckBox;
    @FXML
    private CheckBox SPrCheckBox;
    @FXML
    private CheckBox UICheckBox;
    @FXML
    private CheckBox UIACheckBox;
    @FXML
    private CheckBox UMOCheckBox;
    @FXML
    private CheckBox SCheckBox;
    @FXML
    private CheckBox NaCheckBox;
    @FXML
    private CheckBox PoCheckBox;
    @FXML
    private CheckBox BaCheckBox;

    @FXML
    private CheckBox LICheckBox;
    @FXML
    private CheckBox LIACheckBox;
    @FXML
    private CheckBox IdCheckBox;
    @FXML
    private CheckBox BCheckBox;
    @FXML
    private CheckBox PogCheckBox;
    @FXML
    private CheckBox MeCheckBox;
    @FXML
    private CheckBox GnCheckBox;
    @FXML
    private CheckBox DCheckBox;
    @FXML
    private CheckBox GoCheckBox;
    @FXML
    private CheckBox ArCheckBox;
    @FXML
    private CheckBox CoCheckBox;
    @FXML
    private CheckBox LMoCheckBox;
    @FXML
    private CheckBox MFCheckBox;

    /**
     * 上中牙切点
     */
    @FXML
    private CheckBox angleOfUpperIncisorCheckBox;
    /**
     * 1-NA角
     */
    @FXML
    private CheckBox NAAngleCheckBox;
    /**
     * SNPr（上牙槽缘角）
     */
    @FXML
    private CheckBox SNPrCheckBox;
    @FXML
    private CheckBox NAMMCheckBox;
    @FXML
    private CheckBox incisorPositionCheckBox;
    @FXML
    private CheckBox heightOfUpperIncisorAndAlveolarCheckBox;
    @FXML
    private CheckBox SNBAngleCheckBox;
    @FXML
    private CheckBox SNPAngleCheckBox;
    @FXML
    private CheckBox SNDAngleCheckBox;
    @FXML
    private CheckBox YAxisAngleCheckBox;
    @FXML
    private CheckBox YAxisAngleDownsCheckBox;
    @FXML
    private CheckBox facialAngleCheckBox;
    @FXML
    private CheckBox mandibularPlaneToFrankfortPlaneAngleCheckBox;
    @FXML
    private CheckBox mandibularPlaneToFrankfortPlaneSNAngleCheckBox;
    @FXML
    private CheckBox gonialAngleCheckBox;
    @FXML
    private CheckBox extentOfMandibularBaseCheckBox;
    @FXML
    private CheckBox extentOfAscendingRamusCheckBox;
    @FXML
    private CheckBox SNAAngleCheckBox;

    @FXML
    private Button revokeButton;

    private static File patientImageFile;

    // private static double defaultRate = 1.1;

    private GraphicsContext graphicsContext;

    private List<Point2D> point2DList;
    private Map<Point2D, Point> point2DMap;
    private Map<String, CheckBox> checkBoxMap;
    private Map<String, Label> labelMap;

    static {
        patientImageFile = null;
    }

    @FXML
    public void initialize() {
        point2DList = new LinkedList<>();
        point2DMap = new HashMap<>();
        checkBoxMap = new HashMap<>();
        labelMap = new HashMap<>();
        init();
        dealAction();
    }

    private void dealAction() {
        // TODO 用户头影区域的初始化
//        canvas.setOnScroll(event -> {
//            double deltaY = event.getDeltaY();
//            System.out.println(deltaY);
//            // 保留width ： height
//            patientXImage.setPreserveRatio(true);
//            // 向上滚动,放大图片
//            if(deltaY > 0) {
//                double width = patientXImage.getFitWidth() * defaultRate;
//                double height = patientXImage.getFitHeight() * defaultRate;
//                patientXImage.setFitWidth(width);
//                patientXImage.setFitHeight(height);
//                canvas.setWidth(width);
//                canvas.setHeight(height);
//            // 向下滚动,缩小图片
//            } else {
//                double width = patientXImage.getFitWidth() / defaultRate;
//                double height = patientXImage.getFitHeight() / defaultRate;
//                patientXImage.setFitWidth(width);
//                patientXImage.setFitHeight(height);
//                canvas.setWidth(width);
//                canvas.setHeight(height);
//            }
//        });

        // TODO 完成测量项目的逻辑
        // 测量上中切牙角
        angleOfUpperIncisorCheckBox.setOnMouseClicked(event -> {
            if(angleOfUpperIncisorCheckBox.isSelected()) {
                try {
                    // 上中切牙长轴与前颅底平面（蝶鞍点S与鼻根点Na的连线平面）的后下交角
                    // 1、判断所测量的项目中的所需要的点是否存在
                    Point2D UI = null;
                    Point2D UIA = null;
                    Point2D S = null;
                    Point2D Na = null;
                    for(Point2D point2D : point2DMap.keySet()) {
                        Point point = point2DMap.get(point2D);
                        // 上中切牙长轴所需要的两个点为UI和UIA点
                        if(point.getPointName().equals("UI")) {
                            UI = point2D;
                        }
                        if(point.getPointName().equals("UIA")) {
                            UIA = point2D;
                        }
                        // 前颅底平面所需要的两个点为S点与Na点
                        if(point.getPointName().equals("S")) {
                            S = point2D;
                        }
                        if(point.getPointName().equals("Na")) {
                            Na = point2D;
                        }
                    }
                    if(UI == null || UIA == null || S == null || Na == null) {
                        // 有一个为null，则说明标点不全，提醒用户，所标点不全
                        throw new Exception("您所标记的点不全");
                    }
                    // 2、进行连线
                    graphicsContext.setLineWidth(1);
                    // result用于记录两个直线的交点
                    Point2D result = getLineearIntersectionPoint(S, Na, UI, UIA);
                    System.out.println(result.getX() + " " + result.getY());

                    Line line = new Line();
                    line.setStartX(UI.getX());
                    line.setStartY(UI.getY());
                    line.setEndX(UIA.getX());
                    line.setEndY(UIA.getY());
                    if(!line.contains(result)) {
                        line.setEndX(result.getX());
                        line.setEndY(result.getY());
                    }
                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                    line = new Line();
                    line.setStartX(S.getX());
                    line.setStartY(S.getY());
                    line.setEndX(Na.getX());
                    line.setEndY(Na.getY());
                    if(!line.contains(result)) {
                        line.setEndX(result.getX());
                        line.setEndY(result.getY());
                    }
                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                    // 3、测量对应测量项目的角度值或距离值
                    // 参数顺序：需要计算的角，角1， 角2
                    double angle = Angle(result, S, UI);
                    // TODO 将结果添加入TextArea中
                    System.out.println("角度是：" + angle);

                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Warning Information");
                    alert.setContentText("您所标记的点不全");
                    alert.showAndWait();
                    // 取消选中状态
                    angleOfUpperIncisorCheckBox.setSelected(false);
                }
            }
        });

        SNAAngleCheckBox.setOnMouseClicked(event -> {
            if(SNAAngleCheckBox.isSelected()) {
                try {
                    // 上中切牙长轴与前颅底平面（蝶鞍点S与鼻根点Na的连线平面）的后下交角
                    // 1、判断所测量的项目中的所需要的点是否存在
                    Point2D A = null;
                    Point2D S = null;
                    Point2D Na = null;
                    for(Point2D point2D : point2DMap.keySet()) {
                        Point point = point2DMap.get(point2D);
                        // 上齿槽座点和鼻根点所需要的两个点为A和Na点
                        if(point.getPointName().equals("A")) {
                            A = point2D;
                        }
                        // 前颅底平面所需要的两个点为S点与Na点
                        if(point.getPointName().equals("S")) {
                            S = point2D;
                        }
                        if(point.getPointName().equals("Na")) {
                            Na = point2D;
                        }
                    }
                    if(A == null || S == null || Na == null) {
                        // 有一个为null，则说明标点不全，提醒用户，所标点不全
                        throw new Exception("您所标记的点不全");
                    }
                    // 2、进行连线
                    graphicsContext.setLineWidth(3);
                    // result用于记录两个直线的交点
                    Point2D result = getLineearIntersectionPoint(S, Na, A, Na);
                    System.out.println(result.getX() + " " + result.getY());

                    Line line = new Line();
                    line.setStartX(A.getX());
                    line.setStartY(A.getY());
                    line.setEndX(Na.getX());
                    line.setEndY(Na.getY());
                    if(!line.contains(result)) {
                        line.setEndX(result.getX());
                        line.setEndY(result.getY());
                    }
                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                    line = new Line();
                    line.setStartX(S.getX());
                    line.setStartY(S.getY());
                    line.setEndX(Na.getX());
                    line.setEndY(Na.getY());
                    if(!line.contains(result)) {
                        line.setEndX(result.getX());
                        line.setEndY(result.getY());
                    }
                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                    // 3、测量对应测量项目的角度值或距离值
                    // 参数顺序：需要计算的角，角1， 角2
                    double angle = Angle(result, S, A);
                    // TODO 将结果添加入TextArea中
                    System.out.println("SNA角度是：" + angle);

                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Warning Information");
                    alert.setContentText("您所标记的点不全");
                    alert.showAndWait();
                    // 取消选中状态
                    angleOfUpperIncisorCheckBox.setSelected(false);
                }
            }
        });

        NAAngleCheckBox.setOnMouseClicked(event -> {
            if(NAAngleCheckBox.isSelected()) {

            }
        });

        SNPrCheckBox.setOnMouseClicked(event -> {
            if(SNPrCheckBox.isSelected()) {
            }
        });

        NAMMCheckBox.setOnMouseClicked(event -> {
            if(NAMMCheckBox.isSelected()) {
            }
        });

        incisorPositionCheckBox.setOnMouseClicked(event -> {
            if(incisorPositionCheckBox.isSelected()) {
            }
        });

        heightOfUpperIncisorAndAlveolarCheckBox.setOnMouseClicked(event -> {
            if(heightOfUpperIncisorAndAlveolarCheckBox.isSelected()) {
            }
        });

        SNBAngleCheckBox.setOnMouseClicked(event -> {
            if(SNBAngleCheckBox.isSelected()) {
                try {
                    // 上中切牙长轴与前颅底平面（蝶鞍点S与鼻根点Na的连线平面）的后下交角
                    // 1、判断所测量的项目中的所需要的点是否存在
                    Point2D Id = null;
                    Point2D S = null;
                    Point2D Na = null;
                    for(Point2D point2D : point2DMap.keySet()) {
                        Point point = point2DMap.get(point2D);
                        // 下齿槽座点至鼻根点所需要的两个点为Id和Na点
                        if(point.getPointName().equals("Id")) {
                            Id = point2D;
                        }
                        // 前颅底平面所需要的两个点为S点与Na点
                        if(point.getPointName().equals("S")) {
                            S = point2D;
                        }
                        if(point.getPointName().equals("Na")) {
                            Na = point2D;
                        }
                    }
                    if(Id == null || S == null || Na == null) {
                        // 有一个为null，则说明标点不全，提醒用户，所标点不全
                        throw new Exception("您所标记的点不全");
                    }
                    // 2、进行连线
                    graphicsContext.setLineWidth(3);
                    // result用于记录两个直线的交点
                    Point2D result = getLineearIntersectionPoint(S, Na, Id, Na);
                    System.out.println("123: " + result.getX() + " " + result.getY());
                    System.out.println("456: " + Na.getX() + " " + Na.getY());

                    Line line = new Line();
                    line.setStartX(Id.getX());
                    line.setStartY(Id.getY());
                    line.setEndX(Na.getX());
                    line.setEndY(Na.getY());
                    if(!line.contains(result)) {
                        line.setEndX(result.getX());
                        line.setEndY(result.getY());
                    }
                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                    line = new Line();
                    line.setStartX(S.getX());
                    line.setStartY(S.getY());
                    line.setEndX(Na.getX());
                    line.setEndY(Na.getY());
                    if(!line.contains(result)) {
                        line.setEndX(result.getX());
                        line.setEndY(result.getY());
                    }
                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                    // 3、测量对应测量项目的角度值或距离值
                    // 参数顺序：需要计算的角，角1， 角2
                    double angle = Angle(result, S, Id);
                    // TODO 将结果添加入TextArea中
                    System.out.println("SNB角度是：" + angle);

                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Warning Information");
                    alert.setContentText("您所标记的点不全");
                    alert.showAndWait();
                    // 取消选中状态
                    angleOfUpperIncisorCheckBox.setSelected(false);
                }
            }
        });

        SNPAngleCheckBox.setOnMouseClicked(event -> {
            if(SNPAngleCheckBox.isSelected()) {
                try {
                    // 上中切牙长轴与前颅底平面（蝶鞍点S与鼻根点Na的连线平面）的后下交角
                    // 1、判断所测量的项目中的所需要的点是否存在
                    Point2D Pog = null;
                    Point2D S = null;
                    Point2D Na = null;
                    for(Point2D point2D : point2DMap.keySet()) {
                        Point point = point2DMap.get(point2D);
                        // 上齿槽座点和鼻根点所需要的两个点为A和Na点
                        if(point.getPointName().equals("Pog")) {
                            Pog = point2D;
                        }
                        // 前颅底平面所需要的两个点为S点与Na点
                        if(point.getPointName().equals("S")) {
                            S = point2D;
                        }
                        if(point.getPointName().equals("Na")) {
                            Na = point2D;
                        }
                    }
                    if(Pog == null || S == null || Na == null) {
                        // 有一个为null，则说明标点不全，提醒用户，所标点不全
                        throw new Exception("您所标记的点不全");
                    }
                    // 2、进行连线
                    graphicsContext.setLineWidth(3);
                    // result用于记录两个直线的交点
                    Point2D result = getLineearIntersectionPoint(S, Na, Pog, Na);
                    System.out.println(result.getX() + " " + result.getY());

                    Line line = new Line();
                    line.setStartX(Pog.getX());
                    line.setStartY(Pog.getY());
                    line.setEndX(Na.getX());
                    line.setEndY(Na.getY());
                    if(!line.contains(result)) {
                        line.setEndX(result.getX());
                        line.setEndY(result.getY());
                    }
                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                    line = new Line();
                    line.setStartX(S.getX());
                    line.setStartY(S.getY());
                    line.setEndX(Na.getX());
                    line.setEndY(Na.getY());
                    if(!line.contains(result)) {
                        line.setEndX(result.getX());
                        line.setEndY(result.getY());
                    }
                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                    // 3、测量对应测量项目的角度值或距离值
                    // 参数顺序：需要计算的角，角1， 角2
                    double angle = Angle(result, S, Pog);
                    // TODO 将结果添加入TextArea中
                    System.out.println("SNP角度是：" + angle);

                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Warning Information");
                    alert.setContentText("您所标记的点不全");
                    alert.showAndWait();
                    // 取消选中状态
                    angleOfUpperIncisorCheckBox.setSelected(false);
                }
            }
        });

        SNDAngleCheckBox.setOnMouseClicked(event -> {
            if(SNDAngleCheckBox.isSelected()) {
            }
        });

        YAxisAngleCheckBox.setOnMouseClicked(event -> {
            if(YAxisAngleCheckBox.isSelected()) {
            }
        });

        YAxisAngleDownsCheckBox.setOnMouseClicked(event -> {
            if(YAxisAngleDownsCheckBox.isSelected()) {
            }
        });

        facialAngleCheckBox.setOnMouseClicked(event -> {
            if(facialAngleCheckBox.isSelected()) {
            }
        });

        mandibularPlaneToFrankfortPlaneAngleCheckBox.setOnMouseClicked(event -> {
            if(mandibularPlaneToFrankfortPlaneAngleCheckBox.isSelected()) {
            }
        });

        mandibularPlaneToFrankfortPlaneSNAngleCheckBox.setOnMouseClicked(event -> {
            if(mandibularPlaneToFrankfortPlaneSNAngleCheckBox.isSelected()) {
            }
        });

        gonialAngleCheckBox.setOnMouseClicked(event -> {
            if(gonialAngleCheckBox.isSelected()) {
            }
        });

        extentOfMandibularBaseCheckBox.setOnMouseClicked(event -> {
            if(extentOfMandibularBaseCheckBox.isSelected()) {
            }
        });

        extentOfAscendingRamusCheckBox.setOnMouseClicked(event -> {
            if(extentOfAscendingRamusCheckBox.isSelected()) {
            }
        });

        // TODO 撤销点操作
        revokeButton.setOnMouseClicked(event -> {
            Point2D revokePoint2D = point2DList.get(point2DList.size() - 1);
            // TODO 在画布中擦除
            graphicsContext.clearRect(revokePoint2D.getX(), revokePoint2D.getY(), 5, 5);
            // 在map中删除
            Point point = point2DMap.get(revokePoint2D);
            CheckBox checkBox = checkBoxMap.get(point.getPointName());
            // 取消选中
            checkBox.setSelected(false);
            checkBox.setDisable(false);
            point2DList.remove(revokePoint2D);
            point2DMap.remove(revokePoint2D);
        });

        // TODO HBox的点击事件，并获得点击点的坐标信息
        canvas.setOnMouseClicked(event -> {
            // TODO 画点,要去必须选择一个CheckBox，才进行画点操作
            CheckBox checkBox = null;
            if(graphicsContext == null) {
                synchronized (HeadShadowViewController.class) {
                    if(graphicsContext == null) {
                        graphicsContext = canvas.getGraphicsContext2D();
                    }
                }
            }
            // 设置内容颜色
            graphicsContext.setFill(Color.RED);
            // 设置线条颜色
            graphicsContext.setStroke(Color.RED);
            System.out.println(event.getX() + " " + event.getY());

            // 选择某一个CheckBox，进行画点操作
            if((!OrCheckBox.isDisable() && OrCheckBox.isSelected()) || (!PtmCheckBox.isDisable() && PtmCheckBox.isSelected()) ||
                    (!ANSCheckBox.isDisable() && ANSCheckBox.isSelected()) || (!PNSCheckBox.isDisable() && PNSCheckBox.isSelected()) || (!ACheckBox.isDisable() && ACheckBox.isSelected()) ||
                    (!AMmaxCheckBox.isDisable() && AMmaxCheckBox.isSelected()) || (!SPrCheckBox.isDisable() && SPrCheckBox.isSelected()) || (!UICheckBox.isDisable() && UICheckBox.isSelected()) ||
                    (!UIACheckBox.isDisable() && UIACheckBox.isSelected()) || (!UMOCheckBox.isDisable() && UMOCheckBox.isSelected()) ||
                    (!LICheckBox.isDisable() && LICheckBox.isSelected()) || (!LIACheckBox.isDisable() && LIACheckBox.isSelected()) || (!IdCheckBox.isDisable() && IdCheckBox.isSelected()) ||
                    (!BCheckBox.isDisable() && BCheckBox.isSelected()) || (!PogCheckBox.isDisable() && PogCheckBox.isSelected()) ||
                    (!MeCheckBox.isDisable() && MeCheckBox.isSelected()) || (!GnCheckBox.isDisable() && GnCheckBox.isSelected()) || (!DCheckBox.isDisable() && DCheckBox.isSelected()) ||
                    (!GoCheckBox.isDisable() && GoCheckBox.isSelected()) || (!ArCheckBox.isDisable() && ArCheckBox.isSelected()) ||
                    (!CoCheckBox.isDisable() && CoCheckBox.isSelected()) || (!LMoCheckBox.isDisable() && LMoCheckBox.isSelected()) || (!MFCheckBox.isDisable() && MFCheckBox.isSelected()) ||
                    (!BaCheckBox.isDisable() && BaCheckBox.isSelected()) || (!PoCheckBox.isDisable() && PoCheckBox.isSelected()) || (!SCheckBox.isDisable() && SCheckBox.isSelected()) ||
                    (!NaCheckBox.isDisable() && NaCheckBox.isSelected())) {
                graphicsContext.fillOval(event.getX(), event.getY(), 3, 3);
                Point2D point2D = new Point2D(event.getX(), event.getY());
                // 将点坐标存储下来，用以撤销
                point2DList.add(point2D);
                String pointName = null;
                if(OrCheckBox.isSelected() && !OrCheckBox.isDisable()) {
                    pointName = OrCheckBox.getText();
                    checkBox = OrCheckBox;
                } else if(PtmCheckBox.isSelected() && !PtmCheckBox.isDisable()) {
                    pointName = PtmCheckBox.getText();
                    checkBox = PtmCheckBox;
                } else if(ANSCheckBox.isSelected() && !ANSCheckBox.isDisable()) {
                    pointName = ANSCheckBox.getText();
                    checkBox = ANSCheckBox;
                } else if(PNSCheckBox.isSelected() && !PNSCheckBox.isDisable()) {
                    pointName = PNSCheckBox.getText();
                    checkBox = PNSCheckBox;
                } else if(ACheckBox.isSelected() && !ACheckBox.isDisable()) {
                    pointName = ACheckBox.getText();
                    checkBox = ACheckBox;
                } else if(AMmaxCheckBox.isSelected() && !AMmaxCheckBox.isDisable()) {
                    pointName = AMmaxCheckBox.getText();
                    checkBox = AMmaxCheckBox;
                } else if(SPrCheckBox.isSelected() && !SPrCheckBox.isDisable()) {
                    pointName = SPrCheckBox.getText();
                    checkBox = SPrCheckBox;
                } else if(UICheckBox.isSelected() && !UICheckBox.isDisable()) {
                    pointName = UICheckBox.getText();
                    checkBox = UICheckBox;
                } else if(UIACheckBox.isSelected() && !UIACheckBox.isDisable()) {
                    pointName = UIACheckBox.getText();
                    checkBox = UIACheckBox;
                } else if(UMOCheckBox.isSelected() && !UMOCheckBox.isDisable()) {
                    pointName = UMOCheckBox.getText();
                    checkBox = UMOCheckBox;
                } else if(BaCheckBox.isSelected() && !BaCheckBox.isDisable()) {
                    pointName = BaCheckBox.getText();
                    checkBox = BaCheckBox;
                } else if(PoCheckBox.isSelected() && !PoCheckBox.isDisable()) {
                    pointName = PoCheckBox.getText();
                    checkBox = PoCheckBox;
                } else if(SCheckBox.isSelected() && !SCheckBox.isDisable()) {
                    pointName = SCheckBox.getText();
                    checkBox = SCheckBox;
                } else if(NaCheckBox.isSelected() && !NaCheckBox.isDisable()) {
                    pointName = NaCheckBox.getText();
                    checkBox = NaCheckBox;
                } else if(PoCheckBox.isSelected() && !LICheckBox.isDisable()) {
                    pointName = LICheckBox.getText();
                    checkBox = LICheckBox;
                } else if(LIACheckBox.isSelected() && !LIACheckBox.isDisable()) {
                    pointName = LIACheckBox.getText();
                    checkBox = LIACheckBox;
                } else if(IdCheckBox.isSelected() && !IdCheckBox.isDisable()) {
                    pointName = IdCheckBox.getText();
                    checkBox = IdCheckBox;
                } else if(BCheckBox.isSelected() && !BCheckBox.isDisable()) {
                    pointName = BCheckBox.getText();
                    checkBox = BCheckBox;
                } else if(PogCheckBox.isSelected() && !PogCheckBox.isDisable()) {
                    pointName = PogCheckBox.getText();
                    checkBox = PogCheckBox;
                } else if(MeCheckBox.isSelected() && !MeCheckBox.isDisable()) {
                    pointName = MeCheckBox.getText();
                    checkBox = MeCheckBox;
                } else if(GnCheckBox.isSelected() && !GnCheckBox.isDisable()) {
                    pointName = GnCheckBox.getText();
                    checkBox = GnCheckBox;
                } else if(DCheckBox.isSelected() && !DCheckBox.isDisable()) {
                    pointName = DCheckBox.getText();
                    checkBox = DCheckBox;
                } else if(GoCheckBox.isSelected() && !GoCheckBox.isDisable()) {
                    pointName = GoCheckBox.getText();
                    checkBox = GoCheckBox;
                } else if(ArCheckBox.isSelected() && !ArCheckBox.isDisable()) {
                    pointName = ArCheckBox.getText();
                    checkBox = ArCheckBox;
                } else if(CoCheckBox.isSelected() && !CoCheckBox.isDisable()) {
                    pointName = CoCheckBox.getText();
                    checkBox = CoCheckBox;
                } else if(LMoCheckBox.isSelected() && !LMoCheckBox.isDisable()) {
                    pointName = LMoCheckBox.getText();
                    checkBox = LMoCheckBox;
                } else if(MFCheckBox.isSelected() && !MFCheckBox.isDisable()) {
                    pointName = MFCheckBox.getText();
                    checkBox = MFCheckBox;
                }

                Point point = new Point(pointName, point2D);
                // 将点加入map中
                point2DMap.put(point2D, point);
                checkBox.setSelected(true);
                checkBox.setDisable(true);
                // OrPointLabel, OrPointTextLabel
                Label pointLabel = labelMap.get(checkBox.getText() + "PointLabel");
                Label pointTextLabel = labelMap.get(checkBox.getText() + "PointTextLabel");
                setLabelVisible(pointLabel, pointTextLabel, false);
            } else {
                // 提示用户需选择一个CheckBox
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Information");
                alert.setContentText("若没有标记完，请先选择一个要标记的点");
                alert.showAndWait();
            }
        });

        // TODO 功能选择区域功能
        OrCheckBox.setOnMouseClicked(event -> {
            if(OrCheckBox.isSelected()) {
                setLabelVisible(OrPointLabel, OrPointTextLabel, true);
            } else {
                setLabelVisible(OrPointLabel, OrPointTextLabel, false);
            }
        });

        PtmCheckBox.setOnMouseClicked(event -> {
            if(PtmCheckBox.isSelected()) {
                setLabelVisible(PtmPointLabel, PtmPointTextLabel, true);
            } else {
                setLabelVisible(PtmPointLabel, PtmPointTextLabel, false);
            }
        });

        ANSCheckBox.setOnMouseClicked(event -> {
            if(ANSCheckBox.isSelected()) {
                setLabelVisible(ANSPointLabel, ANSPointTextLabel, true);
            } else {
                setLabelVisible(ANSPointLabel, ANSPointTextLabel, false);
            }
        });

        PNSCheckBox.setOnMouseClicked(event -> {
            if(PNSCheckBox.isSelected()) {
                setLabelVisible(PNSPointLabel, PNSPointTextLabel, true);
            } else {
                setLabelVisible(PNSPointLabel, PNSPointTextLabel, false);
            }
        });

        ACheckBox.setOnMouseClicked(event -> {
            if(ACheckBox.isSelected()) {
                setLabelVisible(APointLabel, APointTextLabel, true);
            } else {
                setLabelVisible(APointLabel, APointTextLabel, false);
            }
        });

        AMmaxCheckBox.setOnMouseClicked(event -> {
            if(AMmaxCheckBox.isSelected()) {
                setLabelVisible(AMaxPointLabel, AMaxPointTextLabel, true);
            } else {
                setLabelVisible(AMaxPointLabel, AMaxPointTextLabel, false);
            }
        });

        SPrCheckBox.setOnMouseClicked(event -> {
            if(SPrCheckBox.isSelected()) {
                setLabelVisible(SPrPointLabel, SPrPointTextLabel, true);
            } else {
                setLabelVisible(SPrPointLabel, SPrPointTextLabel, false);
            }
        });

        UICheckBox.setOnMouseClicked(event -> {
            if(UICheckBox.isSelected()) {
                setLabelVisible(UIPointLabel, UIPointTextLabel, true);
            } else {
                setLabelVisible(UIPointLabel, UIPointTextLabel, false);
            }
        });

        UIACheckBox.setOnMouseClicked(event -> {
            if(UIACheckBox.isSelected()) {
                setLabelVisible(UIAPointLabel, UIAPointTextLabel, true);
            } else {
                setLabelVisible(UIAPointLabel, UIAPointTextLabel, false);
            }
        });

        UMOCheckBox.setOnMouseClicked(event -> {
            if(UMOCheckBox.isSelected()) {
                setLabelVisible(UMoPointLabel, UMoPointTextLabel, true);
            } else {
                setLabelVisible(UMoPointLabel, UMoPointTextLabel, false);
            }
        });

        SCheckBox.setOnMouseClicked(event -> {
            if(SCheckBox.isSelected()) {
                setLabelVisible(SPointLabel, SPointTextLabel, true);
            } else {
                setLabelVisible(SPointLabel, SPointTextLabel, false);
            }
        });

        NaCheckBox.setOnMouseClicked(event -> {
            if(NaCheckBox.isSelected()) {
                setLabelVisible(NaPointLabel, NaPointTextLabel, true);
            } else {
                setLabelVisible(NaPointLabel, NaPointTextLabel, false);
            }
        });

        PoCheckBox.setOnMouseClicked(event -> {
            if(PoCheckBox.isSelected()) {
                setLabelVisible(PoPointLabel, PoPointTextLabel, true);
            } else {
                setLabelVisible(PoPointLabel, PoPointTextLabel, false);
            }
        });

        BaCheckBox.setOnMouseClicked(event -> {
            if(BaCheckBox.isSelected()) {
                setLabelVisible(BaPointLabel, BaPointTextLabel, true);
            } else {
                setLabelVisible(BaPointLabel, BaPointTextLabel, false);
            }
        });

        LICheckBox.setOnMouseClicked(event -> {
            if(LICheckBox.isSelected()) {
                setLabelVisible(LIPointLabel, LIPointTextLabel, true);
            } else {
                setLabelVisible(LIPointLabel, LIPointTextLabel, false);
            }
        });

        LIACheckBox.setOnMouseClicked(event -> {
            if(LIACheckBox.isSelected()) {
                setLabelVisible(LIAPointLabel, LIAPointTextLabel, true);
            } else {
                setLabelVisible(LIAPointLabel, LIAPointTextLabel, false);
            }
        });

        IdCheckBox.setOnMouseClicked(event -> {
            if(IdCheckBox.isSelected()) {
                setLabelVisible(IdPointLabel, IdPointTextLabel, true);
            } else {
                setLabelVisible(IdPointLabel, IdPointTextLabel, false);
            }
        });

        BCheckBox.setOnMouseClicked(event -> {
            if(BCheckBox.isSelected()) {
                setLabelVisible(BPointLabel, BPointTextLabel, true);
            } else {
                setLabelVisible(BPointLabel, BPointTextLabel, false);
            }
        });

        PogCheckBox.setOnMouseClicked(event -> {
            if(PogCheckBox.isSelected()) {
                setLabelVisible(PogPointLabel, PogPointTextLabel, true);
            } else {
                setLabelVisible(PogPointLabel, PogPointTextLabel, false);
            }
        });

        MeCheckBox.setOnMouseClicked(event -> {
            if(MeCheckBox.isSelected()) {
                setLabelVisible(MePointLabel, MePointTextLabel, true);
            } else {
                setLabelVisible(MePointLabel, MePointTextLabel, false);
            }
        });

        GnCheckBox.setOnMouseClicked(event -> {
            if(GnCheckBox.isSelected()) {
                setLabelVisible(GnPointLabel, GnPointTextLabel, true);
            } else {
                setLabelVisible(GnPointLabel, GnPointTextLabel, false);
            }
        });

        DCheckBox.setOnMouseClicked(event -> {
            if(DCheckBox.isSelected()) {
                setLabelVisible(DPointLabel, DPointTextLabel, true);
            } else {
                setLabelVisible(DPointLabel, DPointTextLabel, false);
            }
        });

        GoCheckBox.setOnMouseClicked(event -> {
            if(GoCheckBox.isSelected()) {
                setLabelVisible(GoPointLabel, GoPointTextLabel, true);
            } else {
                setLabelVisible(GoPointLabel, GoPointTextLabel, false);
            }
        });

        ArCheckBox.setOnMouseClicked(event -> {
            if(ArCheckBox.isSelected()) {
                setLabelVisible(ArPointLabel, ArPointTextLabel, true);
            } else {
                setLabelVisible(ArPointLabel, ArPointTextLabel, false);
            }
        });

        CoCheckBox.setOnMouseClicked(event -> {
            if(CoCheckBox.isSelected()) {
                setLabelVisible(CoPointLabel, CoPointTextLabel, true);
            } else {
                setLabelVisible(CoPointLabel, CoPointTextLabel, false);
            }
        });

        LMoCheckBox.setOnMouseClicked(event -> {
            if(LMoCheckBox.isSelected()) {
                setLabelVisible(LMoPointLabel, LMoPointTextLabel, true);
            } else {
                setLabelVisible(LMoPointLabel, LMoPointTextLabel, false);
            }
        });

        MFCheckBox.setOnMouseClicked(event -> {
            if(MFCheckBox.isSelected()) {
                setLabelVisible(MFPointLabel, MFPointTextLabel, true);
            } else {
                setLabelVisible(MFPointLabel, MFPointTextLabel, false);
            }
        });
    }

    public static void setPatientImageFile(File file) {
        patientImageFile = file;
    }

    private void init() {
        initMap();

        // TODO 初始化病人头影显示区
        content.setStyle("-fx-background-color:#f3f3f3;");
        content.setAlignment(Pos.CENTER);

        System.out.println(patientImageFile.getAbsolutePath());
        System.out.println("file:\\" + patientImageFile.getAbsolutePath());
        Image image = new Image("file:\\" + patientImageFile.getAbsolutePath());
        System.out.println(image);
        patientXImage.setImage(image);
        patientXImage.setFitWidth(image.getWidth() * 1.5);
        patientXImage.setFitHeight(image.getHeight() * 1.5);
        canvas.setWidth(patientXImage.getFitWidth());
        canvas.setHeight(patientXImage.getFitHeight());

        // ----------------------------------------------------------------------------
        headShadowGuidanceImageView.setImage(new Image("file:\\" + "E:\\教学课件\\IDEA_PROJECT\\Fx_HeadShadow\\resources\\config\\headShadow.png"));

        // TODO 初始化时，所有指示位置的指向，均不显示。
        setLabelVisible(OrPointLabel, OrPointTextLabel, false);
        setLabelVisible(PtmPointLabel, PtmPointTextLabel, false);
        setLabelVisible(ANSPointLabel, ANSPointTextLabel, false);
        setLabelVisible(PNSPointLabel, PNSPointTextLabel, false);
        setLabelVisible(APointLabel, APointTextLabel, false);
        setLabelVisible(AMaxPointLabel, AMaxPointTextLabel, false);
        setLabelVisible(SPrPointLabel, SPrPointTextLabel, false);
        setLabelVisible(UIPointLabel, UIPointTextLabel, false);
        setLabelVisible(UIAPointLabel, UIAPointTextLabel, false);
        setLabelVisible(UMoPointLabel, UMoPointTextLabel, false);
        setLabelVisible(LIPointLabel, LIPointTextLabel, false);
        setLabelVisible(LIAPointLabel, LIAPointTextLabel, false);
        setLabelVisible(CoPointLabel, CoPointTextLabel, false);
        setLabelVisible(ArPointLabel, ArPointTextLabel, false);
        setLabelVisible(GoPointLabel, GoPointTextLabel, false);
        setLabelVisible(LMoPointLabel, LMoPointTextLabel, false);
        setLabelVisible(MFPointLabel, MFPointTextLabel, false);
        setLabelVisible(DPointLabel, DPointTextLabel, false);
        setLabelVisible(PogPointLabel, PogPointTextLabel, false);
        setLabelVisible(GnPointLabel, GnPointTextLabel, false);
        setLabelVisible(MePointLabel, MePointTextLabel, false);
        setLabelVisible(BPointLabel, BPointTextLabel, false);
        setLabelVisible(IdPointLabel, IdPointTextLabel, false);
        setLabelVisible(SPointLabel, SPointTextLabel, false);
        setLabelVisible(NaPointLabel, NaPointTextLabel, false);
        setLabelVisible(PoPointLabel, PoPointTextLabel, false);
        setLabelVisible(BaPointLabel, BaPointTextLabel, false);
    }

    private void initMap() {
        checkBoxMap.put(OrCheckBox.getText(), OrCheckBox);
        checkBoxMap.put(PtmCheckBox.getText(), PtmCheckBox);
        checkBoxMap.put(ANSCheckBox.getText(), ANSCheckBox);
        checkBoxMap.put(PNSCheckBox.getText(), PNSCheckBox);
        checkBoxMap.put(ACheckBox.getText(), ACheckBox);
        checkBoxMap.put(AMmaxCheckBox.getText(), AMmaxCheckBox);
        checkBoxMap.put(SPrCheckBox.getText(), SPrCheckBox);
        checkBoxMap.put(UICheckBox.getText(), UICheckBox);
        checkBoxMap.put(UIACheckBox.getText(), UIACheckBox);
        checkBoxMap.put(UMOCheckBox.getText(), UMOCheckBox);
        checkBoxMap.put(LICheckBox.getText(), LICheckBox);
        checkBoxMap.put(LIACheckBox.getText(), LIACheckBox);
        checkBoxMap.put(IdCheckBox.getText(), IdCheckBox);
        checkBoxMap.put(BCheckBox.getText(), BCheckBox);
        checkBoxMap.put(PogCheckBox.getText(), PogCheckBox);
        checkBoxMap.put(MeCheckBox.getText(), MeCheckBox);
        checkBoxMap.put(GnCheckBox.getText(), GnCheckBox);
        checkBoxMap.put(DCheckBox.getText(), DCheckBox);
        checkBoxMap.put(GoCheckBox.getText(), GoCheckBox);
        checkBoxMap.put(ArCheckBox.getText(), ArCheckBox);
        checkBoxMap.put(CoCheckBox.getText(), CoCheckBox);
        checkBoxMap.put(LMoCheckBox.getText(), LMoCheckBox);
        checkBoxMap.put(MFCheckBox.getText(), MFCheckBox);
        checkBoxMap.put(SCheckBox.getText(), SCheckBox);
        checkBoxMap.put(NaCheckBox.getText(), NaCheckBox);
        checkBoxMap.put(PoCheckBox.getText(), PoCheckBox);
        checkBoxMap.put(BaCheckBox.getText(), BaCheckBox);

        labelMap.put("OrPointLabel", OrPointLabel);
        labelMap.put("OrPointTextLabel", OrPointTextLabel);
        labelMap.put("PtmPointLabel", PtmPointLabel);
        labelMap.put("PtmPointTextLabel", PtmPointTextLabel);
        labelMap.put("ANSPointLabel", ANSPointLabel);
        labelMap.put("ANSPointTextLabel", ANSPointTextLabel);
        labelMap.put("PNSPointLabel", PNSPointLabel);
        labelMap.put("PNSPointTextLabel", PNSPointTextLabel);
        labelMap.put("APointLabel", APointLabel);
        labelMap.put("APointTextLabel", APointTextLabel);
        labelMap.put("AMaxPointLabel", AMaxPointLabel);
        labelMap.put("AMaxPointTextLabel", AMaxPointTextLabel);
        labelMap.put("SPrPointLabel", SPrPointLabel);
        labelMap.put("SPrPointTextLabel", SPrPointTextLabel);
        labelMap.put("UIPointTextLabel", UIPointTextLabel);
        labelMap.put("UIPointLabel", UIPointLabel);
        labelMap.put("UIAPointLabel", UIAPointLabel);
        labelMap.put("UIAPointTextLabel", UIAPointTextLabel);
        labelMap.put("UMoPointTextLabel", UMoPointTextLabel);
        labelMap.put("UMoPointLabel", UMoPointLabel);
        labelMap.put("LIPointTextLabel", LIPointTextLabel);
        labelMap.put("LIPointLabel", LIPointLabel);
        labelMap.put("LIAPointLabel", LIAPointLabel);
        labelMap.put("LIAPointTextLabel", LIAPointTextLabel);
        labelMap.put("CoPointLabel", CoPointLabel);
        labelMap.put("CoPointTextLabel", CoPointTextLabel);
        labelMap.put("ArPointLabel", ArPointLabel);
        labelMap.put("ArPointTextLabel", ArPointTextLabel);
        labelMap.put("GoPointLabel", GoPointLabel);
        labelMap.put("GoPointTextLabel", GoPointTextLabel);
        labelMap.put("LMoPointLabel", LMoPointLabel);
        labelMap.put("LMoPointTextLabel", LMoPointTextLabel);
        labelMap.put("MFPointLabel", MFPointLabel);
        labelMap.put("MFPointTextLabel", MFPointTextLabel);
        labelMap.put("DPointLabel", DPointLabel);
        labelMap.put("DPointTextLabel", DPointTextLabel);
        labelMap.put("PogPointLabel", PogPointLabel);
        labelMap.put("PogPointTextLabel", PogPointTextLabel);
        labelMap.put("GnPointLabel", GnPointLabel);
        labelMap.put("GnPointTextLabel", GnPointTextLabel);
        labelMap.put("MePointLabel", MePointLabel);
        labelMap.put("MePointTextLabel", MePointTextLabel);
        labelMap.put("BPointLabel", BPointLabel);
        labelMap.put("BPointTextLabel", BPointTextLabel);
        labelMap.put("IdPointLabel", IdPointLabel);
        labelMap.put("IdPointTextLabel", IdPointTextLabel);
        labelMap.put("SPointLabel", SPointLabel);
        labelMap.put("SPointTextLabel", SPointTextLabel);
        labelMap.put("NaPointLabel", NaPointLabel);
        labelMap.put("NaPointTextLabel", NaPointTextLabel);
        labelMap.put("PoPointLabel", PoPointLabel);
        labelMap.put("PoPointTextLabel", PoPointTextLabel);
        labelMap.put("BaPointLabel", BaPointLabel);
        labelMap.put("BaPointTextLabel", BaPointTextLabel);
    }

    /**
     * 计算两个直线的夹角
     */
    private double Angle(Point2D cen, Point2D first, Point2D second) {
        double dx1, dx2, dy1, dy2;
        double angle;

        dx1 = first.getX() - cen.getX();
        dy1 = first.getY() - cen.getY();

        dx2 = second.getX() - cen.getX();
        dy2 = second.getX() - cen.getY();

        double c = Math.sqrt(dx1 * dx1 + dy1 * dy1) * Math.sqrt(dx2 * dx2 + dy2 * dy2);

        if (c == 0) {
            return -1;
        }

        angle = Math.toDegrees(Math.acos((dx1 * dx2 + dy1 * dy2) / c));


        return angle;
    }
    /**
     * 设置label为可见或不可见
     */
    private void setLabelVisible(Label pointLabel, Label pointTextLabel, boolean flag) {
        pointLabel.setVisible(flag);
        pointTextLabel.setVisible(flag);
    }

    /**
     * 求两个直线的交点
     */
    private Point2D getLineearIntersectionPoint(Point2D one, Point2D two, Point2D three, Point2D four) {
        double x;
        double y;

        double x1 = one.getX(); double y1 = one.getY();
        double x2 = two.getX(); double y2 = two.getY();

        double x3 = three.getX(); double y3 = three.getY();
        double x4 = four.getX(); double y4 = four.getY();

        x = ((x1 * y2 - x2 * y1) * (x3 - x4) - (x3 * y4 - x4 * y3) * (x1 - x2)) / ((y2 - y1) * (x3 - x4) - (x1 - x2) * (y4 - y3));
        y = ((x1 * y2 - x2 * y1) * (y4 - y3) - (y2 - y1) * (x3 * y4 - x4 * y3)) / ((x1 - x2) * (y4 - y3) - (y2 - y1) * (x3 - x4));

        Point2D result = new Point2D(x, y);

        return result;
    }

    private Image getImage(File file) {
        try {
            return new Image((file).toURI().toURL().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}