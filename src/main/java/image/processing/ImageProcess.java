package image.processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Contains methods that can reduce code amount
 */
public class ImageProcess
{
    /**
     * @param filePath path to file
     * @return BufferedImage object
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

    /**
     * Saves image into file given by its name
     * file is created or overwrite
     * @param image BufferedImage object to save
     * @param fileName name of the output file
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
}
