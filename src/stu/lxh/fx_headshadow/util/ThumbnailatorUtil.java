package stu.lxh.fx_headshadow.util;

import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

/**
 * Created by LXH on 2019/1/27.
 */
public class ThumbnailatorUtil {
    /**
     * 按比例缩放图片
     */
    public static void generateScale(double rate, String sourceImagePath, String targetImagePath) {
        try {
            Thumbnails.of(sourceImagePath).scale(rate).outputQuality(rate).toFile(targetImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按比例缩放图片
     */
    public static void generateScale(double rate, File source, File target) {
        try {
            Thumbnails.of(source).scale(rate).outputQuality(rate).toFile(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
