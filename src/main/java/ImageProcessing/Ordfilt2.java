package ImageProcessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class Ordfilt2 {

    private String mask;
    private Integer number;
    private Integer maskX;
    private Integer maskY;

    private int[][] red;
    private int[][] green;
    private int[][] blue;

    public BufferedImage ordfilt2(BufferedImage image, Integer maskX, Integer maskY, Integer number) {

        setMaskX(maskX);
        setMaskY(maskY);
        setNumber(number);
        if (maskX*maskX < number)
            System.out.println("Error");



        int width = image.getWidth();
        int height = image.getHeight();

        red = new int[width][height];
        green = new int[width][height];
        blue = new int[width][height];

        int[][] value = new int[width][height];

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int p = image.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                red[x][y] = r;
                green[x][y] = g;
                blue[x][y] = b;

                value[x][y] = image.getRGB(x,y);
            }
        }

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                red[x][y] = getPart(x,y,red,height,width);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                green[x][y] = getPart(x,y,green,height,width);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                blue[x][y] = getPart(x,y,blue,height,width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = value[x][y];
                int a = (p>>24)&0xff;
                int r = red[x][y];
                int g = green[x][y];
                int b = blue[x][y];

                p = (a<<24) | (r<<16) | (g<<8) | b;
                image.setRGB(x, y, p);
            }
        }


        return image;
    }

    public static BufferedImage bufferedImage( String filePath )
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(filePath));
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return image;
    }

    public int getPart(int x, int y, int[][] arr, int height, int width) {
        int[][] copy = arr;
        int[] minArr = new int [maskX*maskY];
        int c = 0;

        int mask1 = getMaskX();
        int mask2 = getMaskY();

        for (int i = y; i < height; i++) {
            for (int j = x; j < width; j++) {
                if ((j+1 < width) && (mask1 > 0)) {
                    mask1--;
                    minArr[c++] = copy[j][i];
                } else if (mask1-- > 0) {
                    minArr[c++] = copy[j][i];
                }
            }
            if (mask2-- <= 1)
                break;
            mask1 = getMaskX();
        }

        Arrays.sort(minArr);

        return minArr[number];
    }

    public Ordfilt2() {
    }

    public int[][] getRed() {
        return red;
    }

    public void setRed(int[][] red) {
        this.red = red;
    }

    public int[][] getGreen() {
        return green;
    }

    public void setGreen(int[][] green) {
        this.green = green;
    }

    public int[][] getBlue() {
        return blue;
    }

    public void setBlue(int[][] blue) {
        this.blue = blue;
    }

    public Integer getMaskX() {
        return maskX;
    }

    public void setMaskX(Integer maskX) {
        this.maskX = maskX;
    }

    public Integer getMaskY() {
        return maskY;
    }

    public void setMaskY(Integer maskY) {
        this.maskY = maskY;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
