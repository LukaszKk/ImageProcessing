package image.processing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Contains image transformation methods
 */
public class ImageAdvanceTransformation {

    /**
     * Uses ordfilt2 algorithm to filter given image.
     *
     * @param image
     * @param maskX
     * @param maskY
     * @param number
     * @return
     */
    public BufferedImage ordfilt2(BufferedImage image, Integer maskX, Integer maskY, Integer number) {

        if (maskX * maskX < number)
            System.out.println("Error");

        int width = image.getWidth();
        int height = image.getHeight();

        int[][] red = new int[width][height];
        int[][] green = new int[width][height];
        int[][] blue = new int[width][height];

        int[][] value = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                red[x][y] = r;
                green[x][y] = g;
                blue[x][y] = b;

                value[x][y] = image.getRGB(x, y);
            }
        }

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                red[x][y] = getPart(x, y, red, height, width, maskX, maskY, number);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                green[x][y] = getPart(x, y, green, height, width, maskX, maskY, number);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                blue[x][y] = getPart(x, y, blue, height, width, maskX, maskY, number);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = value[x][y];
                int a = (p >> 24) & 0xff;
                int r = red[x][y];
                int g = green[x][y];
                int b = blue[x][y];

                p = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, p);
            }
        }

        return image;
    }

    /**
     * Return each pixel after filtration by given mask and number.
     *
     * @param x
     * @param y
     * @param arr
     * @param height
     * @param width
     * @return
     */
    private int getPart(int x, int y, int[][] arr, int height, int width, Integer maskX, Integer maskY, Integer number) {
        int[] minArr = new int[maskX * maskY];
        int c = 0;

        int mask1 = maskX;
        int mask2 = maskY;

        for (int i = y; i < height; i++) {
            for (int j = x; j < width; j++) {
                if ((j + 1 < width) && (mask1 > 0)) {
                    mask1--;
                    minArr[c++] = arr[j][i];
                } else if (mask1-- > 0) {
                    minArr[c++] = arr[j][i];
                }
            }
            if (mask2-- <= 1)
                break;
            mask1 = maskX;
        }

        Arrays.sort(minArr);

        return minArr[number];
    }

    /**
     * Uses the algorithm to create a geodetic map.
     *
     * @param im
     */
    public BufferedImage geodistance(BufferedImage im, int x, int y) {

        int[][] SE = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                SE[i][j] = 1;
        Point a = new Point();

        a.x = im.getWidth()/2;
        a.y = im.getHeight()/2;

        BufferedImage map = null;

        try {
            map = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_BGR);

        } catch (Exception e) {
            System.out.println(e);
        }

        int width = im.getWidth();
        int height = im.getHeight();

        int[][] original = new int[width][height];
        int[][] dilationMap = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                original[j][i] = im.getRGB(j, i);
                if (original[j][i] == Color.WHITE.getRGB())
                    original[j][i] = 1;
                else
                    original[j][i] = 0;
            }
        }

        int[][] marker = new int[width][height];

        marker[a.x][a.y] = 1;

        int iter = 0;
        int condition = (height + width);

        while (iter < condition) {
            marker = dilatation(marker, dilationMap, iter % 255, width, height);

            marker = logicalAndForArrays(marker, original, width, height);

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
     *
     * @param marker
     * @param original
     * @return
     */
    private int[][] logicalAndForArrays(int[][] marker, int[][] original, int width, int height) {

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
     *
     * @param values
     * @param dilMap
     * @param iter
     * @return
     */
    private int[][] dilatation(int[][] values, int[][] dilMap, int iter, int width, int height) {
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
     * Uses the algorithm to find the center of mass for monochromatic values [0-255].
     *
     * @param im
     */
    public void regionprops(BufferedImage im) {

        int width = im.getWidth();
        int height = im.getHeight();

        int[][] values = new int[width][height];
        int[][] gray = new int[width][height];

        int red;
        int green;
        int blue;

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                values[j][i] = im.getRGB(j, i);
                red = (values[j][i] >> 16) & 0xff;
                green = (values[j][i] >> 8) & 0xff;
                blue = values[j][i] & 0xff;
                gray[j][i] = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
            }

        calculate(gray, width, height);
    }

    /**
     * Calculate and store center of mass for each monochromatic value [0-255].
     *
     * @param gray - gray array
     */
    private void calculate(int[][] gray, int width, int height) {

        int[] subX = new int[256];
        int[] subY = new int[256];
        int[] iter = new int[256];
        for (int i = 0; i < 256; i++) {
            for (int x = 0; x < height; x++) {
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
}
