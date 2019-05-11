package image.processing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Contains methods that can reduce code amount
 */
public class ImageProcess
{
    /**
     * Creates BufferedImage objects and returns it
     *
     * @param filePath path to file
     * @return BufferedImage object
     */
    public static BufferedImage bufferedImage( String filePath )
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(filePath));
        } catch( IOException e )
        {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Saves image with extension jpg into file given by its name
     * file is created or overwrite
     *
     * @param image    BufferedImage object to save
     * @param fileName name of the output file
     */
    public static void saveImage( BufferedImage image, String fileName )
    {
        try
        {
            ImageIO.write(image, "jpg", new File(fileName + ".jpg"));
        } catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Shows given image in new JFrame located in the screen center
     *
     * @param fileName name of the image to be shown
     */
    public static void showImage( String fileName )
    {
        JFrame frame = new JFrame();
        final BufferedImage image = bufferedImage( fileName );
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        if( image.getWidth() < screenWidth && image.getHeight() < screenHeight )
        {
            frame.setSize(image.getWidth() + 16, image.getHeight() + 39);
            frame.setLocation(screenWidth / 2 - image.getWidth() / 2 - 8,
                    screenHeight / 2 - image.getHeight() / 2 - 19);
        }
        else
        {
            frame.setSize( screenWidth, screenHeight );
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        JComponent component = new JComponent()
        {
            @Override
            protected void paintComponent( Graphics g )
            {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, this);
            }
        };

        frame.add(component);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
