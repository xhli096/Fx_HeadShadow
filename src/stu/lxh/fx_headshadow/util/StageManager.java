package stu.lxh.fx_headshadow.util;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LXH on 2019/1/14.
 */
public class StageManager {
    private static Map<String, Stage> stageMap;
    private static Map<String, Object> controllerMap;

    static {
        stageMap = new HashMap<>();
        controllerMap = new HashMap<>();
    }

    public static void addStage(String key, Stage stage) {
        stageMap.put(key, stage);
    }

    public static Stage getStage(String key) {
        return stageMap.get(key);
    }

    public static void addController(String key, Object controller) {
        controllerMap.put(key, controller);
    }

    public static Object getController(String key) {
        return controllerMap.get(key);
    }
}
