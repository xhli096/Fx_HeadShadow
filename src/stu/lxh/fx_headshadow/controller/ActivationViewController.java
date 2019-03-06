package stu.lxh.fx_headshadow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

/**
 * Created by LXH on 2019/3/6.
 */
public class ActivationViewController {
    @FXML
    private Hyperlink getLicenseHyperlink;

    @FXML
    public void initialize() {
        init();
        dealAction();
    }

    private void dealAction() {
        getLicenseHyperlink.setOnMouseClicked(event -> {
            // 生成license，填入默认位置
        });
    }

    private void init() {

    }
}
