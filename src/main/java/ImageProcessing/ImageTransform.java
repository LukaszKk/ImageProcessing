package ImageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Contains image transformation methods
 */
public class ImageTransform extends ImageConvert
{
    /**
     * Counts dilation with disk element
     * @param image BufferedImage object
     * @param radius disk radius
     * @return new BufferedImage object
     */
    public BufferedImage dilatation( BufferedImage image, int radius )
    {
        BufferedImage binImage = autoBinarize(image);
        BufferedImage newImage = new BufferedImage( binImage.getWidth(), binImage.getHeight(), BufferedImage.TYPE_INT_RGB );

        for( int i = radius; i < binImage.getHeight()-radius; i++ )
        {
            for( int j = radius; j < binImage.getWidth()-radius; j++ )
            {
                if( grayScale(binImage, j, i) == 0 )
                {
                    if( inCircle(binImage, radius, j, i, 255) )
                        newImage.setRGB(j, i, white);
                    else
                        newImage.setRGB(j, i, black);
                }
                else
                    newImage.setRGB(j, i, white);
            }
        }

        return newImage;
    }

    /**
     * Counts erosion with disk element
     * @param image BufferedImage object
     * @param radius disk radius
     * @return new BufferedImage object
     */
    public BufferedImage erosion( BufferedImage image, int radius )
    {
        BufferedImage binImage = autoBinarize(image);
        BufferedImage newImage = new BufferedImage( binImage.getWidth(), binImage.getHeight(), BufferedImage.TYPE_INT_RGB );

        int black = new Color(0,0,0).getRGB();
        int white = new Color(255,255,255).getRGB();
        for( int i = radius; i < binImage.getHeight()-radius; i++ )
        {
            for( int j = radius; j < binImage.getWidth()-radius; j++ )
            {
                if( grayScale(binImage, j, i) == 255 )
                {
                    if( inCircle(binImage, radius, j, i, 0) )
                        newImage.setRGB(j, i, black);
                    else
                        newImage.setRGB(j, i, white );
                }
                else
                    newImage.setRGB(j, i, black);
            }
        }

        return newImage;
    }

    /**
     * Counts morphological closing with disk element
     * @param image BufferedImage object
     * @param radius disk radius
     * @return new BufferedImage object
     */
    public BufferedImage closing( BufferedImage image, int radius )
    {
        BufferedImage dilatation = dilatation(image, radius);
        return erosion(dilatation, radius);
    }

    /**
     * Counts morphological opening with disk element
     * @param image BufferedImage object
     * @param radius disk radius
     * @return new BufferedImage object
     */
    public BufferedImage opening( BufferedImage image, int radius )
    {
        BufferedImage erosion = erosion(image, radius);
        return dilatation(erosion, radius);
    }

    /**
     * Checks if given point is inside disk
     * @param image BufferedImage object
     * @param radius disk radius
     * @param x X axis coordinate of the point to check
     * @param y Y axis coordinate of the point to check
     * @param val take 0 or 255 value
     * @return true if point is inside, otherwise false
     */
    private boolean inCircle( BufferedImage image, int radius, int x, int y, int val )
    {
        for (int i = y-radius; i < y+radius; i++)
        {
            for (int j = x; (j-x)*(j-x) + (i-y)*(i-y) <= radius*radius; j--)
            {
                //in the circle
                if( grayScale(image, j, i) == val )
                    return true;
            }
            for (int j = x+1; (j-x)*(j-x) + (i-y)*(i-y) <= radius*radius; j++)
            {
                //in the circle
                if( grayScale(image, j, i) == val )
                    return true;
            }
        }

        return false;
    }
}
