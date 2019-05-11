package com.company.processing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class Regionprops {

    private String path;
    private int width;
    private int height;


    public void start() {

        BufferedImage image = null;
        File file = null;

        try {
            file = new File(path);
            image = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println(e);
        }

        width = image.getWidth();
        height = image.getHeight();

        int[][] values = new int[width][height];
        int[][] gray = new int[width][height];

        int red;
        int green;
        int blue;

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                values[j][i] = image.getRGB(j, i);
                red = (values[j][i]>>16)&0xff;
                green = (values[j][i]>>8)&0xff;
                blue = values[j][i]&0xff;
                gray[j][i] = (int) (0.299*red + 0.587*green + 0.114*blue);
            }

        calculate(gray);

        saveImage(file,image);

    }

    private void calculate(int[][] gray) {

        int[] subX = new int[256];
        int[] subY = new int[256];
        int[] iter = new int[256];
        for (int i = 0; i < 256; i++) {
            for (int x = 0; x <height; x++) {
                for (int y = 0; y < width; y++) {
                    if (i == gray[y][x]) {
                        subX[i] += y;
                        subY[i] += x;
                        iter[i]++;
                    }
                }
            }
        }

        int[][] res = new int[2][256];
        for (int i = 0; i < 256; i++) {
            if (iter[i] != 0) {
                res[0][i] = subX[i] / iter[i];
                res[1][i] = subY[i] / iter[i];
            }
        }

        try (PrintWriter printWriter = new PrintWriter(new File("result.txt"))) {

            for (int i = 0; i < 256; i++)
                printWriter.println(res[0][i] + " " + res[1][i]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(File file, BufferedImage image) {
        try
        {
            file = new File("/home/erykk/IdeaProjects/JavaImageProcessing/src/images/result2.jpg");
            ImageIO.write(image, "jpg", file);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public Regionprops(String path) {
        this.path = path;
    }
}
