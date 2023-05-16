package org.example.model;

import com.madgag.gif.fmsware.AnimatedGifEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageProcessor {
    public static BufferedImage deepCopy(BufferedImage bi) {
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

    public static BufferedImage generateMazeImage(int[][] maze)  {
        BufferedImage mazeImage = new BufferedImage(maze.length,maze[0].length,BufferedImage.TYPE_INT_RGB);

        int colorBlack = Color.BLACK.getRGB();
        int colorRed = Color.RED.getRGB();
        int colorBlue = Color.BLUE.getRGB();
        int colorWhite = Color.WHITE.getRGB();


        for(int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                switch (maze[i][j]) {
                    case 1:
                        mazeImage.setRGB(i, j, colorBlack);
                        break;
                    case 2:
                        mazeImage.setRGB(i, j, colorBlue);
                        break;
                    case 3:
                        mazeImage.setRGB(i, j, colorRed);
                        break;
                    case 0:
                        mazeImage.setRGB(i, j, colorWhite);
                        break;
                    default:
                        throw new RuntimeException("Smth went wrong in generateMazeImgage");
                }
            }
        }
        return mazeImage;
    }
}
