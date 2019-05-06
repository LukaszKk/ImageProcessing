package ImageProcessing;


import java.awt.image.BufferedImage;

public class App
{
    public static void main( String[] args )
    {
        ImageProcess im = new ImageProcess();
        BufferedImage image = ImageProcess.bufferedImage( "/home/erykk/IdeaProjects/ImageProcessing/src/images/mono2.png" );

        BufferedImage bin = im.erosion( image, 5 );
        ImageProcess.saveImage( bin, "image" );


        Ordfilt2 filt = new Ordfilt2();
        BufferedImage image1 = Ordfilt2.bufferedImage("/home/erykk/IdeaProjects/ImageProcessing/src/images/cameraman.jpg");
        BufferedImage bufferedImage = filt.ordfilt2(image1,4,3,1);
        ImageProcess.saveImage(bufferedImage,"res2");
    }
}
