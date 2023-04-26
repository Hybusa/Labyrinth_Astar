package org.example.model;

import com.madgag.gif.fmsware.AnimatedGifEncoder;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

public class ImageProcessor {
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static void createGif(BufferedImage[] images, String outputPath, int delay, boolean loop){

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outputPath);
        encoder.setDelay(delay);
        encoder.setRepeat(loop ? 0 : -1);

        for (BufferedImage image : images) {
            encoder.addFrame(image);
        }

        encoder.finish();
    }
}
