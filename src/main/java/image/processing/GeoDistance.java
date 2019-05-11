package com.company.processing;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GeoDistance {

    private String path;
    private int width;
    private int height;
    private Point A;
    private Point B;
    private int[][] SE;
    private int[][] marker;

    public void start() {
        info();

        BufferedImage image = null;
        File file = null;

        BufferedImage map = null;

        try {
            file = new File(path);
            image = ImageIO.read(file);
            map = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_BGR);

        } catch (Exception e) {
            System.out.println(e);
        }

        width = image.getWidth();
        height = image.getHeight();

        int[][] original = new int[width][height];
        int[][] dilationMap = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                original[j][i] = image.getRGB(j,i);
                if (original[j][i] == Color.WHITE.getRGB())
                    original[j][i] = 1;
                else
                    original[j][i] = 0;
            }
        }

        marker = new int[width][height];

        marker[A.x][A.y] = 1;

        int iter = 0;
        int condition = (height + width)*2;

        while (iter < condition) {
            marker = dilatation(marker,dilationMap,iter%255);

            marker = logicalAndForArrays(marker,original);

            iter++;
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map.setRGB(j,i,dilationMap[j][i]);
            }
        }

        try {
            File f = new File("/home/erykk/IdeaProjects/JavaImageProcessing/src/images/res_1.jpg");
            ImageIO.write(map, "jpg", f);
        } catch (IOException e) {
            System.out.println(e);
        }
    }



    private int[][] logicalAndForArrays(int[][] marker, int[][] original) {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((marker[j][i] == original[j][i]) && original[j][i] == 1 )
                    marker[j][i] = 1;
                else if ((marker[j][i] == 1) && original[j][i] == 0 )
                    marker[j][i] = 3;
            }
        }
        return marker;
    }

    private int[][] dilatation(int[][] values, int[][] dilMap, int iter) {
        for (int i = 0; i < height; i++ ) {
            for (int j = 0; j < width; j++ ) {
                if (values[j][i] == 1) {
                    if (j > 0 && values[j - 1][i] == 0) values[j - 1][i] = 2;
                    if (i > 0 && values[j][i - 1] == 0) values[j][i - 1] = 2;
                    if (j + 1 < width && values[j + 1][i] == 0) values[j + 1][i] = 2;
                    if (i + 1 < height && values[j][i + 1] == 0) values[j][i + 1] = 2;
                }
            }
        }

        for (int i = 0; i < height; i++ ) {
            for (int j = 0; j < width; j++ ) {
                if (values[j][i] == 2)
                    dilMap[j][i] = iter;
            }
        }


                for (int i = 0; i < height; i++) {
            for (int j = 0; j< width; j++) {
                if (values[j][i] == 2)
                    values[j][i] = 1;
            }
        }
        return values;
    }

    private void info() {
        System.out.println("NastedPoint A");
        System.out.print("x = ");
        Scanner sc = new Scanner(System.in);
        A.x = sc.nextInt();
        System.out.print("y = ");
        A.y = sc.nextInt();
    }

    public void saveImage(File file, BufferedImage image, String name) {
        try
        {
            file = new File("/home/erykk/IdeaProjects/JavaImageProcessing/src/images/" + name + ".bmp");
            ImageIO.write(image, "bmp", file);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public void saveJpgImage(File file, BufferedImage image, String name) {
        try
        {
            file = new File("/home/erykk/IdeaProjects/JavaImageProcessing/src/images/" + name + ".jpg");
            ImageIO.write(image, "jpg", file);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public GeoDistance(String path) {
        this.path = path;
        SE = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                SE[i][j] = 1;
        A = new Point();
    }
}
