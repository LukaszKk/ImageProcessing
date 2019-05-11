package image.processing;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GeoDistance {

    private int width;
    private int height;
    private Point A;
    private Point B;
    private int[][] SE;
    private int[][] marker;

    /**
     * Uses the algorithm to create a geodetic map.
     *
     * @param im
     */
    public BufferedImage geodistance(BufferedImage im) {
        info();

        BufferedImage image = im;

        BufferedImage map = null;

        try {
            map = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);

        } catch (Exception e) {
            System.out.println(e);
        }

        width = image.getWidth();
        height = image.getHeight();

        int[][] original = new int[width][height];
        int[][] dilationMap = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                original[j][i] = image.getRGB(j, i);
                if (original[j][i] == Color.WHITE.getRGB())
                    original[j][i] = 1;
                else
                    original[j][i] = 0;
            }
        }

        marker = new int[width][height];

        marker[A.x][A.y] = 1;

        int iter = 0;
        int condition = (height + width) * 2;

        while (iter < condition) {
            marker = dilatation(marker, dilationMap, iter % 255);

            marker = logicalAndForArrays(marker, original);

            iter++;
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map.setRGB(j, i, dilationMap[j][i]);
            }
        }

        return map;
    }

    /**
     * Logical operation - AND.
     * @param marker
     * @param original
     * @return
     */
    private int[][] logicalAndForArrays(int[][] marker, int[][] original) {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((marker[j][i] == original[j][i]) && original[j][i] == 1)
                    marker[j][i] = 1;
                else if ((marker[j][i] == 1) && original[j][i] == 0)
                    marker[j][i] = 3;
            }
        }
        return marker;
    }

    /**
     * Carries out dilatation algorithm.
     * @param values
     * @param dilMap
     * @param iter
     * @return
     */
    private int[][] dilatation(int[][] values, int[][] dilMap, int iter) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (values[j][i] == 1) {
                    if (j > 0 && values[j - 1][i] == 0) values[j - 1][i] = 2;
                    if (i > 0 && values[j][i - 1] == 0) values[j][i - 1] = 2;
                    if (j + 1 < width && values[j + 1][i] == 0) values[j + 1][i] = 2;
                    if (i + 1 < height && values[j][i + 1] == 0) values[j][i + 1] = 2;
                }
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (values[j][i] == 2)
                    dilMap[j][i] = iter;
            }
        }


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (values[j][i] == 2)
                    values[j][i] = 1;
            }
        }
        return values;
    }

    /**
     *Read information about starting point of calculation.
     */
    private void info() {
        System.out.println("NastedPoint A");
        System.out.print("x = ");
        Scanner sc = new Scanner(System.in);
        A.x = sc.nextInt();
        System.out.print("y = ");
        A.y = sc.nextInt();
    }

    public GeoDistance() {
        SE = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                SE[i][j] = 1;
        A = new Point();
    }
}
