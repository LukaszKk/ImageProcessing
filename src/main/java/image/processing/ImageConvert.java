package ImageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Contains basic image conversions
 */
public class ImageConvert
{
    protected final int black = new Color(0, 0, 0).getRGB();
    protected final int white = new Color(255, 255, 255).getRGB();

    /**
     * Converts RGB color into one 0-255 value
     *
     * @param image BufferedImage object
     * @param x     X axis coordinate
     * @param y     Y axis coordinate
     * @return double value
     */
    public double grayScale( BufferedImage image, int x, int y )
    {
        Color color = new Color(image.getRGB(x, y));
        double r = color.getRed() * 0.299;
        double g = color.getGreen() * 0.587;
        double b = color.getBlue() * 0.114;
        return r + g + b;
    }

    /**
     * Creates histogram to given image
     *
     * @param image BufferedImage object
     * @return int array
     */
    public int[] histogram( BufferedImage image )
    {
        int[] hist = new int[256];
        Arrays.fill(hist, 0);

        for( int i = 0; i < image.getHeight(); ++i )
            for( int j = 0; j < image.getWidth(); ++j )
                hist[(int) grayScale(image, j, i)]++;

        return hist;
    }

    /**
     * Makes the sum of all bins equal to 1
     *
     * @param hist histogram
     * @return double array
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

    /**
     * Checks if image is already binarized
     *
     * @param image BufferedImage object
     * @return true if is binarized, otherwise false
     */
    private boolean isBinarized( BufferedImage image )
    {
        for( int i = 0; i < image.getHeight(); i++ )
        {
            for( int j = 0; j < image.getWidth(); j++ )
            {
                int color = image.getRGB(j, i);
                if( color != black && color != white )
                    return false;
            }
        }

        return true;
    }

    /**
     * Changes pixels below given value into black and above into white color
     *
     * @param image      BufferedImage object
     * @param splitValue int value of range 0-255
     * @return new BufferedImage object with black and/or white pixels
     */
    public BufferedImage binarize( BufferedImage image, int splitValue ) throws Exception
    {
        if( splitValue < 0 || splitValue > 255 )
            throw new Exception("Wrong argument value");

        if( isBinarized(image) )
            return image;

        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for( int i = 0; i < image.getHeight(); ++i )
        {
            for( int j = 0; j < image.getWidth(); ++j )
            {
                double grayScale = grayScale(image, j, i);
                if( grayScale <= splitValue )
                    newImage.setRGB(j, i, black);
                else
                    newImage.setRGB(j, i, white);
            }
        }

        return newImage;
    }

    /**
     * Counts best image split value and call binarize method
     *
     * @param image BufferedImage object
     * @return new BufferedImage object with black and/or white pixels
     */
    public BufferedImage autoBinarize( BufferedImage image )
    {
        if( isBinarized(image) )
            return image;
        int tMax = maxEntropyIndex(image);
        BufferedImage newImage = null;
        try
        {
            newImage = binarize(image, tMax);
        } catch( Exception e )
        {
            e.printStackTrace();
        }

        return newImage;
    }

    /**
     * Counts best image split value by counting entropy for black and white parts of histogram
     *
     * @param image BufferedImage object
     * @return int value
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
            } else
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
            } else
                histWhite[t] = 0;
        }

        // Find histogram index with maximum entropy
        double jMax = histBlack[0] + histWhite[0];
        int tMax = 0;
        for( int t = 1; t < hist.length; ++t )
        {
            double j = histBlack[t] + histWhite[t];
            if( j > jMax )
            {
                jMax = j;
                tMax = t;
            }
        }

        return tMax;
    }

    /**
     * Convert RGB image to gray
     *
     * @param image BufferedImage object
     * @return new BufferedImage object each color set on the same value( 0-255)
     */
    public BufferedImage toGray( BufferedImage image )
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for( int i = 0; i < image.getWidth(); i++ )
        {
            for( int j = 0; j < image.getHeight(); j++ )
            {
                Color c = new Color(image.getRGB(i, j));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);
                Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);

                newImage.setRGB(i, j, newColor.getRGB());
            }
        }
        return newImage;
    }

    /**
     * calculating the differences between two images
     *
     * @param image1 BufferedImage object
     * @param image2 BufferedImage object
     * @return new BufferedImage object representation differences
     */
    public BufferedImage differentiation( BufferedImage image1, BufferedImage image2 )
    {
        BufferedImage newImage = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
        for( int i = 0; i < image1.getWidth(); i++ )
        {
            for( int j = 0; j < image1.getHeight(); j++ )
            {
                Color c1 = new Color(image1.getRGB(i, j));
                Color c2 = new Color(image2.getRGB(i, j));
                int col = Math.abs(c1.getRed() - c2.getRed());

                Color newColor = new Color(col, col, col);
                newImage.setRGB(i, j, newColor.getRGB());
            }
        }

        return newImage;
    }
}
