package ImageProcessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


/*
 *
 */
public class ImageProcess
{
    private final int black = new Color(0,0,0).getRGB();
    private final int white = new Color(255,255,255).getRGB();

    /*
     *
     */
    public ImageProcess()
    {
    }

    /*
     *
     */
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

    /*
     *
     */
    public static void saveImage( BufferedImage image, String fileName )
    {
        try
        {
            ImageIO.write( image, "jpg", new File(fileName + ".jpg") );
        } catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    /*
     *
     */
    public double grayScale( BufferedImage image, int x, int y )
    {
        Color color = new Color(image.getRGB(x, y));
        double r = color.getRed() * 0.299;
        double g = color.getGreen() * 0.587;
        double b = color.getBlue() * 0.114;
        return r + g + b;
    }

    /*
     *
     */
    public int[] histogram( BufferedImage image )
    {
        int[] hist = new int[256];
        Arrays.fill( hist, 0 );

        for( int i = 0; i < image.getHeight(); ++i )
            for( int j = 0; j < image.getWidth(); ++j )
                hist[(int) grayScale(image, j, i)]++;

        return hist;
    }

    /*
     *
     */
    public BufferedImage binarize( BufferedImage image, int splitValue )
    {
        if( isBinarized(image) )
            return image;

        BufferedImage newImage = new BufferedImage( image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB );

        for( int i = 0; i < image.getHeight(); ++i )
        {
            for( int j = 0; j < image.getWidth(); ++j )
            {
                double grayScale = grayScale( image, j, i );
                if( grayScale <= splitValue )
                    newImage.setRGB(j, i, black);
                else
                    newImage.setRGB(j, i, white);
            }
        }

        return newImage;
    }

    /*
     *
     */
    public BufferedImage autoBinarize( BufferedImage image )
    {
        if( isBinarized(image) )
            return image;
        int tMax = maxEntropyIndex(image);
        return binarize(image, tMax);
    }

    /*
     * make the sum of all bins equal to 1
     */
    public double[] normalizeHistogram( int[] hist )
    {
        double sum = 0;
        for( int value : hist ) sum += value;
        if( sum == 0 )
            throw new IllegalArgumentException("Empty histogram: sum of all bins is zero.");

        double[] normalizedHist = new double[hist.length];
        for( int i = 0; i < hist.length; i++ )
            normalizedHist[i] = hist[i] / sum;

        return normalizedHist;
    }

    /*
     *
     */
    private int maxEntropyIndex( BufferedImage image )
    {
        int[] hist = histogram(image);
        double[] normalizedHist = normalizeHistogram(hist);

        double[] pT = new double[hist.length];
        pT[0] = normalizedHist[0];
        for( int i = 1; i < hist.length; ++i )
            pT[i] = pT[i - 1] + normalizedHist[i];

        // Entropy for black and white parts of the histogram
        final double epsilon = Double.MIN_VALUE;
        double[] histBlack = new double[hist.length];
        double[] histWhite = new double[hist.length];
        for( int t = 0; t < hist.length; ++t )
        {
            // Black entropy
            if( pT[t] > epsilon )
            {
                double max = 0;
                for( int i = 0; i <= t; ++i )
                    if( normalizedHist[i] > epsilon )
                        max -= normalizedHist[i] / pT[t] * Math.log(normalizedHist[i] / pT[t]);

                histBlack[t] = max;
            }
            else
                histBlack[t] = 0;

            // White  entropy
            double pTWhite = 1 - pT[t];
            if( pTWhite > epsilon )
            {
                double max = 0;
                for( int i = t + 1; i < hist.length; ++i )
                    if( normalizedHist[i] > epsilon )
                        max -= normalizedHist[i] / pTWhite * Math.log(normalizedHist[i] / pTWhite);

                histWhite[t] = max;
            }
            else
                histWhite[t] = 0;
        }

        // Find histogram index with maximum entropy
        double jMax = histBlack[0] + histWhite[0];
        int tMax = 0;
        for( int t = 1; t < hist.length; ++t )
        {
            double j = histBlack[t] + histWhite[t];
            if (j > jMax)
            {
                jMax = j;
                tMax = t;
            }
        }

        return tMax;
    }

    /*
     *
     */
    private boolean isBinarized( BufferedImage image )
    {
        for( int i = 0; i < image.getHeight(); i++ )
        {
            for( int j = 0; j < image.getWidth(); j++ )
            {
                int color = image.getRGB( j, i );
                if( color != black && color != white )
                    return false;
            }
        }

        return true;
    }

    /*
     *
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

    /*
     *
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

    /*
     *
     */
    public BufferedImage closing( BufferedImage image, int radius )
    {
        BufferedImage dilatation = dilatation(image, radius);
        return erosion(dilatation, radius);
    }

    /*
     *
     */
    public BufferedImage opening( BufferedImage image, int radius )
    {
        BufferedImage erosion = erosion(image, radius);
        return dilatation(erosion, radius);
    }

    /*
     *
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
