import image.processing.ImageProcess;
import image.processing.ImageTransform;
import image.processing.Reconstruction;

import java.awt.image.BufferedImage;

public class Main
{
    public static void main( String[] args )
    {
        ImageTransform im = new ImageTransform();
        Reconstruction re = new Reconstruction();
        BufferedImage background = ImageProcess.bufferedImage("src\\images\\background.png");
        BufferedImage image = ImageProcess.bufferedImage("src\\images\\image.png");

        try
        {
            /*
            BufferedImage image1 = im.toGray(background);
            ImageProcess.saveImage(image1, "src\\images\\result\\image1");        //background to gray

            BufferedImage image2 = im.toGray(image);
            ImageProcess.saveImage(image2, "src\\images\\result\\image2");        //image to gray

            BufferedImage image3 = im.differentiation(image1, image2);
            ImageProcess.saveImage(image3, "src\\images\\result\\image3");        //differentation

            BufferedImage image4 = im.binarize(image3, 40);
            ImageProcess.saveImage(image4, "src\\images\\result\\image4");        //binarization

            BufferedImage image5 = im.closing(image4, 5);
            ImageProcess.saveImage(image5, "src\\images\\result\\image5");        //closing

            BufferedImage image6 = im.opening(image5, 3);
            ImageProcess.saveImage(image6, "src\\images\\result\\image6");        //opening

            BufferedImage image7 = im.erosion(image6, 10);
            ImageProcess.saveImage(image7, "src\\images\\result\\image7");        //erosion

            BufferedImage image8 = re.reconstruct(image7, image6);
            ImageProcess.saveImage(image8, "src\\images\\result\\image8");        //reconstruction

            ImageProcess.showImage("src\\images\\result\\image8.jpg");
            */

            BufferedImage image9 = im.toGray(background);
            BufferedImage image10 = im.erosion(image9, 5);
            ImageProcess.saveImage(image10, "src\\images\\result\\image9");
            ImageProcess.showImage("src\\images\\result\\image9.jpg");

        } catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
